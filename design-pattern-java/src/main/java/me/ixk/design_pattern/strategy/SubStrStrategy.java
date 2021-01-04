package me.ixk.design_pattern.strategy;

/**
 * @author Otstar Lin
 * @date 2021/1/4 下午 6:33
 */
public class SubStrStrategy implements Strategy {

    @Override
    public String handle(Context context) {
        return context.getString().substring(5);
    }
}
