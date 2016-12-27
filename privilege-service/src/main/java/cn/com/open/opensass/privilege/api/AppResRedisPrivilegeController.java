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

import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
/**
 * redis AppRes 接口 
 * 获取数据：getAppResRedis?appId=
 * 更新数据：updateAppResRedis?appId=
 * 删除数据：delAppResRedis?appId=
 */
@Controller
@RequestMapping("/AppRes/")
public class AppResRedisPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(AppResRedisPrivilegeController.class);
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@RequestMapping("getAppResRedis")
	public void getAppResRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app res start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeResourceService.getAppResRedis(appId);
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
	@RequestMapping("delAppResRedis")
	public void delAppResRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app res start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeResourceService.delAppResRedis(appId);
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
	@RequestMapping("updateAppResRedis")
	public void updateAppResRedisPrivilege(HttpServletRequest request,HttpServletResponse response){
		String appId=request.getParameter("appId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================redis app res start======================");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage message=privilegeResourceService.updateAppResRedis(appId);
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
