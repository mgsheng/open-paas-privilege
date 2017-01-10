package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
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
	@Value("#{properties['privilege-approleredis-query-uri']}")
	private String getAppRoleRedis;
	@Value("#{properties['appId']}")
	private String appId;
	@Value("#{properties['add-privilege-user-uri']}")
	private String addPrivilegeUserUrl;

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
		String id = request.getParameter("id");
		String role = request.getParameter("role");
		String userName = request.getParameter("userName");
		Boolean boo = false;
		JSONObject jsonobj = new JSONObject();
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
				boo = true;
				userRoleList = (List<Map<String, Object>>) JsnMap.get("roleList");
			} else {
				boo = false;
				//如果用户不存在  添加用户
				if (("10002").equals(String.valueOf(JsnMap.get("error_code")))) {
					Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
					map.put("appId", appId);
					map.put("appUserId", id);
					map.put("privilegeRoleId", role);
					map.put("appUserName", userName);
					reslut = sendPost(addPrivilegeUserUrl, map);
					if (reslut != null && !("").equals(reslut)) {
						jsonObject = JSONObject.fromObject(reslut);
						JsnMap = (Map) jsonObject;
						if (!("0").equals(JsnMap.get("status"))) {
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
					}else {
						boo=false;
					}
				}
				System.err.println("该用户没有角色");
			}
		}else {
			boo=false;
		}
		// 用户当前角色ids
		StringBuffer oldRoleIds = new StringBuffer();
		if (userRoleList != null&&userRoleList.size()>0) {
			for (Map<String, Object> map : userRoleList) {
				oldRoleIds.append(map.get("privilegeRoleId"));
				oldRoleIds.append(",");
			}
		}
		if (oldRoleIds.length() > 0 && boo) {
			// 删除用户原有角色
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", id);
			signature.put("method", "1");
			signature.put("privilegeRoleId", oldRoleIds.subSequence(0, oldRoleIds.length() - 1).toString());
			reslut = sendPost(moditUserPrivilegeUrl, signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				Map JsnMap = (Map) jsonObject;
				if (!("0").equals(JsnMap.get("status"))) {
					System.err.println("删除成功");
					boo = true;
				} else {
					boo = false;
					System.err.println("删除失败");
				}
			}else {
				boo=false;
			}
		}

		id = (id == null ? null : new String(id.getBytes("iso-8859-1"), "utf-8"));
		if (role == null || role == "") {
			role = null;
		} else {
			role = new String(role.getBytes("iso-8859-1"), "utf-8");
		}

		if (role != null && boo) {
			// 添加现在的角色
			Map<String, Object> signature = privilegeGetSignatureService.getSignature(appId);
			signature.put("appId", appId);
			signature.put("appUserId", id);
			signature.put("method", "0");
			signature.put("privilegeRoleId", role);
			reslut = sendPost(moditUserPrivilegeUrl, signature);
			if (reslut != null && !("").equals(reslut)) {
				JSONObject jsonObject = JSONObject.fromObject(reslut);
				Map JsnMap = (Map) jsonObject;
				if (!("0").equals(JsnMap.get("status"))) {
					System.err.println("添加成功");
					boo = true;
				} else {
					System.err.println("添加失败");
					boo = false;
				}
			}else {
				boo = false;
			}
		}
		System.out.println("****************id:" + id + "****************role:" + role);
		// 添加日志
		/*
		 * PrivilegeModule privilegeModule =
		 * privilegeModuleService.getModuleById(55); PrivilegeModule
		 * privilegeModule1 =
		 * privilegeModuleService.getModuleById(privilegeModule.getParentId());
		 * String towLevels= privilegeModule.getName(); String oneLevels =
		 * privilegeModule1.getName(); String operator = "管理员"; //操作人 String
		 * operatorId = "1"; //操作人Id PrivilegeResource privilegeResource =
		 * privilegeResourceService.findByCode("shouquan"); if(boo = true){
		 * privilegeLogService.addPrivilegeLog(operator,privilegeResource.
		 * getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+
		 * "给"+""+"用户授权成功",operatorId); }else{
		 * privilegeLogService.addPrivilegeLog(operator,privilegeResource.
		 * getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+
		 * "给"+""+"用户授权失败",operatorId); }
		 */
		// result = true表示该用户授权角色成功
		
		jsonobj.put("result", boo);
		WebUtils.writeJson(response, jsonobj);
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
