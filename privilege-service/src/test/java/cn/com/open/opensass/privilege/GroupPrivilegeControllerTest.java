package cn.com.open.opensass.privilege;


import cn.com.open.opensass.privilege.api.*;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

public class GroupPrivilegeControllerTest extends BaseTest {

    @Autowired
    private GroupAddPrivilegeController fixtureAdd;
    @Autowired
    private GroupGetPrivilegeController fixtureGet;
    @Autowired
    private GroupBatchModifyPrivilegeController fixtureBatchModify;
    @Autowired
    private GroupModifyPrivilegeController fixtureModify;
    @Autowired
    private GroupDelPrivilegeController fixtureDel;

    @Test
    public void addPrivilege() throws UnsupportedEncodingException {
        String groupId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String groupName = "单元测试group";
        String groupPrivilege = "11,12";

        log.info("groupId: " + groupId);
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();

        //增
        MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        addRequest.addParameter("groupId", groupId);
        addRequest.addParameter("groupName", groupName);
        addRequest.addParameter("groupPrivilege", groupPrivilege);
        addRequest.addParameter("status", "0");
        MockHttpServletResponse addResponse = new MockHttpServletResponse();
        fixtureAdd.addPrivilege(addRequest, addResponse);
        log.info(addResponse.getContentAsString());
        Assert.assertEquals("1", JSON.parseObject(addResponse.getContentAsString()).getString("status"));


        //改
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", groupName + random.nextInt(10));
        modifyRequest.addParameter("status", "0");
        modifyRequest.addParameter("method", "0");
        modifyRequest.addParameter("groupPrivilege", "13,14");
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        fixtureModify.modifyPrivilege(modifyRequest, modifyResponse);
        log.info(modifyResponse.getContentAsString());
        Assert.assertEquals("1", JSON.parseObject(addResponse.getContentAsString()).getString("status"));

        //批量修改(批量添加)
        MockHttpServletRequest batchModifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        batchModifyRequest.addParameter("groupId", groupId);
        batchModifyRequest.addParameter("status", "0");
        batchModifyRequest.addParameter("resourceId", "15,14,16");
        MockHttpServletResponse batchModifyResponse = new MockHttpServletResponse();
        fixtureBatchModify.modifyPrivilege(batchModifyRequest, batchModifyResponse);
        log.info(batchModifyResponse.getContentAsString());
        Assert.assertEquals("1", JSON.parseObject(addResponse.getContentAsString()).getString("status"));

        //查
        MockHttpServletRequest getRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        getRequest.addParameter("groupId", groupId);
        getRequest.addParameter("start", "0");
        getRequest.addParameter("limit", "10");
        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        fixtureGet.getGroupPrivilege(getRequest, getResponse);
        log.info(getResponse.getContentAsString());
        Assert.assertTrue(getResponse.getContentAsString().contains("groupList"));
        //删
        MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        delRequest.addParameter("groupId", groupId);
        MockHttpServletResponse delResponse = new MockHttpServletResponse();
        fixtureDel.delPrivilege(delRequest, delResponse);
        log.info(delResponse.getContentAsString());
        Assert.assertEquals("1", JSON.parseObject(addResponse.getContentAsString()).getString("status"));
    }

}