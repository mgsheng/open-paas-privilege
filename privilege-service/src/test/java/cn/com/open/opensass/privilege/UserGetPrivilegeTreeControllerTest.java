package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserGetPrivilegeTreeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class UserGetPrivilegeTreeControllerTest extends BaseTest {

	@Autowired
	private UserGetPrivilegeTreeController fixtureTree;

	@Test
	public void getPrivilegeTree() throws UnsupportedEncodingException {
        String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
        String appId = "8";
        String appKey = "df6bda1a157d49cea82447c3e925dd6d";
        String appUserId = "3094776";
        MockHttpServletRequest request = Signature.getSignatureRequest(appsecret, appId, appKey);
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrivilegeUserVo privilegeUserVo = new PrivilegeUserVo();
        privilegeUserVo.setAppId(appId);
        privilegeUserVo.setAppUserId(appUserId);
        fixtureTree.getPrivilege(request, response, privilegeUserVo);
        log.info(response.getContentAsString());
	}

}
