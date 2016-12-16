package cn.com.open.opensass.privilege.dao.cache;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrlData;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scala.util.parsing.combinator.testing.Str;

import java.util.*;

/**
 * Created by jh on 2016/12/13.
 */
public class RedisDao {

    /*日志组件*/
    private static final Logger log = LoggerFactory.getLogger(RedisDao.class);
    /*redis连接池*/
    private JedisPool jedisPool;
    /*构造函数初始化 不过期需要设置 privilege-service.properties redis.timeout=0 即可*/
    public RedisDao(JedisPoolConfig poolConfig, String host, int port, int timeout, String password)
    {
        jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
    }
    private Schema schema  = RuntimeSchema.getSchema(PrivilegeUrl.class);


    /**
     * 写入jedis
     * @param privilegeUrl
     * @param appid
     * @param uid
     * @return
     */
    public String putUrlRedis(PrivilegeUrl privilegeUrl, String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                byte[] bytes = ProtostuffIOUtil.toByteArray(privilegeUrl,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                /*超时缓存*/
                //int timeout = jedis.getClient().getTimeout();
                String result = jedis.set(key.getBytes(),bytes);
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
    public String getUrlRedis(String appid, String uid)
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
                    PrivilegeUrl privilegeUrl = (PrivilegeUrl) schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,privilegeUrl,schema);
                    return  privilegeUrl.getPrivilegeUrl();
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
     * 修改jedis
     * @param resourceUrlData
     * @param appid
     * @param uid
     * @return
     */
    public String updateUrlRedis(ResourceUrlData resourceUrlData, String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                jedis.del(key);
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
     * 删除jedis
     * @param appid
     * @param uid
     * @return
     */
    public boolean deleteUrlRedis(String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                jedis.del(key);
                return true;
            }
            finally {
                jedis.quit();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return false;
    }

    /**
     * 判断值是否存在jedis
     * @param appid
     * @param uid
     * @return
     */
    public boolean existUrlRedis(String url,String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
                if(!jedis.exists(key)) return false;

                byte[] bytes = jedis.get(key.getBytes());
                /*从缓存中获取到*/
                if (null != bytes)
                {
                    PrivilegeUrl privilegeUrl = (PrivilegeUrl) schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,privilegeUrl,schema);
                    String urlJson = privilegeUrl.getPrivilegeUrl();
                    if(null != url && url.length()>0)
                    {
                        url = url.toLowerCase();
                        if(url.indexOf("?")>0)
                        {
                            url = url.split("//u003F")[0];
                        }
                        ArrayList<String> stringArrayList = getStringFromJson(urlJson);
                        for (String str : stringArrayList)
                        {
                            if(str.indexOf(url)>-1)
                            {
                                return true;
                            }
                        }
                    }
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
        return false;
    }
    private ArrayList<String> getStringFromJson(String json)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        Map map=new HashMap();
        JsonConfig jc=new JsonConfig();
        jc.setClassMap(map);
        jc.setRootClass(Map.class);
        jc.setArrayMode(JsonConfig.MODE_LIST);

        JSONObject jobj=JSONObject.fromObject(json,jc);
        String obj = jobj.get("urlList").toString();
        if(obj != null && obj.length()>0)
        {
            obj = obj.substring(1,obj.length()-1);
            String[] strings = obj.split(",");
            for (String string : strings)
            {
                arrayList.add(string);
            }
        }
        return arrayList;
    }
    /**
     * 将json数组转化为String型
     * @param str
     * @return
     */
    public static String[] getJsonToStringArray(String str) {
        JSONArray jsonArray = JSONArray.fromObject(str);
        String[] arr=new String[jsonArray.size()];
        for(int i=0;i<jsonArray.size();i++){
            arr[i]=jsonArray.getString(i);
            System.out.println(arr[i]);
        }
        return arr;
    }

    /**
     * 判断key是否存在
     * @param appid
     * @param uid
     * @return
     */

    public boolean existKeyRedis(String appid, String uid)
    {
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;

                return jedis.exists(key);
            }
            finally {
                jedis.quit();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return false;
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
        ResourceUrlData resourceUrlData =null;
        if(null != resourceUrlData)
        {
            exist = true;
            /*final String key = RedisConstant.USERPRIVILEGES_CACHE+appid+ RedisConstant.SIGN+uid;
            List<ResourceUrl> resourceUrlList = resourceUrlData.getResourceUrls();
            *//*List<ResourceUrl> pilotList = resourceUrlList.stream().filter(r->r.getKey().equals(key) && r.getUrl().indexOf(url)>0).collect(Collectors.toList());*//*

            for (ResourceUrl resourceUrl : resourceUrlList)
            {
                if(resourceUrl.getKey().equals(key.trim()) && (resourceUrl.getUrl().indexOf(url)>-1 || url.indexOf(resourceUrl.getUrl())>-1))
                {
                }
            }*/
        }
        return exist;
    }



}
