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
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.StringTool;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

@Controller
@RequestMapping("/role/")
public class RoleModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(RoleModifyPrivilegeController.class); 
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 角色权限修改接口
	 */
	@RequestMapping(value = "modifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("===================modify rolePrivilege start======================");
    	
    	String appId=request.getParameter("appId");
    	String privilegeRoleId=request.getParameter("privilegeRoleId");
    	String method=request.getParameter("method");
    	String rolePrivilege=request.getParameter("rolePrivilege");//所拥有权限（多个用，分隔）
    	String privilegeFunId=request.getParameter("privilegeFunId");//所拥有的方法（多个用，分隔）
    	String roleName=request.getParameter("roleName");
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
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,privilegeRoleId,method))){
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
			paraMandaChkAndReturn(10006, response,"认证失败");
			return;
		}
		List<PrivilegeRoleResource> roleResourceList=null;
    	if(rolePrivilege!=null && !("").equals(rolePrivilege)){
    		String[] roleResources = rolePrivilege.split(",");
    		PrivilegeRoleResource roleResource1=null;
			roleResourceList=privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleId);
    		for(String roleResource : roleResources){
    			roleResource1 = privilegeRoleResourceService.findByRoleIdAndResourceId(privilegeRoleId,roleResource);
    			if(("0").equals(method)){//添加权限
					roleResource1 = new PrivilegeRoleResource();
					roleResource1.setPrivilegeRoleId(privilegeRoleId);
					roleResource1.setResourceId(roleResource);
					roleResource1.setCreateUser(createUser);
					roleResource1.setCreateUserId(createUserId);
					if(roleResourceList!=null && roleResourceList.size()>0){
						roleResource1.setPrivilegeFunId(roleResourceList.get(0).getPrivilegeFunId());
					}
					if(status!=null){
						roleResource1.setStatus(Integer.parseInt(status));
					}						
					Boolean sf = privilegeRoleResourceService.savePrivilegeRoleResource(roleResource1);
					if(!sf){
						paraMandaChkAndReturn(10003, response,"添加权限失败");
			            return;
					}
    			}else if(("1").equals(method)){//删除权限
    				if(roleResource1 != null){
    					Boolean df = privilegeRoleResourceService.delPrivilegeRoleResource(roleResource1);
    					if(!df){
							paraMandaChkAndReturn(10005, response,"删除权限失败");
				            return;
						}
    				}
    			}
    		} 
    	}
    	if(privilegeFunId!=null && !("").equals(privilegeFunId)){
    		PrivilegeRoleResource roleResource1=null;
			roleResourceList=privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleId);
			//更新所有记录的privilegeFunId字段
			if(roleResourceList != null && roleResourceList.size()>0){
				for(PrivilegeRoleResource roleRes:roleResourceList){
					if(("0").equals(method)){//添加
						if(roleRes.getPrivilegeFunId() != null && !("").equals(roleRes.getPrivilegeFunId())){
							roleRes.setPrivilegeFunId(roleRes.getPrivilegeFunId()+","+privilegeFunId);
						}else{
							roleRes.setPrivilegeFunId(privilegeFunId);
						}								
					}else if(("1").equals(method)){//删除
						if(roleRes.getPrivilegeFunId() != null && !("").equals(roleRes.getPrivilegeFunId())){
							String[] tFunIds=roleRes.getPrivilegeFunId().split(",");//表中现有的functionId
							String[] sFunIds=privilegeFunId.split(",");//需删除的functionId
							List<String> funIdList=new ArrayList<String>();
							for(int i=0;i<tFunIds.length;i++){
								for(int j=0;j<sFunIds.length;j++){
									if(!(sFunIds[j]).equals(tFunIds[i])){
										funIdList.add(tFunIds[i]);
									}
								}
							}
							roleRes.setPrivilegeFunId(StringTool.listToString(funIdList));
						}
					}
					Boolean uf = privilegeRoleResourceService.updatePrivilegeRoleResource(roleRes);
					if(!uf){
						paraMandaChkAndReturn(10004, response,"修改权限失败");
			            return;
					}
				}
			}else{
				roleResource1 = new PrivilegeRoleResource();
				roleResource1.setPrivilegeRoleId(privilegeRoleId);
				roleResource1.setResourceId("");
				roleResource1.setCreateUser(createUser);
				roleResource1.setCreateUserId(createUserId);
				if(status!=null){
					roleResource1.setStatus(Integer.parseInt(status));
				}						
				roleResource1.setPrivilegeFunId(privilegeFunId);
				Boolean sf = privilegeRoleResourceService.savePrivilegeRoleResource(roleResource1);
				if(!sf){
					paraMandaChkAndReturn(10003, response,"添加权限失败");
		            return;
				}
			}
    	}

    	//修改privilegeRole
    	PrivilegeRole privilegeRole = privilegeRoleService.findRoleById(privilegeRoleId);
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
    	Boolean f1 = privilegeRoleService.updatePrivilegeRole(privilegeRole);//更新role
    	if(!f1){
			paraMandaChkAndReturn(10002, response,"修改角色失败");
            return;
		}
    	//更新缓存
		PrivilegeAjaxMessage message=privilegeRoleService.updateAppRoleRedis(appId);
		if (message.getCode().equals("1")) {
			map.put("status","1");
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
		}

    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
