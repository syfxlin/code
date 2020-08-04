<?php

/**
 * 策略模式
 * 
 * 策略模式定义了一系列的算法，并将每个算法封装起来
 * 而且使他们还可以相互替换。策略模式让算法独立于使用它的客户而独立变化。
 * 策略模式和状态模式，命令模式比较类似
 * 策略模式重在整个算法的替换
 * 状态模式是通过状态来改变行为
 * 命令模式重在调用者和执行者解耦
 */

class Context
{
    /**
     * @var string
     */
    public $str = "This is a string";

    /**
     * @var Strategy
     */
    public $strategy;

    public function __construct(Strategy $strategy)
    {
        $this->strategy = $strategy;
    }

    public function setStrategy(Strategy $strategy)
    {
        $this->strategy = $strategy;
    }

    public function process()
    {
        return $this->strategy->handler($this);
    }
}

interface Strategy
{
    public function handler(Context $ctx);
}

class SubStrStrategy implements Strategy
{
    public function handler(Context $ctx)
    {
        return substr($ctx->str, 5);
    }
}

class ConcatStrategy implements Strategy
{
    public function handler(Context $ctx)
    {
        return $ctx->str . " concat";
    }
}

echo (new Context(new SubStrStrategy))->process() . "\n"; // is a string
echo (new Context(new ConcatStrategy))->process() . "\n"; // This is a string concat
