<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>get-privilege-role(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">i_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="getPrivilegeRole" method="post" class="form-horizontal">
                <input type="hidden" name="getPrivilegeRoleUri" id="getPrivilegeRoleUri" value="${getPrivilegeRoleUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">                
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" id="appId"
                                   class="form-control" ng-model="appId"/>

                            <p class="help-block">应用Id（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start</label>

                        <div class="col-sm-10">
                            <input type="text" name="start" id="start"
                                   class="form-control" ng-model="start"/>

                            <p class="help-block">开始记录数，分页查询（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">limit</label>

                        <div class="col-sm-10">
                            <input type="text" name="limit" id="limit"
                                   class="form-control" ng-model="limit"/>

                            <p class="help-block">每页记录数，分页查询（必传）</p>
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

                            <p class="help-block">组织机构id</p>
                        </div>
                    </div>
                	<div class="form-group">
                        <label class="col-sm-2 control-label">privilegeRoleId</label>

                        <div class="col-sm-10">
                            <input type="text" name="privilegeRoleId" id="privilegeRoleId"
                                   class="form-control" ng-model="privilegeRoleId"/>

                            <p class="help-block">角色Id[多个角色查询用","分开]（必传）</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="getPrivilegeRole"></div> 
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
		var start=$("#start").val();
		var limit=$("#limit").val();
	    var groupId=$("#groupId").val();
	    var deptId=$("#deptId").val();
	    var getPrivilegeRoleUri=$("#getPrivilegeRoleUri").val();
		if(appId=='' || start=='' || limit==''){
		    alert("请输入必传参数");
			return;
		}
		var uri=getPrivilegeRoleUri+"?"+"appId="+appId+"&start="+start+"&limit="+limit+"&groupId="+groupId+"&deptId="+deptId+"&privilegeRoleId="+privilegeRoleId;
		$("#getPrivilegeRole").html(uri);
	}
</script>

</body>
</html>