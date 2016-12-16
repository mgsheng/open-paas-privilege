package cn.com.open.opensass.privilege.redis.impl;
/**
 * redis 存储开开头
 * @author admin
 *
 */
public class RedisConstant {
 public static final String APP_INFO="appInfo_";
 public static final String USER_NAME_CHECK="userNameCheck_";
 public static final String USER_CACHE_INFO="userCacheInfo_";//存储方式：key:userCacheInfo_username value:""

 /**
  * userPrivileges_appid_userid
  */
 public static final String USERPRIVILEGES_CACHE="userUrl_";
 /*间隔符*/
 public static final String SIGN=" _";
}
