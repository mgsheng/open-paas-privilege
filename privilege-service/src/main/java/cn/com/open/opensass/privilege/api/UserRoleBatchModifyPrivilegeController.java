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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Autowired
    private PrivilegeResourceService privilegeResourceService;

    /**
     * 用户角色修改接口
     */
    @RequestMapping(value = "batchModifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request, HttpServletResponse response, PrivilegeUserVo privilegeUserVo) {
        log.info("====================batch modify user resource start======================");
        if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId(), privilegeUserVo.getResourceId(),privilegeUserVo.getFunctionId(),privilegeUserVo.getOperationType()))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
        if (app == null) {
            app = appService.findById(Integer.parseInt(privilegeUserVo.getAppId()));
            redisClient.setObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId(), app);
        }
        //认证
       /* Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);
        if (!f) {
            paraMandaChkAndReturn(10002, response, "认证失败");
            return;
        }*/

        Map<String, Object> map = new HashMap<String, Object>();


        String userData = null;
        StringBuilder stringBuilderUsers = new StringBuilder();
        String[] userList = privilegeUserVo.getAppUserId().split(",");
        for (String user : userList) {
            stringBuilderUsers.append("'").append(user).append("'").append(",");
        }
        if (stringBuilderUsers != null && stringBuilderUsers.length() > 0) {
            userData = stringBuilderUsers.substring(0, stringBuilderUsers.length() - 1);
        }
        List<PrivilegeUser> users = null;
        if (userData != null) {
            users = privilegeUserService.findByAppIdAndUserIds(privilegeUserVo.getAppId(), userData);
        }
        if (users == null || users.size() <= 0) {
            map.put("status", "0");
            map.put("error_code", "10008");
            map.put("message", "无可更新数据");
        } else {
            map = batchUpdateUserResource(privilegeUserVo, users);
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }

    PrivilegeUser getPrivilegeUserByUserId(String userId, List<PrivilegeUser> users) {
        PrivilegeUser privilegeUserReturn = null;
        for (PrivilegeUser privilegeUser : users) {
            if (privilegeUser.getAppUserId().equals(userId)) {
                privilegeUserReturn = privilegeUser;
            }
        }
        return privilegeUserReturn;
    }

    String getReplaceLaterString(String oldString,String replaceString){
        String[] replaceStrings = replaceString.split(",");
        oldString = ","+oldString+","; //两边加上逗号不至于替换出错
        for (String replaceStr : replaceStrings){
            if(oldString.indexOf(","+replaceStr+",")>=0){
                oldString = oldString.replace(","+replaceStr+",",",");
            }
        }
        if(oldString.equals(",")){
            oldString = "";
        } else {
            oldString = oldString.substring(1,oldString.length()-1);
        }
        return oldString;
    }

    /**
     * 添加数据
     * 1. 在现有的resourceIds后加上传值resourceIds，生成需要新增之后的resourceIds.
     * 2. 在现有的functionIds后加上传值functionIds，生成需要新增之后的functionIds.
     * 3. 生成批量更新sql.
     * 删除数据.
     * 1. 替换现有的resourceIds，生成删除之后的resourceIds.
     * 2. 替换现有的functionIds，生成删除之后的functionIds.
     * 3. 生成批量更新sql.
     * @param privilegeUserVo
     * @param userListData
     * @return
     */
    Map<String, Object> batchUpdateUserResource(PrivilegeUserVo privilegeUserVo, List<PrivilegeUser> userListData) {
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
                PrivilegeUser user = getPrivilegeUserByUserId(userData, userListData);
                if (user == null) {
                    stringBuilderUserError.append(userData).append(",");
                } else {
                    /*生成批量更新语句*/
                    privilegeBatchUserVo = new PrivilegeBatchUserVo();
                    privilegeBatchUserVo.setAppId(privilegeUserVo.getAppId());
                    privilegeBatchUserVo.setAppUserId(userData);
                    //0是添加1是删除
                    if(privilegeUserVo.getOperationType().equals("0")){
                        if (user.getResourceId() != null && user.getResourceId() != "") {
                            privilegeBatchUserVo.setResourceIds(user.getResourceId() + "," + privilegeUserVo.getResourceId());
                        } else {
                            privilegeBatchUserVo.setResourceIds(privilegeUserVo.getResourceId());
                        }
                        if (user.getPrivilegeFunId() != null && user.getPrivilegeFunId() != "") {
                            privilegeBatchUserVo.setFunctionIds(user.getPrivilegeFunId() + "," + privilegeUserVo.getFunctionId());
                        } else {
                            privilegeBatchUserVo.setFunctionIds(privilegeUserVo.getResourceId());
                        }
                    } else if(privilegeUserVo.getOperationType().equals("1")){
                        if (user.getResourceId() != null && user.getResourceId() != "") {
                            privilegeBatchUserVo.setResourceIds(getReplaceLaterString(user.getResourceId(),privilegeUserVo.getResourceId()));
                        } else {
                            privilegeBatchUserVo.setResourceIds("");
                        }
                        if (user.getPrivilegeFunId() != null && user.getPrivilegeFunId() != "") {

                            privilegeBatchUserVo.setFunctionIds(getReplaceLaterString(user.getPrivilegeFunId(),privilegeUserVo.getFunctionId()));
                        } else {
                            privilegeBatchUserVo.setFunctionIds("");
                        }
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
                            String[] strUserRedisUpdate = userList.split(",");
                            /*更新redis缓存*/
                            updateRedisCache(privilegeUserVo, strUserRedisUpdate);
                            mapData.put("status", "1");
                            if (userListError != null && userListError != "") {
                                mapData.put("message", "更新成功:" + userList + "!失败数据:" + userListError);
                            } else {
                                mapData.put("message", "更新成功:" + userList + "!");
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
    /*更新redis缓存*/
    PrivilegeAjaxMessage updateRedisCache(final PrivilegeUserVo privilegeUserVo, final String[] users) {
        log.info("====================batch modify functionIds start======================");
        privilegeResourceService.updateAppResRedis(privilegeUserVo.getAppId());
        log.info("====================batch modify resourceIds start======================");
        final PrivilegeAjaxMessage[] message = {null};
        try {
            final ExecutorService threadPool = Executors.newCachedThreadPool();//线程池里面的线程数会动态变化
            for (final String userRedis : users) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (threadPool) {
                            if (userRedis != null && userRedis != "") {
                                log.debug("Thread Name is" + Thread.currentThread().getName() + ",userId:" + userRedis);
                                message[0] = privilegeUserRedisService.updateUserRoleRedis(privilegeUserVo.getAppId(), userRedis);
                                privilegeMenuService.updateMenuRedis(privilegeUserVo.getAppId(), userRedis);
                                privilegeUrlService.updateRedisUrl(privilegeUserVo.getAppId(), userRedis);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            message[0] = null;
            e.printStackTrace();
        }
        return message[0];
    }

}
