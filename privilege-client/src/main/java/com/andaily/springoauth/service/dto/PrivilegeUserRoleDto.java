package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;

public class PrivilegeUserRoleDto{
	private String privilegeRoleId;//角色id(多个角色用“，”分隔)
	private String appId;
	private String appUserId;
	private String appUserName;
	private String privilegeFunId;//所拥有的功能（多个功能以“，”分隔）
	private String groupId;
	private String deptId;
	private String createUser;
	private String createUserId;
	private String resourceId;//所拥有的权限（多个限用“，”分隔）
	private String method;//操作（0-添加角色 1-删除角色）
	private String optUrl;
	
	private String addPrivilegeUserUri;
	private String delPrivilegeUserUri;
	private String modiPrivilegeUserUri;
	private String getPrivilegeUserUri;
	private String verifyPrivilegeUserUri;
	private String privilegeUserRoleQueryTestUrl;
	
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
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		try {
			this.createUser = java.net.URLEncoder.encode(createUser,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} ;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getPrivilegeRoleId() {
		return privilegeRoleId;
	}
	public void setPrivilegeRoleId(String privilegeRoleId) {
		this.privilegeRoleId = privilegeRoleId;
	}
	public String getPrivilegeFunId() {
		return privilegeFunId;
	}
	public void setPrivilegeFunId(String privilegeFunId) {
		this.privilegeFunId = privilegeFunId;
	}
	public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
	public String getAppUserName() {
		return appUserName;
	}
	public void setAppUserName(String appUserName) {
		try {
			this.appUserName = java.net.URLEncoder.encode(appUserName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getAddPrivilegeUserUri() {
		return addPrivilegeUserUri;
	}
	public void setAddPrivilegeUserUri(String addPrivilegeUserUri) {
		this.addPrivilegeUserUri = addPrivilegeUserUri;
	}
	public String getDelPrivilegeUserUri() {
		return delPrivilegeUserUri;
	}
	public void setDelPrivilegeUserUri(String delPrivilegeUserUri) {
		this.delPrivilegeUserUri = delPrivilegeUserUri;
	}	
	public String getModiPrivilegeUserUri() {
		return modiPrivilegeUserUri;
	}
	public void setModiPrivilegeUserUri(String modiPrivilegeUserUri) {
		this.modiPrivilegeUserUri = modiPrivilegeUserUri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}	
	public String getGetPrivilegeUserUri() {
		return getPrivilegeUserUri;
	}
	public void setGetPrivilegeUserUri(String getPrivilegeUserUri) {
		this.getPrivilegeUserUri = getPrivilegeUserUri;
	}	
	public String getOptUrl() {
		return optUrl;
	}
	public void setOptUrl(String optUrl) {
		this.optUrl = optUrl;
	}	
	public String getVerifyPrivilegeUserUri() {
		return verifyPrivilegeUserUri;
	}
	public void setVerifyPrivilegeUserUri(String verifyPrivilegeUserUri) {
		this.verifyPrivilegeUserUri = verifyPrivilegeUserUri;
	}
	
	public String getAddFullUri() throws UnsupportedEncodingException {
        return String.format("%s?privilegeRoleId=%s&appId=%s&appUserId=%s&appUserName=%s&deptId=%s&groupId=%s&privilegeFunId=%s&resourceId=%s&createUser=%s&createUserId=%s",
        		addPrivilegeUserUri,privilegeRoleId,appId,appUserId,appUserName,deptId,groupId,privilegeFunId,resourceId,createUser,createUserId);  
    }
	public String getdelFullUri() {
		return String.format("%s?appId=%s&appUserId=%s&createUser=%s&createUserId=%s",
				delPrivilegeUserUri,appId,appUserId,createUser,createUserId);  
	}
	public String getModiFullUri() {
		return String.format("%s?appUserId=%s&appId=%s&method=%s&privilegeRoleId=%s&resourceId=%s&privilegeFunId=%s&deptId=%s&groupId=%s&createUser=%s&createUserId=%s",
        		modiPrivilegeUserUri,appUserId,appId,method,privilegeRoleId,resourceId,privilegeFunId,deptId,groupId,createUser,createUserId);  
	}
	public String getGetFullUri() {
		return String.format("%s?appUserId=%s&appId=%s",
        		getPrivilegeUserUri,appUserId,appId);  
	}
	public String getVerifyFullUri() {
		return String.format("%s?appUserId=%s&appId=%s&optUrl=%s",
        		verifyPrivilegeUserUri,appUserId,appId,optUrl);  
	}
	public String getPrivilegeUserRoleQueryTestUrl() {
		return String.format("%s?appUserId=%s&appId=%s",
						privilegeUserRoleQueryTestUrl,appUserId,appId);
	}
	public void setPrivilegeUserRoleQueryTestUrl(String privilegeUserRoleQueryTestUrl) {
		this.privilegeUserRoleQueryTestUrl = privilegeUserRoleQueryTestUrl;
	}
	
	
}
