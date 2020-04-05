<?php

/**
 * IoC 控制反转
 */
require_once __DIR__ . "/vendor/autoload.php";
require_once __DIR__ . "/Animal.php";
require_once __DIR__ . "/Container.php";

use Animal\Dog;
use Animal\Cat;

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
    public function __construct(\Animal\Dog $animal)
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

/**
 * 测试
 */
$container = new Container();
$container->bind([
    Dog::class,
    Cat::class => Cat::class
]);
$container->bind(PetShop1::class);
$container->bind(PetShop2::class, PetShop2::class);

$container->make(PetShop1::class)->printName(); // 狗
$container->make(PetShop2::class)->printName(); // 猫

/**
 * 单例
 */
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

$i1->printName(); // 小狗
$i2->printName(); // 小狗

/**
 * 自动载入类
 * 默认开启了自动载入，只需传入正确的类名，无需绑定依赖
 */
$c2 = new Container();
$c2->make(PetShop1::class)->printName(); // 狗

// 自动载入若开启，在未找到绑定的依赖时会尝试加载类
// 若找到了类名则进行自动绑定，后续即使关闭了自动绑定也可以使用已经自动绑定过的类
// 如该例自动绑定了 PetShop1 类和 Dog 类，关闭了也可以使用已经绑定过的类
$c2->useAutoBind(false);
$c2->make(PetShop1::class)->printName(); // 狗

// 关闭了自动绑定后如果使用未绑定的类就会触发异常
try {
    $c2->make(PetShop2::class)->printName();
} catch (Exception $e) {
    echo $e->getMessage(); // Target [PetShop2] is not binding or fail autobind
}
