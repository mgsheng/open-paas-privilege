package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.RoleGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.tools.DateUtils;
import cn.com.open.opensass.privilege.tools.HMacSha1;
import cn.com.open.opensass.privilege.tools.StringTool;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;


public class RoleGetPrivilegeControllerTest extends BaseTest {

    @Autowired
    private RoleGetPrivilegeController fixture;

	@Test //	/role/getRolePrivilege
    public void getRolePrivilege() throws Exception {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String timestamp = DateUtils.toDateText(new Date(), "YYYY-MM-dd'T'hh:mm:ss'Z'");
        String signatureNonce= StringTool.getRandomString(18);

        String encryptText = appId + "&" + appKey + "&" + timestamp + "&" + signatureNonce;
        String signature= HMacSha1.HmacSHA1Encrypt(encryptText, appsecret);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("createUserid", "AdminT1373101");
        request.addParameter("appId", appId);
        request.addParameter("appKey", appKey);
        request.addParameter("groupId", "50C935EB5239532AE0533312640A8C66");
        request.addParameter("start", "0");
        request.addParameter("limit", "10");
        request.addParameter("signature", signature);
        request.addParameter("timestamp", timestamp);
        request.addParameter("signatureNonce", signatureNonce);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.getRolePrivilege(request, response);

        Assert.assertTrue(response.getContentAsString().contains("roleList"));
    }

    @Test //    /role/getRoleList
    public void getRoleList() throws Exception {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String timestamp = DateUtils.toDateText(new Date(), "YYYY-MM-dd'T'hh:mm:ss'Z'");
        String signatureNonce= StringTool.getRandomString(18);
        String encryptText = appId + "&" + appKey + "&" + timestamp + "&" + signatureNonce;
        String signature= HMacSha1.HmacSHA1Encrypt(encryptText, appsecret);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("createUserid", "AdminT1373101");
        request.addParameter("appId", appId);
        request.addParameter("appKey", appKey);
        request.addParameter("groupId", "50C935EB5239532AE0533312640A8C66");
        request.addParameter("privilegeRoleId", "6dec579e03c6f00f0bb849e001590463");
        request.addParameter("start", "0");
        request.addParameter("limit", "10");
        request.addParameter("signature", signature);
        request.addParameter("timestamp", timestamp);
        request.addParameter("signatureNonce", signatureNonce);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.getRoleList(request, response);

        Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));

    }

}
