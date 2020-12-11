package me.ixk.days.day20;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import java.util.List;
import me.ixk.days.day20.entity.UserEntity;
import me.ixk.days.day20.mapper.UserMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/11 下午 8:05
 */
class Day20Test {

    static DB db;

    @BeforeAll
    public static void beforeAll() throws ManagedProcessException {
        db = DB.newEmbeddedDB(3307);
        db.start();
        db.createDB("schema");
        db.source("day20/schema.sql", "syfxlin", "123456", "schema");
    }

    @AfterAll
    public static void afterAll() throws ManagedProcessException {
        db.stop();
    }

    UserMapper mapper = new UserMapper();

    @Test
    void selectAll() {
        List<UserEntity> list = this.mapper.query().list();
        for (UserEntity entity : list) {
            System.out.println(entity);
        }
    }

    @Test
    void select() {
        UserEntity entity =
            this.mapper.query()
                .eq("username", "lisi")
                .eq("password", "222")
                .one();
        System.out.println(entity);
    }

    @Test
    void deleteAndUpdateAndInsert() {
        System.out.println("Insert");
        this.mapper.insert()
            .insert(
                UserEntity
                    .builder()
                    .username("syfxlin")
                    .password("123456")
                    .realname("Otstar Lin")
                    .age(21)
                    .mobile("123456")
                    .build()
            )
            .save();
        System.out.println(this.mapper.query().eq("username", "syfxlin").one());
        System.out.println("Update");
        this.mapper.update()
            .set("password", "1234")
            .eq("username", "syfxlin")
            .save();
        System.out.println(this.mapper.query().eq("username", "syfxlin").one());
        System.out.println("Delete");
        this.mapper.delete().eq("username", "syfxlin").save();
        System.out.println(this.mapper.query().eq("username", "syfxlin").one());
    }
}
