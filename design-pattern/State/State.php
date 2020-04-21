<?php

/**
 * 状态模式
 */

abstract class State
{
    /**
     * @var Context
     */
    protected $ctx;

    public function setContext(Context $ctx)
    {
        $this->ctx = $ctx;
    }

    abstract function eat();
    abstract function sleep();
    abstract function fight();
}

class Eating extends State
{
    public function __construct(Context $ctx)
    {
        $this->setContext($ctx);
    }

    public function eat()
    {
        echo "已经在吃饭了\n";
    }

    public function sleep()
    {
        echo "睡觉\n";
    }

    public function fight()
    {
        echo "打豆豆\n";
    }
}

class Sleeping extends State
{
    public function __construct(Context $ctx)
    {
        $this->setContext($ctx);
    }

    public function eat()
    {
        echo "吃饭\n";
    }

    public function sleep()
    {
        echo "已经在睡觉了\n";
    }

    public function fight()
    {
        echo "打豆豆\n";
    }
}

class FightingDouDou extends State
{
    public function __construct(Context $ctx)
    {
        $this->setContext($ctx);
    }

    public function eat()
    {
        echo "吃饭\n";
    }

    public function sleep()
    {
        echo "睡觉\n";
    }

    public function fight()
    {
        echo "已经在打豆豆了\n";
    }
}

class Context
{
    /**
     * @var State
     */
    protected $state;

    public function setState(State $state): Context
    {
        $this->state = $state;
        return $this;
    }

    public function getState(): State
    {
        return $this->state;
    }

    public function eat()
    {
        $this->state->eat();
        $this->setState(new Sleeping($this));
    }

    public function sleep()
    {
        $this->state->sleep();
        $this->setState(new FightingDouDou($this));
    }

    public function fight()
    {
        $this->state->fight();
        $this->setState(new Eating($this));
    }
}

$ctx = new Context;
$ctx->setState(new Eating($ctx));

$ctx->eat();
$ctx->fight();
$ctx->eat();
// 已经在吃饭了
// 打豆豆
// 已经在吃饭了
