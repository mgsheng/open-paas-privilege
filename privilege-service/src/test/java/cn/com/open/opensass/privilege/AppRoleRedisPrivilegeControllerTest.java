package cn.com.open.opensass.privilege;


import cn.com.open.opensass.privilege.api.AppRoleRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class AppRoleRedisPrivilegeControllerTest extends BaseTest{

	@Autowired
	private AppRoleRedisPrivilegeController fixture;

	@Test //	/AppRole/getAppRoleRedis
	public void getAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAppRoleRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}

	@Test //	/AppRole/delAppRoleRedis
	public void delAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppRoleRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}
	@Test //	/AppRole/updateAppRoleRedis
	public void updateAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppRoleRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}

}
