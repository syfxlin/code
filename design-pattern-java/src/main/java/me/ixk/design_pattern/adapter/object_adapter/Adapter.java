package me.ixk.design_pattern.adapter.object_adapter;

/**
 * @author Otstar Lin
 * @date 2020/12/29 下午 2:36
 */
public class Adapter extends UsbA {

    private final UsbC usbC;

    public Adapter(UsbC usbC) {
        this.usbC = usbC;
    }

    @Override
    public String usingUsbA() {
        return this.usbC.usingUsbC();
    }
}
