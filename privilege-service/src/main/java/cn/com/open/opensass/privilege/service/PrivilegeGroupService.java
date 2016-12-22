package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;

/**
 * 
 */
public interface PrivilegeGroupService {
	Boolean savePrivilegeGroup(PrivilegeGroup privilegeGroup);
	PrivilegeGroup findBygroupId(String groupId,String appId);
	List<PrivilegeGroup>findGroupPage(String groupId,String appId,String startRow,String pageSize);
	Map<String, Object> findGroupPrivilege(String groupId,String appId);
}