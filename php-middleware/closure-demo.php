<?php
// 中间件
class Authenticate
{
    public function handle($request, Closure $next)
    {
        echo "Start 登录\n";
        $response = $next($request);
        echo "End 登录\n";
        return $response;
    }
}

class SimpleMiddleware
{
    public function handle($request, Closure $next)
    {
        echo "Start SimpleMiddleware\n";
        $response = $next($request);
        echo "End SimpleMiddleware\n";
        return $response;
    }
}

// App
class App
{
    public function run($request)
    {
        return "App-$request\n";
    }
}

// 中间件处理器
$middlewares = [
    Authenticate::class,
    SimpleMiddleware::class
];

$next = function ($request) {
    return (new App)->run($request);
};

foreach ($middlewares as $middleware) {
    $next = function ($request) use ($next, $middleware) {
        return (new $middleware)->handle($request, $next);
    };
}

$response = $next("request");

echo $response;
