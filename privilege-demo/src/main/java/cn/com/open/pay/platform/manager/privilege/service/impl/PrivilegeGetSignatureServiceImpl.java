package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.tools.DateTools;
import cn.com.open.pay.platform.manager.tools.LoadPopertiesFile;
import cn.com.open.pay.platform.manager.tools.StringTool;

@Service("privilegeGetSignatureService")
public class PrivilegeGetSignatureServiceImpl implements PrivilegeGetSignatureService {
	final static String SEPARATOR = "&";
	private Map<String, String> map = LoadPopertiesFile.loadProperties();
	private static final String ALGORITHM = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	@Override
	public Map<String, Object> getSignature(String appId) {
		String key = map.get(appId);
		String signature = "";
		String timestamp = "";
		String signatureNonce = "";
		String appKey = "";
		if (key != null) {
			appKey = map.get(key);
			timestamp = DateTools.getSolrDate(new Date());
			StringBuilder encryptText = new StringBuilder();
			signatureNonce = StringTool.getRandom(100, 1);
			encryptText.append(appId);
			encryptText.append(SEPARATOR);
			if (appKey != null) {
				encryptText.append(appKey);
			}
			encryptText.append(SEPARATOR);
			encryptText.append(timestamp);
			encryptText.append(SEPARATOR);
			encryptText.append(signatureNonce);
			try {
				signature = cn.com.open.pay.platform.manager.tools.HMacSha1.HmacSHA1Encrypt(encryptText.toString(),
						key);
				signature = cn.com.open.pay.platform.manager.tools.HMacSha1.getNewResult(signature);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("signature", signature);
		returnMap.put("timestamp", timestamp);
		returnMap.put("signatureNonce", signatureNonce);
		returnMap.put("appKey", appKey);
		return returnMap;
	}

	@Override
	public Map<String, Object> getOauthSignature(String appId, String client_id, String access_token) {
		String key = map.get(appId);
		String timestamp = "";
		String signatureNonce = "";
		String signature = "";
		if (key != null) {
			try {
				timestamp = DateTools.getSolrDate(new Date());
				signatureNonce = StringTool.getRandom(100, 1);
				StringBuilder encryptText = new StringBuilder();
				encryptText.append(client_id);
				encryptText.append(SEPARATOR);
				encryptText.append(access_token);
				encryptText.append(SEPARATOR);
				encryptText.append(timestamp);
				encryptText.append(SEPARATOR);
				encryptText.append(signatureNonce);
				signature = cn.com.open.pay.platform.manager.tools.HMacSha1.HmacSHA1Encrypt(encryptText.toString(),
						key);
				signature = cn.com.open.pay.platform.manager.tools.HMacSha1.getNewResult(signature);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("signature", signature);
		returnMap.put("timestamp", timestamp);
		returnMap.put("signatureNonce", signatureNonce);
		returnMap.put("client_id", key);
		returnMap.put("access_token", access_token);
		return returnMap;
	}

}
