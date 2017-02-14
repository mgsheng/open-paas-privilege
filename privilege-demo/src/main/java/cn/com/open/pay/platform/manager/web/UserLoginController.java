package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
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


import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.AESUtils;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.LoadPopertiesFile;
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
	private Map<String, String> map = LoadPopertiesFile.loadProperties();
	
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
	@Value("#{properties['8']}")
	private String client_secret;
	@Value("#{properties['get-oauth-token-uri']}")
	private String getOauthTokenUrl;
	@Value("#{properties['user-oauth-login-uri']}")
	private String userLoginUrl;
	@Value("#{properties['appId']}")
	private String AppId;
	@Value("#{properties['user-modify-password-uri']}")
	private String userModifyPasswordUrl;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private static final String AccessTokenPrefix = RedisConstant.ACCESSTOKEN_CACHE;

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
		// 从缓存获取token
		String access_token = (String) redisClientTemplate.getObject(AppId + AccessTokenPrefix);
		String client_id = map.get(client_secret);
		// 是否用户密码验证成功 true为登陆成功
		Boolean flag = false;
		// 错误信息
		String errorCode = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		// 若缓存中没有token 获取token
		if (access_token == null) {
			parameters.put("client_id", client_id);
			parameters.put("client_secret", client_secret);
			parameters.put("scope", "read,write");
			parameters.put("grant_type", "client_credentials");
			String result = sendPost(getOauthTokenUrl, parameters);
			if (result != null && !("").equals(result)) {
				String aString = result.substring(0, 1);
				if (aString.equals("{")) {
					JSONObject object = JSONObject.fromObject(result);
					access_token = (String) object.get("access_token");
					if (access_token != null && !("").equals(access_token)) {
						redisClientTemplate.setObjectByTime("8" + AccessTokenPrefix, access_token, 43199);
					}
				}
			}
		}
		String userName = request.getParameter("username").trim();
		String passWord = request.getParameter("password").trim();
		String appId = request.getParameter("appId") == null||request.getParameter("appId") ==("") ? "8" : request.getParameter("appId").trim();
		// 密码aes加密
		try {
			passWord = AESUtils.encrypt(passWord, client_secret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = privilegeGetSignatureService.getOauthSignature("8", client_id, access_token);
		parameters.put("access_token", access_token);
		parameters.put("client_id", client_id);
		parameters.put("client_secret", client_secret);
		parameters.put("scope", "read,write");
		parameters.put("grant_type", "client_credentials");
		parameters.put("username", userName);
		parameters.put("password", passWord);
		// 用户登陆
		String result = sendPost(userLoginUrl, parameters);
		if (result != null && !("").equals(result)) {
			String aString = result.substring(0, 1);
			if (aString.equals("{")) {
				JSONObject object = JSONObject.fromObject(result);
				if ("1".equals(object.get("status"))) {
					String appUserId = null;
					String appID = null;
					JSONArray array = object.getJSONArray("infoList");
					String jsessionId = object.getString("jsessionId");
					if (array.size() > 0) {
						for (int i = 0; i <= array.size(); i++) {
							if (appId != null && !("").equals(appId)) {
								appID = (String) array.getJSONObject(i).get("appId");
								if (appId.equals(appID)) {
									appUserId = (String) array.getJSONObject(i).get("sourceId");
									flag = true;
									break;
								}
							}
						}
					}
					if (flag) {
						HttpSession session = request.getSession();
						Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
						map.put("appId", appId);
						map.put("appUserId", appUserId);
						result = sendPost(getUserPrivilegeUrl, map);
						if (result != null && !("").equals(result)) {
							JSONObject jasonObject = JSONObject.fromObject(result);
							if ("0".equals(jasonObject.get("status"))) {
								if (jasonObject.get("error_code").equals(10002)) {
									flag=false;
									errorCode = "该用户不存在";
								}
							}else {
								Map<String, Object> user = new HashMap<String, Object>();
								user.put("username", userName);
								user.put("appId", appID);
								user.put("appUserId", appUserId);
								session.setAttribute("user", user);
								Cookie cookie = new Cookie("jsessionId", jsessionId);
								cookie.setPath("/");
								response.addCookie(cookie);
							}
						} 
						
					} else {
						errorCode = "您没有该产品信息";
					}

				} else if ("0".equals(object.get("status"))) {
					errorCode = (String) object.get("errMsg");
				} else {
					errorCode = "您没有该产品信息";
				}
			}
		}

		parameters.clear();
		parameters.put("flag", flag);
		parameters.put("errorCode", errorCode);
		WebUtils.writeJsonToMap(response, parameters);
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
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		if (user != null) {
			String userName=(String) user.get("username");
			String appId = (String) user.get("appId");
			String appUserId = (String) user.get("appUserId");
			Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
			map.put("appId", appId);
			map.put("appUserId", appUserId);
			String result = sendPost(getUserPrivilegeUrl, map);
			Map<String, Object> menus = new HashMap<String, Object>();
			if (result != null && !("").equals(result)) {
				JSONObject jasonObject = JSONObject.fromObject(result);
				if ("0".equals(jasonObject.get("status"))) {
					menus.put("status", "0");
					menus.put("errMsg", jasonObject.get("errMsg"));
					model.addAttribute("menus", JSONObject.fromObject(menus));
				} else {
					String groupId = jasonObject.getString("groupId");
					user.put("groupId", groupId);
					JSONArray menu = jasonObject.getJSONArray("menuList");
					JSONArray resource = jasonObject.getJSONArray("resourceList");
					List<PrivilegeResource1> resourceList = JSONArray.toList(resource, PrivilegeResource1.class);
					List<PrivilegeMenu> menuList = JSONArray.toList(menu, PrivilegeMenu.class);
					//根据DisplayOrder排序
					java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
			            @Override
			            public int compare(PrivilegeMenu o1, PrivilegeMenu o2) {
			                return o1.getDisplayOrder()-o2.getDisplayOrder();
			            }
			        });
					// JSONArray
					List<TreeNode> nodes = convertTreeNodeList(menuList);
					JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList));
					System.err.println(jsonArr.toString());
					menus.put("menus", jsonArr);
					model.addAttribute("username", userName);
					model.addAttribute("appId", appId);
					model.addAttribute("menus", JSONObject.fromObject(menus));
				}
			} else {
				menus.put("status", "0");
				menus.put("errMsg", "没有相应菜单");
				model.addAttribute("username", userName);
				model.addAttribute("appId", appId);
				model.addAttribute("menus", JSONObject.fromObject(menus));
			}

			return "login/index";

		}
		return "/index";

	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "update")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String newPass = request.getParameter("newpass");
		Map<String, Object> parameters = new HashMap<String, Object>();
		Boolean boo=isNumeric(newPass);
		if (boo) {
			parameters.put("status", "2");
			parameters.put("errMsg", "密码格式不正确，不能为纯数字");
			WebUtils.writeJsonToMap(response, parameters);
			return;
		}else{
			int value=verifyPassWordSAT(newPass);
			if (value==1) {
				parameters.put("status", "2");
				parameters.put("errMsg", "密码格式不正确");
				WebUtils.writeJsonToMap(response, parameters);
				return;
			}
		}
		// 从缓存获取token
		String access_token = (String) redisClientTemplate.getObject(AppId + AccessTokenPrefix);
		String client_id = map.get(client_secret);
		// 是否用户密码验证成功 true为登陆成功
		Boolean flag = false;
		// 若缓存中没有token 获取token
		if (access_token == null) {
			parameters.put("client_id", client_id);
			parameters.put("client_secret", client_secret);
			parameters.put("scope", "read,write");
			parameters.put("grant_type", "client_credentials");
			String result = sendPost(getOauthTokenUrl, parameters);
			if (result != null && !("").equals(result)) {
				String aString = result.substring(0, 1);
				if (aString.equals("{")) {
					JSONObject object = JSONObject.fromObject(result);
					access_token = (String) object.get("access_token");
					if (access_token != null && !("").equals(access_token)) {
						redisClientTemplate.setObjectByTime(AppId + AccessTokenPrefix, access_token, 43199);
					}
				}
			}
			parameters.clear();
		}
		
		
		String oldPass = request.getParameter("oldpass");
		String userName = request.getParameter("userName");
		try {
			oldPass = AESUtils.encrypt(oldPass, client_secret);
			newPass = AESUtils.encrypt(newPass, client_secret);
			userName = java.net.URLEncoder.encode(userName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = privilegeGetSignatureService.getOauthSignature(AppId, client_id, access_token);
		parameters.put("access_toke", access_token);
		parameters.put("client_id", client_id);
		parameters.put("account", userName);
		parameters.put("old_pwd", oldPass);
		parameters.put("new_pwd", newPass);
		parameters.put("pwdtype", "MD5");
		String result=sendPost(userModifyPasswordUrl,parameters);
		if (result != null && !("").equals(result)) {
			String aString = result.substring(0, 1);
			if (aString.equals("{")) {
				JSONObject object = JSONObject.fromObject(result);
				String status=object.getString("status");
				if ("1".equals(status)) {
					parameters.clear();
					parameters.put("status", "1");
					WebUtils.writeJsonToMap(response, parameters);
				}else {
					String errorCode=object.getString("error_code");
					String errMsg=object.getString("errMsg");
					parameters.clear();
					parameters.put("status", "0");
					parameters.put("errorCode", errorCode);
					parameters.put("errMsg", errMsg);
					WebUtils.writeJsonToMap(response, parameters);
				}
			}
		}
		
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
			node.setId(String.valueOf(privilegeMenu.getMenuId()));
			node.setText(privilegeMenu.getMenuName());
			node.setPid(String.valueOf(privilegeMenu.getParentId()));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("displayOrder", privilegeMenu.getDisplayOrder());
			node.setAttributes(map);
		}
		return node;
	}

	// 构建菜单tree Json
	public JSONArray treeMenuList(List<PrivilegeMenu> menuList, List<PrivilegeResource1> resourceList,
			String parentId) {
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