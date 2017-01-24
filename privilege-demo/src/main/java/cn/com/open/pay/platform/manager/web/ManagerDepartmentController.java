package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeFunction;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeMenu;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeOperation;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource1;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGroupService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 部门管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/department/")
public class ManagerDepartmentController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private ManagerDepartmentService managerDepartmentService;
	
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	
	@Value("#{properties['privilege-group-add-uri']}")
	private String privilegeGroupAddUrl;

	@Value("#{properties['privilege-group-modify-uri']}")
	private String privilegeGroupModifyUrl;

	@Value("#{properties['privilege-group-del-uri']}")
	private String privilegeGroupDelUrl;

	@Value("#{properties['privilege-group-query-uri']}")
	private String privilegeGroupQueryUrl;
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['privilege-operation-name-uri']}")
	private String getOperationNameUrl;

	@Value("#{properties['privilege-groupredis-query-uri']}") 
	private String getGroupredis;
	@Value("#{properties['privilege-get-operation-uri']}")
	private String getAllOperationUrl;
	
	/**
	 * 跳转到所选部门的员工信息页面
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("toDeptUsers")
	public String  deptUsers(HttpServletRequest request,HttpServletResponse response,Model model)throws UnsupportedEncodingException{
		log.info("-------------------------toDeptUsers       start------------------------------------");
		String deptID = request.getParameter("id");
		String deptName = request.getParameter("deptName");
		System.out.println("deptID :"+deptID+"  deptName:"+deptName);
		deptID =( deptID==null?null:new String(deptID.getBytes("iso-8859-1"),"utf-8"));
		deptName =(deptName==null?null:new String(deptName.getBytes("iso-8859-1"),"utf-8"));

		model.addAttribute("deptID",deptID);
		model.addAttribute("deptName", deptName);
		return "department/deptUsers";
	}
	
	/**
	 * 根据封装的User对象中的属性查找所以所选部门的User对象集合
	 * @return
	 */
	@RequestMapping(value="findDeptUsers")
	public void findDeptUsers(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDeptUsers        start------------------------------------");
		
		String deptID = request.getParameter("deptID");
		String deptName =  request.getParameter("deptName");
		if(deptID !=null){
			deptID = new String(deptID.getBytes("iso-8859-1"),"utf-8");
		}
		if(deptName !=null){
			deptName = new String(deptName.getBytes("iso-8859-1"),"utf-8");
		}
		
//		System.out.println("******deptID :   "+deptID+"     deptName:"+deptName);
		//当前第几页
		String page=request.getParameter("page");
//		System.out.println(page);
		
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
//	    System.out.println(rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
	    
		//将请求参数封装到Department对象中
		User user = new User();
		user.setDeptID(Integer.valueOf(deptID));
		user.setDeptName(deptName);
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
		List<User> users = managerDepartmentService.findDeptUsers(user);
		if(users ==null) return;
		int count = managerDepartmentService.findDeptUsersCount(user);
		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	@RequestMapping(value="updateDept")
	public void updateDept(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("utf-8");
		String groupName = request.getParameter("groupName");
		String appId = request.getParameter("appId");
		String status=request.getParameter("status");
//		String treeNodeIds=request.getParameter("temp");
//		String method=request.getParameter("method");
		String resourceIds="";
		String functionIds="";
		String s1="";
		String s2="";
		String addNodeIds=request.getParameter("addIds");
		String delNodeIds=request.getParameter("delIds");
		String groupId = request.getParameter("groupId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("groupId",groupId);
		map.put("groupName", groupName);
		map.put("createUser", "");
		map.put("createUserid", "");
		map.put("status",status );
		if((addNodeIds!=null && !addNodeIds.equals("")) || (delNodeIds!=null && !delNodeIds.equals(""))){
			if(addNodeIds!=null && !addNodeIds.equals("")){//添加权限
				String[] mres=addNodeIds.split(",,");//资源与方法拆分
				for(int j=0;j<mres.length;j++){
					if(mres[j]!=null && !mres[j].equals("")){
						if(!mres[j].contains(",")){
							resourceIds+=mres[j]+",";
						}else{
							String[] rfun=mres[j].split(",");
							for(int i=0;i<rfun.length;i++){
								if(rfun[i]!=null && !rfun[i].equals("")){
									functionIds+=rfun[i]+",";
								}
							}
						}
					}
				}
				if(resourceIds!=null && !("").equals(resourceIds)){
					resourceIds=resourceIds.substring(0, resourceIds.length()-1);
				}
				if(functionIds!=null && !("").equals(functionIds)){
					functionIds=functionIds.substring(0, functionIds.length()-1);
				}
				map.put("groupPrivilege", resourceIds);
				map.put("method", 0);//0-添加权限，1-删除权限
				s1 = sendPost(privilegeGroupModifyUrl,map);
			}	
			resourceIds="";
			functionIds="";
			if(delNodeIds!=null && !delNodeIds.equals("")){//删除权限
				String[] mres=delNodeIds.split(",,");//资源与方法拆分
				for(int j=0;j<mres.length;j++){
					if(mres[j]!=null && !mres[j].equals("")){
						if(!mres[j].contains(",")){
							resourceIds+=mres[j]+",";
						}else{
							String[] rfun=mres[j].split(",");
							for(int i=0;i<rfun.length;i++){
								if(rfun[i]!=null && !rfun[i].equals("")){
									functionIds+=rfun[i]+",";
								}
							}
						}
					}
				}
				if(resourceIds!=null && !("").equals(resourceIds)){
					resourceIds=resourceIds.substring(0, resourceIds.length()-1);
				}
				if(functionIds!=null && !("").equals(functionIds)){
					functionIds=functionIds.substring(0, functionIds.length()-1);
				}
				map.put("groupPrivilege", resourceIds);
				map.put("method", 1);//0-添加权限，1-删除权限
				s2 = sendPost(privilegeGroupModifyUrl,map);
			}
			}else{
				map.put("method", 0);//如果没有添加或删除权限，默认给一个值
				s1 = sendPost(privilegeGroupModifyUrl,map);
			}
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
/*		if(treeNodeIds!=null && !treeNodeIds.equals("")){
			String[] menus=treeNodeIds.split("=");//菜单组之间分隔
			for(int a=0;a<menus.length;a++){
				if(menus[a]!=null && !menus[a].equals("")){
					String[] moduleRes=menus[a].split(",,,");//模块与模块拆分
					for(int i=0;i<moduleRes.length;i++){
						if(moduleRes[i]!=null && !moduleRes[i].equals("")){
							if(moduleRes[i].contains(",,")){
								String[] mres=moduleRes[i].split(",,");//模块与资源拆分
								for(int j=0;j<mres.length;j++){
									if(mres[j]!=null && !mres[j].equals("")){
										if(!mres[j].contains(",")){
											resourceIds+=mres[j]+",";
										}else{
											String[] rfun=mres[j].split(",");//方法与方法拆分
											for(int k=0;k<rfun.length;k++){
												if(rfun[k]!=null && !rfun[k].equals("")){
													functionIds+=rfun[k]+",";
												}
											}
										}
									}
								}
							}else if(moduleRes[i].contains(",")){
								String[] mres=moduleRes[i].split(",");//模块与资源拆分
								for(int k=0;k<mres.length;k++){
									if(mres[k]!=null && !mres[k].equals("")){
										functionIds+=mres[k]+",";
									}
								}
							}
						}
					}
				}
			}
			if(resourceIds!=null && !("").equals(resourceIds)){
				resourceIds=resourceIds.substring(0, resourceIds.length()-1);
			}
			if(functionIds!=null && !("").equals(functionIds)){
				functionIds=functionIds.substring(0, functionIds.length()-1);
			}
		}
*/
		
		JSONObject job1=null;
		JSONObject job2=null;
		Map<String,Object> m =new HashMap<String,Object>();
		if(s1 == null || ("").equals(s1)){
			s1="{'status':'1'}";
		}
		job1=JSONObject.fromObject(s1);
		if(s2 == null || ("").equals(s2)){
			s2="{'status':'1'}";
		}
		job2=JSONObject.fromObject(s2);
		if(("1").equals(job1.get("status")) && ("1").equals(job2.get("status"))){
			m.put("status", "1");
		}
    	WebUtils.writeErrorJson(response, m);
	}
	
	
	/**
	 * 根据ID删除部门
	 * @return
	 */
	@RequestMapping(value="removeDeptByID")
	public void removeDeptByID(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------removeDeptByID       start------------------------------------");
		String appId=request.getParameter("appId");
		String groupId = request.getParameter("id");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("groupId", groupId);
		map.put("createUser", "");
		map.put("createUserId", "");
		String s = sendPost(privilegeGroupDelUrl,map);
		JSONObject job=JSONObject.fromObject(s);
    	WebUtils.writeErrorJson(response, job);
    /*	boolean result = false;
		JSONObject jsonobj = new JSONObject();
    	if(groupId != null && groupId !=""){
			result = managerDepartmentService.removeDeptByID(Integer.valueOf(groupId));
		}
		jsonobj.put("result",result);*/
	}
	
	
	/**
	 * 添加部门
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="addDept")
	public void addDept(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addDept        start------------------------------------");
		
		String groupName =URLDecoder.decode(request.getParameter("groupName"),"UTF-8");
		groupName=URLEncoder.encode(groupName,"UTF-8");
		
		String appId = request.getParameter("appId");
		String status=request.getParameter("status");
		String treeNodeIds=request.getParameter("temp");
		String resourceIds="";
		String functionIds="";
		if(treeNodeIds!=null && !treeNodeIds.equals("")){
			String[] menus=treeNodeIds.split("=");//菜单组之间分隔
			for(int a=0;a<menus.length;a++){
				if(menus[a]!=null && !menus[a].equals("")){
					String[] moduleRes=menus[a].split(",,,");//模块与模块拆分
					for(int i=0;i<moduleRes.length;i++){
						if(moduleRes[i]!=null && !moduleRes[i].equals("")){
							if(moduleRes[i].contains(",,")){
								String[] mres=moduleRes[i].split(",,");//模块与资源拆分
								for(int j=0;j<mres.length;j++){
									if(mres[j]!=null && !mres[j].equals("")){
										if(!mres[j].contains(",")){
											resourceIds+=mres[j]+",";
										}else{
											String[] rfun=mres[j].split(",");//方法与方法拆分
											for(int k=0;k<rfun.length;k++){
												if(rfun[k]!=null && !rfun[k].equals("")){
													functionIds+=rfun[k]+",";
												}
											}
										}
									}
								}
							}else if(moduleRes[i].contains(",")){
								String[] mres=moduleRes[i].split(",");//模块与资源拆分
								for(int k=0;k<mres.length;k++){
									if(mres[k]!=null && !mres[k].equals("")){
										functionIds+=mres[k]+",";
									}
								}
							}
						}
					}
				}
			}
			if(resourceIds!=null && !("").equals(resourceIds)){
				resourceIds=resourceIds.substring(0, resourceIds.length()-1);
			}
			if(functionIds!=null && !("").equals(functionIds)){
				functionIds=functionIds.substring(0, functionIds.length()-1);
			}
		}
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("groupId",getUUID());
		map.put("groupName", groupName);
		map.put("groupPrivilege", resourceIds);
		map.put("createUser", "");
		map.put("createUserid", "");
		map.put("status",status );
		
		String s = sendPost(privilegeGroupAddUrl,map);
		JSONObject job=JSONObject.fromObject(s);
    	WebUtils.writeErrorJson(response, job);
    	/*//判断数据库是否已经存在该部门
    			boolean result = false;
    			Department dept_db = managerDepartmentService.findByDeptName(deptName);
    			if(dept_db != null){
    				// result = false表示该部门已被注册
    				jsonObjArr.put("result", result);
    			    WebUtils.writeJson(response,jsonObjArr);
    				return;
    			}
    			//将参数封装到Department对象中
    			Date createTime = new Date();
    			Department department = new Department();
    			department.setDeptName(deptName);
    			department.setCreateTime(createTime);
    			
    			result = managerDepartmentService.addDept(department);
    			// result = true 表示添加成功
    			jsonObjArr.put("result", result);
    			WebUtils.writeJson(response,jsonObjArr);    	
    	*/
	}
	/**
	 * 跳转到部门信息列表的页面
	 * @return
	 */
	@RequestMapping(value="departmentList")
	public String departmentList(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------departmentList       start------------------------------------");
		String appId=request.getParameter("appId");
		model.addAttribute("appId",appId);
		return "department/departmentList";
	}
	
	/**
	 * 根据封装的Department对象中的属性查找所以符合条件的Department对象
	 * @return
	 */
	@RequestMapping(value="findDepts")
	public void findDepts(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDepts        start------------------------------------");
		
		String deptName = request.getParameter("dept_name");
		String appId=request.getParameter("appId");
		if(deptName !=null ){
			deptName = new String(deptName.getBytes("iso-8859-1"),"utf-8");
		}
//		System.out.println(deptName);
		//当前第几页
		String page=request.getParameter("page");
//		System.out.println(page);
		
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
//	    System.out.println(rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
	    
		//将请求参数封装到Department对象中
		Department department = new Department();
		department.setDeptName(deptName);
		department.setPageSize(pageSize);
		department.setStartRow(startRow);
		List<Department> departments = managerDepartmentService.findDepts(department);
		int count = managerDepartmentService.findQueryCount(department);
		System.out.println(count);
		JSONArray jsonArr = JSONArray.fromObject(departments);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}

	@RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String appId = request.getParameter("appId");
		map.put("appId", appId);
		String groupId = request.getParameter("groupId");
		if(nullEmptyBlankJudge(groupId)){
			groupId = "wstest111";
		}
		map.put("groupId", groupId);
		String reslut = sendPost(getGroupredis, map);
		JSONArray jsonArr = null ;
		JSONObject obj = JSONObject.fromObject(reslut);// 将json字符串转换为json对象
		if(obj.get("status")==null){
			JSONArray objArray = JSONArray.fromObject(obj.get("menuList"));
			JSONArray obj1Array = JSONArray.fromObject(obj.get("resourceList"));
			//获取functionList
			String s1 = sendPost(appResRedisUrl, map);
			JSONObject obj1 = new JSONObject().fromObject(s1);// 将json字符串转换为json对象
			JSONArray obj2Array = JSONArray.fromObject(obj1.get("functionList"));
			// 将json对象转换为java对象
			List<PrivilegeMenu> menuList = JSONArray.toList(objArray, PrivilegeMenu.class);
			List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array, PrivilegeResource1.class);
			List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array, PrivilegeFunction.class);
			
			if(menuList!=null&&resourceList!=null){
				jsonArr= JSONArray.fromObject(buildTree(menuList, resourceList, functionList));
				System.err.println(jsonArr.toString());
				if (request.getParameter("id") != null) {
					jsonArr = new JSONArray();
				}	
		   }
		}else{
			jsonArr = new JSONArray();
		}
		WebUtils.writeJson(response, jsonArr);
	}
	  /**
     * 
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "privilegeGroupQuery")
	public void QueryRoleDetails(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map = new HashMap<String, Object>();
		String appId = request.getParameter("appId");
		map.put("appId", appId);
		String groupId = request.getParameter("groupId");
		if(nullEmptyBlankJudge(groupId)){
			groupId = "wstest111";
		}
		map.put("groupId", groupId);
		String reslut = sendPost(getGroupredis, map);
		JSONArray jsonArr = null ;
		JSONObject obj = JSONObject.fromObject(reslut);// 将json字符串转换为json对象
		if(obj.get("status")==null){
			JSONArray objArray = JSONArray.fromObject(obj.get("menuList"));
			List<Map<String,Object>> resources = JSONArray.fromObject(obj.get("resourceList"));
		List<Map<String,String>> listMap = new LinkedList<Map<String,String>>();
		Map<String,String> nodeMap = null;
		if(resources.size()>0){
			for(Map<String,Object> resMap:resources){
				nodeMap = new HashMap<String,String>();
				nodeMap.put("menuId", "m"+(String)(resMap.get("menuId")));
				nodeMap.put("resourceId", "r"+(String)(resMap.get("resourceId")));
				nodeMap.put("funcId", "");
				listMap.add(nodeMap);
			}
		}
		map.clear();
		map.put("nodeList", listMap);
		}else{
			map.clear();
			map.put("status", "0");
		}
		
		JSONObject jsonObj = JSONObject.fromObject(map);
		WebUtils.writeJson(response,jsonObj);
    }
	/**
	 * 构建树
	 * 
	 * @param treeNodes
	 * @return
	 */
	protected List<TreeNode> buildTree(List<PrivilegeMenu> menuList, List<PrivilegeResource1> resourceList,
			List<PrivilegeFunction> functionList) {
		// 顶级菜单资源集合
		List<TreeNode> results = new ArrayList<TreeNode>();
		List<PrivilegeResource1> pMenus = new ArrayList<PrivilegeResource1>();
		TreeNode fNode = new TreeNode();
		fNode.setId("0");
		fNode.setState("closed");
		fNode.setText("菜单");
		fNode.setIsmodule("00");
		List<TreeNode> cList = new ArrayList<TreeNode>();
		fNode.setChildren(cList);
		TreeNode treeNode = null;
		for (PrivilegeMenu menu : menuList) {
			String menuId = menu.getMenuId();
			String menuname = menu.getMenuName();
			List<TreeNode> childrenList = null;
			if (menu.getParentId().equals("0")) {
				treeNode = new TreeNode();
				treeNode.setId(menuId);
				treeNode.setText(menuname);
				treeNode.setIsmodule("0");
				treeNode.setState("closed");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("menuCode", menu.getMenuCode());
				map.put("menuLevel", menu.getMenuLevel());
				map.put("menuRule", menu.getMenuRule());
				map.put("dislayOrder", menu.getDisplayOrder());
				map.put("parentId", menu.getParentId());
				map.put("status", menu.getDisplayOrder());
				treeNode.setAttributes(map);
				cList.add(treeNode);
				// results.add(treeNode);
				childrenList = new ArrayList<TreeNode>();
				treeNode.setChildren(childrenList);
			}

			for (PrivilegeMenu menu2 : menuList) {
				if (menu2.getParentId().equals(menu.getMenuId())) {
					for (PrivilegeResource1 resource1 : resourceList) {
						if (resource1.getMenuId().equals(menu2.getMenuId())) {
							TreeNode node = new TreeNode();
							List<TreeNode> childrenList2 = new ArrayList<TreeNode>();
							Map<String, Object> resourceMap = new HashMap<String, Object>();
							resourceMap.put("baseUrl", resource1.getBaseUrl());
							resourceMap.put("menuId", menu2.getMenuId());
							resourceMap.put("menuCode", menu2.getMenuCode());
							resourceMap.put("menuLevel", menu2.getMenuLevel());
							resourceMap.put("menuRule", menu2.getMenuRule());
							resourceMap.put("parentId", menu.getParentId());
							resourceMap.put("dislayOrder", menu2.getDisplayOrder());
							resourceMap.put("status", menu2.getDisplayOrder());
							node.setAttributes(resourceMap);
							node.setId(resource1.getResourceId());
							node.setText(resource1.getResourceName());
							node.setIsmodule("1");
							childrenList.add(node);
							/*for (PrivilegeFunction function : functionList) {
								if (function.getResourceId().equals(resource1.getResourceId())) {
									TreeNode Funnode = new TreeNode();
									Funnode.setId(function.getFunctionId());
									String optId = function.getOptId();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("optId", optId);
									String s = sendPost(getOperationNameUrl, map);
									map.put("optUrl", function.getOptUrl());
									// map.put("operationId",
									// function.getOptId());
									Funnode.setAttributes(map);
									JSONObject o = JSONObject.fromObject(s);
									String nameValue = o.getString("optName");
									Funnode.setText(nameValue);
									Funnode.setIsmodule("2");
									childrenList2.add(Funnode);
								}
							}*/
							//node.setChildren(childrenList2);

						}
					}
				}
			}
		}
		results.add(fNode);
		return results;
	}
	@RequestMapping(value = "buildTree2")
	public void getResourceTrees2(HttpServletRequest request,HttpServletResponse response) {
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
		String operation=sendPost(getAllOperationUrl, map);
		obj=JSONObject.fromObject(operation);
		objArray=JSONArray.fromObject(obj.get("operationList"));
		//将json对象转换为java对象
		List<PrivilegeOperation> operationList=JSONArray.toList(objArray, PrivilegeOperation.class);		
		List<PrivilegeResource1> resourceList = JSONArray.toList(obj1Array,PrivilegeResource1.class);
		List<PrivilegeFunction> functionList = JSONArray.toList(obj2Array,PrivilegeFunction.class);
		
		List<TreeNode> nodes = convertTreeNodeList(menuList);
		JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes,resourceList,functionList,operationList));
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
				node.setId("m"+String.valueOf(privilegeMenu.getMenuId()));//菜单ID
				node.setIsmodule("0");
				node.setChecked(false);
				node.setText(privilegeMenu.getMenuName());//菜单名称
				node.setTarget("");
				node.setResource("");
				node.setPid("m"+String.valueOf(privilegeMenu.getParentId()));//父级菜单ID
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
		protected List<TreeNode> buildTree2(List<TreeNode> treeNodes,List<PrivilegeResource1> resourceList,List<PrivilegeFunction> functionList,List<PrivilegeOperation> operationList) {
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
					for(PrivilegeResource1 res:resourceList){
						if((menuId).equals("m"+res.getMenuId())){
							entry.getValue().setId("r"+res.getResourceId());
							entry.getValue().setText(res.getResourceName());
							entry.getValue().setIsmodule("1");
						//	List<TreeNode> treeNodeList1 = new ArrayList<TreeNode>();
						/*	for(PrivilegeFunction func:functionList){
								TreeNode treeNode1=new TreeNode();
								if((res.getResourceId()).equals(func.getResourceId())){
									treeNode1.setId("f"+func.getFunctionId());
									treeNode1.setIsmodule("2");
									for(PrivilegeOperation operation:operationList){
										if (func.getOptId().equals(operation.getId())) {
											treeNode1.setText(operation.getName());
										}
									}
									treeNodeList1.add(treeNode1);
								}
							}
							entry.getValue().setChildren(treeNodeList1);*/
						}
					}
					children.add(entry.getValue());
				}
			}
			aidMap = null;

			return results;
		}
		
		
}
