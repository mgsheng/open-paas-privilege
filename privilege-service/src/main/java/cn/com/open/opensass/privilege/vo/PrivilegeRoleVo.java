package cn.com.open.opensass.privilege.vo;

import java.util.List;

public class PrivilegeRoleVo{
	private String privilegeRoleId;
	private String appId;
	/*private String method;*/
	private String roleName;
	/*private String rolePrivilege;
	private String privilegeFunId;*/
	private String groupId;
	private String groupName;
	private String deptId;
	private String deptName;
	private Integer roleLevel;
	/*private Integer roleType;*/
	private String parentRoleId;
	private String remark;
	private int status;
	/*private String createUser;
	private String createUserId;
	
	private String start;
	private String limit;*/
	
	private List<PrivilegeResourceVo> resourceList;
	private List<PrivilegeFunctionVo> functionList;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/*public String getRolePrivilege() {
		return rolePrivilege;
	}
	public void setRolePrivilege(String rolePrivilege) {
		this.rolePrivilege = rolePrivilege;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}*/
	public String getParentRoleId() {
		return parentRoleId;
	}
	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}
	public String getPrivilegeRoleId() {
		return privilegeRoleId;
	}
	public void setPrivilegeRoleId(String privilegeRoleId) {
		this.privilegeRoleId = privilegeRoleId;
	}
	/*public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getPrivilegeFunId() {
		return privilegeFunId;
	}
	public void setPrivilegeFunId(String privilegeFunId) {
		this.privilegeFunId = privilegeFunId;
	}*/
	public Integer getRoleLevel() {
		return roleLevel;
	}
	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}
	/*public Integer getRoleType() {
		return roleType;
	}
	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}	*/
	public List<PrivilegeResourceVo> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<PrivilegeResourceVo> resourceList) {
		this.resourceList = resourceList;
	}
	public List<PrivilegeFunctionVo> getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List<PrivilegeFunctionVo> functionList) {
		this.functionList = functionList;
	}
}
