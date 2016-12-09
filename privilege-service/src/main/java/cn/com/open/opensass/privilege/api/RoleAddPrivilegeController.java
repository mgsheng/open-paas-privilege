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
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;

@Controller
@RequestMapping("/role/")
public class RoleAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(RoleAddPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	/**
	 * 角色初始创建接口
	 */
	@RequestMapping(value = "addRole")
    public void addRole(HttpServletRequest request,HttpServletResponse response,PrivilegeRoleVo privilegeRoleVo) {
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
    	privilegeRole.setParentRoleId(privilegeRoleVo.getParentRoleId());
    	if(("0").equals(privilegeRoleVo.getParentRoleId())){
        	privilegeRole.setRoleLevel(0);//无层级
    	}else{
    		PrivilegeRole privilegeRole1=privilegeRoleService.findRoleById(privilegeRoleVo.getParentRoleId());
    		if(privilegeRole1 == null){
    			paraMandaChkAndReturn(10001, response,"父角色id不存在");
                return;
    		}
    		privilegeRole.setRoleLevel(1);//有层级
    	}
    	privilegeRole.setRemark(privilegeRoleVo.getRemark());
    	privilegeRole.setCreateUser(privilegeRoleVo.getCreateUser());
    	privilegeRole.setCreateUserId(privilegeRoleVo.getCreateUserId());
    	privilegeRole.setStatus(privilegeRoleVo.getStatus()); 	
    	
    	Boolean f = privilegeRoleService.savePrivilegeRole(privilegeRole);//添加角色表
    	if(f){
	    	if(privilegeRoleVo.getRolePrivilege()!=null){//添加权限资源关系表
	    		String[] roleResources = privilegeRoleVo.getRolePrivilege().split(",");
	    		for(String roleResource : roleResources){
	    			PrivilegeRoleResource privilegeRoleResource = new PrivilegeRoleResource();
	    			privilegeRoleResource.setPrivilegeRoleId(privilegeRole.getPrivilegeRoleId());
	    			privilegeRoleResource.setResourceId(roleResource);
	    			privilegeRoleResource.setCreateUser(privilegeRoleVo.getCreateUser());
	    			privilegeRoleResource.setCreateUserId(privilegeRoleVo.getCreateUserId());
	    			privilegeRoleResource.setStatus(privilegeRoleVo.getStatus());
	    			
	    			Boolean f1 = privilegeRoleResourceService.savePrivilegeRoleResource(privilegeRoleResource);
	    			if(!f1){
	    				paraMandaChkAndReturn(10003, response,"角色资源关系添加失败");
	    	            return;
	    			}
	    		}
	    	}
    	}else{
    		paraMandaChkAndReturn(10002, response,"角色添加失败");
            return;
    	}
    	
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
