package cn.com.open.opensass.privilege.model;

import scala.util.parsing.combinator.testing.Str;

/**
 * Created by jh on 2016/12/13.
 */
public class PrivilegeResource {
    private String resourceId;
    private String menuId;
    private String resourceName;
    private String resourceRule;
    private Integer displayOrder;
    private String appId;
    private String baseUrl;
    private String appUserId;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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
        this.resourceName = resourceName;
    }

    public String getResourceRule() {
        return resourceRule;
    }

    public void setResourceRule(String resourceRule) {
        this.resourceRule = resourceRule;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }
}
