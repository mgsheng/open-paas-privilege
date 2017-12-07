package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.FunctionDelPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * 项目名称 : 权限功能删除接口单元测试.
 * 创建日期 : 2017/11/28 17:53.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/28][17:53]创建文件 by JACKIE.
 */
public class FunctionDelPrivilegeControllerTest extends BaseTest {
    @Autowired
    FunctionDelPrivilegeController functionDelPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delFunctionParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("functionId", "");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionDelPrivilegeController.delFunction(request, response);

        System.out.println("delFunctionParamNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delFunctionAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("functionId", "7b647a7d88f4d6319bf0d600d168dbeb");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionDelPrivilegeController.delFunction(request, response);

        System.out.println("delFunctionAppNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delFunctionValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("functionId", "7b647a7d88f4d6319bf0d600d168dbeb");
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionDelPrivilegeController.delFunction(request, response);

        System.out.println("delFunctionValidFailed:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * functionids为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void delFunctionFunctionIdsNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("functionId", ",");
        MockHttpServletResponse response = new MockHttpServletResponse();
        functionDelPrivilegeController.delFunction(request, response);

        System.out.println("delFunctionFunctionIdsNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
