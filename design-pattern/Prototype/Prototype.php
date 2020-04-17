<?php

/**
 * 原型模式
 * 
 * 原型模式就是从实例派生出实例的设计模式，保留一个原型实例，派生的实例拥有和原型实例相同的属性和方法
 */

abstract class Prototype
{
    public $some_data = "";

    /**
     * 克隆自身的方法
     */
    public abstract function make(): Prototype;
}

class ConcretePrototype extends Prototype
{
    public function __construct($data)
    {
        $this->some_data = $data;
    }

    public function make(): Prototype
    {
        return clone $this;
    }
}

$p = new ConcretePrototype("原型\n");

$c1 = $p->make();
echo $c1->some_data; // 原型
$c1->some_data = "修改后的实例\n";
echo $c1->some_data; // 修改后的实例
$c2 = $p->make();
echo $c2->some_data; // 原型

class Manager
{
    public $map = [];

    public function register(string $abstract, Prototype $prototype)
    {
        $this->map[$abstract] = $prototype;
    }

    public function make(string $abstract): Prototype
    {
        return $this->map[$abstract]->make();
    }
}

$manager = new Manager;

$manager->register(ConcretePrototype::class, new ConcretePrototype("原型"));
$c = $manager->make(ConcretePrototype::class);
echo $c->some_data; // 原型
