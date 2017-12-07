package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeUserCacheDelController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
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
 * 项目名称 : 删除用户相关缓存单元测试.
 * 创建日期 : 2017/11/20 11:08.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/20][11:08]创建文件 by JACKIE.
 */
public class PrivilegeUserCacheDelControllerTest  extends BaseTest {
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    PrivilegeUserCacheDelController privilegeUserCacheDelController;
    public static final String SIGN = RedisConstant.SIGN;
    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void privilegeUserCacheDelNomal() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        privilegeUserCacheDelController.delUserRedisCache(request, response,privilegeUserVo);

        System.out.println("normal:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * App为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void privilegeUserCacheDelAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        redisClient.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        privilegeUserCacheDelController.delUserRedisCache(request, response,privilegeUserVo);

        System.out.println("AppNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void privilegeUserCacheDelParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("");
        privilegeUserCacheDelController.delUserRedisCache(request, response,privilegeUserVo);

        System.out.println("ParamNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void privilegeUserCacheDelValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        privilegeUserCacheDelController.delUserRedisCache(request, response,privilegeUserVo);

        System.out.println("ValidFailed:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 用户redis缓存删除.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void privilegeUserCacheDelUserCache() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        String appUserId = "test";
        String appId = CommonEnum.APP_ID.getDisplay();
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(appId);
        privilegeUserVo.setAppUserId(appUserId);
        //用户redis缓存key拼接,删除
        //privilegeService_userAllCacheInfo_appid_appuserid
        StringBuilder redisUserAllPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
        redisUserAllPrivilegeKey.append(appId);
        redisUserAllPrivilegeKey.append(SIGN);
        redisUserAllPrivilegeKey.append(appUserId);
        redisClient.setObject(redisUserAllPrivilegeKey.toString(),"test data：redisUserAllPrivilegeKey");
        //清空大缓存
        //privilegeService_userCacheInfo_appid_appuserid
        StringBuilder redisUserPrivilegeKey = new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
        redisUserPrivilegeKey.append(appId);
        redisUserPrivilegeKey.append(SIGN);
        redisUserPrivilegeKey.append(appUserId);
        redisClient.setObject(redisUserPrivilegeKey.toString(),"test data：redisUserPrivilegeKey");

        //清空角色以及url
        //privilegeService_userCacheUrl_appid_appuserid
        StringBuilder redisUserUrlPrivilegeKey = new StringBuilder(RedisConstant.USERPRIVILEGES_CACHE);
        redisUserUrlPrivilegeKey.append(appId);
        redisUserUrlPrivilegeKey.append(SIGN);
        redisUserUrlPrivilegeKey.append(appUserId);
        redisClient.setObject(redisUserUrlPrivilegeKey.toString(),"test data：redisUserUrlPrivilegeKey");
        //清空用户菜单缓存
        //privilegeService_userCacheMenus_appid_appuserid
        StringBuilder redisUserCacheMenusPrivilegeKey = new StringBuilder(RedisConstant.USERMENU_CACHE);
        redisUserCacheMenusPrivilegeKey.append(appId);
        redisUserCacheMenusPrivilegeKey.append(SIGN);
        redisUserCacheMenusPrivilegeKey.append(appUserId);
        redisClient.setObject(redisUserCacheMenusPrivilegeKey.toString(),"test data：redisUserCacheMenusPrivilegeKey");
        //清空用户角色缓存
        //privilegeService_userCacheMenus_appid_appuserid
        StringBuilder redisUserRoleCachePrivilegeKey = new StringBuilder(RedisConstant.USERROLE_CACHE);
        redisUserRoleCachePrivilegeKey.append(appId);
        redisUserRoleCachePrivilegeKey.append(SIGN);
        redisUserRoleCachePrivilegeKey.append(appUserId);
        redisClient.setObject(redisUserRoleCachePrivilegeKey.toString(),"test data：redisUserRoleCachePrivilegeKey");
        //删除操作开始
        privilegeUserCacheDelController.delUserRedisCache(request, response,privilegeUserVo);

        System.out.println("AppNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
