package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.MenuRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class MenuRedisPrivilegeControllerTest extends BaseTest {

    @Autowired
    private MenuRedisPrivilegeController fixture;

    @Test //    /menu/getMenu
    public void putRedisData() throws UnsupportedEncodingException {
        String appId = "8";
        String appUserId = "3094776";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("appUserId", appUserId);
        request.addParameter("appId", appId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.putRedisData(request, response);

        Assert.assertTrue(response.getContentAsString().contains("menuList"));
    }

    @Test //    /menu/updateMenu
    public void updateRedisMenu() throws UnsupportedEncodingException {
        String appId = "8";
        String appUserId = "3094776";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("appUserId", appUserId);
        request.addParameter("appId", appId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.updateRedisMenu(request, response);

        Assert.assertTrue(response.getContentAsString().contains("menuList"));
    }

    @Test //    /menu/deleteMenu
    public void delRedisMenu() throws UnsupportedEncodingException {
        String appId = "8";
        String appUserId = "3094776";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("appUserId", appUserId);
        request.addParameter("appId", appId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.delRedisMenu(request, response);
        Assert.assertEquals("Success", response.getContentAsString());
    }

    @Test //    /menu/existMenuKey
    public void existMenuKeyRedis() throws UnsupportedEncodingException {
        String appId = "8";
        String appUserId = "3094776";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("appUserId", appUserId);
        request.addParameter("appId", appId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.existMenuKeyRedis(request, response);

        Assert.assertEquals("TRUE", response.getContentAsString());
    }
}
