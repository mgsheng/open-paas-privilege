package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;


/**
 * 
 */
public interface PrivilegeUserRoleRepository extends Repository {

	void savePrivilegeUserRole(PrivilegeUserRole privilegeUserRole);
}