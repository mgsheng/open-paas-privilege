package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeUser;


/**
 * 
 */
public interface PrivilegeUserRepository extends Repository {

	void savePrivilegeUser(PrivilegeUser privilegeUser);

	void delPrivilegeUserByUid(String appUserId);

	PrivilegeUser findByAppIdAndAppUserId(@Param("appId") String appId, @Param("appUserId") String appUserId);

	void delUserByAppIdAndAppUserId(@Param("appId") String appId, @Param("appUserId") String appUserId);

	void updatePrivilegeUser(PrivilegeUser user);
	ArrayList<String> findUserResources(@Param("appId") String appId, @Param("appUserId") String appUserId);
	List<PrivilegeUser> findUserListByPage(@Param("appId")String appId,@Param("start") int start,@Param("limit") int limit,@Param("groupId")String groupId);
	int getUserCountByAppId(@Param("appId")String appId,@Param("groupId")String groupId);
}