<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>modi-privilege-user-role(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">i_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="modiPrivilegeUserRole" method="post" class="form-horizontal">
                <input type="hidden" name="modiPrivilegeUserUri" id="modiPrivilegeUserUri" value="${modiPrivilegeUserUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appUserId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appUserId" id="appUserId"
                                   class="form-control" ng-model="appUserId"/>

                            <p class="help-block">应用用户Id（必传）</p>
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
								<option value="0">添加角色</option>
								<option value="1">删除角色</option>
							</select>
                            <p class="help-block">操作：0-添加角色，1-删除角色（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">  
                        <label class="col-sm-2 control-label">privilegeRoleId</label>

                        <div class="col-sm-10">
                            <input type="text" name="privilegeRoleId" size="50" id="privilegeRoleId" class="form-control"
                                   ng-model="privilegeRoleId"/>

                            <p class="help-block">角色Id（多个角色可用“,”分隔）</p>
                        </div>
                    </div>
                    <div class="form-group">  
                        <label class="col-sm-2 control-label">resourceId</label>

                        <div class="col-sm-10">
                            <input type="text" name="resourceId" size="50" id="resourceId" class="form-control"
                                   ng-model="resourceId"/>

                            <p class="help-block">所拥有的资源（多个资源用“,”分割）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">privilegeFunId</label>

                        <div class="col-sm-10">
                            <input type="text" name="privilegeFunId" id="privilegeFunId"
                                   class="form-control" ng-model="privilegeFunId"/>

                            <p class="help-block">权限功能Id（多个权限用“,”分割）</p>
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
                        <label class="col-sm-2 control-label">groupId</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupId" id="groupId"
                                   class="form-control" ng-model="groupId"/>

                            <p class="help-block">组织机构Id</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">createUser</label>

                        <div class="col-sm-10">
                            <input type="text" name="createUser" id="createUser"
                                   class="form-control" ng-model="createUser"/>

                            <p class="help-block">操作人用户名</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">createUserId</label>

                        <div class="col-sm-10">
                            <input type="text" name="createUserId" id="createUserId"
                                   class="form-control" ng-model="createUserId"/>

                            <p class="help-block">操作人用户Id</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="modiPrivilegeUser"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用用户角色权限修改接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.visible=false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var appUserId=$("#appUserId").val();
		var appId=$("#appId").val();
		var method=$("#method").val();
		var privilegeRoleId=$("#privilegeRoleId").val();	
	    var resourceId=$("#resourceId").val();
		var privilegeFunId=$("#privilegeFunId").val();
	    var deptId=$("#deptId").val();
	    var groupId=$("#groupId").val();			
	    var createUser=$("#createUser").val();
	    var createUserId=$("#createUserId").val();
	    var modiPrivilegeUserUri=$("#modiPrivilegeUserUri").val();
		if(appId=='' || appUserId=='' || method==''){
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
				    var appKey=data.appKey;				    
					var uri=modiPrivilegeUserUri+"?"+"appUserId="+appUserId+"&appId="+appId+"&method="+method
							+"&privilegeRoleId="+privilegeRoleId+"&resourceId="+resourceId+"&privilegeFunId="+privilegeFunId
							+"&createUser="+createUser+"&createUserId="+createUserId+"&deptId="+deptId+"&groupId="+groupId
							+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#modiPrivilegeUser").html(uri);
				}else{
				    jQuery("#modiPrivilegeUser").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>