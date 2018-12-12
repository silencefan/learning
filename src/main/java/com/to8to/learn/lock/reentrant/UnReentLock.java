package com.to8to.learn.lock.reentrant;

/**
 * @Description: TODO
 * @author: felix.fan
 * @date: 2018/12/12 23:03
 * @version: 1.0
 */
public class UnReentLock {
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }
}
