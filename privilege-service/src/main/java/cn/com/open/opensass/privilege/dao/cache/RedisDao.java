package cn.com.open.opensass.privilege.dao.cache;

import cn.com.open.opensass.privilege.dao.ResourceUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrlData;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Created by jh on 2016/12/13.
 */
public class RedisDao {

    /*日志组件*/
    private static final Logger log = LoggerFactory.getLogger(RedisDao.class);
    /*redis连接池*/
    private JedisPool jedisPool;
    /*构造函数初始化*/
    public RedisDao(JedisPoolConfig poolConfig, String host, int port, int timeout, String password)
    {
        jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
    }
    private Schema schema  = RuntimeSchema.getSchema(ResourceUrlData.class);


    /**
     * 写入jedis
     * @param resourceUrlData
     * @param appid
     * @param uid
     * @return
     */
    public String putUrlRedis(ResourceUrlData resourceUrlData, String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                byte[] bytes = ProtostuffIOUtil.toByteArray(resourceUrlData,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                /*超时缓存*/
                int timeout = jedis.getClient().getTimeout();
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }
            finally {
                jedis.quit();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 从jedis读取
     * @param appid
     * @param uid
     * @return
     */
    public ResourceUrlData getUrlRedis(String appid, String uid)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                byte[] bytes = jedis.get(key.getBytes());
                /*从缓存中获取到*/
                if (null != bytes)
                {
                    ResourceUrlData resourceUrlData = (ResourceUrlData) schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,resourceUrlData,schema);
                    return  resourceUrlData;
                }
            }
            finally {
                jedis.quit();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 判断用户是否存在
     * @param appid
     * @param uid
     * @return
     */

    public boolean isExistUserKey(String appid, String uid)
    {
        boolean exist = false;
        ResourceUrlData resourceUrlData = getUrlRedis(appid,uid);
        if(null != resourceUrlData)
        {
            final String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
            List<ResourceUrl> resourceUrlList = resourceUrlData.getResourceUrls();

            for (ResourceUrl resourceUrl : resourceUrlList)
            {
                if(resourceUrl.getKey().equals(key.trim()))
                {
                    exist = true;
                }
            }
        }
        return exist;
    }


    /**
     * 判断用户权限是否存在
     * @param appid
     * @param uid
     * @return
     */
    public boolean isExistUserValue(String appid, String uid,String url)
    {
        boolean exist = false;
        ResourceUrlData resourceUrlData = getUrlRedis(appid,uid);
        if(null != resourceUrlData)
        {
            final String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
            List<ResourceUrl> resourceUrlList = resourceUrlData.getResourceUrls();
            /*List<ResourceUrl> pilotList = resourceUrlList.stream().filter(r->r.getKey().equals(key) && r.getUrl().indexOf(url)>0).collect(Collectors.toList());*/

            for (ResourceUrl resourceUrl : resourceUrlList)
            {
                if(resourceUrl.getKey().equals(key.trim()) && (resourceUrl.getUrl().indexOf(url)>-1 || url.indexOf(resourceUrl.getUrl())>-1))
                {
                    exist = true;
                }
            }
        }
        return exist;
    }



}
