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
import com.andaily.springoauth.service.dto.PrivilegeUserRoleDto;
import com.andaily.springoauth.tools.LoadPopertiesFile;
/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class PrivilegeUserController {

    private static final Logger LOG = LoggerFactory.getLogger(PrivilegeUserController.class);

    @Value("#{properties['add-privilege-user-uri']}")
    private String addPrivilegeUserUri;
    @Value("#{properties['del-privilege-user-uri']}")
    private String delPrivilegeUserUri;
    @Value("#{properties['modi-privilege-user-uri']}")
    private String modiPrivilegeUserUri;
    @Value("#{properties['get-privilege-user-uri']}")
    private String getPrivilegeUserUri;
    @Value("#{properties['verify-privilege-user-uri']}")
    private String verifyPrivilegeUserUri;
    
    final static String  SEPARATOR = "&";
    private Map<String,String> map=LoadPopertiesFile.loadProperties();
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "addPrivilegeUserRole", method = RequestMethod.GET)
 	public String addPrivilegeUserRole(Model model) {
 		model.addAttribute("addPrivilegeUserUri", addPrivilegeUserUri);
 		return "privilege/privilege_user_role_add";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
    @RequestMapping(value = "addPrivilegeUserRole", method = RequestMethod.POST)
     public String addPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
         final String fullUri = privilegeUserRoleDto.getAddFullUri();
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
    /*
     *  Entrance:   step-1
     * */
	@RequestMapping(value = "delPrivilegeUserRole", method = RequestMethod.GET)
	public String delPrivilegeUserRole(Model model) {
		model.addAttribute("delPrivilegeUserUri", delPrivilegeUserUri);
		return "privilege/privilege_user_role_del";
	}
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
   @RequestMapping(value = "delPrivilegeUserRole", method = RequestMethod.POST)
    public String delPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
        final String fullUri = privilegeUserRoleDto.getdelFullUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }
    /*
     *  Entrance:   step-1
     * */
	@RequestMapping(value = "modiPrivilegeUserRole", method = RequestMethod.GET)
	public String modiPrivilegeUserRole(Model model) {
		model.addAttribute("modiPrivilegeUserUri", modiPrivilegeUserUri);
		return "privilege/privilege_user_role_modify";
	}
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
   @RequestMapping(value = "modiPrivilegeUserRole", method = RequestMethod.POST)
    public String modiPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
        final String fullUri = privilegeUserRoleDto.getModiFullUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }

   /*
    *  Entrance:   step-1
    * */
	@RequestMapping(value = "getPrivilegeUserRole", method = RequestMethod.GET)
	public String getPrivilegeUserRole(Model model) {
		model.addAttribute("getPrivilegeUserUri", getPrivilegeUserUri);
		return "privilege/privilege_user_role_query";
	}
	/* 
   * Redirect to oauth-server bind page:   step-2
   * */
  @RequestMapping(value = "getPrivilegeUserRole", method = RequestMethod.POST)
   public String getPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
       final String fullUri = privilegeUserRoleDto.getGetFullUri();
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
   }
  
   /*
    *  Entrance:   step-1
    * */
	@RequestMapping(value = "verifyPrivilegeUserRole", method = RequestMethod.GET)
	public String verifyPrivilegeUserRole(Model model) {
		model.addAttribute("verifyPrivilegeUserUri", verifyPrivilegeUserUri);
		return "privilege/privilege_user_role_verify";
	}
	/* 
   * Redirect to oauth-server bind page:   step-2
   * */
  @RequestMapping(value = "verifyPrivilegeUserRole", method = RequestMethod.POST)
   public String verifyPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
       final String fullUri = privilegeUserRoleDto.getVerifyFullUri();
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
   }
}