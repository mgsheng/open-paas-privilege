package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * 项目名称 : 用户角色权限获取单元测试.
 * 创建日期 : 2017/11/15 17:14.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/15][17:14]创建文件 by JACKIE.
 */
public class UserRoleGetPrivilegeControllerTest  extends BaseTest {
    @Autowired
    UserRoleGetPrivilegeController userRoleGetPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;
    String appUserId = "20769";

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("");
        privilegeUserVo.setMenuCode("");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId(appUserId);
        privilegeUserVo.setMenuCode("1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        redisClient.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);
        System.out.println("nomarl:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeNomarlA() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("8653");
        privilegeUserVo.setMenuCode("1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        redisClient.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);
        System.out.println("nomarl:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 正常操作管理员.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeNomarlAdmin() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1475");
        privilegeUserVo.setMenuCode("6");
        MockHttpServletResponse response = new MockHttpServletResponse();
        redisClient.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);
        System.out.println("nomarl:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * App为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId(appUserId);
        privilegeUserVo.setMenuCode("1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        redisClient.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId(appUserId);
        privilegeUserVo.setMenuCode("2");
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");//应用id（必传）
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 用户不存在.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPrivilegeNoUser() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("0");
        privilegeUserVo.setMenuCode("2");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
