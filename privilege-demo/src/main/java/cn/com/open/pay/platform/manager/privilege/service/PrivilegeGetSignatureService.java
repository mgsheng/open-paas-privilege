package cn.com.open.pay.platform.manager.privilege.service;

import java.util.Map;



public interface PrivilegeGetSignatureService {
   
	
	Map<String, Object> getOauthSignature(String appId,String client_id,String access_token);
	Map<String, Object> getSignature(String appId);
    
}