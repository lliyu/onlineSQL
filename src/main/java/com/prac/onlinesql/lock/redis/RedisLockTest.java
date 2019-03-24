package com.prac.onlinesql.lock.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ly
 * @create 2019-03-05 21:39
 **/
public class RedisLockTest {

    private static RedisLock lock = new RedisLock();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            executorService.execute(new LockTest());
        }
        executorService.shutdown();
    }

    static class LockTest implements Runnable{

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getId() + ": 开始获取到锁");
                while(!lock.lock("lock", Thread.currentThread().getName() +
                        ":" + Thread.currentThread().getId(), 10)){
                    break;
                }
                System.out.println(Thread.currentThread().getId() + ": 已经获取到锁");
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.releaseLock("lock", Thread.currentThread().getName() +
                        ":" + Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getId() + ": 已经释放锁");
            }
        }
    }

}
