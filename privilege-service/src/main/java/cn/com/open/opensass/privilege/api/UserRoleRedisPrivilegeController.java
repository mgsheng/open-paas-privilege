package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/userRole/")
public class UserRoleRedisPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserRoleRedisPrivilegeController.class);
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
	

	/**
	 * 用户角色缓存接口
	 * 
	 */
	@RequestMapping("getUserRolePrivilege")
	public void getUserPrivilege(HttpServletRequest request, HttpServletResponse response,
			PrivilegeUserVo privilegeUserVo) {
		Map<String, List<Map<String, Object>>> map = new HashMap<String,List<Map<String, Object>>>();
		log.info("====================redis user role start======================");    	
		if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId()))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),
				privilegeUserVo.getAppUserId());
		if (user == null) {
			paraMandaChkAndReturn(10001, response, "该用户不存在");
			return;
		}

		// redis key
		String userCacheRoleKey = "privilegeService_userCacheRole_" + user.getAppId() + "_" + user.getAppUserId();
		// 取缓存
	
			if (redisClientTemplate.getString(userCacheRoleKey) != null) {
				String jString = redisClientTemplate.getString(userCacheRoleKey);
				System.err.println("缓存==" + jString);
				JSONObject  jasonObject = JSONObject.fromObject(jString);
				Map JsonMap = (Map)jasonObject;
			    writeSuccessJson(response, JsonMap);   
			    redisClientTemplate.disconnect();
				return;
			}
		
			
			
		
		
		String privilegeRoleIds = user.getPrivilegeRoleId();
		String privilegeResourceIds = user.getResourceId();
		String privilegeFunctionIds = user.getPrivilegeFunId();
		//RoleList
		if (privilegeRoleIds != null && !("").equals(privilegeRoleIds)) {
			String[] roleIds1 = privilegeRoleIds.split(",");// 将当前user privilegeRoleIds字段数组转list
			List<String> roleIdList = new ArrayList<String>();
			Collections.addAll(roleIdList, roleIds1);
			PrivilegeRole role = null;// roleList
			List<Map<String, Object>> roleList = new ArrayList<Map<String,Object>>();
			for (String roleId : roleIdList) {
				Map<String, Object> map2 = new HashMap<String,Object>();
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
			map.put("roleList", roleList);
		}
		// resourceList
		if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)) {
			String[] resourceIds1 = privilegeResourceIds.split(",");// 将当前user
																	// privilegeResourceIds字段数组转list
			List<String> resourceIdList = new ArrayList<String>();
			Collections.addAll(resourceIdList, resourceIds1);
			PrivilegeResource resource = null;
			List<Map<String, Object>> resourceList = new ArrayList<Map<String,Object>>();
			for (String resourceId : resourceIdList) {
				Map<String, Object> map2 = new HashMap<String,Object>();
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
			map.put("resourceList", resourceList);
		}
		// functionList
		if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
			String[] functionIds1 = privilegeFunctionIds.split(",");
			List<String> functionIdList = new ArrayList<String>();
			Collections.addAll(functionIdList, functionIds1);
			PrivilegeFunction function = null;
			List<Map<String, Object>> functionList = new ArrayList<Map<String,Object>>();
			for (String functionId : functionIdList) {
				Map<String, Object> map2 = new HashMap<String,Object>();
				function = privilegeFunctionService.findByFunctionId(functionId);
				map2.put("resourceId", function.getResourceId());
				map2.put("functionId", function.getId());
				map2.put("optId", function.getOperationId());
				map2.put("optUrl", function.getOptUrl());
				functionList.add(map2);
			}
			map.put("functionList", functionList);
		}
		
		
			
			redisClientTemplate.setString(userCacheRoleKey, JSONObject.fromObject(map).toString());
	
			redisClientTemplate.disconnect();
		
		String json = JSONObject.fromObject(map).toString();
		System.err.println(json + "==json");
		writeSuccessJson(response, map);


	}
}
