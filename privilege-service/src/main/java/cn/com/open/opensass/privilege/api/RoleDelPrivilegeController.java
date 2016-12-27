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

import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

@Controller
@RequestMapping("/role/")
public class RoleDelPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(RoleDelPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	/**
	 * 角色删除接口
	 */
	@RequestMapping(value = "delRole")
    public void delRole(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================del role start======================");
    	
    	String appId = request.getParameter("appId");
    	String privilegeRoleId = request.getParameter("privilegeRoleId");
    	String createUser = request.getParameter("createUser");
    	String createUserId = request.getParameter("createUserId");
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,privilegeRoleId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    	
    	
    	Boolean f = privilegeRoleService.delPrivilegeRoleById(privilegeRoleId);//删除角色表
    	if(f){
    		Boolean f1 = privilegeRoleResourceService.delRoleResourceByRoleId(privilegeRoleId);//删除角色资源关系表
			if(!f1){
				paraMandaChkAndReturn(10002, response,"角色资源关系删除失败");
	            return;
			}
    		/*List<PrivilegeRoleResource> roleResources = privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleVo.getPrivilegeRoleId());
    		if(roleResources != null){
    			for(PrivilegeRoleResource roleResource : roleResources){
    				Boolean f1 = privilegeRoleResourceService.delPrivilegeRoleResource(roleResource);
	    			if(!f1){
	    				paraMandaChkAndReturn(10002, response,"角色资源关系删除失败");
	    	            return;
	    			}
    			}
    		}*/
    	}else{
    		paraMandaChkAndReturn(10001, response,"角色删除失败");
            return;
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
