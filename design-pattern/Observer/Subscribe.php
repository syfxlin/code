<?php

/**
 * 观察者模式 - 订阅/发布模式
 */

/**
 * 订阅者
 */
interface Subscribe
{
    /**
     * 当订阅的消息发生变化时会调用该方法
     * 
     * @param mixed    $change   改变的值
     * @param Publish  $publish  发布者
     * 
     * @return void
     */
    public function change($change, Publish $publish): void;
}

/**
 * 发布者
 */
interface Publish
{
    /**
     * 添加订阅者
     *
     * @param   Subscribe  $subscribe  订阅者
     *
     * @return  bool                   是否添加成功，若该订阅者已经订阅，则返回false
     */
    public function attach(Subscribe $subscribe): bool;

    /**
     * 删除订阅者
     *
     * @param   Subscribe  $subscribe  订阅者
     *
     * @return  bool                   是否删除成功，若订阅者未订阅则返回false
     */
    public function detach(Subscribe $subscribe): bool;

    /**
     * 通知订阅者，可以强制刷新订阅者的状态
     *
     * @param   mixed   $change  更改的内容
     *
     * @return  void
     */
    public function notify($change): void;

    /**
     * 获得状态
     * 
     * @return mixed
     */
    public function getState();

    /**
     * 设置状态，触发订阅更新
     * 
     * @param  mixed $state 状态
     * 
     * @return void
     */
    public function setState($state);
}

class EmailServer implements Publish
{
    private $inbox = [];

    private $subscribe = [];

    public function attach(Subscribe $subscribe): bool
    {
        if (in_array($subscribe, $this->subscribe)) {
            return false;
        }
        $this->subscribe[] = $subscribe;
        return true;
    }

    public function detach(Subscribe $subscribe): bool
    {
        $index = array_search($subscribe, $this->subscribe);
        if ($index === false) {
            return false;
        }
        array_splice($this->subscribe, $index, 1);
        return true;
    }

    public function notify($change): void
    {
        foreach ($this->subscribe as $sub) {
            $sub->change($change, $this);
        }
    }

    public function getState()
    {
        return $this->inbox;
    }

    public function setState($state)
    {
        $this->inbox[] = $state;
        $this->notify($state);
    }

    public function recieveEmail($email)
    {
        $this->setState($email);
    }
}

class Notice implements Subscribe
{
    public $name = "";

    public function __construct($name)
    {
        $this->name = $name;
    }

    public function change($change, Publish $publish): void
    {
        echo "[$this->name]有新邮件啦：$change\n";
    }
}

$email_server = new EmailServer();

$email_server->attach(new Notice("Phone"));
$email_server->attach(new Notice("Laptop"));

for ($i = 0; $i < 3; $i++) {
    $email_server->recieveEmail("这是一封验证码邮件: " . rand(10000, 99999));
}

// [Phone]有新邮件啦：这是一封验证码邮件: 76884
// [Laptop]有新邮件啦：这是一封验证码邮件: 76884
// [Phone]有新邮件啦：这是一封验证码邮件: 72405
// [Laptop]有新邮件啦：这是一封验证码邮件: 72405
// [Phone]有新邮件啦：这是一封验证码邮件: 96268
// [Laptop]有新邮件啦：这是一封验证码邮件: 96268
