<?php
require_once __DIR__ . "/vendor/autoload.php";
require_once __DIR__ . "/Runner.php";
require_once __DIR__ . "/App.php";

use Laminas\Diactoros\ServerRequestFactory;

$middlewares = [
    \Middleware\Middleware1::class,
    \Middleware\Middleware2::class
];

$request = ServerRequestFactory::fromGlobals();

// App
$app = function ($request) {
    return (new App)->run($request);
};

// 函数中间件
$middlewares[] = function ($request, $next) {
    echo "Callable\n";
    // 默认的方式
    return $next->handle($request);
};

// 闭包中间件
$text = "Closure\n";
$middlewares[] = function ($request, $next) use ($text) {
    echo $text;
    // 因为添加了 __invoke 所以也可以像 Laravel 闭包中间件一样直接调用
    return $next($request);
};

$runner = new Runner(array_merge($middlewares, [$app]));

$reponse = $runner($request);

// 由于前面使用了echo，所以使用echo代替了emit
echo $reponse->getBody();
