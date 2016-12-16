package cn.com.open.opensass.privilege.dao;

import cn.com.open.opensass.privilege.model.PrivilegeResource;

import java.util.List;

/**
 * Created by jh on 2016/12/13.
 */
public class ResourceUrl {
    /*key*/
   /* private String key;*/
    /*url地址*/
    private String privilegeUrl;

    private String childUrl;
    /*private String urlFun;*/

    /*private List<PrivilegeResource> privilegeResources;

    public List<PrivilegeResource> getPrivilegeResources() {
        return privilegeResources;
    }*/

    /*public void setPrivilegeResources(List<PrivilegeResource> privilegeResources) {
        this.privilegeResources = privilegeResources;
    }*/

   /* public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }*/

   /* public String getUrlFun() {
        return urlFun;
    }

    public void setUrlFun(String urlFun) {
        this.urlFun = urlFun;
    }*/

   /* public String getUrl() {
        return privilegeUrl;
    }

    public void setUrl(String privilegeUrl) {
        this.privilegeUrl = privilegeUrl;
    }*/

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
