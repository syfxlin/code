package me.ixk.design_pattern.command;

/**
 * @author Otstar Lin
 * @date 2021/1/11 下午 10:30
 */
public class MarkdownEditor implements Editor {

    @Override
    public String copy() {
        return "复制[Markdown编辑器]";
    }

    @Override
    public String paste() {
        return "粘贴[Markdown编辑器]";
    }

    @Override
    public String cut() {
        return "剪切[Markdown编辑器]";
    }
}
