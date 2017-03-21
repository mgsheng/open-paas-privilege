<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>用户信息列表</title>
		<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/easyui.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/master.css" rel="stylesheet" type="text/css" /> 
		<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/icon.css" rel="stylesheet" type="text/css" /> 
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/jquery.insdep-extend.min.js"></script>
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
							<div class="easyui-panel" title="操作" style="padding-top:1%;overflow:hidden;" fit="true" >
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
						组织类型编码：
					</td>
					<td>	
						<input id="showORGANIZATIONTYPECODE"  name="ORGANIZATIONTYPECODE"  type="text" disabled="true">
					</td>
				</tr>
				<tr>
					<td >
						固定电话：
					</td>
					<td >	
						<input id="showPHONENO"  name="PHONENO"  type="text" disabled="true">
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
							所属部门：
						</td>
						<td >	
							<input id="showDEPARTMENT"  name="DEPARTMENT"  type="text" disabled="true">
						</td>
				</tr>
				<!-- 基础特有 -->
					<tr class="base">
						<td >
							超级管理员：
						</td>
						<td >	
							<select id="showISSUPERADMIN" disabled="true">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						<td >
							孔子学院角色ID：
						</td>
						<td >	
							<input id="showKZROLEID"  name="KZROLEID"  type="text" disabled="true">
						</td>
					</tr>
					<tr class="base">
						<td >
							孔子学院ID：
						</td>
						<td >	
							<input id="showKZID"  name="KZID"  type="text" disabled="true"> 
						</td>
						<td >
							职员ID：
						</td>
						<td >	
							<input id="showSTAFFID"  name="STAFFID"  type="text" disabled="true">
						</td>
					</tr>
					<tr class="base">
						<td >
							教材帐号生效日期：
						</td>
						<td >	
							<input id="showACTIVEBEGINDATE"  name="ACTIVEBEGINDATE"  type="text" disabled="true">
						</td>
						<td >
							失效日期：
						</td>
						<td >	
							<input id="showACTIVEENDDATE"  name="ACTIVEENDDATE"  type="text" disabled="true"> 
						</td>
					</tr>
					<tr  class="base">
						<td >
							教材帐号状态：
						</td>
						<td >	
							<select id="showACTIVESTATUS" disabled="true">
								<option value="1">1</option>
							</select>
						</td>
						<td >
							采购价格修改权限：
						</td>
						<td >	
							<select id="showISEDITPURCHASEPRICE" disabled="true">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						</tr>
						<tr class="base">
						<td>
							教育类型
						</td>
						<td >	
							<select id="showStudyType" disabled="true">
								<option value="01">学历教育</option>
								<option value="02">职业教育</option>
								<option value="03">社区教育</option>
								<option value="04">内部培训</option>
								<option value="05">学历教育本部</option>
								<option value="06">孔子学院</option>
								<option value="07">内容服务</option>
								<option value="08">辅修</option>
							</select>
						</td>
					</tr>
				</div> 
			</table>
		</div>
		
	</div>
	<!-- 添加用户窗口 -->
	<div id="addWin" class="easyui-window" title="添加用户" style="top:10%;padding:40px 45px;background: #fafafa;height:400px;"
		minimizable="false" maximizable="false" icon="icon-add" collapsible="false">
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
						<input id="LOGINNAME"  class="easyui-textbox" name="username" type="text"  >
					</td>
				
					<td > 
						真实姓名:
					</td>
					<td >	
						<input id="NAME" class="easyui-textbox" type="text" >
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
						<input id="pwd"  class="easyui-textbox" type="password"  name="password" >
					</td>
					<td>
						确认密码：
					</td>
					<td >	
						<input id="confirm_pwd"  class="easyui-textbox" name="confirmPwd" type="password"   >
					</td>
				</tr>
				<tr>
					<td >
						HR登录名：
					</td>
					<td>	
						<input id="HRLOGINNAME"   class="easyui-textbox" name="HRLOGINNAME"  type="text">
					</td>
					<td >
						组织类型编码：
					</td>
					<td>	
						<input id="ORGANIZATIONTYPECODE"  class="easyui-textbox" name="ORGANIZATIONTYPECODE"  type="text">
					</td>
				</tr>
				<tr>
					<td >
						固定电话：
					</td>
					<td >	
						<input id="PHONENO"  class="easyui-textbox" name="PHONENO"  type="text">
					</td>
					<td >
						移动电话：
					</td>
					<td >	
						<input id="MOBILEPHONE"  class="easyui-textbox"  name="MOBILEPHONE"  type="text">
					</td>
				</tr>
				<tr>
					<td >
						传真电话：
					</td>
					<td >	
						<input id="FAX"  class="easyui-textbox" name="FAX"  type="text">
					</td>
					<td >
						电子邮箱：
					</td>
					<td >	
						<input id="EMAIL"  class="easyui-textbox" name="EMAIL"  type="email">
					</td>
				</tr>
				<tr  class="UNIVERSITY">
						<td >
							所属部门：
						</td>
						<td >	
							<input id="DEPARTMENT"  class="easyui-textbox" name="DEPARTMENT"  type="text">
						</td>
						<td >
							组织机构ID：
						</td>
						<td >	
							<input id="ORGANIZATIONID"  class="easyui-textbox" name="ORGANIZATIONID"  type="text">
						</td>
				</tr>
				<!-- 基础特有 -->
					<tr class="base">
						<td >
							超级管理员：
						</td>
						<td >	
							<select id="ISSUPERADMIN" value="0">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>
						<td >
							孔子学院角色ID：
						</td>
						<td >	
							<input id="KZROLEID"  class="easyui-textbox" name="KZROLEID"  type="text">
						</td>
					</tr>
					<tr class="base">
						<td >
							孔子学院ID：
						</td>
						<td >	
							<input id="KZID"   class="easyui-textbox" name="KZID"  type="text">
						</td>
						<td >
							职员ID：
						</td>
						<td >	
							<input id="STAFFID"  class="easyui-textbox" name="STAFFID"  type="text">
						</td>
					</tr>
					<tr >
						<td >
							教材帐号生效日期：
						</td>
						<td >	
							<input id="ACTIVEBEGINDATE" type="text" class="easyui-datebox" style="width: 100px">
						</td>
						<td >
							失效日期：
						</td>
						<td >	
							<input id="ACTIVEENDDATE" type="text" class="easyui-datebox" style="width: 100px">
						</td>
					</tr>
					<tr >
						<td >
							教材帐号状态：
						</td>
						<td >	
							<select id="ACTIVESTATUS">
								<option value="1">正常</option>
							</select>
						</td>
						<td >
							采购价格修改权限：
						</td>
						<td >	
							<select id="ISEDITPURCHASEPRICE" value="1">
								<option value="1">是</option>
								<!--  <option value="0">否</option>  -->
							</select>
						</td>
						</tr>
						<tr >
						<td>
							教育类型
						</td>
						<td >	
							<select id="StudyType">
								<option value="01">学历教育</option>
								<option value="02">职业教育</option>
								<option value="03">社区教育</option>
								<option value="04">内部培训</option>
								<option value="05">学历教育本部</option>
								<option value="06">孔子学院</option>
								<option value="07">内容服务</option>
								<option value="08">辅修</option>
							</select>
						</td>
						<td  class="dept">
							部门ID：
						</td>
						<td class="dept">	
							<input id="deptId" class="easyui-textbox" type="text">
						</td>
					</tr>
				</div> 
			</table>
		</form>
		</div>
		
		<div region="south" border="false" style="text-align:center;padding:5px 0 line-height: 30px;">
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
						var userName = row.LOGINNAME;
						var groupId = row.groupId;
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
						var userName = row.LOGINNAME;
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
			if (row) {
				openWin();
				//获取该用户的数据，并显示
				showUserValue(row);
			} else {
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
			$('#showDEPARTMENT').val(data.DEPARTMENT);//部门
			$('#showHRLOGINNAME').val(data.HRLOGINNAME);
			$('#showISSUPERADMIN').val(data.ISSUPERADMIN);//是否是超级管理员
			$('#showKZROLEID').val(data.KZROLEID);//孔子学院roleId
			$('#showKZID').val(data.KZID);//孔子学院ID
			$('#showSTAFFID').val(data.STAFFID);//职员ID
			$('#showACTIVEBEGINDATE').val(data.ACTIVEBEGINDATE);//教材帐号生效日期
			$('#showACTIVEENDDATE').val(data.ACTIVEENDDATE);//失效日期：
			$('#showACTIVESTATUS').val(data.ACTIVESTATUS);//教材帐号状态：
			$('#showISEDITPURCHASEPRICE').val(data.ISEDITPURCHASEPRICE);//	采购价格修改权限：
			$('#showStudyType').val(data.STUDYTYPE);//教育类型
			$('#showORGANIZATIONTYPECODE').val(data.ORGANIZATIONTYPECODE);//组织类型ID
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
			$("#LOGINNAME").textbox("setValue",'');
			$('#LOGINNAME').textbox('textbox').attr('readonly',false);  
			$('.pass').removeClass('display');
			$('.dept').removeClass('display');
			$("#deptId").textbox("setValue",'');
			$("#pwd").textbox("setValue",'');
			$("#confirm_pwd").textbox("setValue",'');
			$('#userId').val('');
			$("#NAME").textbox("setValue",'');
			$('#SEX').val('01');
			$("#PHONENO").textbox("setValue",'');
			$("#MOBILEPHONE").textbox("setValue",'');
			$("#FAX").textbox("setValue",'');
			$("#EMAIL").textbox("setValue",'');
			$("#DEPARTMENT").textbox("setValue",'');
			$("#KZROLEID").textbox("setValue",'');
			$('#KZROLEID').textbox('textbox').attr('readonly',false);  
			$("#KZID").textbox("setValue",'');
			$('#KZID').textbox('textbox').attr('readonly',false);  
			$("#STAFFID").textbox("setValue",'');
			$('#STAFFID').textbox('textbox').attr('readonly',false);  
			$('#ISSUPERADMIN').val('0');
			$('#ACTIVEBEGINDATE').val('');//有效时间
			$('#ACTIVEENDDATE').val('');//失效时间
			$('#ACTIVESTATUS').val('1');
			$('#ISEDITPURCHASEPRICE').val('1');	
			$('#StudyType').val('01');//教育类型
			$("#ORGANIZATIONID").textbox("setValue",'');
			$('#ORGANIZATIONID').textbox('textbox').attr('readonly',false); 
			$("#ORGANIZATIONTYPECODE").textbox("setValue",'');
			$('#ORGANIZATIONTYPECODE').textbox('textbox').attr('readonly',false); 
			$("#HRLOGINNAME").textbox("setValue",'');
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
		
		
		
		//打开修改用户窗口
		win();
		function openWin(){
			$('#showMessage').window('open');
		}
		
		//修改用户
		function updateWin(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				openAddWin();
				//获取该用户的数据，并显示
				getUserValue(row);
				$('#group').combobox('select',row.groupId);
			} else {
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
		};
		//获取该用户的数据，并显示
		function getUserValue(data) {
			$('#group').combobox('select',data.groupId);
			$('#userId').val(data.USERID);
			$("#LOGINNAME").textbox("setValue",data.LOGINNAME);//登陆名
			$('#LOGINNAME').textbox('textbox').attr('readonly',true); //只读方式
			$('.pass').addClass("display");//密码隐藏
			$("#pwd").textbox("setValue",data.PASSWORD);
			$('.dept').addClass("display");//部门Id隐藏
			$("#NAME").textbox("setValue",data.NAME);//真实姓名
			$('#SEX').val(data.SEX);
			$("#PHONENO").textbox("setValue",data.PHONENO);//固定电话
			$("#MOBILEPHONE").textbox("setValue",data.MOBILEPHONE);//移动电话
			$("#FAX").textbox("setValue",data.FAX);//传真电话
			$("#EMAIL").textbox("setValue",data.EMAIL);//邮箱
			$("#DEPARTMENT").textbox("setValue",data.DEPARTMENT);//部门
			$('#ISSUPERADMIN').val(data.ISSUPERADMIN);//是否是超级管理员
			$("#KZROLEID").textbox("setValue",data.KZROLEID);//孔子学院roleId
			$('#KZROLEID').textbox('textbox').attr('readonly',true);  //只读方式
			$("#KZID").textbox("setValue",data.KZID);//孔子学院ID
			$('#KZID').textbox('textbox').attr('readonly',true); 
			$("#STAFFID").textbox("setValue",data.STAFFID);//职员ID
			$('#STAFFID').textbox('textbox').attr('readonly',true); 
			$('#ACTIVEBEGINDATE').datebox('setValue',"" );//教材帐号生效日期
			$('#ACTIVEENDDATE').datebox('setValue', "");//失效日期：
			$('#ACTIVESTATUS').val(data.ACTIVESTATUS);//教材帐号状态：
			$('#ISEDITPURCHASEPRICE').val(data.ISEDITPURCHASEPRICE);//	采购价格修改权限：
			$('#StudyType').val(data.STUDYTYPE);//教育类型
			$("#ORGANIZATIONID").textbox("setValue",data.ORGANIZATIONID);
			$('#ORGANIZATIONID').textbox('textbox').attr('readonly',true); //组织ID
			$("#ORGANIZATIONTYPECODE").textbox("setValue",data.ORGANIZATIONTYPECODE);//组织类型ID
			$('#ORGANIZATIONTYPECODE').textbox('textbox').attr('readonly',true); 
			$("#HRLOGINNAME").textbox("setValue",data.HRLOGINNAME);//Hr登陆名
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
			if (row) {
				$.messager.confirm('系统提示', '是否确定删除?', function(r){
					if (r) {
						   var appUserId = row.USERID;
						   var loginName = row.LOGINNAME;
						   var groupType = row.groupType;
						   var url = '${pageContext.request.contextPath}/managerUser/removeUserByID?appId=${appId}&appUserId='+appUserId+'&type=4&loginName='+loginName;
						   $.post(url,
			                    	function(data){
			                        	if (data.status == '1') {
				                			msgShow('系统提示', '删除成功！', 'warning');
				                			findUsers();
				    					} else if (data.status == '0'){
				    						msgShow('系统提示', '删除不成功！', 'warning');
				    						findUsers();
				    					}
			                 });
					}
			   });
			} else {
            	msgShow('系统提示', '请选择要删除的用户！', 'info');
            }
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
					type:'4'
				},
		        url: "${pageContext.request.contextPath}/managerUser/findUserList",  
		        pagination: true,//显示分页工具栏
		        columns:[[
							{ field: 'LOGINNAME', title: '登陆名',align:'center',sortable:true,width:'20%'},
							{ field: 'PASSWORD', title: '密码',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'NAME', title: '姓名',align:'center',sortable:true,width:'10%'},
							{ field: 'SEX', title: '性别',align:'center',sortable:true,width:'10%',
								formatter : function(value, row, index) {  
				                    if(value == '01'){  
				                     	return '<span title="男"><font color=green>男</font></span>';     
				                    } else if (value == '02') {  
				                     	return '<span title="女"><font color=gray>女</font></span>';     
				                    }                     
				                }
							},
							{ field: 'SexValue', title: '性别值',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'PHONENO', title: '固定电话',align:'center',sortable:true,width:'10%'},
							{ field: 'MOBILEPHONE', title: '移动电话',align:'center',sortable:true,width:'10%'},
							{ field: 'EMAIL', title: '邮箱',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'FAX', title: '传真号码',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'HRLOGINNAME', title: 'HR登录名',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'DEPARTMENT', title: '部门',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'KZROLEID', title: '孔子学院角色ID',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'KZID', title: '孔子学院ID',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'STAFFID', title: '职员ID',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'USERID', title: '用户ID',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'type', title: '类型',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'groupId', title: '组织机构ID',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'groupName', title: '组织机构',align:'center',sortable:true,width:'10%'},
							{ field: 'ACTIVEBEGINDATE', title: '教材帐号生效日期',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ACTIVEENDDATE', title: '教材帐号失效日期',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ACTIVESTATUS', title: '教材帐号状态',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ActivieStatusValue', title: '教材帐号状态',hidden:true,align:'center',sortable:true,width:'10%'},
							{ field: 'ISEDITPURCHASEPRICE', title: '采购价格修改权限',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ISSUPERADMIN', title: '超级管理员',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'STUDYTYPE', title: '教育类型',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ORGANIZATIONTYPECODE', title: '组织类型编码',align:'center',hidden:true,sortable:true,width:'10%'},
							{ field: 'ORGANIZATIONID', title: '组织ID',align:'center',hidden:true,sortable:true,width:'10%'}
							
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
       
        
        
        //前端校验
		function check(){
			var regex_username = /^[a-zA-Z0-9_]{6,18}$/;
			var regex_password = /^(\w){6,20}$/;
			var regex_IdCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)|(^\d{17}([0-9]|X)$)|(^\d{16}$)/; 
			var regex_MOBILEPHONE = /^1[3|4|5|8|7][0-9]\d{4,8}$/;
			var regex_PHONENO = /^0\d{2,3}-?\d{7,8}$/;   
			var regex_EMAIL = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
			var username = $.trim($("#LOGINNAME").textbox("getValue"));
			var password = $.trim($("#pwd").textbox("getValue"));
			var confirm_pass = $.trim($("#confirm_pwd").textbox("getValue")) ;
			var NAME = $.trim($("#NAME").textbox("getValue")) ;
			var groupId = $('#group').combobox('getValue');
			var PHONENO = $.trim($("#PHONENO").textbox("getValue"));
			var MOBILEPHONE = $.trim($("#MOBILEPHONE").textbox("getValue"));
			var EMAIL = $.trim($("#EMAIL").textbox("getValue"));
			var userId = $('#userId').val();
			if (NAME == "" || NAME == null) {
				$.messager.alert("系统提示","姓名不能为空，请重新填写！","error");	
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
			}
			
			if(groupId == ''){
				$.messager.alert("系统提示","请选择组织机构！","error");
				return false;
			}
			if (EMAIL.length > 0) {
				if (regex_EMAIL.test(EMAIL) != true) {
					$.messager.alert("系统提示","邮箱格式不正确！","error");
					return false;
				}
				
			}
			return true;
		}
		
		// 提交（用户信息）
		function submitAddForm(){
			$("#ORGANIZATIONID").textbox("getValue")
			var userName = $.trim($("#LOGINNAME").textbox("getValue"));
			var passWord = $.trim($("#pwd").textbox("getValue")) ;
			var groupId = $('#group').combobox('getValue');
			var Name = $("#NAME").textbox("getValue");//真实姓名
			var SEX = $('#SEX').val();
			var PHONENO = $("#PHONENO").textbox("getValue");
			var MOBILEPHONE = $("#MOBILEPHONE").textbox("getValue");
			var FAX = $("#FAX").textbox("getValue");//传真电话
			var EMAIL = $("#EMAIL").textbox("getValue");
			var DEPARTMENT = $("#DEPARTMENT").textbox("getValue");//部门
			var ISSUPERADMIN = $('#ISSUPERADMIN').val();//是否是超级管理员
			var KZROLEID = $("#KZROLEID").textbox("getValue");//孔子学院roleId
			var KZID = $("#KZID").textbox("getValue");//孔子学院ID
			var STAFFID = $("#STAFFID").textbox("getValue");//职员ID
			var ACTIVEBEGINDATE = $('#ACTIVEBEGINDATE').datebox('getValue');//教材帐号生效日期
			var ACTIVEENDDATE = $('#ACTIVEENDDATE').datebox('getValue');//失效日期：
			var ACTIVESTATUS = $('#ACTIVESTATUS').val();//教材帐号状态：
			var ISEDITPURCHASEPRICE = $('#ISEDITPURCHASEPRICE').val();//	采购价格修改权限：
			var StudyType = $('#StudyType').val();//教育类型
			var HRLOGINNAME = $("#HRLOGINNAME").textbox("getValue");
			var deptId = $("#deptId").textbox("getValue");
			var ORGANIZATIONID = $("#ORGANIZATIONID").textbox("getValue");
			if (ACTIVEBEGINDATE.length >0) {
				var newTime = new Date(ACTIVEBEGINDATE);
				ACTIVEBEGINDATE = newTime.format("yyyy-MM-dd hh:mm:ss");
			}
			if (ACTIVEENDDATE.length >0) {
				var newTime = new Date(ACTIVEENDDATE);
				ACTIVEENDDATE = newTime.format("yyyy-MM-dd hh:mm:ss");
			}
			
			$('#ff').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			
			// 提交信息前完成前端校验
			var check_result = check();
			if (!check_result) {
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
						"appId":'${appId}',"appUserName":userName,"passWord":passWord,"groupId":groupId,
						"Name":Name,"SEX":SEX,"PHONENO":PHONENO,"type":'4',"StudyType":StudyType,"KZID":KZID,
						"MOBILEPHONE":MOBILEPHONE,"FAX":FAX,"DEPARTMENT":DEPARTMENT,"EMAIL":EMAIL,"deptId":deptId,
						"ISSUPERADMIN":ISSUPERADMIN,"KZROLEID":KZROLEID,"HRLOGINNAME":HRLOGINNAME,
						"STAFFID":STAFFID,"ACTIVEBEGINDATE":"","ACTIVEENDDATE":"","ORGANIZATIONID":ORGANIZATIONID,
						"ACTIVESTATUS":ACTIVESTATUS,"ISEDITPURCHASEPRICE":ISEDITPURCHASEPRICE
					},
                 	function(data){
                     	if (data.status == '1') {
                			msgShow('系统提示', '添加成功！', 'warning');
                			closeAddWin();
                			findUsers();
    					} else if (data.status == '0') {
    						msgShow('系统提示', '添加不成功！' + data.errMsg, 'warning');
    					} else if (data.status == '2') {
    						msgShow('系统提示', '修改成功！', 'warning');
    						findUsers();
    						closeAddWin();
    					} else if (data.status == '-1') {
    						msgShow('系统提示', '修改不成功！' + data.errMsg, 'warning');
    						closeAddWin();
                			findUsers();
    					}
              });
		}
		
		//页面预加载
		$(function(){
			$.post('${pageContext.request.contextPath}/managerUser/findGroup?type=4',function (data) {
				$('.easyui-combobox').combobox('loadData',data);
				$('#cc').combobox('select',data[0].groupCode);
				findUsers();
			});
			
		});
		Date.prototype.format = function(fmt) {
			var o = {
				"M+": this.getMonth() + 1, //月份 
				"d+": this.getDate(), //日 
				"h+": this.getHours(), //小时 
				"m+": this.getMinutes(), //分 
				"s+": this.getSeconds(), //秒 
				"q+": Math.floor((this.getMonth() + 3) / 3), 
				"S": this.getMilliseconds() //毫秒 
			};
			if (/(y+)/.test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			}
			for (var k in o) {
				if (new RegExp("(" + k + ")").test(fmt)) {
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				}
			}
			return fmt;
		}
	</script>
</html>