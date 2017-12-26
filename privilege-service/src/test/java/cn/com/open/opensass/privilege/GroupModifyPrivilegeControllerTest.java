package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupModifyPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
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

/*

 * 文件名：cn.com.open.opensass.privilege

 * 版权： 权限服务

 * 描述： 权限服务接口，管理用户权限

 * 修改人： LILI

 * 修改时间：2017/12/26 15:39

 * 修改内容：新增

 */
public class GroupModifyPrivilegeControllerTest extends BaseTest {
    @Autowired
    GroupModifyPrivilegeController groupModifyPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 正常运行.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilege() throws UnsupportedEncodingException {
        String groupId = "488A74408834169DE053BD86640A49F5";
        String groupName = "单元测试group";
        String groupPrivilege = "11,12";
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", groupName + random.nextInt(10));
        modifyRequest.addParameter("status", "0");
        modifyRequest.addParameter("method", "0");
        modifyRequest.addParameter("groupPrivilege", "13,14");
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        groupModifyPrivilegeController.modifyPrivilege(modifyRequest, modifyResponse);
        Assert.assertEquals("1", JSON.parseObject(modifyResponse.getContentAsString()).getString("status"));
    }

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeParamNull() throws UnsupportedEncodingException {
        String groupId = "";//"488A74408834169DE053BD86640A49F5";
        String groupName = "单元测试group";
        String groupPrivilege = "11,12";
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", groupName + random.nextInt(10));
        modifyRequest.addParameter("status", "0");
        modifyRequest.addParameter("method", "");
        modifyRequest.addParameter("groupPrivilege", "13,14");
        modifyRequest.addParameter("createUser", "test");
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        groupModifyPrivilegeController.modifyPrivilege(modifyRequest, modifyResponse);
        Assert.assertEquals("0", JSON.parseObject(modifyResponse.getContentAsString()).getString("status"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeValidFailed() throws UnsupportedEncodingException {
        String groupId = "488A74408834169DE053BD86640A49F5";
        String groupName = "单元测试group";
        String groupPrivilege = "11,12";
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", "");
        modifyRequest.addParameter("status", "0");
        modifyRequest.addParameter("method", "");
        modifyRequest.addParameter("groupPrivilege", "13,14");
        modifyRequest.addParameter("createUser", "");
        redisClientTemplate.del(RedisConstant.APP_INFO + appId);
        modifyRequest.removeParameter("appKey");
        modifyRequest.addParameter("appKey", "test");//应用id（必传）
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        groupModifyPrivilegeController.modifyPrivilege(modifyRequest, modifyResponse);
        Assert.assertEquals("0", JSON.parseObject(modifyResponse.getContentAsString()).getString("status"));
    }

    /**
     * 非法操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeErrMethod() throws UnsupportedEncodingException {
        String groupId = "488A74408834169DE053BD86640A49F5";
        String groupName = "单元测试group";
        String groupPrivilege = "11,12";
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        Random random = new Random();
        MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
        modifyRequest.addParameter("groupId", groupId);
        modifyRequest.addParameter("groupName", groupName + random.nextInt(10));
        modifyRequest.addParameter("status", "0");
        modifyRequest.addParameter("method", "2");
        modifyRequest.addParameter("groupPrivilege", "13,14");
        MockHttpServletResponse modifyResponse = new MockHttpServletResponse();
        groupModifyPrivilegeController.modifyPrivilege(modifyRequest, modifyResponse);
        Assert.assertEquals("0", JSON.parseObject(modifyResponse.getContentAsString()).getString("status"));
    }
}
