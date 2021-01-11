package me.ixk.design_pattern.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/11 下午 10:34
 */
class CommandTest {

    @Test
    void command() {
        RichTextEditor richTextEditor = new RichTextEditor();
        MarkdownEditor markdownEditor = new MarkdownEditor();
        assertEquals(
            "复制[富文本编辑器]",
            new CopyCommand(richTextEditor).exec()
        );
        assertEquals(
            "剪切[Markdown编辑器]",
            new CutCommand(markdownEditor).exec()
        );
    }
}
