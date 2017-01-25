package cn.com.open.opensass.privilege.api;

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

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
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
import net.sf.json.JSONObject;

/**
 * 用户权限认证接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/userRole/")
public class VerifyUserPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(VerifyUserPrivilegeController.class);
	private static final String prifix = RedisConstant.USERPRIVILEGES_CACHE;
	private static final String jsonKeyName = "urlList";
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private PrivilegeUrlService privilegeUrlService;
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;

	@RequestMapping("verifyUserPrivilege")
	public void verifyUserPrivilege(HttpServletRequest request, HttpServletResponse response) {
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
		Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
		if (!f) {
			WebUtils.paraMandaChkAndReturn(10001, response, "认证失败");
			return;
		}
		Boolean states = false;
		Boolean isManger = false;
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		log.info("getDataPrivilege用户数据，appid=" + appId + ",用户Id=" + appUserId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == privilegeUser) {
			states = false;
			map.put("status", "0");
			map.put("error_code", "10001");
			writeErrorJson(response, map);
			return;
		}
		states = redisDao.existUrlRedis(prifix, jsonKeyName, optUrl, appId, appUserId);
		if (!states) {
			List<PrivilegeRole> roles = privilegeRoleService.getRoleListByUserIdAndAppId(appUserId, appId);
			for (PrivilegeRole role : roles) {
				if (role.getRoleType() != null) {
					if (role.getRoleType() == 2) {
						isManger = true;
					}
				}
			}
			PrivilegeUrl url = null;
			if (isManger) {
				url = privilegeUrlService.getAllPrivilegeUrl(appId);
			} else {
				url = privilegeUrlService.getPrivilegeUrl(appId, appUserId, privilegeUser);
			}
			String json = url.getPrivilegeUrl();
			JSONObject object = JSONObject.fromObject(json);
			Map Jsonmap = (Map) object;
			List<String> urlList = (List<String>) Jsonmap.get("urlList");
			for (String s : urlList) {
				if (optUrl.indexOf(s) > -1) {
					states = true;
				}
			}
			privilegeUrlService.getRedisUrl(appId, appUserId);
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
		return;

	}
}
