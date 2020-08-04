<?php

/**
 * 桥接模式
 * 
 * 桥接模式就是把对象和对象的一下类型进行分离，然后通过桥接的方式将对象的类型绑定到对象上来达到独立扩展对象和对象类型的功能
 * 在非桥接模式下，我们如果要定义两种口味的饼干，我们需要定义两个饼干类，一个是加糖，一个是加盐
 * 而在桥接模式下，我们只需要修改与之桥接的类型类
 */

interface CookieType
{
    public function getType(): string;
}

abstract class AbstractCookie
{
    /**
     * @var CookieType
     */
    public $type;
    public function __construct(CookieType $type)
    {
        $this->type = $type;
    }
    public abstract function make();
}

class SaltCookieType implements CookieType
{
    public function getType(): string
    {
        return "加盐\n";
    }
}

class SugarCookieType implements CookieType
{
    public function getType(): string
    {
        return "加糖\n";
    }
}

class Cookie extends AbstractCookie
{
    public function __construct(CookieType $type)
    {
        parent::__construct($type);
    }

    public function make()
    {
        echo "制作了一个普通的饼干\n";
        echo $this->type->getType();
    }
}

(new Cookie(new SaltCookieType()))->make();
(new Cookie(new SugarCookieType()))->make();
// 制作了一个普通的饼干
// 加盐
// 制作了一个普通的饼干
// 加糖
