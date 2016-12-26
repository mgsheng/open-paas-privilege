package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;

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
			int startRow, int pageSize) {
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


	@Override
	public PrivilegeResource findByResourceId(String resourceId) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findByResource_Id(resourceId);
	}


	@Override
	public List<Map<String, Object>> getResourceListByUserId(String appUserId, String appId) {
		return privilegeResourceRepository.getResourceListByUserId(appUserId, appId);
	}

	@Override
	public List<PrivilegeResourceVo> findByGroupIdAndAppId(String groupId, String appId) {
		List<PrivilegeResourceVo> privilegeResourceVos =new ArrayList<PrivilegeResourceVo>();
		List<PrivilegeResource> privilegeResources=privilegeResourceRepository.findByGroupIdAndAppId(groupId, appId);
		for(PrivilegeResource resource:privilegeResources){
			PrivilegeResourceVo privilegeResourceVo=new PrivilegeResourceVo();
			privilegeResourceVo.setAppId(resource.getAppId());
			privilegeResourceVo.setBaseUrl(resource.getBaseUrl());
			privilegeResourceVo.setDisplayOrder(resource.getDisplayOrder());
			privilegeResourceVo.setMenuId(resource.getMenuId());
			privilegeResourceVo.setResourceId(resource.getResourceId());
			privilegeResourceVo.setResourceLevel(String.valueOf(resource.getResourceLevel()));
			privilegeResourceVo.setResourceName(resource.getResourceName());
			privilegeResourceVo.setResourceRule(resource.getResourceRule());
			privilegeResourceVo.setStatus(resource.getStatus());
			privilegeResourceVos.add(privilegeResourceVo);
		}
		return privilegeResourceVos;
	}

}