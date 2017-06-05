package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称 : 根据资源id获取功能权限functionids.
 * 创建日期 : 2017/6/1.
 * 创建时间 : 14:52.
 */
@Controller
@RequestMapping("/function/")
public class FunctionGetPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory
            .getLogger(FunctionGetPrivilegeController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private PrivilegeRoleResourceService privilegeRoleResourceService;

    /**
     * 根据resourceId和appUserId获取functionIds列表.
     *
     * @param request  获取请求.
     * @param response 输出数据.
     */
    @RequestMapping("getFunctionIdList")
    public void modifyFunction(HttpServletRequest request,
                               HttpServletResponse response) {

        String appId = request.getParameter("appId");
        String resourceId = request.getParameter("resourceId");
        String appUserId = request.getParameter("appUserId");
        Map<String, Object> map = new HashMap<String, Object>();
        log.info("====================getFunctionIdList start======================");
        if (!paraMandatoryCheck(Arrays.asList(resourceId, appId, appUserId))) {
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
        /**
         * 1. 获取用户表数据.
         * 2. 根据用户表中的privilege_roleid查询privilege_role_resource表中的functionIds.
         * 3. 拼接functionIds.
         */
        String functionIdsData = null;
        StringBuilder stringBuilderFunctionIds = new StringBuilder(50);
        //获取角色资源表中的functionIds数据
        String[] resourceIds = resourceId.split(",");
        List<PrivilegeRoleResource> privilegeRoleResourceList = privilegeRoleResourceService.findFunctionIds(appId,resourceIds, appUserId);
        if (privilegeRoleResourceList != null) {
            stringBuilderFunctionIds.append(",");
            for (PrivilegeRoleResource privilegeRoleResource : privilegeRoleResourceList) {
                stringBuilderFunctionIds.append(privilegeRoleResource.getPrivilegeFunId()).append(",");
            }
        }
        if (stringBuilderFunctionIds != null && stringBuilderFunctionIds.length() > 0) {
            functionIdsData = stringBuilderFunctionIds.substring(0, stringBuilderFunctionIds.length() - 1);
        }
        if (functionIdsData != null && functionIdsData.length()>0) {
            map.put("status", "1");
            map.put("functionIds", functionIdsData);
        } else {
            map.put("status", "0");
            map.put("error_code", "10001");
            map.put("message", "暂无数据");
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }
}
