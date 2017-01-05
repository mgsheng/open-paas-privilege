package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeFunctionVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import net.sf.json.JSONObject;

/**
 * 
 */
@Service("privilegeResourceService")
public class PrivilegeResourceServiceImpl implements PrivilegeResourceService {
	private static final String AppResRedisPrefix=RedisConstant.APPRES_CACHE;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeResourceServiceImpl.class);
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
    private RedisDao redisDao;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
    @Autowired
    private PrivilegeResourceRepository privilegeResourceRepository;

	@Override
	public Boolean savePrivilegeResource(PrivilegeResource privilegeResource) {
	  try {
		  privilegeResourceRepository.savePrivilegeResource(privilegeResource);
		  return true;
	  } catch (Exception e) {
		// TODO: handle exception
		  return false;
	  }
		
		
	}

	@Override
	public PrivilegeResource findByResourceId(String resourceId,String appId) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findByResourceId(resourceId,appId);
	}

	@Override
	public PrivilegeResource findByResourceCode(String resourceId,String appId) {
		return privilegeResourceRepository.findByResourceCode(resourceId,appId);
	}

	@Override
	public List<PrivilegeResource> findResourcePage(String menuId, String appId,
			int startRow, int pageSize,String resourceLevel) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findResourcePage(menuId, appId, startRow, pageSize,resourceLevel);
	}

	@Override
	public Boolean updatePrivilegeResource(PrivilegeResource privilegeResource) {
		try {
			privilegeResourceRepository.updatePrivilegeResource(privilegeResource);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public Boolean deleteByResourceId(String resourceId) {
		// TODO Auto-generated method stub
		try {
			privilegeResourceRepository.deleteByResourceId(resourceId);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}


	@Override
	public PrivilegeResourceVo findByResourceId(String resourceId) {
		PrivilegeResourceVo resourceVo=new PrivilegeResourceVo();
		PrivilegeResource resource=privilegeResourceRepository.findByResource_Id(resourceId);
		resourceVo.setAppId(resource.getAppId());
		resourceVo.setBaseUrl(resource.getBaseUrl());
		resourceVo.setDisplayOrder(resource.getDisplayOrder());
		resourceVo.setMenuId(resource.getMenuId());
		resourceVo.setResourceId(resource.getResourceId());
		resourceVo.setResourceLevel(String.valueOf(resource.getResourceLevel()));
		resourceVo.setResourceName(resource.getResourceName());
		resourceVo.setResourceRule(resource.getResourceRule());
		resourceVo.setStatus(resource.getStatus());
		return resourceVo;
	}


	@Override
	public List<Map<String, Object>> getResourceListByUserId(String appUserId, String appId) {
		return privilegeResourceRepository.getResourceListByUserId(appUserId, appId);
	}

	@Override
	public List<PrivilegeResourceVo> findByGroupIdAndAppId(String groupId, String appId) {
		List<PrivilegeResourceVo> privilegeResourceVos =new ArrayList<PrivilegeResourceVo>();
		List<PrivilegeResource> privilegeResources=privilegeResourceRepository.findByGroupIdAndAppId(groupId, appId);
		for(PrivilegeResource resource:privilegeResources){
			PrivilegeResourceVo privilegeResourceVo=new PrivilegeResourceVo();
			privilegeResourceVo.setAppId(resource.getAppId());
			privilegeResourceVo.setBaseUrl(resource.getBaseUrl());
			privilegeResourceVo.setDisplayOrder(resource.getDisplayOrder());
			privilegeResourceVo.setMenuId(resource.getMenuId());
			privilegeResourceVo.setResourceId(resource.getResourceId());
			privilegeResourceVo.setResourceLevel(String.valueOf(resource.getResourceLevel()));
			privilegeResourceVo.setResourceName(resource.getResourceName());
			privilegeResourceVo.setResourceRule(resource.getResourceRule());
			privilegeResourceVo.setStatus(resource.getStatus());
			privilegeResourceVos.add(privilegeResourceVo);
		}
		return privilegeResourceVos;
	}

	@Override
	public List<PrivilegeResource> findResourceLists(String[] groupIds) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findResourceList(groupIds);
	}

	@Override
	public List<Map<String, Object>> findResourceMap(String[] groupIds) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findResourceMap(groupIds);
	}

	@Override
	public List<PrivilegeResourceVo> getResourceListByAppId(String appId) {
		List<PrivilegeResourceVo> privilegeResourceVos =new ArrayList<PrivilegeResourceVo>();
		List<PrivilegeResource> privilegeResources=privilegeResourceRepository.getResourceListByAppId(appId);
		for(PrivilegeResource resource:privilegeResources){
			PrivilegeResourceVo privilegeResourceVo=new PrivilegeResourceVo();
			privilegeResourceVo.setAppId(resource.getAppId());
			privilegeResourceVo.setBaseUrl(resource.getBaseUrl());
			privilegeResourceVo.setDisplayOrder(resource.getDisplayOrder());
			privilegeResourceVo.setMenuId(resource.getMenuId());
			privilegeResourceVo.setResourceId(resource.getResourceId());
			privilegeResourceVo.setResourceLevel(String.valueOf(resource.getResourceLevel()));
			privilegeResourceVo.setResourceName(resource.getResourceName());
			privilegeResourceVo.setResourceRule(resource.getResourceRule());
			privilegeResourceVo.setStatus(resource.getStatus());
			privilegeResourceVos.add(privilegeResourceVo);
		}
		return privilegeResourceVos;
	}

	@Override
	public PrivilegeAjaxMessage getAppResRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage=new PrivilegeAjaxMessage();
		//redis key
		String AppResRedisKey = AppResRedisPrefix + appId;
		String jsonString = redisClientTemplate.getString(AppResRedisKey);
		//取缓存
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			System.err.println("缓存");
			return ajaxMessage;
		}
		Map<String, Object> redisMap=new HashMap<String, Object>();
		log.info("从数据库获取数据");
		//resourceList
		List<PrivilegeResourceVo> resourceList=getResourceListByAppId(appId);
		redisMap.put("resourceList", resourceList);
		//functionList
		List<PrivilegeFunctionVo> functionList=privilegeFunctionService.getFunctionListByAppId(appId);
		redisMap.put("functionList", functionList);
		redisClientTemplate.setString(AppResRedisKey, JSONObject.fromObject(redisMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(redisMap).toString());
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage delAppResRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Boolean RoleKeyExist = redisDao.deleteRedisKey(AppResRedisPrefix, appId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(RoleKeyExist ? "Success" : "Failed");
		log.info("delMenuRedis接口删除：" + RoleKeyExist);
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage updateAppResRedis(String appId) {
		boolean RedisKeyExist = redisDao.existKeyRedis(AppResRedisPrefix, appId);
		if (RedisKeyExist) {
			delAppResRedis(appId);
		}
		log.info("更新redis");
		return getAppResRedis(appId);
	}

}