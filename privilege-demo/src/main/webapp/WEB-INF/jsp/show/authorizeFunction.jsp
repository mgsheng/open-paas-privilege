<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>授权角色</title>
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/easyui.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/master.css" rel="stylesheet" type="text/css" /> 
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/icon.css" rel="stylesheet" type="text/css" /> 
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/jquery.insdep-extend.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	<link href="${pageContext.request.contextPath}/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/global/css/iconFont/iconfont.css" rel="stylesheet" type="text/css" />
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
	<div id='loading' style='position:absolute;left:0;width:100%;height:100%;top:0;background:#E0ECFF;opacity:0.8;filter:alpha(opacity=80);'>
			<div style='position:absolute;  cursor1:wait;left:50%;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;border:2px solid #ccc;color:#000;'> 
 				<img alt="" src="${pageContext.request.contextPath}/assets/global/img/input-spinner.gif">正在加载，请等待...
			</div>
	</div>
</body>
<script>
	//修改tree 图标样式
	function getRoot(){
		var node = $('#tt').tree('getRoots');
		$.each(node,function(i,n){
			var children = $('#tt').tree('getChildren',n.target);
			$.each(children,function(i,m){
				if(m.ismodule == "1"){
					var menuRule = 'icon iconfont icon-iconfont-LearningCenter';
					if (m.attributes.menuRule != "") {
						menuRule = m.attributes.menuRule;
					}
					$(m.target).children(".tree-icon").attr("class",menuRule);//removeClass("tree-folder").addClass(menuRule);
				}
			});
		});
	}	
	$('#tt').tree({
			//资源菜单展开时，移除tree自带图标
			onExpand: function(node){
				if (node.ismodule == "1") {
					$(node.target).children('.tree-hit').next().removeClass("tree-folder-open");
				}
			},
			onBeforeExpand:function(node){
				if(node.ismodule == "1"){
					var children = $(node.target).next();
					if (children.length == 0) {
						return false;
					}
				}
			}
		});
	
	function getTree() {
		$('#loading').show();
		$.ajax({type:'GET',
			url:'${pageContext.request.contextPath}/managerUser/tree?appId=${appId}&groupId=${groupId}',
			success:function(data) {
					if (data.status == "0") {
						msgShow('系统提示', '该组织机构无权限！', 'info');
					} else {
						var json = data.tree;
						if (json.length>0) {
							$('#tt').tree({data: json});
							getRoot();
							selected();
						}else {
							msgShow('系统提示', '该组织机构无权限！', 'info');
						}
						
					}
					$('#loading').hide();
				}
		});
	}
	//页面预加载
	$(function(){
		getTree();
	});
 
	   //取消选中
	function cancelAuthorizeRole() {
		getTree();
	}
	//勾选用户拥有的功能
	function selected(){
		$.post('${pageContext.request.contextPath}/managerUser/function?id=${id}&appId=${appId}',function(data){
				if (data.functionIds != null) {
					$.each(data.functionIds,function(i,n){
						var node = $("#tt").tree('find',n);
						if(node != null){
							if (node.ismodule == "2") {
								$("#tt").tree('check',node.target);
							}
						}
					});
				}
				if (data.resourceIds != null) {
					$.each(data.resourceIds,function(i,m){
						var node = $("#tt").tree('find',m);
						if (node != null) {
							if (node.ismodule == "1") {
								var nn = $("#tt").tree('getChildren',node.target);
								if (nn.length<=0) {
									$("#tt").tree('check',node.target);
								}
							}
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
		$('#loading').show();
		var resId = [];
		var funId = [];
		var select = $('#tt').tree('getChecked', 'checked');
		$.each(select, function(i, n) {
			if (n.ismodule == 2) {
				funId.push(n.id);
			} else if (n.ismodule == 1) {
				resId.push(n.id);
			}
		});
		resId.join(",");
		funId.join(",");
		 var url = '${pageContext.request.contextPath}/managerUser/authorizeFun?id=${id}&resource='+resId+'&function='+funId+'&userName=${userName}&appId=${appId}';
         $.post(url, function(data) {
        	 $('#loading').hide();
             if (data.result == true) {
              	msgShow('系统提示', '恭喜，授权功能成功！', 'info');
             } else {
               	msgShow('系统提示', '授权功能失败！', 'error');
               	//刷新
               	getTree();
             }
         }); 
	}
	
</script>
</html>