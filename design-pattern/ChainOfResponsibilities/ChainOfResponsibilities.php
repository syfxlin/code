<?php

/**
 * 责任链模式
 * 
 * 按责任链顺序调用，当一个处理器无法处理的时候就调用下一个处理器处理
 */

abstract class Handler
{

    /**
     * @var Handler
     */
    protected $next;

    public function __construct(Handler $handler = null)
    {
        $this->next = $handler;
    }

    public final function handle($data)
    {
        $result = $this->process($data);
        if ($result === null && $this->next !== null) {
            $result = $this->next->handle($data);
        }
        return $result;
    }

    abstract public function process($data);
}

class CpuCache extends Handler
{
    public function process($data)
    {
        if ($data === 'cpu') {
            return "来自处理器缓存\n";
        }
        return null;
    }
}

class MemoryCache extends Handler
{
    public function process($data)
    {
        if ($data === 'memory') {
            return "来自内存缓存\n";
        }
        return null;
    }
}

class DiskCache extends Handler
{
    public function process($data)
    {
        if ($data === 'disk') {
            return "来自磁盘缓存\n";
        }
        return null;
    }
}

$handler = (new CpuCache(new MemoryCache(new DiskCache())));
echo $handler->handle('cpu');
echo $handler->handle('memory');
echo $handler->handle('disk');
// 来自处理器缓存
// 来自内存缓存
// 来自磁盘缓存
