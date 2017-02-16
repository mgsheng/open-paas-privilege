package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.OesUserRepository;
import cn.com.open.pay.platform.manager.privilege.service.OesUserService;
@Service("oesUserService")
public class OesUserServiceImpl implements OesUserService {
	@Autowired
	private OesUserRepository oesUserRepository;

	@Override
	public List<Map<String, Object>> getUserListByPage(String groupId, int start, int limit) {
		return oesUserRepository.getUserListByPage(groupId, start, limit);
	}

	@Override
	public int getUserCountByGroupId(String groupId) {
		return oesUserRepository.getUserCountByGroupId(groupId);
	}
	

}