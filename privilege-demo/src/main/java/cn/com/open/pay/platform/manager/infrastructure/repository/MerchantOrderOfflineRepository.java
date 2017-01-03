package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface MerchantOrderOfflineRepository extends Repository {

	List<MerchantOrderOffline> findAllNoPage(MerchantOrderOffline offline);
	
	int findQueryCount(MerchantOrderOffline merchantOrderOffline);

	void addOrderOffline(MerchantOrderOffline merchantOrderOffline);

	MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId);

	List<MerchantOrderOffline> findAllByPage(MerchantOrderOffline offline);
	
}