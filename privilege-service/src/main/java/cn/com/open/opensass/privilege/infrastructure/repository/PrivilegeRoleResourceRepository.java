package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;



/**
 * 
 */
public interface PrivilegeRoleResourceRepository extends Repository {

	void savePrivilegeRoleResource(PrivilegeRoleResource privilegeRoleResource);

	List<PrivilegeRoleResource> findByPrivilegeRoleId(String privilegeRoleId);

	void delPrivilegeRoleResource(PrivilegeRoleResource roleResource);

	void delRoleResourceByRoleId(String privilegeRoleId);

	PrivilegeRoleResource findByRoleIdAndResourceId(String privilegeRoleId,String resourceId);
}