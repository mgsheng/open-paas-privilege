package cn.com.open.opensass.privilege.service.impl;

import cn.com.open.opensass.privilege.api.UrlRedisPrivilegeController;
import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
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
	private static final Logger log = LoggerFactory.getLogger(UrlRedisPrivilegeController.class);
	private static final String prifix = RedisConstant.USERPRIVILEGES_CACHE;
	private static final String jsonKeyName = "urlList";
	@Autowired
	private RedisDao redisDao;
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
	@Autowired 
	private PrivilegeMenuService privilegeMenuService;

	@Override
	public PrivilegeAjaxMessage getRedisUrl(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		log.info("getDataPrivilege用户数据，appid=" + appId + ",用户Id=" + appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		/* 缓存中是否存在 */
		String urlJedis = redisDao.getUrlRedis(prifix, appId, appUserId);
		log.info("getDataPrivilege接口读取redis数据：" + urlJedis);
		if (null == urlJedis || urlJedis.length() <= 0) {
			Boolean isManger = false;
			int Type=1;// 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
			List<PrivilegeRole> roles = privilegeRoleService.getRoleListByUserIdAndAppId(appUserId, appId);
			for (PrivilegeRole role : roles) {
				if (role.getRoleType() != null) {
					if (role.getRoleType() == 2) {
						if (role.getGroupId() != null && !role.getGroupId().isEmpty()) {
							Type = 3;
						} else {// 角色组织机构id为空，为系统管理员，直接返回认证成功
							Type = 2;
						}
						isManger = true;
						break;
					}
				}
			}
			PrivilegeUrl url = null;
			if (isManger) {
				if (Type==3) {
					url = getGroupPrivilegeUrl(appId, privilegeUser.getGroupId());
				}else {
					url = getAllPrivilegeUrl(appId);
				}
			} else {
				url = getPrivilegeUrl(appId, appUserId, privilegeUser);
			}

			/* 写入redis */
			log.info("getDataPrivilege接口获取数据并写入redis数据开始");
			redisDao.putUrlRedis(prifix, url, appId, appUserId);

			/* 读取redis */
			urlJedis = redisDao.getUrlRedis(prifix, appId, appUserId);
			log.info("getDataPrivilege接口获取数据并写入，读取redis数据开始：" + urlJedis);
		}
		if (null != urlJedis && urlJedis.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(urlJedis);
			return ajaxMessage;
		} else {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("NULL");
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
		log.info("updateDataPrivilege删除redis数据");
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
	public PrivilegeUrl getGroupPrivilegeUrl(String appId,String groupId){
		PrivilegeAjaxMessage message=privilegeGroupService.findGroupPrivilege(groupId, appId);
		//应用资源缓存
		PrivilegeAjaxMessage appAjaxMessage=privilegeResourceService.getAppResRedis(appId);
		JSONObject appObject= JSONObject.fromObject(appAjaxMessage.getMessage());
		//应用功能缓存
		List<Map<String, Object>> functionList=(List<Map<String, Object>>) appObject.get("functionList");
		/* 排重处理 */
		Set<String> setUrl = new HashSet<String>();
		if (message.getCode().equals("1")) {//获取该组织机构缓存，得到该组织机构资源，功能url
			JSONObject JsonObject = JSONObject.fromObject(message.getMessage());
			List<Map<String, Object>> resourceList = (List<Map<String, Object>>) JsonObject.get("resourceList");
			for (Map<String, Object> resource : resourceList) {
				if (resource.get("baseUrl")!=null&&!("").equals(resource.get("baseUrl"))) {
					setUrl.add((String)resource.get("baseUrl"));
				}
				for (Map<String, Object> function : functionList) {
					if (function.get("resourceId").equals(resource.get("resourceId"))) {
						if (function.get("optUrl")!=null&&!("").equals(function.get("optUrl"))) {
							setUrl.add((String)function.get("optUrl"));
						}
					}
				}
			}
		}
		//获取公共菜单，遍历获取公共权限url
		List<PrivilegeMenuVo> menuVos=privilegeMenuService.findMenuByResourceType(0);
		//应用资源缓存
		List<Map<String, Object>> resourceList = (List<Map<String, Object>>) appObject.get("resourceList");
		for (PrivilegeMenuVo menuVo : menuVos) {
			for (Map<String, Object> resource : resourceList) {
				if (menuVo.getMenuId().equals(resource.get("menuId"))) {
					if (resource.get("baseUrl")!=null&&!("").equals(resource.get("baseUrl"))) {
						setUrl.add((String)resource.get("baseUrl"));
					}
				}
				for (Map<String, Object> function : functionList) {
					if (function.get("resourceId").equals(resource.get("resourceId"))) {
						if (function.get("optUrl")!=null&&!("").equals(function.get("optUrl"))) {
							setUrl.add((String)function.get("optUrl"));
						}
					}
				}
			}
		}
		PrivilegeUrl privilegeUrl=new PrivilegeUrl();
		Map<String,Object> url = new HashMap<String,Object>();
		url.put(jsonKeyName, setUrl);
		privilegeUrl.setPrivilegeUrl(JSONObject.fromObject(url).toString());
		return privilegeUrl;
	}
	@Override
	public PrivilegeUrl getPrivilegeUrl(String appId, String appUserId, PrivilegeUser privilegeUser) {

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
		/* roleResource表中 资源function */
		ArrayList<String> functionIdList = new ArrayList<String>();
		List<PrivilegeRoleResource> rivilegeRoleResources = privilegeRoleResourceService.findUserRoleResources(appId,
				appUserId);
		for (PrivilegeRoleResource roleResource : rivilegeRoleResources) {
			if (roleResource!=null) {
				if (roleResource.getResourceId()!=null&&!("").equals(roleResource.getResourceId())) {
					PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(roleResource.getResourceId(), appId);
					if (null != privilegeResource && null != privilegeResource.getBaseUrl()
							&& privilegeResource.getBaseUrl().length() > 0) {
						setUrl.add(privilegeResource.getBaseUrl());
					}
				}
				if (roleResource.getPrivilegeFunId() == null || ("").equals(roleResource.getPrivilegeFunId())) {
					List<Map<String, Object>> functions = privilegeFunctionService.getFunctionByRId(roleResource.getResourceId(),appId);
					for (Map<String, Object> map : functions) {
						if(map.get("optUrl")!=null&&!("").equals(map.get("optUrl"))){
							setUrl.add((String) map.get("optUrl"));
						}
					}
				} else {
					// roleResource表中functionIds
					functionIdList.add(roleResource.getPrivilegeFunId());
				}
			}
			
		}
		
		if (null != functionIdList && functionIdList.size() > 0) {
			for (String functionId : functionIdList) {
				String[] functionIds = functionId.split(",");
				for (String fid : functionIds) {
					if (null != fid && fid.length() > 0) {
						// 通过 roleResource表 functionId 查找资源
						PrivilegeResource resource = privilegeResourceService.getResourceListByFunId(fid,appId);
						if (null != resource && null != resource.getBaseUrl()) {
							setUrl.add(resource.getBaseUrl());
						}
						PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(fid,appId);
						if (null != privilegeFunction && null != privilegeFunction.getOptUrl()
								&& privilegeFunction.getOptUrl().length() > 0) {
							setUrl.add(privilegeFunction.getOptUrl());
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
			for (String functionId : functionIds) {
				if (null != functionId && functionId.length() > 0) {
					PrivilegeResource resource = privilegeResourceService.getResourceListByFunId(functionId,appId);
					if (null != resource && null != resource.getBaseUrl()) {
						setUrl.add(resource.getBaseUrl());
					}

					PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(functionId,appId);
					if (null != privilegeFunction && null != privilegeFunction.getOptUrl()
							&& privilegeFunction.getOptUrl().length() > 0) {
						setUrl.add(privilegeFunction.getOptUrl());
					}
				}
			}
		}
		/* 角色资源获取链接 */

		PrivilegeUrl privilegeUrl = new PrivilegeUrl();
		Map<String,Object> b = new HashMap<String,Object>();
		b.put(jsonKeyName, setUrl);
		String data = JSONObject.fromObject(b).toString();
		privilegeUrl.setPrivilegeUrl(data);
		return privilegeUrl;
	}

	@Override
	public PrivilegeUrl getAllPrivilegeUrl(String appId) {

		Set<String> setUrl =  new HashSet<String>();
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
