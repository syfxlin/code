<?php

namespace Animal;

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
