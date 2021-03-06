<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_function_add(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="addFunction" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeFunctionAddUrl" id="privilegeFunctionAddUrl" value="${privilegeFunctionAddUrl}"/>
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
                        <label class="col-sm-2 control-label">resourceId</label>

                        <div class="col-sm-10">
                            <input type="text" name="resourceId" id="resourceId"
                                   class="form-control" ng-model="resourceId"/>

                            <p class="help-block">资源id（必传）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">operationId</label>

                        <div class="col-sm-10">
                            <input type="text" name="operationId" id="operationId"
                                   class="form-control" ng-model="operationId"/>

                            <p class="help-block">功能id（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">optUrl</label>

                        <div class="col-sm-10">
                            <input type="text" name="optUrl" id="optUrl"
                                   class="form-control" ng-model="optUrl"/>

                            <p class="help-block">功能url地址</p>
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
                        <label class="col-sm-2 control-label">createUserid</label>

                        <div class="col-sm-10">
                            <input type="text" name="createUserId" id="createUserId"
                                   class="form-control" ng-model="createUserid"/>

                            <p class="help-block">操作人用户id</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="addPrivilegeFunction"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用权限功能添加接口</button>
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
		var resourceId=$("#resourceId").val();
	    var operationId=$("#operationId").val();
	    var optUrl=$("#optUrl").val();
	    var createUser=$("#createUser").val();
	    var createUserId=$("#createUserId").val();
	    var privilegeFunctionAddUrl=$("#privilegeFunctionAddUrl").val();
		if(appId=='' || resourceId==''|| operationId==''){
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
					var uri=privilegeFunctionAddUrl+"?"+"appId="+appId+"&resourceId="+resourceId+"&operationId="+operationId+"&optUrl="+optUrl
							+"&createUser="+createUser+"&createUserid="+createUserId
							+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce+"&appKey="+appKey;
					$("#addPrivilegeFunction").html(uri);
				}else{
				    jQuery("#addPrivilegeFunction").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>