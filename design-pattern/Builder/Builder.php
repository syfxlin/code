<?php

/**
 * 建造者模式
 */

class Product
{
    public $name = "";

    public function __construct($name)
    {
        $this->name = $name;
    }

    public function __toString()
    {
        return $this->name;
    }
}

class Cpu extends Product
{
}

class MainBoard extends Product
{
}

class Memory extends Product
{
}

class Disk extends Product
{
}

class Computer
{
    public $cpu;
    public $mainboard;
    public $memory;
    public $disk;

    public function __construct(Cpu $cpu, MainBoard $mainboard, Memory $memory, Disk $disk)
    {
        $this->cpu = $cpu;
        $this->mainboard = $mainboard;
        $this->memory = $memory;
        $this->disk = $disk;
    }

    /**
     * 获取构造者，其他支持内部类的语言，如Java，可以使用内部类实现
     *
     * @return  Builder
     */
    public static function getBuilder(...$args): Builder
    {
        return new Builder(...$args);
    }
}

class Builder
{
    private $computer;

    public function installCpu(string $name): Builder
    {
        $this->computer->cpu = new Cpu($name);
        return $this;
    }

    public function installMainBoard(string $name): Builder
    {
        $this->computer->mainboard = new MainBoard($name);
        return $this;
    }

    public function installMemory(string $name): Builder
    {
        $this->computer->memory = new Memory($name);
        return $this;
    }

    public function installDisk(string $name): Builder
    {
        $this->computer->disk = new Disk($name);
        return $this;
    }

    public function __construct(
        string $cpu = "CPU 0",
        string $mainboard = "MainBoard 0",
        string $memory = "Memory 0",
        string $disk = "Disk 0"
    ) {
        $this->computer = new Computer(
            new Cpu($cpu),
            new MainBoard($mainboard),
            new Memory($memory),
            new Disk($disk)
        );
    }

    public function build()
    {
        return $this->computer;
    }
}

var_dump(
    Computer::getBuilder()
        ->installCpu("Intel(R) Core(TM) i5-8400 CPU @ 2.80GHz")
        ->installMainBoard("H370")
        ->installMemory("DDR4 8G x2")
        ->installDisk("Samsung SM961")
        ->build()
);

// F:\Code\code\design-pattern\Builder\Builder.php:118:
// class Computer#2 (4) {
//   public $cpu =>
//   class Cpu#7 (1) {
//     public $name =>
//     string(39) "Intel(R) Core(TM) i5-8400 CPU @ 2.80GHz"
//   }
//   public $mainboard =>
//   class MainBoard#3 (1) {
//     public $name =>
//     string(4) "H370"
//   }
//   public $memory =>
//   class Memory#4 (1) {
//     public $name =>
//     string(10) "DDR4 8G x2"
//   }
//   public $disk =>
//   class Disk#5 (1) {
//     public $name =>
//     string(13) "Samsung SM961"
//   }
// }
