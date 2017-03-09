package cn.com.open.opensass.privilege.vo;

public class PrivilegeResourceVo {
	private String resourceId;
	private String resourceName;
	private String resourceRule;
	private String appId;
	private int status;
	private String resourceLevel;
	private String menuId;
	private int displayOrder;
	private String baseUrl;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PrivilegeResourceVo)) {
			return false;
		}
		PrivilegeResourceVo privilegeResourceVo = (PrivilegeResourceVo) obj;
		return resourceId.equals(privilegeResourceVo.getResourceId());
	}

	@Override
	public int hashCode() {
		return resourceId.hashCode();
	}
}
