package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeFunctionRepository;
import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.vo.PrivilegeFunctionVo;

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
	@Override
	public List<Map<String, Object>> getFunctionByRId(String resourceId) {
		List<PrivilegeFunction> privilegeFunctions=privilegeFunctionRepository.getFunctionByRId(resourceId);
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		for(PrivilegeFunction function:privilegeFunctions){
			if (function.id()!=null&&!("").equals(function.id())) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("resourceId", function.getResourceId());
				map.put("functionId", function.id());
				map.put("optId", function.getOperationId());
				map.put("optUrl", function.getOptUrl());
				list.add(map);
			}
		}
		return list;
	}
	@Override
	public List<Map<String, Object>> getFunctionListByUserId(String appUserId, String appId) {
		List<PrivilegeFunction> privilegeFunctions=privilegeFunctionRepository.getFunctionListByUserId(appUserId, appId);
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		for(PrivilegeFunction function:privilegeFunctions){
			if (function.id()!=null&&!("").equals(function.id())) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("resourceId", function.getResourceId());
				map.put("functionId", function.id());
				map.put("optId", function.getOperationId());
				map.put("optUrl", function.getOptUrl());
				list.add(map);
			}
		}
		
		return list;
	}
	@Override
	public List<Map<String, Object>> getFunctionMap(String resourceId) {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		List<PrivilegeFunction> functions=privilegeFunctionRepository.getFunctionMap(resourceId);
		for(PrivilegeFunction function:functions){
			if (function.id()!=null&&!("").equals(function.id())) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("resourceId", function.getResourceId());
				map.put("functionId", function.id());
				map.put("optId", function.getOperationId());
				map.put("optUrl", function.getOptUrl());
				list.add(map);
			}
		}
		return list;
	}
	@Override
	public List<PrivilegeFunctionVo> getFunctionListByAppId(String appId) {
		List<PrivilegeFunctionVo> privilegeFunctionVos=new ArrayList<PrivilegeFunctionVo>();
		List<PrivilegeFunction> privilegeFunctions=privilegeFunctionRepository.getFunctionListByAppId(appId);
		for(PrivilegeFunction function:privilegeFunctions){
			if (function.id()!=null&&!("").equals(function.id())) {
				PrivilegeFunctionVo functionVo=new PrivilegeFunctionVo();
				functionVo.setFunctionId(function.id());
				functionVo.setOptId(function.getOperationId());
				functionVo.setOptUrl(function.getOptUrl());
				functionVo.setResourceId(function.getResourceId());
				privilegeFunctionVos.add(functionVo);
			}
		}
		return privilegeFunctionVos;
	}
	@Override
	public List<Map<String, Object>> getFunctionListByFunctionIds(String[] functionIds) {
		List<Map<String, Object>> privilegefunctions=new ArrayList<Map<String, Object>>();
		List<PrivilegeFunction> functions= privilegeFunctionRepository.getFunctionListByFunctionIds(functionIds);
		for(PrivilegeFunction function:functions){
			if (function.id()!=null&&!("").equals(function.id())) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("resourceId", function.getResourceId());
				map.put("functionId", function.id());
				map.put("optId", function.getOperationId());
				map.put("optUrl", function.getOptUrl());
				privilegefunctions.add(map);
			}
		}
		
		return privilegefunctions;
	}
	@Override
	public List<String> findAppFunction(String appId) {
		return privilegeFunctionRepository.findAppFunction(appId);
	}
	
	
}
