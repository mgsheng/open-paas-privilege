package cn.com.open.opensass.privilege.vo;

public class PrivilegeGroupVo {
	private String groupId;
	private String groupPrivilege;
	private String appId;
	private int status;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupPrivilege() {
		return groupPrivilege;
	}
	public void setGroupPrivilege(String groupPrivilege) {
		this.groupPrivilege = groupPrivilege;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
