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

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;

@Controller
@RequestMapping("/role/")
public class RoleModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(RoleModifyPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	/**
	 * 角色权限修改接口
	 */
	@RequestMapping(value = "modifyPrivilege")
    public void userCenterReg(HttpServletRequest request,HttpServletResponse response,PrivilegeRoleVo privilegeRoleVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("===================modify rolePrivilege start======================");
    	if(!paraMandatoryCheck(Arrays.asList(privilegeRoleVo.getAppId(),privilegeRoleVo.getPrivilegeRoleId(),privilegeRoleVo.getMethod()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    	
    	PrivilegeRole privilegeRole = privilegeRoleService.findRoleById(privilegeRoleVo.getPrivilegeRoleId());
    	String method = privilegeRoleVo.getMethod();
    	if(privilegeRoleVo.getRolePrivilege()!=null){
    		String[] roleResources = privilegeRoleVo.getRolePrivilege().split(",");
    		PrivilegeRoleResource roleResource1=null;
    		for(String roleResource : roleResources){
    			roleResource1 = privilegeRoleResourceService.findByRoleIdAndResourceId(privilegeRoleVo.getPrivilegeRoleId(),roleResource);
    			if(("0").equals(method)){//添加权限
					if(roleResource1 == null){
						roleResource1 = new PrivilegeRoleResource();
						roleResource1.setPrivilegeRoleId(privilegeRoleVo.getPrivilegeRoleId());
						roleResource1.setResourceId(roleResource);
						roleResource1.setCreateUser(privilegeRoleVo.getCreateUser());
						roleResource1.setCreateUserId(privilegeRoleVo.getCreateUserId());
						roleResource1.setStatus(privilegeRoleVo.getStatus());
						Boolean f = privilegeRoleResourceService.savePrivilegeRoleResource(roleResource1);
						if(!f){
							paraMandaChkAndReturn(10001, response,"添加权限失败");
				            return;
						}
					}
    			}else if(("1").equals(method)){//删除权限
    				if(roleResource1 != null){
    					Boolean f = privilegeRoleResourceService.delPrivilegeRoleResource(roleResource1);
    					if(!f){
							paraMandaChkAndReturn(10002, response,"删除权限失败");
				            return;
						}
    				}
    			}
    		} 
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
