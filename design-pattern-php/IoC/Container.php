<?php

use Psr\Container\ContainerInterface;

/**
 * IoC 容器，兼容 PSR-11
 */
class Container implements ContainerInterface
{
    /**
     * 容器中存储依赖的数组
     * 存储的是闭包，运行闭包会返回对应的依赖实例
     *
     * @var array
     */
    protected $bindings = [];

    /**
     * 绑定方法
     *
     * @var array
     */
    protected $methodBindings = [];

    /**
     * 已创建的单例实例
     *
     * @var array
     */
    protected $instances = [];

    /**
     * 自动通过类名绑定类
     *
     * @var bool
     */
    protected $autobind = true;

    /**
     * 依赖别名
     *
     * @var string[]
     */
    protected $aliases = [];

    /**
     * 绑定依赖
     *
     * @param string|array $abstract 依赖名或者依赖列表
     * @param Closure|string|null $concrete 依赖闭包
     *
     * @param bool $shared
     * @param bool|string $alias
     * @return  Container
     */
    public function bind(
        $abstract,
        $concrete = null,
        bool $shared = false,
        $alias = false
    ): Container {
        // 同时绑定多个依赖
        if (is_array($abstract)) {
            foreach ($abstract as $_abstract => $value) {
                if (is_int($_abstract)) {
                    $_abstract = $value;
                }
                $_abstract = $this->getAbstract($_abstract);
                $_concrete = $_abstract;
                $_shared = false;
                if (is_bool($value)) {
                    $_shared = $value;
                } elseif (is_array($value)) {
                    [$_concrete, $_shared] = $value;
                }
                $this->setBinding($_abstract, $_concrete, $_shared);
            }
            return $this;
        }
        $abstract = $this->getAbstract($abstract);
        // 为了方便绑定依赖，可以节省一个参数
        if ($concrete === null) {
            $concrete = $abstract;
        }
        $this->setBinding($abstract, $concrete, $shared);
        if ($alias) {
            $this->alias($abstract, $alias);
        }
        // 返回 this 使其支持链式调用
        return $this;
    }

    // 设置 binding
    protected function setBinding(
        string $abstract,
        $concrete,
        bool $shared = false
    ): void {
        $abstract = $this->getAbstract($abstract);
        // 传入的默认是闭包，如果没有传入闭包则默认创建
        if (!$concrete instanceof Closure) {
            $concrete = function (Container $c, array $args = []) use (
                $concrete
            ) {
                return $c->build($concrete, $args);
            };
        }
        // 判断是否是单例，是否被设置过
        if ($shared && isset($this->bindings[$abstract])) {
            throw new RuntimeException(
                "Target [$abstract] is a singleton and has been bind"
            );
        }
        // 设置绑定的闭包
        $this->bindings[$abstract] = compact('concrete', 'shared');
    }

    // 获取 binding
    protected function getBinding(string $abstract)
    {
        $abstract = $this->getAbstract($abstract);
        if (!isset($this->bindings[$abstract])) {
            // 尝试自动绑定
            if (
                $this->autobind &&
                $abstract[0] !== '$' &&
                class_exists($abstract)
            ) {
                $this->setBinding($abstract, $abstract);
            } else {
                throw new RuntimeException(
                    "Target [$abstract] is not binding or fail autobind"
                );
            }
        }
        return $this->bindings[$abstract];
    }

    // 判断 binding 是否存在
    protected function hasBinding(string $abstract): bool
    {
        $abstract = $this->getAbstract($abstract);
        return isset($this->bindings[$abstract]);
    }

    /**
     * 实例化对象
     *
     * @param string $abstract 对象名称
     * @param array $args
     *
     * @return  mixed
     */
    public function make(string $abstract, array $args = [])
    {
        $abstract = $this->getAbstract($abstract);
        $binding = $this->getBinding($abstract);
        $concrete = $binding['concrete'];
        $shared = $binding['shared'];
        // 判断是否是单例，若是单例并且已经实例化过就直接返回实例
        if ($shared && isset($this->instances[$abstract])) {
            return $this->instances[$abstract];
        }
        // 构建实例
        $instance = $concrete($this, $args);
        // 判断是否是单例，若是则设置到容器的单例列表中
        if ($shared) {
            $this->instances[$abstract] = $instance;
        }
        return $instance;
    }

    /**
     * 绑定单例
     *
     * @param string $abstract 依赖名称
     * @param mixed $concrete 依赖闭包
     * @param bool|string $alias
     *
     * @return  Container
     */
    public function singleton(
        string $abstract,
        $concrete = null,
        $alias = false
    ): Container {
        $this->bind($abstract, $concrete, true, $alias);
        return $this;
    }

    /**
     * 绑定已实例化的单例
     *
     * @param   string     $abstract  依赖名称
     * @param   mixed      $instance  已实例化的单例
     *
     * @return  Container
     */
    public function instance(string $abstract, $instance): Container
    {
        $abstract = $this->getAbstract($abstract);
        $this->instances[$abstract] = $instance;
        $this->bindings[$abstract] = [
            // 直接返回单例
            'concrete' => function () use ($instance) {
                return $instance;
            },
            'shared' => true
        ];
        return $this;
    }

