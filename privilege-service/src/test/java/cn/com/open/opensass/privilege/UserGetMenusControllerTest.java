package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserGetMenusController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserGetMenusControllerTest extends BaseTest {

	@Autowired
	private UserGetMenusController fixture;

	@Test //	/userRole/getUserPrivilegeMenus
	public void getPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserId = "3094776";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		fixture.getPrivilege(request, response, modifyPrivilegeUserVo);
		log.info(response.getContentAsString());
		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
}
