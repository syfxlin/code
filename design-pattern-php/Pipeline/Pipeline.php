<?php

/**
 * 管道设计
 * 
 * 管道就像一个流水线，把复杂的问题的解决方案分解成一个个处理单元，然后依次处理，前一个处理单元的结果就是下一个处理单元的输入。
 * Laravel 中的中间件就是利用管道来执行的
 */

class Pipeline
{
    /**
     * 初始传入的数据
     *
     * @var mixed
     */
    protected $passable;

    /**
     * 管道集合
     *
     * @var array
     */
    protected $pipes = [];

    /**
     * 设置管道初始数据
     *
     * @param   mixed   $passable  数据
     *
     * @return  Pipeline
     */
    public function send($passable): Pipeline
    {
        $this->passable = $passable;
        return $this;
    }

    /**
     * 获取管道初始数据
     *
     * @return  mixed
     */
    public function passable()
    {
        return $this->passable;
    }

    /**
     * 设置管道集合
     *
     * @param   array     $pipes  管道集合
     *
     * @return  Pipeline
     */
    public function through(array $pipes): Pipeline
    {
        $this->pipes = $pipes;
        return $this;
    }

    /**
     * 获取管道集合
     *
     * @return  array
     */
    public function pipes(): array
    {
        return $this->pipes;
    }

    /**
     * 运行管道
     *
     * @param   Closure  $destination  管道最终运行闭包
     *
     * @return  mixed
     */
    public function then(Closure $destination)
    {
        $next = $destination;
        $pipes = array_reverse($this->pipes);
        foreach ($pipes as $pipe) {
            $next = function ($passable) use ($next, $pipe) {
                if (is_callable($pipe)) {
                    return $pipe($passable, $next);
                } else {
                    return (new $pipe)->handle($passable, $next);
                }
            };
        }
        return $next($this->passable);
    }
}

class PipeTest
{
    public function handle($passable, $next)
    {
        $passable .= '5';
        return $next($passable);
    }
}

$pipes = [
    function ($passable, $next) {
        $passable .= '1';
        return $next($passable);
    },
    function ($passable, $next) {
        $passable .= '2';
        $result = $next($passable);
        // 后置管道
        $result .= '3';
        return $result;
    },
    function ($passable, $next) {
        $passable .= '4';
        return $next($passable);
    },
    PipeTest::class
];

echo (new Pipeline)->send('0')->through($pipes)->then(function ($passable) {
    $passable .= '6';
    return $passable;
});

// 0124563
