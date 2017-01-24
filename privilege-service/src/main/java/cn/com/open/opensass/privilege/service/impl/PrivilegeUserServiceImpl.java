package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

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
	public PrivilegeUser findByAppIdAndUserId(String appId, String appUserId) {
		return privilegeUserRepository.findByAppIdAndAppUserId(appId, appUserId);
	}

	@Override
	public Boolean updatePrivilegeUser(PrivilegeUser user) {
		try{
			privilegeUserRepository.updatePrivilegeUser(user);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public ArrayList<String> findUserResources(String appId, String appUserId) {
		return privilegeUserRepository.findUserResources(appId, appUserId);
	}

	@Override
	public List<PrivilegeUser> findUserListByPage(String appId, int start, int limit) {
		return privilegeUserRepository.findUserListByPage(appId, start, limit);
	}

	@Override
	public int getUserCountByAppId(String appId) {
		return privilegeUserRepository.getUserCountByAppId(appId);
	}

}