package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.ResourceGetPrivilegeController;
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
 * 项目名称 : 权限资源查询接口单元测试.
 * 创建日期 : 2017/11/28 15:44.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/28][15:44]创建文件 by JACKIE.
 */
public class ResourceGetPrivilegeControllerTest extends BaseTest {
    @Autowired
    private ResourceGetPrivilegeController resourceGetPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClientTemplate;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getResPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("menuId","");
        request.addParameter("start","");
        request.addParameter("Limit","10");
        request.addParameter("resourceLevel","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceGetPrivilegeController.getResPrivilege(request, response);

        System.out.println("getResPrivilegeParamNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getResPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("menuId","");
        request.addParameter("start","0");
        request.addParameter("Limit","10");
        request.addParameter("resourceLevel","");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceGetPrivilegeController.getResPrivilege(request, response);

        System.out.println("getResPrivilegeAppNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 获取列表为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getResPrivilegeListNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("menuId","111111111111111");
        request.addParameter("start","0");
        request.addParameter("Limit","10");
        request.addParameter("resourceLevel","");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceGetPrivilegeController.getResPrivilege(request, response);

        System.out.println("getResPrivilegeListNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
