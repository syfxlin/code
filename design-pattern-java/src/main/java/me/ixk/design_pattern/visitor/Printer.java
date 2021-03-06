package me.ixk.design_pattern.visitor;

/**
 * @author Otstar Lin
 * @date 2021/1/8 下午 7:43
 */
public class Printer implements Visitor {

    @Override
    public String visit(final Component component) {
        if (component instanceof PdfFile) {
            return "Print PDF file";
        } else {
            return "Print Word file";
        }
    }
}
