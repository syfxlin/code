<?php

/**
 * 对象的适配器模式
 */

/**
 * 要使用的设备是 USB C 的
 */
class UsbC
{
    public function usingUsbC()
    {
        echo "这是 USB C 接口\n";
    }
}

class UsbA
{
    public function usingUsbA()
    {
        echo "这是 USB A 接口\n";
    }
}

/**
 * 适配器，适配器继承自 USB A，将 USB C 的功能转成 USB A 的功能
 */
class Adapter extends UsbA
{
    private $usbcDevice;

    public function __construct(UsbC $usbcDevice)
    {
        $this->usbcDevice = $usbcDevice;
    }

    public function usingUsbA()
    {
        $this->usbcDevice->usingUsbC();
    }
}

// UsbA $usba = new Adapter(new UsbC()); 就是说类型是 UsbA，但是可以使用 UsbC 的功能
$usba = new Adapter(new UsbC());

$usba->usingUsbA();

var_dump($usba instanceof UsbA);
var_dump($usba instanceof UsbC);

// 这是 USB C 接口
// F:\Code\code\design-pattern\Adapter\ObjectAdapter.php:46:
// bool(true)
// F:\Code\code\design-pattern\Adapter\ObjectAdapter.php:47:
// bool(false)
