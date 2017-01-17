package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeOperationRepository;
import cn.com.open.opensass.privilege.model.PrivilegeOperation;
import cn.com.open.opensass.privilege.service.PrivilegeOperationService;



/**
 * 
 */
@Service("privilegeOperationService")
public class PrivilegeOperationServiceImpl implements PrivilegeOperationService {

    @Autowired
    private PrivilegeOperationRepository privilegeOperationRepository;
    @Override
	public PrivilegeOperation findById(String optId) {
		PrivilegeOperation opt=privilegeOperationRepository.findById(Integer.parseInt(optId));
        if(opt==null){
        	return null;
        }else{
        	return opt;
        }
    }
	@Override
	public List<PrivilegeOperation> findAllOper() {
		return privilegeOperationRepository.findAllOper();
	}
    
}