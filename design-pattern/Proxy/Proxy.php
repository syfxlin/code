<?php

/**
 * 代理模式
 * 
 * 代理模式和装饰模式比较类似
 */

// 用户接口
interface User
{
    public function buyPhone();
}

// 真实的对象类，只能购买中国版的手机
class ChinaUser implements User
{
    public function check()
    {
        echo "检查手机\n";
    }

    public function buyPhone()
    {
        echo "购买了一台手机\n";
    }
}

// 如果那天想要买一台港版的手机，那么就需要找代购
class HongKongProxy implements User
{
    public $user;

    public function __construct(User $user)
    {
        $this->user = $user;
    }

    public function goHongKong()
    {
        echo "去香港\n";
    }

    public function buyPhone()
    {
        $this->goHongKong();

        // 执行的是目标对象的方法
        $this->user->buyPhone();
    }

    // 动态代理目标对象的其他方法
    public function __call($name, $arguments)
    {
        return $this->user->$name(...$arguments);
    }
}

$proxy = new HongKongProxy(new ChinaUser);
$proxy->buyPhone();
$proxy->check();

// 去香港
// 购买了一台手机
// 检查手机
