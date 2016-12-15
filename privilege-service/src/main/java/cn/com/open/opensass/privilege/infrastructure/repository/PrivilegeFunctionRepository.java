package cn.com.open.opensass.privilege.infrastructure.repository;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import org.apache.ibatis.annotations.Param;

/**
 * Created by jh on 2016/12/15.
 */
public interface PrivilegeFunctionRepository {
    /*根据ID获取值*/
    PrivilegeFunction findByFunctionId(@Param("functionId")String functionId);
}
