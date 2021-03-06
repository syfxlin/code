<?php

/**
 * 单例模式
 * 
 * 单例模式只允许创建一个对象，因此节省内存，加快对象访问速度
 * 因此对象需要被公用的场合适合使用，如多个模块使用同一个数据源连接对象等等
 */

class Singleton
{
    /**
     * 静态属性保存实例
     *
     * @var Singleton|null
     */
    private static $instance = null;

    /**
     * 获取实例的方法
     *
     * @return  Singleton
     */
    public static function getInstance(): Singleton
    {
        if (is_null(self::$instance)) {
            self::$instance = new self;
        }
        return self::$instance;
    }

    // 不允许外部调用生成新对象
    private function __construct()
    {
    }

    // 不允许外部克隆生成新对象，也可以返回当前单例，但是这样会产生歧义
    private function __clone()
    {
    }

    // 不允许外部反序列生成新对象
    private function __wakeup()
    {
    }
}

$instance1 = Singleton::getInstance();
$instance2 = Singleton::getInstance();

var_dump($instance1, $instance2, $instance1 === $instance2);

// Output:
// F:\Code\code\design-pattern\Singleton\Singleton.php:33:
// class Singleton#1 (0) {
// }
// F:\Code\code\design-pattern\Singleton\Singleton.php:33:
// class Singleton#1 (0) {
// }
// F:\Code\code\design-pattern\Singleton\Singleton.php:33:
// bool(true)
