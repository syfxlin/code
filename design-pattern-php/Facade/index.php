<?php

/**
 * 门面模式
 * 
 * 门面应该是留有统一的方法调用，不需要了解内部实现，为子系统提供一组接口
 * 提供统一的高级接口，这样就不需要了解门面内部的实现原理
 * Laravel 框架向外暴露的接口就是使用门面模式实现的
 */

require_once __DIR__ . "/vendor/autoload.php";

use Facades\Hash;
use Facades\Crypt;

var_dump(Hash::make("123"));
var_dump(Crypt::encrypt("123"));

// F:\Code\code\design-pattern\Facade\index.php:7:
// string(60) "$2y$10$7eBA2dqrbz6sAPzK..."
// F:\Code\code\design-pattern\Facade\index.php:8:
// string(188) "eyJpdiI6IlYveEpKY25mTl..."
