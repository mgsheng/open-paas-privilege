package cn.com.open.opensass.privilege.vo;


import java.io.Serializable;

/**
 * Created by jh on 2016/12/19.
 */
public class PrivilegeMenuVo implements Serializable {
    private String menuId;
    private String parentId;
    private String menuName;
    private String menuRule;
    private int menuLevel;
    private int displayOrder;

    public String getMenuId() {
        return menuId;
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

    public String getMenuRule() {
        return menuRule;
    }

    public void setMenuRule(String menuRule) {
        this.menuRule = menuRule;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
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
        if (!(o instanceof PrivilegeMenuVo)) {
            return false;
        }
        PrivilegeMenuVo privilegeMenuVo = (PrivilegeMenuVo) o;
        return menuId.equals(privilegeMenuVo.getMenuId());
    }

    @Override
    public int hashCode() {
        return menuId.hashCode();
    }
}
