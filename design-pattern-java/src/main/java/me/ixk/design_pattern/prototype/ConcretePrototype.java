package me.ixk.design_pattern.prototype;

/**
 * 具体原型类
 *
 * @author Otstar Lin
 * @date 2020/12/25 上午 8:47
 */
public class ConcretePrototype extends Prototype {

    private String data;

    public ConcretePrototype() {}

    public ConcretePrototype(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    @Override
    public Object clone() {
        return new ConcretePrototype(this.data);
    }
}
