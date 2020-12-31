package me.ixk.design_pattern.composite;

/**
 * @author Otstar Lin
 * @date 2020/12/31 下午 12:59
 */
public class File implements Component {

    private final String name;

    public File(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return "file-" + this.name;
    }

    @Override
    public Component[] getSubs() {
        return new Component[0];
    }
}
