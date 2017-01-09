package cn.com.open.pay.platform.manager.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import net.sf.json.JSONArray;
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
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private PayManagerDev payManagerDev;
	@Autowired
	private PrivilegeRoleDetailsService privilegeRoleDetailsService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Value("#{properties['get-privilege-user-uri']}")
	private String getUserPrivilegeUrl;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String appResRedisUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String appMenuRedisUrl;
	@Value("#{properties['getSignature-uri']}")
	private String getSignatureUrl;

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
		// boolean flag = false;
		Boolean flag = true;
		String errorCode = "ok";
		User user = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// user = checkUsername(username, userService);
		/*
		 * if (user != null) { if (user.checkPasswod(password)) { flag = true;
		 * errorCode = "ok"; // Manager
		 * manager=managerService.getManagerById(user.getId()); // int role=0;
		 * // List<PrivilegeRoleDetails> list=null; // List<PrivilegeModule>
		 * modules=null; // if(manager!=null){ // role=manager.getRole(); //
		 * list=privilegeRoleDetailsService.findRoleDetailsByRoleId(role); //
		 * List<Integer>ids=new ArrayList<Integer>(list.size()); // for(int
		 * i=0;i<list.size();i++){ // ids.add(list.get(i).getModuleId()); // }
		 * // if(ids!=null&&list.size()>0){ // modules=
		 * privilegeModuleService.findModuleByIds(ids); // for(int
		 * j=0;j<modules.size();j++){ // // } // } // } //HttpSession session =
		 * request.getSession(); //
		 * session.setAttribute("serverHost",payManagerDev.getServer_host()); //
		 * session.setAttribute("manager",manager); //
		 * session.setAttribute("modules",modules);
		 * //session.setAttribute("user", user);
		 * 
		 * } else { errorCode = "error"; }
		 * 
		 * } else { errorCode = "error"; }
		 */
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
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		Map<String, Object> map = privilegeGetSignatureService.getSignature(appId);
		map.put("appId", appId);
		map.put("appUserId", appUserId);
		String result = getModule(map);
		Map<String, Object> menus=new HashMap<String, Object>();
		if (result!=null&&!("").equals(result)) {
			JSONObject  jasonObject = JSONObject.fromObject(result);
			Map JsonMap = (Map)jasonObject;
			if ("0".equals(JsonMap.get("status"))) {
				menus.put("status", "0");
				menus.put("errMsg", JsonMap.get("errMsg"));
				model.addAttribute("menus", JSONObject.fromObject(menus));
			}else {
				List<Map<String, Object>> list=getMenu(result);
				//Map<String, Object> menus=new HashMap<String, Object>();
				menus.put("menus", list);
				model.addAttribute("appId",appId);
				model.addAttribute("menus", JSONObject.fromObject(menus));
			}
		}else {
			menus.put("status", "0");
			menus.put("errMsg", "没有相应菜单");
			model.addAttribute("appId",appId);
			model.addAttribute("menus", JSONObject.fromObject(menus));
		}
		
		
		
		return "login/index";

		
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

	public String getModule(Map<String, Object> map) {
		String url = getUserPrivilegeUrl;
		String result="";
		// POST的URL
		HttpPost httppost = new HttpPost(url);
		// 建立HttpPost对象
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 建立一个NameValuePair数组，用于存储欲传送的参数
		params.add(new BasicNameValuePair("appId", (String) map.get("appId")));
		params.add(new BasicNameValuePair("appUserId", (String) map.get("appUserId")));
		params.add(new BasicNameValuePair("appKey", (String) map.get("appKey")));
		params.add(new BasicNameValuePair("signatureNonce", (String) map.get("signatureNonce")));
		params.add(new BasicNameValuePair("timestamp", (String) map.get("timestamp")));
		params.add(new BasicNameValuePair("signature", (String) map.get("signature")));
		try {
			// 添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 设置编码
			HttpResponse response = new DefaultHttpClient().execute(httppost);
			// 发送Post,并返回一个HttpResponse对象
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				 result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("返回结果"+result);
		return result;
	}
	public List<Map<String, Object>> getMenu(String result) {
		JSONObject  jasonObject = JSONObject.fromObject(result);
		Map JsonMap = (Map)jasonObject;
		List<Map<String, Object>> resourceList=(List<Map<String, Object>>) JsonMap.get("resourceList");
		List<Map<String, Object>> menulists=(List<Map<String, Object>>) JsonMap.get("menuList");
		System.err.println(menulists.size());
		if(menulists.size()<=0){
			return null;
		}
		// 顶级菜单集合
		List<Map<String, Object>> pMenus = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1=null;
		for(Map<String, Object> map2:menulists){
			String menuId=(String) map2.get("menuId");
			String menuname=(String) map2.get("menuName");
			List<Map<String, Object>> mList=null;
			if (map2.get("parentId").equals("0")) {
				map1=new HashMap<String,Object>();
				mList=new ArrayList<Map<String, Object>>();
				map1.put("menuid", (String) map2.get("menuId"));
				map1.put("menuname", (String) map2.get("menuName"));
				map1.put("icon","");
				pMenus.add(map1);
			}
			for(Map<String, Object> map3:menulists){
				if(map3.get("parentId").equals(map2.get("menuId"))){
					for(Map<String, Object> map6:resourceList){
						if (map6.get("menuId").equals(map3.get("menuId"))) {
							Map<String, Object> map4=new HashMap<String, Object>();
							map4.put("menuid", map3.get("menuId"));
							map4.put("menuname", map3.get("menuName"));
							map4.put("icon", map3.get("icon"));
							map4.put("url",map6.get("baseUrl"));
							mList.add(map4);
							map1.put("menus", mList);
						}
					}
					
				}
			}
		}
		//Map<String, Object> map5=new HashMap<>();
		//map5.put("menus", pMenus);
		return pMenus;
	}
}