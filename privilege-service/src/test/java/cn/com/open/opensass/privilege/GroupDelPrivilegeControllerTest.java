package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupDelPrivilegeController;
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

/*

 * 文件名：cn.com.open.opensass.privilege

 * 版权： 权限服务

 * 描述： 组织机构权限删除接口（删除当前组织机构所有权限）

 * 修改人： LILI

 * 修改时间：2017/12/26 15:57

 * 修改内容：新增

 */
public class GroupDelPrivilegeControllerTest extends BaseTest {
    @Autowired
    GroupDelPrivilegeController groupDelPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupDelNoral() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","488A74408834169DE053BD86640A49F5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupDelPrivilegeController.delPrivilege(request, response);
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupDelParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","488A74408834169DE053BD86640A49F5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupDelPrivilegeController.delPrivilege(request, response);
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupDelValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","488A74408834169DE053BD86640A49F5");
        redisClientTemplate.del(RedisConstant.APP_INFO + CommonEnum.APP_ID.getCode());
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupDelPrivilegeController.delPrivilege(request, response);
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
