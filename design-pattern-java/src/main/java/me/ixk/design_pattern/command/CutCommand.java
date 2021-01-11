package me.ixk.design_pattern.command;

/**
 * @author Otstar Lin
 * @date 2021/1/11 下午 10:33
 */
public class CutCommand implements Command {

    private final Editor editor;

    public CutCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String exec() {
        return editor.cut();
    }
}
