package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserRoleRedisPrivilegeControllerTest extends BaseTest {

	@Autowired
	private UserRoleRedisPrivilegeController userRoleRedisPrivilegeController;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	public static final String SIGN = RedisConstant.SIGN;

	@Test 	// 	/userRole/getUserRoleRedis")
	public void getUserPrivilege() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.getUserPrivilege(request, response);
		log.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("functionList"));
	}

	@Test  	//	/userRole/updateUserRoleRedis
	public void updateUserRoleRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.updateUserRoleRedis(request, response);
		log.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().contains("functionList"));
	}
	
	@Test //	/userRole/deleteUserRoleRedis
	public void delUserRoleRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("appId", "8");
		request.addParameter("appUserId", "3094776");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.delUserRoleRedis(request, response);
		log.info(response.getContentAsString());
		Assert.assertEquals("Success", response.getContentAsString());
	}


	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getUserPrivilegeParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
		request.addParameter("appUserId", "");
		redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.getUserPrivilege(request, response);
		log.info(response.getContentAsString());
		System.out.println("getUserPrivilegeParamNull:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
	/**
	 * 数据为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getUserPrivilegeUserNoExist() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
		request.addParameter("appUserId", "test_test1");
		redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.getUserPrivilege(request, response);
		log.info(response.getContentAsString());
		System.out.println("getUserPrivilegeUserNoExist:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}

	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void updateUserRoleRedisParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
		request.addParameter("appUserId", "");
		redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.updateUserRoleRedis(request, response);
		log.info(response.getContentAsString());
		System.out.println("updateUserRoleRedisParamNull:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
	/**
	 * 删除缓存.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void updateUserRoleRedisDelRedis() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
		request.addParameter("appUserId", "test_test");
		redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
		StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
		redisUserPrivilegeKey.append(CommonEnum.APP_ID.getDisplay());
		redisUserPrivilegeKey.append(SIGN);
		redisUserPrivilegeKey.append("3094776");
		redisClientTemplate.setObject(String.valueOf(redisUserPrivilegeKey),"test");
		StringBuilder redisUserAllPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
		redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
		redisUserAllPrivilegeKey.append(CommonEnum.APP_ID.getDisplay());
		redisUserAllPrivilegeKey.append(SIGN);
		redisUserAllPrivilegeKey.append("3094776");
		redisClientTemplate.setObject(String.valueOf(redisUserAllPrivilegeKey),"test");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userRoleRedisPrivilegeController.updateUserRoleRedis(request, response);
		log.info(response.getContentAsString());
		System.out.println("updateUserRoleRedisDelRedis:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
}
