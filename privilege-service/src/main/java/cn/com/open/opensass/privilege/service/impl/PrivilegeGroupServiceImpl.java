package cn.com.open.opensass.privilege.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeMenuRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import net.sf.json.JSONObject;

/**
 * 
 */
@Service("privilegeGroupService")
public class PrivilegeGroupServiceImpl implements PrivilegeGroupService {

	@Autowired
	private PrivilegeGroupRepository privilegeGroupRepository;
	@Autowired
	private PrivilegeResourceRepository privilegeResourceRepository;
	@Autowired
	private PrivilegeMenuRepository privilegeMenuRepository;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	// 缓存前缀
	private String groupCachePrefix = RedisConstant.PRIVILEGE_GROUPCACHE;

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
	public Map<String, Object> findGroupPrivilege(String groupId, String appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupId == null ||("".equals(groupId)) || appId == null || ("".equals(appId))) {
			map.put("status", "0");
			map.put("error_code", "10001");
			return map;
		}
		String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID = groupCachePrefix + appId + "_" + groupId;
		// 清空缓存
		//redisClientTemplate.del(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
		try {
			// 查询组织机构缓存
			String str = redisClientTemplate.getString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
			if (str != null) {
				JSONObject jsonObject = JSONObject.fromObject(str);
				Map map2 = (Map) jsonObject;
				if (map2 != null && map2.size() > 0) {// 查询缓存命中
					// map.put("status", "1");
					//map.put(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID, map2);
					return map2;
				}
			} else {
				Map<String, Object> redisMap = new HashMap<String, Object>();
				// 根据组Id和appId 查询资源
				List<PrivilegeResource> resourceList = privilegeResourceRepository.findByGroupIdAndAppId(groupId,appId);
				if (resourceList!=null && resourceList.size()>0) {
					redisMap.put("resourceList", resourceList);
				}
				//根据appid查询菜单
				List<PrivilegeMenu> menuList = privilegeMenuRepository.findMenuByAppId(appId);
				if (menuList!=null && menuList.size()>0) {
					redisMap.put("menuList", menuList);
				}
				// 向redis中添加组织机构的缓存
				redisClientTemplate.setString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID,
						JSONObject.fromObject(redisMap).toString());
				return redisMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 删除组织机构的缓存
	 */
	@Override
	public boolean delGroupPrivilegeCache(String groupId, String appId) {
		if (groupId == null ||("".equals(groupId)) || appId == null || ("".equals(appId))) {
			return false;
		}
		String privilegeservice_groupcache_appid_groupid = groupCachePrefix + appId+"_" + groupId;
		redisClientTemplate.del(privilegeservice_groupcache_appid_groupid);
		return true;
	}
	
	/**
	 * 更新组织机构缓存
	 */
	@Override
	public boolean updateGroupPrivilegeCache(String groupId, String appId) {
		if (groupId == null ||("".equals(groupId)) || appId == null || ("".equals(appId))) {
			return false;
		}
		String privilegeservice_groupcache_appid_groupid = groupCachePrefix + appId + "_" + groupId;
		Map<String, Object> redisMap = new HashMap<String, Object>();
		// 根据组Id和appId 查询资源
		List<PrivilegeResource> resourceList = privilegeResourceRepository.findByGroupIdAndAppId(groupId,appId);
		if (resourceList!=null && resourceList.size()>0) {
			redisMap.put("resourceList", resourceList);
		}
		//根据appid查询菜单
		List<PrivilegeMenu> menuList = privilegeMenuRepository.findMenuByAppId(appId);
		if (menuList!=null && menuList.size()>0) {
			redisMap.put("menuList", menuList);
		}
		// 向redis中添加组织机构的缓存
		redisClientTemplate.setString(privilegeservice_groupcache_appid_groupid,
				JSONObject.fromObject(redisMap).toString());
		return true;
	}
	
}