package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleAddPrivilegeController;
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

/*

 * 文件名：cn.com.open.opensass.privilege

 * 版权： 权限服务

 * 描述： 权限服务接口，管理用户权限，用户角色新增的单元测试.

 * 修改人： LILI

 * 修改时间：2017/12/4 11:52

 * 修改内容：新增

 */
public class UserRoleAddPrivilegeControllerTest extends BaseTest{
    @Autowired
    UserRoleAddPrivilegeController userRoleAddPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    public static final String SIGN = RedisConstant.SIGN;
    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddNormal()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        privilegeUserVo.setPrivilegeRoleId("test");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddParamNull()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("");
        privilegeUserVo.setPrivilegeRoleId("test");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空,验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddAppNullValidFailed()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        privilegeUserVo.setPrivilegeRoleId("test");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        redisClientTemplate.del(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 该业务用户已存在.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddExistUser()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("1000478");
        privilegeUserVo.setPrivilegeRoleId("test");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 删除缓存.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddRedisDel()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        privilegeUserVo.setPrivilegeRoleId("test");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");
        //清空大缓存
        StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
        redisUserPrivilegeKey.append(privilegeUserVo.getAppId());
        redisUserPrivilegeKey.append(SIGN);
        redisUserPrivilegeKey.append(privilegeUserVo.getAppUserId());
        redisClientTemplate.setObject(redisUserPrivilegeKey.toString(),"test");
        StringBuilder redisUserAllPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
        redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
        redisUserAllPrivilegeKey.append(privilegeUserVo.getAppId());
        redisUserAllPrivilegeKey.append(SIGN);
        redisUserAllPrivilegeKey.append(privilegeUserVo.getAppUserId());
        redisClientTemplate.setObject(redisUserAllPrivilegeKey.toString(),"test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 用户角色关系添加失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleAddFailed()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("test");
        privilegeUserVo.setPrivilegeRoleId(",te");
        privilegeUserVo.setAppUserName("testname");
        privilegeUserVo.setDeptId("test");
        privilegeUserVo.setGroupId("test");
        privilegeUserVo.setResourceId("test");
        privilegeUserVo.setPrivilegeFunId("test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleAddPrivilegeController.addRole(request, response,privilegeUserVo);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
