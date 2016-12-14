package cn.com.open.opensass.privilege.model;

import java.util.Date;

public class PrivilegeResource extends AbstractDomain {
	/**
	 * 资源
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String appId;
	private String menuId;
	private String resourceName;
	private String resourceRule;
	private int resourceLevel;
	private String baseUrl;
	private int dislayOrder;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private int status;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceRule() {
		return resourceRule;
	}
	public void setResourceRule(String resourceRule) {
		this.resourceRule = resourceRule;
	}
	public int getResourceLevel() {
		return resourceLevel;
	}
	public void setResourceLevel(int resourceLevel) {
		this.resourceLevel = resourceLevel;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public int getDislayOrder() {
		return dislayOrder;
	}
	public void setDislayOrder(int dislayOrder) {
		this.dislayOrder = dislayOrder;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
