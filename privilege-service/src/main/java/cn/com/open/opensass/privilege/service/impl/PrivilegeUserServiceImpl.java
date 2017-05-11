package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeUserRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;

/**
 * 
 */
@Service("PrivilegeUserService")
public class PrivilegeUserServiceImpl implements PrivilegeUserService {

    @Autowired
    private PrivilegeUserRepository privilegeUserRepository;

	@Override
	public Integer connectionTest() {
		return privilegeUserRepository.connectionTest();
	}

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
	public List<PrivilegeUser> findByAppIdAndUserIds(String appId, String appUserId) {
		return privilegeUserRepository.findByAppIdAndAppUserIds(appId,appUserId);
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
	public List<PrivilegeUser> findUserListByPage(String appId, int start, int limit,String groupId) {
		return privilegeUserRepository.findUserListByPage(appId, start, limit,groupId);
	}

	@Override
	public int getUserCountByAppId(String appId,String groupId) {
		return privilegeUserRepository.getUserCountByAppId(appId,groupId);
	}

	@Override
	public Boolean batchUpdateResourceIds(List<PrivilegeBatchUserVo> list) {
		try{
			privilegeUserRepository.batchUpdateResourceIds(list);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}