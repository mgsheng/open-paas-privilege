package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * 
 */
public interface PrivilegeRoleService {

	Boolean savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findRoleById(String pararentRoleId);

	Boolean delPrivilegeRoleById(String privilegeRoleId);

	Boolean updatePrivilegeRole(PrivilegeRole privilegeRole);

	List<String> findRoleByAppId(String appId);

	List<PrivilegeRole> findRoleByPage(String privilegeRoleId, String appId, int start, int limit);

	int findRoleNoPage(String privilegeRoleId, String appId);

	List<Map<String, Object>> getRoleListByUserId(String appUserId, String appId);

	PrivilegeAjaxMessage getAppRoleRedis(String appId);

	List<Map<String, Object>> getRoleListByAppId(String appId);

	PrivilegeAjaxMessage delAppRoleRedis(String appId);

	PrivilegeAjaxMessage updateAppRoleRedis(String appId);

	List<PrivilegeRole> getRoleListByUserIdAndAppId(String appUserId, String appId);

	List<PrivilegeRole> getRoleListByAppIdAndGroupId(String appId, String groupId);

	int getRoleCountByAppIdAndGroupId(String appId, String groupId);
}