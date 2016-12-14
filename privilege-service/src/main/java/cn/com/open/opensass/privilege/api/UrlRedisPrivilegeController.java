package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.dao.ResourceUrl;
import cn.com.open.opensass.privilege.dao.ResourceUrlData;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.*;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
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
 * Created by jh on 2016/12/12.
 *
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


    /**
     * 测试地址
     * http://localhost:8080/url/deleteDataPrivilege/0bb6f4bfc0d711e6a6df0050568c069a?appId=appcbfb25e6c0d611e6a6df0050568c069m&appKey=appcbfb25e6c0d611e6a6df0050568c069m&signature=appcbfb25e6c0d611e6a6df0050568c069m&timestamp=2014-05-26T12:00:00Z&signatureNonce=ss
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
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appUserId, pivilegeToken.getAppId());
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

        if (null == urlJedis && "" != urlJedis ) {
             /*封装list类*/
            ResourceUrlData resourceUrlData = new ResourceUrlData();

        /*url地址列表*/
            List<ResourceUrl> resourceUrls = new ArrayList<>();

        /*获取url数据*/
            /*角色ID*/
            String privilegeRoleId = privilegeUser.getPrivilegeRoleId();
            /*资源列表*/
            List<PrivilegeRoleResource> privilegeRoleResourceList = privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleId);
            List<PrivilegeResource> privilegeResourceList= new ArrayList<PrivilegeResource>();
            /*角色获取资源*/
            resourceUrls = getPrivilegeResourceByUserRoleId(resourceUrls,privilegeRoleResourceList,appId);
            /**
             * 通过用户表的角色Id直接获取资源
             */
            PrivilegeResource privilegeResource = privilegeResourceService.findByResourceCode(privilegeUser.getResourceId(),appId);
            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setUrl(privilegeResource.getBaseUrl());
            //resourceUrl.setUrlFun(privilegeUser.getPrivilegeFunId());
            resourceUrls.add(resourceUrl);
            /*通过groupId获取资源*/
           /* String groupId = privilegeUser.getGroupId();
            PrivilegeGroup privilegeGroup = privilegeGroupService.findBygroupId(groupId,appId);*/
             /*单条url数据*/
           /* ResourceUrl resourceUrl = new ResourceUrl();
            if(null != privilegeResourceList && privilegeResourceList.size()>0)
            {
                *//*resourceUrl.setPrivilegeResources(privilegeResourceList);*//*
            }*/
            resourceUrls.add(resourceUrl);
            resourceUrls.add(resourceUrl);
            resourceUrlData.setResourceUrls(resourceUrls);

            redisDao.putUrlRedis(resourceUrlData, pivilegeToken.getAppId(), privilegeUser.getuId());
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        }
        //map.put("",urlJedis);
        //map.put("key", key);
        //writeSuccessJson(response, map);
        WebUtils.writeJson(response,urlJedis);
        return;
    }

    private List<ResourceUrl> getPrivilegeResourceByUserRoleId(List<ResourceUrl> resourceUrlList,List<PrivilegeRoleResource> privilegeRoleResourceList,String appId)
    {
        for (PrivilegeRoleResource roleResource : privilegeRoleResourceList) {
                 /*获取url地址*/
            try
            {
                PrivilegeResource privilegeResourcedatat = privilegeResourceService.findByResourceCode(roleResource.getResourceId(),appId);
                if(null != privilegeResourcedatat)
                {
                    StringBuffer stringBuffer = new StringBuffer(50);
                    for (PrivilegeRoleResource privilegeRoleResource : privilegeRoleResourceList)
                    {
                        if(null != privilegeRoleResource.getPrivilegeFunId())
                        {
                            stringBuffer.append(privilegeRoleResource.getPrivilegeFunId()).append(",");
                        }
                    }
                    if(stringBuffer.length()>0)
                    {
                        stringBuffer.substring(0,stringBuffer.length()-1);
                    }

                    ResourceUrl resourceUrl = new ResourceUrl();
                    resourceUrl.setUrl(privilegeResourcedatat.getBaseUrl());
                    //resourceUrl.setUrlFun(stringBuffer.toString());
                    resourceUrlList.add(resourceUrl);

                }
            }
            catch (Exception e)
            {
                log.error("获取资源链接url ERROR:"+e);
            }
        }
        return resourceUrlList;
    }

    @RequestMapping(value = "updateDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void updateData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken)
    {


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
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appUserId, pivilegeToken.getAppId());
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

        if (null == urlJedis && "" != urlJedis ) {
             /*封装list类*/
            ResourceUrlData resourceUrlData = new ResourceUrlData();

        /*url地址列表*/
            List<ResourceUrl> resourceUrls = new ArrayList<>();

        /*获取url数据*/
            /*角色ID*/
            String privilegeRoleId = privilegeUser.getPrivilegeRoleId();
            /*资源列表*/
            List<PrivilegeRoleResource> privilegeRoleResourceList = privilegeRoleResourceService.findByPrivilegeRoleId(privilegeRoleId);
            List<PrivilegeResource> privilegeResourceList= new ArrayList<PrivilegeResource>();
            /*角色获取资源*/
            resourceUrls = getPrivilegeResourceByUserRoleId(resourceUrls,privilegeRoleResourceList,appId);
            /**
             * 通过用户表的角色Id直接获取资源
             */
            PrivilegeResource privilegeResource = privilegeResourceService.findByResourceCode(privilegeUser.getResourceId(),appId);
            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setUrl(privilegeResource.getBaseUrl());
            //resourceUrl.setUrlFun(privilegeUser.getPrivilegeFunId());
            resourceUrls.add(resourceUrl);
            /*通过groupId获取资源*/
           /* String groupId = privilegeUser.getGroupId();
            PrivilegeGroup privilegeGroup = privilegeGroupService.findBygroupId(groupId,appId);*/
             /*单条url数据*/
           /* ResourceUrl resourceUrl = new ResourceUrl();
            if(null != privilegeResourceList && privilegeResourceList.size()>0)
            {
                *//*resourceUrl.setPrivilegeResources(privilegeResourceList);*//*
            }*/
            resourceUrls.add(resourceUrl);
            resourceUrls.add(resourceUrl);
            resourceUrlData.setResourceUrls(resourceUrls);

            redisDao.updateUrlRedis(resourceUrlData, pivilegeToken.getAppId(), privilegeUser.getuId());
            urlJedis = redisDao.getUrlRedis(pivilegeToken.getAppId(), privilegeUser.getuId());
        }
        //map.put("",urlJedis);
        //map.put("key", key);
        //writeSuccessJson(response, map);
        WebUtils.writeJson(response,urlJedis);
    }

    @RequestMapping(value = "deleteDataPrivilege/{appUserId}", method = RequestMethod.POST)
    public void deleteData(HttpServletRequest request, HttpServletResponse response, @PathVariable("appUserId") String appUserId, PivilegeToken pivilegeToken)
    {

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
        PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appUserId, pivilegeToken.getAppId());
        if (null == privilegeUser) {

            map.put("status", 1);
            map.put("error_code", "10000");/*此用户不存在*/
            writeErrorJson(response, map);
            return;
        }
        redisDao.deleteUrlRedis(pivilegeToken.getAppId(),privilegeUser.getuId());
        map.put("status", 0);
        writeSuccessJson(response, map);
        return;
    }
}
