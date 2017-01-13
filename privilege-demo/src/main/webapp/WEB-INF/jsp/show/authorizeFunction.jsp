<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>授权角色</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/dataList.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<!-- 授权角色窗口 -->
	<div class="easyui-pannel">
		<div id="authorizeFunWin" class="botton"
			style="margin-top: 0; width: 50%; height: 1000px; border: 1px;">
			<div id="tbFun" style="padding: 10px 10px;">
				<span style="text-align: left;" hidden="true"> <input
					class="easyui-textbox" id="${id}" name="${userName}" hidden="true">
				</span> &nbsp;&nbsp;&nbsp;&nbsp; <span style="float: right;"> <a
					href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true"
					id="ok" onclick="submitAuthorizeFun();">确认</a> <a href="#"
					class="easyui-linkbutton" iconCls="icon-undo" plain="true"
					id="undo" onclick="cancelAuthorizeRole();">取消</a>
					
				</span>
			</div>
			
			<ul id="tt" class="easyui-tree" style="height: 100%" data-options="
				method:'get',animate:true,checkbox:true,lines:true,cascadeCheck:true"></ul>
			
		</div>
	</div>
</body>
<script>
	$('#tt').tree({ 
 	  url: '${pageContext.request.contextPath}/managerUser/tree', 
      onLoadSuccess:function(node,data){
    	 selected();
      }
   });
	//勾选用户拥有的功能
	function selected(){
		$.post('${pageContext.request.contextPath}/managerUser/function?id='+'${id}',function(data){
				if(data.functionList!=null){
					$.each(data.functionList,function(i,n){
						var node=$("#tt").tree('find',n.functionId);
						$("#tt").tree('check',node.target);
					});
				}
				if(data.resourceList!=null){
					$.each(data.resourceList,function(i,m){
						var node=$("#tt").tree('find',m.resourceId);
						var nn=$("#tt").tree('getChildren',node.target);
						if(nn.length<=0){
							$("#tt").tree('check',node.target);
						}
					});
				}
		 
			},"json");
	}
		
	//提示信息
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
	
	
	//授权确认
	function submitAuthorizeFun(){
		var resId=[];
		var funId=[];
		var select=$('#tt').tree('getChecked', ['checked','indeterminate']);
		$.each(select, function(i, n) {
			if(n.ismodule==2){
				funId.push(n.id);
			}else{
				resId.push(n.id);
			}
			console.log(n.text);
		});
		resId.join(",");
		funId.join(",");
		 var url='${pageContext.request.contextPath}/managerUser/authorizeFun?id='+${id}+'&resource='+resId+'&function='+funId+'&userName=${userName}';
         $.post(url, function(data) {
             if(data.result==true){
              	msgShow('系统提示', '恭喜，授权功能成功！', 'info');
              	//刷新
              	//selected();
             }else{
               	msgShow('系统提示', '授权功能失败！', 'error');
               	//刷新
               	$('#tt').tree('reload');
             }
         }); 
	}
	
</script>
</html>