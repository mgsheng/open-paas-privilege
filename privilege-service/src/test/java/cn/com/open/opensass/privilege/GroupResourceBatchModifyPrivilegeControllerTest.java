package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupResourceBatchModifyPrivilegeController;
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
 * 项目名称 : 组织机构权限批量更新单元测试.
 * 创建日期 : 2017/11/16 10:55.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/16][10:55]创建文件 by JACKIE.
 */
public class GroupResourceBatchModifyPrivilegeControllerTest extends BaseTest {
    @Autowired
    GroupResourceBatchModifyPrivilegeController groupResourceBatchModifyPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClient;
    String groupId = "488A74408834169DE053BD86640A49F5";
    String resourceId = "11,12,13";
    String operationType = "1";

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeNomarl() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType",operationType);
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("nomarl:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 正常操作删除.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeNomarlDel() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType","0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("nomarl:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
    /**
     * 操作数据失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeOprationFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType","3");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("OprationFailed:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType",operationType);
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("ParamNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * App为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType",operationType);
        redisClient.del(RedisConstant.APP_INFO + CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("AppNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void modifyPrivilegeValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId",groupId);
        request.addParameter("resourceId",resourceId);
        request.addParameter("operationType",operationType);
        request.removeParameter("appKey");
        request.addParameter("appKey", "test");//应用id（必传）
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupResourceBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("ValidFailed:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
