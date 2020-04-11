<?php

/**
 * 命令模式
 * 
 * 命令模式是对命令进行封装，将发出命令的责任和执行命令的责任分割开。
 * 降低系统的耦合度。请求者与接收者之间通过命令引用，两者都保留了良好的独立性
 * 相同的请求者可以对应不同的接收者
 * 相同的接收者也可以供不同的请求者使用
 */

// Receiver 命令的接受者，也是执行者
interface Editor
{
    public function copy(): void;
    public function parse(): void;
    public function cut(): void;
}

// ConcreteReceiver 实现了接收者的具体接受者
class RichTextEditor implements Editor
{
    public $name = "富文本编辑器";

    public function copy(): void
    {
        echo "复制[$this->name]\n";
    }

    public function parse(): void
    {
        echo "粘贴[$this->name]\n";
    }

    public function cut(): void
    {
        echo "剪切[$this->name]\n";
    }
}

class MarkdownEditor implements Editor
{
    public $name = "Markdown 编辑器";

    public function copy(): void
    {
        echo "复制[$this->name]\n";
    }

    public function parse(): void
    {
        echo "粘贴[$this->name]\n";
    }

    public function cut(): void
    {
        echo "剪切[$this->name]\n";
    }
}

// Command 命令接口
interface Command
{
    public function exec(): void;
}

// ConcreteCommand 具体的命令，调用者可以通过调用不同的命令来达到不同的作用
class CopyCommand implements Command
{
    public $editor;

    public function __construct(Editor $editor)
    {
        $this->editor = $editor;
    }

    public function exec(): void
    {
        $this->editor->copy();
    }
}

class ParseCommand implements Command
{
    public $editor;

    public function __construct(Editor $editor)
    {
        $this->editor = $editor;
    }

    public function exec(): void
    {
        $this->editor->parse();
    }
}

class CutCommand implements Command
{
    public $editor;

    public function __construct(Editor $editor)
    {
        $this->editor = $editor;
    }

    public function exec(): void
    {
        $this->editor->cut();
    }
}

// Invoker 调用者
class Invoker
{
    public $command;

    public function __construct(Command $command)
    {
        $this->command = $command;
    }

    public function setCommand(Command $command)
    {
        $this->command = $command;
    }

    public function action(): void
    {
        $this->command->exec();
    }

    public static function execCommand(Command $command): void
    {
        $command->exec();
    }
}

// Client
$md_editor = new MarkdownEditor();
$rich_editor = new RichTextEditor();
Invoker::execCommand(new CopyCommand($md_editor)); // 复制[Markdown 编辑器]
Invoker::execCommand(new ParseCommand($rich_editor)); // 粘贴[富文本编辑器]
