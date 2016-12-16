package cn.com.open.opensass.privilege.service;

import java.util.ArrayList;

import cn.com.open.opensass.privilege.model.PrivilegeUser;

/**
 * 
 */
public interface PrivilegeUserService {

	Boolean savePrivilegeUser(PrivilegeUser privilegeUser);

	Boolean delPrivilegeUserByUid(String uId);

	Boolean delUserByAppIdAndAppUserId(String appId, String appUserId);
	
	PrivilegeUser findByAppIdAndUserId(String appUserId, String appId);

	void updatePrivilegeUser(PrivilegeUser user);
	ArrayList<String> findUserResources(String appId, String appUserId); 
}