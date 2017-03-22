<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>授权资源</title>
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/easyui.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/master.css" rel="stylesheet" type="text/css" /> 
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/icon.css" rel="stylesheet" type="text/css" /> 
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/jquery.insdep-extend.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<!-- 授权资源窗口 -->
	<div class="easyui-pannel">
		<div id="authorizeResWin" class="botton"
			style="margin-top: 0; width: 50%; height: 1000px; border: 1px;">
			<div id="tbFun" style="padding: 10px 10px;">
				<span style="text-align: left;" hidden="true"> 
				<input  id="${groupCode}" name="${groupName}" hidden="true"/>
				<input  id="roleId"  value="${roleId}" hidden="true"/>
				</span> &nbsp;&nbsp;&nbsp;&nbsp; <span style="float: right;"> <a
					href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true"
					id="ok" onclick="submitAuthorizeRes();">确认</a> <a href="#"
					class="easyui-linkbutton" iconCls="icon-undo" plain="true"
					id="undo" onclick="cancelAuthorizeRes();">取消</a>
					<a href="#"
					class="easyui-linkbutton" iconCls="icon-add" plain="true"
					id="" onclick="CreateGroupRole();">创建组织机构管理员</a>
					<a href="#"
					class="easyui-linkbutton" iconCls="icon-add" plain="true"
					id="" onclick="CreateGroupAdministrator();">创建组织机构管理员用户</a>
				</span>
			</div>
			
			<ul id="tt" class="easyui-tree" style="height: 100%" data-options="
				method:'get',animate:true,checkbox:true,lines:true,cascadeCheck:true"></ul>
		</div>
	</div>
	<div id='loading' style='position:absolute;left:0;width:100%;height:100%;top:0;background:#E0ECFF;opacity:0.8;filter:alpha(opacity=80);'>
			<div style='position:absolute;  cursor1:wait;left:50%;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;border:2px solid #ccc;color:#000;'> 
 				正在加载，请等待...
			</div>
	</div>
	<!--注册组织机构管理员用户-->
	<div id="w" class="easyui-window" title="注册组织机构管理员用户" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 250px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>用户名：</td>
						<td>
							<input id="userName"  class="easyui-textbox" type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>密码：</td>
						<td><input id="txtPass" class="easyui-textbox" type="password" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" class="easyui-textbox" type="password" class="txt01" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"></a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)"></a>
			</div>
		</div>
	</div>
	<!--添加组织机构管理员角色-->
	<div id="roleWin" class="easyui-window" title="创建组织机构管理员" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>角色名称：</td>
						<td><input id="roleName" class="easyui-textbox" type="text" class="txt01" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEnter" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"></a> <a id="btnClose"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)"></a>
			</div>
		</div>
	</div>
