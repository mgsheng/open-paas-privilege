package cn.com.open.pay.platform.manager.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
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
	private static final Logger log = LoggerFactory.getLogger(PrivilegeModuleController.class);
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	/**
	 * 跳转模块管理页面
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "index")
	public String stats(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------index      start------------------------------------");
		String appId = request.getParameter("appId");
		model.addAttribute("appId", appId);
		return "privilege/model/index";
	}
	
	/**
	 * 跳转模块图标页面
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "icon")
	public String icon(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("-------------------------index      start------------------------------------");
		String appId = request.getParameter("appId");
		String menuRule = request.getParameter("menuRule");
		String menuId = request.getParameter("menuId");
		String parentId = request.getParameter("parentId");
		String status = request.getParameter("status");
		String menuLevel = request.getParameter("menuLevel");
		String dislayOrder = request.getParameter("dislayOrder");
		model.addAttribute("appId", appId);
		model.addAttribute("menuRule", menuRule);
		model.addAttribute("menuId", menuId);
		model.addAttribute("parentId", parentId);
		model.addAttribute("status", status);
		model.addAttribute("menuLevel", menuLevel);
		model.addAttribute("dislayOrder", dislayOrder);
		return "privilege/model/icon";
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------tree      start------------------------------------");
		String appId = request.getParameter("appId");
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonArr=null;
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
			map.put("status", "1");
			map.put("tree", jsonArr);
			WebUtils.writeJsonToMap(response, map);
			return;
		}
		map.put("appId", appId);
		//获取应用菜单缓存
		String reslut = sendPost(oesPrivilegeDev.getAppMenuRedisUrl(), map);
		if (reslut!= null && !("").equals(reslut)) {
			JSONObject obj = JSONObject.fromObject(reslut);
			JSONArray menuArray = (JSONArray) obj.get("menuList");
			if (menuArray.size()>0) {
				List<PrivilegeMenu> menuList = JSONArray.toList(menuArray, PrivilegeMenu.class);
				reslut = sendPost(oesPrivilegeDev.getAppResRedisUrl(), map);
				obj = JSONObject.fromObject(reslut);// 将json字符串转换为json对象
				JSONArray resourceArray = (JSONArray) obj.get("resourceList");
				JSONArray functionArray = (JSONArray) obj.get("functionList");
				reslut = sendPost(oesPrivilegeDev.getAllOperationUrl(), map);
				obj = JSONObject.fromObject(reslut);
				JSONArray operationArray = (JSONArray) obj.get("operationList");
				// 将json对象转换为java对象
				List<PrivilegeOperation> operationList = JSONArray.toList(operationArray, PrivilegeOperation.class);
				List<PrivilegeResource1> resourceList = JSONArray.toList(resourceArray, PrivilegeResource1.class);
				List<PrivilegeFunction> functionList = JSONArray.toList(functionArray, PrivilegeFunction.class);
				//根据菜单的displayOrder排序，由小到大
				java.util.Collections.sort(menuList, new Comparator<PrivilegeMenu>() {
		            @Override
		            public int compare(PrivilegeMenu menu1, PrivilegeMenu menu2) {
		                return menu1.getDisplayOrder()-menu2.getDisplayOrder();
		            }
		        });
				List<TreeNode> nodes = convertTreeNodeList(menuList);
				jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList, functionList, operationList));
				map.put("status", "1");
				map.put("tree", jsonArr);
			}else {
				map.put("status", "0");
				map.put("tree", new JSONArray());
			}
		}
		WebUtils.writeJsonToMap(response, map);
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
					node.setState("closed");
				}
				String menuId = entry.getValue().getId();
				for (PrivilegeResource1 res : resourceList) {
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
					}
				}
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}

	
	/**
	 * 获取所有操作名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getAllOperation")
	public void getAllOperation(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------getAllOperation      start------------------------------------");
		Map<String, Object> map = new HashMap<String, Object>();
		String reslut = sendPost(oesPrivilegeDev.getAllOperationUrl(), map);
		JSONObject jsonObject = JSONObject.fromObject(reslut);
		JSONArray jsonArr = (JSONArray) jsonObject.get("operationList");
		WebUtils.writeJson(response, jsonArr);
		return;
	}
	
	/**
	 * 设置模块图标
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateIcon")
	public void updateIcon(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId").trim();
		String menuId = request.getParameter("menuId").trim();//菜单Id
		String menuRule = request.getParameter("menuRule");//菜单图标路径
		String parentId = request.getParameter("parentId").trim();//父Id
		String status = request.getParameter("status");//状态
		String displayOrder = request.getParameter("displayOrder");//排序
		String menuLevel = request.getParameter("menuLevel");//层级
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("menuId", menuId);
		map.put("menuRule", menuRule);
		map.put("parentId", parentId);
		map.put("status", status);
		map.put("dislayOrder", displayOrder);
		map.put("menuLevel", menuLevel);
		String reslut = sendPost(oesPrivilegeDev.getModifyMenuUrl(), map);
		JSONObject jsonObject = JSONObject.fromObject(reslut);
		WebUtils.writeJson(response, jsonObject);
	}
	/**
	 * 修改模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateModule")
	public void updateModule(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------detail       start------------------------------------");
		String appId = request.getParameter("appId").trim();
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
			reslut = sendPost(oesPrivilegeDev.getModifyFunctionUrl(), map);
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
			String url = request.getParameter("url").trim();
			String menuLevel = request.getParameter("menuLevel");
			String menuRule = request.getParameter("menuRule").trim();
			String menuName = request.getParameter("name").trim();
			String menuCode = request.getParameter("code").trim();
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
			map.put("menuRule", menuRule);
			map.put("status", status);
			map.put("dislayOrder", displayOrder);
			map.put("menuLevel", Integer.parseInt(menuLevel) + 1);
			reslut = sendPost(oesPrivilegeDev.getModifyMenuUrl(), map);
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
					reslut = sendPost(oesPrivilegeDev.getModifyResourceUrl(), map);
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
	 * 添加模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addModuel")
	public void addModuel(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------addModuel   start------------------------------------");
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
			reslut = sendPost(oesPrivilegeDev.getAddFunctionUrl(), map);
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
			String menuRule = request.getParameter("menuRule");
			String status = request.getParameter("status");
			String displayOrder = request.getParameter("displayOrder");
			String url = request.getParameter("url");
			String menuLevel = request.getParameter("menuLevel");
			map.put("appId", appId);
			map.put("menuName", menuName);
			map.put("menuCode", menuCode);
			map.put("parentId", parentId);
			map.put("menuRule", menuRule);
			map.put("status", status);
			map.put("dislayOrder", displayOrder);
			map.put("menuLevel", Integer.parseInt(menuLevel) + 1);
			String menuId = null;
			reslut = sendPost(oesPrivilegeDev.getAddMenuUrl(), map);
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
					reslut = sendPost(oesPrivilegeDev.getAddResourceUrl(), map);
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
	public void deleteModuel(HttpServletRequest request, HttpServletResponse response) {
		log.info("-------------------------delModuel   start------------------------------------");
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
			reslut = sendPost(oesPrivilegeDev.getDelFunctionUrl(), map);
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
		if (resourceId != null && !("").equals(resourceId)) {
			map.put("resourceId", resourceId);
			reslut = sendPost(oesPrivilegeDev.getDelResourceUrl(), map);
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
		if (menuId != null && !("").equals(menuId)) {
			map.put("menuId", menuId);
			reslut = sendPost(oesPrivilegeDev.getDelMenuUrl(), map);
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
		map.clear();
		if (boo) {
			map.put("returnMsg", "1");
		} else {
			map.put("returnMsg", "0");
		}
		WebUtils.writeErrorJson(response, map);
	}

}
