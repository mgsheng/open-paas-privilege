package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

/**
 * 模块管理基础类
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/module/")
public class PrivilegeModuleController extends BaseControllerUtil {
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['appId']}")
	private String appId;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-get-operation-uri']}")
	private String getAllOperationUrl;
	@Value("#{properties['privilege-del-function-uri']}")
	private String delFunctionUrl;
	@Value("#{properties['privilege-add-function-uri']}")
	private String addFunctionUrl;
	@Value("#{properties['privilege-modify-function-uri']}")
	private String modifyFunctionUrl;
	@Value("#{properties['privilege-menu-add-uri']}")
	private String addMenuUrl;
	@Value("#{properties['privilege-menu-modi-uri']}")
	private String modifyMenuUrl;
	@Value("#{properties['privilege-menu-del-uri']}")
	private String delMenuUrl;
	@Value("#{properties['privilege-add-resource-uri']}")
	private String addResourceUrl;
	@Value("#{properties['privilege-modify-resource-uri']}")
	private String modifyResourceUrl;
	@Value("#{properties['privilege-del-resource-uri']}")
	private String delResourceUrl;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;

	/**
	 * 跳转模块管理页面
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "index")
	public String stats(HttpServletRequest request, HttpServletResponse response, Model model) {
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "privilege/model/index";
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		String reslut = sendPost(appMenuRedisUrl, map);
		JSONObject obj = JSONObject.fromObject(reslut);// 将json字符串转换为json对象
		JSONArray objArray = (JSONArray) obj.get("menuList");
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
		JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList, functionList, operationList));
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
		}
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
			map.put("status", privilegeMenu.getStatus());
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
					node.setState("close");
				}
				String menuId = entry.getValue().getId();
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				for (PrivilegeResource1 res : resourceList) {
					// TreeNode treeNode=new TreeNode();
					if ((menuId).equals(res.getMenuId())) {
						Map<String, Object> resourceMap = new HashMap<String, Object>();
						resourceMap.put("baseUrl", res.getBaseUrl());
						resourceMap.put("menuId", entry.getValue().getId());
						resourceMap.put("menuCode", entry.getValue().getAttributes().get("menuCode"));
						resourceMap.put("menuLevel", entry.getValue().getAttributes().get("menuLevel"));
						resourceMap.put("menuRule", entry.getValue().getAttributes().get("menuRule"));
						resourceMap.put("parentId", entry.getValue().getPid());
						resourceMap.put("dislayOrder", entry.getValue().getAttributes().get("dislayOrder"));
						resourceMap.put("status", entry.getValue().getAttributes().get("status"));
						entry.getValue().setAttributes(resourceMap);
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
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("optId", func.getOptId());
								map.put("optUrl", func.getOptUrl());
								treeNode1.setAttributes(map);
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

	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree(List<PrivilegeMenu> menuList, List<PrivilegeResource1> resourceList,
			List<PrivilegeFunction> functionList, List<PrivilegeOperation> operationList) {
		// 顶级菜单资源集合
		List<TreeNode> results = new ArrayList<TreeNode>();

		Map<String, TreeNode> aidMap = new LinkedHashMap<String, TreeNode>();
		List<TreeNode> nodes = null;
		if (menuList != null) {
			nodes = new ArrayList<TreeNode>();
			for (PrivilegeMenu menu : menuList) {
				TreeNode node = null;
				if (menu != null && menu.getParentId().equals("0")) {
					// if(menu!=null){
					node = new TreeNode();
					node.setId(String.valueOf(menu.getMenuId()));// 菜单ID
					node.setChecked(false);
					node.setText(menu.getMenuName());// 菜单名称
					node.setTarget("");
					node.setPid(String.valueOf(menu.getParentId()));// 父级部门ID
					node.setResource(menu.getMenuLevel() + "");
					node.setIsmodule("0");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("menuCode", menu.getMenuCode());
					map.put("menuLevel", menu.getMenuLevel());
					map.put("menuRule", menu.getMenuRule());
					map.put("dislayOrder", menu.getDisplayOrder());
					map.put("parentId", menu.getParentId());
					map.put("status", menu.getDisplayOrder());
					node.setAttributes(map);
				}
				if (node != null) {
					nodes.add(node);
					aidMap.put(node.getId(), node);
				}
			}
		}
		nodes = null;
		Set<Entry<String, TreeNode>> entrySet = aidMap.entrySet();
		for (Entry<String, TreeNode> entry : entrySet) {
			String pid = entry.getValue().getId();
			TreeNode node = aidMap.get(pid);
			List<TreeNode> childrenList = new ArrayList<TreeNode>();
			for (PrivilegeMenu menu : menuList) {
				if (menu.getParentId().equals(pid)) {
					for (PrivilegeResource1 resource : resourceList) {
						TreeNode node1 = null;
						if (resource.getMenuId().equals(menu.getMenuId())) {
							node1 = new TreeNode();
							List<TreeNode> childrenList2 = new ArrayList<TreeNode>();
							Map<String, Object> resourceMap = new HashMap<String, Object>();
							resourceMap.put("baseUrl", resource.getBaseUrl());
							resourceMap.put("menuId", menu.getMenuId());
							resourceMap.put("menuCode", menu.getMenuCode());
							resourceMap.put("menuLevel", menu.getMenuLevel());
							resourceMap.put("menuRule", menu.getMenuRule());
							resourceMap.put("parentId", menu.getParentId());
							resourceMap.put("dislayOrder", menu.getDisplayOrder());
							resourceMap.put("status", menu.getDisplayOrder());
							node1.setAttributes(resourceMap);
							node1.setId(resource.getResourceId());
							node1.setText(resource.getResourceName());
							node1.setState("closed");
							node1.setIsmodule("1");
							for (PrivilegeFunction function : functionList) {
								if (function.getResourceId().equals(resource.getResourceId())) {
									TreeNode Funnode = new TreeNode();
									Funnode.setId(function.getFunctionId());
									String optId = function.getOptId();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("optId", optId);
									map.put("optUrl", function.getOptUrl());
									for (PrivilegeOperation operation : operationList) {
										if (optId.equals(operation.getId())) {
											Funnode.setText(operation.getName());
										}
									}
									Funnode.setAttributes(map);
									Funnode.setIsmodule("2");
									if (Funnode != null) {
										childrenList2.add(Funnode);
									}
								}
							}
							node1.setChildren(childrenList2);
						}
						if (node1 != null) {
							childrenList.add(node1);
						}
					}
				}
			}
			node.setChildren(childrenList);
			results.add(node);
		}
		aidMap = null;
		return results;
	}

	@RequestMapping(value = "getAllOperation")
	public void getAllOperation(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reslut = sendPost(getAllOperationUrl, map);
		JSONObject jsonObject = JSONObject.fromObject(reslut);
		JSONArray jsonArr = (JSONArray) jsonObject.get("operationList");
		String str = jsonArr.toString();
		System.err.println(str);
		WebUtils.writeJson(response, str);
		return;
	}

	/**
	 * 删除资源功能
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "delFunction")
	public void delFunction(HttpServletRequest request, HttpServletResponse response) {
		String functionId = request.getParameter("functionId");
		String appId = request.getParameter("appId");
		if (("undefined").equals(functionId)) {
			return;
		}
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("functionId", functionId);
		Boolean boo = false;
		String reslut = sendPost(delFunctionUrl, map);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (jsonObject.get("status").equals("1")) {
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}

	/**
	 * 修改资源功能
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "editFunction")
	public void editFunction(HttpServletRequest request, HttpServletResponse response) {
		String functionId = request.getParameter("functionId");
		String appId = request.getParameter("appId");
		String optUrl = request.getParameter("optUrl");
		String operationId = request.getParameter("operationId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("functionId", functionId);
		map.put("optUrl", optUrl);
		map.put("operationId", operationId);
		Boolean boo = false;
		String reslut = sendPost(modifyFunctionUrl, map);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (jsonObject.get("status").equals("1")) {
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}

	/**
	 * 添加资源功能
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "addFunction")
	public void addFunction(HttpServletRequest request, HttpServletResponse response) {
		String resourceId = request.getParameter("resourceId");
		String appId = request.getParameter("appId");
		String optUrl = request.getParameter("optUrl");
		String operationId = request.getParameter("operationId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("resourceId", resourceId);
		map.put("optUrl", optUrl);
		map.put("operationId", operationId);
		Boolean boo = false;
		String reslut = sendPost(addFunctionUrl, map);
		String functionId = null;
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (jsonObject.get("status").equals("1")) {
				functionId = (String) jsonObject.get("functionId");
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
			map.put("functionId", functionId);
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}

	/**
	 * 添加模块
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "addMenu")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String menuName = request.getParameter("name");
		String appId = request.getParameter("appId");
		String menuCode = request.getParameter("code");
		try {
			menuName = java.net.URLEncoder.encode(menuName, "UTF-8");
			menuCode = java.net.URLEncoder.encode(menuCode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String parentId = request.getParameter("parentId");
		String status = request.getParameter("status");
		String displayOrder = request.getParameter("displayOrder");
		String url = request.getParameter("url");
		String menuLevel = request.getParameter("menuLevel");
		Map<String, Object> map2 = privilegeGetSignatureService.getSignature(appId);
		map2.put("appId", appId);
		map2.put("menuName", menuName);
		map2.put("menuCode", menuCode);
		map2.put("parentId", parentId);
		map2.put("status", status);
		map2.put("dislayOrder", displayOrder);
		map2.put("menuLevel", Integer.parseInt(menuLevel) + 1);
		Boolean boo = false;
		String menuId = null;
		String reslut = sendPost(addMenuUrl, map2);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (jsonObject.get("status").equals("1")) {
				boo = true;
				menuId = (String) jsonObject.get("menuId");
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		String reourceId = null;
		if (!url.equals("")) {// 添加带url的menu
			if (boo) {
				// 添加资源
				map2.put("resourceLevel", "0");
				map2.put("resourceName", menuName);
				map2.put("menuId", menuId);
				map2.put("baseUrl", url);
				reslut = sendPost(addResourceUrl, map2);
				if (reslut != null) {
					JSONObject jsonObject = JSONObject.fromObject(reslut);
					if (jsonObject.get("status").equals("1")) {
						reourceId = (String) jsonObject.get("resourceId");
						boo = true;
					} else {
						boo = false;
					}
				} else {
					boo = false;
				}
			}
		}

		map2.clear();

		if (boo) {
			map2.put("menuId", menuId);
			map2.put("resourceId", reourceId);
			map2.put("returnMsg", "1");
		} else {
			map2.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map2);
	}

	/**
	 * 编辑模块
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "edit")
	public void edit(HttpServletRequest request, HttpServletResponse response) {
		String menuName = request.getParameter("name");
		String menuCode = request.getParameter("code");
		String appId = request.getParameter("appId");
		try {
			menuName = java.net.URLEncoder.encode(menuName, "UTF-8");
			menuCode = java.net.URLEncoder.encode(menuCode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String menuId = request.getParameter("menuId");
		String resourceId = request.getParameter("resourceId");
		String parentId = request.getParameter("parentId");
		String status = request.getParameter("status");
		String displayOrder = request.getParameter("displayOrder");
		String url = request.getParameter("url");
		String menuLevel = request.getParameter("menuLevel");
		Map<String, Object> map2 = privilegeGetSignatureService.getSignature(appId);
		map2.put("appId", appId);
		map2.put("menuId", menuId);
		map2.put("menuName", menuName);
		map2.put("menuCode", menuCode);
		map2.put("parentId", parentId);
		map2.put("status", status);
		map2.put("dislayOrder", displayOrder);
		map2.put("menuLevel", Integer.parseInt(menuLevel) + 1);
		Boolean boo = false;
		String reslut = sendPost(modifyMenuUrl, map2);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (jsonObject.get("status").equals(1)) {
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		if (resourceId != null && !("").equals(resourceId) && !("undefined").equals(resourceId)) {
			if (boo) {
				// 修改
				map2.put("resourceName", menuName);
				map2.put("menuId", menuId);
				map2.put("baseUrl", url);
				map2.put("resourceId", resourceId);
				// String reourceId = null;
				reslut = sendPost(modifyResourceUrl, map2);
				if (reslut != null) {
					JSONObject jsonObject = JSONObject.fromObject(reslut);
					if (jsonObject.get("status").equals("1")) {
						boo = true;
					} else {
						boo = false;
					}
				} else {
					boo = false;
				}
			}
		}

		map2.clear();
		if (boo) {
			map2.put("returnMsg", "2");
		} else {
			map2.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map2);

	}

	/**
	 * 修改模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "detail")
	public void detail(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		String optUrl = request.getParameter("optUrl");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String reslut = null;
		Boolean boo = false;
		//optUrl为空 修改菜单  不为空修改功能
		if (optUrl != null && !("").equals(optUrl)) {
			String operationId = request.getParameter("operationId");
			String functionId = request.getParameter("functionId");
			map.put("optUrl", optUrl);
			map.put("functionId", functionId);
			map.put("operationId", operationId);
			reslut = sendPost(modifyFunctionUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (jsonObject.get("status").equals("1")) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		} else {
			String menuId = request.getParameter("menuId");
			String parentId = request.getParameter("parentId");
			String status = request.getParameter("status");
			String displayOrder = request.getParameter("displayOrder");
			String url = request.getParameter("url");
			String menuLevel = request.getParameter("menuLevel");
			String menuName = request.getParameter("name");
			String menuCode = request.getParameter("code");
			String resourceId = request.getParameter("resourceId");
			try {
				if (menuName != null) {
					menuName = java.net.URLEncoder.encode(menuName, "UTF-8");
				}
				if (menuCode != null) {
					menuCode = java.net.URLEncoder.encode(menuCode, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			map.put("menuId", menuId);
			map.put("menuName", menuName);
			map.put("menuCode", menuCode);
			map.put("parentId", parentId);
			map.put("status", status);
			map.put("dislayOrder", displayOrder);
			map.put("menuLevel", Integer.parseInt(menuLevel) + 1);
			reslut = sendPost(modifyMenuUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (jsonObject.get("status").equals(1)) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
			if (resourceId != null && !("").equals(resourceId) && !("undefined").equals(resourceId)) {
				if (boo) {
					// 修改
					map.put("resourceName", menuName);
					map.put("menuId", menuId);
					map.put("baseUrl", url);
					map.put("resourceId", resourceId);
					reslut = sendPost(modifyResourceUrl, map);
					if (reslut != null) {
						JSONObject jsonObject = JSONObject.fromObject(reslut);
						if (jsonObject.get("status").equals("1")) {
							boo = true;
						} else {
							boo = false;
						}
					} else {
						boo = false;
					}
				}
			}
		}
		map.clear();
		if (boo) {
			map.put("returnMsg", "2");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);

	}

	/**
	 * 删除模块
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "deleteMenu")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String resourceId = request.getParameter("resourceId");
		String appId = request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("menuId", request.getParameter("menuId"));
		Boolean boo = false;
		String reslut = sendPost(delMenuUrl, map);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (JsnMap.get("status").equals("1")) {
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		if (resourceId != null && !("undefined").equals(resourceId)) {
			if (boo) {
				map.put("resourceId", request.getParameter("resourceId"));
				reslut = sendPost(delResourceUrl, map);
				if (reslut != null) {
					JSONObject jsonObject = JSONObject.fromObject(reslut);
					Map JsnMap = (Map) jsonObject;
					if (JsnMap.get("status").equals("1")) {
						boo = true;
					} else {
						boo = false;
					}
				} else {
					boo = false;
				}
			}
		}

		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}
	/**
	 * 添加模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addModuel")
	public void addModuel(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		String optUrl = request.getParameter("optUrl");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String reslut = null;
		Boolean boo = false;
		//如果optUrl为空 则添加菜单  不为空添加功能
		if (optUrl != null && !("").equals(optUrl)) {
			String operationId = request.getParameter("operationId");
			String resourceId = request.getParameter("resourceId");
			String functionId=null;
			map.put("optUrl", optUrl);
			map.put("resourceId", resourceId);
			map.put("operationId", operationId);
			reslut = sendPost(addFunctionUrl, map);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (jsonObject.get("status").equals("1")) {
					boo = true;
					functionId =(String) jsonObject.get("functionId");
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
			map.clear();
			if (boo) {
				map.put("returnMsg", "1");
				map.put("functionId", functionId);
			} else {
				map.put("returnMsg", "0");
			}
			WebUtils.writeErrorJson(response, map);
		} else {
			String menuName = request.getParameter("name");
			String menuCode = request.getParameter("code");
			try {
				if (menuName!=null) {
					menuName = java.net.URLEncoder.encode(menuName, "UTF-8");
				}
				if (menuCode!=null) {
					menuCode = java.net.URLEncoder.encode(menuCode, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String parentId = request.getParameter("parentId");
			String status = request.getParameter("status");
			String displayOrder = request.getParameter("displayOrder");
			String url = request.getParameter("url");
			String menuLevel = request.getParameter("menuLevel");
			map.put("appId", appId);
			map.put("menuName", menuName);
			map.put("menuCode", menuCode);
			map.put("parentId", parentId);
			map.put("status", status);
			map.put("dislayOrder", displayOrder);
			map.put("menuLevel", Integer.parseInt(menuLevel) + 1);
			String menuId = null;
			reslut = sendPost(addMenuUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (jsonObject.get("status").equals("1")) {
					boo = true;
					menuId = (String) jsonObject.get("menuId");
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
			String reourceId = null;
			if (!url.equals("")) {// 添加带url的menu
				if (boo) {
					// 添加资源
					map.put("resourceLevel", "0");
					map.put("resourceName", menuName);
					map.put("menuId", menuId);
					map.put("baseUrl", url);
					reslut = sendPost(addResourceUrl, map);
					if (reslut != null) {
						JSONObject jsonObject = JSONObject.fromObject(reslut);
						if (jsonObject.get("status").equals("1")) {
							reourceId = (String) jsonObject.get("resourceId");
							boo = true;
						} else {
							boo = false;
						}
					} else {
						boo = false;
					}
				}
			}
			map.clear();
			if (boo) {
				map.put("menuId", menuId);
				map.put("resourceId", reourceId);
				map.put("returnMsg", "1");
			} else {
				map.put("returnMsg", "0");
			}
			WebUtils.writeErrorJson(response, map);
		}
	}
	/**
	 * 删除模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteModuel")
	public void findModuel(HttpServletRequest request, HttpServletResponse response) {
		String resourceId = request.getParameter("resourceId");
		String appId = request.getParameter("appId");
		String functionId = request.getParameter("functionId");
		String menuId = request.getParameter("menuId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String reslut = null;
		Boolean boo = false;
		if (functionId != null && !("").equals(functionId)) {
			map.put("functionId", functionId);
			reslut = sendPost(delFunctionUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				Map JsnMap = (Map) jsonObject;
				if (JsnMap.get("status").equals("1")) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		if (resourceId != null && !("").equals(resourceId)) {
			map.put("resourceId", resourceId);
			reslut = sendPost(delResourceUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				Map JsnMap = (Map) jsonObject;
				if (JsnMap.get("status").equals("1")) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		if (menuId != null && !("").equals(menuId)) {
			map.put("menuId", menuId);
			reslut = sendPost(delMenuUrl, map);
			if (reslut != null) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				Map JsnMap = (Map) jsonObject;
				if (JsnMap.get("status").equals("1")) {
					boo = true;
				} else {
					boo = false;
				}
			} else {
				boo = false;
			}
		}
		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}

}
