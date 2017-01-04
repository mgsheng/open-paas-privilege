package cn.com.open.pay.platform.manager.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.dev.PayManagerDev;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.Manager;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.service.ManagerService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import net.sf.json.JSONObject;

/**
 * 用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private PayManagerDev payManagerDev;
	@Autowired
	private PrivilegeRoleDetailsService privilegeRoleDetailsService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;

	/**
	 * 登录验证
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 */
	@RequestMapping("loginVerify")
	public void verify(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		log.info("-----------------------login start----------------");
		boolean flag = false;
		String errorCode = "";
		User user = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		user = checkUsername(username, userService);
		if (user != null) {
			if (user.checkPasswod(password)) {
				flag = true;
				errorCode = "ok";
				// Manager manager=managerService.getManagerById(user.getId());
				// int role=0;
				// List<PrivilegeRoleDetails> list=null;
				// List<PrivilegeModule> modules=null;
				// if(manager!=null){
				// role=manager.getRole();
				// list=privilegeRoleDetailsService.findRoleDetailsByRoleId(role);
				// List<Integer>ids=new ArrayList<Integer>(list.size());
				// for(int i=0;i<list.size();i++){
				// ids.add(list.get(i).getModuleId());
				// }
				// if(ids!=null&&list.size()>0){
				// modules= privilegeModuleService.findModuleByIds(ids);
				// for(int j=0;j<modules.size();j++){
				//
				// }
				// }
				// }
				HttpSession session = request.getSession();
				// session.setAttribute("serverHost",payManagerDev.getServer_host());
				// session.setAttribute("manager",manager);
				// session.setAttribute("modules",modules);
				session.setAttribute("user", user);

			} else {
				errorCode = "error";
			}

		} else {
			errorCode = "error";
		}
		map.put("flag", flag);
		map.put("errorCode", errorCode);
		WebUtils.writeJsonToMap(response, map);
	}

	/**
	 * 登录跳转
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		String username = request.getParameter("userName");
		if (user != null && user.getUsername().equals(username)) {
			model.addAttribute("userName", username);
			model.addAttribute("realName", user.getRealName());
			model.addAttribute("userId", user.getId());
			HttpSession session = request.getSession();

			// session.setAttribute("serverHost",payManagerDev.getServer_host());
			// session.setAttribute("manager",manager);
			// session.setAttribute("modules",modules);
			session.setAttribute("user",user);
			return "login/index";

		}

		// String username = request.getParameter("userName");
		// User user=null;
		// user=userService.findByUsername(username);
		// if(user != null){
		// model.addAttribute("userName",username);
		// model.addAttribute("realName",user.getRealName());
		// }else{
		//
		// }
		//
		return "/index";
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "update")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("newpass");
		String username = request.getParameter("userName");
		User user = null;
		user = userService.findByUsername(username);
		// 刷新盐值，重新加密
		user.buildPasswordSalt();
		user.setPlanPassword(password);
		user.setUpdatePwdTime(new Date());
		userService.updateUser(user);
	}

	@ResponseBody
	@RequestMapping("getModule")
	public void getModule(HttpServletRequest request, HttpServletResponse response) {
		// User user=(User) request.getSession(false).getAttribute("user");
		Integer id = Integer.parseInt(request.getParameter("userId"));
		User user = userService.findUserById(id);//获取user
		Map<String, Object> map = new HashMap<String, Object>();
		if (user != null) {
			String roleIds = user.getRole();
			if (roleIds != null && !("").equals(user.getRole())) {
				String[] roleIds1 = roleIds.split(",");// RoleId字段数组转list
				System.err.println(roleIds1.toString());
				/*List<String> roleIdList = new ArrayList<String>();
				Collections.addAll(roleIdList, roleIds1);*/
				List<Map<String, Object>> moduleList = new ArrayList<Map<String,Object>>();
				for (String roleId : roleIds1) {
					List<Map<String, Object>> modules = privilegeModuleService.getModuleListByroleId(roleId);
					moduleList.addAll(modules);

				}
				map.put("menus", moduleList);

			}
			System.err.println(JSONObject.fromObject(map).toString());

			writeSuccessJson(response, map);
		}

		return;
	}

}