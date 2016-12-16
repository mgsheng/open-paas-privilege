package cn.com.open.opensass.privilege.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;

/**
 * 
 */
public interface PrivilegeRoleResourceService {

	Boolean savePrivilegeRoleResource(PrivilegeRoleResource privilegeRoleResource);

	List<PrivilegeRoleResource> findByPrivilegeRoleId(String privilegeRoleId);

	Boolean delPrivilegeRoleResource(PrivilegeRoleResource roleResource);

	Boolean delRoleResourceByRoleId(String privilegeRoleId);

	PrivilegeRoleResource findByRoleIdAndResourceId(String privilegeRoleId,String roleResource);

	ArrayList<String> findUserResourcesFunId(String appId, String appUserId);
}