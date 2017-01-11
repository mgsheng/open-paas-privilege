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
					<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="ok" onclick="selectAll();">全选</a> 
					<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="ok" onclick="cleanSelectAll();">取消全选</a> 
				</span>
			</div>

			<div id="nav" class="easyui-accordion" fit="true" border="false" >
				<!--  导航内容 -->

			</div>


			<!-- <table id="AuthorizeRole" class="easyui-datagrid" fit="true" title="用户功能" data-options="toolbar:'#tbRole'">
				</table> -->
		</div>
	</div>
</body>
<script>
	function InitLeftMenu(_menus) {
		console.log(_menus);
		$("#nav").accordion({
			animate : false
		});

		$.each(_menus.menus, function(i, n) {
			var menulist = '<div class="panel" style="width: 563px;">'+
			'<div class="panel-header accordion-header"  style="height: 16px;">'+
			'<div class="panel-title panel-with-icon" onclick="selectThis(this);">'+n.resourceName+'</div>'+
			'<div class="panel-icon icon undefined"><input id='+n.resourceId+' name="resource" type="checkbox" onclick="check1(this);"/></div>'+
			'<div class="panel-tool"><a href="javascript:void(0)" class="panel-tool-collapse" style="display: none;"></a>'+
			'<a href="javascript:void(0)" class="accordion-collapse"></a></div></div>'+
			'<div title="" class="panel-body accordion-body" style="display: none; width: 563px; ">';
			if(n.menus != undefined){
				menulist += '<ul>';
				$.each(n.menus, function(j, o) {
					menulist += '<li style="list-style:none"><div><input id='+o.functionId+' name="function" type="checkbox" onclick="check2(this);"/><span class="nav">' + o.functionId+ '</span></div></li> ';
				});
				menulist += '</ul>';
			}
			menulist += '</div></div>';
			/* $('#nav').accordion('add', {
				title : n.resourceName,
				content : menulist,
			}); */
			$("#nav").append(menulist);

		});

		
	}
	//加载授权角色
	$(function() {
		var data = ${menus};
		if (data.status == "0") {
			alert(data.errMsg);
		} else {
			InitLeftMenu(data);
			selected();
		}

		
	});
	function check2(obj){
		var  boo=$(obj).prop("checked");
		var box=$(obj).closest('ul').find('input[name="function"]');
		$.each(box, function(index, item){
			if($(item).prop("checked")){
				boo=true;
			}
		});
		$(obj).closest(".panel").find('input[name="resource"]').prop("checked", boo);  
	}
	function check1(obj){
		var  boo=$(obj).prop("checked");
		$(obj).parent().parent().next().find('input[name="function"]').prop("checked",boo);
	}
	//展开面板
	function selectThis(obj){
		var show=$(obj).parent().next().css('display');
		$(obj).parent().next().css('display',show=='none'?'block':'none');
	}
	function selected(){
		$.post('${pageContext.request.contextPath}/managerUser/function?id='+'${id}',function(data){
			if(data.resourceList!=null){
				$.each(data.resourceList, function(i, n) {
					$("#nav").find("#"+n.resourceId).attr("checked", true);
				});
			}
			if(data.functionList!=null){
				$.each(data.functionList, function(i, n) {
					$("#nav").find("#"+n.functionId).attr("checked", true);
				});
			}
			
		},"json");
	}	
	//提示信息
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
	//取消全选
	function cleanSelectAll(){
		$("#nav").find('input[type="checkbox"]').prop("checked",false);
	}
	//全选
	function selectAll(){
		$("#nav").find('input[type="checkbox"]').prop("checked",true);
	}
	//授权确认
	function submitAuthorizeFun(){
		var res=$("input[name='resource']:checked");
		var fun=$("input[name='function']:checked");
		var resId = [];
		$.each(res, function(index, item){
			resId.push(item.id);
		 });  
		resId.join(",");
		if(resId==""){
			resId = null;
		}
		var funId = [];
		$.each(fun, function(index, item){
			funId.push(item.id);
		});  
		funId.join(",");
		if(funId==""){
			funId = null;
		}
		 var url='${pageContext.request.contextPath}/managerUser/authorizeFun?id='+${id}+'&resource='+resId+'&function='+funId+'&userName=${userName}';
         $.post(url, function(data) {
             if(data.result==true){
              	msgShow('系统提示', '恭喜，授权功能成功！', 'info');
              	//刷新
              	//selected();
             }else{
               	msgShow('系统提示', '授权功能失败！', 'error');
               	//刷新
               	$("input[type='checkbox']:checked").attr("checked",false);
            	selected();
             }
         });
	}
	
</script>
</html>