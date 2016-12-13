package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;

/**
 * 
 */
public interface PrivilegeResourceService {
	Boolean savePrivilegeResource(PrivilegeResource privilegeResource);
	PrivilegeResource findByResourceId(String resourceId,String appId);
	List<PrivilegeResource>findResourcePage(String resourceId,String appId,String startRow,String pageSize);
	Boolean updatePrivilegeResource(PrivilegeResource privilegeResource);
	Boolean deleteByResourceId(String  menuId);
}