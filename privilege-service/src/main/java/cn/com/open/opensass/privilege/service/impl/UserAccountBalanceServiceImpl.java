package cn.com.open.opensass.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.UserAccountBalanceRepository;
import cn.com.open.opensass.privilege.service.UserAccountBalanceService;
import cn.com.open.user.platform.manager.user.model.UserAccountBalance;

/**
 * 
 */
@Service("userAccountBalanceService")
public class UserAccountBalanceServiceImpl implements UserAccountBalanceService {

    @Autowired
    private UserAccountBalanceRepository userAccountBalanceRepository;

	@Override
	public void saveUserAccountBalance(UserAccountBalance userAccountBalance) {
		userAccountBalanceRepository.saveUserAccountBalance(userAccountBalance);
		
	}

	@Override
	public UserAccountBalance findByUserId(String userId) {
		// TODO Auto-generated method stub
		return userAccountBalanceRepository.findByUserId(userId);
	}

	@Override
	public Boolean updateBalanceInfo(UserAccountBalance userAccountBalance) {
		try{
			userAccountBalanceRepository.updateBalanceInfo(userAccountBalance);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	@Override
	public UserAccountBalance getBalanceInfo(String sourceId, Integer appId) {
		// TODO Auto-generated method stub
		return userAccountBalanceRepository.getBalanceInfo(sourceId, appId);
	}


}