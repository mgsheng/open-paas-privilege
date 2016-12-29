<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_menu_del(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="delMenu" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeMenuDelUrl" id="privilegeMenuDelUrl" value="${privilegeMenuDelUrl}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                 <div class="form-group">
                        <label class="col-sm-2 control-label">menuId</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuId" id="menuId"
                                   class="form-control" ng-model="menuId"/>
                            <p class="help-block">菜单id（必传）</p>
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
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 privilege-service的 URL:</span>
                        <br/>

                        <div class="text-primary" id="delPrivilegeMenu"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">组织菜单删除接口接口</button>
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
	    var menuId=$("#menuId").val();
	    var privilegeMenuDelUrl=$("#privilegeMenuDelUrl").val();
		if(appId==''||menuId==''){
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
					var uri=privilegeMenuDelUrl+"?"+"appId="+appId+"&menuId="+menuId
							+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#delPrivilegeMenu").html(uri);
				}else{
				    jQuery("#delPrivilegeMenu").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>