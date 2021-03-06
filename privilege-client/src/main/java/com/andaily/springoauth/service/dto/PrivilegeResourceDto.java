package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class PrivilegeResourceDto {
	/**
	 * 资源
	 */
	private String appId;
	private String menuId;
	private String resourceName;
	private String resourceRule;
	private String resourceLevel;
	private String baseUrl;
	private String dislayOrder;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private String status;
	private String privilegeResourceAddUrl;
	private String privilegeResourceModifyUrl;
	private String privilegeResourceQueryUrl;
	private String privilegeResourceDelUrl;
	private String limit;
	private String start;
	private String resourceId;
	
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&resourceLevel=%s&resourceName=%s&menuId=%s&baseUrl=%s&resourceRule=%s&createUser=%s&createUserid=%s",
        		privilegeResourceAddUrl,appId,resourceLevel,resourceName, menuId, baseUrl,resourceRule, createUser,createUserId);  
    }
	public String getModifyUri() throws UnsupportedEncodingException {
		 return String.format("%s?appId=%s&resourceId=%s&resourceLevel=%s&resourceName=%s&menuId=%s&baseUrl=%s&resourceRule=%s&createUser=%s&createUserid=%s",
				 privilegeResourceModifyUrl,appId,resourceId,resourceLevel,resourceName, menuId, baseUrl,resourceRule, createUser,createUserId);   
    }
	public String getQueryUri() throws UnsupportedEncodingException {
		 return String.format("%s?appId=%s&resourceLevel=%s&menuId=%s&start=%s&Limit=%s",
				 privilegeResourceQueryUrl,appId,resourceLevel, menuId, start,limit);  
    }
	public String getDelUri() throws UnsupportedEncodingException {
		 return String.format("%s?appId=%s&resourceId=%s&createUser=%s&createUserid=%s",
				 privilegeResourceDelUrl,appId,resourceId,createUser,createUserId);  
    }
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getPrivilegeResourceAddUrl() {
		return privilegeResourceAddUrl;
	}
	public void setPrivilegeResourceAddUrl(String privilegeResourceAddUrl) {
		this.privilegeResourceAddUrl = privilegeResourceAddUrl;
	}
	public String getPrivilegeResourceModifyUrl() {
		return privilegeResourceModifyUrl;
	}
	public void setPrivilegeResourceModifyUrl(String privilegeResourceModifyUrl) {
		this.privilegeResourceModifyUrl = privilegeResourceModifyUrl;
	}
	public String getPrivilegeResourceQueryUrl() {
		return privilegeResourceQueryUrl;
	}
	public void setPrivilegeResourceQueryUrl(String privilegeResourceQueryUrl) {
		this.privilegeResourceQueryUrl = privilegeResourceQueryUrl;
	}
	public String getPrivilegeResourceDelUrl() {
		return privilegeResourceDelUrl;
	}
	public void setPrivilegeResourceDelUrl(String privilegeResourceDelUrl) {
		this.privilegeResourceDelUrl = privilegeResourceDelUrl;
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
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		try {
			this.resourceName =java.net.URLEncoder.encode(resourceName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getResourceRule() {
		return resourceRule;
	}
	public void setResourceRule(String resourceRule) {
		try {
			this.resourceRule = java.net.URLEncoder.encode(resourceRule,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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
		}
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
	public String getResourceLevel() {
		return resourceLevel;
	}
	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
	}
	public String getDislayOrder() {
		return dislayOrder;
	}
	public void setDislayOrder(String dislayOrder) {
		this.dislayOrder = dislayOrder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
