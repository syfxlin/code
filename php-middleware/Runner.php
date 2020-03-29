<?php

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\MiddlewareInterface;
use Psr\Http\Server\RequestHandlerInterface;

class Runner implements RequestHandlerInterface
{
    public $middleware;
    public $next;
    public function __construct($middleware, $next)
    {
        $this->middleware = $middleware;
        $this->next = $next;
    }
    public function handle(ServerRequestInterface $request): ResponseInterface
    {
        $next = $this->next;
        $middleware = $this->middleware;
        if (is_callable($middleware)) {
            return $middleware($request, $next);
        }
        $middleware = new $middleware;
        if ($middleware instanceof MiddlewareInterface) {
            return $middleware->process($request, $next);
        }
        if ($middleware instanceof RequestHandlerInterface) {
            return $middleware->handle($request, $next);
        }
    }
}
