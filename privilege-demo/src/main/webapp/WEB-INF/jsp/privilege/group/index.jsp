<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>OES机构列表</title>
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
			<div id="tt" class="easyui-tabs" fit="true" style="font-size:1em;">
				<div title="Oes机构信息">
					<div style="border:0 solid;margin-bottom:0;" fit="true" >
						<div class="top" style="width: 100%">
							<div class="easyui-panel" title="操作" style="padding-top:1%;" fit="true" >
								<form id="fm" method="post" action="/managerUser/findUsers">
									<div style="width:100%;padding:0.8%;">
										<%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openAddWin();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">添加机构</span>
											<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>--%>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinRes();" style="margin-left:2%;padding-bottom:0.6%;display:inline; ">
											<span style="font-weight:bold;">授权资源</span>
											<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="botton" style="margin-top:0px;width:100%;height:68%">
						<table  id="dg"  class="easyui-datagrid" title="查询结果"  style="width:100%;max-width:100%;padding:20px 30px;"
							data-options="singleSelect:true,method:'get'" fit="true">
							<thead>
								<tr>
									<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">机构ID</th>
									<th data-options="field:'groupCode',align:'center'" style="width:15%;max-width:100%;">机构编码</th> 
									<th data-options="field:'groupTypeName',align:'center'" style="width:15%;max-width:100%;">机构类型名称</th> 
									<th data-options="field:'groupName',align:'center'" style="width:15%;max-width:100%;">机构名称</th> 
									<th data-options="field:'groupType',align:'center'" style="width:15%;max-width:100%;">机构类型</th>
							 </tr>
						</thead>
					</table>
				</div>
			</div>
		</div>	
	<!-- 添加机构窗口 -->
	<div id="addWin" class="easyui-window" title="添加机构" style="width:37%;padding:30px 30px;background: #fafafa;height:350px;"
		minimizable="false" maximizable="false" icon="icon-add">
		<form id="ff" class="easyui-form" method="post" style="margin:15px 30px;width:90%" data-options="novalidate:true">
			<table>
				<tr>
					<td > 
						机构编码:
					</td>
					<td style="width:75%;">	
						<input id="groupCode" class="easyui-textbox" name="groupCode" type="text" style="width:100%;height:35px;padding:5px;" required=true/>
					</td>
				</tr>
				<tr>
					<td>
						机构类型名称：
					</td>
					<td style="width:75%;">	
						<input id="groupTypeName" class="easyui-textbox" name="groupTypeName" type="text"  style="width:100%;height:35px;padding:5px;" required=true/>
					</td>
				</tr>
				<tr>
					<td>
						机构名称:
					</td>
					<td style="width:75%;">	
						<input id="groupName" class="easyui-textbox" name="groupName" type="text"  style="width:100%;height:35px;padding:5px;" required=true/>
					</td>
				</tr>
				<tr>
					<td>
						机构类型:
					</td>
					<td style="width:80%;">	
						<input id="groupType" class="easyui-textbox" type="text"  name="groupType" style="width:100%;height:35px;padding:5px;"/>
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitAddForm()" style="width:80px;">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearAddForm()" style="width:80px;">清空</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeAddWin()" style="width:80px;">取消</a>
		</div>
	</div>
</body>
	<script>
		//打开授权资源窗口
		function openWinRes() {
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定授权?', function(r){
					if(r){
						var groupId = row.id;
						var groupName=row.groupName;
						addPanel2(groupName,groupId);
					}
				});
			}else{
            	msgShow('系统提示', '请选择机构！', 'warning');
            }
		}
		
		//添加tab页面
		function addPanel2(groupName,groupId){
			if ($('#tt').tabs('exists', groupName)){
			 	$('#tt').tabs('select', groupName);
			} else {
				 var url = '${pageContext.request.contextPath}/oesGroup/toRes?groupId='+groupId+'&groupName='+groupName+'&appId='+${appId};
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:groupName+'-授权资源',
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}
		
		//添加用户窗口
		function addWin(){
			 $('#addWin').window({
                title: '添加机构',
                modal: true,
                shadow: true,
                closed: true,
                resizable:false
            });
		}
		
		//打开添加用户窗口
		addWin();
		function openAddWin(){
			clearAddForm();
			$('#addWin').window('open');
			$('#addDeptName').combobox({
				url:'${pageContext.request.contextPath}/managerUser/findAllDepts',
				valueField:'id',
				textField:'text'
			});
		}
		
		//关闭添加用户窗口
		function closeAddWin(){
			$('#addWin').window('close');
		}
		
		// 清除添加用户表单
		function clearAddForm(){
			$('#ff').form('clear');
		}
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
		// 查询用户方法
		function findUsers(){
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/oesGroup/getGroupList?appId=${appId}",  
		        pagination: true,//显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  }
                }
		    }); 
		    
			 //设置分页控件 
		    var p = $('#dg').datagrid('getPager'); 
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
		}
		
		// 提交（用户信息）
		function submitAddForm(){
			var groupCode = $.trim($('#groupCode').val()) ;
			var groupTypeName = $.trim($('#groupTypeName').val());
			var groupName = $.trim($('#groupName').val()) ;
			var groupType = $.trim($('#groupType').val()) ;
			$('#ff').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			
			if(groupCode == "" || groupTypeName == "" || groupName == ""){
				alert("请输入必填项");
				return;
			}
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/oesGroup/addGroup",
				data:{"groupCode":groupCode,"groupTypeName":groupTypeName,"groupName":groupName,"groupType":groupType},
				dataType:"json",
				success:function (data){
					if(data.result == "0"){
						$.messager.alert("系统提示","恭喜，添加机构成功!","info");
						closeAddWin();
						window.location.reload();
					}else if(data.result == "1"){
						clearAddForm();
						$.messager.alert("系统提示","该机构编码已被注册，请重新填写机构编码!","error");	
					}else{
						clearAddForm();
						$.messager.alert("系统提示","添加机构失败，请重新添加!","error");
					}
				},
				error:function(){
					$.messager.alert("系统提示","机构添加异常，请刷新页面!","error");
				}
			});
		}
		
		//页面预加载
		$(function(){
			findUsers();
		});
	</script>
</html>