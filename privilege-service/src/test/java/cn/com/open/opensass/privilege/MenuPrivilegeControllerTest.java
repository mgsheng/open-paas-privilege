package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.MenuAddPrivilegeController;
import cn.com.open.opensass.privilege.api.MenuDelPrivilegeController;
import cn.com.open.opensass.privilege.api.MenuGetPrivilegeController;
import cn.com.open.opensass.privilege.api.MenuModifyPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.Random;


public class MenuPrivilegeControllerTest extends BaseTest {

    @Autowired
    private MenuAddPrivilegeController fixtureAdd;
    @Autowired
    private MenuModifyPrivilegeController fixtureModify;
    @Autowired
    private MenuGetPrivilegeController fixtureGet;
    @Autowired
    private MenuDelPrivilegeController fixtureDel;

    @Test
    public void addMenu() throws UnsupportedEncodingException {
        String menuName = "单元测试菜单";
        String menuCode = "单元测试";
        String menuRule = "role";
        String menuLevel = "1";
        String parentId = "0";
        String dislayOrder = "0";
        String status = "0";

        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();

        //增
        MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        addRequest.addParameter("menuName", menuName);
        addRequest.addParameter("menuCode", menuCode);
        addRequest.addParameter("menuLevel", menuLevel);
        addRequest.addParameter("menuRule", menuRule);
        addRequest.addParameter("parentId", parentId);
        addRequest.addParameter("dislayOrder", dislayOrder);
        addRequest.addParameter("status", status);

        MockHttpServletResponse addResponse = new MockHttpServletResponse();
        fixtureAdd.addMenu(addRequest, addResponse);
        String json = addResponse.getContentAsString();
        JSONObject jsonObject = JSONObject.parseObject(json);
        String menuId = jsonObject.getString("menuId");
        Assert.notNull(menuId);
        log.info("menuId: " + menuId);

        //改
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("menuId", menuId);
        modifyRequest.addParameter("menuName", menuName + random.nextInt(10));
        modifyRequest.addParameter("menuCode", menuCode);
        modifyRequest.addParameter("menuLevel", menuLevel);
        modifyRequest.addParameter("menuRule", menuRule);
        modifyRequest.addParameter("parentId", parentId);
        modifyRequest.addParameter("dislayOrder", dislayOrder);
        modifyRequest.addParameter("status", status);
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        fixtureModify.modifyMenu(modifyRequest, modifyResponse);
        log.info(modifyResponse.getContentAsString());

        //查
        MockHttpServletRequest getRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        getRequest.addParameter("menuId", menuId);
        getRequest.addParameter("start", "0");
        getRequest.addParameter("Limit", "10");
        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        fixtureGet.getMenus(getRequest, getResponse);
        log.info(getResponse.getContentAsString());

        //删
        MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        delRequest.addParameter("menuId", menuId);
        MockHttpServletResponse delResponse = new MockHttpServletResponse();
        fixtureDel.delMenu(delRequest, delResponse);
        log.info(delResponse.getContentAsString());
    }




}