package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class PrivilegeGroupDto {
	private String groupId;
	private String groupName;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private String appId;
	private String status;
	private String groupPrivilege;
	
	private String privilegeGroupAddUrl;
	private String privilegeGroupModifyUrl;
	private String privilegeGroupQueryUrl;
	private String privilegeGroupDelUrl;
	private String limit;
	private String start;
	private String method;
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPrivilegeGroupAddUrl() {
		return privilegeGroupAddUrl;
	}
	public void setPrivilegeGroupAddUrl(String privilegeGroupAddUrl) {
		this.privilegeGroupAddUrl = privilegeGroupAddUrl;
	}
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?groupId=%s&groupName=%s&appId=%s&groupPrivilege=%s&createUser=%s&createUserid=%s&status=%s",
        		privilegeGroupAddUrl,groupId,groupName,appId, groupPrivilege, createUser,createUserId, status);  
    }
	public String getModifyUri() throws UnsupportedEncodingException {
        return String.format("%s?groupId=%s&groupName=%s&appId=%s&groupPrivilege=%s&createUser=%s&createUserid=%s&status=%s&method=%s",
        		privilegeGroupModifyUrl,groupId,groupName,appId, groupPrivilege, createUser,createUserId, status,method);  
    }
	public String getQueryUri() throws UnsupportedEncodingException {
        return String.format("%s?groupId=%s&appId=%s&limit=%s&start=%s",
        		privilegeGroupQueryUrl,groupId,appId,limit,start);  
    }
	public String getDelUri() throws UnsupportedEncodingException {
        return String.format("%s?groupId=%s&appId=%s&createUser=%s&createUserid=%s",
        		privilegeGroupDelUrl,groupId,appId, createUser,createUserId);  
    }
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getPrivilegeGroupQueryUrl() {
		return privilegeGroupQueryUrl;
	}
	public void setPrivilegeGroupQueryUrl(String privilegeGroupQueryUrl) {
		this.privilegeGroupQueryUrl = privilegeGroupQueryUrl;
	}
	public String getPrivilegeGroupDelUrl() {
		return privilegeGroupDelUrl;
	}
	public void setPrivilegeGroupDelUrl(String privilegeGroupDelUrl) {
		this.privilegeGroupDelUrl = privilegeGroupDelUrl;
	}
	public String getPrivilegeGroupModifyUrl() {
		return privilegeGroupModifyUrl;
	}
	public void setPrivilegeGroupModifyUrl(String privilegeGroupModifyUrl) {
		this.privilegeGroupModifyUrl = privilegeGroupModifyUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroupPrivilege() {
		return groupPrivilege;
	}
	public void setGroupPrivilege(String groupPrivilege) {
		this.groupPrivilege = groupPrivilege;
	}
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
		try {
			this.groupName = java.net.URLEncoder.encode(groupName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
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
		try {
			this.createUser = java.net.URLEncoder.encode(createUser,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
