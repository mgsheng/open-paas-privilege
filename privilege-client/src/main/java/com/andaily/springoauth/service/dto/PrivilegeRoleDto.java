package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;

public class PrivilegeRoleDto{
	private String privilegeRoleId;
	private String appId;
	private String method;//0-添加权限，1-删除权限
	private String roleName;
	private String rolePrivilege;
	private String groupId;
	private String groupName;
	private String deptId;
	private String deptName;
	private String parentRoleId;
	private String remark;
	private int status;
	private String createUser;
	private String createUserId;
	
	private String start;//开始记录数
	private String limit;//每页记录数
	
	private String addPrivilegeRoleUri;
	private String delPrivilegeRoleUri;
	private String modiPrivilegeRoleUri;
	private String getPrivilegeRoleUri;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		try {
			this.roleName = java.net.URLEncoder.encode(roleName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		try {
			this.remark = java.net.URLEncoder.encode(remark,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		try {
			this.deptName = java.net.URLEncoder.encode(deptName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		try {
			this.groupName = java.net.URLEncoder.encode(groupName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
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
		try {
			this.createUser = java.net.URLEncoder.encode(createUser,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
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
	public String getParentRoleId() {
		return parentRoleId;
	}
	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}	
	public String getPrivilegeRoleId() {
		return privilegeRoleId;
	}
	public void setPrivilegeRoleId(String privilegeRoleId) {
		this.privilegeRoleId = privilegeRoleId;
	}
	public String getDelPrivilegeRoleUri() {
		return delPrivilegeRoleUri;
	}
	public void setDelPrivilegeRoleUri(String delPrivilegeRoleUri) {
		this.delPrivilegeRoleUri = delPrivilegeRoleUri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getModiPrivilegeRoleUri() {
		return modiPrivilegeRoleUri;
	}
	public void setModiPrivilegeRoleUri(String modiPrivilegeRoleUri) {
		this.modiPrivilegeRoleUri = modiPrivilegeRoleUri;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getGetPrivilegeRoleUri() {
		return getPrivilegeRoleUri;
	}
	public void setGetPrivilegeRoleUri(String getPrivilegeRoleUri) {
		this.getPrivilegeRoleUri = getPrivilegeRoleUri;
	}
	
	public String getAddFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&roleName=%s&rolePrivilege=%s&groupId=%s&groupName=%s&deptId=%s&deptName=%s&parentRoleId=%s&remark=%s&createUser=%s&createUserId=%s&status=%s",
        		addPrivilegeRoleUri,appId,roleName,rolePrivilege,groupId,groupName,deptId,deptName,parentRoleId,remark,createUser,createUserId,status);  
    }
	public String getDelFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&privilegeRoleId=%s&createUser=%s&createUserId=%s",
        		delPrivilegeRoleUri,appId,privilegeRoleId,createUser,createUserId);  
    }
	public String getModiFullUri() throws UnsupportedEncodingException {
		return String.format("%s?privilegeRoleId=%s&appId=%s&method=%s&roleName=%s&rolePrivilege=%s&groupId=%s&groupName=%s&deptId=%s&deptName=%s&parentRoleId=%s&remark=%s&createUser=%s&createUserId=%s&status=%s",
        		modiPrivilegeRoleUri,privilegeRoleId,appId,method,roleName,rolePrivilege,groupId,groupName,deptId,deptName,parentRoleId,remark,createUser,createUserId,status);  
    }
	public String getGetFullUri() throws UnsupportedEncodingException {
		return String.format("%s?privilegeRoleId=%s&appId=%s&groupId=%s&deptId=%s&start=%s&limit=%s",
        		getPrivilegeRoleUri,privilegeRoleId,appId,groupId,deptId,start,limit);  
    }
}
