<?php

/**
 * 组合模式
 * 
 * 组合模式一般用于树形结构或需要嵌套的场景
 */

interface Component
{
    public function getName(): string;
    public function setName(string $name): void;
}

class File implements Component
{
    /**
     * @var string
     */
    private $name;

    public function __construct(string $name)
    {
        $this->name = $name;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function setName(string $name): void
    {
        $this->name = $name;
    }
}

class Folder implements Component
{
    /**
     * @var string
     */
    private $name;

    /**
     * @var Component[]
     */
    public $sub = [];

    public function __construct(array $sub = [])
    {
        $this->sub = $sub;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function setName(string $name): void
    {
        $this->name = $name;
    }
}

$folder1 = new Folder([new File('file1'), new File('file2')]);
$folder2 = new Folder([$folder1, new File('file3')]);

var_dump($folder2);

// F:\Code\code\design-pattern\Composite\Composite.php:69:
// class Folder#4 (2) {
//   private $name =>
//   NULL
//   public $sub =>
//   array(2) {
//     [0] =>
//     class Folder#1 (2) {
//       private $name =>
//       NULL
//       public $sub =>
//       array(2) {
//         ...
//       }
//     }
//     [1] =>
//     class File#5 (1) {
//       private $name =>
//       string(5) "file3"
//     }
//   }
// }
