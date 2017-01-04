package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;
import java.util.Date;

import cn.com.open.pay.platform.manager.tools.GuidGenerator;

/**
 * 
 */
public abstract class AbstractDomain implements Serializable {

	/**
     * Domain business guid.
     */
    protected String id = GuidGenerator.generate();

    public AbstractDomain() {
    }
    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDomain)) {
            return false;
        }
        AbstractDomain that = (AbstractDomain) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //For subclass override it
    public void saveOrUpdate() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{ id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}