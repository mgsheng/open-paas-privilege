package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeFunctionRepository;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jh on 2016/12/15.
 */
@Service("privilegeFunctionService")
public class PrivilegeFunctionServiceImpl implements PrivilegeFunctionService {
    @Autowired
    private PrivilegeFunctionRepository privilegeFunctionRepository;
    @Override
    public PrivilegeFunction findByFunctionId(String functionId) {
        return privilegeFunctionRepository.findByFunctionId(functionId);
    }
	@Override
	public Boolean savePrivilegeFunction(PrivilegeFunction privilegeFunction) {
		// TODO Auto-generated method stub
		try {
			privilegeFunctionRepository.savePrivilegeFunction(privilegeFunction);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public List<PrivilegeFunction> findFunctionPage(String functionId,
			String startRow, String pageSize) {
		// TODO Auto-generated method stub
		return privilegeFunctionRepository.findFunctionPage(functionId, startRow, pageSize);
	}
	@Override
	public Boolean updatePrivilegeFunction(PrivilegeFunction privilegeFunction) {
		// TODO Auto-generated method stub
		try {
			privilegeFunctionRepository.updatePrivilegeFunction(privilegeFunction);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public Boolean deleteByFunctionId(String functionId) {
		// TODO Auto-generated method stub
		try {
			privilegeFunctionRepository.deleteByFunctionId(functionId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public Boolean deleteByFunctionIds(String[] functionIds) {
		// TODO Auto-generated method stub
		try {
			privilegeFunctionRepository.deleteByFunctionIds(functionIds);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	
}
