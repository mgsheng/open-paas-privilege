package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeUserRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;

/**
 * 
 */
@Service("PrivilegeUserService")
public class PrivilegeUserServiceImpl implements PrivilegeUserService {

    @Autowired
    private PrivilegeUserRepository privilegeUserRepository;

	@Override
	public void savePrivilegeUser(PrivilegeUser privilegeUser) {
		privilegeUserRepository.savePrivilegeUser(privilegeUser);
	}
}