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

import com.andaily.springoauth.service.dto.PrivilegeGroupDto;
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
	@Value("#{properties['privilege-group-add']}")
	private String privilegeGroupAddUrl;

	@Value("#{properties['privilege-group-modify']}")
	private String privilegeGroupModifyUrl;

	@Value("#{properties['privilege-group-del']}")
	private String privilegeGroupDelUrl;

	@Value("#{properties['privilege-group-query']}")
	private String privilegeGroupQueryUrl;

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
		 String fullUri = privilegeGroupDto.getFullUri();
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