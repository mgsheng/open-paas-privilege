package cn.com.open.pay.platform.manager.privilege.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.OesLatestVisitRepository;
import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;
import cn.com.open.pay.platform.manager.privilege.service.OesLatestVisitService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import net.sf.json.JSONObject;

@Service("OesLatestVisitService")
public class OesLatestVisitServiceImpl implements OesLatestVisitService {
	@Autowired
	private OesLatestVisitRepository oesLatestVisitRepository;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private static final String UserLastVisitPrefix = RedisConstant.USER_LATEST_VISIT;
	private static final String AppResRedisPrefix = RedisConstant.APPRES_CACHE;
	
	@Override
	public Boolean saveOesLatestVisit(OesLatestVisit oesLatestVisit) {
		try{
			oesLatestVisitRepository.saveOesLatestVisit(oesLatestVisit);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<OesLatestVisit> getOesLastVisitByUserId(String userId, int startRow, int pageSize) {
		return oesLatestVisitRepository.getOesLastVisitByUserId(userId, startRow, pageSize);
	}

	@Override
	public List<Map<String, Object>> getUserLastVisitRedis(String userId,String appId) {
		String json=redisClientTemplate.getString(UserLastVisitPrefix+userId);
		if (null != json && json.length() > 0) {
			JSONObject object=JSONObject.fromObject(json);
			List<Map<String, Object>> menuList=(List<Map<String, Object>>) object.get("menuList");
			return menuList;
		}else {
			String appRes=redisClientTemplate.getString(AppResRedisPrefix+appId);
			JSONObject object=JSONObject.fromObject(appRes);
			List<Map<String, Object>> resList=(List<Map<String, Object>>) object.get("resourceList");
			List<OesLatestVisit> userLatestVisits=getOesLastVisitByUserId(userId, 0, 5);
			List<Map<String, Object>> latesVisitRes=new ArrayList<Map<String, Object>>();
			for (OesLatestVisit oesLatestVisit : userLatestVisits) {
				for(Map<String, Object> resource:resList){
					if (oesLatestVisit.getMenuId().equals((String)resource.get("resourceId"))) {
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("id", resource.get("resourceId"));
						map.put("url", resource.get("baseUrl"));
						map.put("name", resource.get("resourceName"));
						latesVisitRes.add(map);
					}
				}
				
			}
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("menuList", latesVisitRes);
			redisClientTemplate.setString(UserLastVisitPrefix+userId, JSONObject.fromObject(map).toString());
			return latesVisitRes;
		}
	}

	@Override
	public Boolean updateUserLastVisitRedis(String userId,String appId) {
		redisClientTemplate.del(UserLastVisitPrefix+userId);
		getUserLastVisitRedis(userId,appId);
		return true;
	}
   
}