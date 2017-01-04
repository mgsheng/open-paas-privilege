package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeGroupRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeGroup;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGroupService;

import net.sf.json.JSONObject;

/**
 * 
 */
@Service("privilegeGroupService")
public class PrivilegeGroupServiceImpl implements PrivilegeGroupService {

	@Autowired
	private PrivilegeGroupRepository privilegeGroupRepository;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeGroupServiceImpl.class);

	@Override
	public Boolean savePrivilegeGroup(PrivilegeGroup privilegeGroup) {
		try {
			privilegeGroupRepository.savePrivilegeGroup(privilegeGroup);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	@Override
	public PrivilegeGroup findBygroupId(String groupId, String appId) {
		// TODO Auto-generated method stub
		return privilegeGroupRepository.findByGroupId(groupId, appId);
	}

	@Override
	public List<PrivilegeGroup> findGroupPage(String groupId, String appId, String startRow, String pageSize) {
		// TODO Auto-generated method stub
		List<PrivilegeGroup> groupPage = privilegeGroupRepository.findGroupPage(groupId, appId,
				Integer.parseInt(startRow), Integer.parseInt(pageSize));

		return groupPage;
	}


}