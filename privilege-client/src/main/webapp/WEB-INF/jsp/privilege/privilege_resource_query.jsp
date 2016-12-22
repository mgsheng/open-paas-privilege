<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_resource_query(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="getResPrivilege" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeResourceQueryUrl" id="privilegeResourceQueryUrl" value="${privilegeResourceQueryUrl}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                 <div class="form-group">
                        <label class="col-sm-2 control-label">menuId</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuId" id="menuId"
                                   class="form-control" ng-model="menuId"/>

                            <p class="help-block">菜单id</p>
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
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" id="appId"
                                   class="form-control" ng-model="appId"/>

                            <p class="help-block">应用Id（必传）</p>
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
                        <label class="col-sm-2 control-label">start</label>

                        <div class="col-sm-10">
                            <input type="text" name="start" id="start"
                                   class="form-control" ng-model="start"/>

                            <p class="help-block">开始记录数，分页查询（必传）</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="queryPrivilegeResource"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用菜单查询接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.roleLevel=0;
        $scope.status=0;
        $scope.visible=false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var appId=$("#appId").val();
	    var menuId=$("#menuId").val();
	    var resourceLevel=$("#resourceLevel").val();
	    var limit=$("#limit").val();
	    var start=$("#start").val();
	    var privilegeResourceQueryUrl=$("#privilegeResourceQueryUrl").val();
		if(appId=='' || start==''||limit==''){
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
					var uri=privilegeMenuQueryUrl+"?"+"appId="+appId+"&limit="+limit+"&menuId="+menuId+"&start="+start+"&resourceLevel="+resourceLevel
							+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#queryPrivilegeResource").html(uri);
				}else{
				    jQuery("#queryPrivilegeResource").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>