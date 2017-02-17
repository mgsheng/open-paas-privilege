package cn.com.open.opensass.privilege.service.impl;

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
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeMenuRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
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
	private PrivilegeMenuRepository privilegeMenuRepository;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private RedisDao redisDao;
	// 缓存前缀
	private String groupCachePrefix = RedisConstant.PRIVILEGE_GROUPCACHE;
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

	// 查询组织机构
	@Override
	public PrivilegeAjaxMessage findGroupPrivilege(String groupId, String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/*PrivilegeGroup group = privilegeGroupRepository.findByGroupId(groupId, appId);
		if (null == group) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("Group Is Null");
			return ajaxMessage;
		}*/

		String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID = groupCachePrefix + appId + SIGN + groupId;

		/* 缓存中是否存在 存在返回 */
		String jsonString = redisClientTemplate.getString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
		if (null != jsonString && jsonString.length() > 0) {
			log.info("获取到缓存");
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			System.err.println("缓存" + jsonString);
			return ajaxMessage;
		}
		log.info("从数据库获取数据");
		Map<String, Object> redisMap = new HashMap<String, Object>();
		//redisMap.put("groupName", group.getGroupName());
		// 根据组Id和appId 查询资源
		List<PrivilegeResourceVo> resourceList = privilegeResourceService.findByGroupIdAndAppId(groupId, appId);
		redisMap.put("resourceList", resourceList);
		// 根据groupId appId查询菜单
		List<PrivilegeMenu> menuList = privilegeMenuRepository.findMenuByGroupIdAndAppId(groupId, appId);

		Set<PrivilegeMenuVo> privilegeMenuVoSet = new HashSet<PrivilegeMenuVo>();
		// 获取所有父菜单
		Set<PrivilegeMenuVo> allMenu = privilegeMenuService.getAllMenuByUserId(menuList, privilegeMenuVoSet);
		redisMap.put("menuList", allMenu);
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
		Boolean KeyExist = redisDao.deleteRedisKey(groupCachePrefix, appId, groupId);
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
		boolean RedisKeyExist = redisDao.existKeyRedis(groupCachePrefix, appId, groupId);
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

}