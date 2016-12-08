package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeRole;

/**
 * 
 */
public interface PrivilegeRoleService {

	Boolean savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findRoleById(String pararentRoleId);

	Boolean delPrivilegeRoleById(String privilegeRoleId);
}