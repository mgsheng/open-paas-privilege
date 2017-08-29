package cn.com.open.opensass.privilege.signature;

import cn.com.open.opensass.privilege.tools.DateUtils;
import cn.com.open.opensass.privilege.tools.HMacSha1;
import cn.com.open.opensass.privilege.tools.StringTool;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

public class Signature {

    public static MockHttpServletRequest getSignatureRequest(String appsecret, String appId, String appKey) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        try {
            String timestamp = DateUtils.toDateText(new Date(), "YYYY-MM-dd'T'hh:mm:ss'Z'");
            String signatureNonce= StringTool.getRandomString(18);
            String encryptText = appId + "&" + appKey + "&" + timestamp + "&" + signatureNonce;
            String signature= HMacSha1.HmacSHA1Encrypt(encryptText, appsecret);

            request.addParameter("createUserid", "AdminT1373101");
            request.addParameter("appId", appId);
            request.addParameter("appKey", appKey);
            request.addParameter("signature", signature);
            request.addParameter("timestamp", timestamp);
            request.addParameter("signatureNonce", signatureNonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

}
