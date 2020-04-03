<?php

/**
 * IoC 控制反转
 */

require_once __DIR__ . "/Container.php";

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

class PetShop1
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
     * @param   Dog  $animal  动物依赖
     *
     * @return  PetShop1
     */
    public function __construct(Dog $animal)
    {
        $this->animal = $animal;
    }

    public function printName()
    {
        echo $this->animal->getName() . "\n";
    }
}

class PetShop2
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
     * @param   Cat  $animal  动物依赖
     *
     * @return  PetShop2
     */
    public function __construct(Cat $animal)
    {
        $this->animal = $animal;
    }

    public function printName()
    {
        echo $this->animal->getName() . "\n";
    }
}

$container = new Container();
$container->bind([
    Dog::class,
    Cat::class => Cat::class
]);
$container->bind(PetShop1::class);
$container->bind(PetShop2::class, PetShop2::class);


$container->make(PetShop1::class)->printName();
$container->make(PetShop2::class)->printName();

// 狗
// 猫
