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

import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 * @author TF
 *获取数据 getRole 
 */
@Controller
@RequestMapping("role")
public class PrivilegeRoleController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(PrivilegeRoleController.class);
	@Autowired
	private PrivilegeRoleService service;

	@RequestMapping("getRole")
	public void getRole(HttpServletRequest request, HttpServletResponse response) {
		log.info("----getRole start---");
		String appId = request.getParameter("appId");
		String groupId = request.getParameter("groupId");
		if (!paraMandatoryCheck(Arrays.asList(appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		List<PrivilegeRole> roleList=service.getRoleListByAppIdAndGroupId(appId, groupId);
		Map<String, Object> roleMap = new HashMap<String, Object>();
		roleMap.put("roleList", roleList);
		writeSuccessJson(response, roleMap);
		return;
	}
}
