package cn.com.open.opensass.privilege.model;

import java.io.Serializable;
import java.util.Date;

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
    public String guid() {
        return id;
    }

    public void id(String guid) {
        this.id = guid;
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