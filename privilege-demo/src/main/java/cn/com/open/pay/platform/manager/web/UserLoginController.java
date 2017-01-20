package cn.com.open.pay.platform.manager.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.dev.PayManagerDev;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.Manager;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.service.ManagerService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private PayManagerDev payManagerDev;
	@Autowired
	private PrivilegeRoleDetailsService privilegeRoleDetailsService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Value("#{properties['get-privilege-user-uri']}")
	private String getUserPrivilegeUrl;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['getSignature-uri']}")
	private String getSignatureUrl;

	/**
	 * 登录验证
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 */
	@RequestMapping("loginVerify")
	public void verify(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		log.info("-----------------------login start----------------");
		Boolean flag = true;
		String errorCode = "ok";
		User user = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("flag", flag);
		map.put("errorCode", errorCode);
		WebUtils.writeJsonToMap(response, map);
	}

	/**
	 * 登录跳转
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("appUserId", appUserId);
		HttpSession session = request.getSession();
		session.setAttribute("user", appUserId);
		String result = sendPost(getUserPrivilegeUrl, map);
		Map<String, Object> menus = new HashMap<String, Object>();
		if (result != null && !("").equals(result)) {
			JSONObject jasonObject = JSONObject.fromObject(result);
			Map JsonMap = (Map) jasonObject;
			if ("0".equals(JsonMap.get("status"))) {
				menus.put("status", "0");
				menus.put("errMsg", JsonMap.get("errMsg"));
				model.addAttribute("menus", JSONObject.fromObject(menus));
			} else {
				JSONArray menu=jasonObject.getJSONArray("menuList");
				JSONArray resource=jasonObject.getJSONArray("resourceList");
				List<PrivilegeResource1> resourceList = JSONArray.toList(resource, PrivilegeResource1.class);
				List<PrivilegeMenu> menuList = JSONArray.toList(menu, PrivilegeMenu.class);
				JSONArray jsonArray2=treeMenuList(menuList,resourceList,"0");
				menus.put("menus", jsonArray2);
				model.addAttribute("appId", appId);
				model.addAttribute("menus", JSONObject.fromObject(menus));
			}
		} else {
			menus.put("status", "0");
			menus.put("errMsg", "没有相应菜单");
			model.addAttribute("appId", appId);
			model.addAttribute("menus", JSONObject.fromObject(menus));
		}

		return "login/index";

	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "update")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("newpass");
		String username = request.getParameter("userName");
		User user = null;
		user = userService.findByUsername(username);
		// 刷新盐值，重新加密
		user.buildPasswordSalt();
		user.setPlanPassword(password);
		user.setUpdatePwdTime(new Date());
		userService.updateUser(user);
	}
	//构建菜单tree Json
	public JSONArray treeMenuList(List<PrivilegeMenu> menuList,List<PrivilegeResource1> resourceList, String parentId) {
		JSONArray childMenu = new JSONArray();
		for (PrivilegeMenu menu : menuList) {
			JSONObject jsonMenu = JSONObject.fromObject(menu);
			String menuId = menu.getMenuId();
			String pid = menu.getParentId();
			if (parentId.equals(pid)) {
				for (PrivilegeResource1 resource : resourceList) {
					if (resource.getMenuId().equals(menuId)) {
							jsonMenu.put("url", resource.getBaseUrl());
					}
				}
				JSONArray childrenNode = treeMenuList(menuList, resourceList, menuId);
				jsonMenu.put("menus", childrenNode);
				childMenu.add(jsonMenu);
			}
		}
		return childMenu;
	}
}