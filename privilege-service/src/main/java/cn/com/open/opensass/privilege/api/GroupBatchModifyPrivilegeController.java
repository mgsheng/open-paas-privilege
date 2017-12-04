package cn.com.open.opensass.privilege.api;


import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.GroupBatchModifyPrivilegeService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 权限资源修改接口
 */
@Controller
@RequestMapping("/group/")
public class GroupBatchModifyPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(GroupBatchModifyPrivilegeController.class);
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private GroupBatchModifyPrivilegeService groupBatchModifyPrivilegeService;

    /**
     * 权限资源修改接口
     *
     * @return Json
     */
    @RequestMapping("batchModifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        String appId = request.getParameter("appId");
        String resourceId = request.getParameter("resourceId");
        String createUser = request.getParameter("createUser");
        String createUserid = request.getParameter("createUserid");
        String status = request.getParameter("status");
        log.info("====================batch modify user group resource start======================");
        if (!paraMandatoryCheck(Arrays.asList(groupId, resourceId, appId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + appId);
        if (app == null) {
            app = appService.findById(Integer.parseInt(appId));
            redisClient.setObject(RedisConstant.APP_INFO + appId, app);
        }
        //认证
        Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
        if (!f) {
            WebUtils.paraMandaChkAndReturn(10002, response, "认证失败");
            return;
        }
        Map<String, Object> map = groupBatchModifyPrivilegeService.batchUpdateUserGroupResource(appId, groupId, resourceId, createUser, createUserid, status);
        if (map.get("status") == "0") {
            writeSuccessJson(response, map);
        } else {
            writeErrorJson(response, map);
        }
        return;

    }
}
