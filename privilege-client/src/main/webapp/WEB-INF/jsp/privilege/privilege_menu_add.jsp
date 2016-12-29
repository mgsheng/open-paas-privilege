<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>privilege_menu_add(interface)</title>
    <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="<%=request.getContextPath()%>/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="addMenu" method="post" class="form-horizontal">
                <input type="hidden" name="privilegeMenuAddUrl" id="privilegeMenuAddUrl" value="${privilegeMenuAddUrl}"/>
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
                        <label class="col-sm-2 control-label">menuName</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuName" id="menuName"
                                   class="form-control" ng-model="menuName"/>

                            <p class="help-block">菜单名称（必传）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">menuCode</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuCode" id="menuCode"
                                   class="form-control" ng-model="menuCode"/>

                            <p class="help-block">菜单关键字</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">menuRule</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuRule" id="menuRule"
                                   class="form-control" ng-model="menuRule"/>

                            <p class="help-block">菜单自定义规则（业务系统自定义参数）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">menuLevel</label>

                        <div class="col-sm-10">
                            <input type="text" name="menuLevel" id="menuLevel"
                                   class="form-control" ng-model="menuLevel"/>

                            <p class="help-block">菜单层级（数字1：一级菜单，2：二级菜单…）</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">parentId</label>

                        <div class="col-sm-10">
                            <input type="text" name="parentId" id="parentId"
                                   class="form-control" ng-model="parentId"/>

                            <p class="help-block">菜单父id（根节点id为0）</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">dislayOrder</label>

                        <div class="col-sm-10">
                            <input type="text" name="dislayOrder" id="dislayOrder"
                                   class="form-control" ng-model="dislayOrder"/>

                            <p class="help-block">:菜单排序(默认为0)</p>
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

                        <div class="text-primary" id="addPrivilegeMenu"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用菜单添加接口接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
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
		var menuName=$("#menuName").val();
	    var menuCode=$("#menuCode").val();
	    var menuRule=$("#menuRule").val();
	    var menuLevel=$("#menuLevel").val();
	    var parentId=$("#parentId").val();
	    var dislayOrder=$("#dislayOrder").val();
	    var status=$("#status").val();
	    var privilegeMenuAddUrl=$("#privilegeMenuAddUrl").val();
		if(appId=='' || menuName==''){
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
					var uri=privilegeMenuAddUrl+"?"+"appId="+appId+"&menuName="+menuName+"&menuCode="+menuCode+"&menuRule="+menuRule
							"&menuLevel="+menuLevel+"&parentId="+parentId+"&dislayOrder="+dislayOrder+"&status="+status
							+"&appKey="+appKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#addPrivilegeMenu").html(uri);
				}else{
				    jQuery("#addPrivilegeMenu").html('无效数据，请重新申请');
				}
			}
 		); 
	}
</script>

</body>
</html>