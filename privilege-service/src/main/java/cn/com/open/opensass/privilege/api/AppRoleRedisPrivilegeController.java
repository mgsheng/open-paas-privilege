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

import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
/**
 * redis AppRole 接口 
 * 获取数据：getAppRoleRedis?appId=
 * 更新数据：updateAppRoleRedis?appId=
 * 删除数据：delAppRoleRedis?appId=
 */
@Controller
@RequestMapping("/AppRole/")
public class AppRoleRedisPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(AppRoleRedisPrivilegeController.class);
	@Autowired
	PrivilegeRoleService privilegeRoleService;
	@RequestMapping("getAppRoleRedis")
	public void getAppRoleRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeRoleService.getAppRoleRedis(appId);
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
	@RequestMapping("delAppRoleRedis")
	public void delAppRoleRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeRoleService.delAppRoleRedis(appId);
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
	@RequestMapping("updateAppRoleRedis")
	public void updateAppRoleRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app role start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeRoleService.updateAppRoleRedis(appId);
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
