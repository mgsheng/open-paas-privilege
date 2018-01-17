package cn.com.open.opensass.privilege.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeUser;

/**
 * 
 */
public interface PrivilegeUserService {

	Integer connectionTest();

	Boolean savePrivilegeUser(PrivilegeUser privilegeUser);

	Boolean delPrivilegeUserByUid(String uId);

	Boolean delUserByAppIdAndAppUserId(String appId, String appUserId);
	
	PrivilegeUser findByAppIdAndUserId(String appId, String appUserId);

	List<PrivilegeUser> findByAppIdAndUserIds(String appId, String appUserId);

	Boolean updatePrivilegeUser(PrivilegeUser user);
	ArrayList<String> findUserResources(String appId, String appUserId); 
	List<PrivilegeUser> findUserListByPage(String appId, int start, int limit,String groupId);
	int getUserCountByAppId(String appId,String groupId);

	Boolean updatePrivilegeUserResourceId(String appUserId);

	Boolean batchUpdateResourceIds(List<PrivilegeBatchUserVo> list);

	/**
	 * 根据groupid列表更新resourceid, functionid数据
	 * @param appId
	 * @param groupId
	 * @param resourceId
	 * @param functionId
	 * @return
	 */
	Map<String, Object> batchUpdateGroupResourceFunction(String appId, String groupId, String resourceId, String functionId);

	/**
	 * 根据appId和groupId列表获取用户数据.
	 * @param appId
	 * @param groupId
	 * @return
	 */
	List<PrivilegeUser> findByGroupIdAndAppId(String appId, String[] groupId);

    List<String> findUserIdByPrivilegeRoleId(String privilegeRoleId, String appId);
}