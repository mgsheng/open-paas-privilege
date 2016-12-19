package cn.com.open.opensass.privilege.service;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;

/**
 * Created by jh on 2016/12/15.
 */
public interface PrivilegeFunctionService {
    /*根据ID获取值*/
    PrivilegeFunction findByFunctionId(String functionId);
	Boolean savePrivilegeFunction(PrivilegeFunction privilegeFunction);
	List<PrivilegeFunction>findFunctionPage(String functionId,String startRow,String pageSize);
	Boolean updatePrivilegeFunction(PrivilegeFunction privilegeFunction);
	Boolean deleteByFunctionId(String  functionId);
	Boolean deleteByFunctionIds(String []  functionIds);
}
