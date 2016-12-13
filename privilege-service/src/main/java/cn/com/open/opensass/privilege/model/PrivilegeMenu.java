package cn.com.open.opensass.privilege.model;

import java.util.Date;

public class PrivilegeMenu extends AbstractDomain {
	/**
	 * 菜单
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int appId;
	private String menuName;
	private String menuCode;
	private String menuRule;
	private int menuLevel;
	private int parentId;
	private int dislayOrder;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private int resourceType;
	private int status;
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuRule() {
		return menuRule;
	}
	public void setMenuRule(String menuRule) {
		this.menuRule = menuRule;
	}
	public int getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
