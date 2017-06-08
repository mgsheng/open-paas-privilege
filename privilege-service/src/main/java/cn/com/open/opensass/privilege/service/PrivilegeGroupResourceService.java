package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;

import java.util.List;

/**
 * 
 */
public interface PrivilegeGroupResourceService {
	Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource getPrivilegeGroupResource(String groupId,String resourceId,String appId);
	List<PrivilegeGroupResource> getPgrs(String groupId, String appId);
	Boolean deleteResource (String  groupId,String resourceId, String appId);
	Boolean deleteByGroupId(String  groupId,String appId);
	Boolean updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	List<String> findResourceIdsByGroupId(String groupId);
	List<PrivilegeGroupResource> getPgrs(String groupId,String appId,String startRow,String pageSize);
	List<PrivilegeGroupResource> findByGroupIdAndAppId(String groupId, String appId);
	Boolean batchUpdateResourceIds(List<PrivilegeBatchUserVo> list);
	Boolean batchDeleteResourceIdsByGroupIds(List<PrivilegeBatchUserVo> list);
}