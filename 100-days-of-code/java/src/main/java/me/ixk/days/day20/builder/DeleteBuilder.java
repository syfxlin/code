package me.ixk.days.day20.builder;

import me.ixk.days.day20.utils.DbUtils;

public class DeleteBuilder<E> extends SqlBuilder<E> {

    public DeleteBuilder(Class<E> entity) {
        super(entity);
    }

    @Override
    public boolean save() {
        return DbUtils.update(this.makeSql(), this.makeValues());
    }

    @Override
    protected String makeSql() {
        StringBuilder sql = new StringBuilder();
        sql
            .append("DELETE FROM ")
            .append(this.getTableName())
            .append(" ")
            .append(this.makeWhere());
        return sql.toString();
    }

    @Override
    protected Object[] makeValues() {
        return this.values.toArray();
    }
}
