package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.VerifyUserPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class VerifyUserPrivilegeControllerTest extends BaseTest {

	@Autowired
	private VerifyUserPrivilegeController fixture;

	@Test
	public void verifyUserPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserid = "3094776";
		String optUrl = "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		request.addParameter("appUserId", appUserid);
		request.addParameter("optUrl", optUrl);
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.verifyUserPrivilege(request, response);
		log.info(response.getContentAsString());
		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
}
