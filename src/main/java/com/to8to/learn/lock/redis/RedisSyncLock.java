package com.to8to.learn.lock.redis;

import com.to8to.server.utils.JedisManager;
import com.to8to.server.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Description: TODO
 * @author: felix.fan
 * @date: 2018/12/11 20:11
 * @version: 1.0
 */
public class RedisSyncLock {
    /** 默认重试频率*/
    private static final int FREQUENCY = 100;
    /** 默认重试次数*/
    private static final int TRY_NUM = 20;

    /** 锁的主键*/
    private String key;
    /** 超时时间*/
    private int timeout;
    /** 进行锁的时间点*/
    private long lockTime;
    /** 锁状态*/
    private boolean state;

    private boolean doTrace;

    /**
     * redis分布式锁
     *
     * @param key
     * @param timeout 超时时间（毫秒）
     */
    public RedisSyncLock(String key, int timeout) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("锁主键不能为空！");
        }

        if (timeout <= 0) {
            throw new RuntimeException("超时时间不能为0！");
        }

        this.key = key;
        this.timeout = timeout;
    }

    /**
     * 尝试获取锁
     *
     * @param tryNum    尝试次数
     * @param frequency 频率
     * @return
     */
    public boolean tryLock(int tryNum, int frequency) throws Exception {
        for (int i = 0; i < tryNum; i++) {
            if (lock()) {
                break;
            } else {
                try {
                    Thread.sleep(frequency);
                } catch (InterruptedException e) {
                }
            }
        }

        return state;
    }

    /**
     * 尝试获取锁（使用默认频率）
     *
     * @param tryNum 尝试次数
     * @return
     */
    public boolean tryLock(int tryNum) throws Exception {
        return tryLock(tryNum, FREQUENCY);
    }

    /**
     * 尝试获取锁，默认尝试次数和频率
     *
     * @return
     */
    public boolean tryLock() throws Exception {
        return tryLock(TRY_NUM);
    }

    /**
     * 上锁，只尝试一次lock操作
     *
     * @return
     */
    public boolean lock() throws Exception {
        lockTime = time();

        Long res = setnx(key, lockTime + "");

        if (res == 1) {
            state = true;
            trace("get a lock!");
        } else {
            // 如果锁超时，那么重新获取锁

            String lockData = get(key);

            if (lockData == null) {
                return lock();
            } else {
                // 刷新时间戳
                lockTime = time();

                // 判断现在的锁是否已经超时
                long oldLockTime = Long.valueOf(lockData);

                if (lockTime - oldLockTime >= timeout) {
                    String returnDate = getSet(key, lockTime + "");

                    // 通过get-set返回和原来一样的数据，那么说明获取到锁
                    // 否则是被别的线程抢先锁上

                    if (lockData.equals(returnDate)) {
                        state = true;
                        trace("get a lock by last lock timeout!");
                    } else {
                        state = false;
                    }
                } else {    // 旧锁未超时
                    state = false;
                }
            }
        }

        // 如果没有获取到锁，那么清除锁时间
        if (!state) {
            lockTime = 0;
        }

        return state;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unlock() throws Exception {
        // 其实这里不能直接删除key'
        // 如果锁超时了，为了避免删除其他线程的锁，那么就不能进行删除操作

        if (lockState()) {
            Long res = del(key);

            if (res > 0) {
                trace("unlock success!");
                state = false;
                return true;
            } else {
                return false;
            }
        } else {
            trace("lock timeout!");
        }
        return true;
    }

    /**
     * 获取锁状态
     *
     * @return
     */
    public boolean lockState() throws Exception {
        if (state == true) {
            if (lockTime > 0
                    && timeout > 0
                    && time() - lockTime > timeout - 5) { // - 5 ms，这个是为了减少误杀问题，提前5ms认为超时
                state = false;
            }
        }

        return state;
    }

    private long setnx(String key, String value) throws Exception {
        Long res = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();

            res = jedis.setnx(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return res;
    }

    private long del(String key) throws Exception {
        Long res = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();

            res = jedis.del(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return res;
    }

    private String get(String key) throws Exception {
        String res = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();

            res = jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    private String getSet(String key, String value) throws Exception {
        String res = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();

            res = jedis.getSet(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    private long time() throws Exception {
        long time = 0;
        Jedis jedis = null;
        try {
            jedis = getJedis();

            List<String> res = jedis.time();

            time = Long.valueOf(res.get(0)) * 1000 + (Long.valueOf(res.get(1)) / 1000);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return time;
    }

    private Jedis getJedis() {
        return JedisManager.getJedis();
    }

    public void trace(boolean trace) {
        this.doTrace = trace;
    }

    private void trace(String logInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("[")
                .append(System.currentTimeMillis())
                .append("] ")
                .append(Thread.currentThread().getName())
                .append(" --> ")
                .append(logInfo);

        String logStr = builder.toString();

        LogUtils.logger().trace(logStr);

        if (doTrace) {
            System.err.println(logStr);
        }
    }
}
