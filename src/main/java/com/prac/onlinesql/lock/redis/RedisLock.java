package com.prac.onlinesql.lock.redis;

import com.prac.onlinesql.mq.jedis.JedisClientPool;

import java.util.Collections;

/**
 * @author ly
 * @create 2019-03-05 20:19
 * redis实现分布式锁
 **/
public class RedisLock {

    private JedisClientPool jedis = new JedisClientPool();

    public boolean lock(String lock,String requestId, long expire){
        String setLock = jedis.setLock(lock, requestId, expire);
        return "OK".equals(setLock);
    }

    public boolean releaseLock(String lock,String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        long result = jedis.eval(script, Collections.singletonList(lock), Collections.singletonList(requestId));
        return result == 1;
    }
}
