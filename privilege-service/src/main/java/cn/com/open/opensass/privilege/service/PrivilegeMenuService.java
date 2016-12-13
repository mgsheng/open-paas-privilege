package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;

/**
 * 
 */
public interface PrivilegeMenuService {
	Boolean savePrivilegeMenu(PrivilegeMenu privilegeMenu);
	PrivilegeMenu findByMenuId(String menuId,String appId);
	List<PrivilegeMenu>findMenuPage(String menuId,String appId,String startRow,String pageSize);
	Boolean updatePrivilegeMenu(PrivilegeMenu privilegeMenu);
	Boolean deleteByMenuId(String  menuId);

}