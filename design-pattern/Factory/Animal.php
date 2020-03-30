<?php
class Animal
{
    public $name = "Animal";

    public function getName(): string
    {
        return $this->name;
    }

    public function printName(): void
    {
        echo $this->getName();
    }
}

class Cat extends Animal
{
    public $name = "Cat";
}

class Dog extends Animal
{
    public $name = "Dog";
}
