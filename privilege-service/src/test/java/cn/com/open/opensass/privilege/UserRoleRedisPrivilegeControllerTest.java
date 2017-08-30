package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserRoleRedisPrivilegeControllerTest extends BaseTest {

	@Autowired
	private UserRoleRedisPrivilegeController fixture;

	@Test 	// 	/userRole/getUserRoleRedis")
	public void getUserPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getUserPrivilege(request, response);
		log.info(response.getContentAsString());
	}

	@Test  	//	/userRole/updateUserRoleRedis
	public void updateUserRoleRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateUserRoleRedis(request, response);
		log.info(response.getContentAsString());
	}
	
	@Test //	/userRole/deleteUserRoleRedis
	public void delUserRoleRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delUserRoleRedis(request, response);
		log.info(response.getContentAsString());
	}


}
