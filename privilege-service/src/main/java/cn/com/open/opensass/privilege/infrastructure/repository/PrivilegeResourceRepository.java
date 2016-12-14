package cn.com.open.opensass.privilege.infrastructure.repository;

import cn.com.open.opensass.privilege.model.PrivilegeResource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jh on 2016/12/13.
 */
public interface PrivilegeResourceRepository extends Repository {
    
	PrivilegeResource findByResourceCode(@Param("resourceId")String resourceId,@Param("appId")String appId);
	void savePrivilegeResource(PrivilegeResource privilegeResource);
	PrivilegeResource findByResourceId(@Param("resourceId")String resourceId,@Param("appId")String appId);
	void updatePrivilegeResource(PrivilegeResource privilegeResource);
	List<PrivilegeResource> findResourcePage(@Param("resourceId")String resourceId,@Param("appId")String appId,@Param("startRow")String startRow,@Param("pageSize")String pageSize);
	void deleteByResourceId(@Param("resourceId")String resourceId);
}
