package cn.com.open.opensass.privilege.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.AppRepository;
import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.AppService;



/**
 * 
 */
@Service("appService")
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;

	@Override
    public App findIdByClientId(String client_Id) {
		App app=appRepository.findIdByClientId(client_Id);
        if(app==null){
        	return null;
        }else{
        	return app;
        }
    }
	
	public App findIdByAppKey(String client_Id) {
        return appRepository.findIdByClientId(client_Id);
    }

	@Override
	public List<App> findAll() {
		return appRepository.findAll();
	}
	
	public List<App> findByAppIds(String appIds){
		Map map=new HashMap();
		Integer[] appIdArray ;
		if(appIds==null || appIds.length()==0){
			appIdArray = new Integer[]{0};
		}else{
			String[] a=appIds.split(",");
			appIdArray = new Integer[a.length];
			if(a!=null && a.length!=0){
				for(int i=0;i<a.length;i++){
					appIdArray[i]=Integer.parseInt(a[i]);
				}
			}
		}
		map.put("appIds", appIdArray);
		return appRepository.findByAppIds(map);
	}

	@Override
	public App findById(Integer appId) {
		return appRepository.findById(appId);
	}
	
	

}