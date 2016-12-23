package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;

/**
 * 
 */
public interface PrivilegeMenuService {
	Boolean savePrivilegeMenu(PrivilegeMenu privilegeMenu);
	PrivilegeMenu findByMenuId(String menuId,String appId);
	List<PrivilegeMenu>findMenuPage(String menuId,String appId,int startRow,int pageSize);
	//Map<String, Object> findByMenuId(String menuId);
	Boolean updatePrivilegeMenu(PrivilegeMenu privilegeMenu);
	Boolean deleteByMenuId(String  menuId);

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
	PrivilegeMenu getMenuById(String menuId);


	/**
	 * 获取redis url 数据
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getMenuRedis(String appId, String appUserId);


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


}