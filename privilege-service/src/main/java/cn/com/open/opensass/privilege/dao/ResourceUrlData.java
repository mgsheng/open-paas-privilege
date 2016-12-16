package cn.com.open.opensass.privilege.dao;

import java.util.List;

/**
 * Created by jh on 2016/12/13.
 */
public class ResourceUrlData {

    private List<ResourceUrl> privilegeUrl;

    public List<ResourceUrl> getResourceUrls() {
        return privilegeUrl;
    }

    public void setResourceUrls(List<ResourceUrl> resourceUrls) {
        this.privilegeUrl = resourceUrls;
    }
}
