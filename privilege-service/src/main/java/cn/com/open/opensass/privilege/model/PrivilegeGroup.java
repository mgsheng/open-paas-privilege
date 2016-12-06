package cn.com.open.opensass.privilege.model;

import java.util.Date;

public class PrivilegeGroup {
	private String groupId;
	private String groupName;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private String appId;
	private int status;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public String getcreateUserId() {
		return createUserId;
	}
	public void setcreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
	


}
