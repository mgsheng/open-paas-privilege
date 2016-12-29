package com.andaily.springoauth.web;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.andaily.springoauth.service.dto.PrivilegeRoleDto;
import com.andaily.springoauth.tools.DateTools;
import com.andaily.springoauth.tools.HMacSha1;
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
 		return "privilege/privilege_role_add";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
    @RequestMapping(value = "addPrivilegeRole", method = RequestMethod.POST)
     public String addPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
    	String key=map.get(privilegeRoleDto.getAppId());
	  	  String signature="";
	  	  String timestamp="";
	  	  String signatureNonce="";
	  	  String appKey="";
		      if(key!=null){
		    	    appKey=map.get(key);
		      		timestamp=DateTools.getSolrDate(new Date());
		  		 	StringBuilder encryptText = new StringBuilder();
		  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		  		 	encryptText.append(privilegeRoleDto.getAppId());
		  			encryptText.append(SEPARATOR);
		  			if(appKey!=null){
		  			  encryptText.append(appKey);
		  			}
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(timestamp);
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(signatureNonce);
		  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
		  			signature=HMacSha1.getNewResult(signature);
		      }
         final String fullUri = privilegeRoleDto.getAddFullUri()+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;;
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
    /*
     *  Entrance:   step-1
     * */
	@RequestMapping(value = "delPrivilegeRole", method = RequestMethod.GET)
	public String delPrivilegeRole(Model model) {
		model.addAttribute("delPrivilegeRoleUri", delPrivilegeRoleUri);
		return "privilege/privilege_role_del";
	}
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
   @RequestMapping(value = "delPrivilegeRole", method = RequestMethod.POST)
    public String delPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
	   String key=map.get(privilegeRoleDto.getAppId());
	  	  String signature="";
	  	  String timestamp="";
	  	  String signatureNonce="";
	  	  String appKey="";
		      if(key!=null){
		    	    appKey=map.get(key);
		      		timestamp=DateTools.getSolrDate(new Date());
		  		 	StringBuilder encryptText = new StringBuilder();
		  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		  		 	encryptText.append(privilegeRoleDto.getAppId());
		  			encryptText.append(SEPARATOR);
		  			if(appKey!=null){
		  			  encryptText.append(appKey);
		  			}
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(timestamp);
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(signatureNonce);
		  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
		  			signature=HMacSha1.getNewResult(signature);
		      } 
	   final String fullUri = privilegeRoleDto.getDelFullUri()+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;;
        
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }
   /*
    *  Entrance:   step-1
    * */
	@RequestMapping(value = "modifyPrivilegeRole", method = RequestMethod.GET)
	public String modifyPrivilegeRole(Model model) {
		model.addAttribute("modiPrivilegeRoleUri", modiPrivilegeRoleUri);
		return "privilege/privilege_role_modify";
	}
	/* 
   * Redirect to oauth-server bind page:   step-2
   * */
  @RequestMapping(value = "modiPrivilegeRole", method = RequestMethod.POST)
   public String modifyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
       
	  String key=map.get(privilegeRoleDto.getAppId());
  	  String signature="";
  	  String timestamp="";
  	  String signatureNonce="";
  	  String appKey="";
	      if(key!=null){
	    	    appKey=map.get(key);
	      		timestamp=DateTools.getSolrDate(new Date());
	  		 	StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(privilegeRoleDto.getAppId());
	  			encryptText.append(SEPARATOR);
	  			if(appKey!=null){
	  			  encryptText.append(appKey);
	  			}
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	  			signature=HMacSha1.getNewResult(signature);
	      }
	  final String fullUri = privilegeRoleDto.getModiFullUri()+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;;
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
   }
  /*
   *  Entrance:   step-1
   * */
	@RequestMapping(value = "getPrivilegeRole", method = RequestMethod.GET)
	public String getPrivilegeRole(Model model) {
		model.addAttribute("getPrivilegeRoleUri", getPrivilegeRoleUri);
		return "privilege/privilege_role_query";
	}
	/* 
  * Redirect to oauth-server bind page:   step-2
  * */
 @RequestMapping(value = "getPrivilegeRole", method = RequestMethod.POST)
  public String getfyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
      
	 String key=map.get(privilegeRoleDto.getAppId());
 	  String signature="";
 	  String timestamp="";
 	  String signatureNonce="";
 	  String appKey="";
	      if(key!=null){
	    	    appKey=map.get(key);
	      		timestamp=DateTools.getSolrDate(new Date());
	  		 	StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(privilegeRoleDto.getAppId());
	  			encryptText.append(SEPARATOR);
	  			if(appKey!=null){
	  			  encryptText.append(appKey);
	  			}
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	  			signature=HMacSha1.getNewResult(signature);
	      }
	 final String fullUri = privilegeRoleDto.getGetFullUri()+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;;
      LOG.debug("Send to Oauth-Server URL: {}", fullUri);
      return "redirect:" + fullUri;
  }
}