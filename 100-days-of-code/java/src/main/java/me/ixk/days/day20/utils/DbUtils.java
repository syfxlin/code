package me.ixk.days.day20.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import me.ixk.days.day20.exceptions.DbException;

public abstract class DbUtils {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        Properties properties = Config.getProperties();
        hikariConfig.setDriverClassName(properties.getProperty("jdbc.driver"));
        hikariConfig.setJdbcUrl(properties.getProperty("jdbc.url"));
        hikariConfig.setUsername(properties.getProperty("jdbc.username"));
        hikariConfig.setPassword(properties.getProperty("jdbc.password"));
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static <T> T one(String sql, Class<T> type, Object... params) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return BeanUtil.fillBean(
                    type.getConstructor().newInstance(),
                    new ValueProvider<>() {
                        @Override
                        public Object value(String key, Type valueType) {
                            try {
                                return resultSet.getObject(
                                    StrUtil.toUnderlineCase(key)
                                );
                            } catch (SQLException e) {
                                return null;
                            }
                        }

                        @Override
                        public boolean containsKey(String key) {
                            try {
                                return (
                                    resultSet.getObject(
                                        StrUtil.toUnderlineCase(key)
                                    ) !=
                                    null
                                );
                            } catch (SQLException e) {
                                return false;
                            }
                        }
                    },
                    new CopyOptions()
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public static <T> List<T> list(
        String sql,
        Class<T> type,
        Object... params
    ) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            List<T> list = new LinkedList<>();
            while (resultSet.next()) {
                list.add(
                    BeanUtil.fillBean(
                        type.getConstructor().newInstance(),
                        new ValueProvider<>() {
                            @Override
                            public Object value(String key, Type valueType) {
                                try {
                                    return resultSet.getObject(
                                        StrUtil.toUnderlineCase(key)
                                    );
                                } catch (SQLException e) {
                                    return null;
                                }
                            }

                            @Override
                            public boolean containsKey(String key) {
                                try {
                                    return (
                                        resultSet.getObject(
                                            StrUtil.toUnderlineCase(key)
                                        ) !=
                                        null
                                    );
                                } catch (SQLException e) {
                                    return false;
                                }
                            }
                        },
                        new CopyOptions()
                    )
                );
            }
            return list;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public static boolean update(String sql, Object... params) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement.execute();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }
}
