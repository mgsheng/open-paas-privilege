package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称 : 删除用户相关缓存.
 * 创建日期 : 2017/11/20 9:59.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/11/20][9:59]创建文件 by LILI.
 */
@Controller
@RequestMapping("/user/")
public class PrivilegeUserCacheDelController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(PrivilegeUserCacheDelController.class);
    public static final String SIGN = RedisConstant.SIGN;
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserRedisService privilegeUserRedisService;

    /**
     * 删除用户相关缓存.
     *
     * @param request
     * @param response
     * @param privilegeUserVo
     */
    @RequestMapping(value = "delUserCache", method = RequestMethod.POST)
    public void delUserRedisCache(HttpServletRequest request, HttpServletResponse response, PrivilegeUserVo privilegeUserVo) {
        //应用Id
        String appId = privilegeUserVo.getAppId().trim();
        //用户Id
        String appUserId = privilegeUserVo.getAppUserId().trim();
        //参数校验是否存在空值
        if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        //App数据获取
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + appId);
        if (app == null) {
            app = appService.findById(Integer.parseInt(appId));
            redisClient.setObject(RedisConstant.APP_INFO + appId, app);
        }
        //签名验证
        Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
        if (!f) {
            paraMandaChkAndReturn(10006, response, "认证失败");
            return;
        }
        //删除缓存
        Boolean delSuccess = privilegeUserRedisService.delUserCaches(appId, appUserId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (delSuccess) {
            map.put("status", "1"); //成功返回1
        } else {
            map.put("status", "0");//失败返回0
        }
        writeSuccessJson(response, map);
        return;
    }
}
