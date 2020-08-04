/**
 * MVVM 类
 */
class Vm {
  /**
   * MVVM 类构造器
   *
   * @param   {object}  options  选项
   *
   * @return  {Vm}
   */
  constructor(options) {
    this.el =
      options.el.nodeType === 1
        ? options.el
        : document.querySelector(options.el);
    // this.el = options.el;
    this.data = this.observable(options.data);
    this.options = options;
    new Compile(this.el, this).compile();
  }

  /**
   * 设置响应式
   *
   * @param   {any}  data
   * @param   {Function[]}  callback
   *
   * @return  {any}
   */
  observable(data) {
    let _this = this;
    if (typeof data === "object") {
      // 递归劫持数据
      for (const key in data) {
        data[key] = _this.observable(data[key]);
      }
      // 创建该数据的消息分发器
      let dispatcher = new Dispatcher(_this);
      return new Proxy(data, {
        get(obj, prop) {
          // 一个临时指向 watcher 的指针，当有新的 watcher 创建时，会首次获取绑定数据，此时就会将 watcher 绑定到 dispatcher
          if (Dispatcher.target) {
            dispatcher.bind(prop);
          }
          if (Dispatcher.targetFor) {
            dispatcher.bindFor();
          }
          return obj[prop];
        },
        set(obj, prop, value) {
          if (obj[prop] !== value) {
            obj[prop] = _this.observable(value);
            // 通知数据更新
            dispatcher.notify();
          }
          // Hook length 属性以监听数组的增加
          if (prop === "length" && dispatcher.for) {
            dispatcher.notifyFor();
          }
          return true;
        },
        deleteProperty(obj, prod) {
          delete obj[prod];
          dispatcher.notify();
          return true;
        }
      });
    }
    return data;
  }

  watch(key, callback) {
    new Watcher(key, callback, this);
  }

  /**
   * 通过索引获取值
   *
   * @param   {string}  key  索引
   *
   * @return  {any}
   */
  get(key) {
    let exps = key.split(".");
    let obj = this.data;
    for (const exp of exps) {
      if (typeof obj !== "object") {
        return undefined;
      }
      obj = obj[exp];
    }
    return obj;
  }

  /**
   * 通过索引设置值
   *
   * @param   {string}  key  索引
   * @param   {any} value 值
   *
   * @return  {void}
   */
  set(key, value) {
    let exps = key.split(".");
    let obj = this.data;
    let last = exps.pop();
    for (const exp of exps) {
      if (typeof obj !== "object") {
        return false;
      }
      obj = obj[exp];
    }
    if (typeof obj !== "object") {
      return false;
    }
    obj[last] = value;
    return true;
  }
}

/**
 * 监听器类
 */
class Watcher {
  /**
   * @param   {Function}  callback  数据更新时的回调函数
   * @param   {Vm}  vm  MVVM 实例
   *
   * @return {Watcher}
   */
  constructor(key, callback, vm) {
    this.key = key;
    this.callback = callback;
    this.vm = vm;
    this.value = this.bind(key);
  }

  /**
   * 当有数据更新时 dispatcher 会调用该方法，该方法会调用创建 watcher 时传入的回调函数
   *
   * @param   {any}  value     新的数据
   * @param   {any}  oldValue  旧的数据
   *
   * @return  {void}
   */
  update() {
    let old = this.value;
    this.value = this.vm.get(this.key);
    this.callback(this.value, old);
  }

  /**
   * 首次获取数据，也可以认为是将数据绑定到 watcher 上
   * 首次获取数据的时候同时会将 watcher 绑定到 dispatcher
   *
   * @param   {string}  key  绑定的数据的索引
   *
   * @return  {any}
   */
  bind(key) {
    Dispatcher.target = this;
    let value = this.vm.get(key);
    Dispatcher.target = null;
    return value;
  }
}

/**
 * 消息分发器类
 */
class Dispatcher {
  // 一个临时指向 watcher 的指针，当有新的 watcher 创建时，会首次获取绑定数据，此时就会将 watcher 绑定到 dispatcher
  static target = null;
  static targetFor = null;

  /**
   * 构造器
   *
   * @param   {Vm}  vm  MVVM 实例
   *
   * @return  {Dispatcher}
   */
  constructor(vm) {
    this.watchers = {};
    this.vm = vm;
    this.for = null;
  }

  /**
   * 添加监听器
   *
   * @param   {Watcher}  watcher  监听器
   *
   * @return  {void}
   */
  add(key, watcher) {
    this.watchers[key] = watcher;
  }

  /**
   * 删除监听器
   *
   * @param   {Watcher}  watcher  监听器
   *
   * @return  {void}
   */
  remove(key) {
    delete this.watchers[key];
  }

  /**
   * 通知更新
   *
   * @param   {any}  value     新的数据
   * @param   {any}  oldValue  旧的数据
   *
   * @return  {void}
   */
  notify() {
    Object.values(this.watchers).forEach(watcher => watcher.update());
  }

  /**
   * 将 watcher 绑定到 dispatcher
   *
   * @return  {void}
   */
  bind(key) {
    this.add(key, Dispatcher.target);
  }

