<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_resource_modify(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="modifyResource" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeResourceModifyUrl" id="privilegeResourceModifyUrl" value="${privilegeResourceModifyUrl}"/>
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
                        <label class="col-sm-2 control-label">resourceLevel</label>

                        <div class="col-sm-10">
                            <input type="text" name="resourceLevel" id="resourceLevel"
                                   class="form-control" ng-model="resourceLevel"/>

                            <p class="help-block">资源级别（0：基础功能 1：服务级别，2:应用级别）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">resourceName</label>

                        <div class="col-sm-10">
                            <input type="text" name="resourceName" id="resourceName"
                                   class="form-control" ng-model="resourceName"/>

                            <p class="help-block">资源名称</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">menuId</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuId" id="menuId"
                                   class="form-control" ng-model="menuId"/>

                            <p class="help-block">菜单id（资源级别为0时-基础功能时,必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">baseUrl</label>

                        <div class="col-sm-10">
                            <input type="text" name="baseUrl" id="baseUrl"
                                   class="form-control" ng-model="baseUrl"/>

                            <p class="help-block">资源地址</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">resourceRule</label>

                        <div class="col-sm-10">
                            <input type="text" name="resourceRule" id="resourceRule"
                                   class="form-control" ng-model="resourceRule"/>

                            <p class="help-block">资源规则（业务系统可自定义）</p>
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

                        <div class="text-primary" id="modifyPrivilegeResource"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用权限资源修改接口</button>
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
		var resourceLevel=$("#resourceLevel").val();
		var resourceId=$("#resourceId").val();
	    var resourceName=$("#resourceName").val();
	    var menuId=$("#menuId").val();
	    var baseUrl=$("#baseUrl").val();
	    var resourceRule=$("#resourceRule").val();
	    var createUser=$("#createUser").val();
	    var createUserId=$("#createUserId").val();
	    var privilegeResourceAddUrl=$("#privilegeResourceAddUrl").val();
		if(appId=='' || resourceId==''){
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
					var uri=privilegeResourceAddUrl+"?"+"appId="+appId+"&resourceId="+resourceId+"&resourceLevel="+resourceLevel+"&resourceName="+resourceName+"&menuId="+menuId
							"&baseUrl="+baseUrl+"&resourceRule="+resourceRule+"&createUser="+createUser+"&createUserid="+createUserId
							+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#modifyPrivilegeResource").html(uri);
				}else{
				    jQuery("#modifyPrivilegeResource").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>