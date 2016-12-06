package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRole;


/**
 * 
 */
public interface PrivilegeRoleRepository extends Repository {

	void savePrivilegeRole(PrivilegeRole privilegeRole);
}