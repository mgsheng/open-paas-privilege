package cn.com.open.opensass.privilege.api;


import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
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
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 *  权限资源修改接口
 */
@Controller
@RequestMapping("/group/")
public class GroupBatchModifyPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(GroupBatchModifyPrivilegeController.class);
    @Autowired
    private PrivilegeGroupResourceService privilegeGroupResourceService;
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeGroupService privilegeGroupService;
    /**
     * 权限资源修改接口
     * @return Json
     */
    @RequestMapping("batchModifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response) {
        String groupId=request.getParameter("groupId");
        String appId=request.getParameter("appId");
        String resourceId=request.getParameter("resourceId");
        String createUser=request.getParameter("createUser");
        String createUserid=request.getParameter("createUserid");
        String status=request.getParameter("status");
        log.info("====================batch modify user group resource start======================");
        if(!paraMandatoryCheck(Arrays.asList(groupId,resourceId,appId))){
            paraMandaChkAndReturn(10000, response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
        if(app==null)
        {
            app=appService.findById(Integer.parseInt(appId));
            redisClient.setObject(RedisConstant.APP_INFO+appId, app);
        }
        /*//认证
        Boolean f= OauthSignatureValidateHandler.validateSignature(request,app);

        if(!f){
            WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
            return;
        }*/
        Map<String, Object> map = batchUpdateUserGroupResource(appId,groupId,resourceId,createUser,createUserid,status);
        if (map.get("status") == "0") {
            writeSuccessJson(response,map);
        } else {
            writeErrorJson(response, map);
        }
        return;

    }
    /*操作数据*/
    Map<String, Object> batchUpdateUserGroupResource(String appId,String groupIds,String resourceIds,String createUser,String createUserid,String status){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            /*批量插入groupid的resourceid*/
            PrivilegeBatchUserVo privilegeBatchUserVo;
            List<PrivilegeBatchUserVo> privilegeBatchUserVoList = new ArrayList<PrivilegeBatchUserVo>();
            String[] groupIdList = groupIds.split(",");
            String[] resourceIdList = resourceIds.split(",");
            for (String groupId:groupIdList){
                if(groupId != null && groupId != ""){
                    for (String resourceId:resourceIdList){
                        if(resourceId != null && resourceId != ""){
                            /*生成批量更新语句*/
                            privilegeBatchUserVo = new PrivilegeBatchUserVo();
                            privilegeBatchUserVo.setAppId(appId);
                            privilegeBatchUserVo.setCreateUser(createUser);
                            privilegeBatchUserVo.setCreateUserId(createUserid);
                            privilegeBatchUserVo.setGroupId(groupId);
                            privilegeBatchUserVo.setResourceIds(resourceId);
                            if(!nullEmptyBlankJudge(status)){
                                privilegeBatchUserVo.setStatus(Integer.parseInt(status));
                            }else{
                                privilegeBatchUserVo.setStatus(1);
                            }
                            privilegeBatchUserVoList.add(privilegeBatchUserVo);
                        }
                    }
                }
            }
            if(null != privilegeBatchUserVoList && privilegeBatchUserVoList.size()>0){
                if(privilegeGroupResourceService.batchUpdateResourceIds(privilegeBatchUserVoList)){
                    PrivilegeAjaxMessage message = null;
                    for (String groupId:groupIdList) {
                        if (groupId != null && groupId != "") {
                            //更新缓存
                            message = privilegeGroupService.updateGroupPrivilegeCache(groupId, appId);
                            //更新机构版本号
                            privilegeGroupService.updateGroupVersion(groupId, appId);
                        }
                    }
                    if (message.getCode().equals("1")) {
                        map.put("status","1");
                        map.put("message", "更新成功:!");
                    } else {
                        map.put("status", "0");
                        map.put("error_code", "10004");
                        map.put("message", "更新redis缓存失败");
                    }
                } else {
                    map.put("status", "0");
                    map.put("error_code", "10005");
                    map.put("message", "更新数据失败");
                }
            } else {
                map.put("status", "0");
                map.put("error_code", "10006");
                map.put("message", "无可更新数据");
            }

        } catch (Exception e){
            map.put("status", "0");
            map.put("error_code", "10009");
            map.put("message", "系统错误");
        }
        return map;
    }
}
