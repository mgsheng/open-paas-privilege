package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;


/**
 * 
 */
public interface PrivilegeGroupResourceRepository extends Repository {

	
	void saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource findByGroupIdAndResourceId(@Param("groupId")String groupId,@Param("resourceId")String resourceId);
	List<PrivilegeGroupResource> findGprs(@Param("groupId")String groupId);
	void deleteResource(@Param("groupId")String groupId ,@Param("resourceId")String resourceId);
	void deleteByGroup(@Param("groupId")String groupId);
	void updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
}