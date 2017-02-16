package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
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

import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.LoadPopertiesFile;
import cn.com.open.pay.platform.manager.tools.WebUtils;


/**
 * 组织机构管理
 * @author admin
 *
 */
@Controller
@RequestMapping("/oesGroup/")
public class OesGroupController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(OesGroupController.class);
	@Autowired
	private OesGroupService oesGroupService;
	/*@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	private Map<String, String> map = LoadPopertiesFile.loadProperties();
	

	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['privilege-get-operation-uri']}")
	private String getAllOperationUrl;
	
	@Value("#{properties['getAllOrganizationByAppkey-uri']}")
	private String getAllOrganizationByAppkeyUrl;*/
	
    /**
     * 跳转到机构管理页面
     * @return Json
     */
    @RequestMapping("index")
    public String groupIndex(HttpServletRequest request,HttpServletResponse response,Model model) {
    	log.info("-------------------------group manager start------------------------------------");
		String appId=request.getParameter("appId");
		model.addAttribute("appId",appId);
		return "privilege/group/index";
    }
    /**
     * 获取应用下机构列表
     * @return Json
     */
    @RequestMapping("getGroupList")
    public void getGroupMessage(HttpServletRequest request,HttpServletResponse response) {
    	log.info("-------------------------getGroupMessage start------------------------------------");
    	Map<String, Object> map = new HashMap<String,Object>();
    	// 当前第几页
		String page = request.getParameter("page");
		String appId = request.getParameter("appId");
		// 每页显示的记录数
		String rows = request.getParameter("rows");
		// 当前页
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1" : page);
		// 每页显示条数
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10" : rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int startRow = (currentPage - 1) * pageSize;
		List<OesGroup> oesGroup = oesGroupService.findAllByPage(startRow,pageSize);
		int count = oesGroupService.findAllCount();
		JSONArray jsonArr = JSONArray.fromObject(oesGroup);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);		
		WebUtils.writeJson(response, jsonObjArr);
		return;
    }
    
    /*@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(appMenuRedisUrl, map);
		JSONObject obj = JSONObject.fromObject(s);// 将json字符串转换为json对象
		JSONArray objArray = JSONArray.fromObject(obj.get("menuList"));
		// 将json对象转换为java对象
		List<PrivilegeMenu> menuList = JSONArray.toList(objArray, PrivilegeMenu.class);
		String s1 = sendPost(appResRedisUrl, map);
		JSONObject obj1 = new JSONObject().fromObject(s1);// 将json字符串转换为json对象
		JSONArray obj1Array = JSONArray.fromObject(obj1.get("resourceList"));
		JSONArray obj2Array = JSONArray.fromObject(obj1.get("functionList"));
		String operation = sendPost(getAllOperationUrl, map);
		obj = JSONObject.fromObject(operation);
		objArray = JSONArray.fromObject(obj.get("operationList"));
		// 将json对象转换为java对象
		List<PrivilegeOperation> operationList = JSONArray.toList(objArray, PrivilegeOperation.class);
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array, PrivilegeResource1.class);
		List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array, PrivilegeFunction.class);

		List<TreeNode> nodes = convertTreeNodeList(menuList);
		JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes, resourceList, functionList, operationList));
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

	*//**
	 * @param departments
	 * @return
	 *//*
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

	*//**
	 * 添加时构建树
	 * 
	 * @param treeNodes
	 * @return
	 *//*
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
	}*/
}
