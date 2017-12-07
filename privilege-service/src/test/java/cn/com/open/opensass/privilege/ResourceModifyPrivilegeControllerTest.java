package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.ResourceModifyPrivilegeController;
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
 * 项目名称 : 资源修改接口单元测试.
 * 创建日期 : 2017/12/1 14:14.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/12/1][14:14]创建文件 by JACKIE.
 */
public class ResourceModifyPrivilegeControllerTest extends BaseTest {
    @Autowired
    ResourceModifyPrivilegeController resourceModifyPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 正常操作
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyResourceNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("resourceName","test");
        request.addParameter("resourceRule","TEST");
        request.addParameter("createUser","TEST");
        request.addParameter("resourceLevel","1");
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("baseUrl","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("resourceId","1001f7b1fb524ff3b7e565a3159b3bb8");
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceModifyPrivilegeController.modifyResource(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * APP为空，以及验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyResourceAppNullValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("resourceName","test");
        request.addParameter("resourceRule","TEST");
        request.addParameter("createUser","TEST");
        request.addParameter("resourceLevel","1");
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("baseUrl","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("resourceId","1001f7b1fb524ff3b7e565a3159b3bb8");
        request.removeParameter("appKey");
        request.addParameter("appKey","TEST");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceModifyPrivilegeController.modifyResource(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 修改的资源不存在.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyResourceNoExist() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("resourceName","test");
        request.addParameter("resourceRule","TEST");
        request.addParameter("createUser","TEST");
        request.addParameter("resourceLevel","1");
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("baseUrl","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("resourceId","1001f7b1fb524ff3b7e565a3159b3bb82222222222");
        MockHttpServletResponse response = new MockHttpServletResponse();
        resourceModifyPrivilegeController.modifyResource(request, response);

        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
