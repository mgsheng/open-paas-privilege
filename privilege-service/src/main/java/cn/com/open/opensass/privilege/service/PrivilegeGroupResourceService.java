package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;

/**
 * 
 */
public interface PrivilegeGroupResourceService {
	Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource getPrivilegeGroupResource(String groupId,String resourceId);

}