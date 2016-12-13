package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeResource;


/**
 * 
 */
public interface PrivilegeResourceRepository extends Repository {

	
	void savePrivilegeResource(PrivilegeResource privilegeResource);
	PrivilegeResource findByResourceId(@Param("resourceId")String resourceId,@Param("appId")String appId);
	void updatePrivilegeResource(PrivilegeResource privilegeResource);
	List<PrivilegeResource> findResourcePage(@Param("resourceId")String resourceId,@Param("appId")String appId,@Param("startRow")String startRow,@Param("pageSize")String pageSize);
	void deleteByResourceId(@Param("resourceId")String resourceId);
}