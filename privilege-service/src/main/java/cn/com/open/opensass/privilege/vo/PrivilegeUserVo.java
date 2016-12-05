package cn.com.open.opensass.privilege.vo;

import java.util.List;

import cn.com.open.opensass.privilege.model.PrivilegeRole;

public class PrivilegeUserVo {
	private String appId;
	private String appUserid;
	private String appUsername;
	private String roleId;
	private String deptId;
	private String groupId;
	private String privilegeFunid;
	private List<PrivilegeRoleVo> roleList;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
	public String getPrivilegeFunid() {
		return privilegeFunid;
	}
	public void setPrivilegeFunid(String privilegeFunid) {
		this.privilegeFunid = privilegeFunid;
	}
	public List<PrivilegeRoleVo> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<PrivilegeRoleVo> roleList) {
		this.roleList = roleList;
	}
	
}
