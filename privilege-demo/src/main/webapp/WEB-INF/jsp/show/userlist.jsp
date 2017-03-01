<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>用户信息列表</title>
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
				<div title="用户信息">
					<div style="border:0 solid;margin-bottom:0;" fit="true" >
						<div class="top" style="width: 100%">
							<div class="easyui-panel" title="操作" style="padding-top:1%;" fit="true" >
								<form id="fm" method="post" action="/managerUser/findUsers">
									<table cellpadding="5%"  style="margin-left:4%;">
										<tr style="width:100%;">
											<td>
													<input class="easyui-textbox" name="username" id="un" prompt="选填" style="width:100%;" label="用&nbsp;户&nbsp;名:"></input> 
											</td>
											<!-- <td>
												机&nbsp;&nbsp;&nbsp;&nbsp;构：&nbsp;<select class="easyui-combobox" data-options="editable:false"  id="groupName" name="groupName" style="width:280px;height:24px;padding:5px;">
												</select>
											</td> -->
											<td>
												<input id="cc" class="easyui-combobox" name="dept" data-options="valueField:'groupCode',textField:'groupName'" label="机&nbsp;&nbsp;构:" style="width:280px;height:24px;padding:5px;">
											</td>
										</tr>
										
									</table>
									<div style="width:100%;padding:0.8%;">
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="findUsers();" style="margin-left:3.5%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">查&nbsp;&nbsp;&nbsp;&nbsp;询</span>
											<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">清&nbsp;&nbsp;
												&nbsp;&nbsp;除</span>
											<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openAddWin();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">添加用户</span>
											<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="removeUserByID();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">删除用户</span>
											<span class="icon-cut">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateWin();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">修改用户</span>
											<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinRole();" style="margin-left:2%;padding-bottom:0.6%;display:inline; ">
											<span style="font-weight:bold;">授权角色</span>
											<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
										</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinFunction();" style="margin-left:2%;padding-bottom:0.6%;display:inline; ">
											<span style="font-weight:bold;">授权功能</span>
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
									<th data-options="field:'Id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
									<th data-options="field:'userId',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
									<th data-options="field:'groupId',align:'center'" hidden="true" style="width:15%;max-width:100%;">groupId</th>
									<th data-options="field:'userName',align:'center'" style="width:15%;max-width:100%;">用&nbsp;&nbsp;户&nbsp;&nbsp;名</th>
									<th data-options="field:'groupName',align:'center'" style="width:15%;max-width:100%;">所属组织结构</th> 
							 </tr>
						</thead>
					</table>
				</div>
			</div>
		</div>	
	<!--修改用户窗口--> 
	<div id="upda" class="easyui-window" title="用户信息" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="background: #fafafa;">
		<div region="center" border="false" style="background: #fff; border: 1px solid #ccc;">
			<table cellpadding="10px" id="tb"  style="border: 0px;margin:10px 10px" >
				<tr>
					<td>组织机构：</td>
					<td>
						<input id="UserGroup" class="easyui-combobox" name="dept" data-options="valueField:'groupCode',textField:'groupName'"  style="width:280px;height:24px;padding:5px;">
					</td>
				</tr>
			</table>
		</div>
		<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateUser()" style="margin:8px"> 确定</a>
			<a id="btnCancel" class="easyui-linkbutton" onclick="closeWin()" icon="icon-cancel" href="javascript:void(0)" style="margin:8px">取消</a>
		</div>
	</div>
	<!-- 添加用户窗口 -->
	<div id="addWin" class="easyui-window" title="添加用户" style="width:37%;padding:30px 45px;background: #fafafa;height:450px;"
		minimizable="false" maximizable="false" icon="icon-add">
		<form id="ff" class="easyui-form" method="post" style="margin:15px 30px;width:90%" data-options="novalidate:true">
			<table>
				<tr>
					<td > 
						用&nbsp;&nbsp;户&nbsp;&nbsp;名:
					</td>
					<td style="width:80%;">	
						<input id="uname" class="easyui-textbox" missingMessage="由6-18位字母、数字、下划线组成" name="username" 
							type="text" style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
					</td>
					<td style="width:80%;">	
						<input id="pwd" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
							 type="text"  name="password" style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						确认密码：
					</td>
					<td style="width:80%;">	
						<input id="confirm_pwd" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
							name="confirmPwd" type="text"  style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td style="margin-bottom:20px">
						组织机构：
					</td>
					<td style="width:80%;">	
						<input id="group" class="easyui-combobox" name="dept" data-options="valueField:'groupCode',textField:'groupName'"  style="width:280px;height:24px;padding:5px;">
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitAddForm()" style="width:80px;margin:10px 15px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeAddWin()" style="width:80px;margin:10px 15px">取消</a>
		</div>
	</div>
	</body>
	<script>
		
		//添加tab页面
		function addPanel(userName,userId){
			if ($('#tt').tabs('exists', userName+'-授权角色')){
			 	$('#tt').tabs('select', userName+'-授权角色');
			} else {
				 var url = '${pageContext.request.contextPath}/managerUser/toRole?id='+userId+'&userName='+userName+'&appId='+${appId};
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:userName+'-授权角色',
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}
		//添加tab页面
		function addPanel2(userName,userId,groupId){
			if ($('#tt').tabs('exists', userName+'-授权功能')){
			 	$('#tt').tabs('select', userName+'-授权功能');
			} else {
				 var url = '${pageContext.request.contextPath}/managerUser/toFunction?id='+userId+'&userName='+userName+'&groupId='+groupId+'&appId='+${appId};
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:userName+'-授权功能',
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}
		//移除tab页面
		function removePanel(){
			var tab = $('#tt').tabs('getSelected');
			if (tab){
				var index = $('#tt').tabs('getTabIndex', tab);
				$('#tt').tabs('close', index);
			}
		}
		//打开授权功能窗口
		function openWinFunction() {
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定授权?', function(r){
					if(r){
						var userId = row.userId;
						var userName=row.userName;
						var groupId=row.groupId;
						addPanel2(userName,userId,groupId);
					}
				});
			}else{
            	msgShow('系统提示', '请选择用户！', 'warning');
            }
		}
		//打开授权角色窗口
		function openWinRole(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定授权?', function(r){
					if(r){
						var userId = row.userId;
						var userName=row.userName;
						addPanel(userName,userId);
					}
				});
			}else{
            	msgShow('系统提示', '请选择用户！', 'warning');
            }
		};
		
		//添加用户窗口
		function addWin(){
			 $('#addWin').window({
                title: '添加用户',
                modal: true,
                shadow: true,
                closed: true,
                resizable:false,
                closable:false
            });
		}
		
		//打开添加用户窗口
		addWin();
		function openAddWin(){
			clearAddForm();
			$('#addWin').window('open');
		}
		
		//关闭添加用户窗口
		function closeAddWin(){
			$('#addWin').window('close');
		}
		
		// 清除添加用户表单
		function clearAddForm(){
			$('#ff').form('clear');
		}
		
		//修改用户窗口
        function win() {
            $('#upda').window({
                title: '用户信息',
                width: 500,
                modal: true,
                shadow: true,
                closed: true,
                height: 300,
                resizable:false,
                closable:false
            });
        }
        
        // 清除查询表单
		function clearForm(){
			$('#fm').form('clear');
		}
		
		// 清空修改用户窗口
		function clearTable(){
			$('#tb').form('clear');
		};
		
		//打开修改用户窗口
		win();
		function openWin(){
			$('#upda').window('open');
		}
		
		//修改用户
		function updateWin(){
			//打开修改用户窗口之前先清空
			clearTable();
			var row = $('#dg').datagrid('getSelected');
			if (row){
				openWin();
				$('#UserGroup').combobox('select',row.groupId);
			}else{
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
		};
		//关闭修改用户窗口
		function closeWin(){
			$('#upda').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
		//根据用户ID删除用户
		function removeUserByID(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定删除?', function(r){
					if (r){
						   var id = row.Id;
						   var appUserId = row.userId;
						   var url='${pageContext.request.contextPath}/managerUser/removeUserByID?appId=${appId}&id='+id+'&appUserId='+appUserId;
				            $.post(url, function(data) {
				                if(data.status=='1'){
				                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
				                }else{
				                  	msgShow('系统提示', '删除失败！', 'error');
				                }
				            },"json");
				            //刷新
				            findUsers();
					}
			   });
			}else{
            	msgShow('系统提示', '请选择要删除的用户！', 'info');
            }
		}
		
		//列表重新加载
		function reload(url,name){
			$('#dg').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		// 查询用户方法
		function findUsers(){
			//用户名
			var groupId = $('#cc').combobox('getValue');
        	var groupName=$('#cc').combobox('getText');
			var username = $('#un').val().trim();
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
				queryParams: {
					userName: username,
					groupId:groupId
				},
		        url: "${pageContext.request.contextPath}/managerUser/findUserList",  
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
       
        
        // 提交修改后的用户信息
         function updateUser() {
		 	var row = $('#dg').datagrid('getSelected');
			var Id=row.Id;
			var appUserId=row.userId;
            var groupId = $('#UserGroup').combobox('getValue');
            if (groupId == '') {
                msgShow('系统提示', '请选择组织机构！', 'warning');
                return false;
            }
            $.ajax({
    				type:"post",
    				url:"${pageContext.request.contextPath}/managerUser/updateUser",
    				data:{"appId":'${appId}',"groupId":groupId,"appUserId":appUserId,"Id":Id},
    				dataType:"json",
    				success:function (data){
    					if (data.status=='1') {
                			msgShow('系统提示', '修改成功！', 'warning');
                			closeWin();
                			findUsers();
    					}else {
    						msgShow('系统提示', '修改不成功！', 'warning');
    					}
    				},
    				error:function(){
    					$.messager.alert("系统提示","用户修改异常，请刷新页面!","error");
    				}
    		});
           
        }
        
        //前端校验
		function check(){
			var regex_username = /^[a-zA-Z0-9_]{6,18}$/;
			var regex_password= /^(\w){6,20}$/;
			var username = $.trim($('#uname').val()) ;
			var password = $.trim($('#pwd').val()) ;
			var confirm_pass = $.trim($('#confirm_pwd').val()) ;
			var groupId = $('#group').combobox('getValue');
			if(username == "" || username == null || username == undefined || regex_username.test(username) != true){
					$.messager.alert("系统提示","用户名不能为空或格式不正确，请重新填写！","error");	
					return false;
			}
			if(password == "" || password == null || password == undefined || regex_password.test(password) != true){
					$.messager.alert("系统提示","密码不能为空或格式不正确，请重新填写！","error");			
					return false;
			}
			if(confirm_pass =="" || confirm_pass == null || confirm_pass == undefined || regex_password.test(confirm_pass) != true){
					$.messager.alert("系统提示","确认密码不能为空或格式不正确，请重新填写！","error");		
					return false;
			}
			if(password != confirm_pass){
				$.messager.alert("系统提示","密码输入不一致，请重新输入！","error");
				return false;
			}
			if(groupId == ''){
				$.messager.alert("系统提示","请选择组织机构！","error");
				return false;
			}
			return true;
		}
		
		// 提交（用户信息）
		function submitAddForm(){
			var userName = $.trim($('#uname').val()) ;
			var passWord = $.trim($('#pwd').val()) ;
			var groupId = $('#group').combobox('getValue');
			$('#ff').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			
			// 提交信息前完成前端校验
			var check_result = check();
			if(!check_result){
				return;
			}
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/managerUser/addUser",
				data:{"appId":'${appId}',"appUserName":userName,"passWord":passWord,"groupId":groupId},
				dataType:"json",
				success:function (data){
					if (data.status=='1') {
            			msgShow('系统提示', '添加成功！', 'warning');
            			closeAddWin();
            			findUsers();
            			$('#ff').clear();
					}else {
						msgShow('系统提示', '添加不成功！'+data.errMsg, 'warning');
					}
				},
				error:function(){
					$.messager.alert("系统提示","用户添加异常，请刷新页面!","error");
				}
			});
		}
		
		//页面预加载
		$(function(){
			findUsers();
			$.post('${pageContext.request.contextPath}/managerUser/findGroup',function (data) {
				$('.easyui-combobox').combobox('loadData',data);
				if (data.length==1) {
					$('#group').combobox('select',data[0].groupCode);
					$('#cc').combobox('select',data[0].groupCode);
				}
			});
		});
	</script>
</html>