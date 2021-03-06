package cn.com.open.opensass.privilege.tools;


import javax.servlet.http.HttpServletRequest;

import cn.com.open.opensass.privilege.model.App;

public class OauthSignatureValidateHandler {
	
	final static String  SEPARATOR = "&";
	/**
	 * Signature验证算法
	 * 格式：client_id&access_token&timestamp&signatureNonce
	 * 加密方法:HmacSHA1
	 * @param request
	 * @param app
	 * @return
	 */
	  public static boolean validateSignature(HttpServletRequest request,App app){
		    String appId=request.getParameter("appId");
		    String appKey=request.getParameter("appKey");
		    String signature=request.getParameter("signature");
		    String timestamp=request.getParameter("timestamp");
		    String signatureNonce=request.getParameter("signatureNonce");
		 	 StringBuilder encryptText = new StringBuilder();
		 	encryptText.append(appId);
			encryptText.append(SEPARATOR);
		 	encryptText.append(appKey);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
		 	try {
			String result=	HMacSha1.HmacSHA1Encrypt(encryptText.toString(), app.getAppsecret());
			if(signature.equals(result))
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		  
	    }  
	  /**
	   * 
	   * @param signature 解密结果
	   * @param encryptText 加密字符串
	   * @param encryptKey  秘钥
	   * @return
	   */
	  public static boolean validateSignature(String signature,String encryptText, String encryptKey){
		 	try {
				String result=	HMacSha1.HmacSHA1Encrypt(encryptText,encryptKey);
				
				
				if(signature.equals(result))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
	}  
}
