package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.open.opensass.privilege.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("privilegeUserRedisService")
public class PrivilegeUserRedisServiceImpl implements PrivilegeUserRedisService {
    private static final String prefix = RedisConstant.USERROLE_CACHE;
    public static final String SIGN = RedisConstant.SIGN;
    // 角色缓存版本key
    private static final String roleVersionRedisPrefix = RedisConstant.ROLEVERSIONCACHE;
    private static final Logger log = LoggerFactory
            .getLogger(PrivilegeUserRedisServiceImpl.class);
    // 应用组织机构版本缓存前缀
    private static final String groupVersionCachePerfix = RedisConstant.GROUPVERSIONCACHE;
    @Autowired
    private AppService appService;
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private PrivilegeRoleService privilegeRoleService;
    @Autowired
    private PrivilegeResourceService privilegeResourceService;
    @Autowired
    private PrivilegeFunctionService privilegeFunctionService;
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Autowired
    private PrivilegeRoleResourceService privilegeRoleResourceService;
    @Autowired
    private PrivilegeGroupService privilegeGroupService;

    @Override
    public PrivilegeAjaxMessage getRedisUserRole(String appId, String appUserId) {

        PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
        /* 验证用户是否存在 */
        PrivilegeUser privilegeUser = privilegeUserService
                .findByAppIdAndUserId(appId, appUserId);
        if (null == privilegeUser) {
            ajaxMessage.setCode("0");
            ajaxMessage.setMessage("User Is Null");
            return ajaxMessage;
        }
        String privilegeResourceIds = privilegeUser.getResourceId();
        String privilegeFunctionIds = privilegeUser.getPrivilegeFunId();
        Map<String, Object> roleMap = new HashMap<String, Object>();
        // 用户组织机构Id
        String groupId = privilegeUser.getGroupId();
        // 组织机构版本号
        String groupVersion = null;
        if (groupId != null && !groupId.isEmpty()) {
            groupVersion = String.valueOf(redisClientTemplate.getObject(groupVersionCachePerfix + appId + SIGN + groupId));
        }
        // redis key
        String userCacheRoleKey = prefix + appId + SIGN + appUserId;
        /*	log.info("获取缓存");
        String jsonString = redisClientTemplate.getString(userCacheRoleKey);

		if (null != jsonString && jsonString.length() > 0) {
			JSONObject object = JSONObject.fromObject(jsonString);
			List<Map<String, Object>> roles = JSONArray.toList(object.getJSONArray("roleList"), Map.class);
			// 如果用户拥有角色，获取该角色的版本号，与用户缓存的版本号对比，若不相同则更新用户缓存
			if (roles.size() > 0) {
				for (Map<String, Object> map : roles) {
					String roleId = (String) map.get("privilegeRoleId");
					Integer roleVersion = (Integer) redisClientTemplate.getObject(roleVersionRedisPrefix + appId + SIGN
									+ roleId);
					if (roleVersion != null) {
						Integer userRoleVersion = (Integer) map.get("version");
						if (userRoleVersion == null) {
							privilegeMenuService.updateMenuRedis(appId,
									appUserId);
							return updateUserRoleRedis(appId, appUserId);
						} else {
							if (!roleVersion.equals(userRoleVersion)) {
								privilegeMenuService.updateMenuRedis(appId,
										appUserId);
								return updateUserRoleRedis(appId, appUserId);
							}
						}
					}
				}
				// 获取组织机构的版本号，与用户缓存的组织机构版本号对比，若不相同则更新用户缓存
				if (groupVersion != null) {
					Integer userGroupVersion = (Integer) object
							.get("groupVersion");
					if (userGroupVersion == null) {
						privilegeMenuService.updateMenuRedis(appId, appUserId);
						return updateUserRoleRedis(appId, appUserId);
					} else {
						if (!userGroupVersion.equals(groupVersion)) {
							privilegeMenuService.updateMenuRedis(appId,
									appUserId);
							return updateUserRoleRedis(appId, appUserId);
						}
					}
				}
				ajaxMessage.setCode("1");
				ajaxMessage.setMessage(jsonString);
				return ajaxMessage;
			}
		}*/
        log.info("从数据库获取数据");
        List<Map<String, Object>> roles = privilegeRoleService
                .getRoleListByUserId(appUserId, appId);
        if (roles.size() > 0) {
            for (Map<String, Object> map : roles) {
                String roleId = (String) map.get("privilegeRoleId");
                Integer roleVersion = (Integer) redisClientTemplate
                        .getObject(roleVersionRedisPrefix + appId + SIGN
                                + roleId);
                if (roleVersion != null) {
                    map.put("version", roleVersion);
                }
            }
        }
        // 如果用户所在的组织机构版本号不为null,加入用户组织机构版本号
        if (groupVersion != null) {
            roleMap.put("groupVersion", groupVersion);
        }
        roleMap.put("roleList", roles);
        List<PrivilegeRole> roleList = privilegeRoleService
                .getRoleListByUserIdAndAppId(appUserId, appId);
        List resourceList = null;
        int Type = 1;// 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
        for (PrivilegeRole role : roleList) {
            if (role.getRoleType() != null) {
                if (role.getRoleType() == 2) {// 若角色为系统管理员 则把app拥有的所有资源放入缓存
                    if (role.getGroupId() != null
                            && !role.getGroupId().isEmpty()) {// 若该管理员为组织机构管理员
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
            List<String> FunIds = privilegeRoleResourceService
                    .findUserResourcesFunId(appId, appUserId);
            // 加入 user表中functionIds
            if (privilegeFunctionIds != null
                    && !("").equals(privilegeFunctionIds)) {
                FunIds.add(privilegeFunctionIds);
            }
            resourceList = privilegeResourceService.getResourceListByUserId(
                    appUserId, appId);

            if (FunIds != null && !("").equals(FunIds)
                    && !("null").equals(FunIds)) {
                for (String string : FunIds) {
                    if (string != null && !("").equals(string)
                            && !("null").equals(string)) {
                        String[] FunctionIds = string.split(",");
                        if (FunctionIds != null && FunctionIds.length > 0) {
                            List<Map<String, Object>> resources = privilegeResourceService
                                    .getResourceListByFunIds(FunctionIds, appId);
                            resourceList.addAll(resources);
                        }
                    }
                }
            }

            if (privilegeResourceIds != null
                    && !("").equals(privilegeResourceIds)
                    && !("null").equals(privilegeResourceIds)) {
                String[] resourceIds1 = privilegeResourceIds.split(",");// 将当前user
                // privilegeResourceIds字段数组转list
                List<String> resourceIdList = new ArrayList<String>();
                Collections.addAll(resourceIdList, resourceIds1);
                PrivilegeResourceVo resource = null;
                List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findByResourceIds(resourceIds1, appId);
                for (PrivilegeResource res : privilegeResourceList) {
                    if (res.getResourceId() != null
                            && !("").equals(res.getResourceId())) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("appId", res.getAppId());
                        map2.put("resourceId", res.getResourceId());
                        map2.put("resourceLevel", res.getResourceLevel());
                        map2.put("resourceName", res.getResourceName());
                        map2.put("resourceRule", res.getResourceRule());
                        map2.put("dislayOrder ", res.getDisplayOrder());
                        map2.put("menuId", res.getMenuId());
                        map2.put("baseUrl", res.getBaseUrl());
                        map2.put("status", res.getStatus());
                        // 通过查roleResource表 user表中functionId 获取的resource 是否包含
                        // user表中resource
                        if (!resourceList.contains(map2)) {
                            resourceIds.add(res.getResourceId());
                        }
                        resourceList.add(map2);
                    }
                }
//				for (String resourceId : resourceIdList) {
//					resource = privilegeResourceService.findByResource_Id(
//							resourceId, appId);
//					if (resource.getResourceId() != null
//							&& !("").equals(resource.getResourceId())) {
//						Map<String, Object> map2 = new HashMap<String, Object>();
//						map2.put("appId", resource.getAppId());
//						map2.put("resourceId", resource.getResourceId());
//						map2.put("resourceLevel", resource.getResourceLevel());
//						map2.put("resourceName", resource.getResourceName());
//						map2.put("resourceRule", resource.getResourceRule());
//						map2.put("dislayOrder ", resource.getDisplayOrder());
//						map2.put("menuId", resource.getMenuId());
//						map2.put("baseUrl", resource.getBaseUrl());
//						map2.put("status", resource.getStatus());
//						// 通过查roleResource表 user表中functionId 获取的resource 是否包含
//						// user表中resource
//						if (!resourceList.contains(map2)) {
//							resourceIds.add(resourceId);
//						}
//						resourceList.add(map2);
//					}
//
//				}
            }
            resourceSet.addAll(resourceList);
            if (Type == 3) {
                // 如果为机构管理员，返回该组织机构的资源
                PrivilegeAjaxMessage message = privilegeGroupService
                        .findGroupPrivilege(privilegeUser.getGroupId(), appId);
                if (!("0").equals(message.getCode())) {
                    JSONObject obj = JSONObject
                            .fromObject(message.getMessage());// 将json字符串转换为json对象
                    JSONArray objArray = JSONArray.fromObject(obj
                            .get("resourceList"));
                    List<PrivilegeResourceVo> PrivilegeResourceVo = JSONArray
                            .toList(objArray, PrivilegeResourceVo.class);
                    resourceSet.addAll(PrivilegeResourceVo);
                }
            }
            roleMap.put("resourceList", resourceSet);
        } else {
            // 如果为管理员，返回应用资源缓存
            PrivilegeAjaxMessage message = privilegeResourceService
                    .getAppResRedis(appId);
            JSONObject obj = JSONObject.fromObject(message.getMessage());// 将json字符串转换为json对象
            JSONArray objArray = JSONArray.fromObject(obj.get("resourceList"));
            roleMap.put("resourceList", objArray);

        }
		/* functionList 判断是否为管理员， */
        // 如果为管理员，返回应用功能缓存
        if (Type == 2) {
            PrivilegeAjaxMessage message = privilegeResourceService
                    .getAppResRedis(appId);
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
                String resourceId = roleResource.getResourceId();
                if (resourceId != null && !resourceId.isEmpty()) {
                    List<Map<String, Object>> funcList = privilegeFunctionService
                            .getFunctionByRId(roleResource.getResourceId(),
                                    appId);
                    privilegeFunctions.addAll(funcList);
                }
                if (roleResource.getPrivilegeFunId() != null
                        && !roleResource.getPrivilegeFunId().isEmpty()) {
                    FunIds.add(roleResource.getPrivilegeFunId());
                }
            }

            // 加入 user表中functionIds
            if (privilegeFunctionIds != null
                    && !("").equals(privilegeFunctionIds)) {
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
            Set<Map<String, Object>> functionSet = new HashSet<Map<String, Object>>();
            functionSet.addAll(privilegeFunctions);
            // 如果为机构管理员，返回该组织机构功能
            if (Type == 3) {
                // 查询单独的resource 包含的function
                if (resourceIds.size() > 0) {
                    for (String resourceId : resourceIds) {
                        List<Map<String, Object>> list = privilegeFunctionService
                                .getFunctionMap(resourceId, appId);
                        privilegeFunctions.addAll(list);
                    }
                }
                functionSet.addAll(privilegeFunctions);
                // 该应用的资源的缓存
                PrivilegeAjaxMessage appMessage = privilegeResourceService
                        .getAppResRedis(appId);
                JSONObject obj = JSONObject.fromObject(appMessage.getMessage());// 将json字符串转换为json对象
                // 该组织机构的缓存
                PrivilegeAjaxMessage message = privilegeGroupService
                        .findGroupPrivilege(privilegeUser.getGroupId(), appId);
                if (!("0").equals(message.getCode())) {
                    // 该应用拥有的功能
                    List<Map<String, Object>> functionList = (List<Map<String, Object>>) obj
                            .get("functionList");
                    JSONObject object = JSONObject.fromObject(message
                            .getMessage());// 将json字符串转换为json对象
                    // 该组织机构拥有的资源
                    List<Map<String, Object>> resources = (List<Map<String, Object>>) object
                            .get("resourceList");
                    List<Map<String, Object>> functions = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> resource : resources) {
                        for (Map<String, Object> function : functionList) {
                            if (function.get("resourceId").equals(
                                    resource.get("resourceId"))) {
                                functions.add(function);
                            }
                        }
                    }
                    functionSet.addAll(functions);
                }
            }
            roleMap.put("functionList", functionSet);
        }

        // 放入缓存
        redisClientTemplate.setString(userCacheRoleKey,
                JSONObject.fromObject(roleMap).toString());
        ajaxMessage.setCode("1");
        ajaxMessage.setMessage(JSONObject.fromObject(roleMap).toString());
        return ajaxMessage;
    }

    @Override
    public PrivilegeAjaxMessage delUserRoleRedis(String appId, String appUserId) {
        PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
        Boolean boolean1 = redisClientTemplate.del(prefix + appId + SIGN
                + appUserId);
        // Boolean UserRoleKeyExist = redisDao.deleteRedisKey(prefix, appId,
        // appUserId);
        ajaxMessage.setCode("1");
        ajaxMessage.setMessage(boolean1 ? "Success" : "Failed");
        log.info("delMenuRedis接口删除：" + boolean1);
        return ajaxMessage;
    }

    @Override
    public PrivilegeAjaxMessage updateUserRoleRedis(String appId,
                                                    String appUserId) {
        /*boolean RedisKeyExist = redisDao
                .existKeyRedis(prefix, appId, appUserId);*/
        boolean RedisKeyExist = redisClientTemplate
                .existKey(prefix+appId+RedisConstant.SIGN+appUserId);
        if (RedisKeyExist) {
            delUserRoleRedis(appId, appUserId);
        }
        log.info("更新redis");
        return getRedisUserRole(appId, appUserId);
    }

    @Override
    public Boolean delUserCaches(String appId, String appUserId) {
        try {
            //用户redis缓存key拼接,删除
            //privilegeService_userAllCacheInfo_appid_appuserid
            StringBuilder redisUserAllPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
            redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
            redisUserAllPrivilegeKey.append(appId);
            redisUserAllPrivilegeKey.append(SIGN);
            redisUserAllPrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUserAllPrivilegeKey.toString())) {
                redisClientTemplate.del(redisUserAllPrivilegeKey.toString());
            }
            //清空大缓存
            //privilegeService_userCacheInfo_appid_appuserid
            StringBuilder redisUserPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
            redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
            redisUserPrivilegeKey.append(appId);
            redisUserPrivilegeKey.append(SIGN);
            redisUserPrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUserPrivilegeKey.toString())) {
                redisClientTemplate.del(redisUserPrivilegeKey.toString());
            }
            //清空角色以及url
            //privilegeService_userCacheUrl_appid_appuserid
            StringBuilder redisUserUrlPrivilegeKey = new StringBuilder(RedisConstant.USERPRIVILEGES_CACHE);
            redisUserUrlPrivilegeKey.append(appId);
            redisUserUrlPrivilegeKey.append(SIGN);
            redisUserUrlPrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUserUrlPrivilegeKey.toString())) {
                redisClientTemplate.del(redisUserUrlPrivilegeKey.toString());
            }
            //清空用户菜单缓存
            //privilegeService_userCacheMenus_appid_appuserid
            StringBuilder redisUserCacheMenusPrivilegeKey = new StringBuilder(RedisConstant.USERMENU_CACHE);
            redisUserCacheMenusPrivilegeKey.append(appId);
            redisUserCacheMenusPrivilegeKey.append(SIGN);
            redisUserCacheMenusPrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUserCacheMenusPrivilegeKey.toString())) {
                redisClientTemplate.del(redisUserCacheMenusPrivilegeKey.toString());
            }
            //清空用户角色缓存
            //privilegeService_userCacheMenus_appid_appuserid
            StringBuilder redisUserRoleCachePrivilegeKey = new StringBuilder(RedisConstant.USERROLE_CACHE);
            redisUserRoleCachePrivilegeKey.append(appId);
            redisUserRoleCachePrivilegeKey.append(SIGN);
            redisUserRoleCachePrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUserRoleCachePrivilegeKey.toString())) {
                redisClientTemplate.del(redisUserRoleCachePrivilegeKey.toString());
            }
            //清空用户菜单缓存
            //privilegeService_userCacheMenus_appid_appuserid
            StringBuilder redisUseMenuCachePrivilegeKey = new StringBuilder(RedisConstant.APPMENUVERSIONCACHE);
            redisUseMenuCachePrivilegeKey.append(appId);
            redisUseMenuCachePrivilegeKey.append(SIGN);
            redisUseMenuCachePrivilegeKey.append(appUserId);
            if (redisClientTemplate.existKey(redisUseMenuCachePrivilegeKey.toString())) {
                redisClientTemplate.del(redisUseMenuCachePrivilegeKey.toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
