<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	<style type="text/css">
		.txt01{
			width: 230px;
		}
	</style>
</head>
<body>
	<div class="easyui-panel" title="用户常用菜单管理" style="width:100%;max-width:100%;padding:20px 30px;height:540px;">
		<div style="padding:2px 5px; text-align: right;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reload" onclick="reload()">reload</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="ok">确认</a>
		</div>
		<div class="easyui-panel" style="padding:5px;height: 95%;overflow-x:scroll;">
		  	<ul id="deptree"  style="height: 100%"class="easyui-tree"
				data-options="method:'get',animate:true,checkbox:true,lines:true,cascadeCheck:true" > 
	 		</ul>
		</div>
	</div>
	<div id='loading' style='position:absolute;left:0;width:100%;height:100%;top:0;background:#E0ECFF;opacity:0.8;filter:alpha(opacity=80);'>
			<div style='position:absolute;  cursor1:wait;left:50%;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;border:2px solid #ccc;color:#000;'> 
 				正在加载，请等待...
			</div>
	</div> 
</body>
<script>
	//遍历树，显示图标
	function getRoot(){
		var node=$('#deptree').tree('getRoots');
		$.each(node,function(i,n){
			if(n.attributes.menuRule!=""){
				$(n.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+n.attributes.menuRule+")");
			}
			var children=$('#deptree').tree('getChildren',n.target);
			$.each(children,function(i,m){
				if(m.attributes.menuRule!=""){
					$(m.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+m.attributes.menuRule+")");
				}else{
						if(m.ismodule=="1"){
							$(m.target).children(".tree-icon").addClass("icon icon-mini-add");
						}
					}
			});
		});
	}
	
	//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
        
      
     //确认添加常用菜单
	$('#ok').click(function() {
		//资源池菜单id
		var resId=[];
		var select=$('#deptree').tree('getChecked', 'checked');
		$.each(select, function(i, n) {
			if(n.ismodule==1){
				resId.push(n.id);
			}
		});
		resId.join(",");
		$.ajax(
			{type:'POST',
			url:'${pageContext.request.contextPath}/user/saveMenu?appId=${appId}&appUserId=${appUserId}&resId='+resId,
			success:function(data) {
					if(data.status=="0"){
						msgShow('系统提示', '设置失败！', 'info');
					}else {
						msgShow('系统提示', '设置成功！', 'info');
					}
				}	
		});
    });
	function getTree() {
		$('#loading').show();  
		$.ajax({type:'GET',
			url:'${pageContext.request.contextPath}/user/tree',
			data:{
				appId:'${appId}',
				appUserId:'${appUserId}'
				},
			success:function(data) {
					if(data.status=="0"){
						msgShow('系统提示', '您没有菜单！', 'info');
					}else {
						var json=data.tree;
						if (json.length>0) {
							$('#deptree').tree({data: json});
							getRoot();
							selected();
							$('#loading').hide();  
						}else {
							msgShow('系统提示', '您没有菜单！', 'info');
						}
						
					}
				}	
		});
	}
  	$(function(){  
  		getTree();
	});
	//重新加载树	 
	function reload(){
		getTree();
	} 
	//勾选用户常用菜单
	function selected(){
		$.post('${pageContext.request.contextPath}/user/getFrequentlyMenu?appUserId=${appUserId}',function(data){
				if(data.resourceId!=null){
					$.each(data.resourceId,function(i,m){
						var node=$("#deptree").tree('find',m);
						if(node!=null){
							if(node.ismodule=="1"){
								$("#deptree").tree('check',node.target);
							}
						}
					});
				}
			},"json");
	}
	
</script>
</html>