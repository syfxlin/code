package me.ixk.days.day20.builder;

import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import me.ixk.days.day20.utils.DbUtils;

public class InsertBuilder<E> extends SqlBuilder<E> {

    protected Object insertEntity = new ArrayList<>();

    public InsertBuilder(Class<E> entity) {
        super(entity);
    }

    public InsertBuilder<E> insert(Object entity) {
        if (!this.getEntity().isInstance(entity)) {
            throw new RuntimeException("Entity 类型不匹配");
        }
        this.insertEntity = entity;
        return this;
    }

    @Override
    public boolean save() {
        return DbUtils.update(this.makeSql(), this.makeValues());
    }

    @Override
    protected String makeSql() {
        List<String> columnNames = this.getColumnNames();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(this.getTableName());
        builder.append(" ( ");
        builder.append(StrUtil.join(", ", columnNames));
        builder.append(" ) ");
        builder.append("VALUES");
        builder.append(" ( ");
        builder.append(StrUtil.repeatAndJoin("?", columnNames.size(), ", "));
        builder.append(" )");
        builder.append(";");
        return builder.toString();
    }

    @Override
    protected Object[] makeValues() {
        List<String> columns = this.getOriginColumnNames();
        Object[] objects = new Object[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            try {
                Field field = this.getEntity().getDeclaredField(columns.get(i));
                boolean acc = field.canAccess(this.insertEntity);
                field.setAccessible(true);
                objects[i] = field.get(this.insertEntity);
                field.setAccessible(acc);
            } catch (Exception e) {
                objects[i] = null;
            }
        }
        return objects;
    }

    @Override
    public InsertBuilder<E> eq(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> ne(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> not(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> isNotNull(String columnName) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> isNull(String columnName) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> gt(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> ge(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> lt(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> le(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> like(String columnName, Object value) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> betweenAnd(
        String columnName,
        Object value1st,
        Object value2nd
    ) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public InsertBuilder<E> in(String columnName, Object[] values) {
        throw new RuntimeException("不支持的操作");
    }
}
