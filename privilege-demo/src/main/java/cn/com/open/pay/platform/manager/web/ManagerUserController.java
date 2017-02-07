package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/managerUser/")
public class ManagerUserController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private UserService userService;

	@Autowired
	private PrivilegeLogService privilegeLogService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Value("#{properties['get-privilege-user-uri']}")
	private String getUserPrivilegeUrl;
	@Value("#{properties['modify-privilege-user-uri']}")
	private String moditUserPrivilegeUrl;
	@Value("#{properties['privilege-approle-redis-query-uri']}")
	private String getAppRoleRedis;
	@Value("#{properties['appId']}")
	private String appId;
	@Value("#{properties['add-privilege-user-uri']}")
	private String addPrivilegeUserUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-operation-name-uri']}")
	private String getOperationNameUrl;
	@Value("#{properties['privilege-get-operation-uri']}")
	private String getAllOperationUrl;
	@Value("#{properties['privilege-usermenu-redis-query-uri']}")
	private String userMenuRedisUrl;
	@Value("#{properties['query-privilege-user-uri']}")
	private String queryUserUrl;

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
		String userName = request.getParameter("userName");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		userName = (userName == null ? null : new String(userName.getBytes("iso-8859-1"), "utf-8"));
		model.addAttribute("id", id);
		model.addAttribute("userName", userName);
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
		String userName = request.getParameter("userName");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		userName = (userName == null ? null : new String(userName.getBytes("iso-8859-1"), "utf-8"));
		model.addAttribute("id", id);
		model.addAttribute("userName", userName);
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
		String id = request.getParameter("id");
		String function = request.getParameter("function");
		String resource = request.getParameter("resource");
		String userName = request.getParameter("userName");
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);
		String reslut = sendPost(getUserPrivilegeUrl, Signature);
		List<Map<String, Object>> userRoleList = null;
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
					reslut = sendPost(addPrivilegeUserUrl, map);
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
				System.err.println("该用户没有角色");
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
			reslut = sendPost(moditUserPrivilegeUrl, signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					System.err.println("修改成功");
					boo = true;
				} else {
					boo = false;
					System.err.println("修改失败");
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
		String appUserId = request.getParameter("id");
		String role = request.getParameter("role");
		String roleId = request.getParameter("roleId");
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
		String reslut = sendPost(getUserPrivilegeUrl, Signature);
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				deptId = (String) (jsonObject.get("deptId").equals("null") ? "" : jsonObject.get("deptId"));
				groupId = (String) (jsonObject.get("groupId").equals("null") ? "" : jsonObject.get("groupId"));
				privilegeFunId = (String) (jsonObject.get("privilegeFunId").equals("null") ? "": jsonObject.get("privilegeFunId"));
				resourceId = (String) (jsonObject.get("resourceId").equals("null") ? "" : jsonObject.get("resourceId"));
				boo = true;
			} else {
				boo = false;
				System.err.println("该用户没有角色");
			}
		} else {
			boo = false;
		}
		
		if (role != null && boo) {
			// 添加勾选的角色
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", appUserId);
			signature.put("method", "0");
			signature.put("privilegeRoleId", role);
			signature.put("deptId", deptId);
			signature.put("groupId", groupId);
			signature.put("privilegeFunId", privilegeFunId);
			signature.put("resourceId", resourceId);
			reslut = sendPost(moditUserPrivilegeUrl, signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					System.err.println("添加成功");
					boo = true;
				} else {
					System.err.println("添加失败");
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		// 删除取消勾选的角色
				if (roleId != null && boo) {
					Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
					signature.put("appId", appId);
					signature.put("appUserId", appUserId);
					signature.put("method", "1");
					signature.put("privilegeRoleId", roleId);
					signature.put("deptId", deptId);
					signature.put("groupId", groupId);
					signature.put("privilegeFunId", privilegeFunId);
					signature.put("resourceId", resourceId);
					reslut = sendPost(moditUserPrivilegeUrl, signature);
					if (reslut != null && !("").equals(reslut)) {
						JSONObject jsonObject = JSONObject.fromObject(reslut);
						if (!("0").equals(jsonObject.get("status"))) {
							System.err.println("删除成功");
							boo = true;
						} else {
							boo = false;
							System.err.println("删除失败");
						}
					} else {
						boo = false;
					}
				}
		System.out.println("****************id:" + appUserId + "****************role:" + role);

		// result = true表示该用户授权角色成功

		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
		return;
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(appMenuRedisUrl, map);
		JSONObject obj = JSONObject.fromObject(s);// 将json字符串转换为json对象
		JSONArray objArray = (JSONArray) obj.get("menuList");
		// 将json对象转换为java对象
		List<PrivilegeMenu> menuList = JSONArray.toList(objArray, PrivilegeMenu.class);
		String s1 = sendPost(appResRedisUrl, map);
		JSONObject obj1 = new JSONObject().fromObject(s1);// 将json字符串转换为json对象
		JSONArray obj1Array = (JSONArray) obj1.get("resourceList");
		JSONArray obj2Array = (JSONArray) obj1.get("functionList");
		String operation = sendPost(getAllOperationUrl, map);
		obj = JSONObject.fromObject(operation);
		objArray = (JSONArray) obj.get("operationList");
		// 将json对象转换为java对象
		List<PrivilegeOperation> operationList = JSONArray.toList(objArray, PrivilegeOperation.class);
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array, PrivilegeResource1.class);
		List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array, PrivilegeFunction.class);
		List<TreeNode> nodes = convertTreeNodeList(menuList);
		JSONArray jsonArr = JSONArray.fromObject(buildTree(nodes, resourceList, functionList, operationList));
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
		}
		WebUtils.writeJson(response, jsonArr);
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
					node.setState("close");
				}
				String menuId = entry.getValue().getId();
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				for (PrivilegeResource1 res : resourceList) {
					// TreeNode treeNode=new TreeNode();
					if ((menuId).equals(res.getMenuId())) {
						entry.getValue().setState("closed");
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
						// treeNodeList.add(entry.getValue());
					}
				}
				/*
				 * List<TreeNode> nodeList = entry.getValue().getChildren();
				 * if(nodeList == null){ nodeList=treeNodeList; }else{
				 * nodeList.addAll(treeNodeList); }
				 * entry.getValue().setChildren(nodeList);
				 */
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
		String id = request.getParameter("id");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);
		String reslut = sendPost(getUserPrivilegeUrl, Signature);

		List<Map<String, Object>> userResourceList = null;
		List<Map<String, Object>> userFunctionList = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (!("0").equals(JsnMap.get("status"))) {
				userResourceList = (List<Map<String, Object>>) JsnMap.get("resourceList");
				userFunctionList = (List<Map<String, Object>>) JsnMap.get("functionList");
			} else {
				System.err.println("该用户没有功能");
			}
		}
		reslut = sendPost(userMenuRedisUrl, Signature);
		List<Map<String, Object>> userMenuList = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (!("0").equals(JsnMap.get("status"))) {
				userMenuList = (List<Map<String, Object>>) JsnMap.get("menuList");
			} else {
				System.err.println("该用户没有菜单");
			}
		}
		Map<String, Object> aMap = new HashMap<String, Object>();
		aMap.put("resourceList", userResourceList);
		aMap.put("functionList", userFunctionList);
		aMap.put("menuList", userMenuList);
		System.err.println(JSONObject.fromObject(aMap).toString());
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
		String id = request.getParameter("id");
		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));

		// 查找当前用户角色
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("appUserId", id);

		String reslut = sendPost(getUserPrivilegeUrl, Signature);
		List<Map<String, Object>> userRoleList = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (!("0").equals(JsnMap.get("status"))) {
				userRoleList = (List<Map<String, Object>>) JsnMap.get("roleList");
			} else {
				System.err.println("该用户没有角色");
			}
		}
		// appRole
		Map<String, Object> SignatureMap = new HashMap<String, Object>();
		SignatureMap.put("appId", appId);
		reslut = sendPost(getAppRoleRedis, SignatureMap);
		List<Map<String, Object>> RoleList = null;
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (!("0").equals(JsnMap.get("status"))) {
				RoleList = (List<Map<String, Object>>) JsnMap.get("roleList");
			} else {
				System.err.println("该app没有角色");
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
		// 每页的开始记录 第一页为1 第二页为number +1
		int startRow = (currentPage - 1) * pageSize;
		int endRow = startRow + (pageSize - 1);

		/* 分页 */
		// appRole 总数
		int roleNum = 0;
		List<Map<String, Object>> roleListPage = new ArrayList<Map<String, Object>>();
		if (RoleList != null) {
			roleNum = RoleList.size();
			for (int i = startRow; i <= endRow; i++) {
				if (i >= RoleList.size()) {
					break;
				} else {
					roleListPage.add(RoleList.get(i));
				}
			}
		}

		JSONObject jsonObjArr = new JSONObject();

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : roleListPage) {
			String roleId = (String) map.get("privilegeRoleId");
			String roleName = (String) map.get("roleName");
			Integer status = (Integer) map.get("status");
			Map<String, Object> roleMap = new LinkedHashMap<String, Object>();
			roleMap.put("id", roleId);
			roleMap.put("name", roleName);
			roleMap.put("status", status);
			if (userRoleList == null || userRoleList.size() <= 0) {
				roleMap.put("checked", false);
			} else {
				for (Map<String, Object> map2 : userRoleList) {
					if (roleId.equals(map2.get("privilegeRoleId"))) {
						roleMap.put("checked", true);
					}
				}
			}
			list1.add(roleMap);
		}
		JSONArray jsonArr = JSONArray.fromObject(list1);
		jsonObjArr.put("total", roleNum);
		jsonObjArr.put("rows", jsonArr);
		System.out.println(jsonArr);
		WebUtils.writeJson(response, jsonObjArr);
		return;
	}

	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree2(List<PrivilegeMenu> menuList, List<PrivilegeResource1> resourceList,
			List<PrivilegeFunction> functionList, List<PrivilegeOperation> operationList) {
		// 顶级菜单资源集合
		List<TreeNode> results = new ArrayList<TreeNode>();
		List<PrivilegeResource1> pMenus = new ArrayList<PrivilegeResource1>();
		TreeNode treeNode = null;
		for (PrivilegeMenu menu : menuList) {
			String menuId = menu.getMenuId();
			String menuname = menu.getMenuName();
			List<TreeNode> childrenList = null;
			if (menu.getParentId().equals("0")) {
				treeNode = new TreeNode();
				treeNode.setId("m" + menuId);
				treeNode.setText(menuname);
				treeNode.setIsmodule("0");
				results.add(treeNode);
				childrenList = new ArrayList<TreeNode>();
			}

			for (PrivilegeMenu menu2 : menuList) {
				if (menu2.getParentId().equals(menu.getMenuId())) {
					for (PrivilegeResource1 resource1 : resourceList) {
						if (resource1.getMenuId().equals(menu2.getMenuId())) {
							TreeNode node = new TreeNode();
							List<TreeNode> childrenList2 = new ArrayList<TreeNode>();
							node.setId("r" + resource1.getResourceId());
							node.setText(resource1.getResourceName());
							node.setIsmodule("1");
							childrenList.add(node);
							for (PrivilegeFunction function : functionList) {
								if (function.getResourceId().equals(resource1.getResourceId())) {
									TreeNode Funnode = new TreeNode();
									Funnode.setId("f" + function.getFunctionId());
									String optId = function.getOptId();
									for (PrivilegeOperation operation : operationList) {
										if (optId.equals(operation.getId())) {
											Funnode.setText(operation.getName());
										}
									}
									Funnode.setIsmodule("2");
									childrenList2.add(Funnode);
								}
							}
							node.setChildren(childrenList2);
							treeNode.setChildren(childrenList);
						}
					}
				}
			}
		}
		return results;
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
		String realname = new String(request.getParameter("realname").getBytes("iso-8859-1"), "utf-8");
		String nickname = new String(request.getParameter("nickname").getBytes("iso-8859-1"), "utf-8");
		String deptName = new String(request.getParameter("updateDeptName").getBytes("iso-8859-1"), "utf-8");
		String deptID = new String(request.getParameter("updateDeptID").getBytes("iso-8859-1"), "utf-8");
		Integer id = Integer.valueOf(new String(request.getParameter("id").getBytes("iso-8859-1"), "utf-8"));

		// 将请求参数封装到User对象中
		User user = new User();
		user.setId(id);
		user.setRealName(realname);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setDeptID(Integer.valueOf(deptID));

		boolean result = userService.updateUser(user);

		// 添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(55);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String towLevels = privilegeModule.getName();
		String oneLevels = privilegeModule1.getName();
		User user1 = (User) request.getSession().getAttribute("user");
		String operator = user1.getUsername(); // 操作人
		String operatorId = user1.getId() + ""; // 操作人Id
		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("update");
		if (result = true) {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "修改" + realname + "用户成功", operatorId);
		} else {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "修改" + realname + "用户失败", operatorId);
		}

		// result = true表示该用户修改成功
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result", result);
		WebUtils.writeJson(response, jsonobj);
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
		Integer id = Integer.valueOf(new String(request.getParameter("id").getBytes("iso-8859-1"), "utf-8"));
		boolean result = userService.removeUserByID(id);
		// 添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(55);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String towLevels = privilegeModule.getName();
		String oneLevels = privilegeModule1.getName();
		User user = (User) request.getSession().getAttribute("user");
		String operator = user.getUsername(); // 操作人
		String operatorId = user.getId() + ""; // 操作人Id
		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("delete");
		if (result = true) {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "成功删除用户", operatorId);
		} else {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "用户删除失败", operatorId);
		}

		// result = true表示该用户删除成功
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result", result);
		WebUtils.writeJson(response, jsonobj);
		return;
	}

	/**
	 * 跳转到用户信息列表的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "userList")
	public String userList() {
		log.info("-------------------------userlist       start------------------------------------");
		return "show/userlist";
	}

	@RequestMapping("findUserList")
	public void findUserList(HttpServletRequest request, HttpServletResponse response) {
		// 当前第几页
		String page = request.getParameter("page");
		System.out.println(page);
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		System.out.println(rows);
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int startRow = (currentPage - 1) * pageSize;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("start", startRow);
		map.put("limit", pageSize);
		String result = sendPost(queryUserUrl, map);
		JSONObject object = JSONObject.fromObject(result);
		WebUtils.writeJson(response, object);
		return;
	}

	/**
	 * 根据条件（username：用户名；realname：真实姓名；nickname：昵称）实现查询用户操作
	 * 
	 * @param request
	 * @param response
	 * @return 查询完毕后返回查询结果 User对象或 User集合
	 */
	@RequestMapping("findUsers")
	public void findUsers(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------findUsers        start------------------------------------");
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String nickname = request.getParameter("nickname");
		String deptName = request.getParameter("deptName");
		username = (username == null ? null : new String(username.getBytes("iso-8859-1"), "utf-8"));
		realname = (realname == null ? null : new String(realname.getBytes("iso-8859-1"), "utf-8"));
		nickname = (nickname == null ? null : new String(nickname.getBytes("iso-8859-1"), "utf-8"));
		deptName = (deptName == null ? null : new String(deptName.getBytes("iso-8859-1"), "utf-8"));
		// 当前第几页
		String page = request.getParameter("page");
		System.out.println(page);
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		System.out.println(rows);
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int startRow = (currentPage - 1) * pageSize;
		// 将请求参数封装到User对象中
		User user = new User();
		user.setUsername(username);
		user.setRealName(realname);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
		// System.out.println("-----------username："+username+"realname："+realname+"nickname："+nickname+"-----------");
		List<User> users = userService.findUsers(user);
		int count = userService.findUsersCount(user);
		// for(User u : users){
		// System.out.println(u.getCreate_Time());
		// }

		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
		// System.out.println(jsonArr);
		WebUtils.writeJson(response, jsonObjArr);
		return;
	}

	/**
	 * 查询所有部门
	 * 
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value = "findAllDepts")
	public void findAllDepts(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------findAllDepts         start------------------------------------");
		List<Department> list = userService.findAllDepts();
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String str = null;
		if (list != null) {
			for (Department d : list) {
				map = new HashMap<String, Object>();
				map.put("id", d.getId());
				map.put("text", d.getDeptName());
				maps.add(map);
			}
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
			// System.out.println(str);
		}
		return;
	}

	/**
	 * 实现添加用户的操作
	 * 
	 * @param user_name
	 *            用户名
	 * @param nickname
	 *            昵称
	 * @param realname
	 *            真实姓名
	 * @param sha_password
	 *            MD5加密密码
	 */
	@ResponseBody
	@RequestMapping("addUser")
	public void addUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("-------------------------addUser         start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String user_name = request.getParameter("user_name");
		String real_name = request.getParameter("real_name");
		String nickname = request.getParameter("nickname");
		String sha_password = request.getParameter("sha_password");
		String deptName = request.getParameter("addDeptName");
		String deptID = request.getParameter("deptID");
		// log.info("user_name："+user_name+"；real_name:"+real_name+"；nickname:"+nickname+"；sha_password:"+sha_password+";deptName:"+deptName);
		JSONObject jsonObjArr = new JSONObject();
		// 判断数据库是否已经存在该用户
		boolean result = false;
		User user_db = userService.findByUsername(user_name);
		if (user_db != null) {
			// result = false表示该用户已被注册
			jsonObjArr.put("result", result);
			WebUtils.writeJson(response, jsonObjArr);
			return;
		}

		User user = new User();
		user.setUsername(user_name);
		user.setRealName(real_name);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setDeptID(Integer.valueOf(deptID));
		user.setCreateTime(new Date().getTime());
		// MD5加密
		user.setPlanPassword(sha_password);
		result = userService.addUser(user);

		// 添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(55);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String towLevels = privilegeModule.getName();
		String oneLevels = privilegeModule1.getName();
		User user1 = (User) request.getSession().getAttribute("user");
		String operator = user1.getUsername(); // 操作人
		String operatorId = user1.getId() + ""; // 操作人Id
		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("add");
		if (result = true) {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "添加" + user_name + "用户成功", operatorId);
		} else {
			privilegeLogService.addPrivilegeLog(operator, privilegeResource.getName(), oneLevels, towLevels,
					privilegeResource.getId() + "", operator + "添加" + user_name + "用户失败", operatorId);
		}

		// result = true 表示添加用户成功
		jsonObjArr.put("result", result);
		WebUtils.writeJson(response, jsonObjArr);
		return;
	}
}
