<?php

/**
 * 装饰模式
 */

// 基础的接口或类，如果需要在其增加功能并不想改变这个接口或类就可以对齐进行装饰
interface Cookie
{
    public function make();
}

// 未被装饰的类
class NormalCookie implements Cookie
{
    public $name = "普通的饼干";

    public function make()
    {
        echo "制作了一个$this->name\n";
    }
}

abstract class CookieDecorator implements Cookie
{
    private $cookie;

    public function __construct(Cookie $cookie)
    {
        $this->cookie = $cookie;
    }
    public function make()
    {
        $this->cookie->make();
    }
}

class SaltCookie extends CookieDecorator
{
    // 如果不需要修改构造器部分的话也可以不加
    // public function __construct(Cookie $cookie)
    // {
    //     parent::__construct($cookie);
    // }

    public function make()
    {
        parent::make();
        // 添加功能（装饰）
        echo "加盐\n";
    }
}

class SugarCookie extends CookieDecorator
{
    public function make()
    {
        parent::make();
        // 添加功能（装饰）
        echo "加糖\n";
    }
}

(new SugarCookie(new SaltCookie(new NormalCookie)))->make();

// 制作了一个普通的饼干
// 加盐
// 加糖
