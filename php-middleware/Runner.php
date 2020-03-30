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
        $this->middlewares = array_map(function ($middleware) {
            if (is_string($middleware)) {
                return new $middleware;
            } else {
                return $middleware;
            }
        }, $middlewares);
    }

    public function handle(ServerRequestInterface $request): ResponseInterface
    {
        $middleware = array_shift($this->middlewares);
        if (is_callable($middleware)) {
            return $middleware($request, $this);
        }
        if ($middleware instanceof MiddlewareInterface) {
            return $middleware->process($request, $this);
        }
        if ($middleware instanceof RequestHandlerInterface) {
            return $middleware->handle($request);
        }
    }

    public function __invoke(ServerRequestInterface $request): ResponseInterface
    {
        return $this->handle($request);
    }
}
