package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.dao.ResourceUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrlData;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PivilegeToken;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by jh on 2016/12/12.
 */
@Controller
@RequestMapping("/url/")
public class UrlRedisPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(UrlRedisPrivilegeController.class);
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private PrivilegeUserService privilegeUserService;

    @RequestMapping(value = "getDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void putdata(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {

        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<String, Object> map = new HashMap<String, Object>();
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appUserId, pivilegeToken.getAppId());
        //privilegeUser.setuId("cbfb25e6c0d611e6a6df0050568c069a");
        if (null == privilegeUser) {

            map.put("status", 0);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        String key = RedisConstant.USERPRIVILEGES_CACHE + pivilegeToken.getAppId() + RedisConstant.SIGN + privilegeUser.getuId();

        /*缓存中是否存在*/
        ResourceUrlData urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());

        if (null == urlJedis) {
             /*封装list类*/
            ResourceUrlData resourceUrlData = new ResourceUrlData();

        /*url地址列表*/
            List<ResourceUrl> resourceUrls = new ArrayList<>();

        /*获取url数据*/

            List<PrivilegeResource> privilegeResource = new ArrayList<>();//privilegesResourceService.findByUidAppIdAndUserId(pivilegeToken.getAppId(),appUserId);
            PrivilegeResource privilegeResourceModel = new PrivilegeResource();
            privilegeResourceModel.setBaseUrl("http://www.google.com");
            privilegeResource.add(privilegeResourceModel);
            privilegeResourceModel = new PrivilegeResource();
            privilegeResourceModel.setBaseUrl("http://www.baidu.com");
            privilegeResource.add(privilegeResourceModel);

        /*获取url地址*/
            StringBuilder stringBuilder = new StringBuilder(50);

            for (PrivilegeResource privilegeResource1 : privilegeResource) {
                stringBuilder.append(privilegeResource1.getBaseUrl()+",");
            }

             /*单条url数据*/
            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setKey(key);
            resourceUrl.setUrl(stringBuilder.toString());
            resourceUrls.add(resourceUrl);
            resourceUrlData.setResourceUrls(resourceUrls);
            redisDao.putUrlRedis(resourceUrlData, pivilegeToken.getAppId(), privilegeUser.getuId());
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        }

        map.put("status", 1);
        map.put("list", urlJedis);
        writeSuccessJson(response, map);
        return;
    }

}
