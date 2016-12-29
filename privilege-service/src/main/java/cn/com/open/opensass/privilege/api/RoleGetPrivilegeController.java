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
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeFunctionVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
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
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 角色权限查询接口
	 */
	@RequestMapping(value = "getRolePrivilege")
    public void getRolePrivilege(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("===================get rolePrivilege start======================");
    	
    	String appId=request.getParameter("appId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("limit");
    	String deptId=request.getParameter("depgId");
    	String groupId=request.getParameter("groupId");
    	String privilegeRoleId=request.getParameter("privilegeRoleId");
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,start,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
	    if(app==null)
		   {
			   app=appService.findById(Integer.parseInt(appId));
			   redisClient.setObject(RedisConstant.APP_INFO+appId, app);
		  }
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
    	List<PrivilegeRoleVo> roles = new ArrayList<PrivilegeRoleVo>();
    	List<PrivilegeResourceVo> resources = new ArrayList<PrivilegeResourceVo>();
    	List<PrivilegeFunctionVo> functions = new ArrayList<PrivilegeFunctionVo>();
    	//取出角色List
    	int count = privilegeRoleService.findRoleNoPage(privilegeRoleId,appId);
    	List<PrivilegeRole> privilegeRoles = privilegeRoleService.findRoleByPage(privilegeRoleId,appId,Integer.parseInt(start),Integer.parseInt(limit));
    	for(PrivilegeRole privilegeRole : privilegeRoles){
			List<PrivilegeRoleResource> privilegeRoleResources = privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRole.getPrivilegeRoleId());

			List<String> funcIdList = new ArrayList<String>();
			for(PrivilegeRoleResource privilegeRoleResource : privilegeRoleResources){
				PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(privilegeRoleResource.getResourceId(), appId);
				PrivilegeResourceVo privilegeResourceVo = new PrivilegeResourceVo();
				if(privilegeResource!=null){
					privilegeResourceVo.setAppId(privilegeResource.getAppId());
					privilegeResourceVo.setResourceId(privilegeResource.getResourceId());
					privilegeResourceVo.setResourceLevel(privilegeResource.getResourceLevel()+"");
					privilegeResourceVo.setResourceName(privilegeResource.getResourceName());
					privilegeResourceVo.setResourceRule(privilegeResource.getResourceRule());
					privilegeResourceVo.setDisplayOrder(privilegeResource.getDisplayOrder());
					privilegeResourceVo.setMenuId(privilegeResource.getMenuId());
					privilegeResourceVo.setBaseUrl(privilegeResource.getBaseUrl());
					privilegeResourceVo.setStatus(privilegeResource.getStatus());
					resources.add(privilegeResourceVo);//添加到资源List
					if(privilegeRoleResource.getPrivilegeFunId()!=null){
						String[] funcIds = privilegeRoleResource.getPrivilegeFunId().split(",");
						for(String funcId : funcIds){
							if(!funcIdList.contains(funcId)){
								funcIdList.add(funcId);
								PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(funcId);
								if(privilegeFunction != null){
									PrivilegeFunctionVo privilegeFunctionVo = new PrivilegeFunctionVo();
		    						privilegeFunctionVo.setFunctionId(privilegeFunction.getId());
		    						privilegeFunctionVo.setOptId(privilegeFunction.getOperationId());
		    						privilegeFunctionVo.setResourceId(privilegeFunction.getResourceId());
		    						privilegeFunctionVo.setOptUrl(privilegeFunction.getOptUrl());
		    						functions.add(privilegeFunctionVo);//添加到方法List
		    					}
							}
						}
					} 
				}
			}
			PrivilegeRoleVo privilegeRoleVo = new PrivilegeRoleVo();
			privilegeRoleVo.setAppId(privilegeRole.getAppId());
			privilegeRoleVo.setDeptId(privilegeRole.getDeptId());
			privilegeRoleVo.setDeptName(privilegeRole.getDeptName());
			privilegeRoleVo.setGroupId(privilegeRole.getGroupId());
			privilegeRoleVo.setGroupName(privilegeRole.getGroupName());
			privilegeRoleVo.setPrivilegeRoleId(privilegeRole.getPrivilegeRoleId());
			privilegeRoleVo.setRemark(privilegeRole.getRemark());
			privilegeRoleVo.setRoleLevel(privilegeRole.getRoleLevel());
			privilegeRoleVo.setRoleName(privilegeRole.getRoleName());
			privilegeRoleVo.setStatus(privilegeRole.getStatus());
			privilegeRoleVo.setResourceList(resources);
			privilegeRoleVo.setFunctionList(functions);
			roles.add(privilegeRoleVo);//添加到角色List
    	}
    	
    	map.put("roleList", roles);
    	map.put("count", count);
    	map.put("status", 1);
		writeErrorJson(response,map);
    	
    	if(map.get("status")=="0"){
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
