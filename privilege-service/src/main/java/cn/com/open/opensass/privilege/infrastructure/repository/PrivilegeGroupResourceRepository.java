package cn.com.open.opensass.privilege.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;


/**
 * 
 */
public interface PrivilegeGroupResourceRepository extends Repository {

	
	Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource findByGroupIdAndResourceId(String groupId,String resourceId);
}