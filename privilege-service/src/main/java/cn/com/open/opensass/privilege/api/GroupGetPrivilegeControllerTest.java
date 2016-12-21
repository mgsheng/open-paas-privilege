package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import net.sf.json.JSONObject;

/**
 * 测试组织机构权限查询接口
 */
@Controller
@RequestMapping("/groupTest/")
public class GroupGetPrivilegeControllerTest extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(GroupGetPrivilegeControllerTest.class);
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
//	@Value("${PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID}")
//	private String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID;

	/**
	 * 组织机构权限查询接口
	 * 
	 * @return Json
	 */

	@RequestMapping("getGroupPrivilegeTest")
	public void getGroupPrivilege(HttpServletRequest request, HttpServletResponse response) {
		String groupId = request.getParameter("groupId");
		String appId = request.getParameter("appId");
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================query start======================");
		if (!paraMandatoryCheck(Arrays.asList(groupId, appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID="privilegeService_groupCache_"+appId+"_"+groupId;
		PrivilegeGroup privilegeGroup = privilegeGroupService.findBygroupId(groupId, appId);
		// 清空缓存
		//redisClientTemplate.del(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
		try {
			// 查询组织机构缓存
			String str= redisClientTemplate.getString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID);
			JSONObject jsonObject = JSONObject.fromObject(str);
			Map map2 = (Map)jsonObject;
			if (privilegeGroup != null && (map2 != null&&map2.size()>0)) {//查询缓存命中
				map.put("status", "1");
				map.put("privilegeGroup", privilegeGroup);
				map.put(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID, map2);
				//System.out.println(map2.toString());
				writeSuccessJson(response, map);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 创建资源和菜单的集合
		List<Map<String, Object>> resourceList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resourceMap = new HashMap<String, Object>();
		Map<String, Object> menuMap = new HashMap<String, Object>();
		Map<String, Object> redisMap = new HashMap<String, Object>();
		try {
			// 查询组对应的资源id
			List<String> resourceIds = privilegeGroupResourceService.findResourceIdsByGroupId(groupId);
			String menuId = null;
			for (String resourceId : resourceIds) {
				// 根据资源ID查询资源信息
				PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(resourceId, appId);
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
				PrivilegeMenu privilegeMenu = privilegeMenuService.findByMenuId(menuId, appId);
				if (privilegeMenu != null) {
					menuMap.put("menuId", privilegeMenu.getMenuId());
					menuMap.put("parentId", privilegeMenu.getParentId());
					menuMap.put("menuName", privilegeMenu.getMenuName());
					menuMap.put("menuRule", privilegeMenu.getMenuRule());
					menuMap.put("menuLevel", privilegeMenu.getMenuLevel());
					menuMap.put("displayOrder", privilegeMenu.getDisplayOrder());
					
					menuList.add(menuMap);
				}
			}
			redisMap.put("resourceList", resourceList);
			redisMap.put("menuList", menuList);
			// 向redis中添加组织机构的缓存
			redisClientTemplate.setString(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID, JSONObject.fromObject(redisMap).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (privilegeGroup != null) {
			map.put("status", "1");
			map.put("privilegeGroup", privilegeGroup);
			map.put(PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID,redisMap);
		} else {
			map.put("status", "0");
			map.put("error_code", "10001");
		}
		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
		// OauthControllerLog.log(startTime, guid, source_id, app,
		// map,userserviceDev);
		return;

	}
}