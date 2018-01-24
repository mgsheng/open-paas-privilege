package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeMenusVo;

/**
 * 
 */
public interface PrivilegeMenuService {
	Boolean savePrivilegeMenu(PrivilegeMenu privilegeMenu);
	PrivilegeMenu findByMenuId(String menuId,String appId);
	List<PrivilegeMenu>findMenuPage(String menuId,String appId,int startRow,int pageSize);
	//Map<String, Object> findByMenuId(String menuId);
	Boolean updatePrivilegeMenu(PrivilegeMenu privilegeMenu);
	Boolean deleteByMenuId(String[]  menuIds,String appId);
	List<PrivilegeMenu> getMenuListByResourceId(String resourceId,String appId);
	List<PrivilegeMenu> getMenuListByResourceId2(String resourceIds[],String appId);
	List<PrivilegeMenuVo> findMenuByResourceType(Integer resourceType);
	/**
	 * 根据用户ID获取菜单列表
	 * @param appUserId
	 * @return
	 */
	List<PrivilegeMenu> getMenuListByUserId(String appUserId,String appId);

	/**
	 * 根据表ID获取菜单
	 * @param menuId
	 * @return
	 */
	PrivilegeMenu getMenuById(String menuId,String appId);


	/**
	 * 获取redis url 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getMenuRedis(String appId, String appUserId);
	/**
	 * 获取redis url 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getMenuRedisByMap(Map<String,String> appId);

	/**
	 * 更新 redis url 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage updateMenuRedis(String appId, String appUserId);

	/**
	 * 删除 redis url 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage delMenuRedis(String appId, String appUserId);

	/**
	 * 判断key  redis 是否存在 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage existMenuKeyRedis(String appId, String appUserId);

	/**
	 * 获取所有menu数据
	 * @param privilegeMenuList
	 * @param privilegeMenuVoSet
	 * @return
	 */
	Set<PrivilegeMenuVo> getAllMenuByUserId(List<PrivilegeMenu> privilegeMenuList, Set<PrivilegeMenuVo> privilegeMenuVoSet);
	/**
	 * 获取所有menu数据
	 * @param appId
	 * @return
	 */
	List<PrivilegeMenu> getMenuListByAppId(String appId);
	List<PrivilegeMenuVo> getMenuVoListByAppId(String appId);
	List<PrivilegeMenuVo> getMenuVoListByAppId(Map<String,String> appId);
	/**
	 * 获取redis menu 数据
	 * @param appId 应用id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getAppMenuRedis(String appId);
	/**
	 * 获取redis menu 数据
	 * @param appId 应用id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getManagerMenuRedis(Map<String,String> appId);
	
	/**
	 * 删除redis menu 数据
	 * @param appId 应用id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage delAppMenuRedis(String appId);
	
	/**
	 * 更新redis menu 数据
	 * @param appId 应用id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage updateAppMenuRedis(String appId);
	List<PrivilegeMenu> getMenuListByFunctionId(String[] functionIds,String appId);
	List<PrivilegeMenu> findByParentId(String parentId, String appId);
	/**
	 * 批量更新 menu
	 * @param privilegeMenus list
	 * @param appId appId
	 * @return
	 */
	Boolean updatePrivilegeMenuBatch(List<PrivilegeMenu> privilegeMenus, String appId);
	
	Set<PrivilegeMenusVo> getAllMenuByUserIds(List<PrivilegeMenu> privilegeMenuList,
			Set<PrivilegeMenusVo> privilegeMenuListReturn);
	PrivilegeAjaxMessage getMenusRedis(String appId, String appUserId);
	PrivilegeAjaxMessage getAppMenusRedis(String appId);
	List<PrivilegeMenusVo> getMenuVoListByAppIds(String appId);
	PrivilegeAjaxMessage getAppMenuRediss(String appId);
}