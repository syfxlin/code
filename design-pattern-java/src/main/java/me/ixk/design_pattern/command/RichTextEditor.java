package me.ixk.design_pattern.command;

/**
 * @author Otstar Lin
 * @date 2021/1/11 下午 10:29
 */
public class RichTextEditor implements Editor {

    @Override
    public String copy() {
        return "复制[富文本编辑器]";
    }

    @Override
    public String paste() {
        return "粘贴[富文本编辑器]";
    }

    @Override
    public String cut() {
        return "剪切[富文本编辑器]";
    }
}
