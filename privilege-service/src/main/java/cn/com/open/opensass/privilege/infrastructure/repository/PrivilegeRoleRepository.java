package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRole;


/**
 * 
 */
public interface PrivilegeRoleRepository extends Repository {

	void savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findByRoleId(String pararentRoleId);

	void delPrivilegeRoleById(String privilegeRoleId);

	void updatePrivilegeRole(PrivilegeRole privilegeRole);

	List<String> findRoleByAppId(String appId);
}