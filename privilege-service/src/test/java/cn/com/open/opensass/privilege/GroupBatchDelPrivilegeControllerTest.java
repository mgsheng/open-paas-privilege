package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupBatchDelPrivilegeController;
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

/**
 * 项目名称 : 批量删除groupid单元测试.
 * 创建日期 : 2017/11/16 9:58.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/16][9:58]创建文件 by JACKIE.
 */
public class GroupBatchDelPrivilegeControllerTest extends BaseTest {
    @Autowired
    GroupBatchDelPrivilegeController groupBatchDelPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;
    String groupId = "5016EF92A96DF65AE0535088640A081A";
    String resourceId = "42";
    String functionId = "2a2aaa4e24d8b7d984565f18e99d0502,3ffc5b25a963b015e5a4426bebef24c5";
    /**
     * 正常操作，无数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","488A74408834169DE053BD86640A49F5");
        request.addParameter("resourceId","12");
        request.addParameter("functionId","2a2aaa4e24d8b7d984565f18e99d0502,3ffc5b25a963b015e5a4426bebef24c5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 正常操作，无数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeNomarlNoData() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("functionId",functionId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 未找到相关数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeNoData() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("functionId",functionId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("resourceId","");
        request.addParameter("functionId",functionId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * App为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("functionId",functionId);
        redisClient.del(RedisConstant.APP_INFO + CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * App为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyDelPrivilegeValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("functionId",functionId);
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");//应用id（必传）
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchDelPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
