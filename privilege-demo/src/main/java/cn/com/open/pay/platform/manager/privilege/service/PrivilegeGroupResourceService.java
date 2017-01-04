package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeGroupResource;


/**
 * 
 */
public interface PrivilegeGroupResourceService {
	Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource getPrivilegeGroupResource(String groupId,String resourceId);
	List<PrivilegeGroupResource> getPgrs(String groupId);
	Boolean deleteResource (String  groupId,String resourceId);
	Boolean deleteByGroupId(String  groupId);
	Boolean updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	List<String> findResourceIdsByGroupId(String groupId);
	List<PrivilegeGroupResource> getPgrs(String groupId,String startRow,String pageSize);
}