package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

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
	public Boolean savePrivilegeGroup(PrivilegeGroup privilegeGroup) {
	  try {
		  privilegeGroupRepository.savePrivilegeGroup(privilegeGroup);
		  return true;
	  } catch (Exception e) {
		// TODO: handle exception
		  return false;
	  }
		
		
	}

	@Override
	public PrivilegeGroup findBygroupId(String groupId,String appId) {
		// TODO Auto-generated method stub
		return privilegeGroupRepository.findByGroupId(groupId,appId);
	}

	@Override
	public List<PrivilegeGroup> findGroupPage(String groupId, String appId,
			String startRow, String pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

  

}