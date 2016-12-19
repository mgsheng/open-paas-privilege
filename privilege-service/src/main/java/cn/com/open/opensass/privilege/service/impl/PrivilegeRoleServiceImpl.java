package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;

/**
 * 
 */
@Service("PrivilegeRoleService")
public class PrivilegeRoleServiceImpl implements PrivilegeRoleService {

    @Autowired
    private PrivilegeRoleRepository privilegeRoleRepository;

	@Override
	public Boolean savePrivilegeRole(PrivilegeRole privilegeRole) {
		try{
			privilegeRoleRepository.savePrivilegeRole(privilegeRole);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PrivilegeRole findRoleById(String pararentRoleId) {
		return privilegeRoleRepository.findByRoleId(pararentRoleId);
	}

	@Override
	public Boolean delPrivilegeRoleById(String privilegeRoleId) {
		try{
			privilegeRoleRepository.delPrivilegeRoleById(privilegeRoleId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean updatePrivilegeRole(PrivilegeRole privilegeRole) {
		try{
			privilegeRoleRepository.updatePrivilegeRole(privilegeRole);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> findRoleByAppId(String appId) {
		return privilegeRoleRepository.findRoleByAppId(appId);
	}

	@Override
	public List<PrivilegeRole> findRoleByPage(String privilegeRoleId,String appId, int start, int limit) {
		return privilegeRoleRepository.findRoleByPage(privilegeRoleId,appId,start,limit);
	}

	@Override
	public int findRoleNoPage(String privilegeRoleId, String appId) {
		List<PrivilegeRole> list = privilegeRoleRepository.findRoleNoPage(privilegeRoleId,appId);
		return list.size();
	}

}