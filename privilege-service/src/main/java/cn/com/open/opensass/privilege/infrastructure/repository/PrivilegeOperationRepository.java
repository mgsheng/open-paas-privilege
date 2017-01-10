package cn.com.open.opensass.privilege.infrastructure.repository;

import cn.com.open.opensass.privilege.model.PrivilegeOperation;
import cn.com.open.opensass.privilege.model.PrivilegeResource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jh on 2016/12/13.
 */
public interface PrivilegeOperationRepository extends Repository {
    
	PrivilegeOperation findById(Integer id);
}
