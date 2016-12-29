package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
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
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import cn.com.open.opensass.privilege.vo.PrivilegeRoleVo;
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
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	/**
	 * 用户角色权限获取接口
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
			map.put("status", 1);
	    	map.put("appId", user.getAppId());
	    	map.put("appUserId", user.getAppUserId());
	    	map.put("appUserName", user.getAppUserName());
	    	map.put("deptId", user.getDeptId());
	    	map.put("groupId",user.getGroupId());
	    	map.put("privilegeFunId", user.getPrivilegeFunId());
		}
		
		//redisClient.del(prefixMenu+user.getAppId()+SIGN+user.getuId());
		//redisClient.del(prefixRole+user.getAppId()+SIGN+user.getuId());
		
		//从redis中获取usermenu,userrole信息
		Map<String, Object> menuMap=(Map<String, Object>) redisClient.getObject(prefixRole+user.getAppId()+SIGN+user.getuId());
		Map<String, Object> roleMap=(Map<String, Object>) redisClient.getObject(prefixMenu+user.getAppId()+SIGN+user.getuId());
		
		if(roleMap == null){//redis中没有，从数据库中查询并存入redis
			roleMap = new HashMap<String,Object>();
			// roleList
			List<Map<String, Object>> roles = privilegeRoleService.getRoleListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("roleList", roles);
			// resourceList
			List<Map<String, Object>> resourceList = privilegeResourceService.getResourceListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("resourceList", resourceList);
			// functionList
			List<Map<String, Object>> functionList = privilegeFunctionService.getFunctionListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("functionList", functionList);
			redisClient.setObject(prefixRole+user.getAppId()+SIGN+user.getuId(),roleMap);
		}
		if(menuMap == null){//redis中没有，从数据库中查询并存入redis
			menuMap = new HashMap<String,Object>();
			List<PrivilegeMenu> menuList = privilegeMenuService.getMenuListByUserId(user.getAppUserId(), user.getAppId());
			Map<String,Object> menumap = new HashMap<String,Object>();
			List<Map<String,Object>> menus = new ArrayList<Map<String,Object>>();
			for(PrivilegeMenu menu:menuList){
				menumap.put("menuId", menu.getMenuId());
				menumap.put("parentId", menu.getParentId());
				menumap.put("menuName", menu.getMenuName());
				menumap.put("menuRule", menu.getMenuRule());
				menumap.put("menuLevel", menu.getMenuLevel());
				menumap.put("displayOrder", menu.getDisplayOrder());
				menus.add(menumap);
			}
			menuMap.put("muneList", menus);
			redisClient.setObject(prefixMenu+user.getAppId()+SIGN+user.getuId(),roleMap);
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
