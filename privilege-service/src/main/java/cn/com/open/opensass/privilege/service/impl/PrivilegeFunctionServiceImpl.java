package cn.com.open.opensass.privilege.service.impl;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeFunctionRepository;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jh on 2016/12/15.
 */
public class PrivilegeFunctionServiceImpl implements PrivilegeFunctionService {
    @Autowired
    private PrivilegeFunctionRepository privilegeFunctionRepository;
    @Override
    public PrivilegeFunction findByFunctionId(String functionId) {
        return privilegeFunctionRepository.findByFunctionId(functionId);
    }
}
