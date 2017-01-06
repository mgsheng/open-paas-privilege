package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
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
    	/*Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
		if(!f){
			paraMandaChkAndReturn(10001, response,"认证失败");
			return;
		}*/
		
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
		
		redisClient.del(prefixMenu+user.getAppId()+SIGN+user.getuId());
		redisClient.del(prefixRole+user.getAppId()+SIGN+user.getuId());
		
		//从redis中获取usermenu,userrole信息
		Map<String, Object> roleMap=(Map<String, Object>) redisClient.getObject(prefixRole+user.getAppId()+SIGN+user.getuId());
		Map<String, Object> menuMap=(Map<String, Object>) redisClient.getObject(prefixMenu+user.getAppId()+SIGN+user.getuId());

		Boolean boo=false;//存放是否有管理员角色标志 true-有，false-没有
		
		//redis中没有roleMap，从数据库中查询并存入redis
		if(roleMap == null){
			roleMap = new HashMap<String,Object>();
			// roleList
			List<Map<String, Object>> roles = privilegeRoleService.getRoleListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("roleList", roles);
			// resourceList
			List<PrivilegeRole> roleList = privilegeRoleService.getRoleListByUserIdAndAppId(user.getAppUserId(), user.getAppId());
			List resourceList=null;
			for (PrivilegeRole role : roleList) {
				if (role.getRoleType() == 2) {// 若角色为系统管理员 则把app拥有的所有资源放入缓存
					resourceList = privilegeResourceService.getResourceListByAppId(user.getAppId());
					boo=true;
				}
			}
			if (!boo) {
				resourceList = privilegeResourceService.getResourceListByUserId(user.getAppUserId(), user.getAppId());
			}
			roleMap.put("resourceList", resourceList);
			// functionList
			List<Map<String, Object>> functionList = privilegeFunctionService.getFunctionListByUserId(user.getAppUserId(), user.getAppId());
			roleMap.put("functionList", functionList);
			redisClient.setObject(prefixRole+user.getAppId()+SIGN+user.getuId(),roleMap);
		}
		
		//redis中没有menuMap，从数据库中查询并存入redis
		if(menuMap == null){
			menuMap = new HashMap<String,Object>();
			List<PrivilegeMenu> privilegeMenuList = new ArrayList<PrivilegeMenu>();
			if(boo){//有管理员角色获取所有应用下菜单
				privilegeMenuList = privilegeMenuService.getMenuListByAppId(user.getAppId());
			}else{//无管理员角色获取相应权限菜单
				privilegeMenuList = privilegeMenuService.getMenuListByUserId(user.getAppUserId(), user.getAppId());
			}			
			Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();
			Set<PrivilegeMenuVo> privilegeMenuListData = privilegeMenuService.getAllMenuByUserId(privilegeMenuList,privilegeMenuListReturn);  /*缓存中是否存在*/
			menuMap.put("menuList", privilegeMenuListData);
			redisClient.setObject(prefixMenu+user.getAppId()+SIGN+user.getuId(),menuMap);
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
