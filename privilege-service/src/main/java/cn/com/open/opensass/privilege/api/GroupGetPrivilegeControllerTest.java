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

import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * redis groupTest 接口 
 * 获取数据：getGroupPrivilegeRedis?appId=&groupId=
 * 更新数据：updateGroupPrivilegeRedis?appId=&groupId=
 * 删除数据：deleteGroupPrivilegeRedis?appId=&groupId=
 */
@Controller
@RequestMapping("/groupTest/")
public class GroupGetPrivilegeControllerTest extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(GroupGetPrivilegeControllerTest.class);
	@Autowired
	private PrivilegeGroupService privilegeGroupService;

	/**
	 * 组织机构权限缓存接口
	 * 
	 * @return Json
	 */

	@RequestMapping("getGroupPrivilegeRedis")
	public void getGroupPrivilege(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String groupId = request.getParameter("groupId").trim();
		String appId = request.getParameter("appId").trim();
		log.info("====================query start======================");
		if (!paraMandatoryCheck(Arrays.asList(groupId, appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		
		PrivilegeAjaxMessage message=privilegeGroupService.findGroupPrivilege(groupId, appId);
		
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
	@RequestMapping("deleteGroupPrivilegeRedis")
	public void delGroupPrivilege(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String groupId = request.getParameter("groupId").trim();
		String appId = request.getParameter("appId").trim();
		log.info("====================query start======================");
		if (!paraMandatoryCheck(Arrays.asList(groupId, appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage ajaxMessage=privilegeGroupService.delGroupPrivilegeCache(groupId, appId);
		if (ajaxMessage.getCode().equals("1")) {
			System.err.println(ajaxMessage.getMessage());
			WebUtils.writeJson(response, ajaxMessage.getMessage());
		} else {
			map.put("status", ajaxMessage.getCode());
			map.put("error_code", ajaxMessage.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
		
	}
	@RequestMapping("updateGroupPrivilegeRedis")
	public void updateGroupPrivilege(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String groupId = request.getParameter("groupId").trim();
		String appId = request.getParameter("appId").trim();
		log.info("====================query start======================");
		if (!paraMandatoryCheck(Arrays.asList(groupId, appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		PrivilegeAjaxMessage ajaxMessage=privilegeGroupService.updateGroupPrivilegeCache(groupId, appId);
		if (ajaxMessage.getCode().equals("1")) {
			System.err.println(ajaxMessage.getMessage());
			WebUtils.writeJson(response, ajaxMessage.getMessage());
		} else {
			map.put("status", ajaxMessage.getCode());
			map.put("error_code", ajaxMessage.getMessage());/* 数据不存在 */
			writeErrorJson(response, map);
		}
		return;
	}
	
}