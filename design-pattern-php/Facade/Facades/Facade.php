<?php

namespace Facades;

class Facade
{
    /**
     * Facade 对应的类名
     *
     * @var string|null
     */
    protected static $class;

    /**
     * 是否是单例接口
     *
     * @var bool
     */
    protected static $isInstance = false;

    /**
     * 是否是静态方法
     *
     * @var bool
     */
    protected static $isStatic = false;

    /**
     * Facade 代理
     *
     * @param   string  $name       方法名
     * @param   array   $arguments  方法参数
     *
     * @return  mixed
     */
    public static function __callStatic(string $name, array $arguments)
    {
        $class = null;
        if (!static::$isInstance) {
            $class = (new static::$class(...static::getArgs()));
        } else {
            $class = (static::$class::getInstance(...static::getArgs()));
        }
        if (static::$isStatic) {
            return $class::$name(...$arguments);
        } else {
            return $class->$name(...$arguments);
        }
    }

    /**
     * 动态获取构造器或者 getInstance 参数
     *
     * @return  array
     */
    public static function getArgs()
    {
        return [];
    }
}