</body>
<script>
	var initialResIds="";//用于存放修改界面中选中的resource
	var checkedResIds="";//存放选中的resource
	var addIds="";//存放修改时添加的权限Id
	var delIds="";//存放修改时删除的权限Id
	//修改tree 图标样式
	function getRoot(){
		var node=$('#tt').tree('getRoots');
		$.each(node,function(i,n){
			if($(n.target).children(".tree-icon").hasClass('tree-file')){
				$(n.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
			}
			$(n.target).children(".tree-icon").addClass("tree-folder");
			var children=$('#tt').tree('getChildren',n.target);
			$.each(children,function(i,m){
				if(m.ismodule=="0"){
					if($(m.target).children(".tree-icon").hasClass('tree-file')){
						$(m.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
					}
				}else if (m.ismodule=="1") {
					$(m.target).children(".tree-icon").addClass("icon icon-mini-add");
				}
			});
		});
	}
	//创建组织机构管理员角色取消按钮
	$('#btnClose').click(function() {
		$('#roleWin').window('close');
		$("#roleName").textbox("setValue",'');
	});
	//创建组织机构管理员角色确定按钮
	$('#btnEnter').click(function() {
		var roleName = $("#roleName").textbox("getValue");
		if ($('#roleId').val() != '') {
            msgShow('系统提示', '该组织机构已经存在管理员角色！', 'warning');
            return false;
        }
        if (roleName == '') {
        	msgShow('系统提示', '请填写角色名称！', 'warning');
            return false;
		}
		$.post('${pageContext.request.contextPath}/oesGroup/addRole',
				{
					appId:'${appId}',
					roleName:roleName,
					groupName:'${groupName}',
					roleType:'2',
					groupId:'${groupCode}',
					status:'0'
				},function(data){
					if (data.status=='1') {
						$('#roleId').val(data.privilegeRoleid);
						msgShow('系统提示', '添加管理员角色成功！', 'info');
						$('#roleWin').window('close');
						$("#roleName").textbox("setValue",'');
					}else {
						msgShow('系统提示', '添加管理员角色失败！', 'error');
					}

				});
    });
    //注册用户确认
	$('#btnEp').click(function() {
        serverLogin();
    });
    $('#btnCancel').click(function() {
    	$('#w').window('close');
    	$("#userName").textbox("setValue",'');
    	$("#txtPass").textbox("setValue",'');
    	$("#txtRePass").textbox("setValue",'');
    });
    //注册用户
	function serverLogin() {
		var userName = $("#userName").textbox("getValue");
    	var passWord = $("#txtPass").textbox("getValue");
        var rePass = $("#txtRePass").textbox("getValue");
        if ($('#roleId').val() == '') {
            msgShow('系统提示', '该组织机构不存在管理角色！请创建', 'warning');
            return false;
        }
        if (userName == '') {
            msgShow('系统提示', '请输入用户名！', 'warning');
            return false;
        }
        if (passWord == '') {
            msgShow('系统提示', '请输入密码！', 'warning');
            return false;
        }else {
        	var len = passWord.length;
        	if (len < 6||len > 20) {
        		 	msgShow('系统提示', '请输入6~20位密码', 'warning');
        		 	return false;
            	}
		}
        if (rePass == '') {
            msgShow('系统提示', '请在一次输入密码！', 'warning');
            return false;
        }
        if (passWord != rePass) {
            msgShow('系统提示', '2次密码不一致！', 'warning');
            return false;
        }
        $.ajax({
        	type:'POST',
        	url:'${pageContext.request.contextPath}/oesGroup/addGroupAdministrator',
			data:{
					appId:'${appId}',
					groupId:'${groupCode}',
					roleId:$('#roleId').val(),
					appUserName:userName,
					passWord:passWord
				},
			dataType:'json',
        	success:function(data) {
            		if (data.status=='1') {
            			msgShow('系统提示', '注册成功！', 'warning');
            			$('#w').window('close');
            			$("#userName").textbox("setValue",'');
            			$("#txtPass").textbox("setValue",'');
            			$("#txtRePass").textbox("setValue",'');
					}else {
						msgShow('系统提示', '注册不成功！'+data.errMsg, 'warning');
					}
        			
            	}
            });
        
	}
	function openWindow() {
         $('#w').window({
             title: '注册管理员用户',
             width: 300,
             modal: true,
             shadow: true,
             closed: true,
             height: 300,
             resizable:false,
             closable:false
         });
     }
    function openRoleWin() {
		$('#roleWin').window({
            title: '创建管理员',
            width: 300,
            modal: true,
            shadow: true,
            closed: true,
            height: 200,
            resizable:false,
            closable:false
        });
	}
	function getTree() {
		 $('#loading').show();  
			//加载菜单树
			  $.get('${pageContext.request.contextPath}/oesGroup/tree?appId=${appId}',
						function (data) {
				  			$('#tt').tree({data: data});
				  			selected();
				  			$('#loading').hide();  
						}
					  );
	}
	 $(function(){ 
		 openRoleWin();
		 openWindow();
		 getTree();
	 });
	//创建组织机构管理员用户
	function CreateGroupAdministrator() {
		 $('#w').window('open');
	}
	
	//创建组织机构管理员角色
	function CreateGroupRole() {
		if ($('#roleId').val() != '') {
            msgShow('系统提示', '该组织机构已经存在管理员角色！', 'warning');
            return false;
        }	
		$('#roleWin').window('open');
	}
	//取消选中
	function cancelAuthorizeRes() {
		 getTree();
	}
	//勾选机构拥有的功能
	function selected(){
		initialResIds="";
		$.post('${pageContext.request.contextPath}/oesGroup/getRes?groupCode=${groupCode}&appId=${appId}',function(data){
			if(data.resourceIds!=null){
				$.each(data.resourceIds,function(i,m){
					var node=$("#tt").tree('find',"r"+m);
					if(node!=null){
						if(node.ismodule=="1"){
								$("#tt").tree('check',node.target);
						}
						initialResIds+=node.id.replace('r','')+",";
					}
				});
			}
		},"json");
	}
		
	//提示信息
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
	
	//展开某节点
	function expand(node){
    	$('#deptree1').tree('expandTo', node.target);
    	if($('#deptree1').tree('getChildren', node.target)!=null){
    		$('#deptree1').tree('expand', node.target);
    	}
    }
	
	//授权确认
	function submitAuthorizeRes(){
		$('#loading').show();   
		checkedResIds="";
		var select=$('#tt').tree('getChecked', 'checked');
		$.each(select, function(i, n) {
			if(n.ismodule==1){
				if(n.id.substring(0,1)=='r'){
					checkedResIds+=n.id.substring(1,n.id.length)+",";
				}
			}
		});
		getIds();
		var url='${pageContext.request.contextPath}/oesGroup/authorizeRes?groupCode=${groupCode}&addIds='+addIds+'&delIds='+delIds+'&appId=${appId}';
        $.post(url, function(data) {
            if(data.result==true){
            	$('#loading').hide(); 
             	msgShow('系统提示', '恭喜，授权资源成功！', 'info');
             	initialResIds=checkedResIds;
            }else{
             	msgShow('系统提示', '授权资源失败！', 'error');
              	//刷新
              	 getTree();
              	 $('#loading').hide(); 
            }
        }); 
       
	}
	
	//获取添加删除的resId
    function getIds(){
    	addIds="";
    	delIds="";
    	if(checkedResIds != initialResIds){//resource前后不一致
    		var checkedRes=checkedResIds.split(",");
    		var initialRes=initialResIds.split(",");
    		for(var i=0;i<checkedRes.length;i++){
    			var bool=false;
    			for(var j=0;j<initialRes.length;j++){
    				if(checkedRes[i]===initialRes[j]){
    					bool=true;
    				}
    			}
    			if(!bool){
    				addIds+=checkedRes[i]+",";
    			}
    		}
    		for(var i=0;i<initialRes.length;i++){
    			var bool=false;
    			for(var j=0;j<checkedRes.length;j++){
    				if(initialRes[i]===checkedRes[j]){
    					bool=true;
    				}
    			}
    			if(!bool){
    				delIds+=initialRes[i]+",";
    			}
    		}
    	}
	}
	
</script>
</html>