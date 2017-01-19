package cn.com.open.opensass.privilege.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeOperationRepository;
import cn.com.open.opensass.privilege.model.PrivilegeOperation;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeOperationService;



/**
 * 
 */
@Service("privilegeOperationService")
public class PrivilegeOperationServiceImpl implements PrivilegeOperationService {
	@Autowired
	private RedisClientTemplate redisClientTemplate;
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
		List<PrivilegeOperation> operations=(List<PrivilegeOperation>) redisClientTemplate.getObject("operation");
		if(operations==null){
			operations=privilegeOperationRepository.findAllOper();
			redisClientTemplate.setObject("operation", operations);
		}
		return operations;
	}
    
}