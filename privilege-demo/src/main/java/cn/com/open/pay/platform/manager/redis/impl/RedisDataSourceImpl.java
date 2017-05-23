package cn.com.open.pay.platform.manager.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.open.pay.platform.manager.redis.RedisDataSource;

@Repository("redisDataSource")
/**
 * reids 接口实现类
 * @author admin
 *
 */
public class RedisDataSourceImpl implements RedisDataSource {

    private static final Logger log = LoggerFactory.getLogger(RedisDataSourceImpl.class);

    @Autowired
    private JedisPool    shardedJedisPool;

    public Jedis getRedisClient() {
        try {
        	Jedis shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            log.error("getRedisClent error", e);
        }
        return null;
    }

    public void returnResource(Jedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
    }

    public void returnResource(Jedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
}
