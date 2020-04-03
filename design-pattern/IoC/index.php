<?php

/**
 * IoC 控制反转
 */
require_once __DIR__ . "/vendor/autoload.php";
require_once __DIR__ . "/Container.php";

interface Animal
{
    public function getName(): string;
    public function setName($name);
}

class Cat implements Animal
{
    public $name = "猫";

    public function getName(): string
    {
        return $this->name;
    }

    public function setName($name)
    {
        $this->name = $name;
    }
}

class Dog implements Animal
{
    public $name = "狗";

    public function getName(): string
    {
        return $this->name;
    }

    public function setName($name)
    {
        $this->name = $name;
    }
}

class PetShop1
{
    /**
     * 动物依赖
     *
     * @var Animal
     */
    public $animal;

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
    public $animal;

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

$c = new Container();
$c->bind([
    Dog::class => [Dog::class, true]
    // Dog::class => true
]);
// $c->instance(Dog::class, new Dog);
// $c->singleton(Cat::class);
$c->bind(PetShop1::class);
$i1 = $c->make(PetShop1::class);
$i2 = $c->make(PetShop1::class);

$i1->animal->setName("大狗");
$i2->animal->setName("小狗");

$i1->printName();
$i2->printName();

// 小狗
// 小狗
