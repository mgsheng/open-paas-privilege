package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.pay.platform.manager.redis.impl.RedisConstant;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.DateTools;
import cn.com.open.pay.platform.manager.tools.StringTool;
import net.sf.json.JSONObject;

@Service("privilegeGetSignatureService")
public class PrivilegeGetSignatureServiceImpl extends BaseControllerUtil implements PrivilegeGetSignatureService {
	final static String SEPARATOR = "&";
	private static final String AccessTokenPrefix = RedisConstant.ACCESSTOKEN_CACHE;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	@Override
	public Map<String, Object> getSignature(String appId) {
		String key = oesPrivilegeDev.getClientSecret();
		String signature = "";
		String timestamp = "";
		String signatureNonce = "";
		String appKey = "";
		if (key != null) {
			appKey = oesPrivilegeDev.getClientId();
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
		String key = oesPrivilegeDev.getClientSecret();
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

	@Override
	public String getToken() {
		// 从缓存获取token
		String access_token = (String) redisClientTemplate.getObject(oesPrivilegeDev.getAppId() + AccessTokenPrefix);
		Map<String, Object> parameters = new HashMap<String, Object>();
		// 若缓存中没有token 获取token
		if (access_token == null) {
			parameters.put("client_id", oesPrivilegeDev.getClientId());
			parameters.put("client_secret", oesPrivilegeDev.getClientSecret());
			parameters.put("scope", "read,write");
			parameters.put("grant_type", oesPrivilegeDev.getGrantType());
			String result = sendPost(oesPrivilegeDev.getOauthTokenUrl(), parameters);
			if (result != null && !("").equals(result)) {
				String aString = result.substring(0, 1);
				if (aString.equals("{")) {
					JSONObject object = JSONObject.fromObject(result);
					access_token = (String) object.get("access_token");
					if (access_token != null && !("").equals(access_token)) {
						redisClientTemplate.setObjectByTime(oesPrivilegeDev.getAppId() + AccessTokenPrefix,
								access_token, 40000);
					}
				}
			}
		}
		return access_token;
	}

}
