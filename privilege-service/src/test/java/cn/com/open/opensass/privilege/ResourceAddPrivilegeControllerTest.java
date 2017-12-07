package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.ResourceAddPrivilegeController;
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

/**
 * 项目名称 : 菜单添加接口单元测试.
 * 创建日期 : 2017/11/29 18:00.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/29][18:00]创建文件 by JACKIE.
 */
public class ResourceAddPrivilegeControllerTest extends BaseTest {
    @Autowired
    ResourceAddPrivilegeController resourceAddPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    public static final String SIGN = RedisConstant.SIGN;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addMenuParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("resourceName","");
        request.addParameter("resourceRule","");
        request.addParameter("createUser","");
        request.addParameter("appId","");
        request.addParameter("resourceLevel","");
        request.addParameter("menuId","");
        request.addParameter("baseUrl","");
        request.addParameter("createUserId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceAddPrivilegeController.addMenu(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 用户验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addMenuValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("resourceName","2222");
        request.addParameter("resourceRule","");
        request.addParameter("createUser","");
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("resourceLevel","1");
        request.addParameter("menuId","");
        request.addParameter("baseUrl","");
        request.addParameter("createUserId","");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        redisClientTemplate.del(RedisConstant.APP_INFO+ CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceAddPrivilegeController.addMenu(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 用户验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addMenuNomal() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("resourceName","学费超限调整——test");
        request.addParameter("resourceRule","test");
        request.addParameter("createUser","test");
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("resourceLevel","1");
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72c");
        request.addParameter("baseUrl","https://www.baidu.com");
        request.addParameter("createUserId","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceAddPrivilegeController.addMenu(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
