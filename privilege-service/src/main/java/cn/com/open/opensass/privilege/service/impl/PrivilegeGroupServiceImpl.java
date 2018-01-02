package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import net.sf.json.JSONObject;

/**
 * 
 */
@Service("privilegeGroupService")
public class PrivilegeGroupServiceImpl implements PrivilegeGroupService {

	@Autowired
	private PrivilegeGroupRepository privilegeGroupRepository;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	/*@Autowired
	private RedisDao redisDao;*/
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	// 缓存前缀
	private String groupCachePrefix = RedisConstant.PRIVILEGE_GROUPCACHE;
	//应用菜单版本缓存前缀
	private static final String appMenuVersionCache = RedisConstant.APPMENUVERSIONCACHE;
	//应用组织机构版本缓存前缀
	private static final String groupVersionCachePerfix = RedisConstant.GROUPVERSIONCACHE;
	// 缓存间隔符
	public static final String SIGN = RedisConstant.SIGN;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeGroupServiceImpl.class);

	@Override
	public Boolean savePrivilegeGroup(PrivilegeGroup privilegeGroup) {
		try {
			privilegeGroupRepository.savePrivilegeGroup(privilegeGroup);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	@Override
	public PrivilegeGroup findBygroupId(String groupId, String appId) {
		// TODO Auto-generated method stub
		return privilegeGroupRepository.findByGroupId(groupId, appId);
	}

	@Override
	public List<PrivilegeGroup> findGroupPage(String groupId, String appId, String startRow, String pageSize) {
		// TODO Auto-generated method stub
		List<PrivilegeGroup> groupPage = privilegeGroupRepository.findGroupPage(groupId, appId,
				Integer.parseInt(startRow), Integer.parseInt(pageSize));

		return groupPage;
	}

	// 查询组织机构权限
	@Override
	public PrivilegeAjaxMessage findGroupPrivilege(String groupId, String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		List<PrivilegeGroupResource> group =privilegeGroupResourceService.findByGroupIdAndAppId(groupId, appId); 
		/*if (group.size()==0) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("GroupResource Is Null");
			return ajaxMessage;
		}*/

		String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID = groupCachePrefix + appId + SIGN + groupId;
		//应用菜单版本号
		Integer version = (Integer) redisClientTemplate.getObject(appMenuVersionCache + appId);
		/* 缓存中是否存在 存在返回 */
		String jsonString = redisClientTemplate.getString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
		if (null != jsonString && jsonString.length() > 0) {
			//从缓存中获取应用菜单版本，与组织机构缓存版本号对比，若版本号不相同，更新组织机构缓存
			if (version != null) {
				JSONObject object = JSONObject.fromObject(jsonString);
				Integer groupMenuCacheVersions = (Integer) object.get("version");
				if (groupMenuCacheVersions == null) {
					 ajaxMessage = updateGroupPrivilegeCache(groupId, appId);
				} else {
					if (version.equals(groupMenuCacheVersions)) {
						ajaxMessage.setCode("1");
						ajaxMessage.setMessage(jsonString);
					} else {
						ajaxMessage = updateGroupPrivilegeCache(groupId, appId);
					}
				}
				return ajaxMessage;
			}
			log.info("获取到缓存");
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}
		log.info("从数据库获取数据");
		Map<String, Object> redisMap = new HashMap<String, Object>();
		// 根据遍历应用资源缓存，获取该组织机构拥有的资源
		PrivilegeAjaxMessage message=privilegeResourceService.getAppResRedis(appId);
		String appRedis=message.getMessage();
		JSONObject Resobject=JSONObject.fromObject(appRedis);
		List<Map<String, Object>> resourceVos=(List<Map<String, Object>>) Resobject.get("resourceList");
		List<Map<String, Object>> resourceList=new ArrayList<Map<String, Object>>();
		for(PrivilegeGroupResource privilegeGroupResource:group){
			for(Map<String, Object> resourceVo:resourceVos){
				if (privilegeGroupResource.getResourceId().equals(resourceVo.get("resourceId"))) {
					resourceList.add(resourceVo);
				}
			}
		}
		redisMap.put("resourceList", resourceList);
		// 根据遍历应用菜单缓存，查找组织机构拥有的菜单
		message=privilegeMenuService.getAppMenuRedis(appId);
		appRedis=message.getMessage();
		JSONObject menuObject=JSONObject.fromObject(appRedis);
		List<Map<String, Object>> menus=(List<Map<String, Object>>) menuObject.get("menuList");
		List<PrivilegeMenu> menuList=new ArrayList<PrivilegeMenu>();
		for (Map<String, Object> resource : resourceList) {
			for (Map<String, Object> privilegeMenu : menus) {
				if (privilegeMenu.get("menuId").equals(resource.get("menuId"))) {
					PrivilegeMenu menu=new PrivilegeMenu();
					menu.id((String) privilegeMenu.get("menuId"));
					menu.setAppId((String)privilegeMenu.get("appId"));
					menu.setMenuName((String)privilegeMenu.get("menuName"));
					menu.setParentId((String)privilegeMenu.get("parentId"));
					menu.setMenuRule((String)privilegeMenu.get("menuRule"));
					menu.setMenuCode((String)privilegeMenu.get("menuCode"));
					menu.setDisplayOrder((Integer)privilegeMenu.get("displayOrder"));
					menu.setMenuLevel((Integer)privilegeMenu.get("menuLevel"));
					menuList.add(menu);
				}
			}
		}
		Set<PrivilegeMenuVo> privilegeMenuVoSet = new HashSet<PrivilegeMenuVo>();
		// 获取所有父菜单
		Set<PrivilegeMenuVo> allMenu = privilegeMenuService.getAllMenuByUserId(menuList, privilegeMenuVoSet);
		redisMap.put("menuList", allMenu);
		//应用菜单版本号不为null,则加入组织机构缓存版本号
		if (version != null) {
			redisMap.put("version", version);
		}
		// 向redis中添加组织机构的缓存
		redisClientTemplate.setString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID,
				JSONObject.fromObject(redisMap).toString());

		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(redisMap).toString());
		return ajaxMessage;

	}

	/**
	 * 删除组织机构的缓存
	 */
	@Override
	public PrivilegeAjaxMessage delGroupPrivilegeCache(String groupId, String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
//		Boolean KeyExist = redisDao.deleteRedisKey(groupCachePrefix, appId, groupId);
		Boolean KeyExist = redisClientTemplate.del(groupCachePrefix+appId+SIGN+groupId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(KeyExist ? "Success" : "Failed");
		log.info("缓存接口删除：" + KeyExist);
		return ajaxMessage;
	}

	/**
	 * 更新组织机构缓存
	 */
	@Override
	public PrivilegeAjaxMessage updateGroupPrivilegeCache(String groupId, String appId) {
//		boolean RedisKeyExist = redisDao.existKeyRedis(groupCachePrefix, appId, groupId);
		boolean RedisKeyExist = redisClientTemplate.existKey(groupCachePrefix+appId+SIGN+groupId);
		if (RedisKeyExist) {
			delGroupPrivilegeCache(groupId, appId);
		}
		log.info("更新redis");
		return findGroupPrivilege(groupId, appId);
	}

	@Override
	public List<PrivilegeGroup> findByAppId(String appId) {
		return privilegeGroupRepository.findByAppId(appId);
	}

	@Override
	public Boolean deleteByGroupId(String groupId, String appId) {
		try{
			privilegeGroupRepository.deleteByGroupId(groupId,appId);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	    *	更新组织机构版本号
	    * @param groupId 机构Id
	    * @param appId  应用Id
	    * @return PrivilegeAjaxMessage
	    */
	@Override
	public PrivilegeAjaxMessage updateGroupVersion(String groupId, String appId) {
		log.info("----------updateGroupVersion start------");
		PrivilegeAjaxMessage message = new PrivilegeAjaxMessage();
		String groupVersion = String.valueOf(redisClientTemplate.getObject(groupVersionCachePerfix + appId + SIGN
				+ groupId));
		if (groupVersion == null) {
			groupVersion = "1";
		} else {
			Integer groupVersionCount = Integer.parseInt(groupVersion)+1;
			groupVersion = String.valueOf(groupVersionCount);
		}
		String result = redisClientTemplate.setObject(groupVersionCachePerfix + appId + SIGN + groupId, groupVersion);
		if ("OK".equals(result)) {
			message.setCode("1");
		} else {
			message.setCode("0");
		}
		message.setMessage(result);
		return message;
	}

}