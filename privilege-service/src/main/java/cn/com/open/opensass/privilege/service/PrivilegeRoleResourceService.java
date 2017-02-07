package cn.com.open.opensass.privilege.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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

	Boolean updatePrivilegeRoleResource(PrivilegeRoleResource roleResource1);
	List<PrivilegeRoleResource> findUserRoleResources(String appId, String appUserId);
	List<String> findfindUserResourcesFunIdByResIsNull(String appId,String appUserId);
}