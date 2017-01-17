package cn.com.open.opensass.privilege.infrastructure.repository;

import cn.com.open.opensass.privilege.model.PrivilegeResource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jh on 2016/12/13.
 */
public interface PrivilegeResourceRepository extends Repository {
    
	PrivilegeResource findByResourceCode(@Param("resourceId")String resourceId,@Param("appId")String appId);
	void savePrivilegeResource(PrivilegeResource privilegeResource);
	PrivilegeResource findByResourceId(@Param("resourceId")String resourceId,@Param("appId")String appId);
	PrivilegeResource findByResource_Id(@Param("resourceId")String resourceId);
	void updatePrivilegeResource(PrivilegeResource privilegeResource);
	List<PrivilegeResource> findResourcePage(@Param("menuId")String menuId,@Param("appId")String appId,@Param("startRow")int startRow,@Param("pageSize")int pageSize,@Param("resourceLevel")String resourceLevel);
	void deleteByResourceId(@Param("resourceIds")String[] resourceIds);
	List<PrivilegeResource> findByGroupIdAndAppId(@Param("groupId")String groupId,@Param("appId")String appId);
	List<PrivilegeResource> getResourceListByUserId(@Param("appUserId")String appUserId,@Param("appId")String appId);
	List<PrivilegeResource> findResourceList(@Param("resourceIds")String [] resourceIds);
	List<Map<String, Object>> findResourceMap(@Param("resourceIds")String [] resourceIds);
	List<PrivilegeResource> getResourceListByAppId(@Param("appId")String appId);
	List<PrivilegeResource> getResourceListByFunIds(@Param("functionIds")String[] functionIds);
	List<PrivilegeResource> getResourceListByMenuId(@Param("menuId")String menuId);
	List<String> findAppResources(String appId);
}
