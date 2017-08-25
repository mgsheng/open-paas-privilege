package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.RoleGetUserListController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.tools.DateUtils;
import cn.com.open.opensass.privilege.tools.HMacSha1;
import cn.com.open.opensass.privilege.tools.StringTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/*.xml","file:src/main/webapp/WEB-INF/soc-servlet.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class RoleGetUserListControllerTest extends BaseTest {

    @Autowired
    private RoleGetUserListController fixture;

    @Test
    public void getUserList() throws Exception {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String timestamp = DateUtils.toDateText(new Date(), "YYYY-MM-dd'T'hh:mm:ss'Z'");
        String signatureNonce= StringTool.getRandomString(18);

        String encryptText = appId + "&" + appKey + "&" + timestamp + "&" + signatureNonce;
        String signature= HMacSha1.HmacSHA1Encrypt(encryptText, appsecret);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("createUserid", "AdminT1373101");
        request.addParameter("appId", appId);
        request.addParameter("appKey", appKey);
        request.addParameter("privilegeRoleId", "6dec579e03c6f00f0bb849e001590463");
        request.addParameter("signature", signature);
        request.addParameter("timestamp", timestamp);
        request.addParameter("signatureNonce", signatureNonce);
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.getUserlist(request, response);
        log.info(response.getContentAsString());
    }

}
