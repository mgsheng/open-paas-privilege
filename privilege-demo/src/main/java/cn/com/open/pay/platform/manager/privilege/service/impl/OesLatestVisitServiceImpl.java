package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.infrastructure.repository.OesLatestVisitRepository;
import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;
import cn.com.open.pay.platform.manager.privilege.service.OesLatestVisitService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import net.sf.json.JSONObject;

@Service("OesLatestVisitService")
public class OesLatestVisitServiceImpl extends BaseControllerUtil implements OesLatestVisitService {
	@Autowired
	private OesLatestVisitRepository oesLatestVisitRepository;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private static final String UserLastVisitPrefix = RedisConstant.USER_LATEST_VISIT;
	private static final String SIGN = RedisConstant.SIGN;

	@Override
	public Boolean saveOesLatestVisit(OesLatestVisit oesLatestVisit) {
		try {
			oesLatestVisitRepository.saveOesLatestVisit(oesLatestVisit);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<OesLatestVisit> getOesLastVisitByUserId(String userId, int startRow, int pageSize) {
		return oesLatestVisitRepository.getOesLastVisitByUserId(userId, startRow, pageSize);
	}

	@Override
	public List<Map<String, Object>> getUserLastVisitRedis(String userId, String appId) {
		// 获取用户最近导航菜单缓存，若没有查询数据库，
		String json = redisClientTemplate.getString(UserLastVisitPrefix + appId + SIGN + userId);
		if (null != json && json.length() > 0) {
			JSONObject object = JSONObject.fromObject(json);
			List<Map<String, Object>> menuList = (List<Map<String, Object>>) object.get("menuList");
			return menuList;
		} else {
			Map<String, Object> parameter = privilegeGetSignatureService.getSignature(appId);
			parameter.put("appId", appId);
			parameter.put("appUserId", userId);
			// 获取用户菜单与资源
			String userPrivilege = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), parameter);
			JSONObject object = JSONObject.fromObject(userPrivilege);
			List<Map<String, Object>> resList = (List<Map<String, Object>>) object.get("resourceList");
			List<Map<String, Object>> menuList = (List<Map<String, Object>>) object.get("menuList");
			//获取最近访问的菜单
			List<OesLatestVisit> userLatestVisits = getOesLastVisitByUserId(userId, 0, 5);
			List<Map<String, Object>> latesVisitRes = new ArrayList<Map<String, Object>>();
			for (OesLatestVisit oesLatestVisit : userLatestVisits) {
				for (Map<String, Object> resource : resList) {
					// 获取资源url
					if (oesLatestVisit.getMenuId().equals((String) resource.get("resourceId"))) {
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
						latesVisitRes.add(map);
					}
				}

			}
			parameter.clear();
			parameter.put("menuList", latesVisitRes);
			redisClientTemplate.setString(UserLastVisitPrefix + appId + SIGN + userId,
					JSONObject.fromObject(parameter).toString());
			return latesVisitRes;
		}
	}

	@Override
	public Boolean updateUserLastVisitRedis(String userId, String appId) {
		redisClientTemplate.del(UserLastVisitPrefix + appId + SIGN + userId);
		getUserLastVisitRedis(userId, appId);
		return true;
	}

}