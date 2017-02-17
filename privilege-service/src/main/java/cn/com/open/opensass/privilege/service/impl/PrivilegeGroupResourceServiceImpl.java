package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 
 */
@Service("privilegeGroupResourceService")
public class PrivilegeGroupResourceServiceImpl implements PrivilegeGroupResourceService {

	@Autowired
	private PrivilegeGroupResourceRepository privilegeGroupResourceRepository;
	
	@Override
	public PrivilegeGroupResource getPrivilegeGroupResource(String groupId,
			String resourceId, String appId) {
			 		
			return privilegeGroupResourceRepository.findByGroupIdAndResourceId(groupId, resourceId, appId);			
		}
	

	@Override
	public Boolean saveprivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource) {
		try {

			privilegeGroupResourceRepository.saveprivilegeGroupResource(privilegeGroupResource);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	@Override
	public Boolean deleteResource(String groupId, String resourceId) {
		try {
			privilegeGroupResourceRepository.deleteResource(groupId, resourceId);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public List<PrivilegeGroupResource> getPgrs(String groupId) {
		// TODO Auto-generated method stub
		return privilegeGroupResourceRepository.findGprs(groupId);
	}

	@Override
	public Boolean deleteByGroupId(String groupId) {
		try {
			privilegeGroupResourceRepository.deleteByGroup(groupId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	@Override
	public Boolean updatePrivilegeGroupResource(PrivilegeGroupResource privilegeGroupResource) {
		try {
			privilegeGroupResourceRepository.updatePrivilegeGroupResource(privilegeGroupResource);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	//根据组ID查询对应的所有资源ID
	@Override
	public List<String> findResourceIdsByGroupId(String groupId) {
		List<String> list = privilegeGroupResourceRepository.findResourceIdsByGroupId(groupId);
		return list;
	}


	@Override
	public List<PrivilegeGroupResource> getPgrs(String groupId,
			String startRow, String pageSize) {
		// TODO Auto-generated method stub
		return privilegeGroupResourceRepository.findGprPage(groupId,Integer.parseInt(startRow), Integer.parseInt(pageSize));
	}

}