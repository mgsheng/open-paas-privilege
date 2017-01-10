package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleGetPrivilegeController extends BaseControllerUtil{
	private static final String prefixRole = RedisConstant.USERROLE_CACHE;
	private static final String prefixMenu = RedisConstant.USERMENU_CACHE;
	public static final String SIGN = RedisConstant.SIGN;
	
	private static final Logger log = LoggerFactory.getLogger(UserRoleGetPrivilegeController.class); 
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired 
	private PrivilegeRoleService privilegeRoleService;
	@Autowired 
	private PrivilegeResourceService privilegeResourceService;
	@Autowired 
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	@Autowired
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 用户角色权限获取接口
	 */
	@RequestMapping(value = "getUserPrivilege")
    public void getPrivilege(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================get user privilege start======================");    	
    	if(!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId()))){
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
			paraMandaChkAndReturn(10001, response,"认证失败");
			return;
		}
		
		//获取当前用户信息
		PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
		if(user == null){
			paraMandaChkAndReturn(10002, response,"用户不存在");
			return;
		}else{
			map.put("status", "1");
	    	map.put("appId", user.getAppId());
	    	map.put("appUserId", user.getAppUserId());
	    	map.put("appUserName", user.getAppUserName());
	    	map.put("deptId", user.getDeptId());
	    	map.put("groupId",user.getGroupId());
	    	map.put("privilegeFunId", user.getPrivilegeFunId());
		}
		try {
			redisClient.del(prefixMenu+user.getAppId()+SIGN+user.getuId());
			redisClient.del(prefixRole+user.getAppId()+SIGN+user.getuId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> roleMap=null;
		Map<String, Object> menuMap=null;
		try {
			//从redis中获取usermenu,userrole信息
			roleMap=(Map<String, Object>) redisClient.getObject(prefixRole+user.getAppId()+SIGN+user.getuId());
			menuMap=(Map<String, Object>) redisClient.getObject(prefixMenu+user.getAppId()+SIGN+user.getuId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Boolean boo=false;//存放是否有管理员角色标志 true-有，false-没有
		String privilegeResourceIds = user.getResourceId();
		String privilegeFunctionIds = user.getPrivilegeFunId();
		
		//redis中没有roleMap，从数据库中查询并存入redis
		if(roleMap == null){
			roleMap = new HashMap<String,Object>();
			// roleList
			List<Map<String, Object>> roles = privilegeRoleService.getRoleListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("roleList", roles);
			// resourceList
			List<PrivilegeRole> roleList = privilegeRoleService.getRoleListByUserIdAndAppId(user.getAppUserId(), user.getAppId());
			List resourceList= new ArrayList<PrivilegeResource>();
			for (PrivilegeRole role:roleList) {
				if (role.getRoleType()!=null) {
					System.out.println(role.getRoleType() == 2);
					if (role.getRoleType() == 2) {// 若角色为系统管理员 则把app拥有的所有资源放入缓存
						resourceList = privilegeResourceService.getResourceListByAppId(user.getAppId());
						boo=true;
					}
				}
				
			}
			// user表多余的resourceId
			List<String> resourceIds = new ArrayList<String>();
			// resourceList
			Set resourceSet = new HashSet();
			if (!boo) {
				resourceList = privilegeResourceService.getResourceListByUserId(user.getAppUserId(), user.getAppId());
				// 通过user表functionId 查 resource
				if(privilegeFunctionIds!=null && !("").equals(privilegeFunctionIds)){
				String[] FunctionIds = privilegeFunctionIds.split(",");
					if (FunctionIds != null && FunctionIds.length > 0) {
						List<Map<String, Object>> resources = privilegeResourceService.getResourceListByFunIds(FunctionIds);
						resourceList.addAll(resources);
					}
				}
				if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)) {
					String[] resourceIds1 = privilegeResourceIds.split(",");// 将当前user
					List<String> resourceIdList = new ArrayList<String>();// privilegeResourceIds字段数组转list
					Collections.addAll(resourceIdList, resourceIds1);
					PrivilegeResourceVo resource = null;
					for (String resourceId : resourceIdList) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						resource = privilegeResourceService.findByResourceId(resourceId);
						map2.put("appId", resource.getAppId());
						map2.put("resourceId", resource.getResourceId());
						map2.put("resourceLevel", resource.getResourceLevel());
						map2.put("resourceName", resource.getResourceName());
						map2.put("resourceRule", resource.getResourceRule());
						map2.put("dislayOrder ", resource.getDisplayOrder());
						map2.put("menuId", resource.getMenuId());
						map2.put("baseUrl", resource.getBaseUrl());
						map2.put("status", resource.getStatus());
						// 通过查roleResource表 user表中functionId 获取的resource 是否包含
						// user表中resource
						if (!resourceList.contains(map2)) {
							System.err.println(resourceId);
							resourceIds.add(resourceId);
						}
						resourceList.add(map2);
					}
				}
				// 获取所有的resource
				List<Map<String, Object>> list = privilegeResourceService.getAllResource(resourceList);
				resourceList.addAll(list);
			}
			resourceSet.addAll(resourceList);
			roleMap.put("resourceList", resourceSet);
			// functionList
			// roleResource表中functionIds
			List<String> FunIds = privilegeRoleResourceService.findUserResourcesFunId(user.getAppId(), user.getAppUserId());
			// 加入 user表中functionIds
			if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
				FunIds.add(privilegeFunctionIds);
			}
			// 查询相应function
			List<Map<String, Object>> privilegeFunctions = new ArrayList<Map<String, Object>>();
			if (FunIds != null) {
				for (String funIds : FunIds) {
					String[] functionIds = funIds.split(",");
					List<Map<String, Object>> functions = privilegeFunctionService.getFunctionListByFunctionIds(functionIds);
					privilegeFunctions.addAll(functions);
				}
			}
			// 查询单独的resource 包含的function
			if (resourceIds.size() > 0) {
				for (String resourceId : resourceIds) {
					List<Map<String, Object>> list = privilegeFunctionService.getFunctionMap(resourceId);
					privilegeFunctions.addAll(list);
				}
			}
			Set<Map<String, Object>> functionSet = new HashSet<Map<String, Object>>();
			functionSet.addAll(privilegeFunctions);
			System.err.println("set" + JSONArray.fromObject(functionSet).toString());
			//List<Map<String, Object>> functionList = privilegeFunctionService.getFunctionListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("functionList", functionSet);
			try {
				redisClient.setObject(prefixRole+user.getAppId()+SIGN+user.getuId(),roleMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//redis中没有menuMap，从数据库中查询并存入redis
		if(menuMap == null){
			menuMap = new HashMap<String,Object>();
			List<PrivilegeMenu> privilegeMenuList = new ArrayList<PrivilegeMenu>();
			if(boo){//有管理员角色获取所有应用下菜单
				privilegeMenuList = privilegeMenuService.getMenuListByAppId(user.getAppId());
			}else{//无管理员角色获取相应权限菜单
				privilegeMenuList = privilegeMenuService.getMenuListByUserId(user.getAppUserId(), user.getAppId());
				//根据user表中functionId resourceId 查询菜单
				if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
					String[] funIds = privilegeFunctionIds.split(",");
					List<PrivilegeMenu> menus = privilegeMenuService.getMenuListByFunctionId(funIds);
					privilegeMenuList.addAll(menus);
				}
				if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)) {
					String[] resIds = privilegeResourceIds.split(",");
					for (String id : resIds) {
						List<PrivilegeMenu> menus = privilegeMenuService.getMenuListByResourceId(id);
						privilegeMenuList.addAll(menus);
					}
				}
			}			
			Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();
			Set<PrivilegeMenuVo> privilegeMenuListData = privilegeMenuService.getAllMenuByUserId(privilegeMenuList,privilegeMenuListReturn);  /*缓存中是否存在*/
			menuMap.put("menuList", privilegeMenuListData);
			try {
				redisClient.setObject(prefixMenu+user.getAppId()+SIGN+user.getuId(),menuMap);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.putAll(menuMap);
		map.putAll(roleMap);

    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    }
}
