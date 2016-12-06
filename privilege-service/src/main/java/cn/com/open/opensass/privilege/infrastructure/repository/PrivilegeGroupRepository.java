package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;


/**
 * 
 */
public interface PrivilegeGroupRepository extends Repository {

	
	void savePrivilegeGroup(PrivilegeGroup privilegeGroup);
	PrivilegeGroup findByGroupId(String groupId);
	void updatePrivilegeGroup(PrivilegeGroup privilegeGroup);
}