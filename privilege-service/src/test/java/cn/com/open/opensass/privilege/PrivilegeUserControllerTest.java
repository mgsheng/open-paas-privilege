package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeUserController;
import cn.com.open.opensass.privilege.base.BaseTest;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class PrivilegeUserControllerTest extends BaseTest{

	@Autowired
	private PrivilegeUserController fixture;

	@Test // /user/getUser
	public void getUserList() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("start", "0");
		request.addParameter("limit", "10");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getUserList(request, response);

		Assert.assertTrue(response.getContentAsString().contains("rows"));
	}

	@Test //	/user/findUser
	public void findUser() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.findUser(request, response);

		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	
}
