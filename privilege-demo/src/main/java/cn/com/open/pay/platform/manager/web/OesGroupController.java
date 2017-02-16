package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;	

	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['privilege-get-operation-uri']}")
	private String getAllOperationUrl;
	@Value("#{properties['privilege-group-modify-uri']}")
	private String groupPrivilegeModifyUrl;
	@Value("#{properties['privilege-group-query-uri']}")
	private String groupPrivilegeQueryUrl;
	
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
    
    /**
     * 添加机构
     * @return Json
     */
    @RequestMapping("addGroup")
    public void addGroup(HttpServletRequest request,HttpServletResponse response,Model model) {
    	log.info("-------------------------addGroup start------------------------------------");
    	String groupCode = request.getParameter("groupCode");
    	String groupTypeName = request.getParameter("groupTypeName");
    	String groupName = request.getParameter("groupName");
    	String groupType = request.getParameter("groupType");
    	String result;
    	OesGroup group = oesGroupService.findByCode(groupCode);
    	if(group==null){
    		OesGroup g=new OesGroup();
    		g.setGroupCode(groupCode);
    		g.setGroupName(groupName);
    		g.setGroupTypeName(groupTypeName);
    		g.setGroupType(groupType);
    		Boolean f = oesGroupService.saveGroup(g);
    		if(f){
    			result="0";//add success
    		}else{
    			result="1";//add field
    		}
    	}else{
    		result="2";//group exist
    	}	
    	Map<String, Object> map = new HashMap<String,Object>();
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
		String groupId = request.getParameter("groupId");
		String groupName = request.getParameter("groupName");
		String appId=request.getParameter("appId");
		model.addAttribute("groupId", groupId);
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
		public void getRes(HttpServletRequest request, HttpServletResponse response)
				throws UnsupportedEncodingException {
			log.info("-------------------------getRes       start------------------------------------");
			String groupId = request.getParameter("groupId");
			String appId = request.getParameter("appId");
			Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
			Signature.put("groupId", groupId);
			Signature.put("appId",appId);
			Signature.put("start",0);
			Signature.put("limit",10);
			String reslut = sendPost(groupPrivilegeQueryUrl, Signature);
			String[] resourceIds = null;
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				if (!("0").equals(jsonObject.get("status"))) {
					// 机构资源表中 资源 
					List<Map<String,Object>> gList = JSONArray.fromObject(jsonObject.get("groupList"));
					String privilegeResId = (String) (gList.get(0).equals("null") ? ""
							: gList.get(0).get("groupPrivilege"));
					if (!("").equals(privilegeResId)) {
						resourceIds = privilegeResId.split(",");
					}
				} else {
					System.err.println("该机构没有资源");
				}
			}

			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("resourceIds", resourceIds);
			System.err.println(JSONObject.fromObject(aMap).toString());
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
		String groupId = request.getParameter("groupId");
		String resource = request.getParameter("resource");
		String appId=request.getParameter("appId");
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
		Map<String, Object> Signature = privilegeGetSignatureService.getSignature(appId);
		Signature.put("appId", appId);
		Signature.put("groupId", groupId);
		Signature.put("groupPrivilege", resource);
		Signature.put("method", "0");
		String reslut = sendPost(groupPrivilegeModifyUrl, Signature);
		if (reslut != null && !("").equals(reslut)) {
			JSONObject jsonObject = JSONObject.fromObject(reslut);
			if (!("0").equals(jsonObject.get("status"))) {
				boo = true;
			} else {
				boo = false;
			}
		}
		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
		return;
	}
    
    @RequestMapping(value = "tree")
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
}
