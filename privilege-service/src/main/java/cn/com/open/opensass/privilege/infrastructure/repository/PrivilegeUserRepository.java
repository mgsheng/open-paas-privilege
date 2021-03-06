package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeUser;


/**
 * 
 */
public interface PrivilegeUserRepository extends Repository {

	Integer connectionTest();

	void savePrivilegeUser(PrivilegeUser privilegeUser);

	void delPrivilegeUserByUid(String appUserId);

	void updatePrivilegeUserResourceId(String appUserId);

	//根据groupId清空resourceid functionid
	void updatePrivilegeUserResourceIdByGroupId(@Param("groupId") String groupId, @Param("appId") String appId);


	PrivilegeUser findByAppIdAndAppUserId(@Param("appId") String appId, @Param("appUserId") String appUserId);

	List<PrivilegeUser> findByAppIdAndAppUserIds(@Param("appId") String appId, @Param("appUserId") String appUserId);

	List<PrivilegeUser> findByGroupIdAndAppId(@Param("appId") String appId, @Param("groupIds") String[] groupIds);

	void delUserByAppIdAndAppUserId(@Param("appId") String appId, @Param("appUserId") String appUserId);

	void updatePrivilegeUser(PrivilegeUser user);
	ArrayList<String> findUserResources(@Param("appId") String appId, @Param("appUserId") String appUserId);
	List<PrivilegeUser> findUserListByPage(@Param("appId")String appId,@Param("start") int start,@Param("limit") int limit,@Param("groupId")String groupId);
	int getUserCountByAppId(@Param("appId")String appId,@Param("groupId")String groupId);

	void batchUpdateResourceIds(@Param("list")List<PrivilegeBatchUserVo> list);

    List<String> findUserIdByPrivilegeRoleId(@Param("privilegeRoleId") String privilegeRoleId, @Param("appId") String appId);
}