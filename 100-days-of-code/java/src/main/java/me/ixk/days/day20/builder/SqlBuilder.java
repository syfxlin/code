package me.ixk.days.day20.builder;

import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Table;

public abstract class SqlBuilder<E> {

    protected List<Object> values = new ArrayList<>();

    protected List<Where<E>> wheres = new ArrayList<>();

    protected Class<E> entity;

    protected List<String> columns = new ArrayList<>();

    public SqlBuilder(Class<E> entity) {
        this.entity = entity;
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            this.columns.add(field.getName());
        }
    }

    public Class<E> getEntity() {
        return this.entity;
    }

    public String getTableName() {
        Table table = this.entity.getAnnotation(Table.class);
        String name;
        if (table == null) {
            name = StrUtil.toUnderlineCase(this.entity.getSimpleName());
        } else {
            name = table.name();
        }
        return "`" + name + "`";
    }

    public String getColumnName(String name) {
        try {
            Column column =
                this.getEntity()
                    .getDeclaredField(name)
                    .getAnnotation(Column.class);
            if (column != null) {
                name = column.name();
            }
        } catch (NoSuchFieldException e) {
            // no code
        }
        return "`" + StrUtil.toUnderlineCase(name) + "`";
    }

    public List<String> getColumnNames() {
        return this.columns.stream()
            .map(this::getColumnName)
            .collect(Collectors.toList());
    }

    public List<String> getOriginColumnNames() {
        return this.columns;
    }

    protected final void isInTable(final String name) {
        if (!this.columns.contains(name)) {
            throw new RuntimeException(
                "字段 \"" +
                name +
                "\" 不存在 \"" +
                this.getTableName() +
                "\" 表中"
            );
        }
    }

    protected final void addWhere(Where<E> where) {
        if (where.isHasValue()) {
            this.values.add(where.getValue());
        }
        this.wheres.add(where);
    }

    public SqlBuilder<E> eq(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " = ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> ne(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " != ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> not(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " <> ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> isNotNull(final String columnName) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " IS NOT NULL "
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> isNull(final String columnName) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " IS NULL "
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> gt(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " > ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> ge(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " >= ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> lt(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " < ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> le(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " <= ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> like(final String columnName, final Object value) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " LIKE ? ",
            value
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> betweenAnd(
        final String columnName,
        final Object value1st,
        final Object value2nd
    ) {
        Where<E> where = new Where<>(
            this,
            columnName,
            Where.PLACEHOLDER + " BETWEEN ? AND ? ",
            new Object[] { value1st, value2nd }
        );
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> in(final String columnName, final Object[] values) {
        Object[] sqlVal = values;
        if (sqlVal.length == 0) {
            sqlVal = new Object[] { null };
        }
        String inSql =
            Where.PLACEHOLDER +
            " IN ( " +
            StrUtil.repeatAndJoin("?", sqlVal.length, ", ") +
            " ) ";
        Where<E> where = new Where<>(this, columnName, inSql, sqlVal);
        this.addWhere(where);
        return this;
    }

    public SqlBuilder<E> or() {
        this.wheres.get(this.wheres.size() - 1).setConnect(Where.OR);
        return this;
    }

    public SqlBuilder<E> and() {
        this.wheres.get(this.wheres.size() - 1).setConnect(Where.AND);
        return this;
    }

    protected final String makeWhere() {
        StringBuilder sql = new StringBuilder();
        if (wheres.size() != 0) {
            sql.append(" WHERE ");
            for (int i = 0; i < wheres.size(); i++) {
                Where<E> where = wheres.get(i);
                if (i != 0) {
                    sql.append(where.getConnect());
                }
                String columnName = this.getColumnName(where.getColumn());
                String realSql = where
                    .getSql()
                    .replace(Where.PLACEHOLDER, columnName);
                sql.append(realSql);
            }
        }
        return sql.toString();
    }

    protected abstract String makeSql();

    protected abstract Object[] makeValues();

    public boolean save() {
        throw new RuntimeException("不支持的操作");
    }

    public E one() {
        throw new RuntimeException("不支持的操作");
    }

    public List<E> list() {
        throw new RuntimeException("不支持的操作");
    }

    public String toSql() {
        return this.makeSql();
    }

    @Override
    public String toString() {
        return this.toSql();
    }

    public Object[] getValues() {
        return this.makeValues();
    }
}
