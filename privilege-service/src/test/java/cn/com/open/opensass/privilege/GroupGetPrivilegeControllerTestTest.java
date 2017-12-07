package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupGetPrivilegeControllerTest;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class GroupGetPrivilegeControllerTestTest extends BaseTest {

	@Autowired
	private GroupGetPrivilegeControllerTest fixture;

	@Test //	/groupTest/getGroupPrivilegeRedis
	public void getGroupPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("groupId", "50C935EB614F532AE0533312640A8C66");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getGroupPrivilege(request, response);

		Assert.assertTrue(response.getContentAsString().contains("menuList"));
	}

	@Test //	/groupTest/deleteGroupPrivilegeRedis")
	public void delGroupPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("groupId", "50C935EB614F532AE0533312640A8C66");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delGroupPrivilege(request, response);

		Assert.assertEquals("Success", response.getContentAsString());
	}
	@Test //	/groupTest/updateGroupPrivilegeRedis
	public void updateGroupPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("groupId", "50C935EB614F532AE0533312640A8C66");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateGroupPrivilege(request, response);

		Assert.assertTrue(response.getContentAsString().contains("menuList"));
	}

}