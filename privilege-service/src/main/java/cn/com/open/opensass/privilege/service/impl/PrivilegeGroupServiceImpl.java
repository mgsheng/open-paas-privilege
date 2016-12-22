package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupResourceRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeMenuRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
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
	private PrivilegeGroupResourceRepository privilegeGroupResourceRepository;
	@Autowired
	private PrivilegeResourceRepository privilegeResourceRepository;
	@Autowired
	private PrivilegeMenuRepository privilegeMenuRepository;
	@Autowired
	private RedisClientTemplate redisClientTemplate;

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
	public Map<String, Object> findGroup(String groupId, String appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupId == null || appId == null) {
			map.put("status", "0");
			map.put("error_code", "10001");
			return map;
		}
		String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID = "privilegeService_groupCache_" + appId + "_" + groupId;
		PrivilegeGroup privilegeGroup = privilegeGroupRepository.findByGroupId(groupId, appId);
		
		// 清空缓存
		//redisClientTemplate.del(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
		try {
			// 查询组织机构缓存
			String str = redisClientTemplate.getString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
			JSONObject jsonObject = JSONObject.fromObject(str);
			Map map2 = (Map) jsonObject;
			if (privilegeGroup != null && (map2 != null && map2.size() > 0)) {// 查询缓存命中
				map.put("status", "1");
				map.put("privilegeGroup", privilegeGroup);
				map.put(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID, map2);
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 创建资源和菜单的集合 
		List<Map<String, Object>> resourceList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resourceMap = new HashMap<String, Object>();
		//Map<String, Object> menuMap = new HashMap<String, Object>();
		Map<String, Object> redisMap = new HashMap<String, Object>();
		try {
			// 查询组对应的资源id
			List<String> resourceIds = privilegeGroupResourceRepository.findResourceIdsByGroupId(groupId);
			String menuId = null;
			for (String resourceId : resourceIds) {
				// 根据资源ID查询资源信息
				PrivilegeResource privilegeResource = privilegeResourceRepository.findByResourceId(resourceId, appId);
				if (privilegeResource != null) {
					resourceMap.put("appId", privilegeResource.getAppId());
					resourceMap.put("resourceId", privilegeResource.getResourceId());
					resourceMap.put("resourceLevel", privilegeResource.getResourceLevel());
					resourceMap.put("resourceName", privilegeResource.getResourceName());
					resourceMap.put("resourceRule", privilegeResource.getResourceRule());
					resourceMap.put("dislayOrder ", privilegeResource.getDisplayOrder());
					resourceMap.put("menuId", privilegeResource.getMenuId());
					resourceMap.put("baseUrl", privilegeResource.getBaseUrl());
					resourceMap.put("status", privilegeResource.getStatus());

					resourceList.add(resourceMap);
					// 获取资源对应的菜单ID,
					menuId = privilegeResource.getMenuId();
				}
				// 根据菜单ID查询菜单信息
				Map<String, Object> privilegeMenu = privilegeMenuRepository.getMenuById(menuId);
				if (privilegeMenu != null&&privilegeMenu.size()>0) {
					/*menuMap.put("menuId", privilegeMenu.getMenuId());
					menuMap.put("parentId", privilegeMenu.getParentId());
					menuMap.put("menuName", privilegeMenu.getMenuName());
					menuMap.put("menuRule", privilegeMenu.getMenuRule());
					menuMap.put("menuLevel", privilegeMenu.getMenuLevel());
					menuMap.put("displayOrder", privilegeMenu.getDisplayOrder());*/
					menuList.add(privilegeMenu);
				}
			}
			redisMap.put("resourceList", resourceList);
			redisMap.put("menuList", menuList);
			// 向redis中添加组织机构的缓存
			redisClientTemplate.setString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID,
					JSONObject.fromObject(redisMap).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (privilegeGroup != null) {
			map.put("status", "1");
			map.put("privilegeGroup", privilegeGroup);
			map.put(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID, redisMap);
		} else {
			map.put("status", "0");
			map.put("error_code", "10001");
		}

		return map;
	}

}