package cn.com.open.opensass.privilege.model;

import java.util.Date;
import java.util.UUID;

public class PrivilegeUser {
	private String uId=UUID.randomUUID().toString().replace("-", "");
	private String appId;
	private String privilegeRoleId;
	private String appUserid;
	private String appUsername;
	private String deptId;
	private String groupId;
	private String privilegeFunId;
	private String resourceId;
	private Date createTime=new Date();
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppUserid() {
		return appUserid;
	}
	public void setAppUserid(String appUserid) {
		this.appUserid = appUserid;
	}
	public String getAppUsername() {
		return appUsername;
	}
	public void setAppUsername(String appUsername) {
		this.appUsername = appUsername;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getPrivilegeFunId() {
		return privilegeFunId;
	}
	public void setPrivilegeFunId(String privilegeFunId) {
		this.privilegeFunId = privilegeFunId;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPrivilegeRoleId() {
		return privilegeRoleId;
	}
	public void setPrivilegeRoleId(String privilegeRoleId) {
		this.privilegeRoleId = privilegeRoleId;
	}
}
