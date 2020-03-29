<?php

use Laminas\Diactoros\Response;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class App
{
    public function run(ServerRequestInterface $request): ResponseInterface
    {
        echo "App\n";
        $reponse = new Response();
        $reponse->getBody()->write('Response');
        return $reponse;
    }
}
