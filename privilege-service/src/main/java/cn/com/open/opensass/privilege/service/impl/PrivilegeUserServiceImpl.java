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
	public Boolean savePrivilegeUser(PrivilegeUser privilegeUser) {
		try{
			privilegeUserRepository.savePrivilegeUser(privilegeUser);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delPrivilegeUserByUid(String uId) {
		try{
			privilegeUserRepository.delPrivilegeUserByUid(uId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PrivilegeUser findByAppIdAndAppUserId(String appId, String appUserId) {
		return privilegeUserRepository.findByAppIdAndAppUserId(appId, appUserId);
	}

	@Override
	public Boolean delUserByAppIdAndAppUserId(String appId, String appUserId) {
		try{
			privilegeUserRepository.delUserByAppIdAndAppUserId(appId,appUserId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PrivilegeUser findByAppIdAndUserId(String appUserId, String appId) {
		// TODO Auto-generated method stub
		return privilegeUserRepository.findByAppIdAndAppUserId(appId, appUserId);
	}

}