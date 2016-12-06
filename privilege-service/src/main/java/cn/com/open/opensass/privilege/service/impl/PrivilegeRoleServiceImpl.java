package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.UserAccountBalanceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.UserAccountBalanceService;
import cn.com.open.user.platform.manager.user.model.UserAccountBalance;

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