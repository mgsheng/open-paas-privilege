package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
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
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['appId']}")
	private String appId;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-operation-name-uri']}")
	private String getOperationNameUrl;
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
		return "privilege/model/index";
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		String reslut = sendPost(appMenuRedisUrl, map);
		JSONObject obj = JSONObject.fromObject(reslut);// 将json字符串转换为json对象
		JSONArray objArray = JSONArray.fromObject(obj.get("menuList"));
		List<PrivilegeMenu> menuList = JSONArray.toList(objArray, PrivilegeMenu.class);
		String s1 = sendPost(appResRedisUrl, map);
		JSONObject obj1 = new JSONObject().fromObject(s1);// 将json字符串转换为json对象
		JSONArray obj1Array = JSONArray.fromObject(obj1.get("resourceList"));
		JSONArray obj2Array = JSONArray.fromObject(obj1.get("functionList"));
		String operation=sendPost(getAllOperationUrl, map);
		obj=JSONObject.fromObject(operation);
		objArray=JSONArray.fromObject(obj.get("operationList"));
		// 将json对象转换为java对象
		List<PrivilegeOperation> operationList=JSONArray.toList(objArray, PrivilegeOperation.class);
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array, PrivilegeResource1.class);
		List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array, PrivilegeFunction.class);
		JSONArray jsonArr = JSONArray.fromObject(buildTree(menuList, resourceList, functionList,operationList));
		if (request.getParameter("id") != null) {
			jsonArr = new JSONArray();
		}
		WebUtils.writeJson(response, jsonArr);
	}

	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree(List<PrivilegeMenu> menuList, List<PrivilegeResource1> resourceList, List<PrivilegeFunction> functionList,List<PrivilegeOperation> operationList) {
		// 顶级菜单资源集合
		List<TreeNode> results = new ArrayList<TreeNode>();
		
		Map<String, TreeNode> aidMap = new LinkedHashMap<String, TreeNode>();
		List<TreeNode> nodes=null;
		if(menuList!=null){
			nodes=new ArrayList<TreeNode>();
			for (PrivilegeMenu menu : menuList) {
				TreeNode node=null;
				if(menu!=null && menu.getParentId().equals("0")) {
					node=new TreeNode();
					node.setId(String.valueOf(menu.getMenuId()));//菜单ID
					node.setChecked(false);
					node.setText(menu.getMenuName());//菜单名称
					node.setTarget("");
					node.setPid(String.valueOf(menu.getParentId()));//父级部门ID
					node.setResource(menu.getMenuLevel()+"");
					node.setIsmodule("0");
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("menuCode", menu.getMenuCode());
					map.put("menuLevel", menu.getMenuLevel());
					map.put("menuRule", menu.getMenuRule());
					map.put("dislayOrder", menu.getDisplayOrder());
					map.put("parentId", menu.getParentId());
					map.put("status", menu.getDisplayOrder());
					node.setAttributes(map);
				}
				if(node!=null){
					nodes.add(node);
					aidMap.put(node.getId(), node);
				}
			}
		}
		nodes=null;
		Set<Entry<String, TreeNode>> entrySet = aidMap.entrySet();
		for (Entry<String, TreeNode> entry : entrySet) {
			String pid = entry.getValue().getId();
			TreeNode node = aidMap.get(pid);
			List<TreeNode> childrenList = new ArrayList<TreeNode>();
			for (PrivilegeMenu menu : menuList) {
				if (menu.getParentId().equals(pid)) {
					for (PrivilegeResource1 resource : resourceList) {
						TreeNode node1=null;
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
									for(PrivilegeOperation operation:operationList){
										if (optId.equals(operation.getId())) {
											Funnode.setText(operation.getName());
										}
									}
									Funnode.setAttributes(map);
									Funnode.setIsmodule("2");
									if(Funnode!=null){
										childrenList2.add(Funnode);
									}
								}
							}
							node1.setChildren(childrenList2);
						}
						if(node1!=null){
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
		map.put("appId", appId);
		String reslut = sendPost(getAllOperationUrl, map);
		JSONObject jsonObject = JSONObject.fromObject(reslut);
		Map JsnMap = (Map) jsonObject;
		JSONArray jsonArr = JSONArray.fromObject(JsnMap.get("operationList"));
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
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("functionId", functionId);
		Boolean boo = false;
		String reslut = sendPost(delFunctionUrl, map);
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
			Map JsnMap = (Map) jsonObject;
			if (JsnMap.get("status").equals("1")) {
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
		String optUrl = request.getParameter("optUrl");
		String operationId = request.getParameter("operationId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("resourceId", resourceId);
		map.put("optUrl", optUrl);
		map.put("operationId", operationId);
		Boolean boo = false;
		String reslut = sendPost(addFunctionUrl, map);
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
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "addMenu")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String menuName=request.getParameter("name");
		try {
			menuName = java.net.URLEncoder.encode(menuName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	//	String menuName = request.getParameter("name");
		String menuCode = request.getParameter("code");
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
		map2.put("menuLevel", menuLevel);
		Boolean boo = false;
		String menuId = null;
		String reslut = sendPost(addMenuUrl, map2);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (JsnMap.get("status").equals("1")) {
				boo = true;
				menuId = (String) JsnMap.get("menuId");
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		if (!parentId.equals("0")) {
			if (boo) {
				// 添加资源
				map2.put("resourceLevel", "0");
				map2.put("resourceName", menuName);
				map2.put("menuId", menuId);
				map2.put("baseUrl", url);
				// String reourceId = null;
				reslut = sendPost(addResourceUrl, map2);
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
		
		map2.clear();
		
		if (boo) {
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
		/*try {
			menuName = new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String menuCode = request.getParameter("code");
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
		map2.put("menuLevel", menuLevel);
		Boolean boo = false;
		String reslut = sendPost(modifyMenuUrl, map2);
		if (reslut != null) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			Map JsnMap = (Map) jsonObject;
			if (JsnMap.get("status").equals(1)) {
				boo = true;
			} else {
				boo = false;
			}
		} else {
			boo = false;
		}
		if (resourceId!=null&&!("").equals(resourceId)) {
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
		
		map2.clear();
		if (boo) {
			map2.put("returnMsg", "2");
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
	@RequestMapping(value = "detail")
	public void detail(HttpServletRequest request, HttpServletResponse response, String id) {
		// 返回
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!nullEmptyBlankJudge(id)) {
			try {
				PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(Integer.parseInt(id));
				map.put("id", privilegeModule.getId());
				map.put("parentId", privilegeModule.getParentId());
				map.put("name", privilegeModule.getName());
				map.put("url", privilegeModule.getUrl());
				map.put("code", privilegeModule.getCode());
				map.put("displayOrder", privilegeModule.getDisplayOrder());
				map.put("status", privilegeModule.getStatus());
				map.put("resources", privilegeModule.getResources());
				PrivilegeModule parentModule = null;
				if (privilegeModule.getParentId() != 0) {
					parentModule = privilegeModuleService.getModuleById(privilegeModule.getParentId());
					map.put("parentName", parentModule.getName());
				}
				map.put("returnMsg", "1");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("id", 0);
				map.put("returnMsg", "0");
			}
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
		String resourceId=request.getParameter("resourceId");
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
		if(resourceId!=null){
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
	 * 查询模块
	 * 
	 * @param request
	 * @param model
	 * @param bool
	 * @return
	 */
	@RequestMapping(value = "findModuel")
	public void findModuel(HttpServletRequest request, HttpServletResponse response) {
	}

}
