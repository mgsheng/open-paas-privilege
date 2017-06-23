package cn.com.open.opensass.privilege.log;

public class PrivilegeServiceLog {

    /* 通用属性 */
    private String interfaceName; //接口名
    private String username; //用户名
    private String sourceUid; //业务系统用户id
    private String invokeStatus; //接口状态 1正常或成功 0 失败或异常
    private String errorCode; //错误码
    private double executionTime; //执行时长
    private String logName; //日志名称
    private String createTime; //创建时间
    private String errorMessage;

    /* 定制属性 */
    private String appId; //应用id
    private String createUser; //操作人用户名
    private String resourceId; //资源id 多个资源用“,”分割
    private String operationId; //功能id
    private String optUrl; //功能url地址
    private String createUserId; //操作人用户id
    private String createUserid; //操作人用户id
    private String functionId; //功能id 多个用,隔开
    private String appUserId; //应用用户id
    private String groupId; //组织机构id
    private String groupName; //组织机构名称
    private String groupPrivilege; //所拥有权限 多个权限用”,”分隔
    private String status; //状态（0：有效，1：无效）
    private String start; // 开始记录数，分页查询
    private String limit; //每页记录数，分页查询
    private String method; //操作(0:添加权限 1：删除权限)
    private String operationType; //操作类型( 0是添加1是删除)
    private String menuName; //菜单名称
    private String menuRule; //菜单自定义规则
    private String menuLevel; //菜单层级（数字1：一级菜单，2：二级菜单…）
    private String menuCode; //菜单关键字
    private String parentId; //菜单父id（根节点id为0）
    private String dislayOrder; //菜单排序(默认为0)
    private String menuId; //菜单id
    private String optId; //操作id
    private String resourceName; //资源名称
    private String resourceRule; //资源规则（业务系统可自定义）
    private String resourceLevel; //资源级别（必传）（0：基础功能 1：服务级别，2:应用级别）
    private String baseUrl; //资源地址
    private String rolePrivilege; //所拥有权限（多个用，分隔）
    private String privilegeFunId;//所拥有的方法（多个用，分隔）
    private String deptId; //部门id
    private String deptName; //部门名称
    private String roleLevel; //角色层级（数字格式0：无层级，1：有层级）
    private String roleType; //角色类型(1:普通角色2：系统管理员)
    private String roleName; //角色名
    private String parentRoleId; //父角色id（无层级关系时为0）
    private String remark; //备注
    private String privilegeRoleId; //角色id
    private String urladdr;
    private String appUserName;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSourceUid() {
        return sourceUid;
    }

    public void setSourceUid(String sourceUid) {
        this.sourceUid = sourceUid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInvokeStatus() {
        return invokeStatus;
    }

    public void setInvokeStatus(String invokeStatus) {
        this.invokeStatus = invokeStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOptUrl() {
        return optUrl;
    }

    public void setOptUrl(String optUrl) {
        this.optUrl = optUrl;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPrivilege() {
        return groupPrivilege;
    }

    public void setGroupPrivilege(String groupPrivilege) {
        this.groupPrivilege = groupPrivilege;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDislayOrder() {
        return dislayOrder;
    }

    public void setDislayOrder(String dislayOrder) {
        this.dislayOrder = dislayOrder;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceRule() {
        return resourceRule;
    }

    public void setResourceRule(String resourceRule) {
        this.resourceRule = resourceRule;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRolePrivilege() {
        return rolePrivilege;
    }

    public void setRolePrivilege(String rolePrivilege) {
        this.rolePrivilege = rolePrivilege;
    }

    public String getPrivilegeFunId() {
        return privilegeFunId;
    }

    public void setPrivilegeFunId(String privilegeFunId) {
        this.privilegeFunId = privilegeFunId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrivilegeRoleId() {
        return privilegeRoleId;
    }

    public void setPrivilegeRoleId(String privilegeRoleId) {
        this.privilegeRoleId = privilegeRoleId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUrladdr() {
        return urladdr;
    }

    public void setUrladdr(String urladdr) {
        this.urladdr = urladdr;
    }

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
