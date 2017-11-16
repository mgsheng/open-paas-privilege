package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.AppMenuRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
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
		JSONObject json = JSON.parseObject(response.getContentAsString());
		String status = json.getString("status");
		Assert.assertEquals("0", status);
	}

	@Test // 	/AppMenu/delAppMenuRedis
	public void delAppMenuRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppMenuRedisPrivilege(request, response);
		String str = response.getContentAsString();
		Assert.assertEquals("Failed", str);
	}

	@Test //	/AppMenu/updateAppMenuRedis
	public void updateAppMenuRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppMenuRedisPrivilege(request, response);
		JSONObject json = JSON.parseObject(response.getContentAsString());
		String status = json.getString("status");
		Assert.assertEquals("0", status);
	}

}
