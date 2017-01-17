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
	void deleteByMenuId(@Param("menuIds")String[] menuIds);
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
	PrivilegeMenu getMenuById(@Param("menuId")String menuId);
	List<PrivilegeMenu> findMenuByGroupIdAndAppId(@Param("groupId")String groupId,@Param("appId")String appId);
	List<PrivilegeMenu> getMenuListByAppId(@Param("appId")String appId);
	List<PrivilegeMenu> getMenuListByFunctionId(@Param("functionIds")String[] functionIds);
	List<PrivilegeMenu> getMenuListByResourceId(@Param("resourceId")String resourceId);
	List<PrivilegeMenu> getMenuListByParentId(@Param("parentId")String parentId, @Param("appId")String appId);
}