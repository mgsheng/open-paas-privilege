<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>verify-privilege-user-role(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">i_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="verifyPrivilegeUserRole" method="post" class="form-horizontal">
                <input type="hidden" name="verifyPrivilegeUserUri" id="verifyPrivilegeUserUri" value="${verifyPrivilegeUserUri}"/>
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
                        <label class="col-sm-2 control-label">optUrl</label>

                        <div class="col-sm-10">
                            <input type="text" name="optUrl" id="optUrl"
                                   class="form-control" ng-model="optUrl"/>

                            <p class="help-block">操作功能Url地址（必传）</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="verifyPrivilegeUser"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用用户权限认证接口</button>
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
		var optUrl=$("#optUrl").val();
	    var verifyPrivilegeUserUri=$("#verifyPrivilegeUserUri").val();
		if(appId=='' || appUserId=='' || optUrl==''){
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
					var uri=verifyPrivilegeUserUri+"?"+"appUserId="+appUserId+"&appId="+appId+"&optUrl="+optUrl
							+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#verifyPrivilegeUser").html(uri);
				}else{
				    jQuery("#verifyPrivilegeUser").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>