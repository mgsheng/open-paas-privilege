package cn.com.open.pay.platform.manager.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class MerchantOrderOffline implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
    
	private String merchantOrderId;
	
	private Integer merchantId;
		
	private Date payTime;
	
	private Double money;
	
	private String appId;
	
	private String remark;
	
	private Date createTime = new Date();
	
	private String sourceUid;
	
	private String sourceUserName;
	
	private String realName;
	
	private String phone;
	
	private String channelId;
	
	private String bankCode;
	
	private String operator;
	
	private Integer startRow;
	private Integer pageSize;
	
	
	private String foundDate; //创建时间
	private String appName;
	private String channelName;
	private String merchantName;
	private String bankName;
	
	private Date startDate;//查询缴费日期开始
	private Date endDate;//查询缴费日期结束

	public MerchantOrderOffline(){
		
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}


	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getSourceUid() {
		return sourceUid;
	}

	public void setSourceUid(String sourceUid) {
		this.sourceUid = sourceUid;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}