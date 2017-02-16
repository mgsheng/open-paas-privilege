package cn.com.open.pay.platform.manager.privilege.model;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;

public class OesUser extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	
	private String groupId;
	private String userName;
	private String userId;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
