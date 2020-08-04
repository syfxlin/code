<?php
abstract class Animal
{

    abstract function eat(): string;
    abstract function run(): string;

    public function adopt(): void
    {
        echo "领养了\n";
        echo $this->eat() . "\n";
        echo $this->run() . "\n";
    }
}

class Cat extends Animal
{
    public function eat(): string
    {
        return "猫吃食物";
    }

    public function run(): string
    {
        return "猫在运动";
    }
}

class Dog extends Animal
{
    public function eat(): string
    {
        return "狗吃食物";
    }

    public function run(): string
    {
        return "狗在运动";
    }
}

$cat = new Cat;
$dog = new Dog;

$cat->adopt();
// 领养了
// 猫吃食物
// 猫在运动

$dog->adopt();
// 领养了
// 狗吃食物
// 狗在运动
