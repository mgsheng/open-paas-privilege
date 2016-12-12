<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>modi-privilege-role(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">i_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="modiPrivilegeRole" method="post" class="form-horizontal">
                <input type="hidden" name="modiPrivilegeRoleUri" id="modiPrivilegeRoleUri" value="${modiPrivilegeRoleUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                	<div class="form-group">
                        <label class="col-sm-2 control-label">privilegeRoleId</label>

                        <div class="col-sm-10">
                            <input type="text" name="privilegeRoleId" id="privilegeRoleId"
                                   class="form-control" ng-model="privilegeRoleId"/>

                            <p class="help-block">角色Id（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" id="appId"
                                   class="form-control" ng-model="appId"/>

                            <p class="help-block">应用Id（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">method</label>

                        <div class="col-sm-10">
                            <select name="method" id="method" class="form-control" ng-mode="method">
								<option value="0">添加权限</option>
								<option value="1">删除权限</option>
							</select>
                            <p class="help-block">操作：0-添加权限，1-删除权限（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">  
                        <label class="col-sm-2 control-label">roleName</label>

                        <div class="col-sm-10">
                            <input type="text" name="roleName" size="50" id="roleName" class="form-control"
                                   ng-model="roleName"/>

                            <p class="help-block">角色名</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">rolePrivilege</label>

                        <div class="col-sm-10">
                            <input type="text" name="rolePrivilege" id="rolePrivilege"
                                   class="form-control" ng-model="rolePrivilege"/>

                            <p class="help-block">所拥有权限（多个权限用“，”分隔）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">privilegeFunId</label>

                        <div class="col-sm-10">
                            <input type="text" name="privilegeFunId" id="privilegeFunId"
                                   class="form-control" ng-model="privilegeFunId"/>

                            <p class="help-block">所拥有功能（多个功能用“，”分隔）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">groupId</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupId" id="groupId"
                                   class="form-control" ng-model="groupId"/>

                            <p class="help-block">组织机构id</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">groupName</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupName" id="groupName" size="50" class="form-control"
                                   ng-model="groupName"/>
                            <p class="help-block">组织机构名称</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">deptId</label>

                        <div class="col-sm-10">
                            <input type="text" name="deptId" id="deptId"
                                   class="form-control" ng-model="deptId"/>

                            <p class="help-block">部门id</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">deptName</label>

                        <div class="col-sm-10">
                            <input type="text" name="deptName" id="deptName"
                                   class="form-control" ng-model="deptName"/>

                            <p class="help-block">部门名称</p>
                        </div>
                    </div>
                    
                     <div class="form-group">
                        <label class="col-sm-2 control-label">roleLevel</label>

                        <div class="col-sm-10">
							<select name="roleLevel" id="roleLevel" class="form-control" ng-mode="roleLevel">
								<option value="0">无层级</option>
								<option value="1">有层级</option>
							</select>
                            <p class="help-block">角色层级：0-无层级 1-有层级</p>
                        </div>
                    </div>
                    
                     <div class="form-group">
                        <label class="col-sm-2 control-label">roleType</label>

                        <div class="col-sm-10">
							<select name="roleType" id="roleType" class="form-control" ng-mode="roleType">
								<option value="1">普通角色</option>
								<option value="2">系统管理员</option>
							</select>
                            <p class="help-block">角色类型：1-普通角色 2-系统管理员</p>
                        </div>
                    </div>
                    
                     <div class="form-group">
                        <label class="col-sm-2 control-label">parentRoleId</label>

                        <div class="col-sm-10">
                            <input type="text" name="parentRoleId" id="parentRoleId"
                                   class="form-control" ng-model="parentRoleId"/>

                            <p class="help-block">父角色id(无层级关系时为0)</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">remark</label>

                        <div class="col-sm-10">
                            <input type="text" name="remark" id="remark"
                                   class="form-control" ng-model="remark"/>

                            <p class="help-block">备注</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">createUser</label>

                        <div class="col-sm-10">
                            <input type="text" name="createUser" id="createUser"
                                   class="form-control" ng-model="createUser"/>

                            <p class="help-block">创建人</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">createUserId</label>

                        <div class="col-sm-10">
                            <input type="text" name="createUserId" id="createUserId"
                                   class="form-control" ng-model="createUserId"/>

                            <p class="help-block">创建人用户Id</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">status</label>

                        <div class="col-sm-10">
							<select name="status" id="status" class="form-control" ng-mode="status">
								<option value="0">有效</option>
								<option value="1">无效</option>
							</select>
                            <p class="help-block">状态</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="modiPrivilegeRole"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用角色权限修改接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.parentRoleId='0';
        $scope.method=0;
        $scope.roleLevel=0;
        $scope.roleType=1;
        $scope.status=0;
        $scope.visible=false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var privilegeRoleId=$("#privilegeRoleId").val();
		var appId=$("#appId").val();
		var method=$("#method").val();
		var roleName=$("#roleName").val();
		var rolePrivilege=$("#rolePrivilege").val();
		var privilegeFunId=$("#privilegeFunId").val();
	    var groupId=$("#groupId").val();
	    var groupName=$("#groupName").val();
	    var deptId=$("#deptId").val();
	    var deptName=$("#deptName").val();
	    var roleLevel=$("#roleLevel").val();
	    var roleType=$("#roleType").val();
	    var parentRoleId=$("#parentRoleId").val();
	    var remark=$("#remark").val();
	    var createUser=$("#createUser").val();
	    var createUserId=$("#createUserId").val();
	    var status=$("#status").val();
	    var modiPrivilegeRoleUri=$("#modiPrivilegeRoleUri").val();
		if(appId=='' || privilegeRoleId=='' || method==''){
		    alert("请输入必传参数");
			return;
		}
		
		$.post("${contextPath}/getSignature",
			{
				appId:appId
			},
			function(data){
				if(data.flag){
				    var signature=data.signature;
				    var timestamp=data.timestamp;
				    var signatureNonce=data.signatureNonce; 				    
					var uri=modiPrivilegeRoleUri+"?"+"privilegeRoleId="+privilegeRoleId+"&appId="+appId+"&method="+method+"&roleName="+roleName
							+"&privilegeFunId="+privilegeFunId+"&rolePrivilege="+rolePrivilege+"&groupId="+groupId+"&groupName="+groupName
							+"&deptId="+deptId+"&deptName="+deptName+"&roleLevel="+roleLevel+"&roleType="+roleType+"&parentRoleId="+parentRoleId
							+"&remark="+remark+"&createUser="+createUser+"&createUserId="+createUserId+"&status="+status
							+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#modiPrivilegeRole").html(uri);
				}else{
				    jQuery("#modiPrivilegeRole").html('无效数据，请重新申请');
				}
			}
 		); 
		
		
	}
</script>

</body>
</html>