package me.ixk.days.day20.builder;

import java.util.ArrayList;
import lombok.Data;

@Data
public class Where<E> {

    public static final String PLACEHOLDER = "#{COLUMN}";

    public static final String AND = "AND ";

    public static final String OR = "OR ";

    private SqlBuilder<E> builder;

    private String sql;

    private String column;

    private String connect = AND;

    private Object value;

    private boolean hasValue;

    public Where(SqlBuilder<E> builder, String column, String sql) {
        this.builder = builder;
        this.column = column;
        this.sql = sql;
        this.hasValue = false;
        this.value = new ArrayList<>();
    }

    public Where(
        SqlBuilder<E> builder,
        String column,
        String sql,
        Object value
    ) {
        this(builder, column, sql);
        this.value = value;
        this.hasValue = true;
    }
}
