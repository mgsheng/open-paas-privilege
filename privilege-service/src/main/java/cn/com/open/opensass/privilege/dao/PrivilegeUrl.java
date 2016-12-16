package cn.com.open.opensass.privilege.dao;

import java.io.Serializable;

/**
 * Created by jh on 2016/12/15.
 */
public class PrivilegeUrl implements Serializable  {
    private String privilegeUrl;

    public String getPrivilegeUrl() {
        return privilegeUrl;
    }

    public void setPrivilegeUrl(String privilegeUrl) {
        this.privilegeUrl = privilegeUrl;
    }

    public boolean equals(Object obj){
        if(obj instanceof PrivilegeUrl){ //
            PrivilegeUrl p = (PrivilegeUrl)obj;
            return(privilegeUrl.equals(p.privilegeUrl));
        }
        return super.equals(obj);
    }
    public int HashCode()
    {
        return privilegeUrl.hashCode();
    }
}
