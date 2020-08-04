<?php

/**
 * 简单/静态工厂
 */

require_once __DIR__ . "/Animal.php";

class AnimalFactory
{
    /**
     * 制造动物
     *
     * @param   string  $type  动物类型
     *
     * @return  Animal
     */
    public static function make(string $type): Animal
    {
        switch ($type) {
            case "Cat":
            case "cat":
                return new Cat;
                break;
            case "Dog":
            case "dog":
                return new Dog;
                break;
            default:
                return new $type;
        }
    }
}

$cat = AnimalFactory::make('cat');
$dog = AnimalFactory::make('dog');

var_dump($cat, $dog);

// F:\Code\code\design-pattern\Factory\SimpleFactory.php:49:
// class Cat#1 (1) {
//   public $name =>
//   string(3) "Cat"
// }
// F:\Code\code\design-pattern\Factory\SimpleFactory.php:49:
// class Dog#2 (1) {
//   public $name =>
//   string(3) "Dog"
// }
