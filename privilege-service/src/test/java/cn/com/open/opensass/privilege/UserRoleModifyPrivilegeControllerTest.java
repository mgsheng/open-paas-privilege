package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleModifyPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * 项目名称 : 用户角色更新单元测试.
 * 创建日期 : 2017/11/29 15:10.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/29][15:10]创建文件 by JACKIE.
 */
public class UserRoleModifyPrivilegeControllerTest extends BaseTest{
    @Autowired
    UserRoleModifyPrivilegeController userRoleModifyPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    public static final String SIGN = RedisConstant.SIGN;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("");
        privilegeUserVo.setMethod("");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyParamNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 用户验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyUserValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test——test");
        privilegeUserVo.setMethod("0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyUserValidFailed:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空，角色为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1000478");
        privilegeUserVo.setMethod("0");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyAppNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * app为空，角色不为空，添加角色.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyAppNullAddRoleNotNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1109918");
        privilegeUserVo.setMethod("0"); //添加角色
        privilegeUserVo.setPrivilegeRoleId("060513abc772fe7cd1ac69be15837430");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyAppNullAddRoleNotNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * app为空，角色不为空，删除角色.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyAppNullDelRoleNotNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1109918");
        privilegeUserVo.setMethod("1"); //添加角色
        privilegeUserVo.setPrivilegeRoleId("060513abc772fe7cd1ac69be15837430");
        //清空大缓存
        StringBuilder redisUserPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
        redisUserPrivilegeKey.append(privilegeUserVo.getAppId());
        redisUserPrivilegeKey.append(SIGN);
        redisUserPrivilegeKey.append(privilegeUserVo.getAppUserId());
        redisClientTemplate.setObject(redisUserPrivilegeKey.toString(),"test");
        StringBuilder redisUserAllPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
        redisUserAllPrivilegeKey.append(privilegeUserVo.getAppId());
        redisUserAllPrivilegeKey.append(SIGN);
        redisUserAllPrivilegeKey.append(privilegeUserVo.getAppUserId());
        redisClientTemplate.setObject(redisUserAllPrivilegeKey.toString(),"test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyAppNullDelRoleNotNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1000478");
        privilegeUserVo.setMethod("0");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleModifyPrivilegeController.modifyPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyValidFailed:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
