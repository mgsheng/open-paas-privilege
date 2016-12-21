package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONObject;
@Repository("privilegeUserRedisService")
public class PrivilegeUserRedisServiceImpl implements PrivilegeUserRedisService {
	private static final String UserRoleprifix = "privilegeService_userCacheRole_";
	private static final String UserMenuprifix = "privilegeService_userCacheMenu_";

	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private  PrivilegeMenuService privilegeMenuService;

	@Override
	public PrivilegeAjaxMessage getRedisUserRole(String appId, String appUserId) {

		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 验证用户是否存在*/
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		
		//redis key
		String userCacheRoleKey = UserRoleprifix + appId + "_" + appUserId;
		Map<String, List<Map<String, Object>>> roleMap = new HashMap<String, List<Map<String, Object>>>();
		/* 缓存中是否存在  存在返回 */
		String jsonString = redisClientTemplate.getString(userCacheRoleKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}
		//相应的IDs
		String privilegeRoleIds = privilegeUser.getPrivilegeRoleId();
		String privilegeResourceIds = privilegeUser.getResourceId();
		String privilegeFunctionIds = privilegeUser.getPrivilegeFunId();
		// RoleList
		if (privilegeRoleIds != null && !("").equals(privilegeRoleIds)) {
			String[] roleIds1 = privilegeRoleIds.split(",");// 将当前user
															// privilegeRoleIds字段数组转list
			List<String> roleIdList = new ArrayList<String>();
			Collections.addAll(roleIdList, roleIds1);
			PrivilegeRole role = null;// roleList
			List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
			for (String roleId : roleIdList) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				role = privilegeRoleService.findRoleById(roleId);
				map2.put("appId", role.getAppId());
				map2.put("deptId", role.getDeptId());
				map2.put("deptName", role.getDeptName());
				map2.put("groupId", role.getGroupId());
				map2.put("groupName", role.getGroupName());
				map2.put("privilegeRoleId", role.getParentRoleId());
				map2.put("parentRoleId", role.getParentRoleId());
				map2.put("remark", role.getRemark());
				map2.put("roleLevel", role.getRoleLevel());
				map2.put("roleName", role.getRoleName());
				map2.put("status", role.getStatus());
				roleList.add(map2);

			}
			roleMap.put("roleList", roleList);
		}
		// resourceList
		if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)) {
			String[] resourceIds1 = privilegeResourceIds.split(",");// 将当前user
																	// privilegeResourceIds字段数组转list
			List<String> resourceIdList = new ArrayList<String>();
			Collections.addAll(resourceIdList, resourceIds1);
			PrivilegeResource resource = null;
			List<Map<String, Object>> resourceList = new ArrayList<Map<String, Object>>();
			for (String resourceId : resourceIdList) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				resource = privilegeResourceService.findByResourceId(resourceId);
				map2.put("appId", resource.getAppId());
				map2.put("resourceId", resource.getResourceId());
				map2.put("resourceLevel", resource.getResourceLevel());
				map2.put("resourceName", resource.getResourceName());
				map2.put("resourceRule", resource.getResourceRule());
				map2.put("dislayOrder ", resource.getDisplayOrder());
				map2.put("menuId", resource.getMenuId());
				map2.put("baseUrl", resource.getBaseUrl());
				map2.put("status", resource.getStatus());
				resourceList.add(map2);

			}
			roleMap.put("resourceList", resourceList);
		}
		// functionList
		if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
			String[] functionIds1 = privilegeFunctionIds.split(",");
			List<String> functionIdList = new ArrayList<String>();
			Collections.addAll(functionIdList, functionIds1);
			PrivilegeFunction function = null;
			List<Map<String, Object>> functionList = new ArrayList<Map<String, Object>>();
			for (String functionId : functionIdList) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				function = privilegeFunctionService.findByFunctionId(functionId);
				map2.put("resourceId", function.getResourceId());
				map2.put("functionId", function.getId());
				map2.put("optId", function.getOperationId());
				map2.put("optUrl", function.getOptUrl());
				functionList.add(map2);
			}
			roleMap.put("functionList", functionList);
		}
		redisClientTemplate.setString(userCacheRoleKey, JSONObject.fromObject(roleMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(roleMap).toString());
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage getRedisUserMenu(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		String userCacheMenuKey = UserMenuprifix + appId + "_" + appUserId;
		Map<String, List<Map<String, Object>>> menuMap = new HashMap<String, List<Map<String, Object>>>();
		Set<String> menuIdSet=new HashSet<String>();
		/* 缓存中是否存在  存在返回 */
		String jsonString = redisClientTemplate.getString(userCacheMenuKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}
		//resourceList
    	String privilegeResourceIds=privilegeUser.getResourceId();
    	
    	if(privilegeResourceIds!=null&&!("").equals(privilegeResourceIds)){
    		String[] resourceIds1 = privilegeResourceIds.split(",");//将当前user privilegeResourceIds字段数组转list
    		List<String> resourceIdList = new ArrayList<String>();
    		Collections.addAll(resourceIdList, resourceIds1);
			PrivilegeResource resource=null;
    		for(String resourceId : resourceIdList){
    			resource=privilegeResourceService.findByResourceId(resourceId);
    			if (resource.getMenuId()!=null&&!("").equals(resource.getMenuId())) {
    				menuIdSet.add(resource.getMenuId());
				}
    		}
    		
    	}
    	
    	//menuList
    	
    	if (menuIdSet!=null) {
			List<Map<String, Object>> menuList=new ArrayList<Map<String,Object>>();
			for(String menuId:menuIdSet){
				
				Map<String, Object> map2=privilegeMenuService.findByMenuId(menuId);
				
				menuList.add(map2);
			}
				menuMap.put("menuList", menuList);
		}
    	
    	redisClientTemplate.setString(userCacheMenuKey, JSONObject.fromObject(menuMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(menuMap).toString());
		return ajaxMessage;
	}

}
