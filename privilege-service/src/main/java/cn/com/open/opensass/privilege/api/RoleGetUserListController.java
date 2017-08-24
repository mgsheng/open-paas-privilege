package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/role/")
public class RoleGetUserListController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(RoleGetUserListController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private PrivilegeRoleService privilegeRoleService;

    /**
     * 根据角色id查询用户列表接口
     */
    @RequestMapping(value = "getUserlist")
    public void getUserlist(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map=new HashMap<>();
        log.info("===================getUserlist start======================");

        String appId = request.getParameter("appId");
        String privilegeRoleId = request.getParameter("privilegeRoleId");
        String createUserid = request.getParameter("createUserid");

        if(!paraMandatoryCheck(Arrays.asList(appId, privilegeRoleId, createUserid))){
            paraMandaChkAndReturn(10000, response,"必传参数中有空值");
            return;
        }
        App app = (App)redisClient.getObject(RedisConstant.APP_INFO + appId);
        if(app==null)
        {
            app=appService.findById(Integer.parseInt(appId));
            redisClient.setObject(RedisConstant.APP_INFO+appId, app);
        }
        Boolean f= OauthSignatureValidateHandler.validateSignature(request,app);
        if(!f) {
            paraMandaChkAndReturn(10001, response, "认证失败");
            return;
        }

        if (privilegeRoleService.findRoleById(privilegeRoleId) == null) {
            map.put("status", "0");
            map.put("error_code", "10002");
            map.put("errMsg", "角色不存在");
            writeErrorJson(response, map);
            return;
        }

        List<String> appUserIdList = privilegeUserService.findUserIdByPrivilegeRoleId(privilegeRoleId, appId);
        List<Map<String,String>> userList = new ArrayList<>();
        for (String appUserId : appUserIdList) {
            Map<String, String> userMap = new HashMap<>();
            userMap.put("appUserId", appUserId);
            userList.add(userMap);
        }
        map.put("status", "1");
        map.put("userList", userList);
        writeSuccessJson(response,map);
    }

}
