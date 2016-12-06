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
	public void savePrivilegeRole(PrivilegeRole privilegeRole) {
		privilegeRoleRepository.savePrivilegeRole(privilegeRole);
	}

}