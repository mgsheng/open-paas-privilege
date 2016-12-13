package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeUserRole;

/**
 * 
 */
public interface PrivilegeUserRoleService {

	Boolean savePrivilegeUserRole(PrivilegeUserRole privilegeUserRole);

	Boolean delPrivilegeUserRoleByUid(String appUserId);
}