package cn.com.open.opensass.privilege.vo;


import java.io.Serializable;

/**
 * Created by jh on 2016/12/19.
 */
public class PrivilegeMenusVo implements Serializable {
    private String menuId;
    private String parentId;
    private String menuName;
    private int displayOrder;
    private String menuCode;
    private int status;
     
     
	public String getMenuId() {
        return menuId;
    }

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrivilegeMenusVo)) {
            return false;
        }
        PrivilegeMenusVo privilegeMenuVo = (PrivilegeMenusVo) o;
        return menuId.equals(privilegeMenuVo.getMenuId());
    }

    @Override
    public int hashCode() {
        return menuId.hashCode();
    }

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
}
