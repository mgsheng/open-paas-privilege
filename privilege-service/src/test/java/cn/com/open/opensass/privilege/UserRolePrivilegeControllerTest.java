package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.*;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.signature.Signature;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class UserRolePrivilegeControllerTest extends BaseTest {

	@Autowired
	private UserRoleAddPrivilegeController fixtureAdd;
	@Autowired
	private UserRoleModifyPrivilegeController fixtureModify;
	@Autowired
	private UserRoleGetPrivilegeController fixtureGet;
	@Autowired
	private UserRoleDelPrivilegeController fixtureDel;
	@Autowired
	private UserRoleBatchModifyPrivilegeController fixtureBatchModify;

	@Test
	public void addRole() throws UnsupportedEncodingException {
		String privilegeRoleId = "c94b35130ac54cac891ef9cb4e4542da,99f6c14eb08f483aa78facc49f700eed"; //班主任 校长
		String appUserId = "junitTestUserId";
		String appUserName = "单元测试用户名";
		String groupId = "50C935EB5239532AE0533312640A8C66";
		String privilegeFunId = "3e016029eeb9a92852a656f33fbf1dc6,b0bf8b3daf61246d13276dc8dcdfb87d";
		String resourceId = "11,12";

		String appsecret = "67d73cec5d6b4c8a8a9883748f4066fe";
		String appId = "8";
		String appKey = "df6bda1a157d49cea82447c3e925dd6d";

		//增
		MockHttpServletRequest addRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse addResponse = new MockHttpServletResponse();

		PrivilegeUserVo addPrivilegeUserVo = new PrivilegeUserVo();
		addPrivilegeUserVo.setAppId(appId);
		addPrivilegeUserVo.setAppUserId(appUserId);
		addPrivilegeUserVo.setAppUserName(appUserName);
		addPrivilegeUserVo.setPrivilegeFunId(privilegeFunId);
		addPrivilegeUserVo.setResourceId(resourceId);
		addPrivilegeUserVo.setGroupId(groupId);
		addPrivilegeUserVo.setPrivilegeRoleId(privilegeRoleId);

		fixtureAdd.addRole(addRequest, addResponse, addPrivilegeUserVo);
		log.info(addResponse.getContentAsString());

		//改
		MockHttpServletRequest modifyRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse modifyResponse = new MockHttpServletResponse();

		PrivilegeUserVo modifyPrivilegeUserVo = new PrivilegeUserVo();
		modifyPrivilegeUserVo.setAppId(appId);
		modifyPrivilegeUserVo.setAppUserId(appUserId);
		modifyPrivilegeUserVo.setAppUserName(appUserName);
		modifyPrivilegeUserVo.setPrivilegeFunId(privilegeFunId);
		modifyPrivilegeUserVo.setResourceId(resourceId);
		modifyPrivilegeUserVo.setGroupId(groupId);
		modifyPrivilegeUserVo.setMethod("0");
		modifyPrivilegeUserVo.setPrivilegeRoleId("37cab4fe42364bc7affbec5f6358d01e"); //教材管理员

		fixtureModify.modifyPrivilege(modifyRequest, modifyResponse, modifyPrivilegeUserVo);
		log.info(modifyResponse.getContentAsString());

		//批量增
		MockHttpServletRequest batchModifyAddRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse batchModifyAddResponse = new MockHttpServletResponse();

		PrivilegeUserVo batchModifyAddPrivilegeUserVo = new PrivilegeUserVo();
		batchModifyAddPrivilegeUserVo.setAppId(appId);
		batchModifyAddPrivilegeUserVo.setAppUserId(appUserId);
		batchModifyAddPrivilegeUserVo.setFunctionId("1b5b0657f72ae81541808a70d9af3663");
		batchModifyAddPrivilegeUserVo.setResourceId("13,14");
		batchModifyAddPrivilegeUserVo.setOperationType("0");

		fixtureBatchModify.modifyPrivilege(batchModifyAddRequest, batchModifyAddResponse, batchModifyAddPrivilegeUserVo);
		log.info(batchModifyAddResponse.getContentAsString());

		//批量删
		MockHttpServletRequest batchModifyDelRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse batchModifyDelResponse = new MockHttpServletResponse();

		PrivilegeUserVo batchModifyDelPrivilegeUserVo = new PrivilegeUserVo();
		batchModifyDelPrivilegeUserVo.setAppId(appId);
		batchModifyDelPrivilegeUserVo.setAppUserId(appUserId);
		batchModifyDelPrivilegeUserVo.setAppUserName(appUserName);
		batchModifyDelPrivilegeUserVo.setFunctionId("1b5b0657f72ae81541808a70d9af3663");
		batchModifyDelPrivilegeUserVo.setResourceId("14");
		batchModifyDelPrivilegeUserVo.setOperationType("1");

		fixtureBatchModify.modifyPrivilege(batchModifyDelRequest, batchModifyDelResponse, batchModifyDelPrivilegeUserVo);
		log.info(batchModifyDelResponse.getContentAsString());

		//查
		MockHttpServletRequest getRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse getResponse = new MockHttpServletResponse();
		PrivilegeUserVo getPrivilegeUserVo = new PrivilegeUserVo();
		getPrivilegeUserVo.setAppId(appId);
		getPrivilegeUserVo.setAppUserId(appUserId);
		fixtureGet.getPrivilege(getRequest, getResponse, getPrivilegeUserVo);
		log.info(getResponse.getContentAsString());

		//删
		MockHttpServletRequest delRequest = Signature.getSignatureRequest(appsecret, appId, appKey);
		MockHttpServletResponse delResponse = new MockHttpServletResponse();

		PrivilegeUserVo delPrivilegeUserVo = new PrivilegeUserVo();
		delPrivilegeUserVo.setAppId(appId);
		delPrivilegeUserVo.setAppUserId(appUserId);

		fixtureDel.delRole(delRequest, delResponse, delPrivilegeUserVo);
		log.info(delResponse.getContentAsString());
	}

}
