package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.RoleAddPrivilegeController;
import cn.com.open.opensass.privilege.api.RoleDelPrivilegeController;
import cn.com.open.opensass.privilege.api.RoleModifyPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Random;


public class RolePrivilegeControllerTest extends BaseTest {

    @Autowired
    private RoleAddPrivilegeController fixtureAdd;
    @Autowired
    private RoleModifyPrivilegeController fixtureModify;
    @Autowired
    private RoleDelPrivilegeController fixtureDel;

	@Test //	/role/addRole
    public void addRole() throws UnsupportedEncodingException {
        String roleName = "单元测试role";
        String rolePrivilege = "11,12";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";

        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();

        //增
        MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        addRequest.addParameter("roleName", roleName);
        addRequest.addParameter("rolePrivilege", rolePrivilege);
        addRequest.addParameter("roleLevel", "0");
        addRequest.addParameter("roleType", "2");
        addRequest.addParameter("parentRoleId", "0");
        addRequest.addParameter("groupId", groupId);
        addRequest.addParameter("groupName", groupName);
        addRequest.addParameter("status", "0");
        MockHttpServletResponse addResponse = new MockHttpServletResponse();
        fixtureAdd.addRole(addRequest, addResponse);

        String json = addResponse.getContentAsString();

        JSONObject jsonObject = JSONObject.parseObject(json);
        String privilegeRoleid = jsonObject.getString("privilegeRoleid");

        Assert.assertNotNull(privilegeRoleid);


        //改
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("privilegeRoleId", privilegeRoleid);
        modifyRequest.addParameter("method", "0");
        modifyRequest.addParameter("rolePrivilege", "13,14");
        modifyRequest.addParameter("roleName", roleName + random.nextInt(10));
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", groupName + random.nextInt(10));
        modifyRequest.addParameter("status", "0");

        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        fixtureModify.modifyPrivilege(modifyRequest, modifyResponse);
        Assert.assertEquals("1", JSONObject.parseObject(modifyResponse.getContentAsString()).getString("status"));

        //删
        MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        delRequest.addParameter("privilegeRoleId", privilegeRoleid);
        MockHttpServletResponse delResponse = new MockHttpServletResponse();
        fixtureDel.delRole(delRequest, delResponse);
        Assert.assertEquals("1", JSONObject.parseObject(delResponse.getContentAsString()).getString("status"));

    }

}
