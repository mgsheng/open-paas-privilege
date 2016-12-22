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
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
/**
 * redis menu 接口
 * 获取数据：getMenu?appId=&appUserId=
 */
@Controller
@RequestMapping("/userRole/")
public class UserRoleRedisPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserRoleRedisPrivilegeController.class);
	@Autowired
	private PrivilegeUserRedisService privilegeUserRedisService;
	

	/**
	 * 用户角色缓存接口
	 * 
	 */
	 @RequestMapping(value = "getUserRoleRedis", method = RequestMethod.GET)
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
			System.err.println(message.getMessage());
			WebUtils.writeJson(response, message.getMessage());
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;

	}
}
