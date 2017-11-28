package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/UserMenu/")
public class UserMenuRedisPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(UserMenuRedisPrivilegeController.class);


    @RequestMapping("getUserMenuPrivilege")
    public void MenuRedisPrivilege(HttpServletRequest request, HttpServletResponse response, PrivilegeUserVo privilegeUserVo) {
        Set<String> menuIdSet = new HashSet<String>();
        log.info("====================redis user menu start======================");
        Map<String, List<Map<String, Object>>> menuMap = new HashMap<String, List<Map<String, Object>>>();
        if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }


    }
}
