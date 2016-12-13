package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeUserRoleRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;

/**
 * 
 */
@Service("PrivilegeUserRoleService")
public class PrivilegeUserRoleServiceImpl implements PrivilegeUserRoleService {

    @Autowired
    private PrivilegeUserRoleRepository privilegeUserRoleRepository;

	@Override
	public Boolean savePrivilegeUserRole(PrivilegeUserRole privilegeUserRole) {
		try{
			privilegeUserRoleRepository.savePrivilegeUserRole(privilegeUserRole);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean delPrivilegeUserRoleByUid(String appUserId) {
		try{
			privilegeUserRoleRepository.delPrivilegeUserRoleByUid(appUserId);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}