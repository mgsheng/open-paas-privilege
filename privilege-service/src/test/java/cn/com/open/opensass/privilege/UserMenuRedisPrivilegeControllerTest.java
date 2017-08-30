package cn.com.open.opensass.privilege;

import cn.com.open.opensass.privilege.api.UserMenuRedisPrivilegeController;
import cn.com.open.opensass.privilege.base.BaseTest;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class UserMenuRedisPrivilegeControllerTest extends BaseTest {

	@Autowired
	private UserMenuRedisPrivilegeController fixture;

	@Test //	/UserMenu/getUserMenuPrivilege
	public void MenuRedisPrivilege(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		fixture.MenuRedisPrivilege(request, response, new PrivilegeUserVo());
	}

}
