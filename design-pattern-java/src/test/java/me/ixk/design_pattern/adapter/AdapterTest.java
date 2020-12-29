package me.ixk.design_pattern.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.ixk.design_pattern.adapter.class_adapter.Adapter;
import me.ixk.design_pattern.adapter.object_adapter.UsbC;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/29 下午 2:33
 */
class AdapterTest {

    @Test
    void classAdapter() {
        Adapter adapter = new Adapter();
        assertEquals("这是 USB C 接口", adapter.usingUsbA());
    }

    @Test
    void objectAdapter() {
        me.ixk.design_pattern.adapter.object_adapter.Adapter adapter = new me.ixk.design_pattern.adapter.object_adapter.Adapter(
            new UsbC()
        );
        assertEquals("这是 USB C 接口", adapter.usingUsbA());
    }
}
