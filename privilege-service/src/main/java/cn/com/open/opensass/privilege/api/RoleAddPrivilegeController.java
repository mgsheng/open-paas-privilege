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

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

@Controller
@RequestMapping("/role/")
public class RoleAddPrivilegeController extends BaseControllerUtil{
	private static final String AppRoleRedisPrefix = RedisConstant.APPROLE_CACHE;
	private static final Logger log = LoggerFactory.getLogger(RoleAddPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 角色初始创建接口
	 */
	@RequestMapping(value = "addRole")
    public void addRole(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add role start======================");    
    	
    	String appId=request.getParameter("appId");
    	String roleName=request.getParameter("roleName");
    	String rolePrivilege=request.getParameter("rolePrivilege");//所拥有权限（多个用，分隔）
    	String privilegeFunId=request.getParameter("privilegeFunId");//所拥有的方法（多个用，分隔）
    	String groupId=request.getParameter("groupId");
    	String groupName=request.getParameter("groupName");
    	String deptId=request.getParameter("deptId");
    	String deptName=request.getParameter("deptName");
    	String roleLevel=request.getParameter("roelLevel");
    	String roleType=request.getParameter("roleType");
    	String parentRoleId=request.getParameter("parentRoleId");
    	String remark=request.getParameter("remark");
    	String createUser=request.getParameter("createUser");
    	String createUserId=request.getParameter("createUserId");
    	String status=request.getParameter("status");
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,roleName))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    	
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
	    if(app==null){
		   app=appService.findById(Integer.parseInt(appId));
		   redisClient.setObject(RedisConstant.APP_INFO+appId, app);
	    }
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
		if(!f){
			paraMandaChkAndReturn(10004, response,"认证失败");
			return;
		}
    	PrivilegeRole privilegeRole=new PrivilegeRole();
    	privilegeRole.setAppId(appId);
    	privilegeRole.setRoleName(roleName);
    	privilegeRole.setGroupId(groupId);
    	privilegeRole.setGroupName(groupName);
    	privilegeRole.setDeptId(deptId);
    	privilegeRole.setDeptName(deptName);
    	if(parentRoleId==null || ("0").equals(parentRoleId)){
    		privilegeRole.setRoleLevel(0);
    		privilegeRole.setParentRoleId("0");
    	}else{
    		PrivilegeRole privilegeRole1=privilegeRoleService.findRoleById(parentRoleId);
    		if(privilegeRole1 == null){
    			paraMandaChkAndReturn(10001, response,"父角色id不存在");
                return;
    		}
    		privilegeRole.setRoleLevel(1);
        	privilegeRole.setParentRoleId(parentRoleId);
    	}
    	if(roleType!=null){
    		privilegeRole.setRoleType(Integer.parseInt(roleType));
    	}
    	privilegeRole.setRemark(remark);
    	privilegeRole.setCreateUser(createUser);
    	privilegeRole.setCreateUserId(createUserId);
    	if(status!=null){
    		privilegeRole.setStatus(Integer.parseInt(status));
    	} 	
    	
    	Boolean sf = privilegeRoleService.savePrivilegeRole(privilegeRole);//添加角色表
    	if(sf){
	    	if(rolePrivilege!=null){//添加权限资源关系表
	    		String[] roleResources = rolePrivilege.split(",");
	    		for(String roleResource : roleResources){
	    			PrivilegeRoleResource privilegeRoleResource = new PrivilegeRoleResource();
	    			privilegeRoleResource.setPrivilegeRoleId(privilegeRole.getPrivilegeRoleId());
	    			privilegeRoleResource.setResourceId(roleResource);
	    			privilegeRoleResource.setPrivilegeFunId(privilegeFunId);
	    			privilegeRoleResource.setCreateUser(createUser);
	    			privilegeRoleResource.setCreateUserId(createUserId);
	    			privilegeRoleResource.setStatus(Integer.parseInt(status));
	    			
	    			Boolean f1 = privilegeRoleResourceService.savePrivilegeRoleResource(privilegeRoleResource);
	    			if(!f1){
	    				paraMandaChkAndReturn(10003, response,"角色资源关系添加失败");
	    	            return;
	    			}
	    		}
	    	}else if(privilegeFunId!=null){
	    		PrivilegeRoleResource privilegeRoleResource = new PrivilegeRoleResource();
    			privilegeRoleResource.setPrivilegeRoleId(privilegeRole.getPrivilegeRoleId());
    			privilegeRoleResource.setPrivilegeFunId(privilegeFunId);
    			privilegeRoleResource.setCreateUser(createUser);
    			privilegeRoleResource.setCreateUserId(createUserId);
    			privilegeRoleResource.setStatus(Integer.parseInt(status));
    			
    			Boolean f1 = privilegeRoleResourceService.savePrivilegeRoleResource(privilegeRoleResource);
    			if(!f1){
    				paraMandaChkAndReturn(10003, response,"角色资源关系添加失败");
    	            return;
    			}
	    	}
	    	//存放缓存
	    	PrivilegeAjaxMessage message1=privilegeRoleService.delAppRoleRedis(appId);
	    	if (message1.getCode().equals("1")) {
	    		PrivilegeAjaxMessage message=privilegeRoleService.getAppRoleRedis(appId);
	    		if (message.getCode().equals("1")) {
	    			map.put("status","1");
	        		map.put("privilegeRoleid", privilegeRole.getPrivilegeRoleId());
	    		} else {
	    			map.put("status", message.getCode());
	    			map.put("error_code", message.getMessage());/* 数据不存在 */
	    		}
	    	}
    	}else{
    		paraMandaChkAndReturn(10002, response,"角色添加失败");
            return;
    	}
    	
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
