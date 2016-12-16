package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PivilegeToken;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import net.sf.json.JSONObject;
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
 * redis url 接口
 * 必传参数：PivilegeToken
 * 获取数据：etDataPrivilege/{appUserId}
 * 修改数据：updateDataPrivilege/{appUserId}
 * 删除数据：deleteDataPrivilege/{appUserId}
 * 判断url是否存在：existUrlPrivilege/{durl}/{appUserId}
 * 判断key是否在redis中存在 ：existKeyPrivilege/{appUserId}
 */
@Controller
@RequestMapping("/url/")
public class UrlRedisPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(UrlRedisPrivilegeController.class);
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private PrivilegeRoleResourceService privilegeRoleResourceService;
    @Autowired
    private PrivilegeResourceService privilegeResourceService;
    @Autowired
    private PrivilegeFunctionService privilegeFunctionService;


    /**
     * 测试地址
     * http://localhost:8080/url/deleteDataPrivilege/0bb6f4bfc0d711e6a6df0050568c069a?appId=appcbfb25e6c0d611e6a6df0050568c069m&appKey=appcbfb25e6c0d611e6a6df0050568c069m&signature=appcbfb25e6c0d611e6a6df0050568c069m&timestamp=2014-05-26T12:00:00Z&signatureNonce=ss
     *
     * @param request
     * @param response
     * @param appUserId
     * @param pivilegeToken
     */
    @RequestMapping(value = "getDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void putdata(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {

        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        log.info("getDataPrivilege用户数据，appid="+pivilegeToken.getAppId()+",用户Id="+appUserId);
        if (null == privilegeUser) {

            map.put("status", 0);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        /*appid*/
        String appId = pivilegeToken.getAppId().trim();
        /*缓存中是否存在*/
        String urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        log.info("getDataPrivilege接口读取redis数据："+urlJedis);
        if (null == urlJedis && "" != urlJedis) {
            PrivilegeUrl privilegeUrl = getPrivilegeUrl(appId, appUserId, privilegeUser);
            /*写入redis*/
            log.info("getDataPrivilege接口获取数据并写入redis数据开始");
            redisDao.putUrlRedis(privilegeUrl, pivilegeToken.getAppId(), privilegeUser.getuId());
            /*读取redis*/
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
            log.info("getDataPrivilege接口获取数据并写入，读取redis数据开始："+urlJedis);
        }
        if (null != urlJedis) {
            WebUtils.writeJson(response, urlJedis);
        } else {
            map.put("status", 0);
            map.put("error_code", "10002");/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }

    /**
     * 获取写入redis的数据
     *
     * @param appId         应用ID
     * @param appUserId     应用ID 用户ID
     * @param privilegeUser 获取用户model
     * @return
     */
    private PrivilegeUrl getPrivilegeUrl(String appId, String appUserId, PrivilegeUser privilegeUser) {
        Set<String> setUrl = (Set<String>) new HashSet();
        /*资源*/
        ArrayList<String> urlList = privilegeUserService.findUserResources(appId, appUserId);
        if (null != urlList && urlList.size() > 0) {
            for (String string : urlList) {
                if (null != string && string.length() > 0) {
                    setUrl.add(string);
                }
            }
        }
        /*资源funId*/
        ArrayList<String> functionIdList = privilegeRoleResourceService.findUserResourcesFunId(appId, appUserId);
        if (null != functionIdList && functionIdList.size() > 0) {
            for (String functionId : functionIdList) {
                if (null != functionId && functionId.length() > 0) {
                    PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(functionId);
                    setUrl.add(privilegeFunction.getOptUrl());
                }
            }
        }
        /*用户资源*/
        if (null != privilegeUser.getResourceId() && privilegeUser.getResourceId().length() > 0) {
            String[] resourceIds = privilegeUser.getResourceId().split(",");
            for (String resourceId : resourceIds) {
                if (null != resourceId && resourceId.length() > 0) {
                    PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(privilegeUser.getResourceId(), appId);
                    setUrl.add(privilegeResource.getBaseUrl());
                }
            }
        }
        /*链接资源*/
        if (null != privilegeUser.getPrivilegeFunId() && privilegeUser.getPrivilegeFunId().length() > 0) {
            String[] functionIds = privilegeUser.getPrivilegeFunId().split(",");
            for (String functionId : functionIds) {
                if (null != functionId && functionId.length() > 0) {
                    PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(functionId);
                    setUrl.add(privilegeFunction.getOptUrl());
                }
            }
        }
            /*角色资源获取链接*/

        PrivilegeUrl privilegeUrl = new PrivilegeUrl();
        Map b = new HashMap();
        b.put("urlList", setUrl);
        String data = JSONObject.fromObject(b).toString();
        privilegeUrl.setPrivilegeUrl(data);
        return privilegeUrl;
    }

    /**
     * 更新jedis
     *
     * @param request
     * @param response
     * @param appUserId
     * @param pivilegeToken
     */
    @RequestMapping(value = "updateDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void updateData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {


        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<Object, Object> map = new HashMap<Object, Object>();
        String data = "";
        log.info("===================get updateDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        log.info("updateDataPrivilege用户数据，appid="+pivilegeToken.getAppId()+",用户Id="+appUserId);
        if (null == privilegeUser) {

            map.put("status", 0);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        /*appid*/
        String appId = pivilegeToken.getAppId().trim();
        /*删除redis健值*/
        log.info("updateDataPrivilege删除redis数据");
        redisDao.deleteUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());

        log.info("updateDataPrivilege写入redis数据");
        PrivilegeUrl privilegeUrl = getPrivilegeUrl(appId, appUserId, privilegeUser);
        /*写入redis*/
        redisDao.putUrlRedis(privilegeUrl, pivilegeToken.getAppId(), privilegeUser.getuId());
        /*读取redis*/
        String urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        log.info("updateDataPrivilege读取redis数据====="+urlJedis);
        if (urlJedis != null && urlJedis.length() > 0) {
            WebUtils.writeJson(response, urlJedis);
        } else {
            map.put("status", 0);
            map.put("error_code", "10002");/*数据不存在*/
            writeErrorJson(response, map);
        }
    }

    /**
     * 根据key删除jedis
     *
     * @param request
     * @param response
     * @param appUserId
     * @param pivilegeToken
     */
    @RequestMapping(value = "deleteDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void deleteData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {

        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get deleteDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        log.info("deleteDataPrivilege用户数据，appid="+pivilegeToken.getAppId()+",用户Id="+appUserId);
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        log.info("deleteDataPrivilege删除redis数据");
        redisDao.deleteUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        map.put("status", 0);
        writeSuccessJson(response, map);
        return;
    }


    /**
     * 判断url是否存在
     * http://localhost:8080/url/existUrlPrivilege/baidu.com/0bb6f4bfc0d711e6a6df0050568c069a?appId=appcbfb25e6c0d611e6a6df0050568c069m&appKey=appcbfb25e6c0d611e6a6df0050568c069m&signature=appcbfb25e6c0d611e6a6df0050568c069m&timestamp=2014-05-26T12:00:00Z&signatureNonce=ss
     *
     * @param request
     * @param response
     * @param appUserId
     * @param durl
     * @param pivilegeToken
     */

    @RequestMapping(value = "existUrlPrivilege/{durl}/{appUserId}", method = RequestMethod.POST)
    public void existUrlData(HttpServletRequest request, HttpServletResponse response, @PathVariable("durl") String durl, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {

        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get existUrlPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        log.info("existUrlPrivilege用户数据，appid="+pivilegeToken.getAppId()+",用户Id="+appUserId+",url="+durl);
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        boolean exist = redisDao.existUrlRedis(durl, pivilegeToken.getAppId(), privilegeUser.getuId());
        log.info("existUrlPrivilege==url是否存在："+exist);
        map.put("exist", exist);
        writeSuccessJson(response, map);
        return;
    }

    @RequestMapping(value = "existKeyPrivilege/{appUserId}", method = RequestMethod.POST)
    public void existkeyData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken) {

        /**
         * 1. 判断传入参数是否正确
         * 2. 获取jedis数据，判断jedis是否为空
         * 3. 如果为空则读取数据库，将数据库数据放到jedis缓存中
         */
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get existKeyPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        log.info("existKeyPrivilege用户数据，appid="+pivilegeToken.getAppId()+",用户Id="+appUserId+"");
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        boolean exist = redisDao.existKeyRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        log.info("existKeyPrivilege==url是否存在："+exist);
        map.put("exist", exist);
        writeSuccessJson(response, map);
        return;
    }
}
