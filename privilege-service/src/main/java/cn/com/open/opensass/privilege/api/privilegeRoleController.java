package cn.com.open.opensass.privilege.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;

@Controller
@RequestMapping("/role/")
public class privilegeRoleController {
	 @Autowired
	 private PrivilegeRoleService privilegeRoleService;
	 /*@Value("#{properties['server-host']}")
     private String serverHost;*/
	/**
	 * 首页
	 * @param code
	 */
	@RequestMapping("addRole")
    public void userCenterReg(HttpServletRequest request,HttpServletResponse response,PrivilegeRole privilegeRole) {
		privilegeRoleService.savePrivilegeRole(privilegeRole);
	}
}
