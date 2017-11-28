package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeRoleController;
import cn.com.open.opensass.privilege.base.BaseTest;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class PrivilegeRoleControllerTest extends BaseTest {

	@Autowired
	private PrivilegeRoleController privilegeRoleController;

	/**
	 * 正常获取数据.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getRole() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("start", "0");
		request.addParameter("limit", "10");
		MockHttpServletResponse response = new MockHttpServletResponse();
		privilegeRoleController.getRole(request, response);
		System.out.println("getRole:"+response.getContentAsString());
		log.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("roleList"));
	}
	/**
	 * 正常获取数据.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getRoleType() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("start", "0");
		request.addParameter("limit", "10");
		request.addParameter("roleType", "2");
		MockHttpServletResponse response = new MockHttpServletResponse();
		privilegeRoleController.getRole(request, response);
		System.out.println("getRole:"+response.getContentAsString());
		log.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("roleList"));
	}
	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getRoleParam() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "");
		request.addParameter("start", "0");
		request.addParameter("limit", "10");
		MockHttpServletResponse response = new MockHttpServletResponse();
		privilegeRoleController.getRole(request, response);
		System.out.println("getRoleParam:"+response.getContentAsString());
		log.info(response.getContentAsString());
		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
}
