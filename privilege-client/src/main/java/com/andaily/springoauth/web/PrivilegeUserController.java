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
   /* @Value("#{properties['del-privilege-user-uri']}")
    private String delPrivilegeUserUri;*/
    
    final static String  SEPARATOR = "&";
    private Map<String,String> map=LoadPopertiesFile.loadProperties();
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "addPrivilegeUserRole", method = RequestMethod.GET)
 	public String addPrivilegeUserRole(Model model) {
 		model.addAttribute("addPrivilegeUserUri", addPrivilegeUserUri);
 		return "privilege/add_privilege_user_role";
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
	/*@RequestMapping(value = "delPrivilegeUserRole", method = RequestMethod.GET)
	public String delPrivilegeUserRole(Model model) {
		model.addAttribute("delPrivilegeUserRoleUri", delPrivilegeUserUri);
		return "privilege/del_privilege_user_role";
	}*/
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
   /*@RequestMapping(value = "delPrivilegeUserRole", method = RequestMethod.POST)
    public String delPrivilegeUserRole(PrivilegeUserRoleDto privilegeUserRoleDto) throws Exception {
        final String fullUri = privilegeUserRoleDto.getDelFullUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }*/
   /*
    *  Entrance:   step-1
    * */
	/*@RequestMapping(value = "modifyPrivilegeRole", method = RequestMethod.GET)
	public String modifyPrivilegeRole(Model model) {
		model.addAttribute("modiPrivilegeRoleUri", modiPrivilegeRoleUri);
		return "privilege/modi_privilege_role";
	}*/
	/* 
   * Redirect to oauth-server bind page:   step-2
   * */
  /*@RequestMapping(value = "modifyPrivilegeRole", method = RequestMethod.POST)
   public String modifyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
       final String fullUri = privilegeRoleDto.getModiFullUri();
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
   }*/
  /*
   *  Entrance:   step-1
   * */
	/*@RequestMapping(value = "getPrivilegeRole", method = RequestMethod.GET)
	public String getPrivilegeRole(Model model) {
		model.addAttribute("getPrivilegeRoleUri", getPrivilegeRoleUri);
		return "privilege/get_privilege_role";
	}*/
	/* 
  * Redirect to oauth-server bind page:   step-2
  * */
 /*@RequestMapping(value = "getPrivilegeRole", method = RequestMethod.POST)
  public String getfyPrivilegeRole(PrivilegeRoleDto privilegeRoleDto) throws Exception {
      final String fullUri = privilegeRoleDto.getGetFullUri();
      LOG.debug("Send to Oauth-Server URL: {}", fullUri);
      return "redirect:" + fullUri;
  }*/

}