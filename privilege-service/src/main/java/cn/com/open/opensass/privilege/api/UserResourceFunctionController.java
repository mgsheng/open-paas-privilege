package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * 文件名：清空用户权限资源Id和functionId
 * 版权： 奥鹏教育
 * 描述： 教育平台
 */
@Controller
@RequestMapping("/user/")
public class UserResourceFunctionController extends BaseControllerUtil {
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserService privilegeUserService;

    /**
     * 根据resourceId和appUserId获取functionIds列表.
     *
     * @param request  获取请求.
     * @param response 输出数据.
     */
    @RequestMapping("clearResourceFunction")
    public void modifyFunction(HttpServletRequest request,
                               HttpServletResponse response) {
        String appId = request.getParameter("appId");
        String appUserId = request.getParameter("appUserId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + appId);
        if (app == null) {
            app = appService.findById(Integer.parseInt(appId));
            redisClient.setObject(RedisConstant.APP_INFO + appId, app);
        }
        Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
        if (!f) {
            WebUtils.paraMandaChkAndReturn(10002, response, "认证失败");
            return;
        }

        Boolean result = privilegeUserService.updatePrivilegeUserResourceId(appUserId); //清空用户本身resourceId
        if (result) {
            map.put("status", "1");
        } else {
            map.put("status", "0");
            map.put("message", "清空失败");
        }
        writeSuccessJson(response, map);
    }
}
