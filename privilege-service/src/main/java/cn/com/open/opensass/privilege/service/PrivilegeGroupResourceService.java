package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;

/**
 * 
 */
public interface PrivilegeGroupResourceService {
	Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource getPrivilegeGroupResource(String groupId,String resourceId,String appId);
	List<PrivilegeGroupResource> getPgrs(String groupId);
	Boolean deleteResource (String  groupId,String resourceId);
	Boolean deleteByGroupId(String  groupId);
	Boolean updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	List<String> findResourceIdsByGroupId(String groupId);
	List<PrivilegeGroupResource> getPgrs(String groupId,String startRow,String pageSize);
}