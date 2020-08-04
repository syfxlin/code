<?php

namespace Facades;

class Crypt extends Facade
{
    protected static $class = \Utils\Crypt::class;

    public static function getArgs()
    {
        return [
            base64_decode("iLSnBejAN/ndiydlMpOh/hj1q/8kgSLcnLz3td4HZcg=")
        ];
    }
}
