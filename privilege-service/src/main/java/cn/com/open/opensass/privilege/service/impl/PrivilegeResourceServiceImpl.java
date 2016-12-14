package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;

/**
 * 
 */
@Service("privilegeResourceService")
public class PrivilegeResourceServiceImpl implements PrivilegeResourceService {
	
    @Autowired
    private PrivilegeResourceRepository privilegeResourceRepository;

	@Override
	public Boolean savePrivilegeResource(PrivilegeResource privilegeResource) {
	  try {
		  privilegeResourceRepository.savePrivilegeResource(privilegeResource);
		  return true;
	  } catch (Exception e) {
		// TODO: handle exception
		  return false;
	  }
		
		
	}

	@Override
	public PrivilegeResource findByResourceId(String resourceId,String appId) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findByResourceId(resourceId,appId);
	}

	@Override
	public PrivilegeResource findByResourceCode(String resourceId,String appId) {
		return privilegeResourceRepository.findByResourceCode(resourceId,appId);
	}

	@Override
	public List<PrivilegeResource> findResourcePage(String resourceId, String appId,
			String startRow, String pageSize) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findResourcePage(resourceId, appId, startRow, pageSize);
	}

	@Override
	public Boolean updatePrivilegeResource(PrivilegeResource privilegeResource) {
		try {
			privilegeResourceRepository.updatePrivilegeResource(privilegeResource);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public Boolean deleteByResourceId(String resourceId) {
		// TODO Auto-generated method stub
		try {
			privilegeResourceRepository.deleteByResourceId(resourceId);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

}