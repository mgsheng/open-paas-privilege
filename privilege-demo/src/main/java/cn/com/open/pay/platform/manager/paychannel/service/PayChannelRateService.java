package cn.com.open.pay.platform.manager.paychannel.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
/**
 * 渠道费率管理
 * @author lvjq
 *
 */
public interface PayChannelRateService {
	
	/**
	 * 查询所有渠道编码,渠道名称
	 * @return
	 */
	public List<PayChannelDictionary> findPayChannelCodeAll();
	
	/**
	 * 添加支付渠道费率前，先根据条件查询数据库是否已经存在该记录
	 * @param rate
	 * @return
	 */
	public List<ChannelRate> findChannelRate(ChannelRate rate);
	
	/**
	 * 添加支付渠道费率
	 * @param rate
	 * @return
	 */
	public boolean addPayChannelRate(ChannelRate rate);
	
	/**
	 * 查询渠道编码
	 * @return
	 */
	public List<PayChannelDictionary> findPayChannelCode(PayChannelDictionary payChannelDictionary);
	
	/**
	 * 查询所有商户名称，商户号
	 * @return
	 */
	public List<MerchantInfo> findMerchantNamesAll();
	
	
	/**
	 * 根据id删除目标渠道费率记录
	 * @param rate
	 * @return
	 */
	public boolean removeChannelRate(ChannelRate rate);
	
	/**
	 *  根据条件，查询所有符合要求的费率情况
	 * @param rate
	 * @return
	 */
	public List<ChannelRate> findRateAll(ChannelRate rate);
	
	/**
	 *  根据条件，查询所有符合要求的费率情况的数目
	 * @param rate
	 * @return
	 */
	public int findRateAllCount(ChannelRate rate);
	
	/**
	 * 修改费率
	 * @param rate
	 * @return
	 */
	public boolean updateRate(ChannelRate rate);
	
}
