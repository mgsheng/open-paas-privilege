package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.StringTool;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;

@Controller
@RequestMapping("/role/")
public class RoleGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(RoleGetPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired 
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	/**
	 * 角色权限查询接口
	 */
	@RequestMapping(value = "getRolePrivilege")
    public void getRolePrivilege(HttpServletRequest request,HttpServletResponse response,PrivilegeRoleVo privilegeRoleVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("===================get rolePrivilege start======================");
    	if(!paraMandatoryCheck(Arrays.asList(privilegeRoleVo.getAppId(),privilegeRoleVo.getStart(),privilegeRoleVo.getLimit()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    

    	List<PrivilegeRole> roles = new ArrayList<PrivilegeRole>();
    	List<PrivilegeResource> resources = new ArrayList<PrivilegeResource>();
    	List<PrivilegeFunction> funcs = new ArrayList<PrivilegeFunction>();
    	
    	String privilegeRoleIds=privilegeRoleVo.getPrivilegeRoleId();
    	
    	if(privilegeRoleIds == null || ("").equals(privilegeRoleIds.trim())){
    		List<String> roleIds = privilegeRoleService.findRoleByAppId(privilegeRoleVo.getAppId());
    		privilegeRoleIds = StringTool.listToString(roleIds);
    	}
    	
    	if(privilegeRoleIds != null){
    		String[] roleIds = privilegeRoleIds.split(",");
    		PrivilegeRole role = null;
    		for(String roleId : roleIds){
    			role = privilegeRoleService.findRoleById(roleId);
    			List<PrivilegeRoleResource> roleResources = privilegeRoleResourceService.findByPrivilegeRoleId(roleId);
    			if(roleResources!=null){
        			PrivilegeResource resource = null;
    				for(PrivilegeRoleResource roleResource : roleResources){
    					resource = privilegeResourceService.findByResourceId(roleResource.getResourceId(), privilegeRoleVo.getAppId());
    					if(resource != null){
    						resources.add(resource);
    					}
    					if(roleResource.getPrivilegeFunId()!=null){
    						String[] funcIds = roleResource.getPrivilegeFunId().split(",");
    						PrivilegeFunction function = null;
    						for(String funcId : funcIds){
    							function = privilegeFunctionService.findByFunctionId(funcId);
    							if(function != null){
    	    						funcs.add(function);
    	    					}
    						}
    					}    					
    				}
    				role.setFunctionList(funcs);
    				role.setResourceList(resources);
    			}    			    			
    			roles.add(role);
    		}
    	}
    	map.put("roleList", roles);
    	map.put("count", roles.size());
    	map.put("status", 1);
		writeErrorJson(response,map);
    	
    	if(map.get("status")=="0"){
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
