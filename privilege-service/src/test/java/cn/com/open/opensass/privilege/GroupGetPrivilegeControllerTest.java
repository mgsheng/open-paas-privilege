package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupGetPrivilegeController;
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
 * 项目名称 : 组织机构权限查询接口单元测试.
 * 创建日期 : 2017/11/28 15:57.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/28][15:57]创建文件 by JACKIE.
 */
public class GroupGetPrivilegeControllerTest extends BaseTest {
    @Autowired
    private GroupGetPrivilegeController groupGetPrivilegeController;
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getGroupPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("start","");
        request.addParameter("limit","10");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupGetPrivilegeController.getGroupPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("getGroupPrivilegeParamNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getGroupPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","0467f043c4a1e97bc631fed6e3acf663");
        request.addParameter("start","0");
        request.addParameter("limit","10");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupGetPrivilegeController.getGroupPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("getGroupPrivilegeAppNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getGroupPrivilegeValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","0467f043c4a1e97bc631fed6e3acf663");
        request.addParameter("start","0");
        request.addParameter("limit","10");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupGetPrivilegeController.getGroupPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("getGroupPrivilegeValidFailed:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 获取数据为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getGroupPrivilegeDataNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId",CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","1234444");
        request.addParameter("start","0");
        request.addParameter("limit","10");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupGetPrivilegeController.getGroupPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("getGroupPrivilegeDataNull:"+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
