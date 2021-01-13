package me.ixk.design_pattern.mediator;

/**
 * @author Otstar Lin
 * @date 2021/1/13 下午 10:34
 */
public class Button implements Component {

    public String click(final int click) {
        return "click" + click;
    }
}
