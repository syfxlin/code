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
     * 绑定依赖
     *
     * @param   string|array  $abstract  依赖名或者依赖列表
     * @param   Closure|string|null  $concrete  依赖闭包
     *
     * @return  Container
     */
    public function bind($abstract, $concrete = null, $shared = false): Container
    {
        // 同时绑定多个依赖
        if (is_array($abstract)) {
            foreach ($abstract as $_abstract => $value) {
                if (is_int($_abstract)) {
                    $_abstract = $value;
                }
                $_concrete = $_abstract;
                $_shared = false;
                if (is_bool($value)) {
                    $_shared = $value;
                } else if (is_array($value)) {
                    $_concrete = $value[0];
                    $_shared = $value[1];
                }
                $this->setBinding($_abstract, $_concrete, $_shared);
            }
            return $this;
        }
        // 为了方便绑定依赖，可以节省一个参数
        if (is_null($concrete)) {
            $concrete = $abstract;
        }
        $this->setBinding($abstract, $concrete, $shared);
        // 返回 this 使其支持链式调用
        return $this;
    }

    // 设置 binding
    protected function setBinding(string $abstract, $concrete, bool $shared = false)
    {
        // 传入的默认是闭包，如果没有传入闭包则默认创建
        if (!$concrete instanceof Closure) {
            $concrete = function ($c) use ($concrete) {
                return $c->build($concrete);
            };
        }
        // 判断是否是单例，是否被设置过
        if ($shared && isset($this->bindings[$abstract])) {
            throw new RuntimeException("Target [$abstract] is a singleton and has been bind");
        }
        // 设置绑定的闭包
        $this->bindings[$abstract] = compact('concrete', 'shared');
    }

    // 获取 binding
    protected function getBinding(string $abstract)
    {
        if (!isset($this->bindings[$abstract])) {
            // 尝试自动绑定
            if ($this->autobind && class_exists($abstract)) {
                $this->setBinding($abstract, $abstract);
            } else {
                throw new RuntimeException("Target [$abstract] is not binding or fail autobind");
            }
        }
        return $this->bindings[$abstract];
    }

    // 判断 binding 是否存在
    protected function hasBinding(string $abstract): bool
    {
        return isset($this->bindings[$abstract]);
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
        $binding = $this->getBinding($abstract);
        $concrete = $binding['concrete'];
        $shared = $binding['shared'];
        // 判断是否是单例，若是单例并且已经实例化过就直接返回实例
        if ($shared && isset($this->instances[$abstract])) {
            return $this->instances[$abstract];
        }
        // 构建实例
        $instance = $concrete($this);
        // 判断是否是单例，若是则设置到容器的单例列表中
        if ($shared) {
            $this->instances[$abstract] = $instance;
        }
        return $instance;
    }

    /**
     * 绑定单例
     *
     * @param   string     $abstract  依赖名称
     * @param   mixed      $concrete  依赖闭包
     *
     * @return  Container
     */
    public function singleton(string $abstract, $concrete = null): Container
    {
        $this->bind($abstract, $concrete, true);
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
     * @param   Closure|string  $class  类名或者闭包
     *
     * @return  mixed
     */
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
     * @param   string  $abstract  依赖名
     *
     * @return  bool
     */
    public function has($id)
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
}
