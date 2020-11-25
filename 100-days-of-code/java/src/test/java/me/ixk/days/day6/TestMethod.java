package me.ixk.days.day6;

import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day6.annotation.Log;

/**
 * @author Otstar Lin
 * @date 2020/11/25 下午 7:14
 */
@Slf4j
public class TestMethod {

    @Log
    public String index() {
        log.info("Method-Log");
        return "Method";
    }
}
