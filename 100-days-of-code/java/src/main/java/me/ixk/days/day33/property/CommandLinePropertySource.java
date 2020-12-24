/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

import java.util.HashMap;
import java.util.Map;
import me.ixk.days.day33.exceptions.LoadEnvironmentFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令行配置数据源
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 9:15
 */
public class CommandLinePropertySource extends MapPropertySource<Object> {

    public static final String COMMAND_LINE_START = "--";
    public static final String COMMAND_LINE_KV_SPLIT = "=";
    private static final Logger log = LoggerFactory.getLogger(
        CommandLinePropertySource.class
    );

    public CommandLinePropertySource(String name, String[] source) {
        super(name, parseProperties(source));
    }

    private static Map<String, Object> parseProperties(String[] source) {
        final Map<String, Object> properties = new HashMap<>(source.length);
        for (String arg : source) {
            if (
                !arg.startsWith(COMMAND_LINE_START) ||
                !arg.contains(COMMAND_LINE_KV_SPLIT)
            ) {
                log.error("Incorrect command parameter format");
                throw new LoadEnvironmentFileException(
                    "Incorrect command parameter format"
                );
            }
            final String[] kv = arg.substring(2).split("=");
            properties.put(kv[0], kv[1]);
        }
        return properties;
    }
}
