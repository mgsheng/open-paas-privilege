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
  * 公共缓存前缀名
  */
 public static final String PUBLICSERVICE_CACHE="privilegeService_";

 /**
  * userPrivileges_appid_userid
  */
 public static final String USERPRIVILEGES_CACHE=PUBLICSERVICE_CACHE+"userCacheUrl_";

 /**
  * userCacheMenu_appid_userid
  */
 public static final String USERMENU_CACHE=PUBLICSERVICE_CACHE+"userCacheMenu_";
 /**
  * userCacheRole_appid_userid
  */
 public static final String USERROLE_CACHE=PUBLICSERVICE_CACHE+"userCacheRole_";
 
 /**
  * appRoleCache_appid
  */
 public static final String APPROLE_CACHE=PUBLICSERVICE_CACHE+"appRoleCache_";
 /**
  * appMenuCache_appid
  */
 public static final String APPMENU_CACHE=PUBLICSERVICE_CACHE+"appMenuCache_";
 /**
  * appResCache_appid
  */
 public static final String APPRES_CACHE=PUBLICSERVICE_CACHE+"appResCache_";
 /*间隔符*/
 public static final String SIGN="_";
 /**
  * 组织机构缓存key
  */
 public static final String PRIVILEGE_GROUPCACHE = "privilegeService_groupCache_";
}
