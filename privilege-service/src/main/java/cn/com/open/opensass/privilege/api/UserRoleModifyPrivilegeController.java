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

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.StringTool;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserRoleModifyPrivilegeController.class); 
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired 
	private PrivilegeUserRoleService privilegeUserRoleService;
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

    	String method = privilegeUserVo.getMethod();
    	PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(user==null){
    		 paraMandaChkAndReturn(10001, response,"该用户不存在");
             return;
    	}
    	String privilegeRoleIds=user.getPrivilegeRoleId();
    	String privilegeRoleId = "";//最终更新到 user privilegeRoleId 字段
    	
    	if(privilegeUserVo.getPrivilegeRoleId()!=null){
    		String[] roleIds1 = privilegeRoleIds.split(",");//将当前user privilegeRoleId字段数组转list
    		List<String> roleIdList = new ArrayList<String>();
    		Collections.addAll(roleIdList, roleIds1);
    		
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
						Boolean f = privilegeUserRoleService.savePrivilegeUserRole(userRole);
						if(!f){
							paraMandaChkAndReturn(10001, response,"添加角色失败");
				            return;
						}
					}
    			}else if(("1").equals(method)){//删除角色
    				if(userRole != null){
        				roleIdList.remove(roleId);
    					Boolean f = privilegeUserRoleService.delPrivilegeUserRole(userRole);
    					if(!f){
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
        	privilegeUserService.updatePrivilegeUser(user);
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
