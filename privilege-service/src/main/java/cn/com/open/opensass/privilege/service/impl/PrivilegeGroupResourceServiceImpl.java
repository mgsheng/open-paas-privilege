package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;

/**
 * 
 */
@Service("privilegeGroupResourceService")
public class PrivilegeGroupResourceServiceImpl implements PrivilegeGroupResourceService {
	
    @Autowired
    private PrivilegeGroupResourceRepository privilegeGroupResourceRepository;


	@Override
	public PrivilegeGroupResource getPrivilegeGroupResource(String groupId,
			String resourceId) {
		return privilegeGroupResourceRepository.findByGroupIdAndResourceId(groupId, resourceId);
	}


	@Override
	public Boolean saveprivilegeGroupResource(
			PrivilegeGroupResource privilegeGroupResource) {
		try {
			privilegeGroupResourceRepository.saveprivilegeGroupResource(privilegeGroupResource);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}


}