  bindFor() {
    this.for = Dispatcher.targetFor;
  }

  notifyFor() {
    this.for.update();
  }
}

class Compile {
  /**
   * 编译类
   *
   * @param   {Element}  el
   * @param   {VM}  vm
   *
   * @return  {Compile}
   */
  constructor(el, vm) {
    this.el = el;
    this.vm = vm;
    this.types = ["text", "html", "model", "bind", "class", "id", "show", "if"];
  }

  compile() {
    // this.el
    //   .querySelectorAll(":not([x-for])")
    //   .forEach(item => this.compileItem(item));
    this.compileItem(this.el);
    this.el.querySelectorAll("[x-for]").forEach(item => {
      this.compileFor(item);
    });
  }

  /**
   * 编译子元素
   *
   * @param   {Element}  node
   * @param   {string}  before
   *
   * @return  {void}
   */
  compileItem(node, before = "") {
    this.types.forEach(type => {
      node.querySelectorAll(`[x-${type}]`).forEach(item => {
        this[
          `compile${type.slice(0, 1).toUpperCase() +
            type.slice(1).toLowerCase()}`
        ](item, before);
      });
    });
  }

  /**
   * 编译 Text
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileText(node, before = "") {
    node.textContent = new Watcher(
      before + node.getAttribute("x-text"),
      value => {
        node.textContent = value;
      },
      this.vm
    ).value;
  }

  /**
   * 编译 HTML
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileHtml(node, before = "") {
    node.textContent = new Watcher(
      before + node.getAttribute("x-html"),
      value => {
        node.innerHTML = value;
      },
      this.vm
    ).value;
  }

  /**
   * 编译 Model
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileModel(node, before = "") {
    let key = node.getAttribute("x-model");
    node.value = new Watcher(
      before + key,
      value => {
        node.value = value;
      },
      this.vm
    ).value;
    node.addEventListener("change", e => {
      this.vm.set(key, node.value);
    });
  }

  /**
   * 编译 Bind
   * TODO: Bind 多个值
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileBind(node, before = "") {
    let [attr, key] = node.getAttribute("x-bind").split(":");
    node.setAttribute(
      attr,
      new Watcher(
        before + key,
        value => {
          node.setAttribute(attr, value);
        },
        this.vm
      ).value
    );
  }

  /**
   * 编译 Class
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileClass(node, before = "") {
    node.className = new Watcher(
      before + node.getAttribute("x-class"),
      value => {
        node.className = value;
      },
      this.vm
    ).value;
  }

  /**
   * 编译 Id
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileId(node, before = "") {
    node.id = new Watcher(
      before + node.getAttribute("x-id"),
      value => {
        node.id = value;
      },
      this.vm
    ).value;
  }

  /**
   * 编译 Show
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileShow(node, before = "") {
    node.style.display = new Watcher(
      before + node.getAttribute("x-show"),
      value => {
        node.style.display = value ? "" : "none";
      },
      this.vm
    ).value
      ? ""
      : "none";
  }

  /**
   * 编译 If
   *
   * @param   {Element}  node
   * @param   {string}   before
   *
   * @return  {void}
   */
  compileIf(node, before = "") {
    let [key, target] = node.getAttribute("x-if").split(/\s+={2,3}\s+/);
    node.style.display =
      new Watcher(
        before + key,
        value => {
          node.style.display = value == target ? "" : "none";
        },
        this.vm
      ).value == target
        ? ""
        : "none";
  }

  /**
   * 编译 For
   *
   * @param    {Element}  node
   *
   * @return {void}
   */
  compileFor(node) {
    let [alias, key] = node.getAttribute("x-for").split(/\s+in\s+/);
    this.types.forEach(type => {
      node.querySelectorAll(`[x-${type}]`).forEach(item => {
        item.setAttribute(
          `x-${type}`,
          item.getAttribute(`x-${type}`).replace(new RegExp(`^${alias}.`), "")
        );
      });
    });
    // 拷贝
    let copy = node.cloneNode(true);
    let renderList = (list, watcher) => {
      Dispatcher.targetFor = watcher;
      node.innerHTML = "";
      let nodes = [];
      for (const k in list) {
        nodes[k] = copy.cloneNode(true);
        this.compileItem(nodes[k], key + "." + k + ".");
      }
      for (const n of nodes) {
        node.append(...n.children);
      }
      Dispatcher.targetFor = null;
    };
    let watcher = new Watcher(
      key,
      value => {
        renderList(value, watcher);
      },
      this.vm
    );
    let list = watcher.value;
    renderList(list, watcher);
  }
}

let vm = new Vm({
  el: "#app",
  data: {
    titleText: "title",
    titleHtml: "title",
    inputModel: "input",
    textareaModel: "textarea",
    selectModel: true,
    imgBind: "https://ixk.me/assets/img/1.jpg",
    ulFor: [
      {
        class: "class-1",
        value: 1
      },
      {
        class: "class-2",
        value: 2
      }
    ],
    divShow: true,
    btnIf: 1
  }
});
