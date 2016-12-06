package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;

/**
 * 
 */
@Service("privilegeGroupService")
public class PrivilegeGroupServiceImpl implements PrivilegeGroupService {
	
    @Autowired
    private PrivilegeGroupRepository privilegeGroupRepository;

	@Override
	public void savePrivilegeGroup(PrivilegeGroup privilegeGroup) {
		privilegeGroupRepository.savePrivilegeGroup(privilegeGroup);
		
	}

	@Override
	public PrivilegeGroup findBygroupId(String groupId) {
		// TODO Auto-generated method stub
		return privilegeGroupRepository.findByGroupId(groupId);
	}

  

}