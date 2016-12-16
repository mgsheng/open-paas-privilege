package cn.com.open.opensass.privilege.dao;

import java.io.Serializable;

/**
 * Created by jh on 2016/12/15.
 */
public class PrivilegeUrl  {
    private String privilegeUrl;
    private String childUrl;

    public String getPrivilegeUrl() {
        return privilegeUrl;
    }

    public void setPrivilegeUrl(String privilegeUrl) {
        this.privilegeUrl = privilegeUrl;
    }

    public String getChildUrl() {
        return childUrl;
    }

    public void setChildUrl(String childUrl) {
        this.childUrl = childUrl;
    }

}
