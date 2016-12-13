package cn.com.open.opensass.privilege.infrastructure.repository;

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
}