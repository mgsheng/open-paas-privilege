package com.andaily.springoauth.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.andaily.springoauth.service.dto.PrivilegeFunctionDto;
import com.andaily.springoauth.service.dto.PrivilegeGroupDto;
import com.andaily.springoauth.service.dto.PrivilegeMenuDto;
import com.andaily.springoauth.service.dto.PrivilegeResourceDto;
import com.andaily.springoauth.tools.AESUtil;
import com.andaily.springoauth.tools.DateTools;
import com.andaily.springoauth.tools.HMacSha1;
import com.andaily.springoauth.tools.LoadPopertiesFile;
import com.andaily.springoauth.tools.WebUtils;

/**
 * Handle 'authorization_code' type actions
 * 
 * 
 */
@Controller
public class PrivilegeInterfaceController {

	private static final Logger LOG = LoggerFactory
			.getLogger(PrivilegeInterfaceController.class);
	@Value("#{properties['privilege-group-add-uri']}")
	private String privilegeGroupAddUrl;

	@Value("#{properties['privilege-group-modify-uri']}")
	private String privilegeGroupModifyUrl;

	@Value("#{properties['privilege-group-del-uri']}")
	private String privilegeGroupDelUrl;

	@Value("#{properties['privilege-group-query-uri']}")
	private String privilegeGroupQueryUrl;
	
	@Value("#{properties['privilege-menu-add-uri']}")
	private String privilegeMenuAddUrl;
	
	@Value("#{properties['privilege-menu-modify-uri']}")	
	private String privilegeMenuModifyUrl;
	
	@Value("#{properties['privilege-menu-query-uri']}")
	private String privilegeMenuQueryUrl;
	
	@Value("#{properties['privilege-menu-del-uri']}")
	private String privilegeMenuDelUrl;
	
	@Value("#{properties['privilege-resource-add-uri']}")
	private String privilegeResourceAddUrl;
	
	@Value("#{properties['privilege-resource-modify-uri']}")	
	private String privilegeResourceModifyUrl;
	
	@Value("#{properties['privilege-resource-query-uri']}")
	private String privilegeResourceQueryUrl;
	
	@Value("#{properties['privilege-resource-del-uri']}")
	private String privilegeResourceDelUrl;
	
	@Value("#{properties['privilege-function-add-uri']}")
	private String privilegeFunctionAddUrl;
	
	@Value("#{properties['privilege-function-modify-uri']}")	
	private String privilegeFunctionModifyUrl;
	
	@Value("#{properties['privilege-function-del-uri']}")
	private String privilegeFunctionDelUrl;
	//查询组织机构测试地址属性
	@Value("#{properties['privilege-groupredis-query-uri']}")
	private String privilegeGroupQueryTestUrl;
	//查询组织机构缓存删除测试地址
	@Value("#{properties['privilege-groupredis-del-uri']}")
	private String privilegeGroupRedisDelUrl;
	//查询组织机构缓存更新测试地址
	@Value("#{properties['privilege-groupredis-update-uri']}")
	private String privilegeGroupRedisUpdateUrl;
	@Value("#{properties['privilege-approleredis-query-uri']}")
	private String privilegeAppRoleRedisGetUrl;
	@Value("#{properties['privilege-approleredis-del-uri']}")
	private String privilegeAppRoleRedisDelUrl;
	@Value("#{properties['privilege-approleredis-update-uri']}")
	private String privilegeAppRoleRedisUpdateUrl;
	@Value("#{properties['privilege-appmenu-redis-query-uri']}")
	private String privilegeAppMenuRedisGetUrl;
	@Value("#{properties['privilege-appmenu-redis-del-uri']}")
	private String privilegeAppMenuRedisDelUrl;
	@Value("#{properties['privilege-appmenu-redis-update-uri']}")
	private String privilegeAppMenuRedisUpdateUrl;
	@Value("#{properties['privilege-appres-redis-query-uri']}")
	private String privilegeAppResRedisGetUrl;
	@Value("#{properties['privilege-appres-redis-del-uri']}")
	private String privilegeAppResRedisDelUrl;
	@Value("#{properties['privilege-appres-redis-update-uri']}")
	private String privilegeAppResRedisUpdateUrl;
	
	final static String SEPARATOR = "&";
	private Map<String, String> map = LoadPopertiesFile.loadProperties();

