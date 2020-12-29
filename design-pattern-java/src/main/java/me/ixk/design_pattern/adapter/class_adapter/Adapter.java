package me.ixk.design_pattern.adapter.class_adapter;

/**
 * @author Otstar Lin
 * @date 2020/12/29 下午 2:32
 */
public class Adapter extends UsbC implements UsbA {

    @Override
    public String usingUsbA() {
        return this.usingUsbC();
    }
}
