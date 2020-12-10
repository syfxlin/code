package me.ixk.days.day19;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 软件事务内存
 *
 * @author Otstar Lin
 * @date 2020/12/10 上午 10:37
 */
public class StmTransaction implements Transaction {

    /**
     * 事务版本号
     */
    private static final AtomicLong TRANSACTION_VERSION_SEQ = new AtomicLong(0);

    /**
     * 全局锁，用于事务提交时保证线程安全
     */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 当前事务版本号
     */
    private final long trVersion = TRANSACTION_VERSION_SEQ.incrementAndGet();
    /**
     * 事务变量存储，当查询或更新对象时，会存储该 Map 中
     * <p>
     * 动态快照机制，即查询和更新才会快照事务引用中存储的变量，事务内所有的更新和读取均使用该快照
     */
    private final Map<TransactionRef<?>, Object> inMap = new HashMap<>();
    /**
     * 需要更新的列表
     */
    private final Set<TransactionRef<?>> update = new HashSet<>();
    /**
     * 记录版本号
     */
    private final Map<TransactionRef<?>, Long> version = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final TransactionRef<T> ref) {
        // 判断是否已经快照
        if (!inMap.containsKey(ref)) {
            final VersionedRef<T> versionedRef = ref.getRef();
            // 不存在快照的时候则从事务引用中读取原始值（快照）
            inMap.put(ref, versionedRef.getValue());
            // 版本号不存在时则记录最初的版本号
            if (!version.containsKey(ref)) {
                version.put(ref, versionedRef.getVersion());
            }
        }
        return (T) inMap.get(ref);
    }

    @Override
    public <T> void set(final TransactionRef<T> ref, final T value) {
        // 更新的值是事务内的快照
        inMap.put(ref, value);
        // 如果不存在版本号则记录最初的版本号
        if (!version.containsKey(ref)) {
            version.put(ref, ref.getRef().getVersion());
        }
        // 添加到更新列表，用于事务提交时检查
        update.add(ref);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean commit() {
        // 事务提交时加锁，防止其他事务干扰
        LOCK.lock();
        try {
            boolean isValid = true;
            for (final TransactionRef<?> ref : inMap.keySet()) {
                // 检查事务引用在事务执行期间是否更新
                if (ref.getRef().getVersion() != version.get(ref)) {
                    // 如果事务期间事务引用被更改了，则提交失败
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                // 如果事务提交成功则更新事务引用
                for (final TransactionRef<?> ref : update) {
                    ref.setRef(new VersionedRef(this.get(ref), trVersion));
                }
            }
            return isValid;
        } finally {
            LOCK.unlock();
        }
    }

    public static void atomic(final Consumer<Transaction> consumer) {
        // 提交成功标志
        boolean committed = false;
        // 如果提交失败则重试，直到提交成功，类似于 CAS
        while (!committed) {
            // 创建新事务
            final StmTransaction stm = new StmTransaction();
            // 执行事务
            consumer.accept(stm);
            // 提交事务
            committed = stm.commit();
        }
    }
}
