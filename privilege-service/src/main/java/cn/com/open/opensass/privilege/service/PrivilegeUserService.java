package cn.com.open.opensass.privilege.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeUser;

/**
 * 
 */
public interface PrivilegeUserService {

	Boolean savePrivilegeUser(PrivilegeUser privilegeUser);

	Boolean delPrivilegeUserByUid(String uId);

	Boolean delUserByAppIdAndAppUserId(String appId, String appUserId);
	
	PrivilegeUser findByAppIdAndUserId(String appUserId, String appId);

	Boolean updatePrivilegeUser(PrivilegeUser user);
	ArrayList<String> findUserResources(String appId, String appUserId); 
	List<PrivilegeUser> findUserListByPage(String appId, int start, int limit);
	int getUserCountByAppId(String appId);
}