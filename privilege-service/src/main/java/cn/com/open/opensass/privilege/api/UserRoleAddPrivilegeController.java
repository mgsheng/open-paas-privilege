package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserRoleAddPrivilegeController.class); 
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired 
	private PrivilegeUserRoleService privilegeUserRoleService;
	/**
	 * 用户角色初始创建接口
	 */
	@RequestMapping(value = "addRole")
    public void addRole(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add user role start======================");    	
    	if(!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getPrivilegeRoleId(),privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId(),privilegeUserVo.getAppUsername()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}  
    	
    	PrivilegeUser user=new PrivilegeUser();
    	user.setAppId(privilegeUserVo.getAppId());
    	user.setPrivilegeRoleId(privilegeUserVo.getPrivilegeRoleId());
    	user.setAppUserid(privilegeUserVo.getAppUserId());
    	user.setAppUsername(privilegeUserVo.getAppUsername());
    	user.setDeptId(privilegeUserVo.getDeptId());
    	user.setGroupId(privilegeUserVo.getGroupId());
    	user.setResourceId(privilegeUserVo.getResourceId());
    	user.setPrivilegeFunId(privilegeUserVo.getResourceId());
    	
    	privilegeUserService.savePrivilegeUser(user);
    	
    	String[] roles=privilegeUserVo.getPrivilegeRoleId().split(",");
    	for(String role : roles){
    		PrivilegeUserRole userRole=new PrivilegeUserRole();
    		userRole.setUserId(user.getuId());
    		userRole.setPrivilegeRoleId(role);
    		userRole.setCreateUser(privilegeUserVo.getCreateUser());
    		userRole.setCreateUserId(privilegeUserVo.getCreateUserId());
    		privilegeUserRoleService.savePrivilegeUserRole(userRole);
    	}
    	
    	map.put("status", 1);
    	
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
