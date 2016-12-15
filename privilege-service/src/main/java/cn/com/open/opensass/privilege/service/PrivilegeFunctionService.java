package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import org.apache.ibatis.annotations.Param;

/**
 * Created by jh on 2016/12/15.
 */
public interface PrivilegeFunctionService {
    /*根据ID获取值*/
    PrivilegeFunction findByFunctionId(String functionId);
}
