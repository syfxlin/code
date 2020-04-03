<?php

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\MiddlewareInterface;
use Psr\Http\Server\RequestHandlerInterface;

class Runner implements RequestHandlerInterface
{
    public $middlewares;

    public function __construct($middlewares)
    {
        // 实例化所有的中间件，若已经实例化或为可运行的中间件则不处理
        $this->middlewares = array_map(function ($middleware) {
            // 若是 string 就认为是类名这进行实例化
            if (is_string($middleware)) {
                return new $middleware;
            } else {
                return $middleware;
            }
        }, $middlewares);
    }

    public function handle(ServerRequestInterface $request): ResponseInterface
    {
        // 从中间件列表中取出一个中间件
        $middleware = array_shift($this->middlewares);
        /**
         * 可运行中间件和 PSR-15 中间件都可以传入 this
         * 即该中间件运行完毕后可以继续调用 Runner 的 handle 来重复处理
         * 即 $response = $next->handle($request);
         * 直到中间件都处理完毕为止
         */
        // 若是可运行的中间件就直接运行
        if (is_callable($middleware)) {
            return $middleware($request, $this);
        }
        // 若是 PSR-15 中间件则调用 process 方法
        if ($middleware instanceof MiddlewareInterface) {
            return $middleware->process($request, $this);
        }
        // 若是 PSR-15 请求处理器则调用 handle 方法，由于 handle 无法传入 this
        // 所以运行了 PSR-15 请求处理器后就无法在调用后续的中间件
        // 请求处理器应放到最后执行，并且需要最终的请求处理器来构造 Response，或者手动创建并返回 Response
        if ($middleware instanceof RequestHandlerInterface) {
            return $middleware->handle($request);
        }
        throw new RuntimeException("No final request handler (Must return ResponseInterface)");
    }

    public function __invoke(ServerRequestInterface $request): ResponseInterface
    {
        return $this->handle($request);
    }
}
