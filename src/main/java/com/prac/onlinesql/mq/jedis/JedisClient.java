package com.prac.onlinesql.mq.jedis;

import java.util.List;

public interface JedisClient {

	String set(String key, String value);

    long eval(String script, List keys, List values);

    String setLock(String key, String value, long time);

    String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	Boolean hexists(String key, String field);
	List<String> hvals(String key);
	Long del(String key);
}
