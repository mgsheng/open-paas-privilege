package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
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
import java.util.*;

/**
 * 项目名称 : 角色批量更新，在原有基础上新增角色.
 * 创建日期 : 2017/5/9.
 * 创建时间 : 15:45.
 */
@Controller
@RequestMapping("/userRole/")
public class UserRoleBatchModifyPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(UserRoleModifyPrivilegeController.class);
    @Autowired
    private PrivilegeUserService privilegeUserService;
    @Autowired
    private AppService appService;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private PrivilegeUserRedisService privilegeUserRedisService;
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    @Autowired
    private PrivilegeUrlService privilegeUrlService;

    /**
     * 用户角色修改接口
     */
    @RequestMapping(value = "batchModifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response, PrivilegeUserVo privilegeUserVo) {
        log.info("====================batch modify user resource start======================");
        if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId(), privilegeUserVo.getResourceId()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        if (app == null) {
            app = appService.findById(Integer.parseInt(privilegeUserVo.getAppId()));
            redisClient.setObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId(), app);
        }
        //认证
        Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
        if (!f) {
            paraMandaChkAndReturn(10002, response, "认证失败");
            return;
        }


        String userData = null;
        StringBuilder stringBuilderUsers = new StringBuilder();
        String[] userList = privilegeUserVo.getAppUserId().split(",");
        for (String user:userList){
            stringBuilderUsers.append("'").append(user).append("'").append(",");
        }
        if(stringBuilderUsers!=null &&stringBuilderUsers.length()>0){
            userData = stringBuilderUsers.substring(0,stringBuilderUsers.length()-1);
        }
        List<PrivilegeUser> users = null;
        if(userData != null){
            users = privilegeUserService.findByAppIdAndUserIds(privilegeUserVo.getAppId(), userData);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if(users == null || users.size()<=0){
            map.put("status", "0");
            map.put("error_code", "10008");
            map.put("message", "无可更新数据");
        } else {
            map = batchUpdateUserResource(privilegeUserVo,users);
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }

    PrivilegeUser getPrivilegeUserByUserId(String userId,List<PrivilegeUser> users){
        PrivilegeUser privilegeUserReturn = null;
        for (PrivilegeUser privilegeUser : users){
            if(privilegeUser.getAppUserId().equals(userId)){
                privilegeUserReturn = privilegeUser;
            }
        }
        return privilegeUserReturn;
    }

    /*操作数据*/
    Map<String, Object> batchUpdateUserResource(PrivilegeUserVo privilegeUserVo,List<PrivilegeUser> userListData) {
        Map<String, Object> mapData = new HashMap<String, Object>();
        try {
            /*批量更新resourceid*/
            List<PrivilegeBatchUserVo> privilegeBatchUserVoList = new ArrayList<PrivilegeBatchUserVo>();
            PrivilegeBatchUserVo privilegeBatchUserVo = null;
            StringBuilder stringBuilderUser = new StringBuilder(50);
            StringBuilder stringBuilderUserError = new StringBuilder(50);
            String[] users = privilegeUserVo.getAppUserId().split(",");
            for (String userData : users) {
                mapData = new HashMap<String, Object>();
                /*PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(), userData);*/
                PrivilegeUser user = getPrivilegeUserByUserId(userData,userListData);
                if (user == null) {
                    stringBuilderUserError.append(userData).append(",");
                } else {
                    /*生成批量更新语句*/
                    privilegeBatchUserVo = new PrivilegeBatchUserVo();
                    privilegeBatchUserVo.setAppId(privilegeUserVo.getAppId());
                    privilegeBatchUserVo.setAppUserId(userData);
                    if (user.getResourceId() != null && user.getResourceId() != "") {
                        privilegeBatchUserVo.setResourceIds(user.getResourceId() + "," + privilegeUserVo.getResourceId());
                    } else {
                        privilegeBatchUserVo.setResourceIds(privilegeUserVo.getResourceId());
                    }
                    privilegeBatchUserVoList.add(privilegeBatchUserVo);
                    stringBuilderUser.append(userData).append(",");
                }
            }

        /*执行批量操作*/
            if (privilegeBatchUserVoList.size() > 0) {
                String userList;
                String userListError = null;
                if (stringBuilderUserError != null && stringBuilderUserError.length() > 0) {
                    userListError = stringBuilderUserError.substring(0, stringBuilderUserError.length() - 1);
                }
                if (stringBuilderUser != null && stringBuilderUser.length() > 0) {
                    userList = stringBuilderUser.substring(0, stringBuilderUser.length() - 1);
                    if (userList != null && userList != "") {
                        Boolean batchUpdate = privilegeUserService.batchUpdateResourceIds(privilegeBatchUserVoList);
                        if (batchUpdate) {
                            PrivilegeAjaxMessage message = null;
                            String[] strUserRedisUpdate = userList.split(",");
                            //更新缓存
                            for (String userRedis : strUserRedisUpdate) {
                                if (userRedis != null && userRedis != "") {
                                    message = privilegeUserRedisService.updateUserRoleRedis(privilegeUserVo.getAppId(), userRedis);
                                    privilegeMenuService.updateMenuRedis(privilegeUserVo.getAppId(), userRedis);
                                    privilegeUrlService.updateRedisUrl(privilegeUserVo.getAppId(), userRedis);
                                }
                            }
                            if (message != null && message.getCode().equals("1")) {
                                mapData.put("status", "1");
                                if (userListError != null && userListError != "") {
                                    mapData.put("message", "更新成功:" + userList + "!失败数据:" + userListError);
                                } else {
                                    mapData.put("message", "更新成功:" + userList + "!");
                                }
                            } else {
                                mapData.put("status", "0");
                                mapData.put("error_code", "10004");
                                mapData.put("message", "更新redis缓存失败");/* 数据不存在 */
                            }
                        } else {
                            mapData.put("status", "0");
                            mapData.put("error_code", "10005");
                            mapData.put("message", "更新失败:" + userList);
                        }
                    } else {
                        mapData.put("status", "0");
                        mapData.put("error_code", "10006");
                        mapData.put("message", "更新失败:用户数据不存在");
                    }
                } else {
                    mapData.put("status", "0");
                    mapData.put("error_code", "10007");
                    mapData.put("message", "无可更新数据，请检查数据是否正确");
                }

            } else {
                mapData.put("status", "0");
                mapData.put("error_code", "10008");
                mapData.put("message", "无可更新数据");
            }
        } catch (Exception e) {
            mapData.put("status", "0");
            mapData.put("error_code", "10009");
            mapData.put("message", "系统错误");
        }
        return mapData;
    }
}
