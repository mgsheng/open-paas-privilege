package cn.com.open.opensass.privilege.vo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

 public class UserMenuVo  implements Comparable,Serializable{
    private String menuId;
    private String parentId;
    private String menuName;
    private String menuRule;
    private int menuLevel;
    private int displayOrder;
    private String menuCode;
    private String appId;
    private int status;
	private String baseUrl;
	private TreeSet<UserMenuVo> childMenus;
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
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public TreeSet<UserMenuVo> getChildMenus() {
		return childMenus;
	}
	public void setChildMenus(TreeSet<UserMenuVo> childMenus) {
		this.childMenus = childMenus;
	}
	public int compareTo(Object o) {
		UserMenuVo userMenuVo = (UserMenuVo) o;
		if (displayOrder < userMenuVo.getDisplayOrder()){
			return -1;
		}
		return 1;
	}
}
