package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;

/**
 * 
 */
@Service("PrivilegeRoleResourceService")
public class PrivilegeRoleResourceServiceImpl implements PrivilegeRoleResourceService {

    @Autowired
    private PrivilegeRoleResourceRepository privilegeRoleResourceRepository;

	@Override
	public Boolean savePrivilegeRoleResource(PrivilegeRoleResource privilegeRoleResource) {
		try{
			privilegeRoleResourceRepository.savePrivilegeRoleResource(privilegeRoleResource);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<PrivilegeRoleResource> findByPrivilegeRoleId(String privilegeRoleId) {
		return privilegeRoleResourceRepository.findByPrivilegeRoleId(privilegeRoleId);
	}

	@Override
	public Boolean delPrivilegeRoleResource(PrivilegeRoleResource roleResource) {
		try{
			privilegeRoleResourceRepository.delPrivilegeRoleResource(roleResource);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean delRoleResourceByRoleId(String privilegeRoleId) {
		try{
			privilegeRoleResourceRepository.delRoleResourceByRoleId(privilegeRoleId);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public PrivilegeRoleResource findByRoleIdAndResourceId(String privilegeRoleId,String resourceId) {
		return privilegeRoleResourceRepository.findByRoleIdAndResourceId(privilegeRoleId,resourceId);
	}

}