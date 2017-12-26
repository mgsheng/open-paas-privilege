package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.MenuModifyPrivilegeController;
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

 * 描述： 权限服务接口，菜单修改接口

 * 修改人： LILI

 * 修改时间：2017/12/26 16:07

 * 修改内容：新增

 */
public class MenuModifyPrivilegeControllerTest extends BaseTest {
    @Autowired
    MenuModifyPrivilegeController menuModifyPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyMenuNormal() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuModifyPrivilegeController.modifyMenu(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals(1));
    }

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyMenuParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuModifyPrivilegeController.modifyMenu(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyMenuValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","TEST");
        redisClientTemplate.del(RedisConstant.APP_INFO + CommonEnum.APP_ID.getCode());
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuModifyPrivilegeController.modifyMenu(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyMenuNormalParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("menuId","00ac0e93925b4a1284de69ca850ae72b");
        request.addParameter("menuName","tse");
        request.addParameter("menuRule","test");
        request.addParameter("menuCode","test");
        request.addParameter("menuLevel","222");
        request.addParameter("parentId","222");
        request.addParameter("dislayOrder","3333");
        request.addParameter("status","2222");
        MockHttpServletResponse response = new MockHttpServletResponse();
        menuModifyPrivilegeController.modifyMenu(request, response);
        System.out.println("test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals(1));
    }
}
