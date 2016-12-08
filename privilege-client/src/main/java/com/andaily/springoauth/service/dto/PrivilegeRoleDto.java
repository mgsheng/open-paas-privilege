package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class PrivilegeRoleDto  implements Serializable{
	private String privilegeRoleId;
	private String roleId;
	private String roleName;
	private String appId;
	private String deptId;
	private String groupId;
	private String roleLevel;
	private String remark;
	private String deptName;
	private String groupName;
	private String rolePrivilege;
	private int status;
	private String createUser;
	private String createUserId;
	private String addPrivilegeRoleUri;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPrivilegeRoleId() {
		return privilegeRoleId;
	}
	public void setPrivilegeRoleId(String privilegeRoleId) {
		this.privilegeRoleId = privilegeRoleId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	public String getRoleLevel() {
		return roleLevel;
	}
	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getRolePrivilege() {
		return rolePrivilege;
	}
	public void setRolePrivilege(String rolePrivilege) {
		this.rolePrivilege = rolePrivilege;
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
	public String getAddPrivilegeRoleUri() {
		return addPrivilegeRoleUri;
	}
	public void setAddPrivilegeRoleUri(String addPrivilegeRoleUri) {
		this.addPrivilegeRoleUri = addPrivilegeRoleUri;
	}	
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&roleName=%s&rolePrivilege=%s&groupId=%s&groupName=%s&deptId=%s&deptName=%s&roleLevel=%s&remark=%s&createUser=%s&createUserId=%s&status=%s",
        		addPrivilegeRoleUri,appId,roleName,rolePrivilege,groupId,groupName,deptId,deptName,roleLevel,remark,createUser,createUserId,status);  
    }
}
