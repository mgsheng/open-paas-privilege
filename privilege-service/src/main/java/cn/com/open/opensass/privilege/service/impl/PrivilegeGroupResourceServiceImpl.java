package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;

/**
 * 
 */
@Service("privilegeGroupResourceService")
public class PrivilegeGroupResourceServiceImpl implements PrivilegeGroupResourceService {
	
    @Autowired
    private PrivilegeGroupResourceRepository privilegeGroupResourceRepository;

	@Override
	public void savePrivilegeGroup(PrivilegeGroup privilegeGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PrivilegeGroup findBygroupId(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}



  

}