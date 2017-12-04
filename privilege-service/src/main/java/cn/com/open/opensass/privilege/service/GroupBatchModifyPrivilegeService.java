package cn.com.open.opensass.privilege.service;

/*

 * 文件名：cn.com.open.opensass.privilege.service

 * 版权： 权限服务

 * 描述： 权限服务接口，管理用户权限,批量更新缓存.

 * 修改人： LILI

 * 修改时间：2017/12/4 10:57

 * 修改内容：新增

 */

import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

import java.util.Map;

public interface GroupBatchModifyPrivilegeService {
    /**
     * 批量更新资源，分组权限数据.
     * @param appId
     * @param groupIds
     * @param resourceIds
     * @param createUser
     * @param createUserid
     * @param status
     * @return
     */
    public Map<String, Object> batchUpdateUserGroupResource(String appId, String groupIds, String resourceIds, String createUser, String createUserid, String status);

    /**
     * 更新缓存.
     * @param appId
     * @param groupIdList
     * @return
     */
    PrivilegeAjaxMessage updateRedisCache(final String appId, final String[] groupIdList);
}
