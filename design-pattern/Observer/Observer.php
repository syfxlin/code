<?php

/**
 * 观察者模式 - 响应模式
 * 和订阅/发布模式类似，只是使用 __set __get 魔术方法代替 setState 和 getState
 */

interface Observer
{
    public function change($change, Observable $observable): void;
}

interface Observable
{
    public function attach(Observer $observer): bool;
    public function detach(Observer $observer): bool;
    public function notify($change): void;
    // __construct($data)
    // __get()
    // __set()
}

class EmailServer implements Observable
{
    private $data = [];

    private $observer = [];

    public function attach(Observer $observer): bool
    {
        if (in_array($observer, $this->observer)) {
            return false;
        }
        $this->observer[] = $observer;
        return true;
    }

    public function detach(Observer $observer): bool
    {
        $index = array_search($observer, $this->observer);
        if ($index === false) {
            return false;
        }
        array_splice($this->observer, $index, 1);
        return true;
    }

    public function notify($change): void
    {
        foreach ($this->observer as $obs) {
            $obs->change($change, $this);
        }
    }

    public function __set($name, $value)
    {
        if ($name === "_") {
            $this->data = $value;
            $this->notify($value);
            return;
        }
        $result = &$this->data;
        $names = explode("_", $name);
        $last_name = array_pop($names);
        foreach ($names as $key) {
            if ($key === "") {
                continue;
            } else if (!isset($result) || is_null($result)) {
                throw new RuntimeException("上级变量为 null");
            } else if (is_array($result)) {
                $result = &$result[$key];
            } else if (is_object($result)) {
                $result = &$result->$key;
            } else {
                throw new RuntimeException("当前类型不为 object 或 array");
            }
        }
        if (is_array($result)) {
            $result[$last_name] = $value;
            $this->notify($value);
        } else if (is_object($result)) {
            $result->$last_name = $value;
            $this->notify($value);
        } else {
            throw new RuntimeException("上级变量为 null");
        }
    }

    public function __get($name)
    {
        $result = $this->data;
        foreach (explode("_", $name) as $key) {
            if ($key === "") {
                continue;
            } else if (!isset($result) || is_null($result)) {
                throw new RuntimeException("上级变量为 null");
            } else if (is_array($result)) {
                $result = $result[$key];
            } else if (is_object($result)) {
                $result = $result->$key;
            }
        }
        return $result;
    }

    public function recieveEmail($email)
    {
        $index = count($this->_);
        $name = "_$index";
        $this->$name = $email;
    }
}

class Notice implements Observer
{
    public $name = "";

    public function __construct($name)
    {
        $this->name = $name;
    }

    public function change($change, Observable $observable): void
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

// [Phone]有新邮件啦：这是一封验证码邮件: 39352
// [Laptop]有新邮件啦：这是一封验证码邮件: 39352
// [Phone]有新邮件啦：这是一封验证码邮件: 69310
// [Laptop]有新邮件啦：这是一封验证码邮件: 69310
// [Phone]有新邮件啦：这是一封验证码邮件: 51506
// [Laptop]有新邮件啦：这是一封验证码邮件: 51506
