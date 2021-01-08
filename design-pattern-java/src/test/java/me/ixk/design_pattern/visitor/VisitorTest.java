package me.ixk.design_pattern.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/8 下午 7:45
 */
class VisitorTest {

    @Test
    void visitor() {
        final FileManager manager = new FileManager();
        final Printer printer = new Printer();
        final PdfFile pdfFile = new PdfFile();
        final WordFile wordFile = new WordFile();
        assertEquals("View PDF file", pdfFile.accept(manager));
        assertEquals("Print Word file", wordFile.accept(printer));
    }
}
