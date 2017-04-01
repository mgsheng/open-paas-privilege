package cn.com.open.opensass.privilege.service.impl;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jh on 2016/12/16.
 */
@Service("privilegeUrlService")
public class PrivilegeUrlServiceImpl implements PrivilegeUrlService {
	private static final Logger log = LoggerFactory.getLogger(PrivilegeUrlServiceImpl.class);
	private static final String prifix = RedisConstant.USERPRIVILEGES_CACHE;
	private static final String SIGN = RedisConstant.SIGN;
	private static final String roleVersionPrifix = RedisConstant.ROLEVERSIONCACHE;
	private static final String jsonKeyName = "urlList";
	//应用组织机构版本缓存前缀
	private static final String groupVersionCachePerfix = RedisConstant.GROUPVERSIONCACHE;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	

	@Override
	public PrivilegeAjaxMessage getRedisUrl(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		log.info("getRedisUrl用户数据，appid=" + appId + ",用户Id=" + appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		//用户组织机构Id
		String groupId = privilegeUser.getGroupId();
		//组织机构版本号
		Integer groupVersion = (Integer) redisClientTemplate.getObject(groupVersionCachePerfix + appId + SIGN
						+ groupId);
		/* 缓存中是否存在 */
		String key = prifix + appId +SIGN +appUserId; 
		String urlJedis = redisClientTemplate.getString(key);
		if (urlJedis != null ) {
			//如果用户拥有角色，获取该角色的版本号，与用户缓存的版本号对比，若不相同则更新用户url缓存
			JSONObject object = JSONObject.fromObject(urlJedis);
			JSONArray roleArray = object.getJSONArray("roleList");
			if (roleArray != null) {
				List<Map<String, Object>> roles = JSONArray.toList(roleArray,Map.class);
				if (roles.size()>0) {
					for (Map<String, Object> role : roles) {
						String privilegeRoleId = (String) role.get("privilegeRoleId");
						Integer roleVersion = (Integer) redisClientTemplate.getObject(roleVersionPrifix+appId+SIGN+privilegeRoleId);
						if (roleVersion != null) {
							Integer userRoleVersion = (Integer) role.get("version");
							if (userRoleVersion == null) {
								return updateRedisUrl(appId, appUserId);
							} else {
								if (!userRoleVersion.equals(roleVersion)) {
									return updateRedisUrl(appId, appUserId);
								}
							}
							
						}
					}
				}
			} else {
				return updateRedisUrl(appId, appUserId);
			}
			//获取组织机构的版本号，与用户缓存的组织机构版本号对比，若不相同则更新用户缓存
			if (groupVersion != null) {
				Integer userGroupVersion =  (Integer) object.get("groupVersion");
				if (userGroupVersion == null) {
					return updateRedisUrl(appId, appUserId);
				} else {
					if (!userGroupVersion.equals(groupVersion)) {
						return updateRedisUrl(appId, appUserId);
					}
				}
			}
			//如果用户拥有角色，获取该角色的版本号，与用户缓存的版本号对比，若不相同则更新用户缓存
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(urlJedis);
			return ajaxMessage;
		} else {
			int Type = 1;// 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
			List<PrivilegeRole> roles = privilegeRoleService.getRoleListByUserIdAndAppId(appUserId, appId);
			for (PrivilegeRole role : roles) {
				if (role.getRoleType() != null) {
					if (role.getRoleType() == 2) {
						if (role.getGroupId() != null && !role.getGroupId().isEmpty()) {
							Type = 3;
						} else {// 角色组织机构id为空，为系统管理员
							Type = 2;
						}
						break;
					}
				}
			}
			List<Map<String, Object>>  roleList = new ArrayList<Map<String,Object>>();
			if (roles.size()>0) {
				for (PrivilegeRole privilegeRole : roles) {
					Integer roleVersion = (Integer) redisClientTemplate.getObject(roleVersionPrifix+appId+SIGN+privilegeRole.getPrivilegeRoleId());
					Map<String, Object> role = new HashMap<String, Object>();
					role.put("privilegeRoleId", privilegeRole.getPrivilegeRoleId());
					//如果该角色版本号不为null，加入用户url角色的版本号
					if (roleVersion != null) {
						role.put("version", roleVersion);
					}
					roleList.add(role);
				}
			}
			
			PrivilegeUrl url = null;
			if (Type == 2) {
				url = getAllPrivilegeUrl(appId);
			} else {
				url = getPrivilegeUrl(appId, appUserId, privilegeUser, Type);
			}
			String json = url.getPrivilegeUrl();
			JSONObject object = JSONObject.fromObject(json);
			List<String> urlList = (List<String>) object.get("urlList");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("urlList", urlList);
			map.put("roleList", roleList);
			//如果用户所在的组织机构版本号不为null,加入用户组织机构版本号
			if (groupVersion != null) {
				map.put("groupVersion", groupVersion);
			}
			/* 写入redis */
			log.info("getRedisUrl接口获取数据并写入redis数据开始");
			redisClientTemplate.setString(key,JSONObject.fromObject(map).toString());
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(JSONObject.fromObject(map).toString());
			return ajaxMessage;
		}
	}

	@Override
	public PrivilegeAjaxMessage updateRedisUrl(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		log.info("updateDataPrivilege用户数据，appid=" + appId + ",用户Id=" + appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("USER-NULL");
			return ajaxMessage;
		}

		/* 删除redis健值 */
		log.info("updateRedisUrl删除redis数据");
		redisDao.deleteRedisKey(prifix, appId, appUserId);
		return getRedisUrl(appId, appUserId);
	}

	@Override
	public PrivilegeAjaxMessage deleteRedisUrl(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		log.info("deleteDataPrivilege删除redis数据");
		boolean ok = redisDao.deleteRedisKey(prifix, appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(ok ? "Success" : "Failed");
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage existRedisUrl(String appId, String appUserId, String url) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		boolean exist = redisDao.existUrlRedis(prifix, jsonKeyName, url, appId, appUserId);
		log.info("existUrlPrivilege==url是否存在：" + exist);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(exist ? "TRUE" : "FALSE");
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage existRedisKey(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		log.info("deleteDataPrivilege删除redis数据");
		boolean exist = redisDao.existKeyRedis(prifix, appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(exist ? "TRUE" : "FALSE");
		return ajaxMessage;
	}

	@Override
	public PrivilegeUrl getGroupPrivilegeUrl(String appId, String groupId) {
		PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(groupId, appId);
		// 应用资源缓存
		PrivilegeAjaxMessage appAjaxMessage = privilegeResourceService.getAppResRedis(appId);
		JSONObject appObject = JSONObject.fromObject(appAjaxMessage.getMessage());
		// 应用功能缓存
		List<Map<String, Object>> functionList = (List<Map<String, Object>>) appObject.get("functionList");
		/* 排重处理 */
		Set<String> setUrl = new HashSet<String>();
		if (message.getCode().equals("1")) {// 获取该组织机构缓存，得到该组织机构资源，功能url
			JSONObject JsonObject = JSONObject.fromObject(message.getMessage());
			List<Map<String, Object>> resourceList = (List<Map<String, Object>>) JsonObject.get("resourceList");
			for (Map<String, Object> resource : resourceList) {
				if (resource.get("baseUrl") != null && !("").equals(resource.get("baseUrl"))) {
					setUrl.add((String) resource.get("baseUrl"));
				}
				for (Map<String, Object> function : functionList) {
					if (function.get("resourceId").equals(resource.get("resourceId"))) {
						if (function.get("optUrl") != null && !("").equals(function.get("optUrl"))) {
							setUrl.add((String) function.get("optUrl"));
						}
					}
				}
			}
		}
		PrivilegeUrl privilegeUrl = new PrivilegeUrl();
		Map<String, Object> url = new HashMap<String, Object>();
		url.put(jsonKeyName, setUrl);
		privilegeUrl.setPrivilegeUrl(JSONObject.fromObject(url).toString());
		return privilegeUrl;
	}

	@Override
	public PrivilegeUrl getPrivilegeUrl(String appId, String appUserId, PrivilegeUser privilegeUser, int Type) {

		/* 排重处理 */
		Set<String> setUrl = new HashSet<String>();
		/* 资源 */
		ArrayList<String> urlList = privilegeUserService.findUserResources(appId, appUserId);
		if (null != urlList && urlList.size() > 0) {
			for (String string : urlList) {
				if (null != string && string.length() > 0) {
					setUrl.add(string);
				}
			}
		}
		/* roleResource表中 资源与function */
		List<PrivilegeRoleResource> rivilegeRoleResources = privilegeRoleResourceService.findUserRoleResources(appId,
				appUserId);
		for (PrivilegeRoleResource roleResource : rivilegeRoleResources) {
			if (roleResource != null) {
				//如果资源Id不为null,查询资源和该资源下的功能
				if (roleResource.getResourceId() != null && !roleResource.getResourceId().isEmpty()) {
					PrivilegeResource privilegeResource = privilegeResourceService
							.findByResourceId(roleResource.getResourceId(), appId);
					if (null != privilegeResource && null != privilegeResource.getBaseUrl()
							&& privilegeResource.getBaseUrl().length() > 0) {
						setUrl.add(privilegeResource.getBaseUrl());
					}
					List<Map<String, Object>> funcList = privilegeFunctionService.getFunctionByRId(roleResource.getResourceId(), appId);
					for (Map<String, Object> map : funcList) {
						if (map.get("optUrl") != null && !("").equals(map.get("optUrl"))) {
							setUrl.add((String) map.get("optUrl"));
						}
					}
				}
				//功能ID不为null，查询功能和该功能所在的资源
				if (roleResource.getPrivilegeFunId() != null && !roleResource.getPrivilegeFunId().isEmpty()) {
					String[] functionIds = roleResource.getPrivilegeFunId().split(",");
					List<Map<String, Object>> functions = privilegeFunctionService
							.getFunctionListByFunctionIds(functionIds, appId);
					for (Map<String, Object> map : functions) {
						if (map.get("optUrl") != null && !("").equals(map.get("optUrl"))) {
							setUrl.add((String) map.get("optUrl"));
						}
					}
					List<Map<String, Object>> resourceList=privilegeResourceService.getResourceListByFunIds(functionIds, appId);
					for (Map<String, Object> map : resourceList) {
						if (map.get("baseUrl") != null && !("").equals(map.get("baseUrl"))) {
							setUrl.add((String)map.get("baseUrl"));
						}
					}
				}
			}

		}

		/* 用户资源 */
		if (null != privilegeUser.getResourceId() && privilegeUser.getResourceId().length() > 0) {
			String[] resourceIds = privilegeUser.getResourceId().split(",");
			for (String resourceId : resourceIds) {
				if (null != resourceId && resourceId.length() > 0) {
					PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(resourceId, appId);
					if (null != privilegeResource && null != privilegeResource.getBaseUrl()
							&& privilegeResource.getBaseUrl().length() > 0) {
						setUrl.add(privilegeResource.getBaseUrl());
					}
				}
			}
		}
		/* 链接资源 */
		if (null != privilegeUser.getPrivilegeFunId() && privilegeUser.getPrivilegeFunId().length() > 0) {
			String[] functionIds = privilegeUser.getPrivilegeFunId().split(",");
			List<Map<String, Object>> resourceList=privilegeResourceService.getResourceListByFunIds(functionIds, appId);
			for (Map<String, Object> map : resourceList) {
				if (map.get("baseUrl")!=null&&!("").equals(map.get("baseUrl"))) {
					setUrl.add((String)map.get("baseUrl"));
				}
			}
			for (String functionId : functionIds) {
				if (null != functionId && functionId.length() > 0) {
					PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(functionId, appId);
					if (null != privilegeFunction && null != privilegeFunction.getOptUrl()
							&& privilegeFunction.getOptUrl().length() > 0) {
						setUrl.add(privilegeFunction.getOptUrl());
					}
				}
			}
		}
		// 如果用户角色为组织机构管理员，把组织机构权限加入用户权限中
		if (Type == 3) {
			PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(privilegeUser.getGroupId(), appId);
			// 应用资源缓存
			PrivilegeAjaxMessage appAjaxMessage = privilegeResourceService.getAppResRedis(appId);
			JSONObject appObject = JSONObject.fromObject(appAjaxMessage.getMessage());
			// 应用功能缓存
			List<Map<String, Object>> functionList = (List<Map<String, Object>>) appObject.get("functionList");
			if (message.getCode().equals("1")) {// 获取该组织机构缓存，得到该组织机构资源，功能url
				JSONObject JsonObject = JSONObject.fromObject(message.getMessage());
				List<Map<String, Object>> resourceList = (List<Map<String, Object>>) JsonObject.get("resourceList");
				for (Map<String, Object> resource : resourceList) {
					if (resource.get("baseUrl") != null && !("").equals(resource.get("baseUrl"))) {
						setUrl.add((String) resource.get("baseUrl"));
					}
					for (Map<String, Object> function : functionList) {
						if (function.get("resourceId").equals(resource.get("resourceId"))) {
							if (function.get("optUrl") != null && !("").equals(function.get("optUrl"))) {
								setUrl.add((String) function.get("optUrl"));
							}
						}
					}
				}
			}
		}
		PrivilegeUrl privilegeUrl = new PrivilegeUrl();
		Map<String, Object> b = new HashMap<String, Object>();
		b.put(jsonKeyName, setUrl);
		String data = JSONObject.fromObject(b).toString();
		privilegeUrl.setPrivilegeUrl(data);
		return privilegeUrl;
	}

	@Override
	public PrivilegeUrl getAllPrivilegeUrl(String appId) {

		Set<String> setUrl = new HashSet<String>();
		/* 资源 */
		ArrayList<String> urlList = (ArrayList<String>) privilegeResourceService.findAppResources(appId);
		if (null != urlList && urlList.size() > 0) {
			for (String string : urlList) {
				if (null != string && string.length() > 0) {
					setUrl.add(string);
				}
			}
		}
		/* 资源funId */
		ArrayList<String> functionIdList = (ArrayList<String>) privilegeFunctionService.findAppFunction(appId);
		if (null != functionIdList && functionIdList.size() > 0) {
			for (String functionUrl : functionIdList) {
				if (null != functionUrl && functionUrl.length() > 0) {
					setUrl.add(functionUrl);
				}
			}
		}
		PrivilegeUrl privilegeUrl = new PrivilegeUrl();
		Map b = new HashMap();
		b.put(jsonKeyName, setUrl);
		String data = JSONObject.fromObject(b).toString();
		privilegeUrl.setPrivilegeUrl(data);
		return privilegeUrl;
	}

}
