package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.tools.AESUtils;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/managerUser/")
public class ManagerUserController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(ManagerUserController.class);

	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private OesGroupService oesGroupService;


	/**
	 * 跳转到用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "toRole")
	public String toRole(HttpServletRequest request, HttpServletResponse response, Model model)
			throws UnsupportedEncodingException {
		log.info("-------------------------toRole       start------------------------------------");
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		String userName = request.getParameter("userName");
		String groupId = request.getParameter("groupId");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		userName = (userName == null ? null : new String(userName.getBytes("iso-8859-1"), "utf-8"));
		model.addAttribute("id", id);
		model.addAttribute("userName", userName);
		model.addAttribute("appId", appId);
		model.addAttribute("groupId", groupId);
		return "show/authorizeRole";
	}

	/**
	 * 跳转到用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "toFunction")
	public String toFunction(HttpServletRequest request, HttpServletResponse response, Model model)
			throws UnsupportedEncodingException {
		log.info("-------------------------toFunction       start------------------------------------");
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		String userName = request.getParameter("userName");
		String groupId = request.getParameter("groupId");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		userName = (userName == null ? null : new String(userName.getBytes("iso-8859-1"), "utf-8"));
		model.addAttribute("id", id);
		model.addAttribute("userName", userName);
		model.addAttribute("appId", appId);
		model.addAttribute("groupId", groupId);
		return "show/authorizeFunction";
	}

	/**
	 * 授权用户功能
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("authorizeFun")
	public void authorizeFunction(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------authorizeFun start------------------------------------");
		String id = request.getParameter("id");
		String function = request.getParameter("function");
		String resource = request.getParameter("resource");
		String userName = request.getParameter("userName");
		String appId = request.getParameter("appId");
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);
		String reslut = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), Signature);
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				boo = true;
			} else {
				boo = false;
				// 如果用户不存在 添加用户
				if (("10002").equals(String.valueOf(jsonObject.get("error_code")))) {
					Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
					map.put("appId", appId);
					map.put("appUserId", id);
					map.put("resourceId", resource);
					map.put("privilegeFunId", function);
					map.put("appUserName", userName);
					reslut = sendPost(oesPrivilegeDev.getAddPrivilegeUserUrl(), map);
					if (reslut != null && !("").equals(reslut)) {
						jsonObject = JSONObject.fromObject(reslut);
						if (!("0").equals(jsonObject.get("status"))) {
							boo = true;
							jsonobj.put("result", boo);
							WebUtils.writeJson(response, jsonobj);
							return;
						} else {
							boo = false;
							jsonobj.put("result", boo);
							WebUtils.writeJson(response, jsonobj);
							return;
						}
					} else {
						boo = false;
					}
				}
			}
		} else {
			boo = false;
		}
		// 更新资源与功能
		if (boo) {
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", id);
			signature.put("method", "1");
			signature.put("resourceId", resource);
			signature.put("privilegeFunId", function);
			reslut = sendPost(oesPrivilegeDev.getModitUserPrivilegeUrl(), signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
		return;

	}

	/**
	 * 授权用户角色
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("authorizeRole")
	public void authorizeRole(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------authorizeRole        start------------------------------------");
		String appUserId = request.getParameter("id");// 用户id
		String addRoleId = request.getParameter("addRoleId");// 添加的角色id
		String delRoleId = request.getParameter("delRoleId");// 删除的角色id
		String appId = request.getParameter("appId").trim();
		String deptId = null;
		String groupId = null;
		String privilegeFunId = null;
		String resourceId = null;
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", appUserId);
		// 查询当前用户权限
		String reslut = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), Signature);
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				deptId = (String) (jsonObject.get("deptId").equals("null") ? "" : jsonObject.get("deptId"));
				groupId = (String) (jsonObject.get("groupId").equals("null") ? "" : jsonObject.get("groupId"));
				privilegeFunId = (String) (jsonObject.get("privilegeFunId").equals("null") ? ""
						: jsonObject.get("privilegeFunId"));
				resourceId = (String) (jsonObject.get("resourceId").equals("null") ? "" : jsonObject.get("resourceId"));
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}

		if (addRoleId != null && boo) {
			// 添加勾选的角色
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", appUserId);
			signature.put("method", "0");
			signature.put("privilegeRoleId", addRoleId);
			signature.put("deptId", deptId);
			signature.put("groupId", groupId);
			signature.put("privilegeFunId", privilegeFunId);
			signature.put("resourceId", resourceId);
			reslut = sendPost(oesPrivilegeDev.getModitUserPrivilegeUrl(), signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		// 删除取消勾选的角色
		if (delRoleId != null && !("null").equals(delRoleId) && boo) {
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", appUserId);
			signature.put("method", "1");
			signature.put("privilegeRoleId", delRoleId);
			signature.put("deptId", deptId);
			signature.put("groupId", groupId);
			signature.put("privilegeFunId", privilegeFunId);
			signature.put("resourceId", resourceId);
			reslut = sendPost(oesPrivilegeDev.getModitUserPrivilegeUrl(), signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}

		// result = true表示该用户授权角色成功
		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
		return;
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------tree       start------------------------------------");
		JSONArray jsonArr = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// 防止组织结构菜单树 异步加载
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
			map.put("status", "1");
			map.put("tree", jsonArr);
			WebUtils.writeJsonToMap(response, map);
			return;
		}
		String groupId = request.getParameter("groupId");
		String appId = request.getParameter("appId");
		map.put("appId", appId);
		map.put("groupId", groupId);
		// 获取组织结构缓存
		String s = sendPost(oesPrivilegeDev.getGroupCacheUrl(), map);
		if (s != null && !("").equals(s)) {
			JSONObject jsonObject = JSONObject.fromObject(s);
			// status为1时，该组织机构存在，构建tree
			if (!("0").equals(jsonObject.get("status"))) {

				JSONArray menuArray = (JSONArray) jsonObject.get("menuList");
				// 如果menuList不为空则构建tree,否则直接返回无权限状态
				if (menuArray.size() > 0) {
					JSONArray resourceArray = (JSONArray) jsonObject.get("resourceList");
					// 将json对象转换为java对象
					List<PrivilegeMenu> menuList = JSONArray.toList(menuArray, PrivilegeMenu.class);
					s = sendPost(oesPrivilegeDev.getAppResRedisUrl(), map);
					jsonObject = JSONObject.fromObject(s);// 将json字符串转换为json对象
					JSONArray functionArray = (JSONArray) jsonObject.get("functionList");
					s = sendPost(oesPrivilegeDev.getAllOperationUrl(), map);
					jsonObject = JSONObject.fromObject(s);
					JSONArray operationArray = (JSONArray) jsonObject.get("operationList");
					// 将json对象转换为java对象
					List<PrivilegeOperation> operationList = JSONArray.toList(operationArray, PrivilegeOperation.class);
					List<PrivilegeResource1> resourceList = JSONArray.toList(resourceArray, PrivilegeResource1.class);
					List<PrivilegeFunction> functionList = JSONArray.toList(functionArray, PrivilegeFunction.class);
					// 根据DisplayOrder排序
					java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
						@Override
						public int compare(PrivilegeMenu o1, PrivilegeMenu o2) {
							return o1.getDisplayOrder() - o2.getDisplayOrder();
						}
					});
					List<TreeNode> nodes = convertTreeNodeList(menuList);
					jsonArr = JSONArray.fromObject(buildTree(nodes, resourceList, functionList, operationList));
					map.put("status", "1");
					map.put("tree", jsonArr);
				} else {
					map.put("status", "0");
					map.put("tree", new JSONArray());
				}
				WebUtils.writeJsonToMap(response, map);
			} else {
				jsonArr = new JSONArray();
				map.put("status", "0");
				map.put("tree", jsonArr);
				WebUtils.writeJsonToMap(response, map);
			}
		}

	}

	/**
	 * 添加时构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree(List<TreeNode> treeNodes, List<PrivilegeResource1> resourceList,
			List<PrivilegeFunction> functionList, List<PrivilegeOperation> operationList) {
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
						entry.getValue().setId(res.getResourceId());
						entry.getValue().setText(res.getResourceName());
						entry.getValue().setIsmodule("1");
						List<TreeNode> treeNodeList1 = new ArrayList<TreeNode>();
						for (PrivilegeFunction func : functionList) {
							TreeNode treeNode1 = new TreeNode();
							if ((res.getResourceId()).equals(func.getResourceId())) {
								treeNode1.setId(func.getFunctionId());
								treeNode1.setIsmodule("2");

								for (PrivilegeOperation operation : operationList) {
									if (func.getOptId().equals(operation.getId())) {
										treeNode1.setText(operation.getName());
									}
								}
								treeNodeList1.add(treeNode1);
							}
						}
						entry.getValue().setChildren(treeNodeList1);
					}
				}
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
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

	private TreeNode convertTreeNode(PrivilegeMenu privilegeMenu) {
		TreeNode node = null;
		if (privilegeMenu != null) {
			node = new TreeNode();
			node.setId(String.valueOf(privilegeMenu.getMenuId()));// 菜单ID
			node.setIsmodule("0");
			node.setChecked(false);
			node.setText(privilegeMenu.getMenuName());// 菜单名称
			node.setTarget("");
			node.setResource("");
			node.setPid(String.valueOf(privilegeMenu.getParentId()));// 父级菜单ID
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuCode", privilegeMenu.getMenuCode());
			map.put("menuLevel", privilegeMenu.getMenuLevel());
			map.put("menuRule", privilegeMenu.getMenuRule());
			map.put("dislayOrder", privilegeMenu.getDisplayOrder());
			map.put("parentId", privilegeMenu.getParentId());
			map.put("status", privilegeMenu.getDisplayOrder());
			node.setAttributes(map);
		}
		return node;
	}

	/**
	 * 查询指定用户的功能情况
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("function")
	public void function(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------function start------------------------------------");
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);
		String reslut = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), Signature);
		String[] functionIds = null;
		String[] resourceIds = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				// 用户表中 资源 与功能
				String privilegeResId = (String) (jsonObject.get("resourceId").equals("null") ? ""
						: jsonObject.get("resourceId"));
				String privilegeFunId = (String) (jsonObject.get("privilegeFunId").equals("null") ? ""
						: jsonObject.get("privilegeFunId"));
				if (!("").equals(privilegeFunId)) {
					functionIds = privilegeFunId.split(",");
				}
				if (!("").equals(privilegeResId)) {
					resourceIds = privilegeResId.split(",");
				}
			} else {
			}
		}

		Map<String, Object> aMap = new HashMap<String, Object>();
		aMap.put("resourceIds", resourceIds);
		aMap.put("functionIds", functionIds);
		WebUtils.writeJson(response, JSONObject.fromObject(aMap));
		return;
	}

	/**
	 * 查询指定用户的角色情况
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("role")
	public void role(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------role        start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String groupId = request.getParameter("groupId").trim();// 该用户所属组织机构id
		String id = request.getParameter("id").trim();// 用户appUserId
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		String appId = request.getParameter("appId").trim();
		// 查找当前用户角色
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);
		String reslut = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), Signature);
		List<Map<String, Object>> userRoleList = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				userRoleList = (List<Map<String, Object>>) jsonObject.get("roleList");
			}
		}
		// 当前第几页
		String page = request.getParameter("page");
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		int startRow = (currentPage - 1) * pageSize;
		// 查询应用的角色 或者组织机构的角色
		Map<String, Object> SignatureMap = new HashMap<String, Object>();
		SignatureMap.put("appId", appId);
		SignatureMap.put("groupId", groupId);
		SignatureMap.put("start", startRow);
		SignatureMap.put("limit", pageSize);
		reslut = sendPost(oesPrivilegeDev.getQueryRoleUrl(), SignatureMap);
		List<Map<String, Object>> RoleList = null;
		int count = 0;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				RoleList = (List<Map<String, Object>>) jsonObject.get("roleList");
				count = jsonObject.getInt("total");
			}
		}
		JSONObject jsonObjArr = new JSONObject();
		for (Map<String, Object> map : RoleList) {
			String roleId = (String) map.get("privilegeRoleId");
			if (userRoleList == null || userRoleList.size() <= 0) {
				map.put("checked", false);
			} else {
				for (Map<String, Object> map2 : userRoleList) {
					if (roleId.equals(map2.get("privilegeRoleId"))) {
						map.put("checked", true);
					}
				}
			}
		}
		JSONArray jsonArr = JSONArray.fromObject(RoleList);
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
		WebUtils.writeJson(response, jsonObjArr);
		return;
	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateUser")
	public void updateUserByID(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------updateUserByID        start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		String groupId = request.getParameter("groupId");
		String type = request.getParameter("type");// 类型id
		String groupCodeName = "";// 存放组织机构Id的字段名称
		switch (type) {
		case "3":
			groupCodeName = "UNIVERSITYCODE";
			break;
		case "4":
			groupCodeName = "ORGANIZATIONID";
			break;
		case "5":
			groupCodeName = "DCENTERCODE";
			break;
		case "6":
			groupCodeName = "LCENTERCODE";
			break;
		default:
			break;
		}
		// 公共参数
		String appUserName = request.getParameter("appUserName");// 真实姓名
		String passWord = request.getParameter("passWord");
		String Name = request.getParameter("Name");// 真实姓名
		String SEX = request.getParameter("SEX");// 性别
		String PHONENO = request.getParameter("PHONENO");// 固定电话
		String MOBILEPHONE = request.getParameter("MOBILEPHONE");// 移动电话
		String EMAIL = request.getParameter("EMAIL");// 邮箱
		String HRLOGINNAME = request.getParameter("HRLOGINNAME");// HR用户名
		String IDCARD = request.getParameter("IDCARD");// 身份证
		String ADDRESS = request.getParameter("ADDRESS");// 地址
		String POSTCODE = request.getParameter("POSTCODE");// 邮政编码
		String FAX = request.getParameter("FAX");// 传真

		// 院校特有参数
		String ATTACH = request.getParameter("ATTACH");// 归属
		String DEPARTMENT = request.getParameter("DEPARTMENT");// 部门
		String PERSONALDES = request.getParameter("PERSONALDES");// 个人描述

		// 基础特有参数
		String KZROLEID = request.getParameter("KZROLEID");// 孔子学院角色ID
		String ISSUPERADMIN = request.getParameter("ISSUPERADMIN");// 是否是超级管理员
		String KZID = request.getParameter("KZID"); // 孔子学院ID
		String STAFFID = request.getParameter("STAFFID");// 职员ID
		String ORGANIZATIONTYPECODE = request.getParameter("ORGANIZATIONTYPECODE"); // 组织机构类型ID
		String ACTIVEBEGINDATE = request.getParameter("ACTIVEBEGINDATE");// 教材帐号生效日期
		String ACTIVEENDDATE = request.getParameter("ACTIVEENDDATE");// 教材帐号失效日期
		String ACTIVESTATUS = request.getParameter("ACTIVESTATUS");// 教材帐号状态
		String ISEDITPURCHASEPRICE = request.getParameter("ISEDITPURCHASEPRICE");// 是否有采购价格修改权限(0否1是)
		String StudyType = request.getParameter("StudyType");// 教材帐号状态
		String ORGANIZATIONID = request.getParameter("ORGANIZATIONID");//组织机构Id
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("LOGINNAME", appUserName);
		jsonObject.put("PASSWORD", passWord);
		jsonObject.put("NAME", Name);
		jsonObject.put("SEX", SEX);
		if (!"4".equals(type)) {
			jsonObject.put("IDCARD", IDCARD);
			jsonObject.put("ADDRESS", ADDRESS == null ? "" : ADDRESS);
			jsonObject.put("POSTCODE", POSTCODE == null ? "" : POSTCODE);
		}
		jsonObject.put("PHONENO", PHONENO);
		jsonObject.put("MOBILEPHONE", MOBILEPHONE);
		jsonObject.put("FAX", FAX == null ? "" : FAX);
		jsonObject.put("EMAIL", EMAIL == null ? "" : EMAIL);
		jsonObject.put("HRLOGINNAME", HRLOGINNAME == null ? "" : HRLOGINNAME);
		jsonObject.put("Operator", user.get("username"));

		if ("3".equals(type)) {// 如果为院校用户
			jsonObject.put("ATTACH", ATTACH == null ? "0" : ATTACH);
			jsonObject.put("DEPARTMENT", DEPARTMENT == null ? "" : DEPARTMENT);
			jsonObject.put("PERSONALDES", PERSONALDES == null ? "" : PERSONALDES);
		} else if ("4".equals(type)) {// 如果为基础用户
			jsonObject.put("DEPARTMENT", DEPARTMENT == null ? "" : DEPARTMENT);
			jsonObject.put("ROLEID", "");
			jsonObject.put("DEPARTMENT", DEPARTMENT == null ? "" : DEPARTMENT);
			jsonObject.put("ISSUPERADMIN", ISSUPERADMIN == null ? "0" : ISSUPERADMIN);
			jsonObject.put("KZROLEID", KZROLEID == null ? "" : KZROLEID);
			jsonObject.put("KZID", KZID == null ? "" : KZID);
			jsonObject.put("ORGANIZATIONTYPECODE", ORGANIZATIONTYPECODE == null ? "" : ORGANIZATIONTYPECODE);
			jsonObject.put("STAFFID", STAFFID == null ? "" : STAFFID);
			jsonObject.put("ACTIVEBEGINDATE", ACTIVEBEGINDATE == null ? "" : ACTIVEBEGINDATE);
			jsonObject.put("ACTIVEENDDATE", ACTIVEENDDATE == null ? "" : ACTIVEENDDATE);
			jsonObject.put("ACTIVESTATUS", ACTIVESTATUS == null ? "1" : ACTIVESTATUS);
			jsonObject.put("ISEDITPURCHASEPRICE", ISEDITPURCHASEPRICE == null ? "1" : ISEDITPURCHASEPRICE);
			jsonObject.put("StudyType", StudyType == null ? "" : StudyType);
		}
		if (type.equals("4")) {//如果为基础用户
			jsonObject.put(groupCodeName, ORGANIZATIONID == null ? "" : ORGANIZATIONID);// 相应组织编号
		} else {
			jsonObject.put(groupCodeName, groupId);// 相应组织编号
		}
		Boolean boo = false;// 是否修改成功
		Map<String, Object> map = new HashMap<String, Object>();
		String result = sendPutByJson(oesPrivilegeDev.getUpdateOesUserUrl() + "?type=" + type, jsonObject);
		if (result != null && !result.isEmpty()) {
			jsonObject = JSONObject.fromObject(result);
			if (jsonObject.getInt("Code") == 0) {
				JSONArray array = JSONArray.fromObject(jsonObject.get("Data"));
				map = (Map<String, Object>) array.get(0);
				String Status = (String) map.get("Status");
				if ("True".equals(Status)) {
					boo = true;
				}
			}
		}
		// 更新oes_user
		map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("appUserId", appUserId);
		if (boo) {
			// 先查询该用户的信息
			result = sendPost(oesPrivilegeDev.getUserPrivilegeUrl(), map);
			JSONObject object = JSONObject.fromObject(result);
			if ("1".equals(object.get("status"))) {
				String privilegeFunId = (String) (object.get("privilegeFunId").equals("null") ? ""
						: object.get("privilegeFunId"));
				String resourceId = (String) (object.get("resourceId").equals("null") ? "" : object.get("resourceId"));
				String deptId = (String) (object.get("deptId").equals("null") ? "" : object.get("deptId"));
				map.put("privilegeFunId", privilegeFunId);
				map.put("resourceId", resourceId);
				map.put("deptId", deptId);
				map.put("groupId", groupId);
				map.put("method", "1");
				// 更新用户
				result = sendPost(oesPrivilegeDev.getModitUserPrivilegeUrl(), map);
				object = JSONObject.fromObject(result);
				map.clear();
				if ("1".equals(object.get("status"))) {
					map.put("status", "2");
				} else {
					map.put("status", "-1");
				}
			} else {
				map.put("status", "-1");
				map.put("errMsg", object.get("errMsg"));
			}
		} else {
			map.put("status", "-1");
		}
		WebUtils.writeJsonToMap(response, map);
		return;
	}

	/**
	 * 根据用户id删除用户
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping("removeUserByID")
	public void removeUser(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		String loginName = request.getParameter("loginName");
		String type = request.getParameter("type");// 用户类型
		String token = privilegeGetSignatureService.getToken();
		Map<String, Object> map = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(),
				oesPrivilegeDev.getClientId(), token);
		map.put("grant_type", oesPrivilegeDev.getGrantType());
		map.put("client_id", oesPrivilegeDev.getClientId());
		map.put("access_token",token);
		map.put("scope", "read,write");
		map.put("source_id", appUserId);
		map.put("username", loginName);
		String result = sendPost(oesPrivilegeDev.getUserCenterUnBindUrl(), map);
		JSONObject object = null;
		if (result != null && !result.isEmpty()) {
			object = JSONObject.fromObject(result);
			if ("1".equals(object.getString("status"))) {
				// 调用删除oes用户接口
				result = sendDelete(oesPrivilegeDev.getDelOesUserUrl() + "?type=" + type + "&loginName=" + loginName);
				object = JSONObject.fromObject(result);
				if (object.getInt("Code") == 0) {// 如果删除成功，调用删除权限用户接口，不成功，返回
					JSONArray array = JSONArray.fromObject(object.get("Data"));
					map = (Map<String, Object>) array.get(0);
					String Status = (String) map.get("Status");
					if ("True".equals(Status)) {
						map = privilegeGetSignatureService.getSignature(appId);
						map.put("appUserId", appUserId);
						map.put("appId", appId);
						// 删除权限库中用户
						result = sendPost(oesPrivilegeDev.getDelUserPrivilegeUrl(), map);
						object = JSONObject.fromObject(result);
						WebUtils.writeJson(response, object);
					} else {
						map.clear();
						map.put("status", "0");
						map.put("errMsg", "失败");
						WebUtils.writeJsonToMap(response, map);
					}
				} else {
					map.clear();
					map.put("status", "0");
					map.put("errMsg", "失败");
					WebUtils.writeJsonToMap(response, map);
				}
			} else {
				map.clear();
				map.put("status", "0");
				map.put("errMsg", object.get("errMsg"));
				WebUtils.writeJsonToMap(response, map);
			}
		}
		

	}

	/**
	 * 跳转到基础用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "userList")
	public String userList(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------userlist       start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "show/userlist";
	}

	/**
	 * 跳转到院校用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "UniversityUserList")
	public String UniversityUserList(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------userlist       start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "show/UniversityUserList";
	}

	/**
	 * 跳转到大区用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "DcenterUserList")
	public String DcenterUserList(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------userlist       start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "show/DcenterUserList";
	}

	/**
	 * 跳转到学习中心用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "LcenterUserList")
	public String LcenterUserList(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------userlist       start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "show/LcenterUserList";
	}

	/**
	 * 查找用户
	 * 
	 * @return 返回的json
	 */
	@RequestMapping("findUserList")
	public void findUserList(HttpServletRequest request, HttpServletResponse response) {
		String groupId = request.getParameter("groupId");
		OesGroup oesGroup = oesGroupService.findByCode(groupId);
		String type = request.getParameter("type");// 用户类型
		if ("4".equals(type)) {
			groupId = "";
		}
		String userName = request.getParameter("userName");
		// 当前第几页
		String page = request.getParameter("page");
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		//url 参数
		String parameter = "type=" + type + "&loginName=" + userName + "&orgCode=" + groupId + "&page="+currentPage +
				"&page_size=" +pageSize ;
		String result = sendGet(oesPrivilegeDev.getFindOesUserUrl(), parameter);
		JSONObject object = JSONObject.fromObject(result);
		List<Map<String, Object>> users = null;
		int userNum = 0;
		if (object.getInt("Code") == 0) {
			users = JSONArray.toList(object.getJSONArray("Data"), Map.class);
			userNum = object.getInt("Count");
			for (Map<String, Object> user : users) {
				user.put("groupId", oesGroup.getGroupCode());
				user.put("groupName", oesGroup.getGroupName());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", users);
		map.put("total", userNum);
		WebUtils.writeJson(response, JSONObject.fromObject(map));
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
		String type = request.getParameter("type");
		String typeName = null;
		switch (type) {
		case "3":
			typeName = "院校";
			break;
		case "4":
			typeName = "基础";
			break;
		case "5":
			typeName = "大区";
			break;
		case "6":
			typeName = "学习中心";
			break;
		default:
			break;
		}
		int roleType = (int) user.get("Type");
		List<OesGroup> groupList = null;
		// 用户角色类型，1-普通用户，2-管理员，3-组织机构管理员
		// 如果当前用户为管理员，显示所有组织结构，否则显示当前用户所在的组织机构
		if (roleType == 1 || roleType == 3) {
			groupList = new ArrayList<OesGroup>();
			OesGroup group = oesGroupService.findByTypeNameAndCode(typeName, groupId);
			groupList.add(group);
		} else {
			groupList = oesGroupService.findByTypeName(typeName);
		}
		List<Map<String, Object>> groups = new ArrayList<Map<String, Object>>();
		for (OesGroup oesGroup : groupList) {
			Map<String, Object> group = new HashMap<String, Object>();
			group.put("groupCode", oesGroup.getGroupCode());
			group.put("groupName", oesGroup.getGroupName());
			switch (oesGroup.getGroupTypeName()) {
			case "大区":
				group.put("groupType", "5");
				group.put("codeName", "DCENTERCODE");
				break;
			case "学习中心":
				group.put("groupType", "6");
				group.put("codeName", "LCENTERCODE");
				break;
			case "基础":
				group.put("groupType", "4");
				group.put("codeName", "ORGANIZATIONID");
				break;
			case "院校":
				group.put("groupType", "3");
				group.put("codeName", "UNIVERSITYCODE");
				break;
			default:
				break;
			}
			groups.add(group);
		}
		JSONArray jsonArr = JSONArray.fromObject(groups);
		WebUtils.writeJson(response, jsonArr);
		return;
	}

	/**
	 * 实现添加用户的操作
	 * 
	 * @param appUserName
	 *            用户名
	 * @param appId
	 *            应用Id
	 * @param groupId
	 *            组织机构Id
	 * @param passWord
	 *            密码
	 * @param passWord
	 *            密码
	 */
	@ResponseBody
	@RequestMapping("addUser")
	public void addUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------addUser         start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String appId = request.getParameter("appId");
		String groupId = request.getParameter("groupId");
		String appUserName = request.getParameter("appUserName");
		String deptId = request.getParameter("deptId") == null ? "" : request.getParameter("deptId");
		String type = request.getParameter("type");// 类型id
		String groupCodeName = "";// 存放组织机构Id的字段名称
		switch (type) {
		case "3":
			groupCodeName = "UNIVERSITYCODE";
			break;
		case "4":
			groupCodeName = "ORGANIZATIONID";
			break;
		case "5":
			groupCodeName = "DCENTERCODE";
			break;
		case "6":
			groupCodeName = "LCENTERCODE";
			break;
		default:
			break;
		}
		// 公共参数
		String Name = request.getParameter("Name");// 真实姓名
		String SEX = request.getParameter("SEX");// 性别
		String PHONENO = request.getParameter("PHONENO");// 固定电话
		String MOBILEPHONE = request.getParameter("MOBILEPHONE");// 移动电话
		String EMAIL = request.getParameter("EMAIL");// 邮箱
		String HRLOGINNAME = request.getParameter("HRLOGINNAME");// HR用户名
		String IDCARD = request.getParameter("IDCARD");// 身份证
		String ADDRESS = request.getParameter("ADDRESS");// 地址
		String POSTCODE = request.getParameter("POSTCODE");// 邮政编码
		String FAX = request.getParameter("FAX");// 传真
		// 院校特有参数
		String ATTACH = request.getParameter("ATTACH");// 归属
		String DEPARTMENT = request.getParameter("DEPARTMENT");// 部门
		String PERSONALDES = request.getParameter("PERSONALDES");// 个人描述

		// 基础特有参数
		String KZROLEID = request.getParameter("KZROLEID");// 孔子学院角色ID
		String ISSUPERADMIN = request.getParameter("ISSUPERADMIN");// 是否是超级管理员
		String KZID = request.getParameter("KZID"); // 孔子学院ID
		String STAFFID = request.getParameter("STAFFID");// 职员ID
		String ORGANIZATIONTYPECODE = request.getParameter("ORGANIZATIONTYPECODE"); // 组织机构类型ID
		String ACTIVEBEGINDATE = request.getParameter("ACTIVEBEGINDATE");// 教材帐号生效日期
		String ACTIVEENDDATE = request.getParameter("ACTIVEENDDATE");// 教材帐号失效日期
		String ACTIVESTATUS = request.getParameter("ACTIVESTATUS");// 教材帐号状态
		String ISEDITPURCHASEPRICE = request.getParameter("ISEDITPURCHASEPRICE");// 是否有采购价格修改权限(0否1是)
		String StudyType = request.getParameter("StudyType");// 教材帐号状态
		String ORGANIZATIONID = request.getParameter("ORGANIZATIONID");
		appUserName = java.net.URLEncoder.encode(appUserName, "UTF-8");
		String passWord = request.getParameter("passWord");
		try {// 密码AES加密
			passWord = AESUtils.encrypt(passWord, oesPrivilegeDev.getClientSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 从缓存中获取token
		String access_token = privilegeGetSignatureService.getToken();
		Map<String, Object> parameters = new HashMap<String, Object>();
		// 验证用户是否存在
		parameters = privilegeGetSignatureService.getOauthSignature(oesPrivilegeDev.getAppId(),
				oesPrivilegeDev.getClientId(), access_token);
		parameters.put("access_token", access_token);
		parameters.put("account", appUserName);
		parameters.put("client_id", oesPrivilegeDev.getClientId());
		parameters.put("accountType", "1");
		String result = sendPost(oesPrivilegeDev.getUserCenterVerifyUrl(), parameters);
		if (result != null && !("").equals(result)) {
			String aString = result.substring(0, 1);
			if (aString.equals("{")) {
				JSONObject object = JSONObject.fromObject(result);
				if ("0".equals(object.get("status"))) {// 用户名已存在
					WebUtils.writeJson(response, object);
					return;
				}
			}
		}
		// oes 添加用户
		String appUserId = null;// 用户ID
		parameters.clear();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("LOGINNAME", appUserName);
		jsonObject.put("PASSWORD", passWord);
		jsonObject.put("NAME", Name);
		jsonObject.put("SEX", SEX);
		if (!"4".equals(type)) {
			jsonObject.put("IDCARD", IDCARD);
			jsonObject.put("ADDRESS", ADDRESS == null ? "" : ADDRESS);
			jsonObject.put("POSTCODE", POSTCODE == null ? "" : POSTCODE);
		}
		jsonObject.put("PHONENO", PHONENO);
		jsonObject.put("MOBILEPHONE", MOBILEPHONE);
		jsonObject.put("FAX", FAX == null ? "" : FAX);
		jsonObject.put("EMAIL", EMAIL == null ? "" : EMAIL);
		jsonObject.put("HRLOGINNAME", HRLOGINNAME == null ? "" : HRLOGINNAME);
		jsonObject.put("Operator", user.get("username"));

		if ("3".equals(type)) {// 如果为院校用户
			jsonObject.put("ATTACH", ATTACH == null ? "0" : ATTACH);
			jsonObject.put("DEPARTMENT", DEPARTMENT == null ? "" : DEPARTMENT);
			jsonObject.put("PERSONALDES", PERSONALDES == null ? "" : PERSONALDES);
		}
		if ("4".equals(type)) {// 如果为基础用户
			jsonObject.put("DEPARTMENT", DEPARTMENT == null ? "" : DEPARTMENT);
			jsonObject.put("ROLEID", "");
			jsonObject.put("ISSUPERADMIN", ISSUPERADMIN == null ? "0" : ISSUPERADMIN);
			jsonObject.put("KZROLEID", KZROLEID == null ? "" : KZROLEID);
			jsonObject.put("KZID", KZID == null ? "" : KZID);
			jsonObject.put("ORGANIZATIONTYPECODE", ORGANIZATIONTYPECODE == null ? "" : ORGANIZATIONTYPECODE);
			jsonObject.put("STAFFID", STAFFID == null ? "" : STAFFID);
			jsonObject.put("ACTIVEBEGINDATE", ACTIVEBEGINDATE == null ? "" : ACTIVEBEGINDATE);
			jsonObject.put("ACTIVEENDDATE", ACTIVEENDDATE == null ? "" : ACTIVEENDDATE);
			jsonObject.put("ACTIVESTATUS", ACTIVESTATUS == null ? "1" : ACTIVESTATUS);
			jsonObject.put("ISEDITPURCHASEPRICE", ISEDITPURCHASEPRICE == null ? "1" : ISEDITPURCHASEPRICE);
			jsonObject.put("StudyType", StudyType == null ? "" : StudyType);
		}
		
		if (type.equals("4")) {
			jsonObject.put(groupCodeName, ORGANIZATIONID == null ? "" : ORGANIZATIONID);// 相应组织编号
		}else {
			jsonObject.put(groupCodeName, groupId);// 相应组织编号
		}
		result = sendPostByJson(oesPrivilegeDev.getAddOesUserUrl() + "?Type=" + type, jsonObject);
		parameters.clear();
		if (result != null) {
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("Code") == 0) {// oes用户中心注册成功后，调用添加权限用户接口
				JSONArray array = JSONArray.fromObject(object.get("Data"));
				Map<String, Object> map = (Map<String, Object>) array.get(0);
				String Status = (String) map.get("Status");
				if ("True".equals(Status)) {
					appUserId = (String) map.get("UserId");
					parameters = privilegeGetSignatureService.getSignature(appId);
					parameters.put("appId", appId);
					parameters.put("appUserId", appUserId);
					parameters.put("appUserName", appUserName);
					parameters.put("groupId", groupId);
					parameters.put("deptId", deptId);
					result = sendPost(oesPrivilegeDev.getAddPrivilegeUserUrl(), parameters);
					object = JSONObject.fromObject(result);
					if (("1").equals(object.getString("status"))) {// 如果权限用户添加成功
						parameters = privilegeGetSignatureService.getOauthSignature(appId,
								oesPrivilegeDev.getClientId(), access_token);
						parameters.put("access_token", access_token);
						parameters.put("grant_type", oesPrivilegeDev.getGrantType());
						parameters.put("scope", "read,write");
						parameters.put("username", appUserName);
						parameters.put("client_id", oesPrivilegeDev.getClientId());
						parameters.put("isValidate", 1);
						parameters.put("password", passWord);
						parameters.put("source_id", appUserId);
						result = sendPost(oesPrivilegeDev.getUserCenterRegUrl(), parameters);
						if (result != null && !("").equals(result)) {
							String aString = result.substring(0, 1);
							if (aString.equals("{")) {
								object = JSONObject.fromObject(result);
								WebUtils.writeJson(response, object);
								return;
							}
						}
					} else {// 如果权限用户添加失败
						parameters.put("status", "0");
						parameters.put("errMsg", "保存失败");
						WebUtils.writeJsonToMap(response, parameters);
						return;
					}
				}
			} else {// 如果oes用户添加不成功，返回错误提示
				parameters.put("errMsg", object.get("Message"));
				WebUtils.writeJsonToMap(response, parameters);
				return;
			}
		}
	}
}
