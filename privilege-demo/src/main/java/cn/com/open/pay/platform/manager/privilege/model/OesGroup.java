package cn.com.open.pay.platform.manager.privilege.model;


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
	private String groupTypeName;
	private String groupName;
	private String groupType;
	private String groupLogo;
	
	
	public String getGroupLogo() {
		return groupLogo;
	}
	public void setGroupLogo(String groupLogo) {
		this.groupLogo = groupLogo;
	}
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
	public String getGroupTypeName() {
		return groupTypeName;
	}
	public void setGroupTypeName(String groupTypeName) {
		this.groupTypeName = groupTypeName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
}
