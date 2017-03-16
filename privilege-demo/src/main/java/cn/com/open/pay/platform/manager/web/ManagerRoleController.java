package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGroupService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/managerRole/")
public class ManagerRoleController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private PrivilegeRoleDetailsService privilegeRoleDetailsService;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private OesGroupService oesGroupService;
	/**
	 * 跳转到查询角色的页面
	 * 
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "roleMessage")
	public String showSearch(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------showSearch         start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "privilege/role/roleMessage";
	}

	@RequestMapping(value = "getRoleMessage")
	public void showSearch1(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------showSearch         start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String groupId = null;
		if (user != null) {
			// 用户角色类型，1-普通用户，2-管理员，3-组织机构管理员
			int Type=(int) user.get("Type");
			if (Type == 1 || Type == 3) {
				groupId = user.get("groupId").equals("null") ? null : (String) user.get("groupId");
			}
		}
		String appId = request.getParameter("appId");
		// 当前第几页
		String page = request.getParameter("page");
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		int startRow = (currentPage - 1) * pageSize;
		// appRole
		Map<String, Object> SignatureMap = new HashMap<String, Object>();
		SignatureMap.put("appId", appId);
		SignatureMap.put("groupId", groupId);
		SignatureMap.put("start", startRow);
		SignatureMap.put("limit", pageSize);
		String s = sendPost(oesPrivilegeDev.getQueryRoleUrl(), SignatureMap);
		JSONObject job = JSONObject.fromObject(s);
		SignatureMap.clear();
		List<Map<String, Object>> roles = JSONArray.fromObject(job.get("roleList"));
		if (roles.size() > 0) {
			for (Map<String, Object> m : roles) {
				if ((Integer) (m.get("status")) == 0) {
					m.put("status", "有效");
				} else {
					m.put("status", "无效");
				}
				if ((Integer) (m.get("roleType")) == 1) {
					m.put("roleType", "普通角色");
				} else {
					m.put("roleType", "管理员");
				}
			}
		}
		SignatureMap.put("rows", roles);
		SignatureMap.put("total", job.getInt("total"));
		WebUtils.writeErrorJson(response, SignatureMap);
	}

	/**
	 * 添加角色
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
		String deptName = request.getParameter("deptName");
		if (deptName!=null) {
			deptName = java.net.URLEncoder.encode(deptName, "UTF-8");
		}
		String groupName = request.getParameter("groupName");
		String roleType = request.getParameter("roleType");
		String groupId = request.getParameter("groupId");
		groupName = java.net.URLEncoder.encode(groupName, "UTF-8");
		String remark = request.getParameter("remark");
		if (remark!=null) {
			remark = java.net.URLEncoder.encode(remark, "UTF-8");
		}
		String status = request.getParameter("status");
		String treeNodeIds = request.getParameter("temp");
		String resourceIds = "";
		String functionIds = "";
		if (treeNodeIds != null && !treeNodeIds.equals("")) {
			String[] menus = treeNodeIds.split("=");// 菜单组之间分隔
			for (int a = 0; a < menus.length; a++) {
				if (menus[a] != null && !menus[a].equals("")) {
					String[] moduleRes = menus[a].split(",,,");// 模块与模块拆分
					for (int i = 0; i < moduleRes.length; i++) {
						if (moduleRes[i] != null && !moduleRes[i].equals("")) {
							if (moduleRes[i].contains(",,")) {
								String[] mres = moduleRes[i].split(",,");// 模块与资源拆分
								for (int j = 0; j < mres.length; j++) {
									if (mres[j] != null && !mres[j].equals("")) {
										if (!mres[j].contains(",")) {
											resourceIds += mres[j] + ",";
										} else {
											String[] rfun = mres[j].split(",");// 方法与方法拆分
											for (int k = 0; k < rfun.length; k++) {
												if (rfun[k] != null && !rfun[k].equals("")) {
													functionIds += rfun[k] + ",";
												}
											}
										}
									}
								}
							} else if (moduleRes[i].contains(",")) {
								String[] mres = moduleRes[i].split(",");// 模块与资源拆分
								for (int k = 0; k < mres.length; k++) {
									if (mres[k] != null && !mres[k].equals("")) {
										functionIds += mres[k] + ",";
									}
								}
							}
						}
					}
				}
			}
			if (resourceIds != null && !("").equals(resourceIds)) {
				resourceIds = resourceIds.substring(0, resourceIds.length() - 1);
			}
			if (functionIds != null && !("").equals(functionIds)) {
				functionIds = functionIds.substring(0, functionIds.length() - 1);
			}
		}
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("roleName", roleName);
		map.put("status", status);
		map.put("rolePrivilege", resourceIds);
		map.put("privilegeFunId", functionIds);
		map.put("groupId", groupId);
		map.put("groupName", groupName);
		map.put("deptId", "");
		map.put("deptName", deptName);
		map.put("roleLevel", "");
		map.put("roleType", roleType);
		map.put("parentRoleId", "");
		map.put("remark", remark);
		map.put("createUser", "");
		map.put("createUserId", "");

		String s = sendPost(oesPrivilegeDev.getRoleAddUrl(), map);
		JSONObject job = JSONObject.fromObject(s);
		WebUtils.writeErrorJson(response, job);
	}

	/**
	 * 修改角色
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "updateRole")
	public void updateRole(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		log.info("-------------------------update        start------------------------------------");
		String appId = request.getParameter("appId");
		String privilegeRoleId = request.getParameter("privilegeRoleId");
		String roleName = request.getParameter("roleName");
		String roleType = request.getParameter("roleType");
		roleName = java.net.URLEncoder.encode(roleName, "UTF-8");
		String deptName = request.getParameter("deptName");
		if (deptName != null) {
			deptName = java.net.URLEncoder.encode(deptName, "UTF-8");
		}
		String groupName = request.getParameter("groupName");
		if (groupName != null) {
			groupName = java.net.URLEncoder.encode(groupName, "UTF-8");
		}
		String remark = request.getParameter("remark");
		if (remark != null) {
			remark = java.net.URLEncoder.encode(remark, "UTF-8");
		}
		String status = request.getParameter("status");
		String addNodeIds = request.getParameter("addIds");
		String delNodeIds = request.getParameter("delIds");
		String resourceIds = "";
		String functionIds = "";
		String s1 = "";
		String s2 = "";
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("privilegeRoleId", privilegeRoleId);
		map.put("appId", appId);
		map.put("roleName", roleName);
		map.put("status", status);
		map.put("groupId", "");
		map.put("groupName", groupName);
		map.put("deptId", "");
		map.put("deptName", deptName);
		map.put("roleLevel", "");
		map.put("roleType", roleType);
		map.put("parentRoleId", "");
		map.put("remark", remark);
		map.put("createUser", "");
		map.put("createUserId", "");
		if ((addNodeIds != null && !addNodeIds.equals("")) || (delNodeIds != null && !delNodeIds.equals(""))) {
			if (addNodeIds != null && !addNodeIds.equals("")) {// 添加权限
				String[] mres = addNodeIds.split(",,");// 资源与方法拆分
				for (int j = 0; j < mres.length; j++) {
					if (mres[j] != null && !mres[j].equals("")) {
						if (!mres[j].contains(",")) {
							resourceIds += mres[j] + ",";
						} else {
							String[] rfun = mres[j].split(",");
							for (int i = 0; i < rfun.length; i++) {
								if (rfun[i] != null && !rfun[i].equals("")) {
									functionIds += rfun[i] + ",";
								}
							}
						}
					}
				}
				if (resourceIds != null && !("").equals(resourceIds)) {
					resourceIds = resourceIds.substring(0, resourceIds.length() - 1);
				}
				if (functionIds != null && !("").equals(functionIds)) {
					functionIds = functionIds.substring(0, functionIds.length() - 1);
				}
				map.put("rolePrivilege", resourceIds);
				map.put("privilegeFunId", functionIds);
				map.put("method", 0);// 0-添加权限，1-删除权限
				s1 = sendPost(oesPrivilegeDev.getRoleModiUrl(), map);
			}
			resourceIds = "";
			functionIds = "";
			if (delNodeIds != null && !delNodeIds.equals("")) {// 删除权限
				String[] mres = delNodeIds.split(",,");// 资源与方法拆分
				for (int j = 0; j < mres.length; j++) {
					if (mres[j] != null && !mres[j].equals("")) {
						if (!mres[j].contains(",")) {
							resourceIds += mres[j] + ",";
						} else {
							String[] rfun = mres[j].split(",");
							for (int i = 0; i < rfun.length; i++) {
								if (rfun[i] != null && !rfun[i].equals("")) {
									functionIds += rfun[i] + ",";
								}
							}
						}
					}
				}
				if (resourceIds != null && !("").equals(resourceIds)) {
					resourceIds = resourceIds.substring(0, resourceIds.length() - 1);
				}
				if (functionIds != null && !("").equals(functionIds)) {
					functionIds = functionIds.substring(0, functionIds.length() - 1);
				}
				map.put("rolePrivilege", resourceIds);
				map.put("privilegeFunId", functionIds);
				map.put("method", 1);// 0-添加权限，1-删除权限
				s2 = sendPost(oesPrivilegeDev.getRoleModiUrl(), map);
			}
		} else {
			map.put("method", 0);// 如果没有添加或删除权限，默认给一个值
			s1 = sendPost(oesPrivilegeDev.getRoleModiUrl(), map);
		}

		JSONObject job1 = null;
		JSONObject job2 = null;
		Map<String, Object> m = new HashMap<String, Object>();
		if (s1 == null || ("").equals(s1)) {
			s1 = "{'status':'1'}";
		}
		job1 = JSONObject.fromObject(s1);
		if (s2 == null || ("").equals(s2)) {
			s2 = "{'status':'1'}";
		}
		job2 = JSONObject.fromObject(s2);
		if (("1").equals(job1.get("status")) && ("1").equals(job2.get("status"))) {
			m.put("status", "1");
		}
		WebUtils.writeErrorJson(response, m);
	}

	/**
	 * 删除角色
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "deleteRole")
	public void deleteRole(HttpServletRequest request, HttpServletResponse response, String id) {
		String appId = request.getParameter("appId");
		String privilegeRoleId = request.getParameter("id");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("privilegeRoleId", privilegeRoleId);
		map.put("createUser", "");
		map.put("createUserId", "");
		String s = sendPost(oesPrivilegeDev.getRoleDelUrl(), map);
		JSONObject job = JSONObject.fromObject(s);
		WebUtils.writeErrorJson(response, job);
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "QueryRoleDetails")
	public void QueryRoleDetails(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		String privilegeRoleId = request.getParameter("id");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("privilegeRoleId", privilegeRoleId);
		map.put("start", "0");
		map.put("limit", "50");
		map.put("deptId", "");
		map.put("groupId", "");
		String s = sendPost(oesPrivilegeDev.getRolePrivilegeUrl(), map);
		JSONObject o = JSONObject.fromObject(s);
		List<Map<String, Object>> roles = JSONArray.fromObject(o.get("roleList"));
		Map<String, Object> m = roles.get(0);
		List<Map<String, Object>> resources = JSONArray.fromObject(m.get("resourceList"));
		List<Map<String, Object>> functions = JSONArray.fromObject(m.get("functionList"));
		List<Map<String, String>> listMap = new LinkedList<Map<String, String>>();
		Map<String, String> nodeMap = null;
		if (resources.size() > 0) {
			for (Map<String, Object> resMap : resources) {
				nodeMap = new HashMap<String, String>();
				nodeMap.put("menuId", "m" + (String) (resMap.get("menuId")));
				nodeMap.put("resourceId", "r" + (String) (resMap.get("resourceId")));
				nodeMap.put("funcId", "");
				listMap.add(nodeMap);
			}
		}
		if (functions.size() > 0) {
			for (Map<String, Object> funcMap : functions) {
				nodeMap = new HashMap<String, String>();
				nodeMap.put("menuId", "");
				nodeMap.put("resourceId", "r" + (String) funcMap.get("resourceId"));
				nodeMap.put("funcId", "f" + (String) funcMap.get("functionId"));
				listMap.add(nodeMap);
			}
		}
		map.clear();
		map.put("nodeList", listMap);
		JSONObject jsonObj = JSONObject.fromObject(map);
		WebUtils.writeJson(response, jsonObj);
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		String groupId = request.getParameter("groupId");
		String appId = request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("groupId", groupId);
		JSONArray jsonArr = null;
		//获取组织机构缓存
		String s = sendPost(oesPrivilegeDev.getGroupCacheUrl(), map);
		if (s!=null&&!("").equals(s)) {
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
					jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList, functionList, operationList));
					map.put("status", "1");
					map.put("tree", jsonArr);
				} else {
					map.put("status", "0");
					map.put("tree", new JSONArray());
				}
				WebUtils.writeJsonToMap(response, map);
			}else {
				jsonArr = new JSONArray();
				map.put("status", "0");
				map.put("tree", jsonArr);
				WebUtils.writeJsonToMap(response, map);
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
	protected List<TreeNode> buildTree2(List<TreeNode> treeNodes, List<PrivilegeResource1> resourceList,
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
					if ((menuId).equals("m" + res.getMenuId())) {
						entry.getValue().setId("r" + res.getResourceId());
						entry.getValue().setText(res.getResourceName());
						entry.getValue().setIsmodule("1");
						List<TreeNode> treeNodeList1 = new ArrayList<TreeNode>();
						for (PrivilegeFunction func : functionList) {
							TreeNode treeNode1 = new TreeNode();
							if ((res.getResourceId()).equals(func.getResourceId())) {
								treeNode1.setId("f" + func.getFunctionId());
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

	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree(List<TreeNode> treeNodes, List<PrivilegeResource> resourceList) {
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
				// else{
				// 添加三级
				String resource = entry.getValue().getResource();
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				if (resource != null) {
					String[] resourceStrArray = resource.split(",");
					for (int i = 0; i < resourceStrArray.length; i++) {
						String vl = resourceStrArray[i];
						for (int j = 0; j < resourceList.size(); j++) {
							int id = resourceList.get(j).getId();
							String nameValue = resourceList.get(j).getName();
							TreeNode treeNode = new TreeNode();
							if (Integer.parseInt(vl) == id) {
								treeNode.setId(vl);
								treeNode.setText(nameValue);
								// treeNode.setState("closed");
								treeNode.setIsmodule("1");
								treeNodeList.add(treeNode);
							}

						}
					}
				}

				entry.getValue().setChildren(treeNodeList);
				// }
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}

	@RequestMapping(value = "tree1")
	public void getModelTree1(HttpServletRequest request, HttpServletResponse response, String id) {

		PrivilegeRoleDetails privilegeRoleDetails = new PrivilegeRoleDetails();
		privilegeRoleDetails.setRoleId(Integer.parseInt(id));
		List<PrivilegeRoleDetails> privilegeRoleDetailslist = privilegeRoleDetailsService
				.QueryRoleDetails(privilegeRoleDetails);

		List<TreeNode> nodes = privilegeModuleService.getDepartmentTree(privilegeRoleDetailslist);// 见方法02
		// buildTree(nodes)见方法01，return返回值自动转为json,不懂的话看下面紫色部分，懂的略过
		List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findAllResource();
		JSONArray jsonArr = JSONArray.fromObject(buildTree1(nodes, privilegeResourceList, privilegeRoleDetailslist));
		WebUtils.writeJson(response, jsonArr);
	}

	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree1(List<TreeNode> treeNodes, List<PrivilegeResource> resourceList,
			List<PrivilegeRoleDetails> privilegeRoleDetailslist) {
		String[] sourceStrArray = null;
		String[] sourceStrArray1 = null;
		String[] sourceStrArray2 = null;
		StringBuffer module = new StringBuffer();
		StringBuffer moduleRole = new StringBuffer();
		if (privilegeRoleDetailslist != null) {
			for (int i = 0; i < privilegeRoleDetailslist.size(); i++) {
				PrivilegeRoleDetails privilegeRoleDetails = privilegeRoleDetailslist.get(i);
				String resources = privilegeRoleDetails.getResources();
				int moduleId = 0;
				String resourcesArry = null;
				if (resources != null) {
					moduleId = privilegeRoleDetails.getModuleId();
					resourcesArry = privilegeRoleDetails.getResources();
					module.append(moduleId + ",");
					moduleRole.append(resourcesArry + ",,");
				}
			}
		}

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
				// else{
				// 添加三级
				String resource = entry.getValue().getResource();
				String idt = entry.getValue().getId();
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				if (resource != null) {
					String[] resourceStrArray = resource.split(",");
					for (int i = 0; i < resourceStrArray.length; i++) {
						String vl = resourceStrArray[i];
						for (int j = 0; j < resourceList.size(); j++) {
							int id = resourceList.get(j).getId();
							String nameValue = resourceList.get(j).getName();
							TreeNode treeNode = new TreeNode();
							if (Integer.parseInt(vl) == id) {
								treeNode.setId(vl);
								treeNode.setText(nameValue);
								// treeNode.setState("closed");
								treeNode.setIsmodule("1");

								sourceStrArray = module.toString().split(",");
								for (int p = 0; p < sourceStrArray.length; p++) {
									String value = sourceStrArray[p];
									if (value.equals(idt)) {
										sourceStrArray1 = moduleRole.toString().split(",,");
										sourceStrArray2 = sourceStrArray1[p].split(",");
										for (int u = 0; u < sourceStrArray2.length; u++) {
											String vo = sourceStrArray2[u];
											if (vl.equals(vo)) {
												treeNode.setChecked(true);
											}
										}
										// node.setChecked(true);
									}
								}

								treeNodeList.add(treeNode);
							}

						}
					}
				}

				entry.getValue().setChildren(treeNodeList);
				// }
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}
	/**
	 * 查询组织机构
	 * 
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value = "findGroup")
	public void findGroup(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		log.info("-------------------------findGroup         start------------------------------------");
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
		String groupId = user.get("groupId").equals("null") ? null
				: (String) user.get("groupId");
		int Type = (int) user.get("Type");
		List<OesGroup> groupList = null;
		// 用户角色类型，1-普通用户，2-管理员，3-组织机构管理员
		// 如果当前用户为管理员，显示所有组织结构，否则显示当前用户所在的组织机构
		if (Type == 1 || Type == 3) {
			groupList = new ArrayList<OesGroup>();
			OesGroup group = oesGroupService.findByCode(groupId);
			groupList.add(group);
		} else {
			groupList = oesGroupService.findAll();
		}

		JSONArray jsonArr = JSONArray.fromObject(groupList);
		WebUtils.writeJson(response, jsonArr);
		return;
	}
}
