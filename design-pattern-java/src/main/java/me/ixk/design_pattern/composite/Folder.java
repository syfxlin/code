package me.ixk.design_pattern.composite;

/**
 * @author Otstar Lin
 * @date 2020/12/31 下午 1:00
 */
public class Folder implements Component {

    private final String name;
    private final Component[] subs;

    public Folder(final String name, final Component[] subs) {
        this.name = name;
        this.subs = subs;
    }

    @Override
    public String getName() {
        return "folder-" + this.name;
    }

    @Override
    public Component[] getSubs() {
        return this.subs;
    }
}
