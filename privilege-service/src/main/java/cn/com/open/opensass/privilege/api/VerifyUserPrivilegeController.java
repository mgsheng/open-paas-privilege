package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUrlService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户权限认证接口
 */
@Controller
@RequestMapping("/userRole/")
public class VerifyUserPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory
			.getLogger(VerifyUserPrivilegeController.class);
	public static final String SIGN = RedisConstant.SIGN;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeUrlService privilegeUrlService;
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;

	@RequestMapping("verifyUserPrivilege")
	public void verifyUserPrivilege(HttpServletRequest request,
			HttpServletResponse response) {
		String appUserId = request.getParameter("appUserId").trim();
		String appId = request.getParameter("appId").trim();
		String optUrl = request.getParameter("optUrl").trim();
		log.info("===================verifyUserPrivilege start======================");
		if (!paraMandatoryCheck(Arrays.asList(appUserId, appId, optUrl))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}

		App app = (App) redisClient.getObject(RedisConstant.APP_INFO + appId);
		if (app == null) {
			app = appService.findById(Integer.parseInt(appId));
			redisClient.setObject(RedisConstant.APP_INFO + appId, app);
		}
		Boolean f = OauthSignatureValidateHandler.validateSignature(request,
				app);
		if (!f) {
			WebUtils.paraMandaChkAndReturn(10001, response, "认证失败");
			return;
		}
		// 权限是否认证成功 认证成功为true
		Boolean states = false;

		PrivilegeUser privilegeUser = privilegeUserService
				.findByAppIdAndUserId(appId, appUserId);
		log.info("getDataPrivilege用户数据，appid=" + appId + ",用户Id=" + appUserId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == privilegeUser) {
			// 若没有该用户 返回认证失败
			map.put("status", "0");
			map.put("error_code", "10001");
			writeErrorJson(response, map);
			return;
		}

		// 判断是否是管理员
		List<PrivilegeRole> roles = privilegeRoleService
				.getRoleListByUserIdAndAppId(appUserId, appId);
		for (PrivilegeRole role : roles) {
			if (role.getRoleType() != null) {
				if (role.getRoleType() == 2) {
					// 角色组织机构id为空，为系统管理员
					if (role.getGroupId() == null || role.getGroupId().isEmpty()) {
						map.put("status", "1");
						writeErrorJson(response, map);
						return;
					} 
				}

			}
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
		 StringBuilder redisUserAllPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
			redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
			redisUserAllPrivilegeKey.append(appId);
			redisUserAllPrivilegeKey.append(SIGN);
			redisUserAllPrivilegeKey.append(appUserId);
			if(redisClient.existKey(redisUserAllPrivilegeKey.toString()))
			{
				redisClient.del(redisUserAllPrivilegeKey.toString());
			}
		// 获取用户url缓存
		PrivilegeAjaxMessage message = privilegeUrlService.getRedisUrl(appId,
				appUserId);
		String redisString = message.getMessage();
		JSONObject object = JSONObject.fromObject(redisString);
		List<String> urlList = JSONArray.toList(object.getJSONArray("urlList"),
				String.class);
		// 验证是否有权限
		for (String url : urlList) {
			if (url.toLowerCase().contains(optUrl.toLowerCase())) {
				states = true;
			}
		}
		if (states) {
			map.put("status", "1");
		} else {
			map.put("status", "0");
			map.put("error_code", "10001");
		}

		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
	}
}
