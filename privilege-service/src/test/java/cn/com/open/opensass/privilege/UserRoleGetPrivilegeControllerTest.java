package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserRoleGetPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.CommonEnum;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * 项目名称 : 用户角色权限获取单元测试.
 * 创建日期 : 2017/11/15 17:14.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/15][17:14]创建文件 by JACKIE.
 */
public class UserRoleGetPrivilegeControllerTest  extends BaseTest {
    @Autowired
    UserRoleGetPrivilegeController userRoleGetPrivilegeController;

    @Test
    public void getPrivilegeParamNull() throws UnsupportedEncodingException {
        MockHttpServletRequest request = Signature.getSignatureRequest(CommonEnum.APP_SECRET.getDisplay(), CommonEnum.APP_ID.getDisplay(), CommonEnum.APP_KEY.getDisplay());
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(CommonEnum.APP_ID.getDisplay());
        privilegeUserVo.setAppUserId("");
        privilegeUserVo.setMenuCode("");
        MockHttpServletResponse response = new MockHttpServletResponse();
        userRoleGetPrivilegeController.getPrivilege(request, response,privilegeUserVo);
        log.info(response.getContentAsString());
        System.out.println("param-null："+response.getContentAsString());
        Assert.assertTrue(JSONObject.fromObject(response.getContentAsString()).get("status").equals("0"));
    }
}
