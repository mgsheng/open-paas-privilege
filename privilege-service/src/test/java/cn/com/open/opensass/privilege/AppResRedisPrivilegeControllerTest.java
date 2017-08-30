package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.AppResRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
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
		log.info(response.getContentAsString());
	}

	@Test   //	/AppRes/delAppResRedis
	public void delAppResRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppResRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}
	@Test   //	/AppRes/updateAppResRedis
	public void updateAppResRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppResRedisPrivilege(request, response);
		log.info(response.getContentAsString());
	}
}
