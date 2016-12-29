package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class PrivilegeMenuDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String appId;
	private String menuName;
	private String menuCode;
	private String menuRule;
	private String menuLevel;
	private String parentId;
	private String dislayOrder;
	private Date createTime;
	private String createUser;
	private String createUserId;
	private String resourceType;
	private String status;
	private String privilegeMenuAddUrl;
	private String privilegeMenuModifyUrl;
	private String privilegeMenuQueryUrl;
	private String privilegeMenuDelUrl;
	private String limit;
	private String start;
	private String menuId;
	
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&menuName=%s&menuCode=%s&menuRule=%s&menuLevel=%s&parentId=%s&dislayOrder=%s&status=%s",
        		privilegeMenuAddUrl,appId,menuName,menuCode, menuRule, menuLevel,parentId, dislayOrder,status);  
    }
	public String getModifyUri() throws UnsupportedEncodingException {
		  return String.format("%s?appId=%s&menuId=%s&menuName=%s&menuCode=%s&menuRule=%s&menuLevel=%s&parentId=%s&dislayOrder=%s&status=%s",
				  privilegeMenuModifyUrl,appId,menuId,menuName,menuCode, menuRule, menuLevel,parentId, dislayOrder,status);  
    }
	public String getQueryUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&menuId=%s&Limit=%s&start=%s",
        		privilegeMenuQueryUrl,appId,menuId,limit,start);  
    }
	public String getDelUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&menuId=%s",
        		privilegeMenuDelUrl,appId,menuId);  
    }
	public String getPrivilegeMenuAddUrl() {
		return privilegeMenuAddUrl;
	}
	public void setPrivilegeMenuAddUrl(String privilegeMenuAddUrl) {
		this.privilegeMenuAddUrl = privilegeMenuAddUrl;
	}
	public String getPrivilegeMenuModifyUrl() {
		return privilegeMenuModifyUrl;
	}
	public void setPrivilegeMenuModifyUrl(String privilegeMenuModifyUrl) {
		this.privilegeMenuModifyUrl = privilegeMenuModifyUrl;
	}
	public String getPrivilegeMenuQueryUrl() {
		return privilegeMenuQueryUrl;
	}
	public void setPrivilegeMenuQueryUrl(String privilegeMenuQueryUrl) {
		this.privilegeMenuQueryUrl = privilegeMenuQueryUrl;
	}
	public String getPrivilegeMenuDelUrl() {
		return privilegeMenuDelUrl;
	}
	public void setPrivilegeMenuDelUrl(String privilegeMenuDelUrl) {
		this.privilegeMenuDelUrl = privilegeMenuDelUrl;
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
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		try {
			this.menuName = java.net.URLEncoder.encode(menuName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuRule() {
		return menuRule;
	}
	public void setMenuRule(String menuRule) {
		try {
			this.menuRule =java.net.URLEncoder.encode(menuRule,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDislayOrder() {
		return dislayOrder;
	}
	public void setDislayOrder(String dislayOrder) {
		this.dislayOrder = dislayOrder;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
