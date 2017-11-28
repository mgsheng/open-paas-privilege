package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.FunctionGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class FunctionGetPrivilegeControllerTest extends BaseTest {

    @Autowired
    private FunctionGetPrivilegeController functionGetPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Test //  /function/getFunctionIdList")
    public void modifyFunction() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        request.addParameter("appUserId", "3094776");
        request.addParameter("resourceId", "151,185");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        JSONObject json = JSON.parseObject(response.getContentAsString());
        Assert.assertEquals("0", json.getString("status"));
    }
    /**
     * 获取数据为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyFunctionParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId", "");
        request.addParameter("resourceId", "151,185");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        log.info(response.getContentAsString());
        System.out.println("modifyFunctionParamNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空,暂无数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyFunctionAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId", "3094776");
        request.addParameter("resourceId", "151,185");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        log.info(response.getContentAsString());
        System.out.println("modifyFunctionAppNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 正常操作，有数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyFunctionSuccess() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId", "1503143");
        request.addParameter("resourceId", "1326,10629");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        log.info(response.getContentAsString());
        System.out.println("modifyFunctionSuccess:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyFunctionValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("appUserId", "1503143");
        request.addParameter("resourceId", "1326,10629");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionGetPrivilegeController.modifyFunction(request, response);
        log.info(response.getContentAsString());
        System.out.println("modifyFunctionValidFailed:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
