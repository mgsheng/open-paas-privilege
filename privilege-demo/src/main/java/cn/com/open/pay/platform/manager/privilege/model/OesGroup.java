package cn.com.open.pay.platform.manager.privilege.model;

import java.util.Date;

public class OesGroup extends AbstractDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Integer id;
	private String groupCode;
	private String groupName;
	private Date groupType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Date getGroupType() {
		return groupType;
	}
	public void setGroupType(Date groupType) {
		this.groupType = groupType;
	}
}
