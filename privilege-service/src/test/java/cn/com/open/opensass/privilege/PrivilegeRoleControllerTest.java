package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeRoleController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class PrivilegeRoleControllerTest extends BaseTest {

	@Autowired
	private PrivilegeRoleController fixture;

	@Test // /role/getRole
	public void getRole() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("start", "0");
		request.addParameter("limit", "10");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getRole(request, response);
		log.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("roleList"));
	}
}
