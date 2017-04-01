package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
	List<PrivilegeGroup> findByAppId(String appId);
	Boolean deleteByGroupId(String groupId, String appId);
	/**
	 * 更新组织机构版本号缓存
	 * @param groupId 
	 * @param appId
	 * @return
	 */
	PrivilegeAjaxMessage updateGroupVersion(String groupId,String appId);
}