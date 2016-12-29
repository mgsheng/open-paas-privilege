<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_group_modify(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="modifyPrivilege" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeGroupModifyUrl" id="privilegeGroupModifyUrl" value="${privilegeGroupModifyUrl}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                 <div class="form-group">
                        <label class="col-sm-2 control-label">groupId</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupId" id="groupId"
                                   class="form-control" ng-model="groupId"/>

                            <p class="help-block">组织机构id（必传）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">groupName</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupName" id="groupName"
                                   class="form-control" ng-model="groupName"/>

                            <p class="help-block">组织机构名称（必传）</p>
                        </div>
                    </div>
                       <div class="form-group">
                        <label class="col-sm-2 control-label">method</label>

                        <div class="col-sm-10">
							<select name="method" id="method" class="form-control" ng-mode="method">
								<option value="0">添加</option>
								<option value="1">删除</option>
							</select>
                            <p class="help-block">操作（必传）</p>
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
                        <label class="col-sm-2 control-label">groupPrivilege</label>

                        <div class="col-sm-10">
                            <input type="text" name="groupPrivilege" id="groupPrivilege"
                                   class="form-control" ng-model="groupPrivilege"/>

                            <p class="help-block">所拥有权限（多个权限用“，”分隔）</p>
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

                        <div class="text-primary" id="addPrivilegeRole"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用组织机构权限授权接口</button>
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
		var appId=$("#appId").val();
		var groupPrivilege=$("#groupPrivilege").val();
	    var groupId=$("#groupId").val();
	    var groupName=$("#groupName").val();
	    var createUser=$("#createUser").val();
	    var createUserId=$("#createUserId").val();
	    var status=$("#status").val();
	    var privilegeGroupModifyUrl=$("#privilegeGroupModifyUrl").val();
		if(appId=='' || groupName==''||groupId==''){
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
					var uri=privilegeGroupModifyUrl+"?"+"appId="+appId+"&groupPrivilege="+groupPrivilege+"&groupId="+groupId+"&groupName="+groupName
							"&createUser="+createUser+"&createUserid="+createUserid+"&status="+status
							+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#addPrivilegeRole").html(uri);
				}else{
				    jQuery("#addPrivilegeRole").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>