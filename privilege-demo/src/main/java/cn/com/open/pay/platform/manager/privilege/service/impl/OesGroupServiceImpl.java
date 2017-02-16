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
		

}