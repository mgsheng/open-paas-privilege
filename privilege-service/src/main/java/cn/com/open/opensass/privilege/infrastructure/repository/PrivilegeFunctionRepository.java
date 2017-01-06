package cn.com.open.opensass.privilege.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;

/**
 * Created by jh on 2016/12/15.
 */
public interface PrivilegeFunctionRepository {
    /*根据ID获取值*/
    PrivilegeFunction findByFunctionId(@Param("functionId")String functionId);
	void savePrivilegeFunction(PrivilegeFunction privilegeFunction);
	void updatePrivilegeFunction(PrivilegeFunction privilegeFunction);
	void deleteByFunctionId(@Param("functionId")String functionId);
	void deleteByFunctionIds(@Param("functionIds")String[] functionIds);
	List<PrivilegeFunction> findFunctionPage(@Param("functionId")String functionId,@Param("startRow")String startRow,@Param("pageSize")String pageSize);
	List<PrivilegeFunction>getFunctionByRId(@Param("resourceId")String resourceId);
	List<Map<String, Object>>getFunctionMap(@Param("resourceId")String resourceId);
	List<PrivilegeFunction> getFunctionListByUserId(@Param("appUserId")String appUserId,@Param("appId")String appId);
	List<PrivilegeFunction> getFunctionListByAppId(@Param("appId")String appId);
}
