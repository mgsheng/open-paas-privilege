package com.andaily.springoauth.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.andaily.springoauth.service.dto.PrivilegeRoleDto;
import com.andaily.springoauth.tools.LoadPopertiesFile;
/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class PrivilegeRoleController {

    private static final Logger LOG = LoggerFactory.getLogger(PrivilegeRoleController.class);

    @Value("#{properties['add-privilege-role-uri']}")
    private String addPrivilegeRoleUri;
    @Value("#{properties['del-privilege-role-uri']}")
    private String delPrivilegeRoleUri;
    @Value("#{properties['modi-privilege-role-uri']}")
    private String modiPrivilegeRoleUri;
    @Value("#{properties['get-privilege-role-uri']}")
    private String getPrivilegeRoleUri;
    
    final static String  SEPARATOR = "&";
    private Map<String,String> map=LoadPopertiesFile.loadProperties();
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "addPrivilegeRole", method = RequestMethod.GET)
 	public String addPrivilegeRole(Model model) {
 		model.addAttribute("addPrivilegeRoleUri", addPrivilegeRoleUri);
 		return "privilege/add_privilege_role";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
    @RequestMapping(value = "addPrivilegeRole", method = RequestMethod.POST)
     public String addPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
         final String fullUri = privilegeRoleDto.getAddFullUri();
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
    /*
     *  Entrance:   step-1
     * */
	@RequestMapping(value = "delPrivilegeRole", method = RequestMethod.GET)
	public String delPrivilegeRole(Model model) {
		model.addAttribute("delPrivilegeRoleUri", delPrivilegeRoleUri);
		return "privilege/del_privilege_role";
	}
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
   @RequestMapping(value = "delPrivilegeRole", method = RequestMethod.POST)
    public String delPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
        final String fullUri = privilegeRoleDto.getDelFullUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }
   /*
    *  Entrance:   step-1
    * */
	@RequestMapping(value = "modifyPrivilegeRole", method = RequestMethod.GET)
	public String modifyPrivilegeRole(Model model) {
		model.addAttribute("modiPrivilegeRoleUri", modiPrivilegeRoleUri);
		return "privilege/modi_privilege_role";
	}
	/* 
   * Redirect to oauth-server bind page:   step-2
   * */
  @RequestMapping(value = "modifyPrivilegeRole", method = RequestMethod.POST)
   public String modifyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
       final String fullUri = privilegeRoleDto.getModiFullUri();
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
   }
  /*
   *  Entrance:   step-1
   * */
	@RequestMapping(value = "getPrivilegeRole", method = RequestMethod.GET)
	public String getPrivilegeRole(Model model) {
		model.addAttribute("getPrivilegeRoleUri", getPrivilegeRoleUri);
		return "privilege/get_privilege_role";
	}
	/* 
  * Redirect to oauth-server bind page:   step-2
  * */
 @RequestMapping(value = "getPrivilegeRole", method = RequestMethod.POST)
  public String getfyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
      final String fullUri = privilegeRoleDto.getGetFullUri();
      LOG.debug("Send to Oauth-Server URL: {}", fullUri);
      return "redirect:" + fullUri;
  }
    
    
    /**
	   * 获取 HMAC-SHA1 签名方法对对encryptText进行签名 值
	   * @param request
	   * @param response
	   * @param clientId
	   * @param accessToken
	   */
	   /* @RequestMapping(value = "getSignature1",method = RequestMethod.POST)
	    public void getSignature(HttpServletRequest request,HttpServletResponse response,String clientId,String accessToken,String password,String oldPassword){
	    	//返回数据
	    	boolean flag = false;
	    	String key=map.get(clientId);
	    	  String signature="";
	    	  String timestamp="";
	    	  String signatureNonce="";
	      	if(key!=null){ 
	      		timestamp=DateTools.getSolrDate(new Date());
	  		 	StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(clientId);
	  			encryptText.append(SEPARATOR);
	  		 	encryptText.append(accessToken);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  			try {
	  				signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				} catch (Exception e) {
					e.printStackTrace();
					flag=false;
				}
	  			signature=HMacSha1.getNewResult(signature);
	  			flag=true;
	      	}
	      	Map<String,Object> returnMap = new HashMap<String, Object>();
	      	returnMap.put("flag",flag);
	      	returnMap.put("signature",signature);
	      	returnMap.put("timestamp",timestamp);
	      	returnMap.put("signatureNonce",signatureNonce);
	    	WebUtils.writeJsonToMap(response, returnMap);
	    }*/
}