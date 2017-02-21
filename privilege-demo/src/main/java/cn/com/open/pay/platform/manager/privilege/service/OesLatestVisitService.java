package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;
import java.util.Map;

import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;

/**
 * 
 */
public interface OesLatestVisitService {
	
	Boolean saveOesLatestVisit(OesLatestVisit oesLatestVisit);
	
	List<OesLatestVisit> getOesLastVisitByUserId( String userId, int startRow,  int pageSize);
	
	List<Map<String, Object>> getUserLastVisitRedis(String userId,String appId);
	
	Boolean updateUserLastVisitRedis(String userId,String appId);
	
}