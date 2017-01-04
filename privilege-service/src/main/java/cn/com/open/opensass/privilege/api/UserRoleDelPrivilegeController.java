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
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleDelPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserRoleDelPrivilegeController.class); 
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
	/**
	 * 用户角色删除接口
	 */
	@RequestMapping(value = "delRole")
    public void delRole(HttpServletRequest request,HttpServletResponse response,PrivilegeUserVo privilegeUserVo) {
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================del user role start======================");    	
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
			paraMandaChkAndReturn(10004, response,"认证失败");
			return;
		}
    	PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(user==null){
    		paraMandaChkAndReturn(10001, response,"用户不存在");
            return;
    	}
    	Boolean df = privilegeUserService.delUserByAppIdAndAppUserId(privilegeUserVo.getAppId(),privilegeUserVo.getAppUserId());
    	if(df){
    		Boolean f1 = privilegeUserRoleService.delPrivilegeUserRoleByUid(user.getuId());
    		if(!f1){
    			paraMandaChkAndReturn(10003, response,"删除用户角色关系失败");
                return;
    		}
    		//删除缓存
			PrivilegeAjaxMessage message=privilegeUserRedisService.delUserRoleRedis(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
			if (message.getCode().equals("1")) {
				map.put("status","1");
			} else {
				map.put("status", message.getCode());
				map.put("error_code", message.getMessage());/* 数据不存在 */
			}
    	}else{
    		paraMandaChkAndReturn(10002, response,"删除用户失败");
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
