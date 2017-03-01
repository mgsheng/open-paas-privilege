package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("privilegeUserRedisService")
public class PrivilegeUserRedisServiceImpl implements PrivilegeUserRedisService {
	private static final String prefix = RedisConstant.USERROLE_CACHE;
	public static final String SIGN = RedisConstant.SIGN;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeUserRedisServiceImpl.class);
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
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;

	@Override
	public PrivilegeAjaxMessage getRedisUserRole(String appId, String appUserId) {

		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 验证用户是否存在 */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		String privilegeResourceIds = privilegeUser.getResourceId();
		String privilegeFunctionIds = privilegeUser.getPrivilegeFunId();
		Map<String, Object> roleMap = new HashMap<String, Object>();

		// redis key
		String userCacheRoleKey = prefix + appId + SIGN + appUserId;
		/* 缓存中是否存在 存在返回 */
		log.info("获取缓存");
		String jsonString = redisClientTemplate.getString(userCacheRoleKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}
		log.info("从数据库获取数据");
		List<Map<String, Object>> roles = privilegeRoleService.getRoleListByUserId(appUserId, appId);
		roleMap.put("roleList", roles);
		List<PrivilegeRole> roleList = privilegeRoleService.getRoleListByUserIdAndAppId(appUserId, appId);
		List resourceList = null;
		int Type = 1;// 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
		for (PrivilegeRole role : roleList) {
			if (role.getRoleType() != null) {
				if (role.getRoleType() == 2) {// 若角色为系统管理员 则把app拥有的所有资源放入缓存
					if (role.getGroupId() != null && !role.getGroupId().isEmpty()) {// 若该管理员为组织机构管理员
						Type = 3;
					} else {
						Type = 2;
					}
					break;
				}
			}
		}
		// user表多余的resourceId
		List<String> resourceIds = new ArrayList<String>();
		// resourceList
		Set resourceSet = new HashSet();
		if (Type != 2) {
			// roleResource 中functionId
			List<String> FunIds = privilegeRoleResourceService.findUserResourcesFunId(appId, appUserId);
			// 加入 user表中functionIds
			if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
				FunIds.add(privilegeFunctionIds);
			}
			resourceList = privilegeResourceService.getResourceListByUserId(appUserId, appId);
			if (FunIds != null && !("").equals(FunIds) && !("null").equals(FunIds)) {
				for (String string : FunIds) {
					if (string != null && !("").equals(string) && !("null").equals(string)) {
						String[] FunctionIds = string.split(",");
						if (FunctionIds != null && FunctionIds.length > 0) {
							List<Map<String, Object>> resources = privilegeResourceService
									.getResourceListByFunIds(FunctionIds, appId);
							resourceList.addAll(resources);
						}
					}
				}
			}

			if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)
					&& !("null").equals(privilegeResourceIds)) {
				String[] resourceIds1 = privilegeResourceIds.split(",");// 将当前user
																		// privilegeResourceIds字段数组转list
				List<String> resourceIdList = new ArrayList<String>();
				Collections.addAll(resourceIdList, resourceIds1);
				PrivilegeResourceVo resource = null;
				for (String resourceId : resourceIdList) {
					resource = privilegeResourceService.findByResource_Id(resourceId, appId);
					if (resource.getResourceId() != null && !("").equals(resource.getResourceId())) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("appId", resource.getAppId());
						map2.put("resourceId", resource.getResourceId());
						map2.put("resourceLevel", resource.getResourceLevel());
						map2.put("resourceName", resource.getResourceName());
						map2.put("resourceRule", resource.getResourceRule());
						map2.put("dislayOrder ", resource.getDisplayOrder());
						map2.put("menuId", resource.getMenuId());
						map2.put("baseUrl", resource.getBaseUrl());
						map2.put("status", resource.getStatus());
						// 通过查roleResource表 user表中functionId 获取的resource 是否包含
						// user表中resource
						if (!resourceList.contains(map2)) {
							resourceIds.add(resourceId);
						}
						resourceList.add(map2);
					}

				}
			}
			resourceSet.addAll(resourceList);
			if (Type == 3) {
				// 如果为机构管理员，返回该组织机构的资源
				PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(privilegeUser.getGroupId(),
						appId);
				if (!("0").equals(message.getCode())) {
					JSONObject obj = JSONObject.fromObject(message.getMessage());// 将json字符串转换为json对象
					JSONArray objArray = JSONArray.fromObject(obj.get("resourceList"));
					List<PrivilegeResourceVo> PrivilegeResourceVo=JSONArray.toList(objArray, PrivilegeResourceVo.class);
					resourceSet.addAll(PrivilegeResourceVo);
				} 
			}
			roleMap.put("resourceList", resourceSet);
		} else {
			// 如果为管理员，返回应用资源缓存
			PrivilegeAjaxMessage message = privilegeResourceService.getAppResRedis(appId);
			JSONObject obj = JSONObject.fromObject(message.getMessage());// 将json字符串转换为json对象
			JSONArray objArray = JSONArray.fromObject(obj.get("resourceList"));
			roleMap.put("resourceList", objArray);

		}
		/* functionList 判断是否为管理员， */
		// 如果为管理员，返回应用功能缓存
		if (Type == 2) {
			PrivilegeAjaxMessage message = privilegeResourceService.getAppResRedis(appId);
			JSONObject obj1 = JSONObject.fromObject(message.getMessage());// 将json字符串转换为json对象
			JSONArray objArray = JSONArray.fromObject(obj1.get("functionList"));
			roleMap.put("functionList", objArray);
		} else {
			// functionList
			List<String> FunIds = new ArrayList<String>();
			List<Map<String, Object>> privilegeFunctions = new ArrayList<Map<String, Object>>();
			List<PrivilegeRoleResource> rivilegeRoleResources = privilegeRoleResourceService
					.findUserRoleResources(appId, appUserId);
			for (PrivilegeRoleResource roleResource : rivilegeRoleResources) {
				if (roleResource.getPrivilegeFunId() == null || ("").equals(roleResource.getPrivilegeFunId())) {
					List<Map<String, Object>> functions = privilegeFunctionService
							.getFunctionByRId(roleResource.getResourceId(), appId);
					privilegeFunctions.addAll(functions);
				} else {
					// roleResource表中functionIds
					FunIds.add(roleResource.getPrivilegeFunId());
				}
			}

			// 加入 user表中functionIds
			if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
				FunIds.add(privilegeFunctionIds);
			}
			// 查询相应function
			if (FunIds != null && FunIds.size() > 0) {
				for (String funIds : FunIds) {
					String[] functionIds = funIds.split(",");
					List<Map<String, Object>> functions = privilegeFunctionService
							.getFunctionListByFunctionIds(functionIds, appId);
					privilegeFunctions.addAll(functions);
				}
			}
			// 查询单独的resource 包含的function
			if (resourceIds.size() > 0) {
				for (String resourceId : resourceIds) {
					List<Map<String, Object>> list = privilegeFunctionService.getFunctionMap(resourceId, appId);
					privilegeFunctions.addAll(list);
				}
			}
			Set<Map<String, Object>> functionSet = new HashSet<Map<String, Object>>();
			functionSet.addAll(privilegeFunctions);
			// 如果为机构管理员，返回该组织机构功能
			if (Type == 3) {
				// 该应用的资源的缓存
				PrivilegeAjaxMessage appMessage = privilegeResourceService.getAppResRedis(appId);
				JSONObject obj = JSONObject.fromObject(appMessage.getMessage());// 将json字符串转换为json对象
				// 该组织机构的缓存
				PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(privilegeUser.getGroupId(),
						appId);
				if (!("0").equals(message.getCode())) {
					// 该应用拥有的功能
					List<Map<String, Object>> functionList = (List<Map<String, Object>>) obj.get("functionList");
					JSONObject object = JSONObject.fromObject(message.getMessage());// 将json字符串转换为json对象
					// 该组织机构拥有的资源
					List<Map<String, Object>> resources = (List<Map<String, Object>>) object.get("resourceList");
					List<Map<String, Object>> functions = new ArrayList<Map<String, Object>>();
					// 遍历选出该组织机构拥有的功能
					for (Map<String, Object> resource : resources) {
						for (Map<String, Object> function : functionList) {
							if (function.get("resourceId").equals(resource.get("resourceId"))) {
								functions.add(function);
							}
						}
					}
					functionSet.addAll(functions);
				}
			}
		}

		// 放入缓存
		redisClientTemplate.setString(userCacheRoleKey, JSONObject.fromObject(roleMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(roleMap).toString());
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage delUserRoleRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Boolean boolean1 = redisClientTemplate.del(prefix + appId + SIGN + appUserId);
		// Boolean UserRoleKeyExist = redisDao.deleteRedisKey(prefix, appId,
		// appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(boolean1 ? "Success" : "Failed");
		log.info("delMenuRedis接口删除：" + boolean1);
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage updateUserRoleRedis(String appId, String appUserId) {
		boolean RedisKeyExist = redisDao.existKeyRedis(prefix, appId, appUserId);
		if (RedisKeyExist) {
			delUserRoleRedis(appId, appUserId);
		}
		log.info("更新redis");
		return getRedisUserRole(appId, appUserId);
	}

}
