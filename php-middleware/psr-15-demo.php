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
    return $next->handle($request);
};

// 闭包中间件
$text = "Closure\n";
$middlewares[] = function ($request, $next) use ($text) {
    echo $text;
    return $next->handle($request);
};

$runner = new Runner(array_merge($middlewares, [$app]));

$reponse = $runner->handle($request);

// 由于前面使用了echo，所以使用echo代替了emit
echo $reponse->getBody();
