package me.ixk.days.day20.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import me.ixk.days.day20.utils.DbUtils;

public class UpdateBuilder<E> extends SqlBuilder<E> {

    private final List<String> updateColumn = new ArrayList<>();

    public UpdateBuilder(final Class<E> entity) {
        super(entity);
    }

    public UpdateBuilder<E> set(final String columnName, final Object value) {
        this.updateColumn.add(columnName);
        this.values.add(value);
        return this;
    }

    public UpdateBuilder<E> set(final Object entity) {
        return this.set(entity, true);
    }

    public UpdateBuilder<E> set(final Object entity, final boolean ignoreNull) {
        final List<String> columnNames = this.columns;
        final List<Object> values = new ArrayList<>();
        final Field[] fields = entity.getClass().getDeclaredFields();
        for (final Field field : fields) {
            final String columnName = field.getName();
            if (!columnNames.contains(columnName)) {
                continue;
            }
            final boolean acc = field.canAccess(entity);
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
            } catch (final IllegalAccessException e) {
                value = null;
            }
            field.setAccessible(acc);
            //如果class中的值是null，并且设置忽略null，跳过
            if (ignoreNull && value == null) {
                continue;
            }
            this.updateColumn.add(columnName);
            values.add(value);
        }
        this.values = values;
        return this;
    }

    @Override
    public boolean save() {
        return DbUtils.update(this.makeSql(), this.makeValues());
    }

    @Override
    protected String makeSql() {
        if (updateColumn.size() == 0) {
            throw new RuntimeException("没有需要更新的字段");
        }
        final StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(this.getTableName()).append(" ");
        sql.append("SET ");
        for (int i = 0; i < updateColumn.size(); i++) {
            final String column = this.getColumnName(updateColumn.get(i));
            if (i == 0) {
                sql.append(column).append(" = ?");
            } else {
                sql.append(", ").append(column).append(" = ?");
            }
        }
        sql.append(this.makeWhere());
        return sql.toString();
    }

    @Override
    protected Object[] makeValues() {
        return this.values.toArray();
    }
}
