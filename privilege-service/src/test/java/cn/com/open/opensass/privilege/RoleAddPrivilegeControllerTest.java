package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.RoleAddPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
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

 * 描述： 权限服务接口，管理用户权限

 * 修改人： LILI

 * 修改时间：2017/12/29 16:33

 * 修改内容：新增

 */
public class RoleAddPrivilegeControllerTest extends BaseTest {
    @Autowired
    RoleAddPrivilegeController roleAddPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;

    /**
     * 新增正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delRoleNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("roleName", "test");
        request.addParameter("rolePrivilege", "test");
        request.addParameter("roleLevel", "0");
        request.addParameter("roleType", "2");
        request.addParameter("parentRoleId", "0");
        request.addParameter("groupId", "test");
        request.addParameter("groupName", "TEst");
        request.addParameter("status", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleAddPrivilegeController.addRole(request, response);
        Assert.assertEquals("1", com.alibaba.fastjson.JSONObject.parseObject(response.getContentAsString()).getString("status"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void putDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.removeParameter("appId");
        request.addParameter("appId","");
        request.addParameter("roleName", "");
        request.addParameter("rolePrivilege", "test");
        request.addParameter("roleLevel", "0");
        request.addParameter("roleType", "2");
        request.addParameter("parentRoleId", "0");
        request.addParameter("groupId", "test");
        request.addParameter("groupName", "TEst");
        request.addParameter("status", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleAddPrivilegeController.addRole(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delRoleValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("roleName", "test");
        request.addParameter("rolePrivilege", "test");
        request.addParameter("roleLevel", "0");
        request.addParameter("roleType", "2");
        request.addParameter("parentRoleId", "0");
        request.addParameter("groupId", "test");
        request.addParameter("groupName", "TEst");
        request.addParameter("status", "0");
        redisClient.del(RedisConstant.APP_INFO +CommonEnum.APP_ID.getDisplay());
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleAddPrivilegeController.addRole(request, response);
        Assert.assertEquals("0", com.alibaba.fastjson.JSONObject.parseObject(response.getContentAsString()).getString("status"));
    }
}
