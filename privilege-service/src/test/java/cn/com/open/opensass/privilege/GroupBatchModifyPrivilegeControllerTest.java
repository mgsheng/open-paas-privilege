package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupBatchModifyPrivilegeController;
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
 * 权限资源修改接口单元测试.
 */
public class GroupBatchModifyPrivilegeControllerTest extends BaseTest{
    @Autowired
    GroupBatchModifyPrivilegeController groupBatchModifyPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;

    /**
     * 正常操作.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupBatchModifyNormal()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","50C935EB5A4F532AE0533312640A8C66");
        request.addParameter("resourceId","416");
        request.addParameter("createUser","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("status","1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * app为空,验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupBatchModifyAppNullValidFailed()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","50C935EB5A4F532AE0533312640A8C66");
        request.addParameter("resourceId","416");
        request.addParameter("createUser","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("status","1");
        redisClientTemplate.del(RedisConstant.APP_INFO + CommonEnum.APP_ID.getDisplay());
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupBatchModifyParamNull()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","50C935EB5A4F532AE0533312640A8C66");
        request.addParameter("resourceId","416");
        request.addParameter("createUser","TEST");
        request.addParameter("createUserid","TEST");
        request.addParameter("status","1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 无可更新数据.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void groupBatchModifyUpdateFailed()throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId",",");
        request.addParameter("resourceId",",");
        request.addParameter("createUser","1657c18383a5a7500370c355128f469f");
        request.addParameter("createUserid","");
        request.addParameter("status","0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupBatchModifyPrivilegeController.modifyPrivilege(request, response);
        log.info(response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
