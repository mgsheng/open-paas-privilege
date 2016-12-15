package cn.com.open.opensass.privilege.model;

import java.util.Date;

/**
 * 资源相关操作链接类
 */
public class PrivilegeFunction extends AbstractDomain {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
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
        this.createUser = createUser;
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
