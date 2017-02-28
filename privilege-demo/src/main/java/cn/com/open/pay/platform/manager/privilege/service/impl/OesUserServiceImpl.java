package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.OesUserRepository;
import cn.com.open.pay.platform.manager.privilege.model.OesUser;
import cn.com.open.pay.platform.manager.privilege.service.OesUserService;
@Service("oesUserService")
public class OesUserServiceImpl implements OesUserService {
	@Autowired
	private OesUserRepository oesUserRepository;

	@Override
	public List<Map<String, Object>> getUserListByPage(String groupId, int start, int limit,String userName) {
		return oesUserRepository.getUserListByPage(groupId, start, limit,userName);
	}

	@Override
	public int getUserCount(String groupId,String userName) {
		return oesUserRepository.getUserCount(groupId,userName);
	}

	@Override
	public Boolean saveUser(OesUser oesUser) {
		try {
			oesUserRepository.saveUser(oesUser);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean deleteUser(Integer Id) {
		try {
			oesUserRepository.deleteUser(Id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

}
