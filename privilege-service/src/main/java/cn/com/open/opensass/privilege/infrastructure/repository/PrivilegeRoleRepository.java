package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRole;

/**
 * 
 */
public interface PrivilegeRoleRepository extends Repository {

	void savePrivilegeRole(PrivilegeRole privilegeRole);

	PrivilegeRole findByRoleId(String privilegeRoleId);

	void delPrivilegeRoleById(String privilegeRoleId);

	List<Map<String, Object>> getRoleListByUserId(@Param("appUserId") String appUserId, @Param("appId") String appId);

	void updatePrivilegeRole(PrivilegeRole privilegeRole);

	List<String> findRoleByAppId(String appId);

	List<PrivilegeRole> findRoleByPage(@Param("privilegeRoleId") String privilegeRoleId, @Param("appId") String appId, @Param("deptId")String deptId,  @Param("groupId") String groupId,
			@Param("start") int start, @Param("limit") int limit);

	List<PrivilegeRole> findRoleNoPage(@Param("privilegeRoleId") String privilegeRoleId, @Param("appId") String appId, @Param("deptId")String deptId,  @Param("groupId") String groupId);

	List<PrivilegeRole> getRoleListByAppId(@Param("appId") String appId);

	List<PrivilegeRole> getRoleListByUserIdAndAppId(@Param("appUserId") String appUserId, @Param("appId") String appId);

	List<PrivilegeRole> getRoleListByAppIdAndGroupId(@Param("appId") String appId, @Param("groupId") String groupId,@Param("roleName") String roleName,@Param("roleType") Integer roleType,@Param("start") int start,@Param("limit") int limit);

	int getRoleCountByAppIdAndGroupId(@Param("appId") String appId, @Param("groupId") String groupId,@Param("roleType") Integer roleType,@Param("roleName") String roleName);
}