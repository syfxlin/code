<?php

/**
 * 迭代器模式
 */

interface IIterator
{
    public function current();
    public function key();
    public function next();
    public function rewind(): void;
    public function hasNext(): bool;
}

interface Aggregate
{
    public function getIterator(): IIterator;
}

class MyAggregate implements Aggregate
{
    public $list = [];

    public function __construct(array $list)
    {
        $this->list = $list;
    }

    public function getIterator(): IIterator
    {
        return new MyIterator($this);
    }
}

class MyIterator implements IIterator
{
    /**
     * @var MyAggregate
     */
    private $aggregate;

    /**
     * @var int
     */
    private $cursor = 0;

    public function __construct(MyAggregate $aggregate)
    {
        $this->aggregate = $aggregate;
    }

    public function current()
    {
        return $this->aggregate->list[$this->cursor];
    }

    public function key()
    {
        return $this->cursor;
    }

    public function next()
    {
        $data = $this->current();
        $this->cursor++;
        return $data;
    }

    public function rewind(): void
    {
        $this->cursor = 0;
    }

    public function hasNext(): bool
    {
        return $this->cursor < count($this->aggregate->list);
    }
}

$it = (new MyAggregate([1, 2, 3, 4, 5, 6, 7, 8, 9]))->getIterator();

while ($it->hasNext()) {
    echo $it->next() . " ";
}
// 1 2 3 4 5 6 7 8 9 
