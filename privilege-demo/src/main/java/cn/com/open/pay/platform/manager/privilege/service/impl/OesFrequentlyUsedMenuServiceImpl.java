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
		//从缓存中取，如果有，返回
		String json = redisClientTemplate.getString(USER_FREQUENTLY_MENU + appId + SIGN + userId);
		List<Map<String, Object>> FrequentlyRes = new ArrayList<Map<String, Object>>();
		if (json != null && !json.isEmpty()) {
			JSONObject object = JSONObject.fromObject(json);
			List<Map<String, Object>> menuList = (List<Map<String, Object>>) object.get("menuList");
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
						for (Map<String, Object> resource : resList) {
							if (menuId.equals((String) resource.get("resourceId"))) {//获取菜单url
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", resource.get("resourceId"));
								map.put("url", resource.get("baseUrl"));
								map.put("name", resource.get("resourceName"));
								// 获取菜单图标
								for (Map<String, Object> menu : menuList) {
									if (resource.get("menuId").equals(menu.get("menuId"))) {
										map.put("menuRule", menu.get("menuRule"));
										break;
									}
								}
								FrequentlyRes.add(map);
							}
						}
					}
					parameter.clear();
					parameter.put("menuList", FrequentlyRes);
					redisClientTemplate.setString(USER_FREQUENTLY_MENU+appId+SIGN+userId, JSONObject.fromObject(parameter).toString());
				}
			}
		}
		return FrequentlyRes;
	}

	@Override
	public Boolean updateUserFrequentlyMenuRedis(String userId, String appId) {
		Boolean boo=redisClientTemplate.del(USER_FREQUENTLY_MENU+appId+SIGN+userId);
		getUserFrequentlyMenuRedis(userId, appId);
		return boo;
	}

}