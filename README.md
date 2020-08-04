# Code

存放非项目的代码

## 算法

- [二分查找](https://github.com/syfxlin/code/tree/master/algorithm/src/binarySearchDemo)
- [Boyer-Moore 字符串搜索](https://github.com/syfxlin/code/tree/master/algorithm/src/boyerMooreDemo)
- [桶排序](https://github.com/syfxlin/code/tree/master/algorithm/src/bucketSortDemo)
- [简单计算器](https://github.com/syfxlin/code/tree/master/algorithm/src/calculatorDemo)
- [归并排序](https://github.com/syfxlin/code/tree/master/algorithm/src/mergeSortDemo)
- [快速排序](https://github.com/syfxlin/code/tree/master/algorithm/src/quickSortDemo)
- [希尔排序](https://github.com/syfxlin/code/tree/master/algorithm/src/shellSortDemo)

## 数据结构

- [数组列表](https://github.com/syfxlin/code/tree/master/data-struct/src/MyArrayListDemo)
  - 二分查找
- [图(old)](https://github.com/syfxlin/code/tree/master/data-struct/src/MyGraphDemo)
- [双向链表](https://github.com/syfxlin/code/tree/master/data-struct/src/MyLinkedListDemo)
  - 栈/队列
  - 冒泡排序/快速排序
  - 链表实现的优先队列
- [有向/无向图(邻接表)](https://github.com/syfxlin/code/tree/master/data-struct/src/MyNewGraphDemo)
  - 转换成邻接矩阵
  - 获取顶点的度
  - 获取孤立点/悬挂点
  - 获取联通区域列表
  - DFS/BFS
  - 判断是否联通
  - Kruskal 最小生成树
  - Dijkstra 最短路径
- [单向链表](https://github.com/syfxlin/code/tree/master/data-struct/src/MySingleLinkedListDemo)
  - 同双向链表
- [跳表](https://github.com/syfxlin/code/tree/master/data-struct/src/MySkipListDemo)
- [二叉树(链表)](https://github.com/syfxlin/code/tree/master/data-struct/src/MyTreeDemo)
  - 前中后遍历和层次遍历
  - 链表二叉树和数组二叉树互转
  - 广义表和二叉树互转
  - 叶节点列表/遍历
  - 是否是完全二叉树
  - BST 二叉搜索树(添加/搜索/删除)
  - 旋转二叉树(左旋/右旋)
  - Heap 堆
  - 前序遍历构建完全二叉树
- [字典树](https://github.com/syfxlin/code/tree/master/data-struct/src/MyTrieTreeDemo)

## 设计模式

> 由于 PHP 有一些局限性，无法完全的展示设计模式，比如单例的多种实现线程安全等，所以我打算用 Java 重写一下这些设计模式

- [单例模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Singleton) `Singleton`
- [工厂模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Factory) `Factory`
  - [简单/静态工厂模式](https://github.com/syfxlin/code/blob/master/design-pattern-php/Factory/SimpleFactory.php) `SimpleFactory`
  - [工厂方法模式](https://github.com/syfxlin/code/blob/master/design-pattern-php/Factory/FactoryMethod.php) `FactoryMethod`
- [建造者模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Builder) `Builder`
- [门面模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Facade) `Facade`
- [观察者模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Observer) `Observer`
- [适配器模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Adapter) `Adapter`
  - [类的适配器模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Adapter/ClassAdapter.php)
  - [对象的适配器模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Adapter/ObjectAdapter.php)
- [依赖注入](https://github.com/syfxlin/code/tree/master/design-pattern-php/DI) `DI`
- [控制反转](https://github.com/syfxlin/code/tree/master/design-pattern-php/IoC) `IoC, PSR-11`
- [管道设计](https://github.com/syfxlin/code/tree/master/design-pattern-php/Pipeline) `Pipeline`
- [装饰模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Decorator) `Decorator`
- [代理模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Proxy) `Proxy`
- [组合模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Composite) `Composite`
- [迭代器模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Iterator) `Iterator`
- [命令模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Command) `Command`
- [策略模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Strategy) `Strategy`
- [桥接模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Bridge) `Bridge`
- [原型模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Prototype) `Prototype`
- [模板模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/Template) `Template`
- [状态模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/State) `State`
- [责任链模式](https://github.com/syfxlin/code/tree/master/design-pattern-php/ChainOfResponsibilities) `Chain Of Responsibilities`

## 其他

- [PHP 中间件](https://github.com/syfxlin/blog-code/tree/master/php-middleware) `PSR-15, Pipeline` - [博客文章](https://blog.ixk.me/middleware-implementation-with-php.html)
- [React Store](https://github.com/syfxlin/blog-code/tree/master/react-store) `TypeScript`
- [Vue Store](https://github.com/syfxlin/blog-code/tree/master/vue-store) `JavaScript`
- [Vue 3 Store](https://github.com/syfxlin/code/tree/master/vue3-ts-store) `TypeScript`
- [MVVM](https://github.com/syfxlin/code/tree/master/MVVM) `JavaScript, Proxy`

## 一些乱七八糟的文件

- [IDEA Atom One Dark Pro 配色文件](https://github.com/syfxlin/blog-code/tree/master/other/Atom-One-Dark-Pro.icls)
- [PowerShell Profile 配置文件](https://github.com/syfxlin/blog-code/tree/master/other/posh-profile.ps1)
- [PowerShell 设置代理脚本](https://github.com/syfxlin/blog-code/tree/master/other/posh-proxy.ps1)
- [Windows Terminal 配置文件](https://github.com/syfxlin/blog-code/tree/master/other/windows-terminal-profile.json)
