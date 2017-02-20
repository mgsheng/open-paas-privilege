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

import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
/**
 * redis AppMenu 接口 
 * 获取数据：getAppMenuRedis?appId=
 * 更新数据：updateAppMenuRedis?appId=
 * 删除数据：delAppMenuRedis?appId=
 */
@Controller
@RequestMapping("/AppMenu/")
public class AppMenuRedisPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(AppMenuRedisPrivilegeController.class);
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@RequestMapping("getAppMenuRedis")
	public void getAppMenuRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app menu start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeMenuService.getAppMenuRedis(appId);
		if (message.getCode().equals("1")) {
			WebUtils.writeJson(response, message.getMessage());
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}
	@RequestMapping("delAppMenuRedis")
	public void delAppMenuRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app menu start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeMenuService.delAppMenuRedis(appId);
		if (message.getCode().equals("1")) {
			WebUtils.writeJson(response, message.getMessage());
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}
	@RequestMapping("updateAppMenuRedis")
	public void updateAppMenuRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app menu start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeMenuService.updateAppMenuRedis(appId);
		if (message.getCode().equals("1")) {
			WebUtils.writeJson(response, message.getMessage());
		} else {
			map.put("status", message.getCode());
			map.put("error_code", message.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}

}
