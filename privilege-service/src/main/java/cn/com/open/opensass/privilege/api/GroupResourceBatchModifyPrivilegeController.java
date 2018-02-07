package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称 : 组织机构权限批量更新.
 * 创建日期 : 2017/6/8.
 * 创建时间 : 11:31.
 */
@Controller
@RequestMapping("/group/")
public class GroupResourceBatchModifyPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(GroupResourceBatchModifyPrivilegeController.class);
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeGroupResourceService privilegeGroupResourceService;
    @Autowired
    private PrivilegeGroupService privilegeGroupService;

    /**
     * 组织机构权限批量更新
     *
     * @return Json
     */
    @RequestMapping(value = "batchGrModifyPrivilege",method = RequestMethod.POST)
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter("groupId"); //组织机构id(多个用“,”分隔）（必传）
        String appId = request.getParameter("appId"); //（必传）
        String resourceId = request.getParameter("resourceId"); //添加或删除的权限（多个权限用“,”分隔） （必传）
        String operationType = request.getParameter("operationType"); //操作类型( 0是添加1是删除) （必传）
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
        Map<String, Object> map = batchUpdateUserGroupResource(appId, groupId, resourceId, createUser, createUserid, status,operationType);

        if (map.get("status") == "0") {
            writeSuccessJson(response, map);
        } else {
            writeErrorJson(response, map);
        }
        return;

    }
    /*操作数据*/
    Map<String, Object> batchUpdateUserGroupResource(String appId, String groupIds, String resourceIds, String createUser, String createUserid, String status,String operationType) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            /*批量插入groupid的resourceid*/
            PrivilegeBatchUserVo privilegeBatchUserVo;
            List<PrivilegeBatchUserVo> privilegeBatchUserVoList = new ArrayList<PrivilegeBatchUserVo>();
            String[] groupIdList = groupIds.split(",");
            String[] resourceIdList = resourceIds.split(",");
            for (String groupId : groupIdList) {
                if (groupId != null && groupId != "") {
                    for (String resourceId : resourceIdList) {
                        if (resourceId != null && resourceId != "") {
                            /*生成批量更新语句*/
                            privilegeBatchUserVo = new PrivilegeBatchUserVo();
                            privilegeBatchUserVo.setAppId(appId);
                            privilegeBatchUserVo.setGroupId(groupId);
                            privilegeBatchUserVo.setResourceIds(resourceId);
                            if(operationType.equals("0")){ //添加操作
                                privilegeBatchUserVo.setCreateUser(createUser);
                                privilegeBatchUserVo.setCreateUserId(createUserid);
                                if (!nullEmptyBlankJudge(status)) {
                                    privilegeBatchUserVo.setStatus(Integer.parseInt(status));
                                } else {
                                    privilegeBatchUserVo.setStatus(0);
                                }
                            }
                            privilegeBatchUserVoList.add(privilegeBatchUserVo);
                        }
                    }
                }
            }
            if (null != privilegeBatchUserVoList && privilegeBatchUserVoList.size() > 0) {
                Boolean success = false;
                if(operationType.equals("0")){//添加操作
                    success = privilegeGroupResourceService.batchUpdateResourceIds(privilegeBatchUserVoList);
                } else if(operationType.equals("1")){//删除操作
                    success = privilegeGroupResourceService.batchDeleteResourceIdsByGroupIds(privilegeBatchUserVoList);
                }
                if (success) {
                    /*更新缓存*/
                    updateRedisCache(appId,groupIdList);
                    map.put("status", "1");
                    map.put("message", "操作成功:!");
                } else {
                    map.put("status", "0");
                    map.put("error_code", "10005");
                    map.put("message", "操作数据失败");
                }
            } else {
                map.put("status", "0");
                map.put("error_code", "10006");
                map.put("message", "无可操作数据");
            }

        } catch (Exception e) {
            map.put("status", "0");
            map.put("error_code", "10009");
            map.put("message", "系统错误");
        }
        return map;
    }
    /*更新redis缓存*/
    PrivilegeAjaxMessage updateRedisCache(final String appId, final String[] groupIdList){
        final PrivilegeAjaxMessage[] message = {null};
        try{
            final ExecutorService threadPool = Executors.newCachedThreadPool();//线程池里面的线程数会动态变化

            for (final String groupId : groupIdList) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (threadPool) {
                            if (groupId != null && groupId != "") {
                                log.debug("Thread Name is" + Thread.currentThread().getName() + ",groupId:" + groupId);
                                //更新缓存
                                message[0] = privilegeGroupService.updateGroupPrivilegeCache(groupId, appId);
                                //更新机构版本号
//                                privilegeGroupService.updateGroupVersion(groupId, appId);
                            }
                        }
                    }
                });
            }
        }
        catch (Exception e){
            message[0] = null;
            e.printStackTrace();
        }
        return message[0];
    }
}
