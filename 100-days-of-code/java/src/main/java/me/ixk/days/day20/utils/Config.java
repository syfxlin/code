package me.ixk.days.day20.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Config {

    public static Properties getProperties() {
        return getProperties("/day20/application.properties");
    }

    public static Properties getProperties(final String path) {
        final Properties properties = new Properties();
        final InputStream stream = Config.class.getResourceAsStream(path);
        try {
            properties.load(stream);
            return properties;
        } catch (final IOException e) {
            return null;
        }
    }
}
