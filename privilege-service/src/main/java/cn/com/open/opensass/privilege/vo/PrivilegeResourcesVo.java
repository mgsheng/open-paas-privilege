package cn.com.open.opensass.privilege.vo;

public class PrivilegeResourcesVo {
	private String resourceId;
	private String resourceName;
	private int status;
	private String menuId;

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
 

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
 
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PrivilegeResourcesVo)) {
			return false;
		}
		PrivilegeResourcesVo privilegeResourceVo = (PrivilegeResourcesVo) obj;
		return resourceId.equals(privilegeResourceVo.getResourceId());
	}

	@Override
	public int hashCode() {
		return resourceId.hashCode();
	}
}
