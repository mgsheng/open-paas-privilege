package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.FunctionGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class FunctionGetPrivilegeControllerTest extends BaseTest {

    @Autowired
    private FunctionGetPrivilegeController functionGetPrivilegeController;

    @Test //  /function/getFunctionIdList")
    public void modifyFunction() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        request.addParameter("appUserId", "3094776");
        request.addParameter("resourceId", "151,185");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        JSONObject json = JSON.parseObject(response.getContentAsString());
        Assert.assertEquals("0", json.getString("status"));
    }
}
