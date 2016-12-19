package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeRole;

/**
 * 
 */
public interface PrivilegeRoleService {

	Boolean savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findRoleById(String pararentRoleId);

	Boolean delPrivilegeRoleById(String privilegeRoleId);

	Boolean updatePrivilegeRole(PrivilegeRole privilegeRole);

	List<String> findRoleByAppId(String appId);

	List<PrivilegeRole> findRoleByPage(String privilegeRoleId, String appId,int start, int limit);

	int findRoleNoPage(String privilegeRoleId, String appId);
}