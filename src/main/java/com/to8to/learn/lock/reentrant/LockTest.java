package com.to8to.learn.lock.reentrant;

/**
 * @Description: TODO
 * @author: felix.fan
 * @date: 2018/12/12 23:05
 * @version: 1.0
 */
public class LockTest {
    ///** 不可重入锁*/
    //UnReentLock lock = new UnReentLock();
    /** 可重入锁*/
    ReentLock lock = new ReentLock();

    public void print() {
        try {
            System.out.println("第一次开始加锁。。。");
            lock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doAdd();
        lock.unlock();
    }

    private void doAdd() {
        try {
            System.out.println("第二次开始加锁。。。");
            lock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        lockTest.print();
    }
}
