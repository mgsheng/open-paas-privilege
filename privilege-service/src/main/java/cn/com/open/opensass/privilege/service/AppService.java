package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.App;



/**
 * 
 */
public interface AppService {

	App findIdByClientId(String client_Id);

	List<App> findAll();
	
	List<App> findByAppIds(String appIds);

	App findById(Integer appId);
	
	
}