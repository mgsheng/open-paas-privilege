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
	//用户最后访问菜单缓存key
	public static final String USER_LATEST_VISIT="userLastVisit_";
	
	//用户常用菜单缓存key
	public static final String USER_FREQUENTLY_MENU="userFrequentlyUsedMenu_";
	
	/*间隔符*/
	 public static final String SIGN="_";
	
}
