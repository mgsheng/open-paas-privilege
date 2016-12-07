package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;

@Controller
@RequestMapping("/role/")
public class PrivilegeRoleController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(PrivilegeRoleController.class); 
	@Autowired
	 private PrivilegeRoleService privilegeRoleService;
	 /*@Value("#{properties['server-host']}")
     private String serverHost;*/
	/**
	 * 角色初始创建接口
	 */
	@RequestMapping(value = "addRole")
    public void userCenterReg(HttpServletRequest request,HttpServletResponse response,PrivilegeRoleVo privilegeRoleVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add role start======================");
    	if(!paraMandatoryCheck(Arrays.asList(privilegeRoleVo.getAppId(),privilegeRoleVo.getRoleName()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}
    	PrivilegeRole privilegeRole=new PrivilegeRole();
    	privilegeRole.setAppId(privilegeRoleVo.getAppId());
    	privilegeRole.setRoleName(privilegeRoleVo.getRoleName());
    	privilegeRole.setGroupId(privilegeRoleVo.getGroupId());
    	privilegeRole.setGroupName(privilegeRoleVo.getGroupName());
    	privilegeRole.setDeptId(privilegeRoleVo.getDeptId());
    	privilegeRole.setDeptName(privilegeRoleVo.getDeptName());
    	privilegeRole.setRoleLevel(privilegeRoleVo.getRoleLevel());
    	privilegeRole.setRemark(privilegeRoleVo.getRemark());
    	privilegeRole.setCreateUser(privilegeRoleVo.getCreateUser());
    	privilegeRole.setCreateUserId(privilegeRoleVo.getCreateUserId());
    	privilegeRole.setStatus(privilegeRoleVo.getStatus());
    	
    	privilegeRoleService.savePrivilegeRole(privilegeRole);
    	map.put("status", 1);
    	map.put("privilegeRoleid", privilegeRole.getPrivilegeRoleId());
    	
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    	
    }
}