	/**
	 * 组织机构权限初始创建接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addPrivilege", method = RequestMethod.GET)
	public String addPrivilege(Model model) {
		model.addAttribute("privilegeGroupAddUrl", privilegeGroupAddUrl);
		return "privilege/privilege_group_add";
	}

	@RequestMapping(value = "addPrivilege", method = RequestMethod.POST)
	public String addPrivilege(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		  String key=map.get(privilegeGroupDto.getAppId());
	  	  String signature="";
	  	  String timestamp="";
	  	  String signatureNonce="";
	  	  String appKey="";
		      if(key!=null){
		    	    appKey=map.get(key);
		      		timestamp=DateTools.getSolrDate(new Date());
		  		 	StringBuilder encryptText = new StringBuilder();
		  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		  		 	encryptText.append(privilegeGroupDto.getAppId());
		  			encryptText.append(SEPARATOR);
		  			if(appKey!=null){
		  			  encryptText.append(appKey);
		  			}
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(timestamp);
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(signatureNonce);
		  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
		  			signature=HMacSha1.getNewResult(signature);
		      }
		 String fullUri = privilegeGroupDto.getFullUri()+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;;
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}

	/**
	 * 组织机构权限授权接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyPrivilege", method = RequestMethod.GET)
	public String modifyPrivilege(Model model) {
		model.addAttribute("privilegeGroupModifyUrl", privilegeGroupModifyUrl);
		return "privilege/privilege_group_modify";
	}

	@RequestMapping(value = "modifyPrivilege", method = RequestMethod.POST)
	public String modifyPrivilege(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getModifyUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}

	/**
	 * 组织机构权限删除接口（删除当前组织机构所有权限）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delPrivilege", method = RequestMethod.GET)
	public String delPrivilege(Model model) {
		model.addAttribute("privilegeGroupDelUrl", privilegeGroupDelUrl);
		return "privilege/privilege_group_del";
	}

	@RequestMapping(value = "delPrivilege", method = RequestMethod.POST)
	public String delPrivilege(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getDelUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 组织机构权限查询接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getGroupPrivilege", method = RequestMethod.GET)
	public String getGroupPrivilege(Model model) {
		model.addAttribute("privilegeGroupQueryUrl", privilegeGroupQueryUrl);
		return "privilege/privilege_group_query";
	}

	@RequestMapping(value = "getGroupPrivilege", method = RequestMethod.POST)
	public String getGroupPrivilege(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getQueryUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用角色 缓存查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getAppRolePrivilegeRedis", method = RequestMethod.GET)
	public String getAppRolePrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppRoleRedisGetUrl);
		return "privilege/privilege_app_role_query_redis";
	}

	@RequestMapping(value = "getAppRolePrivilegeRedis", method = RequestMethod.POST)
	public String getAppRolePrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用角色 缓存删除
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delAppRolePrivilegeRedis", method = RequestMethod.GET)
	public String delAppRolePrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppRoleRedisDelUrl);
		return "privilege/privilege_app_role_del_redis";
	}

	@RequestMapping(value = "delAppRolePrivilegeRedis", method = RequestMethod.POST)
	public String delAppRolePrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用角色 缓存更新
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updateAppRolePrivilegeRedis", method = RequestMethod.GET)
	public String updateAppRolePrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppRoleRedisUpdateUrl);
		return "privilege/privilege_app_role_update_redis";
	}

	@RequestMapping(value = "updateAppRolePrivilegeRedis", method = RequestMethod.POST)
	public String updateAppRolePrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用菜单 缓存查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getAppMenuPrivilegeRedis", method = RequestMethod.GET)
	public String getAppMenuPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppMenuRedisGetUrl);
		return "privilege/privilege_app_menu_query_redis";
	}

	@RequestMapping(value = "getAppMenuPrivilegeRedis", method = RequestMethod.POST)
	public String getAppMenuPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用菜单 缓存删除
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delAppMenuPrivilegeRedis", method = RequestMethod.GET)
	public String delAppMenuPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppMenuRedisDelUrl);
		return "privilege/privilege_app_menu_del_redis";
	}

	@RequestMapping(value = "delAppMenuPrivilegeRedis", method = RequestMethod.POST)
	public String delAppMenuPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用菜单 缓存更新
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updateAppMenuPrivilegeRedis", method = RequestMethod.GET)
	public String updateAppMenuPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppMenuRedisUpdateUrl);
		return "privilege/privilege_app_menu_update_redis";
	}

	@RequestMapping(value = "updateAppMenuPrivilegeRedis", method = RequestMethod.POST)
	public String updateAppMenuPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用资源 缓存查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getAppResPrivilegeRedis", method = RequestMethod.GET)
	public String getAppResPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppResRedisGetUrl);
		return "privilege/privilege_app_res_query_redis";
	}

	@RequestMapping(value = "getAppResPrivilegeRedis", method = RequestMethod.POST)
	public String getAppResPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用资源 缓存删除
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delAppResPrivilegeRedis", method = RequestMethod.GET)
	public String delAppResPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppResRedisDelUrl);
		return "privilege/privilege_app_res_del_redis";
	}

	@RequestMapping(value = "delAppResPrivilegeRedis", method = RequestMethod.POST)
	public String delAppResPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 应用资源 缓存更新
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updateAppResPrivilegeRedis", method = RequestMethod.GET)
	public String updateAppResPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeAppResRedisUpdateUrl);
		return "privilege/privilege_app_res_update_redis";
	}

	@RequestMapping(value = "updateAppResPrivilegeRedis", method = RequestMethod.POST)
	public String updateAppResPrivilegeRedis(HttpServletRequest request)
			throws Exception {
		final String fullUri = request.getParameter("privilegeGroupQueryTestUrl")+"?appId="+request.getParameter("appId");
		System.err.println(fullUri);
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	
	/**
	 * 组织机构权限查询接口=========测试
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getGroupPrivilegeTest", method = RequestMethod.GET)
	public String getGroupPrivilegeTest(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeGroupQueryTestUrl);
		return "privilege/privilege_group_query_test";
	}

	@RequestMapping(value = "getGroupPrivilegeTest", method = RequestMethod.POST)
	public String getGroupPrivilegeTest(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getQueryTestUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 组织机构权限缓存删除接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delGroupPrivilegeRedis", method = RequestMethod.GET)
	public String delGroupPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeGroupRedisDelUrl);
		return "privilege/privilege_group_del_redis";
	}
	@RequestMapping(value = "delGroupPrivilegeRedis", method = RequestMethod.POST)
	public String delGroupPrivilegeRedis(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getQueryTestUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 * 组织机构权限缓存更新接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updateGroupPrivilegeRedis", method = RequestMethod.GET)
	public String updateGroupPrivilegeRedis(Model model) {
		model.addAttribute("privilegeGroupQueryTestUrl", privilegeGroupRedisUpdateUrl);
		return "privilege/privilege_group_update_redis";
	}
	@RequestMapping(value = "updateGroupPrivilegeRedis", method = RequestMethod.POST)
	public String updateGroupPrivilegeRedis(PrivilegeGroupDto privilegeGroupDto)
			throws Exception {
		final String fullUri = privilegeGroupDto.getQueryTestUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	
	/**
	 *菜单添加接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addMenu", method = RequestMethod.GET)
	public String addMenu(Model model) {
		model.addAttribute("privilegeMenuAddUrl", privilegeMenuAddUrl);
		return "privilege/privilege_menu_add";
	}

	@RequestMapping(value = "addMenu", method = RequestMethod.POST)
	public String addMenu(PrivilegeMenuDto privilegeMenuDto)
			throws Exception {
		final String fullUri = privilegeMenuDto.getFullUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *菜单编辑接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyMenu", method = RequestMethod.GET)
	public String modifyMenu(Model model) {
		model.addAttribute("privilegeMenuModifyUrl", privilegeMenuModifyUrl);
		return "privilege/privilege_menu_modify";
	}

	@RequestMapping(value = "modifyMenu", method = RequestMethod.POST)
	public String modifyMenu(PrivilegeMenuDto privilegeMenuDto)
			throws Exception {
		final String fullUri = privilegeMenuDto.getModifyUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *菜单删除接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delMenu", method = RequestMethod.GET)
	public String delMenu(Model model) {
		model.addAttribute("privilegeMenuDelUrl", privilegeMenuDelUrl);
		return "privilege/privilege_menu_del";
	}

	@RequestMapping(value = "delMenu", method = RequestMethod.POST)
	public String delMenu(PrivilegeMenuDto privilegeMenuDto)
			throws Exception {
		final String fullUri = privilegeMenuDto.getDelUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *菜单查询接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.GET)
	public String getMenus(Model model) {
		model.addAttribute("privilegeMenuQueryUrl", privilegeMenuQueryUrl);
		return "privilege/privilege_menu_query";
	}

	@RequestMapping(value = "getMenus", method = RequestMethod.POST)
	public String getMenus(PrivilegeMenuDto privilegeMenuDto)
			throws Exception {
		final String fullUri = privilegeMenuDto.getQueryUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *资源添加接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addResource", method = RequestMethod.GET)
	public String addResource(Model model) {
		model.addAttribute("privilegeResourceAddUrl", privilegeResourceAddUrl);
		return "privilege/privilege_resource_add";
	}

	@RequestMapping(value = "addResource", method = RequestMethod.POST)
	public String addResource(PrivilegeResourceDto privilegeResourceDto)
			throws Exception {
		final String fullUri = privilegeResourceDto.getFullUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *资源编辑接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyResource", method = RequestMethod.GET)
	public String modifyResource(Model model) {
		model.addAttribute("privilegeResourceModifyUrl", privilegeResourceModifyUrl);
		return "privilege/privilege_resource_modify";
	}
	@RequestMapping(value = "modifyResource", method = RequestMethod.POST)
	public String modifyResource(PrivilegeResourceDto privilegeResourceDto)
			throws Exception {
		final String fullUri = privilegeResourceDto.getModifyUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *资源删除接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delResource", method = RequestMethod.GET)
	public String delResource(Model model) {
		model.addAttribute("privilegeResourceDelUrl", privilegeResourceDelUrl);
		return "privilege/privilege_resource_del";
	}

	@RequestMapping(value = "delResource", method = RequestMethod.POST)
	public String delResource(PrivilegeResourceDto privilegeResourceDto)
			throws Exception {
		final String fullUri = privilegeResourceDto.getDelUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	
	/**
	 *资源查询接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getResPrivilege", method = RequestMethod.GET)
	public String getResPrivilege(Model model) {
		model.addAttribute("privilegeResourceQueryUrl", privilegeResourceQueryUrl);
		return "privilege/privilege_resource_query";
	}

	@RequestMapping(value = "getResPrivilege", method = RequestMethod.POST)
	public String getResPrivilege(PrivilegeResourceDto privilegeResourceDto)
			throws Exception {
		final String fullUri = privilegeResourceDto.getQueryUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}


	/**
	 *权限功能添加接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addFunction", method = RequestMethod.GET)
	public String addFunction(Model model) {
		model.addAttribute("privilegeFunctionAddUrl", privilegeFunctionAddUrl);
		return "privilege/privilege_function_add";
	}

	@RequestMapping(value = "addFunction", method = RequestMethod.POST)
	public String addFunction(PrivilegeFunctionDto privilegeFunctionDto)
			throws Exception {
		final String fullUri = privilegeFunctionDto.getFullUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *权限功能编辑接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyFunction", method = RequestMethod.GET)
	public String modifyFunction(Model model) {
		model.addAttribute("privilegeFunctionModifyUrl", privilegeFunctionModifyUrl);
		return "privilege/privilege_function_modify";
	}
	@RequestMapping(value = "modifyFunction", method = RequestMethod.POST)
	public String modifyFunction(PrivilegeFunctionDto privilegeFunctionDto)
			throws Exception {
		final String fullUri = privilegeFunctionDto.getModifyUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}
	/**
	 *权限功能删除接口
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delFunction", method = RequestMethod.GET)
	public String delFunction(Model model) {
		model.addAttribute("privilegeFunctionDelUrl", privilegeFunctionDelUrl);
		return "privilege/privilege_function_del";
	}

	@RequestMapping(value = "delFunction", method = RequestMethod.POST)
	public String delFunction(PrivilegeFunctionDto privilegeFunctionDto)
			throws Exception {
		final String fullUri = privilegeFunctionDto.getDelUri();
		LOG.debug("Send to Oauth-Server URL: {}", fullUri);
		return "redirect:" + fullUri;
	}

	/**
	 * 获取 HMAC-SHA1 签名方法对对encryptText进行签名 值
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @param accessToken
	 */
	@RequestMapping(value = "/getSignature", method = RequestMethod.POST)
	public void getSignature(HttpServletRequest request,
			HttpServletResponse response, String appId) {
		Boolean flag = true;
		String key = map.get(appId);
		String result = "";
		String timestamp = "";
		String signatureNonce = "";
		if (key != null) {
			timestamp = DateTools.getSolrDate(new Date());
			StringBuilder encryptText = new StringBuilder();
			signatureNonce = com.andaily.springoauth.tools.StringTools
					.getRandom(100, 1);
			encryptText.append(appId);
			encryptText.append(SEPARATOR);
			encryptText.append(timestamp);
			encryptText.append(SEPARATOR);
			encryptText.append(signatureNonce);
			try {
				result = HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
			result = HMacSha1.getNewResult(result);
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", flag);
		returnMap.put("signature", result);
		returnMap.put("timestamp", timestamp);
		returnMap.put("signatureNonce", signatureNonce);
		WebUtils.writeJsonToMap(response, returnMap);
	}

	/**
	 * 生成加密串
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"null".equals(v)
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		String temp_params = sb.toString();
		return sb.toString().substring(0, temp_params.length() - 1);
	}
}