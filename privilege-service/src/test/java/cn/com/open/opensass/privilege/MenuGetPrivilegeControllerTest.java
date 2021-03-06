package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.MenuGetPrivilegeController;
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

 * 修改时间：2017/12/20 17:38

 * 修改内容：新增菜单查询接口单元测试

 */
public class MenuGetPrivilegeControllerTest extends BaseTest {
    @Autowired
    MenuGetPrivilegeController menuGetPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getMenusParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","");
        request.addParameter("start","");
        request.addParameter("Limit","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuGetPrivilegeController.getMenus(request, response);
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getMenusAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("start","1");
        request.addParameter("Limit","2");
        redisClientTemplate.del(RedisConstant.APP_INFO+ CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuGetPrivilegeController.getMenus(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getMenusValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("start","1");
        request.addParameter("Limit","2");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuGetPrivilegeController.getMenus(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 正常使用.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getMenusValidNormal() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","90c3923a65e246a596bee925a3a8282e");
        request.addParameter("start","1");
        request.addParameter("Limit","2");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuGetPrivilegeController.getMenus(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
