package cn.com.open.opensass.privilege.service.impl;

import cn.com.open.opensass.privilege.api.GroupBatchModifyPrivilegeController;
import cn.com.open.opensass.privilege.service.GroupBatchModifyPrivilegeService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*

 * 文件名：cn.com.open.opensass.privilege.service.impl

 * 版权： 权限服务

 * 描述： 权限服务接口，管理用户权限,批量更新缓存.

 * 修改人： LILI

 * 修改时间：2017/12/4 10:59

 * 修改内容：新增

 */

@Service("groupBatchModifyPrivilegeService")
public class GroupBatchModifyPrivilegeServiceImpl  extends BaseControllerUtil implements GroupBatchModifyPrivilegeService{
    private static final Logger log = LoggerFactory.getLogger(GroupBatchModifyPrivilegeServiceImpl.class);

    @Autowired
    PrivilegeGroupResourceService privilegeGroupResourceService;
    @Autowired
    private PrivilegeGroupService privilegeGroupService;

    @Override
    public Map<String, Object> batchUpdateUserGroupResource(String appId, String groupIds, String resourceIds, String createUser, String createUserid, String status) {

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
                            privilegeBatchUserVo.setCreateUser(createUser);
                            privilegeBatchUserVo.setCreateUserId(createUserid);
                            privilegeBatchUserVo.setGroupId(groupId);
                            privilegeBatchUserVo.setResourceIds(resourceId);
                            if (!nullEmptyBlankJudge(status)) {
                                privilegeBatchUserVo.setStatus(Integer.parseInt(status));
                            } else {
                                privilegeBatchUserVo.setStatus(0);
                            }
                            privilegeBatchUserVoList.add(privilegeBatchUserVo);
                        }
                    }
                }
            }
            if (null != privilegeBatchUserVoList && privilegeBatchUserVoList.size() > 0) {
                if (privilegeGroupResourceService.batchUpdateResourceIds(privilegeBatchUserVoList)) {
                    /*更新缓存*/
                    updateRedisCache(appId, groupIdList);
                    map.put("status", "1");
                    map.put("message", "更新成功:!");
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

        } catch (Exception e) {
            map.put("status", "0");
            map.put("error_code", "10009");
            map.put("message", "系统错误");
        }
        return map;
    }

    @Override
    public PrivilegeAjaxMessage updateRedisCache(final String appId, String[] groupIdList) {
        final PrivilegeAjaxMessage[] message = {null};
        try {
            final ExecutorService threadPool = Executors.newCachedThreadPool();//线程池里面的线程数会动态变化

            for (final String groupId : groupIdList) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (threadPool) {
                            if (groupId != null && groupId != "") {
                                //更新缓存
                                message[0] = privilegeGroupService.updateGroupPrivilegeCache(groupId, appId);
                                //更新机构版本号
                                privilegeGroupService.updateGroupVersion(groupId, appId);
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
