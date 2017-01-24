package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.StringTool;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserRoleModifyPrivilegeController.class); 
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired 
	private PrivilegeUserRoleService privilegeUserRoleService;
	@Autowired
	private PrivilegeUserRedisService privilegeUserRedisService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	/**
	 * 用户角色修改接口
	 */
	@RequestMapping(value = "modifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================modify user role start======================");    	
    	if(!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId(),privilegeUserVo.getMethod()))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}  
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+privilegeUserVo.getAppId());
	    if(app==null){
		   app=appService.findById(Integer.parseInt(privilegeUserVo.getAppId()));
		   redisClient.setObject(RedisConstant.APP_INFO+privilegeUserVo.getAppId(), app);
	    }
	     //认证
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
    	
		if(!f){
			paraMandaChkAndReturn(10003, response,"认证失败");
			return;
		}
    	String method = privilegeUserVo.getMethod();
    	PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(user==null){
    		 paraMandaChkAndReturn(10001, response,"该用户不存在");
             return;
    	}
    	String privilegeRoleIds=user.getPrivilegeRoleId();
    	String privilegeRoleId = "";//最终更新到 user privilegeRoleId 字段
    	
    	if(privilegeUserVo.getPrivilegeRoleId()!=null){
    		String[] roleIds1 =null;
    		List<String> roleIdList = new ArrayList<String>();
    		if (privilegeRoleIds!=null&&!("").equals(privilegeRoleIds)) {
				
    			 roleIds1 = privilegeRoleIds.split(",");//将当前user privilegeRoleId字段数组转list
    			 Collections.addAll(roleIdList, roleIds1);
			}
    		
    		String[] roleIds = privilegeUserVo.getPrivilegeRoleId().split(",");
    		PrivilegeUserRole userRole=null;
    		for(String roleId : roleIds){
    			userRole = privilegeUserRoleService.findByUidAndRoleId(user.getuId(),roleId);
    			if(("0").equals(method)){//添加角色
					if(userRole == null){
						roleIdList.add(roleId);
						privilegeRoleId = privilegeRoleId + roleId + ",";
						userRole = new PrivilegeUserRole();
						userRole.setUserId(user.getuId());
						userRole.setPrivilegeRoleId(roleId);
						userRole.setCreateUserId(privilegeUserVo.getCreateUserId());
						userRole.setCreateUser(privilegeUserVo.getCreateUser());
						Boolean sf = privilegeUserRoleService.savePrivilegeUserRole(userRole);
						if(!sf){
							paraMandaChkAndReturn(10001, response,"添加角色失败");
				            return;
						}
					}
    			}else if(("1").equals(method)){//删除角色
    				if(userRole != null){
        				roleIdList.remove(roleId);
    					Boolean df = privilegeUserRoleService.delPrivilegeUserRole(userRole);
    					if(!df){
							paraMandaChkAndReturn(10002, response,"删除角色失败");
				            return;
						}
    				}
    			}
    		} 
    		privilegeRoleId = StringTool.listToString(roleIdList);

        	user.setPrivilegeRoleId(privilegeRoleId);
        	user.setDeptId(privilegeUserVo.getDeptId());
        	user.setGroupId(privilegeUserVo.getGroupId());
        	user.setResourceId(privilegeUserVo.getResourceId());
        	user.setPrivilegeFunId(privilegeUserVo.getPrivilegeFunId());
        	//更新用户信息
        	boolean uf = privilegeUserService.updatePrivilegeUser(user);
        	if(uf){
        		//更新缓存
    			PrivilegeAjaxMessage message=privilegeUserRedisService.updateUserRoleRedis(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
    			privilegeMenuService.updateMenuRedis(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
    			if (message.getCode().equals("1")) {
    				map.put("status","1");
    			} else {
    				map.put("status", message.getCode());
    				map.put("error_code", message.getMessage());/* 数据不存在 */
    			}
        	}
    	}else {
    		user.setDeptId(privilegeUserVo.getDeptId());
        	user.setGroupId(privilegeUserVo.getGroupId());
        	user.setResourceId(privilegeUserVo.getResourceId());
        	user.setPrivilegeFunId(privilegeUserVo.getPrivilegeFunId());
        	boolean uf = privilegeUserService.updatePrivilegeUser(user);
        	if(uf){
        		//更新缓存
        		
    			PrivilegeAjaxMessage message=privilegeUserRedisService.updateUserRoleRedis(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
    			privilegeMenuService.updateMenuRedis(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
    			if (message.getCode().equals("1")) {
    				map.put("status","1");
    			} else {
    				map.put("status", message.getCode());
    				map.put("error_code", message.getMessage());/* 数据不存在 */
    			}
        	}
		}
    	
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
