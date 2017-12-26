package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserGetPrivilegeTreeController;
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


public class UserGetPrivilegeTreeControllerTest extends BaseTest {

	@Autowired
	private UserGetPrivilegeTreeController userGetPrivilegeTreeController;
    @Autowired
    private RedisClientTemplate redisClient;

    /**
     * 用户菜单权限获取接口.
     * @throws UnsupportedEncodingException
     */
	@Test
	public void getPrivilegeTree() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String appUserId = "3094776";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(appId);
        privilegeUserVo.setAppUserId(appUserId);
        userGetPrivilegeTreeController.getPrivilege(request, response, privilegeUserVo);

        Assert.assertEquals("1", JSONObject.parseObject(response.getContentAsString()).getString("status"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId("");
        privilegeUserVo.setAppUserId("");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userGetPrivilegeTreeController.getPrivilege(request, response,privilegeUserVo);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeeValidFailed() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String appUserId = "3094776";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(appId);
        privilegeUserVo.setAppUserId(appUserId);
        redisClient.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");//应用id（必传）
        userGetPrivilegeTreeController.getPrivilege(request, response, privilegeUserVo);
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 没有此用户.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeeNoUser() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String appUserId = "30947762222";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(appId);
        privilegeUserVo.setAppUserId(appUserId);
        userGetPrivilegeTreeController.getPrivilege(request, response, privilegeUserVo);
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
