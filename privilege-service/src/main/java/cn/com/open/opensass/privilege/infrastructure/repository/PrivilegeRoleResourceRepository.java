package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;



/**
 * 
 */
public interface PrivilegeRoleResourceRepository extends Repository {

	void savePrivilegeRoleResource(PrivilegeRoleResource privilegeRoleResource);

	List<PrivilegeRoleResource> findByPrivilegeRoleId(String privilegeRoleId);

	void delPrivilegeRoleResource(PrivilegeRoleResource roleResource);

	void delRoleResourceByRoleId(String privilegeRoleId);

	PrivilegeRoleResource findByRoleIdAndResourceId(String privilegeRoleId,String resourceId);

	ArrayList<String> findUserResourcesFunId(@Param("appId") String appId, @Param("appUserId") String appUserId);
}