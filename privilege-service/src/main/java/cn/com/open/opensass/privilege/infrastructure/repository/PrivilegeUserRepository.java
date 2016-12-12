package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeUser;


/**
 * 
 */
public interface PrivilegeUserRepository extends Repository {

	void savePrivilegeUser(PrivilegeUser privilegeUser);
}