package me.ixk.days.day27;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

/**
 * 生吞异常样例
 *
 * @author Otstar Lin
 * @date 2020/12/18 下午 3:44
 */
@Slf4j
public class SqlUtil {

    public static Connection error() {
        try {
            // 特意拼写错误
            Class.forName("com.mysql.cj.jdbc.Driverr");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai",
                "syfxlin",
                "123456"
            );
        } catch (ClassNotFoundException | SQLException e) {
            // 不对异常进行处理，生吞了异常
            // 一旦发生异常，则难以定位

            // 使用 e.printStackTrace() 也不是好的选择，一旦有人修改了 System.out 的 PrintStream 或者在庞大的微服务中
            // 使用 pST 会难以判断日志输出的位置，对修复 Bug 非常不利
            // e.printStackTrace();
        }
        return null;
    }

    public static Connection right() {
        try {
            // 特意拼写错误
            Class.forName("com.mysql.cj.jdbc.Driverr");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai",
                "syfxlin",
                "123456"
            );
        } catch (ClassNotFoundException | SQLException e) {
            // 在发生异常时应按清空判断对异常进行处理，即使是为了把异常单纯的处理掉，也应该使用日志系统记录下这个异常
            log.error("Get connection failed", e);
            // 或者向上抛出，让上层进行处理
            // 需要注意的是我们在抛出新异常的时候，如果有老异常，那么应该要把老异常包装起来一起抛出，这样才不会丢失重要的信息，能更好的找到 Bug
            throw new IllegalStateException("Get connection failed", e);
        }
    }

    public static void finallyError(Connection connection) {
        try {
            // 进行一些操作
            throw new RuntimeException("Error");
        } catch (Exception e) {
            log.error("Operate connection failed", e);
            throw new IllegalStateException("Operate connection failed", e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException("Finally exception", e);
            }
        }
    }

    public static void finallyRight1(Connection connection) throws Exception {
        Exception ex = null;
        try {
            // 进行一些操作
            throw new RuntimeException("Error");
        } catch (Exception e) {
            log.error("Operate connection failed", e);
            ex = new IllegalStateException("Operate connection failed", e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e = new RuntimeException("Finally exception", e);
                if (ex == null) {
                    ex = e;
                } else {
                    // 将 finally 的异常附加到 catch 的异常
                    ex.addSuppressed(e);
                }
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

    public static void finallyRight2(Connection connection) {
        // 使用 try-with-resource 语句可以自动附加异常
        try (Connection c = connection) {
            throw new RuntimeException("Error");
        } catch (Exception e) {
            throw new IllegalStateException("Operate connection failed", e);
        }
    }
}
