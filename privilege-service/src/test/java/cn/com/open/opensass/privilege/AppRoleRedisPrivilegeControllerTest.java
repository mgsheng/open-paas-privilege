package cn.com.open.opensass.privilege;


import cn.com.open.opensass.privilege.api.AppRoleRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class AppRoleRedisPrivilegeControllerTest extends BaseTest{

	@Autowired
	private AppRoleRedisPrivilegeController fixture;
	@Autowired
	RedisClientTemplate redisClientTemplate;

	/**
	 * 正常操作
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAppRoleRedisPrivilege(request, response);
		System.out.println(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("roleList"));
	}

	/**
	 * 参数为空
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getAppRoleRedisPrivilegeParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAppRoleRedisPrivilege(request, response);
		Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}


	/**
	 * 正常操作
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void delAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppRoleRedisPrivilege(request, response);
		Assert.assertEquals("Failed", response.getContentAsString());
	}

	/**
	 * 参数为空
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void delAppRoleRedisParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.delAppRoleRedisPrivilege(request, response);
		Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}

	/**
	 * 正常操作
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void updateAppRoleRedisPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "28");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppRoleRedisPrivilege(request, response);
		Assert.assertTrue(response.getContentAsString().contains("roleList"));
	}

	/**
	 * 参数为空
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void updateAppRoleRedisPrivilegeParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.updateAppRoleRedisPrivilege(request, response);
		Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
}
