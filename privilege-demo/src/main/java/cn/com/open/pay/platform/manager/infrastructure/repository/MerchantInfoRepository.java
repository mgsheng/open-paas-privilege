package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface MerchantInfoRepository extends Repository {
	
	/**
	 * 查询所有商户名称，商户号
	 * @return
	 */
	public List<MerchantInfo> findMerchantNamesAll();
	
	/**
	 *查询list
	 * @return
	 */
	public List<MerchantInfo> findDepts(MerchantInfo department);

	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(MerchantInfo department);
	
	/**
	 * 根据商户id 删除
	 * @return
	 */
	public void removeCommercialID(Integer id);
	/**
	 * 添加
	 * @return
	 */
	public void insert(MerchantInfo merchantInfo);
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	public void updateMerchantInfo(MerchantInfo merchantInfo);

	public MerchantInfo findNameById(Integer merchantId);
	
	
	
}
