package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
public interface PayChannelSwitchRepository  extends Repository{
	
	
	
	/**
	 * 查询所有渠道编码，渠道名称
	 * @return
	 */
	public List<PayChannelSwitch> findPayChannelTypeAll();

	public PayChannelSwitch findNameById(String channelId);
	
	
}
