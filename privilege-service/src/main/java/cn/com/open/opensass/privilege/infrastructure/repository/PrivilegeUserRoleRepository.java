package cn.com.open.opensass.privilege.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;


/**
 * 
 */
public interface PrivilegeUserRoleRepository extends Repository {

	void savePrivilegeUserRole(PrivilegeUserRole privilegeUserRole);

	void delPrivilegeUserRoleByUid(String appUserId);

	PrivilegeUserRole findByUidAndRoleId(@Param("userId") String uId,@Param("privilegeRoleId") String roleId);

	void delPrivilegeUserRole(PrivilegeUserRole userRole);
}