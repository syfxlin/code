<?php

/**
 * 工厂方法模式，和简单工厂的差别是工厂方法模式是具体的工厂产生具体的产品
 */

require_once __DIR__ . "/Animal.php";

/**
 * 动物工厂接口
 */
interface AnimalFactory
{
    public static function make(): Animal;
}

class CatFactory implements AnimalFactory
{
    public static function make(): Animal
    {
        return new Cat;
    }
}

class DogFactory implements AnimalFactory
{
    public static function make(): Animal
    {
        return new Dog;
    }
}

$cat = CatFactory::make();
$dog = DogFactory::make();

var_dump($cat, $dog);

// F:\Code\code\design-pattern\Factory\FactoryMethod.php:36:
// class Cat#1 (1) {
//   public $name =>
//   string(3) "Cat"
// }
// F:\Code\code\design-pattern\Factory\FactoryMethod.php:36:
// class Dog#2 (1) {
//   public $name =>
//   string(3) "Dog"
// }
