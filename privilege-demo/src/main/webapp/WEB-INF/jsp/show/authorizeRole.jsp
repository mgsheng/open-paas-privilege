<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>授权角色</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	</head>
	<body >
		<!-- 授权角色窗口 -->
		<div class="easyui-pannel">
			<div id="authorizeRoleWin" class="botton" style="margin-top:0;width:100%;height:500px;border:1px;" >
				<div id="tbRole" style="padding:10px 10px;">
					<span style="text-align:left;" hidden="true" >
						<input class="easyui-textbox" id="${id}" name="${userName}" hidden="true">
					</span>
						&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="float:right;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="ok" onclick="submitAuthorizeRole();">确认</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" id="undo" onclick="cancelAuthorizeRole();">取消</a>
					</span>
				</div>
				<table id="AuthorizeRole" class="easyui-datagrid" fit="true" title="用户权限" data-options="toolbar:'#tbRole'">
				</table>
			</div>
		</div>
		<div id='loading' style='position:absolute;left:0;width:100%;height:100%;top:0;background:#E0ECFF;opacity:0.8;filter:alpha(opacity=80);'>
			<div style='position:absolute;  cursor1:wait;left:50%;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;border:2px solid #ccc;color:#000;'> 
 				正在加载，请等待...
			</div>
		</div>
	</body>
	<script>
		//取消勾选的ids
		var unCheckRoleIds=[];
		//勾选的ids
		var onCheckRoleIds=[];
		//加载授权角色
		$(function(){
			 	$('#loading').hide();
				$('#AuthorizeRole').datagrid({
					url: '${pageContext.request.contextPath}/managerUser/role?id=${id}&appId=${appId}',
					type:'post',
					collapsible:true,
					rownumbers:true,
					pagination:true,
		        	pagination: true,//显示分页工具栏
					fit:true,
					nowrap: false, //true 就会把数据显示在一行里
            		striped: true, //true奇偶行使用不同背景色
            		fitColumns: true, //true 真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
					rownumbers: true,
					singleSelect: false,
					selectOnCheck: true,
					checkOnSelect: true,
					columns:[[{ field:'ck',checkbox:true,sortable:true},
									{ field: 'privilegeRoleId', title: 'ID',align:'center',sortable:true,width:'20%'},
									{ field: 'roleName', title: '角色名称',align:'center',sortable:true,width:'20%'},
									{ field: 'status', title: '状态',align:'center',sortable:true,width:'20%',
										formatter : function(value, row, index) {  
						                    if(value==0){  
						                     	return '<span title="启用"><font color=green>启用</font></span>';     
						                    }else{  
						                     	return '<span title="禁用"><font color=gray>禁用</font></span>';     
						                    }                     
						                }}
									]], 
					onLoadSuccess:function(data){
						if (data.total<1){
                       		$.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  		}         
						if(data){
							$.each(data.rows, function(index, item){
								if(item.checked){
									$('#AuthorizeRole').datagrid('checkRow', index);
								}
							});
						} 
					 	var rows = $("#AuthorizeRole").datagrid("getRows");
						if(onCheckRoleIds!=null){
								$.each(onCheckRoleIds,function(i){
										$.each(rows,function(n){
												if(onCheckRoleIds[i]==rows[n].id){
														$('#AuthorizeRole').datagrid('checkRow', n);
													}
											});
									});
							} 
					},
					onUncheck: function(rowIndex,rowData){
						unCheckRoleIds.push(rowData.privilegeRoleId);
						$.each(onCheckRoleIds,function(i){
								if(rowData.privilegeRoleId==onCheckRoleIds[i]){
									onCheckRoleIds.splice(i,1); 
								}
							}); 
			    	},
			    	onCheck: function(rowIndex,rowData){
			    	 	onCheckRoleIds.push(rowData.privilegeRoleId);
			    		$.each(unCheckRoleIds,function(i){
							if(rowData.privilegeRoleId==unCheckRoleIds[i]){
								unCheckRoleIds.splice(i,1); 
							}
						}); 
			    	}               
				});
		    
			 //设置分页控件 
		    var p = $('#AuthorizeRole').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 15,//每页显示的记录条数，默认为10 
		        pageList: [5,10,15,20],//可以设置每页记录条数的列表 
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
		        onBeforeRefresh:function(){
		            $(this).pagination('loading');
		            $(this).pagination('loaded');
		        } 
		    }); 

		});
		//授权角色修改确认
		function submitAuthorizeRole(){
			 $('#loading').show();
			 unCheckRoleIds.join(",");
			 if(unCheckRoleIds==""){
				 unCheckRoleIds = null;
				}
			 onCheckRoleIds.join(",");
			 if(onCheckRoleIds==""){
				 onCheckRoleIds = null;
				}
			 var url='${pageContext.request.contextPath}/managerUser/authorizeRole?id=${id}&addRoleId='+onCheckRoleIds+'&delRoleId='+unCheckRoleIds+'&userName=${userName}&appId=${appId}';
            $.post(url, function(data) {
            	unCheckRoleIds=[];
                onCheckRoleIds=[];
                $('#loading').hide();
                if(data.result==true){
                 	msgShow('系统提示', '恭喜，授权角色成功！', 'info');
                 	//刷新
                 	var url='${pageContext.request.contextPath}/managerUser/role?id=${id}&userName=${userName}&appId=${appId}';
                 	reload(url,name);
                }else{
                  	msgShow('系统提示', '授权角色失败！', 'error');
                  	//刷新
                 	var url='${pageContext.request.contextPath}/managerUser/role?id=${id}&userName=${userName}&appId=${appId}';
                 	reload(url,name);
                }
                
            });
		};
		
		//授权角色窗口取消按钮
		function cancelAuthorizeRole(){
			unCheckRoleIds=[];
            onCheckRoleIds=[];
			var url='${pageContext.request.contextPath}/managerUser/role?id=${id}&appId=${appId}';
       		reload(url,name);
		}
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
		//列表重新加载
		function reload(url,name){
			$('#AuthorizeRole').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		
	</script>
</html>