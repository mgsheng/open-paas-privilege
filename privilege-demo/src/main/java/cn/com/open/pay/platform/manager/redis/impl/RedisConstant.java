package cn.com.open.pay.platform.manager.redis.impl;

/**
 * redis 存储开开头
 * 
 * @author admin
 *
 */
public class RedisConstant {
	public static final String ACCESSTOKEN_CACHE="_userservice_accessToken";
	public static final String APP_INFO = "appInfo_";
	public static final String USER_NAME_CHECK = "userNameCheck_";
	public static final String USER_CACHE_INFO = "userCacheInfo_";// 存储方式：key:userCacheInfo_username
																	// value:""
	public static final String USER_LATEST_VISIT="userLastVisit_";
	public static final String PUBLICSERVICE_CACHE="privilegeService_";
	 /**
	  * appResCache_appid
	  */
	public static final String APPRES_CACHE=PUBLICSERVICE_CACHE+"appResCache_";
}
