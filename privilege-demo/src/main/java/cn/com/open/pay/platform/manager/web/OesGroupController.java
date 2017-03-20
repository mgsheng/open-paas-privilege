package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.OesUser;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;
import cn.com.open.pay.platform.manager.privilege.service.OesUserService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.AESUtils;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

/**
 * 组织机构管理
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/oesGroup/")
public class OesGroupController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(OesGroupController.class);
	@Autowired
	private OesGroupService oesGroupService;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private OesUserService oesUserService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private static final String AccessTokenPrefix = RedisConstant.ACCESSTOKEN_CACHE;

	/**
	 * 跳转到机构管理页面
	 * 
	 * @return Json
	 */
	@RequestMapping("index")
	public String groupIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------group manager start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "privilege/group/index";
	}

	/**
	 * 获取应用下机构列表
	 * 
	 * @return Json
	 */
	@RequestMapping("getGroupList")
	public void getGroupMessage(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------getGroupMessage start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		// 角色类型 1-普通角色，2-系统管理员，3-组织机构管理员
		int Type = (int) user.get("Type");
		String groupCode = null;
		if (Type == 2) {
			groupCode = request.getParameter("groupCode");
		} else {
			groupCode = (String) user.get("groupId");
		}
		String groupName = request.getParameter("groupName");
		// 当前第几页
		String page = request.getParameter("page");
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int startRow = (currentPage - 1) * pageSize;
		List<OesGroup> oesGroup = oesGroupService.findByPage(groupName, groupCode, startRow, pageSize);
		int count = oesGroupService.findCount(groupName, groupCode);
		JSONArray jsonArr = JSONArray.fromObject(oesGroup);
		JSONObject jsonObjArr = new JSONObject();
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
		WebUtils.writeJson(response, jsonObjArr);
		return;
	}

	/**
	 * 添加机构
	 * 
	 * @return Json
	 */
	@RequestMapping("addGroup")
	public void addGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------addGroup start------------------------------------");
		String groupCode = request.getParameter("groupCode");
		String groupTypeName = request.getParameter("groupTypeName");
		String groupName = request.getParameter("groupName");
		String groupType = request.getParameter("groupType");
		String result;
		OesGroup group = oesGroupService.findByCode(groupCode);
		if (group == null) {
			OesGroup g = new OesGroup();
			g.setGroupCode(groupCode);
			g.setGroupName(groupName);
			g.setGroupTypeName(groupTypeName);
			g.setGroupType(groupType);
			Boolean f = oesGroupService.saveGroup(g);
			if (f) {
				result = "0";// add success
			} else {
				result = "1";// add field
			}
		} else {
			result = "2";// group exist
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		JSONObject json = JSONObject.fromObject(map);
		WebUtils.writeJson(response, json);
		return;
	}

	/**
	 * 跳转到机构授权资源页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "toRes")
	public String toRes(HttpServletRequest request, HttpServletResponse response, Model model)
			throws UnsupportedEncodingException {
		log.info("-------------------------toRes       start------------------------------------");
		String groupCode = request.getParameter("groupCode");
		String groupName = request.getParameter("groupName");
		String appId = request.getParameter("appId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("groupId", groupCode);
		map.put("roleType", "2");
		map.put("start", "0");
		map.put("limit", "5");
		String result = sendPost(oesPrivilegeDev.getQueryRoleUrl(), map);
		JSONObject object = JSONObject.fromObject(result);
		int count = object.getInt("total");
		if (count > 0) {
			List<Map<String, Object>> roleList = (List<Map<String, Object>>) object.get("roleList");
			model.addAttribute("roleId", roleList.get(0).get("privilegeRoleId"));
		}
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("groupName", groupName);
		model.addAttribute("appId", appId);
		return "privilege/group/authorizeRes";
	}

	/**
	 * 获取机构已授权资源
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "getRes")
	public void getRes(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------getRes       start------------------------------------");
		String groupCode = request.getParameter("groupCode");
		String appId = request.getParameter("appId");
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("groupId", groupCode);
		Signature.put("appId", appId);
		Signature.put("start", 0);
		Signature.put("limit", 10);
		String reslut = sendPost(oesPrivilegeDev.getGroupPrivilegeQueryUrl(), Signature);
		String[] resourceIds = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				// 机构资源表中 资源
				List<Map<String, Object>> gList = JSONArray.fromObject(jsonObject.get("groupList"));
				String privilegeResId = (String) (gList.get(0).equals("null") ? ""
						: gList.get(0).get("groupPrivilege"));
				if (!("").equals(privilegeResId)) {
					resourceIds = privilegeResId.split(",");
				}
			} 
		}

		Map<String, Object> aMap = new HashMap<String, Object>();
		aMap.put("resourceIds", resourceIds);
		WebUtils.writeJson(response, JSONObject.fromObject(aMap));
		return;
	}

	/**
	 * 授权机构资源
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("authorizeRes")
	public void authorizeRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------authorizeRes start------------------------------------");
		String groupCode = request.getParameter("groupCode");// 对应privilege_group_resource中的groupId
		String appId = request.getParameter("appId");
		String addIds = request.getParameter("addIds");
		String delIds = request.getParameter("delIds");
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("groupId", groupCode);
		String result1 = "";
		String result2 = "";
		if (addIds != null && !("").equals(addIds)) {
			Signature.put("groupPrivilege", addIds);
			Signature.put("method", "0");
			result1 = sendPost(oesPrivilegeDev.getGroupPrivilegeModifyUrl(), Signature);
		}
		if (delIds != null && !("").equals(delIds)) {
			Signature.put("groupPrivilege", delIds);
			Signature.put("method", "1");
			result2 = sendPost(oesPrivilegeDev.getGroupPrivilegeModifyUrl(), Signature);
		}
		if ((addIds == null || ("").equals(addIds)) && (delIds == null || ("").equals(delIds))) {
			Signature.put("method", "0");
		}
		JSONObject job1 = null;
		JSONObject job2 = null;
		Map<String, Object> m = new HashMap<String, Object>();
		if (result1 == null || ("").equals(result1)) {
			result1 = "{'status':'1'}";
		}
		job1 = JSONObject.fromObject(result1);
		if (result2 == null || ("").equals(result2)) {
			result2 = "{'status':'1'}";
		}
		job2 = JSONObject.fromObject(result2);
		if (("1").equals(job1.get("status")) && ("1").equals(job2.get("status"))) {
			boo = true;
		}
		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
		return;
	}

	/**
	 * 查询组织机构
	 * 
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value = "findGroup")
	public void findGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------findGroup         start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String groupId = user.get("groupId").equals("null") ? null : (String) user.get("groupId");
		// 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
		int Type = (int) user.get("Type");
		List<OesGroup> groupList = null;
		// 如果当前用户为系统管理员，显示所有组织结构，否则显示当前用户所在的组织机构
		if (Type == 2) {
			groupList = oesGroupService.findAll();
		} else {
			groupList = new ArrayList<OesGroup>();
			OesGroup group = oesGroupService.findByCode(groupId);
			groupList.add(group);
		}
		JSONArray jsonArr = JSONArray.fromObject(groupList);
		WebUtils.writeJson(response, jsonArr);
		return;
	}

	/**
	 * 添加组织机构管理员角色
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "addRole")
	public void addRole(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------add         start------------------------------------");
		String appId = request.getParameter("appId");
		String roleName = request.getParameter("roleName");
		roleName = java.net.URLEncoder.encode(roleName, "UTF-8");
		String groupName = request.getParameter("groupName");
		String roleType = request.getParameter("roleType");
		String groupId = request.getParameter("groupId");
		groupName = java.net.URLEncoder.encode(groupName, "UTF-8");
		String status = request.getParameter("status");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("roleName", roleName);
		map.put("status", status);
		map.put("groupId", groupId);
		map.put("groupName", groupName);
		map.put("roleType", roleType);
		map.put("parentRoleId", "");
		map.put("createUser", "");
		map.put("createUserId", "");
		String s = sendPost(oesPrivilegeDev.getRoleAddUrl(), map);
		JSONObject job = JSONObject.fromObject(s);
		WebUtils.writeErrorJson(response, job);
	}

	/**
	 * 注册组织机构管理员
	 * 
	 * @return 返回到前端json数据
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "addGroupAdministrator")
	public void CreateGroupAdministrator(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		OesUser oesUser = new OesUser();
		String appId = request.getParameter("appId");
		String groupId = request.getParameter("groupId");
		String roleId = request.getParameter("roleId");
		String appUserName = request.getParameter("appUserName");
		oesUser.setGroupId(groupId);
		oesUser.setUserName(appUserName);
		appUserName = java.net.URLEncoder.encode(appUserName, "UTF-8");
		String passWord = request.getParameter("passWord");
		try {
			passWord = AESUtils.encrypt(passWord, oesPrivilegeDev.getClientSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 从缓存中获取token
		String access_token = (String) redisClientTemplate.getObject(oesPrivilegeDev.getAppId() + AccessTokenPrefix);
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
						redisClientTemplate.setObjectByTime(oesPrivilegeDev.getAppId() + AccessTokenPrefix,
								access_token, 40000);
					}
				}
			}
		}
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(),
				oesPrivilegeDev.getClientId(), access_token);
		parameters.put("grant_type", oesPrivilegeDev.getGrantType());
		parameters.put("scope", "read,write");
		parameters.put("username", appUserName);
		parameters.put("client_id", oesPrivilegeDev.getClientId());
		parameters.put("isValidate", 1);
		parameters.put("password", passWord);
		String appUserId = UUID.randomUUID().toString().replaceAll("-", "");
		oesUser.setUserId(appUserId);
		parameters.put("source_id", appUserId);
		Boolean boo = false;// 是否注册成功标识
		String result = sendPost(oesPrivilegeDev.getUserCenterRegUrl(), parameters);
		if (result != null && !("").equals(result)) {
			String aString = result.substring(0, 1);
			if (aString.equals("{")) {
				JSONObject object = JSONObject.fromObject(result);
				if ("1".equals(object.get("status"))) {
					boo = true;
				} else {
					WebUtils.writeJson(response, result);
					return;
				}
			}
		}
		if (boo) {// 用户中心注册成功后，添加权限用户
			parameters = privilegeGetSignatureService.getSignature(appId);
			parameters.put("appId", appId);
			parameters.put("appUserId", appUserId);
			parameters.put("appUserName", appUserName);
			parameters.put("groupId", groupId);
			parameters.put("privilegeRoleId", roleId);
			result = sendPost(oesPrivilegeDev.getAddPrivilegeUserUrl(), parameters);
			JSONObject object = JSONObject.fromObject(result);
			if (("1").equals(object.getString("status"))) {
				boo = oesUserService.saveUser(oesUser);
				parameters.clear();
				if (boo) {
					parameters.put("status", "1");
				} else {
					parameters.put("status", "0");
					parameters.put("errMsg", "保存失败");
				}
			} else {
				parameters.put("status", "0");
				parameters.put("errMsg", "保存失败");
			}
			WebUtils.writeJsonToMap(response, parameters);
			return;
		}
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(oesPrivilegeDev.getAppMenuRedisUrl(), map);
		JSONObject obj = JSONObject.fromObject(s);// 将json字符串转换为json对象
		JSONArray objArray = JSONArray.fromObject(obj.get("menuList"));
		// 将json对象转换为java对象
		List<PrivilegeMenu> menuList = JSONArray.toList(objArray, PrivilegeMenu.class);
		String s1 = sendPost(oesPrivilegeDev.getAppResRedisUrl(), map);
		JSONObject obj1 = new JSONObject().fromObject(s1);// 将json字符串转换为json对象
		JSONArray obj1Array = JSONArray.fromObject(obj1.get("resourceList"));
		String operation = sendPost(oesPrivilegeDev.getAllOperationUrl(), map);
		obj = JSONObject.fromObject(operation);
		// 将json对象转换为java对象
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array, PrivilegeResource1.class);
		List<TreeNode> nodes = convertTreeNodeList(menuList);
		JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList));
		WebUtils.writeJson(response, jsonArr);
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

	/**
	 * @param departments
	 * @return
	 */
	private TreeNode convertTreeNode(PrivilegeMenu privilegeMenu) {
		TreeNode node = null;
		if (privilegeMenu != null) {
			node = new TreeNode();
			node.setId("m" + String.valueOf(privilegeMenu.getMenuId()));// 菜单ID
			node.setIsmodule("0");
			node.setChecked(false);
			node.setText(privilegeMenu.getMenuName());// 菜单名称
			node.setTarget("");
			node.setResource("");
			node.setPid("m" + String.valueOf(privilegeMenu.getParentId()));// 父级菜单ID
			Map<String, Object> map = new HashMap<String, Object>();
			node.setAttributes(map);
		}
		return node;
	}

	/**
	 * 添加时构建树
	 * 
	 * @param treeNodes
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
					if ((menuId).equals("m" + res.getMenuId())) {
						entry.getValue().setId("r" + res.getResourceId());
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
}
