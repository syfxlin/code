package me.ixk.design_pattern.command;

/**
 * @author Otstar Lin
 * @date 2021/1/11 下午 10:32
 */
public class PasteCommand implements Command {

    private final Editor editor;

    public PasteCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String exec() {
        return editor.paste();
    }
}
