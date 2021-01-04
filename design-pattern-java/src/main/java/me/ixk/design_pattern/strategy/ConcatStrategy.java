package me.ixk.design_pattern.strategy;

/**
 * @author Otstar Lin
 * @date 2021/1/4 下午 6:34
 */
public class ConcatStrategy implements Strategy {

    @Override
    public String handle(final Context context) {
        return context.getString() + " concat";
    }
}
