package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import net.sf.json.JSONObject;

/**
 * 测试组织机构权限查询接口
 */
@Controller
@RequestMapping("/groupTest/")
public class GroupGetPrivilegeControllerTest extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(GroupGetPrivilegeControllerTest.class);
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	/**
	 * 组织机构权限查询
	 * 
	 * @return Json
	 */

	@RequestMapping("getGroupPrivilegeTest")
	public void getGroupPrivilege(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String groupId = request.getParameter("groupId");
		String appId = request.getParameter("appId");
		log.info("====================query start======================");
		if (!paraMandatoryCheck(Arrays.asList(groupId, appId))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		try {
			// 查询组织机构缓存
			Map<String, Object> groupPrivilege = privilegeGroupService.findGroupPrivilege(groupId, appId);
			if (groupPrivilege != null && groupPrivilege.size() > 0) {
				writeSuccessJson(response, groupPrivilege);
				return;
			} else {
				map.put("status", "0");
				map.put("error_code", "10001");
				writeErrorJson(response, map);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}