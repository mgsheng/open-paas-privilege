package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserGetPrivilegeControllerTest extends BaseTest {
	
	@Autowired
	private UserGetPrivilegeController fixture;
	@Autowired
	private RedisClientTemplate redisClient;

	/**
	 * 正常操作.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserId = "3094776";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		fixture.getPrivilege(request, response, modifyPrivilegeUserVo);

		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}

	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeParamNull() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserId = "3094776";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId("");
		modifyPrivilegeUserVo.setAppUserId("");
		fixture.getPrivilege(request, response, modifyPrivilegeUserVo);

		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}

	/**
	 * 验证失败.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeValidFailed() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserId = "3094776";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		redisClient.del(RedisConstant.APP_INFO + appId);
		request.removeParameter("appKey");
		request.addParameter("appKey", "test");//应用id（必传）
		fixture.getPrivilege(request, response, modifyPrivilegeUserVo);

		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}

	/**
	 * 没有此用户.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeNoUser() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserId = "30947763333333333";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		fixture.getPrivilege(request, response, modifyPrivilegeUserVo);

		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
}
