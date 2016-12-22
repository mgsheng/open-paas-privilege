package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

public interface PrivilegeUserRedisService {
	PrivilegeAjaxMessage getRedisUserRole(String appId,String appUserId);
	PrivilegeAjaxMessage delUserRoleRedis(String appId, String appUserId);
	PrivilegeAjaxMessage updateUserRoleRedis(String appId, String appUserId);
}
