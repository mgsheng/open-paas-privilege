package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.PrivilegeOperationController;
import cn.com.open.opensass.privilege.base.BaseTest;
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
		log.info(response.getContentAsString());
	}
	@Test //	/operation/getAllOperation
	public void getAllOperation() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.getAllOperation(request, response);
		log.info(response.getContentAsString());
	}
}
