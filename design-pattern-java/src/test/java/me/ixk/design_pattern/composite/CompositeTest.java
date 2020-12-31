package me.ixk.design_pattern.composite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/31 下午 1:04
 */
class CompositeTest {

    @Test
    void composite() {
        final Component folder = new Folder(
            "folder1",
            new Component[] {
                new File("file1"),
                new Folder("folder2", new Component[0]),
            }
        );
        assertEquals("folder-folder1", folder.getName());
        assertEquals("file-file1", folder.getSubs()[0].getName());
        assertEquals(0, folder.getSubs()[1].getSubs().length);
    }
}
