package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.ApiCommonController;
import cn.com.open.opensass.privilege.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

public class ApiCommonControllerTest extends BaseTest {

    @Autowired
    private ApiCommonController fixture;

    // common/status
    @Test
    public void status() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        fixture.status(request, response);
        log.info(response.getContentAsString());
    }
}
