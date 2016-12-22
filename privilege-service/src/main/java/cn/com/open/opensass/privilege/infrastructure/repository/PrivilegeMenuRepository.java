package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;
import java.util.Map;

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
	List<PrivilegeMenu> findMenuPage(@Param("menuId")String menuId,@Param("appId")String appId,@Param("startRow")int startRow,@Param("pageSize")int pageSize);

	/**
	 * 根据用户ID获取菜单列表
	 * @param appUserId
	 * @return
	 */
	List<PrivilegeMenu> getMenuListByUserId(@Param("appUserId")String appUserId,@Param("appId")String appId);
	/**
	 * 根据表ID获取菜单
	 * @param menuId
	 * @return
	 */
	Map<String, Object> getMenuById(@Param("menuId")String menuId);
	List<PrivilegeMenu> findMenuByAppId(@Param("appId")String appId);
}