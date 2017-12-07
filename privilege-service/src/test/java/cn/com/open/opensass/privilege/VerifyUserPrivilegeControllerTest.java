package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.VerifyUserPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class VerifyUserPrivilegeControllerTest extends BaseTest {

	@Autowired
	private VerifyUserPrivilegeController verifyUserPrivilegeController;
	@Autowired
	private RedisClientTemplate redisClient;
	public static final String SIGN = RedisConstant.SIGN;

	/**
	 * 正常操作.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserid = "3094776";
		String optUrl = "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		request.addParameter("appUserId", appUserid);
		request.addParameter("optUrl", optUrl);
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}/**
	 * 判断是否是管理员.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyAdminUserPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";
		String appUserid = "1000478";
		String optUrl = "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		request.addParameter("appUserId", appUserid);
		request.addParameter("optUrl", optUrl);
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);
		System.out.println("verifyAdminUserPrivilege:"+response.getContentAsString());

		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilegeParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appUserId","3094776");
		request.addParameter("optUrl", "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu");
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		System.out.println("verifyUserPrivilegeParamNull:"+response.getContentAsString());
		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * App为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilegeAppNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appUserId","");
		request.addParameter("optUrl", "");
		redisClient.del(RedisConstant.APP_INFO +  CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		System.out.println("verifyUserPrivilegeAppNull:"+response.getContentAsString());
		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * 验证失败.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilegeValidFailed() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appUserId","3094776");
		request.addParameter("optUrl", "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu");
		request.removeParameter("appKey");
		request.addParameter("appKey","test");
		redisClient.del(RedisConstant.APP_INFO +  CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		System.out.println("verifyUserPrivilegeValidFailed:"+response.getContentAsString());
		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * 若没有该用户 返回认证失败.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilegeNoUserId() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appUserId","test_test_d");
		request.addParameter("optUrl", "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu");
		redisClient.del(RedisConstant.APP_INFO +  CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		System.out.println("verifyUserPrivilegeNoUserId:"+response.getContentAsString());
		Assert.assertEquals("0", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * 若没有该用户 返回认证失败.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void verifyUserPrivilegeDelRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appUserId","3094776");
		request.addParameter("optUrl", "/Query/condition/lcenter_8.aspx?Stat=_MatricTestStu");
		redisClient.del(RedisConstant.APP_INFO +  CommonEnum.APP_ID.getDisplay());
		StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
		redisUserPrivilegeKey.append(CommonEnum.APP_ID.getDisplay());
		redisUserPrivilegeKey.append(SIGN);
		redisUserPrivilegeKey.append("3094776");
		redisClient.setObject(String.valueOf(redisUserPrivilegeKey),"test");
		StringBuilder redisUserAllPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
		redisUserAllPrivilegeKey.append(CommonEnum.APP_ID.getDisplay());
		redisUserAllPrivilegeKey.append(SIGN);
		redisUserAllPrivilegeKey.append("3094776");
		redisClient.setObject(String.valueOf(redisUserAllPrivilegeKey),"test");
		MockHttpServletResponse response = new MockHttpServletResponse();
		verifyUserPrivilegeController.verifyUserPrivilege(request, response);

		System.out.println("verifyUserPrivilegeNoUserId:"+response.getContentAsString());
		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
}
