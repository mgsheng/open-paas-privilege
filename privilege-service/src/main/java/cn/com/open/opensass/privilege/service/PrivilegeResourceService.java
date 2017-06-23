package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;

/**
 * 
 */
public interface PrivilegeResourceService {
	Boolean savePrivilegeResource(PrivilegeResource privilegeResource);
	PrivilegeResource findByResourceId(String resourceId,String appId);
	PrivilegeResourceVo findByResource_Id(String resourceId,String appId);
	List<PrivilegeResource> findByResourceIds(String resourceId[],String appId);
	PrivilegeResource findByResourceCode(String resourceId,String appId);
	List<PrivilegeResource>findResourcePage(String menuId,String appId,int startRow,int pageSize,String resourceLevel);
	Boolean updatePrivilegeResource(PrivilegeResource privilegeResource);
	Boolean deleteByResourceId(String[]  resourceIds,String appId);
	List<Map<String, Object>> getResourceListByUserId(String appUserId,String appId);
	//PrivilegeResource findResourceByAppUserId(String userId);
	List<PrivilegeResourceVo> findByGroupIdAndAppId(String groupId,String appId);
	List<PrivilegeResource> findResourceLists(String groupIds[]);
	List<Map<String, Object>> findResourceMap(String groupIds[]);
	List<PrivilegeResourceVo> getResourceListByAppId(String appId);
	PrivilegeAjaxMessage getAppResRedis(String appId);
	PrivilegeAjaxMessage delAppResRedis(String appId);
	PrivilegeAjaxMessage updateAppResRedis(String appId);
	List<Map<String, Object>> getResourceListByFunIds(String[] functionIds,String appId);
	PrivilegeResourceVo getResourceListByMenuId(String menuId);
	List<String> findAppResources(String appId);
	PrivilegeResource getResourceListByFunId(String functionId,String appId);
}