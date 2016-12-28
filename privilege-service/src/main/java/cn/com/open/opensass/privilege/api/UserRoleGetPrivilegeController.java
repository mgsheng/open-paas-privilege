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

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserRoleGetPrivilegeController.class); 
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired 
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired 
	private PrivilegeResourceService privilegeResourceService;
	@Autowired 
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 用户角色修改接口
	 */
	@RequestMapping(value = "getUserPrivilege")
    public void modifyPrivilege(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================get user privilege start======================");    	
    	if(!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}  
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+privilegeUserVo.getAppId());
	    if(app==null)
		   {
			   app=appService.findById(Integer.parseInt(privilegeUserVo.getAppId()));
			   redisClient.setObject(RedisConstant.APP_INFO+privilegeUserVo.getAppId(), app);
		  }
	     //认证
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
    	
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
    	List<PrivilegeMenu> menus = new ArrayList<PrivilegeMenu>();
    	List<PrivilegeRole> roles = new ArrayList<PrivilegeRole>();
    	List<PrivilegeResourceVo> resources = new ArrayList<PrivilegeResourceVo>();
    	
    	PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(user.getPrivilegeRoleId()!=null && !("").equals(user.getPrivilegeRoleId())){//通过角色获取resource,menu
    		String[] roleIds = user.getPrivilegeRoleId().split(",");
    		PrivilegeRole role = null;
    		PrivilegeRoleVo roleVo = new PrivilegeRoleVo();
    		for(String roleId : roleIds){
    			role = privilegeRoleService.findRoleById(roleId);
    			List<PrivilegeRoleResource> roleResources = privilegeRoleResourceService.findByPrivilegeRoleId(roleId);
    			roleVo.setAppId(role.getAppId());
    			roleVo.setDeptId(role.getDeptId());
    			roleVo.setDeptName(role.getDeptName());
    			roleVo.setGroupId(role.getGroupId());
    			roleVo.setGroupName(role.getGroupId());
    			roleVo.setPrivilegeRoleId(role.getPrivilegeRoleId());
    			roleVo.setRemark(role.getRemark());
    			roleVo.setRoleLevel(role.getRoleLevel());
    			roleVo.setRoleName(role.getRoleName());
    			if(roleResources!=null){
        			PrivilegeResource resource = null;
        			PrivilegeResourceVo resourceVo = new PrivilegeResourceVo();
        			PrivilegeMenu menu = null;
    				for(PrivilegeRoleResource roleResource : roleResources){
    					resource = privilegeResourceService.findByResourceId(roleResource.getResourceId(), privilegeUserVo.getAppId());
    					resourceVo.setAppId(resource.getAppId());
    					resourceVo.setResourceId(resource.getResourceId());
    					resourceVo.setResourceLevel(resource.getResourceLevel()+"");
    					resourceVo.setResourceName(resource.getResourceName());
    					resourceVo.setResourceRule(resource.getResourceRule());
    					resourceVo.setDisplayOrder(resource.getDisplayOrder());
    					resourceVo.setMenuId(resource.getMenuId());
    					resourceVo.setBaseUrl(resource.getBaseUrl());
    					resourceVo.setStatus(resource.getStatus());
    					resources.add(resourceVo);
    					menu = privilegeMenuService.findByMenuId(resource.getMenuId(), resource.getAppId());
    					menus.add(menu);
    					while(!("0").equals(menu.getParentId())){
    						menu = privilegeMenuService.findByMenuId(menu.getParentId()+"", menu.getAppId());
    						menus.add(menu);    						
    					}
    				}
    				roleVo.setResourceList(resources);
    			}    			    			
    			roles.add(role);
    		}
    	}
    	
    	if(user.getResourceId()!=null && !("").equals(user.getResourceId())){//通过资源获取resource,menu
    		String[] resourceIds = user.getResourceId().split(",");
    		PrivilegeResource resource = null;
    		PrivilegeResourceVo resourceVo = new PrivilegeResourceVo();
    		PrivilegeMenu menu = null;
    		for(String resourceId : resourceIds){
    			resource = privilegeResourceService.findByResourceId(resourceId, user.getAppId());
    			resourceVo.setAppId(resource.getAppId());
				resourceVo.setResourceId(resource.getResourceId());
				resourceVo.setResourceLevel(resource.getResourceLevel()+"");
				resourceVo.setResourceName(resource.getResourceName());
				resourceVo.setResourceRule(resource.getResourceRule());
				resourceVo.setDisplayOrder(resource.getDisplayOrder());
				resourceVo.setMenuId(resource.getMenuId());
				resourceVo.setBaseUrl(resource.getBaseUrl());
				resourceVo.setStatus(resource.getStatus());
    			resources.add(resourceVo);
    			menu = privilegeMenuService.findByMenuId(resource.getMenuId(), resource.getAppId());
				menus.add(menu);
				while(!("0").equals(menu.getParentId())){
					menu = privilegeMenuService.findByMenuId(menu.getParentId()+"", menu.getAppId());
					menus.add(menu);    						
				}
    		}
    	}
    	
    	user.setMenuList(menus);
    	user.setRoleList(roles);    	
    	
    	map.put("status", 1);
    	map.put("appId", user.getAppId());
    	map.put("appUserId", user.getAppUserId());
    	map.put("appUserName", user.getAppUserName());
    	map.put("deptId", user.getDeptId());
    	map.put("groupId",user.getGroupId());
    	map.put("privilegeFunId", user.getPrivilegeFunId());
    	map.put("roleId",user.getPrivilegeRoleId());
    	map.put("menuList", user.getMenuList());
    	map.put("roleList", user.getRoleList());
    	
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
