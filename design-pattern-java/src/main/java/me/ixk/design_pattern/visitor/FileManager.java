package me.ixk.design_pattern.visitor;

/**
 * @author Otstar Lin
 * @date 2021/1/8 下午 7:43
 */
public class FileManager implements Visitor {

    @Override
    public String visit(final Component component) {
        if (component instanceof PdfFile) {
            return "View PDF file";
        } else {
            return "View Word file";
        }
    }
}
