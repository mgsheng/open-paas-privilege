package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * 
 */
public interface PrivilegeGroupService {
	Boolean savePrivilegeGroup(PrivilegeGroup privilegeGroup);
	PrivilegeGroup findBygroupId(String groupId,String appId);
	List<PrivilegeGroup>findGroupPage(String groupId,String appId,String startRow,String pageSize);
	PrivilegeAjaxMessage findGroupPrivilege(String groupId,String appId);
	PrivilegeAjaxMessage delGroupPrivilegeCache(String groupId,String appId);
	PrivilegeAjaxMessage updateGroupPrivilegeCache(String groupId,String appId);
	
}