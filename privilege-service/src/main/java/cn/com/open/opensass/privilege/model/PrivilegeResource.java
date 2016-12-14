package cn.com.open.opensass.privilege.model;

import java.util.Date;

public class PrivilegeResource extends AbstractDomain {
	/**
	 * 资源
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <result column="menu_id" property="menuId" jdbcType="VARCHAR" />
		<result column="resource_name" property="resourceName" jdbcType="VARCHAR" />
		<result column="app_id" property="appId" jdbcType="INTEGER" />
		<result column="create_time" property="createTime" jdbcType="TIMESTAMP" />
		<result column="create_user" property="createUser" jdbcType="VARCHAR" />
		<result column="create_userid" property="createUserId" jdbcType="VARCHAR" />
		<result column="status" property="status" jdbcType="INTEGER" />
		<result column="resource_rule" property="resourcerule" jdbcType="VARCHAR" />
		<result column="resource_level" property="resourceLevel" jdbcType="INTEGER" />
		<result column="display_order" property="displayOrder" jdbcType="INTEGER" />
		<result column="base_url" property="baseUrl" jdbcType="VARCHAR" />
	 */
	private String appId;
	private String menuId;
	private String resourceName;
	private String resourceRule;
	private int resourceLevel;
	private String baseUrl;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private int status;
	private String resourcerule;
	private int displayOrder;
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getResourcerule() {
		return resourcerule;
	}
	public void setResourcerule(String resourcerule) {
		this.resourcerule = resourcerule;
	}
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
