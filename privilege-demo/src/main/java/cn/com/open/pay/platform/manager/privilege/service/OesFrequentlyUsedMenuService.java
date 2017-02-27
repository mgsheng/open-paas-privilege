package cn.com.open.pay.platform.manager.privilege.service;



import java.util.List;
import java.util.Map;

import cn.com.open.pay.platform.manager.privilege.model.OesFrequentlyUsedMenu;


public interface OesFrequentlyUsedMenuService {

	Boolean saveOesFrequentlyUsedMenu(OesFrequentlyUsedMenu oesFrequentlyUsedMenu);

	OesFrequentlyUsedMenu getOesFrequentlyUsedMenuByUserId(String userId);

	Boolean updateOesFrequentlyUsedMenuByUserId(OesFrequentlyUsedMenu oesFrequentlyUsedMenu);
	
	List<Map<String, Object>> getUserFrequentlyMenuRedis(String userId,String appId);
	
	Boolean updateUserFrequentlyMenuRedis(String userId,String appId);
}