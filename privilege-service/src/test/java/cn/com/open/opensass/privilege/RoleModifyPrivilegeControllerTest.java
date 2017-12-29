package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.RoleModifyPrivilegeController;
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
import java.util.Random;

/**
 * 项目名称 : 角色修改单元测试.
 * 创建日期 : 2017/11/28 10:27.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/28][10:27]创建文件 by JACKIE.
 */
public class RoleModifyPrivilegeControllerTest extends BaseTest{
    @Autowired
    RoleModifyPrivilegeController roleModifyPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;

    /**
     * 角色更新，参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("privilegeRoleId","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 角色更新,app为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleAppNull() throws UnsupportedEncodingException {
        String roleName = "单元测试role";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","c55a18ec18534e1585846d0aebaf95ba");
        request.addParameter("method", "0");
        request.addParameter("rolePrivilege", "13,14");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        redisClient.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 角色更新,验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleValidFailed() throws UnsupportedEncodingException {
        String roleName = "单元测试role";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","c55a18ec18534e1585846d0aebaf95ba");
        request.addParameter("method", "0");
        request.addParameter("rolePrivilege", "13,14");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 角色更新,删除成功！.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleModifyDeleteErr() throws UnsupportedEncodingException {
        String roleName = "单元测试role";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","7a5568b9c98247f397810104e13c085a");
        request.addParameter("method", "1");
        request.addParameter("rolePrivilege", "8e26270b43ea45b89fd3d8937983d49e");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 角色更新,此权限不存在，请核实！.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleModifyDeleteOk() throws UnsupportedEncodingException {
        String roleName = "单元测试role";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","c55a18ec18534e1585846d0aebaf95ba");
        request.addParameter("method", "1");
        request.addParameter("rolePrivilege", "13,14");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);

        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }

    /**
     * 角色更新,此权限不存在，请核实！.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleModifyDeleteOkM() throws UnsupportedEncodingException {
        String roleName = "JIAOSEMINGCHENG";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","edb3171696068fa857d26f62cad58e58");
        request.addParameter("method", "1");
        request.addParameter("rolePrivilege", "11869,2947,2946");
        request.addParameter("privilegeFunId", "4a5682a19ce7b4b09858cf28448dd96c,17f5e6db87929fb55cebeb7fd58c1d41,10a409f7044f0c29cda1e2e2930fa6ef,709b6839d764651ee36ecdefdb0c486e,a17c66baf7ecfb46712b81a5d64dcf21,625a61cc569767faf06e83d43d9d5257,986934ffbe7c548efeaa7945a26b0393,fb8062d98de2413ae3bb5953582ba9b3,cd6b73b67c77edeaff94e24b961119dd");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        request.addParameter("deptId", "100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);
        System.out.println("Test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 角色更新,此权限不存在，请核实！.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void roleModifyDeleteOkM1() throws UnsupportedEncodingException {
        String roleName = "JIAOSEMINGCHENG";
        String groupId = "50C935EB5239532AE0533312640A8C66";
        String groupName = "四川遂宁应用技术职业学校奥鹏学习中心[17]VIP";
        Random random = new Random();
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("privilegeRoleId","edb3171696068fa857d26f62cad58e58");
        request.addParameter("method", "0");
        request.addParameter("rolePrivilege", "11869,2947,2946");
        request.addParameter("privilegeFunId", "4a5682a19ce7b4b09858cf28448dd96c,17f5e6db87929fb55cebeb7fd58c1d41,10a409f7044f0c29cda1e2e2930fa6ef,709b6839d764651ee36ecdefdb0c486e,a17c66baf7ecfb46712b81a5d64dcf21,625a61cc569767faf06e83d43d9d5257,986934ffbe7c548efeaa7945a26b0393,fb8062d98de2413ae3bb5953582ba9b3,cd6b73b67c77edeaff94e24b961119dd");
        request.addParameter("roleName", roleName + random.nextInt(10));
        request.addParameter("groupId", groupId);
        request.addParameter("groupName", groupName + random.nextInt(10));
        request.addParameter("status", "0");
        request.addParameter("deptId", "100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        roleModifyPrivilegeController.modifyPrivilege(request, response);
        System.out.println("Test:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
