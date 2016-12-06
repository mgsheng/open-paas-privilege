package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;


/**
 * 
 */
public interface PrivilegeGroupResourceRepository extends Repository {

	
	void savePrivilegeGroup(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource findByGroupId(String groupId);
	void updatePrivilegeGroupResource(PrivilegeGroup privilegeGroupResource);
}