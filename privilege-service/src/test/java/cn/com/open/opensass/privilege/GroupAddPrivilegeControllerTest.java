package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.GroupAddPrivilegeController;
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

/**
 * 项目名称 : 组织机构权限初始创建接口单元测试.
 * 创建日期 : 2017/11/29 17:19.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/29][17:19]创建文件 by JACKIE.
 */
public class GroupAddPrivilegeControllerTest extends BaseTest {
    @Autowired
    GroupAddPrivilegeController groupAddPrivilegeController;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    public static final String SIGN = RedisConstant.SIGN;

    /**
     * 参数为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("groupName","");
        request.addParameter("createUser","");
        request.addParameter("appId","");
        request.addParameter("groupPrivilege","");
        request.addParameter("createUserid","");
        request.addParameter("status","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupAddPrivilegeController.addPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("addPrivilegeParamNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * 用户验证失败.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void userRoleModifyUserValidFailed() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("groupId","");
        request.addParameter("groupName","");
        request.addParameter("createUser","");
        request.addParameter("appId","");
        request.addParameter("groupPrivilege","");
        request.addParameter("createUserid","");
        request.addParameter("status","");
        request.removeParameter("appKey");
        request.addParameter("appKey","test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupAddPrivilegeController.addPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("userRoleModifyUserValidFailed:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空,数据已存在.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addPrivilegeAppNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId", CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","50C935EB50F3532AE0533312640A8C66");
        request.addParameter("groupName","广西玉林市硕联网络奥鹏培训中心111");
        request.addParameter("createUser","1657c18383a5a7500370c355128f469f");
        request.addParameter("groupPrivilege","2226,");
        request.addParameter("createUserid","1657c18383a5a7500370c355128f469f");
        request.addParameter("status","");
        redisClientTemplate.del(RedisConstant.APP_INFO+CommonEnum.APP_ID.getDisplay());
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupAddPrivilegeController.addPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("addPrivilegeAppNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
    /**
     * app为空,数据已存在.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addPrivilegeAppNullNotExistData() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId", CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","50C935EB50F3532AE0533312640A8C65");
        request.addParameter("groupName","广西玉林市硕联网络奥鹏培训中心111");
        request.addParameter("createUser","1657c18383a5a7500370c355128f469f");
        request.addParameter("groupPrivilege","2226,");
        request.addParameter("createUserid","1657c18383a5a7500370c355128f469f");
        request.addParameter("status","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupAddPrivilegeController.addPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("addPrivilegeAppNullNotExistData:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }

    /**
     * app为空,groupPrivilege为空.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void addPrivilegeGroupPrivilegeNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        request.addParameter("appId", CommonEnum.APP_ID.getDisplay());
        request.addParameter("groupId","50C935EB50F3532AE0533312640A8C65");
        request.addParameter("groupName","广西玉林市硕联网络奥鹏培训中心111");
        request.addParameter("createUser","1657c18383a5a7500370c355128f469f");
        request.addParameter("groupPrivilege","");
        request.addParameter("createUserid","1657c18383a5a7500370c355128f469f");
        request.addParameter("status","");
        MockHttpServletResponse response = new MockHttpServletResponse();
        groupAddPrivilegeController.addPrivilege(request, response);
        log.info(response.getContentAsString());
        System.out.println("addPrivilegeGroupPrivilegeNull:"+response.getContentAsString());
        Assert.assertTrue(net.sf.json.JSONObject.fromObject(response.getContentAsString()).get("status").equals("1"));
    }
}
