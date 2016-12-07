package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

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
			String resourceId ,String appId) {
		return privilegeGroupResourceRepository.findByGroupIdAndResourceId(groupId, resourceId,appId);
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


	@Override
	public Boolean deleteResource(String groupId, String resourceId,String appId) {
		try {
			privilegeGroupResourceRepository.deleteResource(groupId, resourceId,appId);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}


	@Override
	public List<PrivilegeGroupResource> getPgrs(String groupId, String appId) {
		// TODO Auto-generated method stub
		return privilegeGroupResourceRepository.findGprs(groupId, appId);
	}


	@Override
	public Boolean deleteByGroupId(String groupId, String appId) {
		try {
			privilegeGroupResourceRepository.deleteByGroup(groupId, appId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}


}