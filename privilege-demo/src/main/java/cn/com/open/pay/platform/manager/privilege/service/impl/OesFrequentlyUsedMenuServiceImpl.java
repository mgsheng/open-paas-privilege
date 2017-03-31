package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.infrastructure.repository.OesFrequentlyUsedMenuRepository;
import cn.com.open.pay.platform.manager.privilege.model.OesFrequentlyUsedMenu;
import cn.com.open.pay.platform.manager.privilege.service.OesFrequentlyUsedMenuService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import net.sf.json.JSONObject;

@Service("OesFrequentlyUsedMenuService")
public class OesFrequentlyUsedMenuServiceImpl extends BaseControllerUtil implements OesFrequentlyUsedMenuService {

	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private static final String USER_FREQUENTLY_MENU = RedisConstant.USER_FREQUENTLY_MENU;
	private static final String SIGN = RedisConstant.SIGN;
	private static final String appMenuVersionCache = RedisConstant.APPMENUVERSIONCACHE;
	@Autowired
	private OesFrequentlyUsedMenuRepository oesFrequentlyUsedMenuRepository;

	@Override
	public Boolean saveOesFrequentlyUsedMenu(OesFrequentlyUsedMenu oesFrequentlyUsedMenu) {
		try {
			oesFrequentlyUsedMenuRepository.saveOesFrequentlyUsedMenu(oesFrequentlyUsedMenu);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public OesFrequentlyUsedMenu getOesFrequentlyUsedMenuByUserId(String userId) {
		return oesFrequentlyUsedMenuRepository.getOesFrequentlyUsedMenuByUserId(userId);
	}

	@Override
	public Boolean updateOesFrequentlyUsedMenuByUserId(OesFrequentlyUsedMenu oesFrequentlyUsedMenu) {
		try {
			oesFrequentlyUsedMenuRepository.updateOesFrequentlyUsedMenuByUserId(oesFrequentlyUsedMenu);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> getUserFrequentlyMenuRedis(String userId, String appId) {
		//从缓存中获取应用菜单版本
		Integer appMenuVersion =  (Integer) redisClientTemplate.getObject(appMenuVersionCache+appId);
		//从缓存中取，如果有，返回
		String json = redisClientTemplate.getString(USER_FREQUENTLY_MENU + appId + SIGN + userId);
		List<Map<String, Object>> FrequentlyRes = new ArrayList<Map<String, Object>>();
		if (json != null && !json.isEmpty()) {
			JSONObject object = JSONObject.fromObject(json);
			List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
			//从缓存中获取应用菜单版本，与用户常用菜单缓存版本号对比，若版本号不相同，更新用户常用菜单缓存
			if (appMenuVersion != null) {
				Integer userMenuVersion = (Integer) object.get("version");
				if (userMenuVersion == null) {
					menuList = updateUserFrequentlyMenuRedis(userId, appId);
				} else {
					if (appMenuVersion.equals(userMenuVersion)) {
						menuList = (List<Map<String, Object>>) object.get("menuList");
					} else {
						menuList = updateUserFrequentlyMenuRedis(userId, appId);
					}
				}
				return menuList;
			}
			menuList = (List<Map<String, Object>>) object.get("menuList");
			return menuList;
		} else {//缓存中没有，从数据库读取
			OesFrequentlyUsedMenu oesFrequentlyUsedMenu = oesFrequentlyUsedMenuRepository
					.getOesFrequentlyUsedMenuByUserId(userId);
			if (oesFrequentlyUsedMenu != null) {
				if (oesFrequentlyUsedMenu.getMenuId() != null && !oesFrequentlyUsedMenu.getMenuId().isEmpty()) {
					String[] menuIds = oesFrequentlyUsedMenu.getMenuId().split(",");
					Map<String, Object> parameter = new HashMap<String, Object>();
					parameter.put("appId", appId);
					String appRes = sendPost(oesPrivilegeDev.getAppResRedisUrl(), parameter);
					json = sendPost(oesPrivilegeDev.getAppMenuRedisUrl(), parameter);
					JSONObject object = JSONObject.fromObject(appRes);
					List<Map<String, Object>> resList = (List<Map<String, Object>>) object.get("resourceList");
					object = JSONObject.fromObject(json);
					List<Map<String, Object>> menuList = (List<Map<String, Object>>) object.get("menuList");
					for (String menuId : menuIds) {
						for (Map<String, Object> menu : menuList) {
							if (menuId.equals(menu.get("menuId"))) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("menuRule", menu.get("menuRule"));
								map.put("id", menu.get("menuId"));
								for (Map<String, Object> resource : resList) {
									if (resource.get("menuId").equals(menu.get("menuId"))) {
										map.put("url", resource.get("baseUrl"));//获取菜单url
										map.put("name", resource.get("resourceName"));
										break;
									}
								}
								FrequentlyRes.add(map);
							}
						}
					}
					parameter.clear();
					parameter.put("menuList", FrequentlyRes);
					//应用菜单版本不为null ，加入 常用菜单缓存版本号
					if (appMenuVersion != null) {
						parameter.put("version", appMenuVersion);
					}
					//放入redis
					redisClientTemplate.setString(USER_FREQUENTLY_MENU+appId+SIGN+userId, JSONObject.fromObject(parameter).toString());
				}
			}
		}
		return FrequentlyRes;
	}

	@Override
	public List<Map<String, Object>> updateUserFrequentlyMenuRedis(String userId, String appId) {
		redisClientTemplate.del(USER_FREQUENTLY_MENU+appId+SIGN+userId);
		return getUserFrequentlyMenuRedis(userId, appId);
	}

}