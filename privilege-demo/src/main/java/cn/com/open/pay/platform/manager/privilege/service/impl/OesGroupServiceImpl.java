package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.OesGroupRepository;
import cn.com.open.pay.platform.manager.privilege.model.OesGroup;
import cn.com.open.pay.platform.manager.privilege.service.OesGroupService;

@Service("OesGroupService")
public class OesGroupServiceImpl implements OesGroupService {
   
	@Autowired
	private OesGroupRepository oesGroupRepository;

	@Override
	public List<OesGroup> findAllByPage(int startRow, int pageSize) {
		List<OesGroup> groupList=oesGroupRepository.findAllByPage(startRow,pageSize);
		return groupList;
	}

	@Override
	public int findAllCount() {
		int count=oesGroupRepository.findAllCount();
		return count;
	}

	@Override
	public OesGroup findByCode(String groupCode) {
		OesGroup group=oesGroupRepository.findByCode(groupCode);
		return group;
	}

	@Override
	public Boolean saveGroup(OesGroup g) {
		try{
			oesGroupRepository.saveGroup(g);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<OesGroup> findAll() {
		return oesGroupRepository.findAll();
	}

	@Override
	public List<OesGroup> findByPage(String groupName, String groupCode, int startRow, int pageSize) {
		return oesGroupRepository.findByPage(groupName,groupCode,startRow,pageSize);
	}

	@Override
	public int findCount(String groupName, String groupCode) {
		int count=oesGroupRepository.findCount(groupName,groupCode);
		return count;
	}

	@Override
	public OesGroup findByTypeNameAndCode(String typeName, String groupCode) {
		return oesGroupRepository.findByTypeNameAndCode(typeName, groupCode);
	}

	@Override
	public List<OesGroup> findByTypeName(String typeName) {
		return oesGroupRepository.findByTypeName(typeName);
	}
		

}