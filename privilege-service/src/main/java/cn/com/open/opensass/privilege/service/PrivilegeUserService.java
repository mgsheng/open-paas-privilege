package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeUser;
import scala.util.parsing.combinator.testing.Str;

/**
 * 
 */
public interface PrivilegeUserService {

	Boolean savePrivilegeUser(PrivilegeUser privilegeUser);

	Boolean delPrivilegeUserByUid(String uId);

	Boolean delUserByAppIdAndAppUserId(String appId, String appUserId);
	
	PrivilegeUser findByAppIdAndUserId(String appUserId, String appId);

	void updatePrivilegeUser(PrivilegeUser user);

}