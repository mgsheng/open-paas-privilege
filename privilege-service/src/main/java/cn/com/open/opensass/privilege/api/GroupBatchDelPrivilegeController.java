package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO .
 * 项目名称 : ${CLASS_NAME}.
 * 创建日期 : 2017/6/7.
 * 创建时间 : 17:43.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/6/7][17:43]创建文件 by JACKIE.
 */
@Controller
@RequestMapping("/group/")
public class GroupBatchDelPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(GroupBatchDelPrivilegeController.class);
    public static final String SIGN = RedisConstant.SIGN;
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    @Autowired
    private PrivilegeUrlService privilegeUrlService;
    @Autowired
    private PrivilegeResourceService privilegeResourceService;
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private PrivilegeUserRedisService privilegeUserRedisService;

    /**
     * 批量删除groupid.
     * @param request
     * @param response
     */
    @RequestMapping(value = "batchDelModifyPrivilege",method = RequestMethod.POST)
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        String appId = request.getParameter("appId");
        String resourceId = request.getParameter("resourceId");
        String functionId = request.getParameter("functionId");
        log.info("====================batch delete user  resource  functionIds  by groupids start======================");
        if (!paraMandatoryCheck(Arrays.asList(groupId, resourceId, appId,functionId))) {
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
        //批量更新语句
        Map<String,Object> map = privilegeUserService.batchUpdateGroupResourceFunction(appId,groupId,resourceId,functionId);

        if (map.get("status") == "1") {
            if(map.get("userIdList") != null){
                String userId = map.get("userIdList").toString();
                String[] userIds = userId.split(",");
                //批量更新缓存
                updateRedisCache(appId,userIds);
            }
            map.clear();
            map.put("status","1");
            map.put("message", "批量删除成功!");
            writeSuccessJson(response, map);
        } else {
            writeErrorJson(response, map);
        }

    }


    /*更新redis缓存*/
    PrivilegeAjaxMessage updateRedisCache(final String appId, final String[] users) {
        log.info("====================batch modify functionIds start======================");
        privilegeResourceService.updateAppResRedis(appId);
        log.info("====================batch modify resourceIds start======================");
        final PrivilegeAjaxMessage[] message = {null};

        try {
            int count = users.length/4;
            if(count == 0){
                count = Runtime.getRuntime().availableProcessors();
            }
            final ExecutorService threadPool = Executors.newFixedThreadPool(count);//线程池里面有10个线程
            for (final String userId : users) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                            if (userId != null && userId != "") {
                                log.debug("Thread Delete Name is" + Thread.currentThread().getName() + ",userId:" + userId);
                                log.info("====================batch modify big cache start======================");
                                StringBuilder redisUserPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
                                redisUserPrivilegeKey.append(RedisConstant.USER_CACHE_INFO);
                                redisUserPrivilegeKey.append(appId);
                                redisUserPrivilegeKey.append(SIGN);
                                redisUserPrivilegeKey.append(userId);
                                if(redisClient.existKey(redisUserPrivilegeKey.toString()))
                                {
                                    redisClient.del(redisUserPrivilegeKey.toString());
                                }
                                StringBuilder redisUserAllPrivilegeKey=new StringBuilder(RedisConstant.PUBLICSERVICE_CACHE);
                    			redisUserAllPrivilegeKey.append(RedisConstant.USER_ALL_CACHE_INFO);
                    			redisUserAllPrivilegeKey.append(appId);
                    			redisUserAllPrivilegeKey.append(SIGN);
                    			redisUserAllPrivilegeKey.append(userId);
                    			if(redisClient.existKey(redisUserAllPrivilegeKey.toString()))
                    			{
                    				redisClient.del(redisUserAllPrivilegeKey.toString());
                    			}
                                message[0] = privilegeUserRedisService.updateUserRoleRedis(appId, userId);
                                privilegeMenuService.updateMenuRedis(appId, userId);
                                privilegeUrlService.updateRedisUrl(appId, userId);
                        }
                    }
                });
            }
            threadPool.shutdown();
            while (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                //等待线程池执行完毕
            }
        } catch (Exception e) {
            message[0] = null;
            e.printStackTrace();
        }
        return message[0];
    }

}
