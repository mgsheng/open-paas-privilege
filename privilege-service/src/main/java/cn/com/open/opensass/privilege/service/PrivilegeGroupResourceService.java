package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;

/**
 * 
 */
public interface PrivilegeGroupResourceService {
	void savePrivilegeGroup(PrivilegeGroup privilegeGroup);
	PrivilegeGroup findBygroupId(String groupId);

}