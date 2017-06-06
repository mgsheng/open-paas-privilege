package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * redis userRole 接口 
 * 获取数据：getUserRoleRedis?appId=&appUserId=
 * 更新数据：updateUserRoleRedis?appId=&appUserId=
 * 删除数据：deleteUserRoleRedis?appId=&appUserId=
 */
@Controller
@RequestMapping("/userRole/")
public class UserRoleRedisPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserRoleRedisPrivilegeController.class);
	public static final String SIGN = RedisConstant.SIGN;
	@Autowired
	private PrivilegeUserRedisService privilegeUserRedisService;
	@Autowired
	private RedisClientTemplate redisClient;

	/**
	 * 用户角色缓存接口
	 * 
	 */
	@RequestMapping(value = "getUserRoleRedis")
	public void getUserPrivilege(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId").trim();
		String appUserId = request.getParameter("appUserId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis user role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}

		PrivilegeAjaxMessage message = privilegeUserRedisService.getRedisUserRole(appId, appUserId);
		if (message.getCode().equals("1")) {
			WebUtils.writeJson(response, message.getMessage());
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;

	}
	@RequestMapping(value = "updateUserRoleRedis")
	public void updateUserRoleRedis(HttpServletRequest request,HttpServletResponse response){
		String appId = request.getParameter("appId").trim();
		String appUserId = request.getParameter("appUserId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================del redis user role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		//清空大缓存
		StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
		redisUserPrivilegeKey.append(appId);
		redisUserPrivilegeKey.append(SIGN);
		redisUserPrivilegeKey.append(appUserId);
		if(redisClient.existKey(redisUserPrivilegeKey.toString()))
		{
			redisClient.del(redisUserPrivilegeKey.toString());
		}
		PrivilegeAjaxMessage ajaxMessage=privilegeUserRedisService.updateUserRoleRedis(appId, appUserId);
		if (ajaxMessage.getCode().equals("1")) {
			WebUtils.writeJson(response, ajaxMessage.getMessage());
		} else {
			map.put("status", ajaxMessage.getCode());
			map.put("error_code", ajaxMessage.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}
	
	@RequestMapping(value = "deleteUserRoleRedis")
	public void delUserRoleRedis(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId").trim();
		String appUserId = request.getParameter("appUserId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================del redis user role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		//清空大缓存
		StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
		redisUserPrivilegeKey.append(appId);
		redisUserPrivilegeKey.append(SIGN);
		redisUserPrivilegeKey.append(appUserId);
		if(redisClient.existKey(redisUserPrivilegeKey.toString()))
		{
			redisClient.del(redisUserPrivilegeKey.toString());
		}
		PrivilegeAjaxMessage ajaxMessage=privilegeUserRedisService.delUserRoleRedis(appId, appUserId);
		if (ajaxMessage.getCode().equals("1")) {
			WebUtils.writeJson(response, ajaxMessage.getMessage());
		} else {
			map.put("status", ajaxMessage.getCode());
			map.put("error_code", ajaxMessage.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}
}
