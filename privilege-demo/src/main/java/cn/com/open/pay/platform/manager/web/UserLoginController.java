package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
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
		session.setAttribute("user", map);
		String result = sendPost(getUserPrivilegeUrl, map);
		Map<String, Object> menus = new HashMap<String, Object>();
		if (result != null && !("").equals(result)) {
			JSONObject jasonObject = JSONObject.fromObject(result);
			if ("0".equals(jasonObject.get("status"))) {
				menus.put("status", "0");
				menus.put("errMsg", jasonObject.get("errMsg"));
				model.addAttribute("menus", JSONObject.fromObject(menus));
			} else {
				JSONArray menu=jasonObject.getJSONArray("menuList");
				JSONArray resource=jasonObject.getJSONArray("resourceList");
				List<PrivilegeResource1> resourceList = JSONArray.toList(resource, PrivilegeResource1.class);
				List<PrivilegeMenu> menuList = JSONArray.toList(menu, PrivilegeMenu.class);
				//JSONArray jsonArray2=treeMenuList(menuList,resourceList,"0");
				List<TreeNode> nodes = convertTreeNodeList(menuList);
				JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList));
				menus.put("menus", jsonArr);
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
	private List<TreeNode> convertTreeNodeList(List<PrivilegeMenu> modules) {
		List<TreeNode> nodes = null;
		if (modules != null) {
			nodes = new ArrayList<TreeNode>();
			for (PrivilegeMenu menu : modules) {
				TreeNode node = convertTreeNode(menu);
				if (node != null) {
					nodes.add(node);
				}
			}
		}
		return nodes;
	}
	protected List<TreeNode> buildTree2(List<TreeNode> treeNodes, List<PrivilegeResource1> resourceList) {
		List<TreeNode> results = new ArrayList<TreeNode>();
		Map<String, TreeNode> aidMap = new LinkedHashMap<String, TreeNode>();
		for (TreeNode node : treeNodes) {
			aidMap.put(node.getId(), node);
		}
		treeNodes = null;
		Set<Entry<String, TreeNode>> entrySet = aidMap.entrySet();
		for (Entry<String, TreeNode> entry : entrySet) {
			String pid = entry.getValue().getPid();
			TreeNode node = aidMap.get(pid);
			if (node == null) {
				results.add(entry.getValue());
			} else {
				List<TreeNode> children = node.getChildren();
				if (children == null) {
					children = new ArrayList<TreeNode>();
					node.setChildren(children);
				}
				String menuId = entry.getValue().getId();
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				for (PrivilegeResource1 res : resourceList) {
					if ((menuId).equals(res.getMenuId())) {
						Map<String, Object> resourceMap = new HashMap<String, Object>();
						resourceMap.put("baseUrl", res.getBaseUrl());
						entry.getValue().setAttributes(resourceMap);
						entry.getValue().setId(res.getResourceId());
						entry.getValue().setText(res.getResourceName());
					}
				}
				
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}
	
	private TreeNode convertTreeNode(PrivilegeMenu privilegeMenu) {
		TreeNode node = null;
		if (privilegeMenu != null) {
			node = new TreeNode();
			node.setId(String.valueOf(privilegeMenu.getMenuId()));// 菜单ID
			node.setText(privilegeMenu.getMenuName());// 菜单名称
			node.setPid(String.valueOf(privilegeMenu.getParentId()));// 父级菜单ID
			Map<String, Object> map = new HashMap<String, Object>();
			node.setAttributes(map);
		}
		return node;
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