    /**
     * 构建实例
     *
     * @param Closure|string $class 类名或者闭包
     * @param array $args
     * @return  mixed
     *
     * @throws ReflectionException
     */
    public function build($class, array $args = [])
    {
        if ($class instanceof Closure) {
            return $class($this, $args);
        }
        if (!class_exists($class)) {
            return $class;
        }
        // 取得反射类
        $reflector = new ReflectionClass($class);
        // 检查类是否可实例化
        if (!$reflector->isInstantiable()) {
            // 如果不能，意味着接口不能正常工作，报错
            throw new RuntimeException("Target [$class] is not instantiable");
        }
        // 取得构造函数
        $constructor = $reflector->getConstructor();
        // 检查是否有构造函数
        if ($constructor === null) {
            // 如果没有，就说明没有依赖，直接实例化
            return new $class();
        }
        // 取得构造方法中的参数数组
        $parameters = $constructor->getParameters();
        // 返回已注入依赖的参数数组
        $dependency = $this->injectingDependencies($parameters, $args);
        // 利用注入后的参数创建实例
        return $reflector->newInstanceArgs($dependency);
    }

    /**
     * 注入依赖
     *
     * @param array $parameters 参数列表
     * @param array $args
     *
     * @return  array
     */
    protected function injectingDependencies(
        array $parameters,
        array $args = []
    ): array {
        $dependency = [];
        foreach ($parameters as $parameter) {
            if (isset($args[$parameter->name])) {
                $dependency[] = $args[$parameter->name];
                continue;
            }
            // 利用参数的类型声明，获取到参数的类型，然后从 bindings 中获取依赖注入
            $dependencyClass = $parameter->getClass();
            if ($dependencyClass === null) {
                $dependency[] = $this->resolvePrimitive($parameter);
            } else {
                // 实例化依赖
                $dependency[] = $this->resolveClass($parameter);
            }
        }
        return $dependency;
    }

    /**
     * 处理非类的依赖
     *
     * @param ReflectionParameter $parameter
     *
     * @return  mixed
     */
    protected function resolvePrimitive(ReflectionParameter $parameter)
    {
        $abstract = '$' . $parameter->name;
        // 通过变量名匹配别名
        if ($this->isAlias($parameter->name)) {
            $abstract = $this->getAbstract($parameter->name);
        }
        try {
            $concrete = $this->getBinding($abstract)['concrete'];
        } catch (RuntimeException $e) {
            $concrete = null;
        }
        if ($concrete !== null) {
            return $concrete instanceof Closure ? $concrete($this) : $concrete;
        }
        if ($parameter->isDefaultValueAvailable()) {
            return $parameter->getDefaultValue();
        }
        throw new RuntimeException("Target [$$parameter->name] is not binding");
    }

    /**
     * 处理类依赖
     *
     * @param ReflectionParameter $parameter
     *
     * @return  mixed
     */
    protected function resolveClass(ReflectionParameter $parameter)
    {
        try {
            return $this->make($parameter->getClass()->name);
        } catch (RuntimeException $e) {
            if ($parameter->isDefaultValueAvailable()) {
                return $parameter->getDefaultValue();
            }
            throw $e;
        }
    }

    /**
     * 设置自动绑定
     *
     * @param   bool  $use  是否自动绑定类
     *
     * @return  void
     */
    public function useAutoBind(bool $use): void
    {
        $this->autobind = $use;
    }

    /**
     * 判断是否绑定了指定的依赖
     *
     * @param $id
     * @return  bool
     */
    public function has($id): bool
    {
        return $this->hasBinding($id);
    }

    /**
     * 同 make
     *
     * @param   string  $id  对象名称
     *
     * @return  mixed
     */
    public function get($id)
    {
        return $this->make($id);
    }

    public function hasMethod(string $method): bool
    {
        return isset($this->methodBindings[$method]);
    }

    public function bindMethod(string $method, $callback): void
    {
        $this->methodBindings[$method] = $callback;
    }

    protected function getMethodBind(string $method)
    {
        if (isset($this->methodBindings[$method])) {
            return $this->methodBindings[$method];
        }
        throw new RuntimeException("Target [$method] is not binding");
    }

    public function call($method, array $args = [])
    {
        if (is_string($method) && preg_match('/@|::/', $method) > 0) {
            return $this->callClass($method, $args);
        }
        if (is_string($method)) {
            $method = $this->getMethodBind($method);
        }
        return $this->callFunction($method, $args);
    }

    protected function callFunction($method, array $args = [])
    {
        $reflector = new ReflectionFunction($method);
        $parameters = $reflector->getParameters();
        $dependency = $this->injectingDependencies($parameters, $args);
        return $reflector->invokeArgs($dependency);
    }

    protected function callClass($target, array $args = [])
    {
        $class = null;
        $method = null;
        $object = null;
        $invokeObject = null;
        if (strpos($target, '@') !== false) {
            [$class, $method] = explode('@', $target);
            $object = $this->build($class);
            $invokeObject = $object;
        } else {
            [$class, $method] = explode('::', $target);
            $object = $class;
        }
        $reflector = new ReflectionMethod($object, $method);
        $parameters = $reflector->getParameters();
        $dependency = $this->injectingDependencies($parameters, $args);
        return $reflector->invokeArgs($invokeObject, $dependency);
    }

    public function isAlias($name): bool
    {
        return isset($this->aliases[$name]);
    }

    public function alias($abstract, $alias): void
    {
        if ($abstract === $alias) {
            return;
        }
        $this->aliases[$alias] = $abstract;
    }

    public function getAlias($abstract)
    {
        foreach ($this->aliases as $alias => $value) {
            if ($value === $abstract) {
                return $alias;
            }
        }
        return $abstract;
    }

    public function getAbstract($alias)
    {
        return $this->aliases[$alias] ?? $alias;
    }

    public function removeAlias($alias): void
    {
        unset($this->aliases[$alias]);
    }
}
