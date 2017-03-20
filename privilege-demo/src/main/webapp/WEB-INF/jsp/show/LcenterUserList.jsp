<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>学习中心用户信息列表</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css">
			.display{display: none;}
		</style>
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
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="showMes();" style="margin-left:2%;padding-bottom:0.6%;display:inline;">
											<span style="font-weight:bold;">查看详情</span>
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
					</table>
				</div>
			</div>
		</div>	
	<!--修改用户窗口--> 
	<div id="showMessage" class="easyui-window" title="用户信息详情" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="background: #fafafa;">
		<div  style="overflow-x: auto;">
		<form id="show" >
			<table>
				<tr>
					<td >
						组织机构：
					</td>
					<td >	
						<input id="showGroup"  name="dept" class="disable" disabled="true">
					</td>
				</tr>
				<tr>
					<td > 
						登&nbsp;&nbsp;录&nbsp;&nbsp;名:
					</td>
					<td >	
						<input id="showLOGINNAME"  name="username" type="text"   disabled="true">
					</td>
				</tr>
				<tr>
					<td > 
						真实姓名:
					</td>
					<td >	
						<input id="showNAME" type="text" disabled="true">
					</td>
				</tr>
				<tr>
					<td > 
						性别:
					</td>
					<td >	
						<select  id="showSEX"  disabled="true">
								<option value="01">男</option>
								<option value="02">女</option>
							</select> 
					</td>
				</tr>
				<tr>
					<td>
						密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
					</td>
					<td >	
						<input id="showPwd"  type="password"  name="text" disabled="true">
					</td>
				</tr>
				<tr>
					<td >
						HR登录名：
					</td>
					<td>	
						<input id="showHRLOGINNAME"  name="HRLOGINNAME"  type="text" disabled="true">
					</td>
					<td >
						身份证号：
					</td>
					<td>	
						<input id="showIDCARD"    type="text" disabled="true">
					</td>
				</tr>
				<tr>
					<td >
						固定电话：
					</td>
					<td >	
						<input id="showPHONENO"   type="text" disabled="true">
					</td>
					<td >
						移动电话：
					</td>
					<td >	
						<input id="showMOBILEPHONE"  name="MOBILEPHONE"  type="text" disabled="true">
					</td>
				</tr>
				<tr>
					<td >
						传真电话：
					</td>
					<td >	
						<input id="showFAX"  name="FAX"  type="text" disabled="true">
					</td>
					<td >
						电子邮箱：
					</td>
					<td >	
						<input id="showEMAIL"  name="EMAIL"  type="email" disabled="true">
					</td>
				</tr>
				<tr   >
						<td >
							地址：
						</td>
						<td >	
							<input id="showADDRESS"  name="ADDRESS"  type="text" disabled="true">
						</td>
				</tr>
				<tr   >
						<td >
							邮编：
						</td>
						<td >	
							<input id="showPOSTCODE"  name="POSTCODE"  type="text" disabled="true">
						</td>
				</tr>
					
				</div> 
			</table>
		</div>
		
	</div>
	<!-- 添加用户窗口 -->
	<div id="addWin" class="easyui-window" title="添加用户" style="width:37%;padding:30px 45px;background: #fafafa;height:450px;"
		minimizable="false" maximizable="false" icon="icon-add">
		<div  style="overflow-x: auto;">
		<form id="ff" method="post" >
		<input id="userId" type="hidden">
			<table>
				<tr>
					<td >
						组织机构：
					</td>
					<td >	
						<input id="group" class="easyui-combobox" name="dept" data-options="valueField:'groupCode',textField:'groupName'"  style="width:200px;height:24px;padding:5px;">
					</td>
				</tr>
				<tr>
					<td > 
						登&nbsp;&nbsp;录&nbsp;&nbsp;名:
					</td>
					<td >	
						<input id="LOGINNAME"  name="username" type="text" >
					</td>
				</tr>
				<tr>
					<td > 
						真实姓名:
					</td>
					<td >	
						<input id="NAME" type="text" >
					</td>
				</tr>
				<tr>
					<td > 
						性别:
					</td>
					<td >	
						<select  id="SEX"  >
								<option value="01">男</option>
								<option value="02">女</option>
							</select> 
					</td>
				</tr>
				<tr class="pass">
					<td>
						密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
					</td>
					<td >	
						<input id="pwd" 
							 type="password"  name="password" >
					</td>
				</tr>
				<tr class="pass">
					<td>
						确认密码：
					</td>
					<td >	
						<input id="confirm_pwd" 
							name="confirmPwd" type="password"   >
					</td>
				</tr>
				<tr>
					<td >
						HR登录名：
					</td>
					<td>	
						<input id="HRLOGINNAME"  name="HRLOGINNAME"  type="text">
					</td>
					<td >
						身份证号：
					</td>
					<td>	
						<input id="IDCARD"    type="text">
					</td>
				</tr>
				<tr>
					<td >
						固定电话：
					</td>
					<td >	
						<input id="PHONENO"  name="PHONENO"  type="text">
					</td>
					<td >
						移动电话：
					</td>
					<td >	
						<input id="MOBILEPHONE"  name="MOBILEPHONE"  type="text">
					</td>
				</tr>
				<tr>
					<td >
						传真电话：
					</td>
					<td >	
						<input id="FAX"  name="FAX"  type="text">
					</td>
					<td >
						电子邮箱：
					</td>
					<td >	
						<input id="EMAIL"  name="EMAIL"  type="email">
					</td>
				</tr>
				<tr  >
						<td >
							联系地址：
						</td>
						<td >	
							<input id="ADDRESS"  name="ADDRESS"  type="text">
						</td>
				</tr>
				<tr  >
						<td >
							邮编：
						</td>
						<td >	
							<input id="POSTCODE"  name="POSTCODE"  type="text">
						</td>
						<td class="dept">
							部门ID：
						</td>
						<td class="dept">	
							<input id="deptId" type="text">
						</td>
				</tr>
				
				</div> 
			</table>
		</form>
		</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitAddForm()" style="width:80px;margin:10px 15px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeAddWin()" style="width:80px;margin:10px 15px">取消</a>
		</div>
	</div>
	
	</body>
	<script>
		var group;//存放组织机构选择的下拉框数据
		var UserGroup//存放添加用户时 组织机构数据
		$('#cc').combobox({
			onSelect: function(record){
				group = record;
			}
		});
		$('#group').combobox({
			onSelect: function(record){
				UserGroup = record;
			}
		});
		//添加tab页面
		function addPanel(userName,userId,groupId){
			if ($('#tt').tabs('exists', userName+'-授权角色')){
			 	$('#tt').tabs('select', userName+'-授权角色');
			} else {
				 var url = '${pageContext.request.contextPath}/managerUser/toRole?id='+userId+'&userName='+userName+'&appId=+${appId}&groupId='+groupId;
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
						var userId = row.USERID;
						var userName=row.LOGINNAME;
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
						var userId = row.USERID;
						var groupId = row.groupId;
						var userName=row.LOGINNAME;
						addPanel(userName,userId,groupId);
					}
				});
			}else{
            	msgShow('系统提示', '请选择用户！', 'warning');
            }
		};
		//打开详情窗口
		function showMes() {
			var row = $('#dg').datagrid('getSelected');
			if (row){
				openWin();
				//获取该用户的数据，并显示
				showUserValue(row);
			}else{
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
		}
		//显示用户详情
		function showUserValue(data) {
			$('#showGroup').val(data.groupName);
			$('#showLOGINNAME').val(data.LOGINNAME);
			$('#showPwd').val(data.PASSWORD);
			$('#showNAME').val(data.NAME);//真实姓名
			$('#showSEX').val(data.SEX);
			$('#showPHONENO').val(data.PHONENO);//固定电话
			$('#showMOBILEPHONE').val(data.MOBILEPHONE);//移动电话
			$('#showFAX').val(data.FAX);//传真电话
			$('#showEMAIL').val(data.EMAIL);
			$('#showIDCARD').val(data.IDCARD);//身份证号
			$('#showADDRESS').val(data.ADDRESS);//地址
			$('#showPOSTCODE').val(data.POSTCODE);//邮编
			
		}
		//添加用户窗口
		function addWin(){
			 $('#addWin').window({
                title: '添加用户',
                modal: true,
                shadow: true,
                closed: true,
                width: 700,
                height: 560,
                resizable:true,
                closable:false
            });
		}
		
		//打开添加用户窗口
		addWin();
		function openAddWin(){
			$('#userId').val('');
			clearAddForm();
			$('#addWin').window('open');
		}
		
		//关闭添加用户窗口
		function closeAddWin(){
			$('#addWin').window('close');
		}
		
		// 清除添加用户表单
		function clearAddForm(){
			$('#userId').val('');
			$('#LOGINNAME').val('').prop("disabled",false);
			$('.pass').removeClass("display");
			$('#pwd').val('');
			$('#confirm_pwd').val('');
			$('#NAME').val('');//真实姓名
			$('#SEX').val('01');
			$('#PHONENO').val('');//固定电话
			$('#MOBILEPHONE').val('');//移动电话
			$('#FAX').val('');//传真电话
			$('#EMAIL').val('');
			$('#IDCARD').val('');//身份证号
			$('#ADDRESS').val('');//地址
			$('#POSTCODE').val('');//邮编
			$('#HRLOGINNAME').val('');//HR登陆名
			$('#deptId').val('');
			$('.dept').removeClass("display");
		}
		
		//修改用户窗口
        function win() {
            $('#showMessage').window({
                title: '用户信息',
                modal: true,
                shadow: true,
                closed: true,
                width: 700,
                height: 560,
                resizable:true,
                closable:true
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
			$('#showMessage').window('open');
		}
		
		//修改用户
		function updateWin(){
			//打开修改用户窗口之前先清空
			var row = $('#dg').datagrid('getSelected');
			if (row){
				openAddWin();
				//获取该用户的数据，并显示
				getUserValue(row);
				$('#group').combobox('select',row.groupId);
			}else{
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
		};
		//获取该用户的数据，并显示
		function getUserValue(data) {
			$('#group').combobox('select',data.LCENTERCODE);
			$('#userId').val(data.USERID);
			$('#LOGINNAME').val(data.LOGINNAME).prop("disabled",true);
			$('.pass').addClass("display");
			$('#pwd').val(data.PASSWORD);
			$('.dept').addClass("display");
			$('#NAME').val(data.NAME);//真实姓名
			$('#SEX').val(data.SEX);
			$('#PHONENO').val(data.PHONENO);//固定电话
			$('#MOBILEPHONE').val(data.MOBILEPHONE);//移动电话
			$('#FAX').val(data.FAX);//传真电话
			$('#EMAIL').val(data.EMAIL);
			$('#IDCARD').val(data.IDCARD);//身份证号
			$('#ADDRESS').val(data.ADDRESS);//地址
			$('#POSTCODE').val(data.POSTCODE);//邮编
			$('#HRLOGINNAME').val(data.HRLOGINNAME);//HR登陆名
		}
		//关闭修改用户窗口
		function closeWin(){
			$('#showMessage').window('close');
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
						   var appUserId = row.USERID;
						   var loginName = row.LOGINNAME;
						   var url='${pageContext.request.contextPath}/managerUser/removeUserByID?appId=${appId}&appUserId='+appUserId+'&type=6&loginName='+loginName;
						   $.post(url,
			                    	function(data){
			                        	if (data.status=='1') {
				                			msgShow('系统提示', '删除成功！', 'warning');
				                			findUsers();
				    					}else if (data.status=='0'){
				    						msgShow('系统提示', '删除不成功！', 'warning');
				    						findUsers();
				    					}
			                 });
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
			var groupId = null;
			if (group != null) {
				groupId = group.groupCode;
			}
			
			//用户名
			var username = $('#un').val().trim();
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
				queryParams: {
					userName: username,
					groupId:groupId,
					type:'6'
				},
		        url: "${pageContext.request.contextPath}/managerUser/findUserList",  
		        pagination: true,//显示分页工具栏
		        columns:[[
							{ field: 'LOGINNAME', title: '登陆名',align:'center',sortable:true,width:'20%'},
							{ field: 'PASSWORD', title: '密码',align:'center',sortable:true,width:'10%'},
							{ field: 'NAME', title: '姓名',align:'center',sortable:true,width:'10%'},
							{ field: 'SEX', title: '性别',align:'center',sortable:true,width:'10%',
								formatter : function(value, row, index) {  
				                    if(value=='01'){  
				                     	return '<span title="男"><font color=green>男</font></span>';     
				                    } else if (value=='02') {  
				                     	return '<span title="女"><font color=gray>女</font></span>';     
				                    }                     
				                }
							},
							{ field: 'PHONENO', title: '固定电话',align:'center',sortable:true,width:'10%'},
							{ field: 'MOBILEPHONE', title: '移动电话',align:'center',sortable:true,width:'10%'},
							{ field: 'EMAIL', title: '邮箱',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'IDCARD', title: '身份证号',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ADDRESS', title: '地址',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'POSTCODE', title: '邮编号码',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'FAX', title: '传真号码',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'HRLOGINNAME', title: 'HR登录名',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'USERID', title: '用户ID',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'type', title: '类型',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'groupId', title: '组织机构ID',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'groupName', title: '组织机构',align:'center',sortable:true,width:'10%'},
							{ field: 'LCENTERCODE', title: '学习中心ID',hidden:true,align:'center',sortable:true,width:'10%'}
							]],
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
            $.post("${pageContext.request.contextPath}/managerUser/updateUser",
            		{"appId":'${appId}',"groupId":groupId,"appUserId":appUserId,"Id":Id},
                 	function(data){
            			if (data.status=='1') {
                			msgShow('系统提示', '修改成功！', 'warning');
                			closeWin();
                			findUsers();
    					}else if (data.status=='0'){
    						msgShow('系统提示', '修改不成功！', 'warning');
    					}
              });
        }
        
        //前端校验
		function check(){
			var regex_username = /^[a-zA-Z0-9_]{6,18}$/;
			var regex_password = /^(\w){6,20}$/;
			var regex_IdCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)|(^\d{17}([0-9]|X)$)|(^\d{16}$)/; 
			var regex_MOBILEPHONE = /^1[3|4|5|8|7][0-9]\d{4,8}$/;
			var regex_PHONENO=/^0\d{2,3}-?\d{7,8}$/;   
			var regex_EMAIL=/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
			var username = $.trim($('#LOGINNAME').val()) ;
			var password = $.trim($('#pwd').val()) ;
			var confirm_pass = $.trim($('#confirm_pwd').val()) ;
			var NAME = $.trim($('#NAME').val()) ;
			var groupId = $('#group').combobox('getValue');
			var IDCARD = $.trim($('#IDCARD').val());
			var PHONENO = $.trim($('#PHONENO').val());
			var MOBILEPHONE = $.trim($('#MOBILEPHONE').val());
			var EMAIL = $('#EMAIL').val();
			var userId = $('#userId').val();
			if (NAME == "" || NAME == null) {
				$.messager.alert("系统提示","姓名不能为空，请重新填写！","error");	
				return false;
			}
			if (IDCARD == "" || IDCARD == null || regex_IdCard.test(IDCARD) != true) {
				$.messager.alert("系统提示","身份证不能为空或格式不对，请重新填写！","error");	
				return false;
			}
			if (PHONENO == "" || PHONENO == null) {
				$.messager.alert("系统提示","固定电话不能为空，请重新填写！","error");	
				return false;
			}
			if (MOBILEPHONE == "" || MOBILEPHONE == null || regex_MOBILEPHONE.test(MOBILEPHONE) != true) {
				$.messager.alert("系统提示","移动电话号码不能为空或格式不正确，请重新填写！","error");	
				return false;
			}
			if(username == "" || username == null || username == undefined || regex_username.test(username) != true){
					$.messager.alert("系统提示","用户名不能为空或格式不正确，请重新填写！","error");	
					return false;
			}
			if (userId.length <= 0) {
				if (password == "" || password == null || password == undefined || regex_password.test(password) != true){
					$.messager.alert("系统提示","密码不能为空或格式不正确，请重新填写！","error");			
					return false;
				}
				if (confirm_pass =="" || confirm_pass == null || confirm_pass == undefined || regex_password.test(confirm_pass) != true){
					$.messager.alert("系统提示","确认密码不能为空或格式不正确，请重新填写！","error");		
					return false;
				}
				if (password != confirm_pass){
					$.messager.alert("系统提示","密码输入不一致，请重新输入！","error");
					return false;
				}
			}
			
			if(groupId == ''){
				$.messager.alert("系统提示","请选择组织机构！","error");
				return false;
			}
			if (EMAIL.length > 0) {
				if(regex_EMAIL.test(EMAIL) != true){
					$.messager.alert("系统提示","邮箱格式不正确！","error");
					return false;
				}
				
			}
			return true;
		}
		
		// 提交（用户信息）
		function submitAddForm(){
			var userName = $.trim($('#LOGINNAME').val());
			var passWord = $.trim($('#pwd').val()) ;
			var groupId = $('#group').combobox('getValue');
			var codeName = UserGroup.codeName;
			var Name = $('#NAME').val();//真实姓名
			var SEX = $('#SEX').val();
			var PHONENO = $('#PHONENO').val();
			var MOBILEPHONE = $('#MOBILEPHONE').val();
			var FAX = $('#FAX').val();//传真电话
			var EMAIL = $('#EMAIL').val();
			var IDCARD = $('#IDCARD').val();//身份证号
			var POSTCODE = $('#POSTCODE').val();//邮编
			var ADDRESS = $('#ADDRESS').val();//地址
			var HRLOGINNAME = $('#HRLOGINNAME').val();//HR登陆名
			var deptId = $('#deptId').val();
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
			var url = '';
			var userId = $('#userId').val();
			if ($('#userId').val() != '') {
				url = '${pageContext.request.contextPath}/managerUser/updateUser?appUserId='+userId;
			} else {
				url = '${pageContext.request.contextPath}/managerUser/addUser';
			}
			$.post(url,
					{
						"appId":'${appId}',"appUserName":userName,"passWord":passWord,"groupId":groupId,"deptId":deptId,
						"Name":Name,"SEX":SEX,"PHONENO":PHONENO,"type":'6',"IDCARD":IDCARD,"POSTCODE":POSTCODE,
						"MOBILEPHONE":MOBILEPHONE,"FAX":FAX,"EMAIL":EMAIL,"HRLOGINNAME":HRLOGINNAME,"ADDRESS":ADDRESS
					},
                 	function(data){
                     	if (data.status == '1') {
                			msgShow('系统提示', '添加成功！', 'warning');
                			closeAddWin();
                			findUsers();
    					} else if (data.status == '0') {
    						msgShow('系统提示', '添加不成功！'+ data.errMsg, 'warning');
    					} else if (data.status == '2') {
    						msgShow('系统提示', '修改成功！', 'warning');
    						closeAddWin();
                			findUsers();
    					} else if (data.status == '-1') {
    						msgShow('系统提示', '修改不成功！'+ data.errMsg, 'warning');
    					}
              });
		}
		
		//页面预加载
		$(function(){
			$.post('${pageContext.request.contextPath}/managerUser/findGroup?type=6',function (data) {
				$('.easyui-combobox').combobox('loadData',data);
				$('#cc').combobox('select',data[0].groupCode);
				findUsers();
			});
			
		});
	</script>
</html>