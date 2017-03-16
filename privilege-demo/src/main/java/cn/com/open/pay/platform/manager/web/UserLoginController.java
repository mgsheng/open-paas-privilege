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
import cn.com.open.pay.platform.manager.privilege.model.OesFrequentlyUsedMenu;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesFrequentlyUsedMenuService;
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
	@Autowired
	private OesFrequentlyUsedMenuService oesFrequentlyUsedMenuService;

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
		String access_token = privilegeGetSignatureService.getToken();
		// 是否用户密码验证成功 true为登陆成功
		Boolean flag = false;
		// 错误信息
		String errorCode = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		String userName = request.getParameter("username").trim();
		String passWord = request.getParameter("password").trim();
		String appId = request.getParameter("appId") == null || request.getParameter("appId") == ("")
				? oesPrivilegeDev.getAppId() : request.getParameter("appId").trim();
		// 密码aes加密
		try {
			passWord = AESUtils.encrypt(passWord, oesPrivilegeDev.getClientSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(),
				oesPrivilegeDev.getClientId(), access_token);
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
				JSONObject jsonObject = JSONObject.fromObject(result);
				if ("0".equals(jsonObject.get("status"))) {
					menus.put("status", "0");
					menus.put("errMsg", jsonObject.get("errMsg"));
					model.addAttribute("menus", JSONObject.fromObject(menus));
				} else {
					// 用户组织机构id
					String groupId = jsonObject.getString("groupId");
					// 用户是否是管理员
					Boolean isManager = jsonObject.getBoolean("isManager");
					// 用户角色类型，1-普通用户，2-管理员，3-组织机构管理员
					int Type = jsonObject.getInt("Type");
					user.put("groupId", groupId);
					user.put("Type", Type);
					user.put("isManager", isManager);
					user.put("privilege", jsonObject);
					JSONArray menu = jsonObject.getJSONArray("menuList");
					JSONArray resource = jsonObject.getJSONArray("resourceList");
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
					JSONArray jsonArr = JSONArray.fromObject(buildTree(nodes));
					menus.put("menus", jsonArr);
					// 根据该用户查找用户所在组织机构logo
					OesGroup group = oesGroupService.findByCode(groupId);
					String logoUrl = oesPrivilegeDev.getLogoUrl();
					if (group != null) {
						if (group.getGroupLogo() != null && !("").equals(group.getGroupLogo())) {
							logoUrl = group.getGroupLogo();
							logoUrl=logoUrl.replaceFirst("I", "i");
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
		String access_token = privilegeGetSignatureService.getToken();
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
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(),
				oesPrivilegeDev.getClientId(), access_token);
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
		request.getSession().invalidate();
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
	 * 
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
					node.setState("closed");
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
						entry.getValue().setIsmodule("1");
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
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String appUserId = (String) user.get("appUserId");
		String appId = (String) user.get("appId");
		String menuId = request.getParameter("menuId");
		String menuName = request.getParameter("menuName");
		OesLatestVisit oesLatestVisit = new OesLatestVisit();
		oesLatestVisit.setMenuId(menuId);
		oesLatestVisit.setMenuName(menuName);
		oesLatestVisit.setUserId(appUserId);
		oesLatestVisitService.saveOesLatestVisit(oesLatestVisit);
		oesLatestVisitService.updateUserLastVisitRedis(appUserId, appId);

	}

	/**
	 * 用户菜单tree
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------tree      start------------------------------------");
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		JSONArray jsonArr = null;
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
			map.put("status", "1");
			map.put("tree", jsonArr);
			WebUtils.writeJsonToMap(response, map);
			return;
		}
		map.put("appId", appId);
		map.put("appUserId", appUserId);
		// 获取应用菜单缓存
		String reslut = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), map);
		if (reslut != null && !("").equals(reslut)) {
			JSONObject obj = JSONObject.fromObject(reslut);
			JSONArray menuArray = (JSONArray) obj.get("menuList");
			if (menuArray.size() > 0) {
				List<PrivilegeMenu> menuList = JSONArray.toList(menuArray, PrivilegeMenu.class);
				JSONArray resourceArray = (JSONArray) obj.get("resourceList");
				// 将json对象转换为java对象
				List<PrivilegeResource1> resourceList = JSONArray.toList(resourceArray, PrivilegeResource1.class);
				// 根据菜单的displayOrder排序，由小到大
				java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
					@Override
					public int compare(PrivilegeMenu menu1, PrivilegeMenu menu2) {
						return menu1.getDisplayOrder() - menu2.getDisplayOrder();
					}
				});
				List<TreeNode> nodes = convertTreeNodeList(menuList);
				jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList));
				map.put("status", "1");
				map.put("tree", jsonArr);
			} else {
				map.put("status", "0");
				map.put("tree", new JSONArray());
			}
		}
		WebUtils.writeJsonToMap(response, map);
	}
	protected List<TreeNode> buildTree(List<TreeNode> treeNodes){
		List<TreeNode> results = new ArrayList<TreeNode>();
		for (TreeNode treeNode : treeNodes) {
			if (treeNode.getPid().equals("0")) {
				for (TreeNode childrenTreeNode : treeNodes) {
					if (treeNode.getId().equals(childrenTreeNode.getPid())) {
						List<TreeNode> children = treeNode.getChildren();
						if (children==null) {
							children=new ArrayList<TreeNode>();
							treeNode.setChildren(children);
						}
						children.add(childrenTreeNode);
					}
				}
				results.add(treeNode);
			}
			
		}
		return results;
	}
	/**
	 * 跳转首页
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "getHomePage")
	public String getHomePage(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String appUserId = (String) user.get("appUserId");
		String appId = (String) user.get("appId");
		//获取最近访问菜单
		List<Map<String, Object>> latestVisitRes = oesLatestVisitService.getUserLastVisitRedis(appUserId,
			appId);
		//获取常用菜单
		List<Map<String, Object>> frequentlyUsedRes = oesFrequentlyUsedMenuService
				.getUserFrequentlyMenuRedis(appUserId, appId);
		Map<String, Object> menu = new HashMap<String, Object>();
		menu.put("latestVisit", latestVisitRes);
		menu.put("frequentlyUsedMenu", frequentlyUsedRes);
		model.addAttribute("menus", JSONObject.fromObject(menu));
		model.addAttribute("appId", appId);
		return "login/homePage";
	}
	/**
	 * 跳转常用菜单管理
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "index")
	public String stats(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------index      start------------------------------------");
		String appId = request.getParameter("appId");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String appUserId = (String) user.get("appUserId");
		model.addAttribute("appId", appId);
		model.addAttribute("appUserId", appUserId);
		return "user/index";
	}
	/**
	 * 获取子菜单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getMenu")
	public String getMenu(HttpServletRequest request, HttpServletResponse response,Model model) {
		String menuId=request.getParameter("menuId").trim();
		Map<String, Object> user=(Map<String, Object>) request.getSession().getAttribute("user");
		JSONObject object=(JSONObject) user.get("privilege");
		List<PrivilegeResource1> resourceList=JSONArray.toList(object.getJSONArray("resourceList"), PrivilegeResource1.class);
		List<PrivilegeMenu> menuList=JSONArray.toList(object.getJSONArray("menuList"),PrivilegeMenu.class);
		//根据菜单的dislayOrder排序
		java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
			@Override
			public int compare(PrivilegeMenu o1, PrivilegeMenu o2) {
				return o1.getDisplayOrder() - o2.getDisplayOrder();
			}
		});
		Map<String, PrivilegeResource1> aidMap = new LinkedHashMap<String, PrivilegeResource1>();
		for (PrivilegeResource1 resource1 : resourceList) {
			aidMap.put(resource1.getMenuId(), resource1);
		}
		List<TreeNode> treeNodes=convertTreeNodeList(menuList);
		List<TreeNode> result=new ArrayList<TreeNode>();
		//查找子菜单
		for (TreeNode node : treeNodes) {
			if (menuId.equals(node.getPid())) {
				for (TreeNode treeNode : treeNodes) {
					if (node.getId().equals(treeNode.getPid())) {
						List<TreeNode> children = node.getChildren();
						if (children==null) {
							children=new ArrayList<TreeNode>();
							node.setChildren(children);
						}
						PrivilegeResource1 res= aidMap.get(treeNode.getId());
						if (res!=null) {
							treeNode.setId(res.getResourceId());
							Map<String, Object> map=new HashMap<String,Object>();
							map.put("baseUrl", res.getBaseUrl());
							map.put("menuRule", treeNode.getAttributes().get("menuRule"));
							treeNode.setAttributes(map);
							children.add(treeNode);
						}
					}
				}
				if (node.getChildren()==null||node.getChildren().size()==0) {
					PrivilegeResource1 res= aidMap.get(node.getId());
					if (res!=null) {
						node.setId(res.getResourceId());
						Map<String, Object> map=new HashMap<String,Object>();
						map.put("menuRule", node.getAttributes().get("menuRule"));
						map.put("baseUrl", res.getBaseUrl());
						node.setAttributes(map);
					}
				}
				result.add(node);
			}
		}
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("menus", JSONArray.fromObject(result));
		model.addAttribute("menu", JSONObject.fromObject(map));
		model.addAttribute("appId", user.get("appId"));
		return "login/menu";
	}
	/**
	 * 保存常用菜单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "saveMenu")
	public void saveMenu(HttpServletRequest request, HttpServletResponse response) {
		String appUserId = request.getParameter("appUserId");
		String appId = request.getParameter("appId");
		String resId = request.getParameter("resId");
		// 是否有常用访问菜单
		OesFrequentlyUsedMenu oesFrequentlyUsedMenu = oesFrequentlyUsedMenuService
				.getOesFrequentlyUsedMenuByUserId(appUserId);
		Boolean boo = false;
		// 如果没有，保存 如果有 更新
		if (oesFrequentlyUsedMenu != null) {
			oesFrequentlyUsedMenu.setMenuId(resId);
			boo = oesFrequentlyUsedMenuService.updateOesFrequentlyUsedMenuByUserId(oesFrequentlyUsedMenu);
		} else {
			oesFrequentlyUsedMenu = new OesFrequentlyUsedMenu();
			oesFrequentlyUsedMenu.setMenuId(resId);
			oesFrequentlyUsedMenu.setUserId(appUserId);
			boo = oesFrequentlyUsedMenuService.saveOesFrequentlyUsedMenu(oesFrequentlyUsedMenu);
		}
		// 更新常用访问菜单缓存
		if (boo) {
			oesFrequentlyUsedMenuService.updateUserFrequentlyMenuRedis(appUserId, appId);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", boo);
		WebUtils.writeJsonToMap(response, map);
	}

	/**
	 * 查询用户常用菜单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getFrequentlyMenu")
	public void getFrequentlyMenu(HttpServletRequest request, HttpServletResponse response) {
		String appUserId = request.getParameter("appUserId");
		OesFrequentlyUsedMenu oesFrequentlyUsedMenu = oesFrequentlyUsedMenuService
				.getOesFrequentlyUsedMenuByUserId(appUserId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (oesFrequentlyUsedMenu != null) {
			String[] menuIds = oesFrequentlyUsedMenu.getMenuId().split(",");
			map.put("resourceId", menuIds);
		} else {
			map.put("resourceId", null);
		}
		WebUtils.writeJsonToMap(response, map);
	}

}