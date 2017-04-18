package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;


/**
 * 
 */
public interface PrivilegeGroupResourceRepository extends Repository {

	List<PrivilegeGroupResource> findByGroupIdAndAppId(@Param("groupId")String groupId,@Param("appId")String appId);
	void saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	PrivilegeGroupResource findByGroupIdAndResourceId(@Param("groupId")String groupId,@Param("resourceId")String resourceId,@Param("appId")String appId);
	List<PrivilegeGroupResource> findGprs(@Param("groupId")String groupId, @Param("appId") String appId);
	void deleteResource(@Param("groupId")String groupId ,@Param("resourceId")String resourceId ,@Param("appId")String appId);
	void deleteByGroup(@Param("groupId")String groupId,@Param("appId")String appId);
	void updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource);
	List<String> findResourceIdsByGroupId(@Param("groupId")String groupId);
	List<PrivilegeGroupResource> findGprPage(@Param("groupId")String groupId,@Param("appId")String appId,@Param("startRow")int startRow,@Param("pageSize")int pageSize);
}