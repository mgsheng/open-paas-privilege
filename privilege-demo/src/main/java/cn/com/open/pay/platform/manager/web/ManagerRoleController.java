package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

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

import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
@Controller
@RequestMapping("/managerRole/")
public class ManagerRoleController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private PrivilegeRoleService roleService;
	@Autowired
	private PrivilegeRoleDetailsService privilegeRoleDetailsService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeLogService privilegeLogService;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Value("#{properties['privilege-approle-redis-query-uri']}")
	private String appRoleRedisUrl;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['privilege-role-del-uri']}")
	private String roleDelUrl;
	@Value("#{properties['privilege-role-add-uri']}")
	private String roleAddUrl;
	@Value("#{properties['privilege-operation-name-uri']}")
	private String getOperationNameUri;
	
	
	/**
	 * 跳转到查询角色的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="roleMessage")
	public String showSearch(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------showSearch         start------------------------------------");
		String appId=request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(appRoleRedisUrl,map);
		JSONObject job=JSONObject.fromObject(s);
		model.addAttribute("appId",appId);
		model.addAttribute("roleList", job.get("roleList"));
		return "privilege/role/roleMessage";
	}
	
	/**
	 * 根据条件实现查询角色操作
	 * @param request
	 * @param response
	 * @param model
	 * @return  查询完毕后返回查询结果
	 */
	@RequestMapping("QueryRoleMessage")
	public String QueryRoleMessage(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------Search         start------------------------------------");
		String appId=request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(appRoleRedisUrl,map);
		JSONObject job=JSONObject.fromObject(s);
		model.addAttribute("appId",appId);
		model.addAttribute("roleList", job.get("roleList"));
		return "privilege/role/roleMessage";
	}
	
	
	 /**
     * 添加角色
     * @param request
     * @param model
     * @param bool
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "addRole")
	public void addRole(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
    	log.info("-------------------------add         start------------------------------------");
		String appId=request.getParameter("appId");
		String roleName=request.getParameter("roleName");
		String status=request.getParameter("status");
		String treeNodeIds=request.getParameter("temp");
		String resourceIds="";
		String functionIds="";
		if(treeNodeIds!=null && !treeNodeIds.equals("")){
			String[] moduleRes=treeNodeIds.split(",,,");//模块与模块拆分
			for(int i=0;i<moduleRes.length;i++){
				if(moduleRes[i]!=null && !moduleRes[i].equals("") && moduleRes[i].contains(",,")){
					String[] mres=moduleRes[i].split(",,");//模块与资源拆分
					if(mres[0]!=null && !mres[0].equals("")){
						resourceIds+=mres[0]+",";
						if(mres.length>1 && mres[1]!=null && !mres[1].equals("")){
							functionIds=mres[1].replace(",", ",");
						}
					}
				}
			}
		}
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("roleName", roleName);
		map.put("status", status);
		map.put("rolePrivilege", resourceIds);
		map.put("privilegeFunId", functionIds);
		map.put("groupId", "");
		map.put("groupName", "");
		map.put("deptId", "");
		map.put("deptName", "");
		map.put("roleLevel", "");
		map.put("roleType", "");
		map.put("parentRoleId", "");
		map.put("remark", "");
		map.put("createUser", "");
		map.put("createUserId", "");
		
		String s = sendPost(roleAddUrl,map);
		JSONObject job=JSONObject.fromObject(s);
    	
    	/*String  id= request.getParameter("id");
    	String  status= request.getParameter("status");
    	String  roleTreeNodeIds= request.getParameter("temp");

    	Map<String,Object> map = new HashMap<String, Object>();
    	String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
    	//添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(82);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String  towLevels = privilegeModule.getName();
		String  oneLevels = privilegeModule1.getName();
		User user1 = (User)request.getSession().getAttribute("user");
		String operator = user1.getUsername(); //操作人
		String operatorId = user1.getId()+""; //操作人Id
		
    	if(id==""){
    		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("add");
			List <PrivilegeRole> list= roleService.findByName(name);
			if(list!=null&& list.size()>0){
				map.put("returnMsg", "0");
			}else{
				PrivilegeRole privilegeRole=new PrivilegeRole();
				privilegeRole.setName(name);
				privilegeRole.setCreateTime(new Date());
				if(nullEmptyBlankJudge(status)){
					status="0";
				}
				privilegeRole.setStatus(Integer.parseInt(status));
				roleService.savePrivilegeRole(privilegeRole);
				int roleId = privilegeRole.getId(); //获取角色id
				if(roleTreeNodeIds!=null && !roleTreeNodeIds.equals("")){
					PrivilegeRoleDetails roleDetails=null;//角色权限实体
					String[] moduleRes=roleTreeNodeIds.split(",,,");//模块与模块拆分
					for(int i=0;i<moduleRes.length;i++){
						if(moduleRes[i]!=null && !moduleRes[i].equals("")){
							String[] mres=moduleRes[i].split(",,");//模块与资源拆分
							if(mres[0]!=null && !mres[0].equals("")){
								roleDetails=new PrivilegeRoleDetails();
								roleDetails.setRoleId(roleId);
								roleDetails.setModuleId(Integer.parseInt(mres[0]));
								if(mres.length>1 && mres[1]!=null && !mres[1].equals("")){
									roleDetails.setResources(mres[1].replace(",", ","));
								}
								privilegeRoleDetailsService.savePrivilegeRole(roleDetails);
							}
						}
					}
				}
				privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+"添加‘"+name+"’角色",operatorId);
				map.put("returnMsg", "1");
			}
    	}else{
    		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("update");
    		PrivilegeRole privilegeRole=new PrivilegeRole();
    		privilegeRole.setId(Integer.parseInt(id));
    		privilegeRole.setName(name);
    		privilegeRole.setStatus(Integer.parseInt(status));
    		roleService.updatePrivilegeRole(privilegeRole);
    		privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+"修改‘"+name+"’角色",operatorId);
    		map.put("returnMsg", "2");
    	}*/
    	WebUtils.writeErrorJson(response, job);
    }
    
    
    /**
     * 修改角色
     * @param request
     * @param model
     * @param bool
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "updateRole")
	public void updateRole(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
    	String  id= request.getParameter("id");
    	String  status= request.getParameter("status");
    	String  roleTreeNodeIds= request.getParameter("temp");

    	Map<String,Object> map = new HashMap<String, Object>();
    	String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
    	
		PrivilegeRole privilegeRole=new PrivilegeRole();
		privilegeRole.setId(Integer.parseInt(id));
		privilegeRole.setName(name);
		privilegeRole.setStatus(Integer.parseInt(status));
		roleService.updatePrivilegeRole(privilegeRole);
		privilegeRoleDetailsService.deletePrivilegeRoleDetail(id);
		if(roleTreeNodeIds!=null && !roleTreeNodeIds.equals("")){
			PrivilegeRoleDetails roleDetails=null;//角色权限实体
			String[] moduleRes=roleTreeNodeIds.split(",,,");//模块与模块拆分
			for(int i=0;i<moduleRes.length;i++){
				if(moduleRes[i]!=null && !moduleRes[i].equals("")){
					String[] mres=moduleRes[i].split(",,");//模块与资源拆分
					if(mres[0]!=null && !mres[0].equals("")){
						roleDetails=new PrivilegeRoleDetails();
						roleDetails.setRoleId(Integer.parseInt(id));
						roleDetails.setModuleId(Integer.parseInt(mres[0]));
						if(mres.length>1 && mres[1]!=null && !mres[1].equals("")){
							roleDetails.setResources(mres[1].replace(",", ","));
						}
						privilegeRoleDetailsService.savePrivilegeRole(roleDetails);
					}
				}
			}
		}
		//添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(82);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String towLevels = privilegeModule.getName();
		String  oneLevels = privilegeModule1.getName();
		User user1 = (User)request.getSession().getAttribute("user");
		String operator = user1.getUsername(); //操作人
		String operatorId = user1.getId()+""; //操作人Id
		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("update");
		privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+"修改‘"+name+"’角色",operatorId);
		map.put("returnMsg", "2");
    	WebUtils.writeErrorJson(response, map);
    }
    
    /**
     * 删除角色
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "deleteRole")
	public void deleteRole(HttpServletRequest request,HttpServletResponse response,String id) {
		String appId=request.getParameter("appId");
		String privilegeRoleId = request.getParameter("id");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("privilegeRoleId", privilegeRoleId);
		map.put("createUser", "");
		map.put("createUserId", "");
		String s = sendPost(roleDelUrl,map);
		JSONObject job=JSONObject.fromObject(s);
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
	public void QueryRoleDetails(HttpServletRequest request,HttpServletResponse response) {
    	
    	String appId=request.getParameter("appId");
    	String privilegeRoleId=request.getParameter("id");
    	List<Map<String,String>> listMap = new LinkedList<Map<String,String>>();
    	PrivilegeRoleDetails privilegeRoleDetails=new PrivilegeRoleDetails();
    	//privilegeRoleDetails.setRoleId(Integer.parseInt(id));
    	List<PrivilegeRoleDetails> list = privilegeRoleDetailsService.QueryRoleDetails(privilegeRoleDetails);
//    	String id = request.getParameter("id");
    	if(list!=null && list.size()>0){
			Map<String,String> map = null;
			for(PrivilegeRoleDetails roleDetail : list){
				map = new HashMap<String,String>();
				map.put("id", roleDetail.getId()+"");
				map.put("moduleId", roleDetail.getModuleId()+"");
				map.put("resources", roleDetail.getResources()==null?"":roleDetail.getResources());
				listMap.add(map);
			}
		}
		//返回
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("list", listMap);
//		JsonUtil.writeJson(response,JsonUtil.getContentData(map));
		JSONArray jsonArr = JSONArray.fromObject(map);
		WebUtils.writeJson(response,jsonArr);
		
	
    }
    
    @RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request,HttpServletResponse response) {
		String appId=request.getParameter("appId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		String s = sendPost(appMenuRedisUrl,map);
		JSONObject obj = JSONObject.fromObject(s);//将json字符串转换为json对象
		JSONArray objArray=JSONArray.fromObject(obj.get("menuList"));
		//将json对象转换为java对象
		List<PrivilegeMenu> menuList = JSONArray.toList(objArray,PrivilegeMenu.class);
		String s1 = sendPost(appResRedisUrl,map);
		JSONObject obj1 = new JSONObject().fromObject(s1);//将json字符串转换为json对象
		JSONArray obj1Array=JSONArray.fromObject(obj1.get("resourceList"));
		JSONArray obj2Array=JSONArray.fromObject(obj1.get("functionList"));
		//将json对象转换为java对象
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array,PrivilegeResource1.class);
		List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array,PrivilegeFunction.class);
		List<TreeNode> nodes = convertTreeNodeList(menuList);
		JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes,resourceList,functionList));
		WebUtils.writeJson(response,jsonArr);
    }
    private List<TreeNode> convertTreeNodeList(List<PrivilegeMenu> modules) {
		List<TreeNode> nodes = null;
		if(modules != null){
			nodes = new ArrayList<TreeNode>();
			for(PrivilegeMenu menu:modules){
				TreeNode node = convertTreeNode(menu);
				if(node != null){
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
	private TreeNode convertTreeNode(PrivilegeMenu privilegeMenu){
		TreeNode node = null;
		if(privilegeMenu != null){
			node = new TreeNode();
			node.setId(String.valueOf(privilegeMenu.getMenuId()));//菜单ID
			node.setIsmodule("0");
			node.setChecked(false);
			node.setText(privilegeMenu.getMenuName());//菜单名称
			node.setTarget("");
			node.setResource("");
			node.setPid(String.valueOf(privilegeMenu.getParentId()));//父级菜单ID
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
    
	/**
	* 添加时构建树
	* @param treeNodes
	* @return
	*/
	protected List<TreeNode> buildTree2(List<TreeNode> treeNodes,List<PrivilegeResource1> resourceList,List<PrivilegeFunction> functionList) {
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
				List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
				for(PrivilegeResource1 res:resourceList){
					TreeNode treeNode=new TreeNode();
					if((menuId).equals(res.getMenuId())){
						treeNode.setId(res.getResourceId());
						treeNode.setText(res.getResourceName());
						treeNode.setIsmodule("1");
						List<TreeNode> treeNodeList1 = new ArrayList<TreeNode>();
						for(PrivilegeFunction func:functionList){
							String optId=func.getOptId();								
							Map<String, Object> map = new HashMap<String,Object>();
							map.put("optId", optId);
							String s = sendPost(getOperationNameUri,map);
							JSONObject o = JSONObject.fromObject(s);
							String nameValue = o.getString("optName");
							TreeNode treeNode1=new TreeNode();
							if((res.getResourceId()).equals(func.getResourceId())){
								treeNode1.setId(func.getOptId());
								treeNode1.setText(nameValue);
								treeNode1.setIsmodule("2");
							}
							treeNodeList1.add(treeNode1);
						}
						treeNode.setChildren(treeNodeList1);
						treeNodeList.add(treeNode);
					}
				}
				entry.getValue().setChildren(treeNodeList);
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}
	
	
    /**
	* 构建树
	* @param treeNodes
	* @return
	*/
	protected List<TreeNode> buildTree(List<TreeNode> treeNodes,List<PrivilegeResource> resourceList ) {
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
//				else{
					//添加三级
					String resource = entry.getValue().getResource();
					List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
					if(resource!=null){
						String[] resourceStrArray = resource.split(",");
						for(int i=0;i<resourceStrArray.length;i++){
							String vl = resourceStrArray[i];
							for(int j=0;j<resourceList.size();j++){
								int id = resourceList.get(j).getId();
								String nameValue = resourceList.get(j).getName();
								TreeNode treeNode=new TreeNode();
								if(Integer.parseInt(vl)==id){
									treeNode.setId(vl);
									treeNode.setText(nameValue);
//									treeNode.setState("closed");
									treeNode.setIsmodule("1");
									treeNodeList.add(treeNode);
								}
								
							}
						}
					}
					
					entry.getValue().setChildren(treeNodeList);
//				}
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}

	
	 @RequestMapping(value = "tree1")
		public void getModelTree1(HttpServletRequest request,HttpServletResponse response,String id) {
		 
		 
		 	PrivilegeRoleDetails privilegeRoleDetails=new PrivilegeRoleDetails();
	    	privilegeRoleDetails.setRoleId(Integer.parseInt(id));
	    	List<PrivilegeRoleDetails> privilegeRoleDetailslist = privilegeRoleDetailsService.QueryRoleDetails(privilegeRoleDetails);
		 
			List<TreeNode> nodes = privilegeModuleService.getDepartmentTree(privilegeRoleDetailslist);//见方法02
			 //buildTree(nodes)见方法01，return返回值自动转为json,不懂的话看下面紫色部分，懂的略过
			List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findAllResource();
			JSONArray jsonArr = JSONArray.fromObject(buildTree1(nodes,privilegeResourceList,privilegeRoleDetailslist));
	    	WebUtils.writeJson(response,jsonArr);
		}
	    
	    
	    /**
		* 构建树
		* @param treeNodes
		* @return
		*/
		protected List<TreeNode> buildTree1(List<TreeNode> treeNodes,List<PrivilegeResource> resourceList ,List<PrivilegeRoleDetails> privilegeRoleDetailslist ) {
			String[] sourceStrArray = null;
			String[] sourceStrArray1 = null;
			String[] sourceStrArray2 = null;
			StringBuffer module=new StringBuffer();
			StringBuffer moduleRole=new StringBuffer();
			if(privilegeRoleDetailslist!=null){
				for(int i=0;i<privilegeRoleDetailslist.size();i++){
					PrivilegeRoleDetails privilegeRoleDetails = privilegeRoleDetailslist.get(i);
					String resources = privilegeRoleDetails.getResources();
					int moduleId=0;
					String resourcesArry = null;
					if(resources!=null){
						moduleId = privilegeRoleDetails.getModuleId();
						resourcesArry = privilegeRoleDetails.getResources();
					    module.append(moduleId+",");
					    moduleRole.append(resourcesArry+",,");
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
//					else{
						//添加三级
						String resource = entry.getValue().getResource();
						String idt = entry.getValue().getId();
						List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
						if(resource!=null){
							String[] resourceStrArray = resource.split(",");
							for(int i=0;i<resourceStrArray.length;i++){
								String vl = resourceStrArray[i];
								for(int j=0;j<resourceList.size();j++){
									int id = resourceList.get(j).getId();
									String nameValue = resourceList.get(j).getName();
									TreeNode treeNode=new TreeNode();
									if(Integer.parseInt(vl)==id){
										treeNode.setId(vl);
										treeNode.setText(nameValue);
//										treeNode.setState("closed");
										treeNode.setIsmodule("1");
										
										sourceStrArray = module.toString().split(",");
										for(int p=0;p<sourceStrArray.length;p++){
											String value = sourceStrArray[p];
											if(value.equals(idt)){
												sourceStrArray1  =	moduleRole.toString().split(",,");
												sourceStrArray2 = sourceStrArray1[p].split(",");
												for(int u=0;u<sourceStrArray2.length;u++){
													String vo = sourceStrArray2[u];
													if(vl.equals(vo)){
														treeNode.setChecked(true);
													}
												}
//												node.setChecked(true);
											}
										}
										
										treeNodeList.add(treeNode);
									}
									
								}
							}
						}
						
						entry.getValue().setChildren(treeNodeList);
//					}
					children.add(entry.getValue());
				}
			}
			aidMap = null;

			return results;
		}
	
	

	
}
