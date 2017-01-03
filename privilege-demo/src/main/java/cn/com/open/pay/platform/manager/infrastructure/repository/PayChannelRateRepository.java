package cn.com.open.pay.platform.manager.infrastructure.repository;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;

/**
 * 渠道费率管理
 * @author lvjq
 *
 */
public interface PayChannelRateRepository extends Repository{
	
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
	public void addPayChannelRate(ChannelRate rate);
	
	/**
	 * 根据id删除目标渠道费率记录
	 * @param rate
	 * @return
	 */
	public void removeChannelRate(ChannelRate rate);
	
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
	public void updateRate(ChannelRate rate);
	
}
