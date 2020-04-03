<?php
class Container
{
    /**
     * 容器中存储依赖的数组
     * 存储的是闭包，运行闭包会返回对应的依赖实例
     *
     * @var array
     */
    protected $bindings = [];

    /**
     * 绑定依赖
     *
     * @param   string|array  $abstract  依赖名或者依赖列表
     * @param   Closure|string|null  $concrete  依赖闭包
     *
     * @return  Container
     */
    public function bind($abstract, $concrete = null): Container
    {
        // 同时绑定多个依赖
        if (is_array($abstract)) {
            foreach ($abstract as $key => $value) {
                if (is_int($key)) {
                    $this->setBind($value, $value);
                } else {
                    $this->setBind($key, $value);
                }
            }
            return $this;
        }
        // 为了方便绑定依赖，可以节省一个参数
        if (is_null($concrete)) {
            $concrete = $abstract;
        }
        $this->setBind($abstract, $concrete);
        // 返回 this 使其支持链式调用
        return $this;
    }

    protected function setBind($abstract, $concrete)
    {
        // 传入的默认是闭包，如果没有传入闭包则默认创建
        if (!$concrete instanceof Closure) {
            $concrete = function ($c) use ($concrete) {
                return $c->build($concrete);
            };
        }
        // 设置绑定的闭包
        $this->bindings[$abstract] = $concrete;
    }

    /**
     * 实例化对象
     *
     * @param   string  $abstract  对象名称
     *
     * @return  mixed
     */
    public function make(string $abstract)
    {
        // 当依赖没有 binding 到容器时抛出异常
        if (!isset($this->bindings[$abstract])) {
            throw new RuntimeException("Target [$abstract] is not binding");
        }
        $concrete = $this->bindings[$abstract];
        return $concrete($this);
    }

    public function build($class)
    {
        if ($class instanceof Closure) {
            return $class($this);
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
        if (is_null($constructor)) {
            // 如果没有，就说明没有依赖，直接实例化
            return new $class;
        }
        // 取得构造方法中的参数数组
        $parameters = $constructor->getParameters();
        // 返回已注入依赖的参数数组
        $dependency = $this->injectingDependencies($parameters);
        // 利用注入后的参数创建实例
        return $reflector->newInstanceArgs($dependency);
    }

    /**
     * 注入依赖
     *
     * @param   array  $parameters  参数列表
     *
     * @return  array
     */
    protected function injectingDependencies(array $parameters): array
    {
        $dependency = [];
        foreach ($parameters as $parameter) {
            // 利用参数的类型声明，获取到参数的类型，然后从 bindings 中获取依赖注入
            $dependencyClass = $parameter->getClass();
            if (is_null($dependencyClass)) {
                $dependency[] = null;
            } else {
                // 实例化依赖
                $dependency[] = $this->make($dependencyClass->name);
            }
        }
        return $dependency;
    }

    /**
     * 判断是否绑定了指定的依赖
     *
     * @param   string  $abstract  依赖名
     *
     * @return  bool
     */
    public function has(string $abstract): bool
    {
        return isset($this->bindings[$abstract]);
    }
}
