package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupResourceBatchModifyPrivilegeController;
import cn.com.open.opensass.privilege.api.UrlRedisPrivilegeController;
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
 * 项目名称 : project.
 * 创建日期 : 2017/11/16 11:10.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/16][11:10]创建文件 by JACKIE.
 */
public class UrlRedisPrivilegeControllerTest  extends BaseTest {
    @Autowired
    UrlRedisPrivilegeController urlRedisPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;
    String appUserId = "20769";

    /**
     * 新增正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void putDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.putdata(request, response);

        Object object = JSONObject.fromObject(response.getContentAsString()).get("urlList");
        Assert.assertTrue(object.toString().indexOf("http://")>0);
    }
    /**
     * 新增参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void putDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.putdata(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 新增没有查询到user.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void putDataError() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.putdata(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 修改正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void updateDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.updateData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("groupVersion").equals(7));
    }
    /**
     * 修改参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void updateDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.updateData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 修改没有查询到user.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void updateDataError() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.updateData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 删除正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.deleteData(request, response);

        Assert.assertTrue(response.getContentAsString().equals("Success"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.deleteData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 删除正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delDataFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","test_test_1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.deleteData(request, response);

        Assert.assertTrue(response.getContentAsString().equals("Failed"));
    }
    /**
     * 删除正常操作.
     * @throws UnsupportedEncodingException
     */
    /*@Test
    public void existUrlDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        request.addParameter("urladdr","http://open.com.cn");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existUrlData(request, response);

        System.out.println("existUrlDataNomarl:"+response.getContentAsString());
        Assert.assertTrue(response.getContentAsString().equals("Success"));
    }*/
    /**
     * 删除正常操作失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void existUrlDataNomarlFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","test_Test_1");
        request.addParameter("urladdr","http://open.com.cn");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existUrlData(request, response);

        Assert.assertTrue(response.getContentAsString().equals("FALSE"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void existUrlDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","");
        request.addParameter("urladdr","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existUrlData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 删除正常判断是否包含key.
     * @throws UnsupportedEncodingException
     */
    /*@Test
    public void existkeyDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existkeyData(request, response);

        Assert.assertTrue(response.getContentAsString().equals("Success"));
    }*/
    /**
     * 删除正常判断是否包含key.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void existkeyDataNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId",appUserId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existkeyData(request, response);

        Assert.assertTrue(response.getContentAsString().equals("TRUE"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void existkeyDataParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        urlRedisPrivilegeController.existkeyData(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
