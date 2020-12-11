package me.ixk.days.day20.builder;

import cn.hutool.core.util.StrUtil;
import java.util.List;
import me.ixk.days.day20.utils.DbUtils;

public class QueryBuilder<E> extends SqlBuilder<E> {

    enum Order {
        DESC,
        ASC,
    }

    private List<String> selectColumns = null;

    private String limitSql = "";
    private String orderBySql = "";
    private String groupBySql = "";

    public QueryBuilder(Class<E> entity) {
        super(entity);
    }

    public void qc(List<String> selectColumns) {
        //不为空，并且list中有值的时候
        if (selectColumns != null && selectColumns.size() != 0) {
            this.selectColumns = selectColumns;
        }
    }

    public QueryBuilder<E> orderBy(Order type, String... orderBy) {
        String[] columns = new String[orderBy.length];
        for (int i = 0; i < orderBy.length; i++) {
            columns[i] = this.getColumnName(orderBy[i]);
        }
        this.orderBySql =
            " ORDER BY " + StrUtil.join(", ", columns) + " " + type.name();
        return this;
    }

    public QueryBuilder<E> limit(int count) {
        this.limitSql = " LIMIT " + count;
        return this;
    }

    public QueryBuilder<E> limit(int line, int count) {
        this.limitSql = " LIMIT " + line + ", " + count;
        return this;
    }

    public QueryBuilder<E> groupBy(String column) {
        this.groupBySql = " GROUP BY " + this.getColumnName(column);
        return this;
    }

    public E one() {
        return DbUtils.one(this.makeSql(), this.getEntity(), this.makeValues());
    }

    @Override
    public List<E> list() {
        return DbUtils.list(
            this.makeSql(),
            this.getEntity(),
            this.makeValues()
        );
    }

    @Override
    protected String makeSql() {
        if (this.selectColumns == null) {
            this.selectColumns = this.getColumnNames();
        }
        StringBuilder sql = new StringBuilder();
        sql
            .append("SELECT ")
            .append(StrUtil.join(", ", this.selectColumns))
            .append(" FROM ")
            .append(this.getTableName())
            .append(this.makeWhere())
            .append(this.groupBySql)
            .append(this.orderBySql)
            .append(this.limitSql);
        return sql.toString();
    }

    @Override
    protected Object[] makeValues() {
        return this.values.toArray();
    }
}
