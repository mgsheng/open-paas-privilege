package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public interface PrivilegeRoleService {

	Boolean savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findRoleById(String privilegeRoleId);

	Boolean delPrivilegeRoleById(String privilegeRoleId);

	Boolean updatePrivilegeRole(PrivilegeRole privilegeRole);

	List<String> findRoleByAppId(String appId);

	List<PrivilegeRole> findRoleByPage(String privilegeRoleId, String appId, String deptId, String groupId, int start, int limit);

	int findRoleNoPage(String privilegeRoleId, String appId, String deptId, String groupId);

	List<Map<String, Object>> getRoleListByUserId(String appUserId, String appId);

	PrivilegeAjaxMessage getAppRoleRedis(String appId);

	List<Map<String, Object>> getRoleListByAppId(String appId);

	PrivilegeAjaxMessage delAppRoleRedis(String appId);

	PrivilegeAjaxMessage updateAppRoleRedis(String appId);

	List<PrivilegeRole> getRoleListByUserIdAndAppId(String appUserId, String appId);

	List<PrivilegeRole> getRoleListByAppIdAndGroupId(String appId, String groupId,String roleName,Integer roleType,int start, int limit);

	int getRoleCountByAppIdAndGroupId(String appId, String groupId,Integer roleType, String roleName);
	
	PrivilegeAjaxMessage updateRoleVersion(String appId,String privilegeRoleId);
}