package cn.com.open.opensass.privilege.service;
import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeOperation;



/**
 * 
 */
public interface PrivilegeOperationService {

	PrivilegeOperation findById(String optId);	
	List<PrivilegeOperation> findAllOper();
}