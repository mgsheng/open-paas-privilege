package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserGetMenusController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserGetMenusControllerTest extends BaseTest {

	@Autowired
	private UserGetMenusController userGetMenusController;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	String appUserId = "3094776";

	/**
	 * 正常处理
	 * @throws UnsupportedEncodingException
	 */
	@Test //	/userRole/getUserPrivilegeMenus
	public void getPrivilege() throws UnsupportedEncodingException {
		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";

		MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse response = new MockHttpServletResponse();
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		userGetMenusController.getPrivilege(request, response, modifyPrivilegeUserVo);

		Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
	}
	/**
	 * 参数为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeParamNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId("");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("ParamNull:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
	/**
	 * app为空.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeAppNull() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		redisClientTemplate.del(RedisConstant.APP_INFO +CommonEnum.APP_ID.getDisplay());
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("AppNull:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
	}
	/**
	 * 验证失败.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeValidFailed() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		request.removeParameter("appKey");
		request.addParameter("appKey","test");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("getPrivilegeValidFailed:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
	/**
	 * 用户不存在.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeUserNotExist() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId("test_Test1");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("getPrivilegeUserNotExist:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
	}
	/**
	 * 用户存在-管理员.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeAdminUserExist() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId("1000478");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("getPrivilegeAdminUserExist:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
	}

	/**
	 * 用户存在-管理员.
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getPrivilegeNormalUserExist() throws UnsupportedEncodingException {
		MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
		modifyPrivilegeUserVo.setAppUserId("1004098");
		MockHttpServletResponse response = new MockHttpServletResponse();
		userGetMenusController.getPrivilege(request, response,modifyPrivilegeUserVo);

		System.out.println("getPrivilegeNormalUserExist:"+response.getContentAsString());
		Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
	}
}
