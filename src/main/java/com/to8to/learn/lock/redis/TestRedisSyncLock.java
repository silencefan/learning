package com.to8to.learn.lock.redis;

import com.to8to.server.utils.JedisManager;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.ByteArrayInputStream;
import java.util.Random;

/**
 * @Description: TODO
 * @author: felix.fan
 * @date: 2018/12/12 9:09
 * @version: 1.0
 */
public class TestRedisSyncLock {
    private static String key = "test_redis_lock_2016";
    private static int timeoutSecond = 5000;

    static class LockTest implements Runnable
    {
        Random r = new Random();

        RedisSyncLock lock1= new RedisSyncLock(key, timeoutSecond);

        public void run()
        {
            lock1.trace(true);

            try {
                Thread.sleep(r.nextInt(8000));
            } catch (InterruptedException e) { }

            boolean lock = false;

            while(true)
            {
                try {
                    lock = lock1.tryLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(lock)
                {
                    try {
                        Thread.sleep(r.nextInt(8000));
                    } catch (InterruptedException e) { }

                    try {
                        lock = lock1.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(r.nextInt(200));
                } catch (InterruptedException e) { }
            }
        }

    }

    public static void main(String[] args) throws ConfigurationException
    {
        // 初始化redis

        String data = "cluster=master_6379\n"
                +"passwd=testpasswd\n"
                +"sentinels=192.168.1.83:26379 192.168.1.83:26380 192.168.1.84:26379\n"
                +"max_total=16\n"
                +"max_idle=8\n"
                +"min_idle=0\n"
                +"time_out=5000\n";

        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load(new ByteArrayInputStream(data.getBytes()));
        JedisManager.init(config);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) { }

        LockTest lock1 = new LockTest();
        LockTest lock2 = new LockTest();
        LockTest lock3 = new LockTest();
        LockTest lock4 = new LockTest();
        LockTest lock5 = new LockTest();
        LockTest lock6 = new LockTest();
        LockTest lock7 = new LockTest();
        LockTest lock8 = new LockTest();
        LockTest lock9 = new LockTest();
        LockTest lock0 = new LockTest();

        Thread thread1 = new Thread(lock1);
        Thread thread2 = new Thread(lock2);
        Thread thread3 = new Thread(lock3);
        Thread thread4 = new Thread(lock4);
        Thread thread5 = new Thread(lock5);
        Thread thread6 = new Thread(lock6);
        Thread thread7 = new Thread(lock7);
        Thread thread8 = new Thread(lock8);
        Thread thread9 = new Thread(lock9);
        Thread thread0 = new Thread(lock0);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread0.start();
    }
}
