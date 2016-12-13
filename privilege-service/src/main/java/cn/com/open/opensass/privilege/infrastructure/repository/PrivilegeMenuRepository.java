package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeMenu;


/**
 * 
 */
public interface PrivilegeMenuRepository extends Repository {

	
	void savePrivilegeMenu(PrivilegeMenu privilegeMenu);
	PrivilegeMenu findByMenuId(@Param("menuId")String menuId,@Param("appId")String appId);
	void updatePrivilegeMenu(PrivilegeMenu privilegeMenu);
	void deleteByMenuId(@Param("menuId")String menuId);
	List<PrivilegeMenu> findMenuPage(@Param("menuId")String menuId,@Param("appId")String appId,@Param("startRow")String startRow,@Param("pageSize")String pageSize);
}