package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.AppResRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class AppResRedisPrivilegeControllerTest extends BaseTest {

	@Autowired
	private AppResRedisPrivilegeController fixture;

	@Test  	// 	/AppRes/getAppResRedis
	public void getAppResRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAppResRedisPrivilege(request, response);
		String str = response.getContentAsString();
		Assert.assertTrue(str.contains("functionList"));

	}

	@Test   //	/AppRes/delAppResRedis
	public void delAppResRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppResRedisPrivilege(request, response);
		Assert.assertEquals("Success", response.getContentAsString());
	}
	@Test   //	/AppRes/updateAppResRedis
	public void updateAppResRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppResRedisPrivilege(request, response);
		String str = response.getContentAsString();
		Assert.assertTrue(str.contains("functionList"));
	}
}
