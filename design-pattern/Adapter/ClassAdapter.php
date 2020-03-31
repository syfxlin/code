<?php

/**
 * 类的适配器模式
 */

interface UsbA
{
    public function usingUsbA(): void;
}

class UsbC
{
    public function usingUsbC(): void
    {
        echo "这是 USB C 接口\n";
    }
}

/**
 * 适配器集成自 USB C，同时也实现了 USB A 的接口
 */
class Adapter extends UsbC implements UsbA
{
    public function usingUsbA(): void
    {
        $this->usingUsbC();
    }
}

$usba = new Adapter();

$usba->usingUsbA();

var_dump($usba instanceof UsbA);
var_dump($usba instanceof UsbC);

// 这是 USB C 接口
// F:\Code\code\design-pattern\Adapter\ClassAdapter.php:35:
// bool(true)
// F:\Code\code\design-pattern\Adapter\ClassAdapter.php:36:
// bool(true)
