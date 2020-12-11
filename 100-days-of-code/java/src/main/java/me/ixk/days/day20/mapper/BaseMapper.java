package me.ixk.days.day20.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import javax.persistence.Id;
import me.ixk.days.day20.builder.DeleteBuilder;
import me.ixk.days.day20.builder.InsertBuilder;
import me.ixk.days.day20.builder.QueryBuilder;
import me.ixk.days.day20.builder.UpdateBuilder;

public abstract class BaseMapper<K, E> {

    protected String tableName;

    protected String idName;

    protected Class<E> entityType;

    public BaseMapper() {
        this.idName = this.getIdName();
        this.entityType = this.getEntityType();
    }

    protected String getIdName() {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                return field.getName();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected Class<E> getEntityType() {
        return (Class<E>) (
            (ParameterizedType) this.getClass().getGenericSuperclass()
        ).getActualTypeArguments()[1];
    }

    public QueryBuilder<E> query() {
        return new QueryBuilder<>(this.entityType);
    }

    public DeleteBuilder<E> delete() {
        return new DeleteBuilder<>(this.entityType);
    }

    public InsertBuilder<E> insert() {
        return new InsertBuilder<>(this.entityType);
    }

    public UpdateBuilder<E> update() {
        return new UpdateBuilder<>(this.entityType);
    }
}
