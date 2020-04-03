<?php

/**
 * 依赖注入
 * 
 * PetShop 不控制 Animal 类的创建和销毁，只负责使用，这个称之为 IOC（控制反转）
 * 为了使用 Animal 的实例就必须将 Animal 传入 PetShop，传入的过程称之为 DI（依赖注入）
 * 
 * 依赖注入可以实现不同的依赖的代码复用
 * 即 PetShop 可以通过传入不同的 Animal 来打印不同的动物名，而不必在为对应的 Animal 另外写一个方法
 *
 * 依赖注入有 3 种方式
 * - 构造器注入
 * - 接口注入
 * - 设置属性注入
 */

interface Animal
{
    public function getName(): string;
}

class Cat implements Animal
{
    public function getName(): string
    {
        return "猫";
    }
}

class Dog implements Animal
{
    public function getName(): string
    {
        return "狗";
    }
}

class PetShop
{
    /**
     * 动物依赖
     *
     * @var Animal
     */
    private $animal;

    /**
     * 构造器注入
     *
     * @param   Animal  $animal  动物依赖
     *
     * @return  PetShop
     */
    public function __construct(Animal $animal)
    {
        $this->animal = $animal;
    }

    public function printName()
    {
        echo $this->animal->getName() . "\n";
    }
}

// 注入猫依赖
(new PetShop(new Cat()))->printName();

// 注入狗依赖
(new PetShop(new Dog()))->printName();

// 猫
// 狗
