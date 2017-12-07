package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeOperationController;
import cn.com.open.opensass.privilege.base.BaseTest;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;


public class PrivilegeOperationControllerTest extends BaseTest {

	@Autowired
	private PrivilegeOperationController fixture;

	@Test //	/operation/getOperation
	public void getOperationName() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("optId", "10000");
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getOperationName(request, response);

		Assert.assertEquals("修改", JSON.parseObject(response.getContentAsString()).getString("optName"));
	}
	@Test //	/operation/getAllOperation
	public void getAllOperation() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAllOperation(request, response);

		Assert.assertTrue(response.getContentAsString().contains("operationList"));
	}
}
