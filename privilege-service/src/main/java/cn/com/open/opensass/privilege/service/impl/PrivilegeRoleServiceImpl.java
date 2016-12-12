package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;

/**
 * 
 */
@Service("PrivilegeRoleService")
public class PrivilegeRoleServiceImpl implements PrivilegeRoleService {

    @Autowired
    private PrivilegeRoleRepository privilegeRoleRepository;

	@Override
	public Boolean savePrivilegeRole(PrivilegeRole privilegeRole) {
		try{
			privilegeRoleRepository.savePrivilegeRole(privilegeRole);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public PrivilegeRole findRoleById(String pararentRoleId) {
		return privilegeRoleRepository.findByRoleId(pararentRoleId);
	}

	@Override
	public Boolean delPrivilegeRoleById(String privilegeRoleId) {
		try{
			privilegeRoleRepository.delPrivilegeRoleById(privilegeRoleId);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public void updatePrivilegeRole(PrivilegeRole privilegeRole) {
		privilegeRoleRepository.updatePrivilegeRole(privilegeRole);
	}

}