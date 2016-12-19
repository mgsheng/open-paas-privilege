package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 资源相关操作链接类
 */
public class PrivilegeFunctionDto  {
    /**
     *全县功能
     */
    /*id*/
    private String id;
    /*资源id*/
    private String resourceId;
    /*操作按钮Id*/
    private String operationId;
    /*操作url地址*/
    private String optUrl;
    /*创建人*/
    private String createUser;
    /*创建人Id*/
    private String createUserId;
    /*创建时间*/
    private Date createTime;
    private String appId;
    
    
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

	private String privilegeFunctionAddUrl;
	private String privilegeFunctionModifyUrl;
	private String privilegeFunctionDelUrl;
	
	
	
	public String getPrivilegeFunctionAddUrl() {
		return privilegeFunctionAddUrl;
	}
	public void setPrivilegeFunctionAddUrl(String privilegeFunctionAddUrl) {
		this.privilegeFunctionAddUrl = privilegeFunctionAddUrl;
	}
	public String getPrivilegeFunctionModifyUrl() {
		return privilegeFunctionModifyUrl;
	}
	public void setPrivilegeFunctionModifyUrl(String privilegeFunctionModifyUrl) {
		this.privilegeFunctionModifyUrl = privilegeFunctionModifyUrl;
	}
	public String getPrivilegeFunctionDelUrl() {
		return privilegeFunctionDelUrl;
	}
	public void setPrivilegeFunctionDelUrl(String privilegeFunctionDelUrl) {
		this.privilegeFunctionDelUrl = privilegeFunctionDelUrl;
	}
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&resourceId=%s&operationId=%s&optUrl=%s&createUser=%s&createUserid=%s",
        		privilegeFunctionAddUrl,appId,resourceId,operationId,optUrl,createUser,createUserId);  
    }
	public String getModifyUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&functionId=%s&operationId=%s&optUrl=%s&createUser=%s&createUserid=%s",
        		privilegeFunctionModifyUrl,appId,functionId,operationId, optUrl, createUser,createUserId);  
    }
	public String getDelUri() throws UnsupportedEncodingException {
        return String.format("%s?functionId=%s&appId=%s",
        		privilegeFunctionDelUrl,functionId,appId);  
    }
	
	private String functionId;
	

    public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOptUrl() {
        return optUrl;
    }

    public void setOptUrl(String optUrl) {
        this.optUrl = optUrl;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        try {
			this.createUser =  java.net.URLEncoder.encode(createUser,"UTF-8");
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
