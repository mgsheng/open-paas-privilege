package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

public interface PrivilegeUserRedisService {
	PrivilegeAjaxMessage getRedisUserRole(String appId,String appUserId);
	PrivilegeAjaxMessage delUserRoleRedis(String appId, String appUserId);
	PrivilegeAjaxMessage updateUserRoleRedis(String appId, String appUserId);

	/**
	 * 清空用户缓存.
	 * @param appId 应用Id.
	 * @param appUserId 用户Id.
	 * @return 清空缓存成功true还是失败false.
	 */
	Boolean delUserCaches(String appId, String appUserId);
}
