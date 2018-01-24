package cn.com.open.opensass.privilege.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.vo.PrivilegeFunctionVo;
import cn.com.open.opensass.privilege.vo.PrivilegeFunctionsVo;

/**
 * Created by jh on 2016/12/15.
 */
public interface PrivilegeFunctionService {
    /*根据ID获取值*/
    PrivilegeFunction findByFunctionId(String functionId,String appId);
	Boolean savePrivilegeFunction(PrivilegeFunction privilegeFunction);
	List<PrivilegeFunction>findFunctionPage(String functionId,String startRow,String pageSize);
	Boolean updatePrivilegeFunction(PrivilegeFunction privilegeFunction);
	Boolean deleteByFunctionId(String  functionId);
	Boolean deleteByFunctionIds(String []  functionIds);
	/**
	 * 获取功能根据resourceId
	 * @param functionId
	 * @param startRow
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>>getFunctionByRId(String resourceId,String appId);
	
	List<Map<String, Object>>getFunctionMap(String resourceId,String appId);
	List<Map<String, Object>> getFunctionListByUserId(String appUserId,String appId);
	List<PrivilegeFunctionVo> getFunctionListByAppId(String appId);
	List<Map<String, Object>> getFunctionListByFunctionIds(String[] functionIds,String appId); 
	List<String> findAppFunction(String appId);

	List<PrivilegeFunctionsVo> getFunctionListByAppIds(String appId);
	List<Map<String, Object>> getFunctionByRIds(String resourceId, String appId);
	List<Map<String, Object>> getFunctionMaps(String resourceId, String appId);
}
