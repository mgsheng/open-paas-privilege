package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrlData;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.*;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.*;
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
    @Autowired
    private PrivilegeRoleResourceService privilegeRoleResourceService;
    @Autowired
    private PrivilegeResourceService privilegeResourceService;
    @Autowired
    private PrivilegeGroupService privilegeGroupService;
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
        if (null == privilegeUser) {

            map.put("status", 0);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        /*appid*/
        String appId = pivilegeToken.getAppId().trim();
        String key = RedisConstant.USERPRIVILEGES_CACHE + pivilegeToken.getAppId() + RedisConstant.SIGN + privilegeUser.getuId();

        /*缓存中是否存在*/
        String urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        System.out.println("是否为空（过期）："+urlJedis);
        if (null == urlJedis && "" != urlJedis) {

             /*封装list类*/
            PrivilegeUrl privilegeUrl = new PrivilegeUrl();

        /*url地址列表*/
            List<ResourceUrl> resourceUrls = new ArrayList<ResourceUrl>();
            /*资源获取通过UserId*/
            PrivilegeResource privilegeResourcesss = privilegeResourceService.findResourceByAppUserId(privilegeUser.getAppUserId());
            privilegeUrl.setPrivilegeUrl(privilegeResourcesss.getBaseUrl());

            /*角色*/
           /* PrivilegeRoleResource privilegeRoleResource = privilegeRoleResourceService.findByRoleIdAndResourceId(privilegeUser.getPrivilegeRoleId(),"123");

            List<PrivilegeUrl> privilegeUrlListOne =  getPrivilegeFunctionList(privilegeRoleResource.getPrivilegeFunId());

            List<ResourceUrl> resourceUrlsdata = new ArrayList<>();
            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setPrivilegeUrl(privilegeResourcesss.getBaseUrl());
            if(null != privilegeUrlListOne && privilegeUrlListOne.size()>0)
            {
               // resourceUrl.setChildUrl(privilegeUrlListOne);
            }
            resourceUrlsdata.add(resourceUrl);*/
            redisDao.putUrlRedis(privilegeUrl, pivilegeToken.getAppId(), privilegeUser.getuId());
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        }

        //map.put("",urlJedis);
        //map.put("key", key);
        //writeSuccessJson(response, map);
        if (null != urlJedis) {
            WebUtils.writeJson(response, urlJedis);
        } else {
            map.put("status", 0);
            map.put("error_code", "10002");/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }


    /*返回功能列表*/
    private List<PrivilegeUrl> getPrivilegeFunctionList(String funIdList)
    {
        List<PrivilegeUrl> privilegeUrlList = new ArrayList<PrivilegeUrl>();
        PrivilegeFunction privilegeFunction = null;
        PrivilegeUrl privilegeUrl = null;
        if(null != funIdList && funIdList.length()>0)
        {
            String[] strings = funIdList.split(",");
            StringBuffer stringBuffer = new StringBuffer(50);
            for (String string : strings)
            {
                if(null != string && "" != string)
                {
                    privilegeFunction = privilegeFunctionService.findByFunctionId(string);
                    if(null != privilegeFunction.getOptUrl() && "" != privilegeFunction.getOptUrl())
                        privilegeUrl = new PrivilegeUrl();
                        stringBuffer.append(privilegeFunction.getOptUrl());
                }
            }
            privilegeUrlList.add(privilegeUrl);
        }
        if(privilegeUrlList.size()>0)
            return privilegeUrlList;
        return null;
    }
    /*综合查询*/

    /**
     *
     * @param privilegeRoleId 角色ID
     * @param resourceId 资源ID（多个）
     * @param functionId funId （多个）
     * @param appId
     * @return
     */
    private List<ResourceUrl> getResourceIdAndFunId(String privilegeRoleId,String resourceId,String functionId, String appId,String UserId)
    {
        return null;
    }

    /*
        获取url列表方法
     */
    private List<ResourceUrl> getPrivilegeResourceByUserRoleId(List<ResourceUrl> resourceUrlList, List<PrivilegeRoleResource> privilegeRoleResourceList, String appId) {
        for (PrivilegeRoleResource roleResource : privilegeRoleResourceList) {
                 /*获取url地址*/
            try {
                PrivilegeResource privilegeResourcedatat = privilegeResourceService.findByResourceId(roleResource.getResourceId(), appId);
                if (null != privilegeResourcedatat) {
                    List<PrivilegeUrl> privilegeUrlList = new ArrayList<PrivilegeUrl>();
                    PrivilegeUrl privilegeUrl = null;
                    for (PrivilegeRoleResource privilegeRoleResource : privilegeRoleResourceList) {
                        if (null != privilegeRoleResource.getPrivilegeFunId()) {
                            /*获取function中的opt_url*/
                            PrivilegeFunction privilegeFunction = privilegeFunctionService.findByFunctionId(privilegeRoleResource.getPrivilegeFunId());
                            if (null != privilegeFunction && null != privilegeFunction.getOptUrl() && "" != privilegeFunction.getOptUrl()) {
                                privilegeUrl = new PrivilegeUrl();
                                privilegeUrl.setChildUrl(privilegeFunction.getOptUrl());
                                privilegeUrlList.add(privilegeUrl);
                            }
                        }
                    }
                    ResourceUrl resourceUrl = new ResourceUrl();
                    if (privilegeUrlList.size() > 0) {
                        resourceUrl.setPrivilegeUrl(privilegeResourcedatat.getBaseUrl());
                        //resourceUrl.setChildUrl(privilegeUrlList);
                    } else {
                        resourceUrl.setPrivilegeUrl(privilegeResourcedatat.getBaseUrl());
                    }
                    resourceUrlList.add(resourceUrl);

                }
            } catch (Exception e) {
                log.error("获取资源链接url ERROR:" + e);
            }
        }
        return resourceUrlList;
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
        String data="";
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        if (null == privilegeUser) {

            map.put("status", 0);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        /*appid*/
        String appId = pivilegeToken.getAppId().trim();
        String key = RedisConstant.USERPRIVILEGES_CACHE + pivilegeToken.getAppId() + RedisConstant.SIGN + privilegeUser.getuId();

        /*缓存中是否存在*/
        redisDao.deleteUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        String urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());

        if (null == urlJedis && "" != urlJedis) {
        	
        	
        ArrayList<String> urlList=privilegeUserService.findUserResources(appId, appUserId);
        Map b=new HashMap();
        b.put("urlList", urlList);
         data=JSONObject.fromObject(b).toString();
      /*  	
             封装list类
            ResourceUrlData resourceUrlData = new ResourceUrlData();

        url地址列表
            List<ResourceUrl> resourceUrls = new ArrayList<ResourceUrl>();

        获取url数据
            角色ID
            String privilegeRoleId = privilegeUser.getPrivilegeRoleId();
            资源列表
            List<PrivilegeRoleResource> privilegeRoleResourceList = privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleId);
            List<PrivilegeResource> privilegeResourceList = new ArrayList<PrivilegeResource>();
            角色获取资源
            resourceUrls = getPrivilegeResourceByUserRoleId(resourceUrls, privilegeRoleResourceList, appId);
            *//**
             * 通过用户表的角色Id直接获取资源
             *//*
            PrivilegeResource privilegeResource = privilegeResourceService.findByResourceId(privilegeUser.getResourceId(), appId);
            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setPrivilegeUrl(privilegeResource.getBaseUrl());
            //resourceUrl.setUrlFun(privilegeUser.getPrivilegeFunId());
            resourceUrls.add(resourceUrl);
            通过groupId获取资源
            String groupId = privilegeUser.getGroupId();
            PrivilegeGroup privilegeGroup = privilegeGroupService.findBygroupId(groupId,appId);
             单条url数据
            ResourceUrl resourceUrl = new ResourceUrl();
            if(null != privilegeResourceList && privilegeResourceList.size()>0)
            {
                resourceUrl.setPrivilegeResources(privilegeResourceList);
            }
            resourceUrls.add(resourceUrl);
            resourceUrls.add(resourceUrl);
            resourceUrlData.setResourceUrls(resourceUrls);

            redisDao.updateUrlRedis(resourceUrlData, pivilegeToken.getAppId(), privilegeUser.getuId());
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());*/
        }
        //map.put("",urlJedis);
        //map.put("key", key);
        //writeSuccessJson(response, map);
        if (data!=null&&data.length()>0) {
            WebUtils.writeJson(response, data);
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
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        redisDao.deleteUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        map.put("status", 0);
        writeSuccessJson(response, map);
        return;
    }


    /**
     * 判断url是否存在
     *
     * @param request
     * @param response
     * @param appUserId
     * @param durl
     * @param pivilegeToken
     */

    @RequestMapping(value = "existUrlPrivilege/{appUserId}/{durl}", method = RequestMethod.POST)
    public void existUrlData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, @PathVariable("durl") String durl, PivilegeToken pivilegeToken) {

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
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        boolean exist = redisDao.existUrlRedis(durl, pivilegeToken.getAppId(), privilegeUser.getuId());
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
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(pivilegeToken.getAppId(), pivilegeToken.getAppKey(), pivilegeToken.getSignature(), pivilegeToken.getSignatureNonce(), pivilegeToken.getTimestamp()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        /*获取用户UID*/
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(pivilegeToken.getAppId(), appUserId);
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        boolean exist = redisDao.existKeyRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        map.put("exist", exist);
        writeSuccessJson(response, map);
        return;
    }
}
