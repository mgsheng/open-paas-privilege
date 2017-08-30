package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.AppMenuRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class AppMenuRedisPrivilegeControllerTest extends BaseTest {

	@Autowired
	private AppMenuRedisPrivilegeController fixture;

	// 	/AppMenu/getAppMenuRedis
	@Test
	public void getAppMenuRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAppMenuRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}

	@Test // 	/AppMenu/delAppMenuRedis
	public void delAppMenuRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppMenuRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}

	@Test //	/AppMenu/updateAppMenuRedis
	public void updateAppMenuRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppMenuRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}

}
