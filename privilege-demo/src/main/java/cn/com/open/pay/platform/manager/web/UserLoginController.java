package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;
import cn.com.open.pay.platform.manager.privilege.service.OesLatestVisitService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.AESUtils;
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
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private OesLatestVisitService oesLatestVisitService;
	@Autowired
	private OesGroupService oesGroupService;
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
		log.info("-----------------------loginVerify start----------------");
		// 从缓存获取token
		String access_token = (String) redisClientTemplate.getObject(oesPrivilegeDev.getAppId() + AccessTokenPrefix);
		// 是否用户密码验证成功 true为登陆成功
		Boolean flag = false;
		// 错误信息
		String errorCode = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		// 若缓存中没有token 获取token
		if (access_token == null) {
			parameters.put("client_id", oesPrivilegeDev.getClientId());
			parameters.put("client_secret", oesPrivilegeDev.getClientSecret());
			parameters.put("scope", "read,write");
			parameters.put("grant_type", oesPrivilegeDev.getGrantType());
			String result = sendPost(oesPrivilegeDev.getOauthTokenUrl(), parameters);
			if (result != null && !("").equals(result)) {
				String aString = result.substring(0, 1);
				if (aString.equals("{")) {
					JSONObject object = JSONObject.fromObject(result);
					access_token = (String) object.get("access_token");
					if (access_token != null && !("").equals(access_token)) {
						redisClientTemplate.setObjectByTime(oesPrivilegeDev.getAppId() + AccessTokenPrefix, access_token, 43190);
					}
				}
			}
		}
		String userName = request.getParameter("username").trim();
		String passWord = request.getParameter("password").trim();
		String appId = request.getParameter("appId") == null || request.getParameter("appId") == ("") ? oesPrivilegeDev.getAppId()
				: request.getParameter("appId").trim();
		// 密码aes加密
		try {
			passWord = AESUtils.encrypt(passWord, oesPrivilegeDev.getClientSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(), oesPrivilegeDev.getClientId(), access_token);
		parameters.put("access_token", access_token);
		parameters.put("client_id", oesPrivilegeDev.getClientId());
		parameters.put("client_secret", oesPrivilegeDev.getClientSecret());
		parameters.put("scope", "read,write");
		parameters.put("grant_type", oesPrivilegeDev.getGrantType());
		parameters.put("username", userName);
		parameters.put("password", passWord);
		parameters.put("pwdtype", oesPrivilegeDev.getPwdType());
		// 用户登陆
		String result = sendPost(oesPrivilegeDev.getUserLoginUrl(), parameters);
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
					// 如果有该产品信息，调用查找用户接口，查看是否有对应的用户，若没有提示用户不存在，若存在用户则登陆
					if (flag) {
						HttpSession session = request.getSession();
						Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
						map.put("appId", appID);
						map.put("appUserId", appUserId);
						result = sendPost(oesPrivilegeDev.getFindUserUrl(), map);
						if (result != null && !("").equals(result)) {
							JSONObject jasonObject = JSONObject.fromObject(result);
							if ("0".equals(jasonObject.get("status"))) {
								flag = false;
								errorCode = "该用户不存在";
							} else {
								Map<String, Object> user = new HashMap<String, Object>();
								user.put("username", userName);
								user.put("appId", appID);
								user.put("appUserId", appUserId);
								user.put("jsessionId", jsessionId);
								session.setAttribute("user", user);
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
		log.info("-----------------------login start----------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		if (user != null) {
			String userName = (String) user.get("username");
			String appId = (String) user.get("appId");
			String appUserId = (String) user.get("appUserId");
			String jsessionId = (String) user.get("jsessionId");
			Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
			map.put("appId", appId);
			map.put("appUserId", appUserId);
			String result = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), map);
			Map<String, Object> menus = new HashMap<String, Object>();
			if (result != null && !("").equals(result)) {
				JSONObject jasonObject = JSONObject.fromObject(result);
				if ("0".equals(jasonObject.get("status"))) {
					menus.put("status", "0");
					menus.put("errMsg", jasonObject.get("errMsg"));
					model.addAttribute("menus", JSONObject.fromObject(menus));
				} else {
					String groupId = jasonObject.getString("groupId");
					Boolean isManager=jasonObject.getBoolean("isManager");
					user.put("groupId", groupId);
					user.put("isManager", isManager);
					JSONArray menu = jasonObject.getJSONArray("menuList");
					JSONArray resource = jasonObject.getJSONArray("resourceList");
					List<PrivilegeResource1> resourceList = JSONArray.toList(resource, PrivilegeResource1.class);
					List<PrivilegeMenu> menuList = JSONArray.toList(menu, PrivilegeMenu.class);
					// 根据DisplayOrder排序
					java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
						@Override
						public int compare(PrivilegeMenu o1, PrivilegeMenu o2) {
							return o1.getDisplayOrder() - o2.getDisplayOrder();
						}
					});
					// JSONArray
					List<TreeNode> nodes = convertTreeNodeList(menuList);
					JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList));
					List<Map<String, Object>> latestVisitRes=oesLatestVisitService.getUserLastVisitRedis(appUserId, appId);
					menus.put("menus", jsonArr);
					menus.put("latestVisit", latestVisitRes);
					//根据该用户查找用户所在组织机构logo
					OesGroup group=oesGroupService.findByCode(groupId);
					String logoUrl=oesPrivilegeDev.getLogoUrl();
					if (group!=null) {
						if (group.getGroupLogo()!=null&&!("").equals(group.getGroupLogo())) {
							logoUrl=group.getGroupLogo();
							logoUrl.replaceFirst("I", "i");
						}
					}
					model.addAttribute("logo", logoUrl);
					model.addAttribute("username", userName);
					model.addAttribute("appId", appId);
					model.addAttribute("jsessionId", jsessionId);
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
		log.info("-----------------------updatePassWord start----------------");
		String newPass = request.getParameter("newpass");
		Map<String, Object> parameters = new HashMap<String, Object>();
		Boolean boo = isNumeric(newPass);
		if (boo) {
			parameters.put("status", "2");
			parameters.put("errMsg", "密码格式不正确，不能为纯数字");
			WebUtils.writeJsonToMap(response, parameters);
			return;
		} else {
			int value = verifyPassWordSAT(newPass);
			if (value == 1) {
				parameters.put("status", "2");
				parameters.put("errMsg", "密码格式不正确");
				WebUtils.writeJsonToMap(response, parameters);
				return;
			}
		}
		// 从缓存获取token
		String access_token = (String) redisClientTemplate.getObject(oesPrivilegeDev.getAppId() + AccessTokenPrefix);
		// 若缓存中没有token 获取token
		if (access_token == null) {
			parameters.put("client_id", oesPrivilegeDev.getClientId());
			parameters.put("client_secret", oesPrivilegeDev.getClientSecret());
			parameters.put("scope", "read,write");
			parameters.put("grant_type", oesPrivilegeDev.getGrantType());
			String result = sendPost(oesPrivilegeDev.getOauthTokenUrl(), parameters);
			if (result != null && !("").equals(result)) {
				String aString = result.substring(0, 1);
				if (aString.equals("{")) {
					JSONObject object = JSONObject.fromObject(result);
					access_token = (String) object.get("access_token");
					if (access_token != null && !("").equals(access_token)) {
						redisClientTemplate.setObjectByTime(oesPrivilegeDev.getAppId() + AccessTokenPrefix, access_token, 43190);
					}
				}
			}
			parameters.clear();
		}
		String oldPass = request.getParameter("oldpass");
		String userName = request.getParameter("userName");
		// 密码AES加密
		try {
			oldPass = AESUtils.encrypt(oldPass, oesPrivilegeDev.getClientSecret());
			newPass = AESUtils.encrypt(newPass, oesPrivilegeDev.getClientSecret());
			userName = java.net.URLEncoder.encode(userName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(), oesPrivilegeDev.getClientId(), access_token);
		parameters.put("access_toke", access_token);
		parameters.put("client_id", oesPrivilegeDev.getClientId());
		parameters.put("account", userName);
		parameters.put("old_pwd", oldPass);
		parameters.put("new_pwd", newPass);
		parameters.put("pwdtype", oesPrivilegeDev.getPwdType());
		String result = sendPost(oesPrivilegeDev.getUserModifyPasswordUrl(), parameters);
		if (result != null && !("").equals(result)) {
			String aString = result.substring(0, 1);
			if (aString.equals("{")) {
				JSONObject object = JSONObject.fromObject(result);
				String status = object.getString("status");
				if ("1".equals(status)) {
					parameters.clear();
					parameters.put("status", "1");
				} else {
					String errorCode = object.getString("error_code");
					String errMsg = object.getString("errMsg");
					parameters.clear();
					parameters.put("status", "0");
					parameters.put("errorCode", errorCode);
					parameters.put("errMsg", errMsg);
				}
			} else {
				parameters.clear();
				parameters.put("status", "0");
				parameters.put("errMsg", "修改失败");
				
			}
		}
		WebUtils.writeJsonToMap(response, parameters);

	}
	/**
	 * 登出跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loginOut")
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {
		log.info("-----------------------loginOut start----------------");
		Map<String, Object> user=(Map<String, Object>) request.getSession().getAttribute("user");
		if (user!=null) {
			request.getSession().removeAttribute("user");
		}
		return "/index";
	}
	/**
	 * @param modules
	 * @return
	 */
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
	/**
	 * 构建tree
	 * @param treeNodes
	 * @param resourceLists
	 * @return
	 */
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
						resourceMap.put("menuRule", entry.getValue().getAttributes().get("menuRule"));
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
			map.put("menuRule", privilegeMenu.getMenuRule());
			node.setAttributes(map);
		}
		return node;
	}
	
	/**
	 * 保存最近访问菜单
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "saveLatestVisit")
	public void saveLatestVisit(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> user=(Map<String, Object>) request.getSession().getAttribute("user");
		String appUserId = (String) user.get("appUserId");
		String appId = (String) user.get("appId");
		String menuId = request.getParameter("menuId");
		String menuName = request.getParameter("menuName");
		//是否存放标识，如果前5个最近访问中有该菜单则不存放，相反则存放
		Boolean boo=true;
		List<OesLatestVisit> oesLatestVisits=oesLatestVisitService.getOesLastVisitByUserId(appUserId, 0, 5);
		for (OesLatestVisit oesLatestVisit : oesLatestVisits) {
			if (menuId.equals(oesLatestVisit.getMenuId())) {
				boo=false;
				break;
			}
		}
		if (boo) {
			OesLatestVisit oesLatestVisit=new OesLatestVisit();
			oesLatestVisit.setMenuId(menuId);
			oesLatestVisit.setMenuName(menuName);
			oesLatestVisit.setUserId(appUserId);
			boo=oesLatestVisitService.saveOesLatestVisit(oesLatestVisit);
			oesLatestVisitService.updateUserLastVisitRedis(appUserId,appId);
		}
		
	}
	
}