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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import net.sf.json.JSONObject;
@Controller
@RequestMapping("/UserMenu/")
public class UserMenuRedisPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserMenuRedisPrivilegeController.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	
	@Autowired 
	private PrivilegeResourceService privilegeResourceService; 
	
	@Autowired
	private  RedisClientTemplate redisClientTemplate;
	@Autowired
	private  PrivilegeMenuService privilegeMenuService;
	
	@RequestMapping("getUserMenuPrivilege")
	public void MenuRedisPrivilege(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo){
		Set<String> menuIdSet=new HashSet<String>();
		log.info("====================redis user menu start======================");    	
		Map<String,List<Map<String, Object>>> menuMap=new HashMap<String,List<Map<String, Object>>>();
		if(!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId()))){
  		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
            return;
		}  
		PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(user==null){
    		 paraMandaChkAndReturn(10001, response,"该用户不存在");
             return;
    	}
    	
    	//redis key
    	String userCacheMenuKey="privilegeService_userCacheMenu_"+user.getAppId()+"_"+user.getAppUserId();
    	
    		//取缓存
        	if (redisClientTemplate.getString(userCacheMenuKey)!=null) {
    			String jString=redisClientTemplate.getString(userCacheMenuKey);
    			JSONObject  jasonObject = JSONObject.fromObject(jString);
    			Map JsonMap = (Map)jasonObject;
    		    writeSuccessJson(response, JsonMap);  
    		    redisClientTemplate.disconnect();
    			return;
    		}
		
		
    	
    	//resourceList
    	String privilegeResourceIds=user.getResourceId();
    	
    	if(privilegeResourceIds!=null&&!("").equals(privilegeResourceIds)){
    		String[] resourceIds1 = privilegeResourceIds.split(",");//将当前user privilegeResourceIds字段数组转list
    		List<String> resourceIdList = new ArrayList<String>();
    		Collections.addAll(resourceIdList, resourceIds1);
			PrivilegeResource resource=null;
    		for(String resourceId : resourceIdList){
    			resource=privilegeResourceService.findByResourceId(resourceId);
    			if (resource.getMenuId()!=null&&!("").equals(resource.getMenuId())) {
    				menuIdSet.add(resource.getMenuId());
				}
    		}
    		
    	}
    	
    	//menuList
    	
    	if (menuIdSet!=null) {
			List<Map<String, Object>> menuList=new ArrayList<Map<String,Object>>();
			for(String menuId:menuIdSet){
				Map<String, Object> map2=privilegeMenuService.findByMenuId(menuId);
				
				menuList.add(map2);
			}
				menuMap.put("menuList", menuList);
		}
    	
    		redisClientTemplate.setString(userCacheMenuKey, JSONObject.fromObject(menuMap).toString());
		
			redisClientTemplate.disconnect();
	
    	
    	
    	/*String	json=JSONObject.fromObject(menuMap).toString();
    	System.err.println(json+"==json");*/
    	writeSuccessJson(response,menuMap);
	}
}