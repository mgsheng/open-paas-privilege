package cn.com.open.pay.platform.manager.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import cn.com.open.pay.platform.manager.tools.DateTools;
import cn.com.open.pay.platform.manager.tools.LoadPopertiesFile;
import cn.com.open.pay.platform.manager.tools.StringTool;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/signature/")
public class PrivilegeSignatureController {
	final static String SEPARATOR = "&";
	private Map<String, String> map = LoadPopertiesFile.loadProperties();
	@RequestMapping(value = "/getSignature", method = RequestMethod.POST)
	public void getSignature(HttpServletRequest request,
			HttpServletResponse response, String appId) {
		Boolean flag = true;
		String key=map.get(appId);
	  	  String signature="";
	  	  String timestamp="";
	  	  String signatureNonce="";
	  	  String appKey="";
		      if(key!=null){
		    	    appKey=map.get(key);
		      		timestamp=DateTools.getSolrDate(new Date());
		  		 	StringBuilder encryptText = new StringBuilder();
		  		 	signatureNonce=StringTool.getRandom(100,1);
		  		 	encryptText.append(appId);
		  			encryptText.append(SEPARATOR);
		  			if(appKey!=null){
		  			  encryptText.append(appKey);
		  			}
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(timestamp);
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(signatureNonce);
		  		 	try {
						signature=cn.com.open.pay.platform.manager.tools.HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
						signature=cn.com.open.pay.platform.manager.tools.HMacSha1.getNewResult(signature);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		      }
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", flag);
		returnMap.put("signature", signature);
		returnMap.put("timestamp", timestamp);
		returnMap.put("signatureNonce", signatureNonce);
		returnMap.put("appKey", appKey);
		WebUtils.writeJsonToMap(response, returnMap);
	}
}
