package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.*;
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


public class MenuResourceFunctionPrivilegeTest extends BaseTest {

    @Autowired
    private MenuAddPrivilegeController fixtureAdd;
    @Autowired
    private MenuModifyPrivilegeController fixtureModify;
    @Autowired
    private MenuGetPrivilegeController fixtureGet;
    @Autowired
    private MenuDelPrivilegeController fixtureDel;
    @Autowired
    private ResourceAddPrivilegeController fixtureResAdd;
    @Autowired
    private ResourceModifyPrivilegeController fixtureResModify;
    @Autowired
    private ResourceDelPrivilegeController fixtureResDel;
    @Autowired
    private ResourceGetPrivilegeController fixtureResGet;
    @Autowired
    private FunctionAddPrivilegeController fixtureFunAdd;
    @Autowired
    private FunctionModifyPrivilegeController fixtureFunModify;
    @Autowired
    private FunctionDelPrivilegeController fixtureFunDel;

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

        //资源单元测试
        resourceTest(appsecret, appId, appKey, menuId, random);

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

    private void resourceTest(String appsecret, String appId, String appKey, String menuId, Random random) throws UnsupportedEncodingException {
        String resourceLevel = "0";
        String resourceName = "单元测试资源";
        String baseUrl = "http://www.baidu.com";

        //增
        MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        addRequest.addParameter("resourceLevel", resourceLevel);
        addRequest.addParameter("resourceName", resourceName);
        addRequest.addParameter("baseUrl", baseUrl);
        addRequest.addParameter("menuId", menuId);

        MockHttpServletResponse addResponse = new MockHttpServletResponse();
        fixtureResAdd.addMenu(addRequest, addResponse);
        String json = addResponse.getContentAsString();
        JSONObject jsonObject = JSONObject.parseObject(json);
        String resourceId = jsonObject.getString("resourceId");
        Assert.notNull(resourceId);
        log.info("resourceId: " + resourceId);

        functionTest(appsecret, appId, appKey, random, resourceId);

        //改
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("resourceId", resourceId);
        modifyRequest.addParameter("resourceLevel", "1");
        modifyRequest.addParameter("resourceName", resourceName + random.nextInt(10));
        modifyRequest.addParameter("resourceRule", "rule");
        modifyRequest.addParameter("baseUrl", baseUrl + random.nextInt(10));
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        fixtureResModify.modifyResource(modifyRequest, modifyResponse);
        log.info(modifyResponse.getContentAsString());

        //查
        MockHttpServletRequest getRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        getRequest.addParameter("menuId", menuId);
        getRequest.addParameter("start", "0");
        getRequest.addParameter("Limit", "10");
        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        fixtureResGet.getResPrivilege(getRequest, getResponse);
        log.info(getResponse.getContentAsString());


        //删
        MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        delRequest.addParameter("resourceId", resourceId);
        MockHttpServletResponse delResponse = new MockHttpServletResponse();
        fixtureResDel.delResource(delRequest, delResponse);
        log.info(delResponse.getContentAsString());
    }

    private void functionTest(String appsecret, String appId, String appKey, Random random, String resourceId) throws UnsupportedEncodingException {

        String operationId = "10000";
        String optUrl = "http://www/google.com";
        //增
        MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        addRequest.addParameter("resourceId", resourceId);
        addRequest.addParameter("operationId", operationId);
        addRequest.addParameter("optUrl", optUrl);
        MockHttpServletResponse addResponse = new MockHttpServletResponse();
        fixtureFunAdd.addFunction(addRequest, addResponse);
        String json = addResponse.getContentAsString();
        JSONObject jsonObject = JSONObject.parseObject(json);
        String functionId = jsonObject.getString("functionId");
        Assert.notNull(functionId);
        log.info("functionId: " + functionId);

        //改
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("functionId", functionId);
        modifyRequest.addParameter("operationId", operationId);
        modifyRequest.addParameter("optUrl", optUrl + random.nextInt(10));
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        fixtureFunModify.modifyFunction(modifyRequest, modifyResponse);
        log.info(modifyResponse.getContentAsString());

        //删
        MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        delRequest.addParameter("functionId", functionId);
        MockHttpServletResponse delResponse = new MockHttpServletResponse();
        fixtureFunDel.delFunction(delRequest, delResponse);
        log.info(delResponse.getContentAsString());



    }

}