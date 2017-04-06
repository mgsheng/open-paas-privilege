<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/easyui.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/master.css" rel="stylesheet" type="text/css" /> 
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/icon.css" rel="stylesheet" type="text/css" /> 
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/jquery.insdep-extend.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body style="overflow: hidden;">

    <div class="easyui-panel" title="操作" style="padding-top:1%;overflow: hidden;height:100px"  >
		<form id="fm" method="post" action="/oesGroup/findGroups">
			<table cellpadding="5%"  style="margin-left:4%;">
				<tr style="width:100%;">
					<td>
						<input class="easyui-textbox" name="queryRoleName" id="queryRoleName" prompt="选填" style="width:100%;" label="角色名称:"></input> 
					</td>
					<td>
						<input id="queryGroupName" class="easyui-combobox" data-options="valueField:'groupCode',textField:'groupName'" label="机构名称:" style="width:280px;height:24px;padding:5px;">
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="loadData();" style="margin-left:3.5%;padding-bottom:11.6%;display:inline;">
							<span style="font-weight:bold;">查&nbsp;&nbsp;&nbsp;&nbsp;询</span>
							<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</a>
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();" style="margin-left:2%;padding-bottom:11.6%;display:inline;">
							<span style="font-weight:bold;">清&nbsp;&nbsp;&nbsp;&nbsp;除</span>
							<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</a>
					</td>
				</tr>
										
			</table>
		</form>
	</div>
	
	<table id="dg" class="easyui-datagrid" title="权限角色管理" style="width:100%;height:440px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
			<tr>
				<th data-options="field:'privilegeRoleId',width:280">ID</th>
				<th data-options="field:'roleName',width:200">名称</th>
				<th data-options="field:'deptName',width:150">部门名称</th>
				<th data-options="field:'groupName',width:150">机构名称</th>
				<th data-options="field:'remark',width:100">备注</th>
				<th data-options="field:'roleType',width:100,align:'right'">类型</th>
				<th data-options="field:'status',width:100,align:'right'">状态</th>
				<th data-options="field:'groupId',width:100,align:'right'" hidden="true" >groupId</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px; text-align: right;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit" onclick="editMessage();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" onclick="removeit();"></a>
	</div>
	<div id="w" class="easyui-window" title="角色添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save" 
		style="width: 300px; height: 150px; padding: 5px;top:100px;left:340px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<input id="id" type="hidden" />
					<tr style="height: 40px">
						<td>名称：</td>
						<td><input id="roleName"  class="easyui-textbox" type="text" class="txt01" value=""/>
						</td>
						<td>部门名称：</td>
						<td><input id="deptName"  class="easyui-textbox" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>状态：</td>
						<td>
			                <select  id="status" name="status" id="status" style="width:100%">
								<option value="0">有效</option>
								<option value="1">无效</option>
							</select> 
						</td>
						<td>类型：</td>
						<td>
			                <select  id="roleType" name="roleType"  style="width:100%">
								<option value="1">普通角色</option>
								<option value="2">管理员</option>
							</select> 
						</td>
					</tr>
					<tr style="height: 40px">
						<td>组织机构：</td>
						<td colspan="3">
							<input id="cc" class="easyui-combobox"  data-options="valueField:'groupCode',textField:'groupName'"  style="width:280px;height:24px;padding:5px;">
						</td>
					</tr>
					<tr style="height: 40px">
						<td>备注：</td>
						<td colspan="3"><textarea id="remark" rows="2" cols="40"></textarea>
						</td>
					</tr>
				</table>
								  
			
			<div class="easyui-panel" style="padding:5px;height: 51%;widows:300px;margin-top:5px;overflow-y:scroll;">
				  <ul id="deptree1"  style="height: 100%;width: 200px" class="easyui-tree" 
					 data-options="method:'get',lines:true,animate: true,checkbox:true"> 
			 	  </ul>
			</div>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
</body>

	<script>
	var group;//存放组织机构选择的下拉框数据
	var initialResIds = '';//用于存放修改界面中选中的resource
	var initialFunIds = '';//用于存放修改界面中选中的function
	var checkedResIds = '';//存放选中的resource
	var checkedFunIds = '';//存放选中的function
	var addIds = '';//存放修改时添加的权限Id
	var delIds = '';//存放修改时删除的权限Id
	var checkIds = '';//存放添加时的选中Id
	var roleId = '';
	var groupList = '';//存放下拉框组织机构所有数据
	var queryGroup = null;//存放查询时组织机构选择的下拉框数据
	$('#queryGroupName').combobox({
			onSelect: function(record){
				queryGroup = record;
			}
		});
	$(document).ready(function(){
		
		$.post('${pageContext.request.contextPath}/managerRole/findGroup',function (data) {
			$('#cc').combobox('loadData',data);
			$('#queryGroupName').combobox('loadData',data);
				if (data.length == 1) {
					$('#queryGroupName').combobox('select',data[0].groupCode);
				}
			groupList = data;
		});
		
		loadData();
		$.ajax({	
				type:'GET',
				url:'${pageContext.request.contextPath}/managerRole/tree?appId=${appId}',
				success:function(data) {
					if (data.status == "0"){
						msgShow('系统提示', '该无权限！', 'info');
						$('#deptree1').tree({data: []});
					} else {
						var json = data.tree;
						if (json.length>0) {
							$('#deptree1').tree({data: json});
							if (roleId.length>0) {
									selected(roleId);
							}
						} else {
							msgShow('系统提示', '无权限！', 'info');
						}
									
					}
				}
			});
		//选择组织机构
		$('#cc').combobox({
			 onSelect: function(record){
				group = record;
			} 
		});
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
		openPwd();
		$('#add').click(function() {
			//如果下拉框只有一条数据（即该用户不是系统管理员），选择
			if (groupList.length==1) {
				$('#cc').combobox('select',groupList[0].groupCode);
			}
			$("#roleName").textbox("setValue",'');
			document.getElementById("id").value=""; 
			$("#deptName").textbox("setValue",'');
		    document.getElementById("remark").value=""; 
		    $("#status").get(0).selectedIndex = 0;//index为索引值
		   	clearChoose();
		   	$('#w').window('open');
		});
		$('#btnEp').click(function() {
	   		serverUpdate();
	    });
		$('#btnCancel').click(function(){closePwd();});
	});

	function selected(roleId) {
		$.ajax({
		    url:"${pageContext.request.contextPath}/managerRole/QueryRoleDetails?appId=${appId}&id="+roleId, 
			success: function(data) {
				var node;
				$(data.nodeList).each(function(){
	            	if(this.funcId!=null && this.funcId!=""){
	            		node=$('#deptree1').tree('find',this.funcId);
	            		if(node!=null){
		            		$('#deptree1').tree('check', node.target);
		            		expand(node);//展开相应菜单
	            		}
	            		initialFunIds+=this.funcId.replace('f','')+",";
	            	}else{
	            		node=$('#deptree1').tree('find',this.resourceId);
	            		if(node!=null){
		            		$('#deptree1').tree('check', node.target);
		            		expand(node);//展开相应菜单
	            		}
	            		initialResIds+=this.resourceId.replace('r','')+",";
	            	}
				});
			}
	   });
	}
	
	//清空树选中节点并收起
	function clearChoose(){  
	    $('#deptree1').tree('collapseAll');
	    var node = $('#deptree1').tree('getChecked', ['checked','checked']);
    	for(var i = 0;i<node.length;i++){
       		$('#deptree1').tree('uncheck', node[i].target);
    	}
	}  
	//清除查询内容
	function clearForm(){
		$("#queryRoleName").textbox("setValue","");
		if (queryGroup != null) {
				$('#queryGroupName').combobox('unselect',queryGroup.groupCode);
			}
		queryGroup = null;
		var data = $('#queryGroupName').combobox('getData');
			if (data.length == 1) {
				queryGroup = data[0];
				$('#queryGroupName').combobox('select',data[0].groupCode);
			}
	}
	//加载数据
	function loadData(){
		var roleName = $("#queryRoleName").textbox("getValue");
		var groupCode = '';
		if (queryGroup != null) {
		 	groupCode = queryGroup.groupCode;
		}
		$('#dg').datagrid({
			collapsible:true,
			rownumbers:true,
			pagination: true,
			queryParams: {
					roleName: roleName,
					groupId : groupCode
				},
	        url: '${pageContext.request.contextPath}/managerRole/getRoleMessage?appId=${appId}',  
	        onLoadSuccess:function(data){
	           if (data.total<1){
	                  $.messager.alert("提示","没有符合查询条件的数据!");
	           }
	      	}
	    }); 
	}
	//设置登录窗口
    function openPwd() {
       $('#w').window({
           title: '角色添加',
           width: 550,
           modal: true,
           shadow: true,
           closed: true,
           height: 500,
           resizable:false
       });
       var id = $('#id').val();
    }
    //关闭登录窗口
    function closePwd() {
       $('#w').window('close');
    }
    
    function removeit(){
		 var roleName = $("#roleName").textbox("getValue");
		 var appId = ${appId};
		 var row = $('#dg').datagrid('getSelected');
		 if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.privilegeRoleId;
					   var url="${pageContext.request.contextPath}/managerRole/deleteRole?id="+id+"&appId="+appId;
			            $.post(url, function(data) {
			            	if(data.status!=null){
			            		if(data.status=='1'){
					                  msgShow('系统提示', '恭喜，删除成功！', 'info');
					                  //刷新
						              var url='${pageContext.request.contextPath}/managerRole/getRoleMessage';
						              reload(url,appId);
					                }else{
					                  msgShow('系统提示', '删除失败！', 'info');
					                }
			            	}
			                
			            });
				}
			   });
			}
		}
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
        //添加/修改
        function serverUpdate() {         	
        	var privilegeRoleId = $("#id").val();
        	var appId = ${appId};
        	var ui = $('#deptree1').tree('getChecked', ['checked','checked']);
        	getCheckedIds(ui,checkIds);
            var roleName = $("#roleName").textbox("getValue");
            var status= $('#status').val();
            var deptName = $("#deptName").textbox("getValue");
            var groupId = group.groupCode;
            var groupName = group.groupName;
            var roleType = $('#roleType').val();
            var remark = $('#remark').val();
            if (roleName == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            var url;
            if (privilegeRoleId == null || privilegeRoleId == ""){
                url='${pageContext.request.contextPath}/managerRole/addRole';
            } else {
            	getIds();
            	url = '${pageContext.request.contextPath}/managerRole/updateRole';
            }
            $.post(url,{
            	appId:appId,
            	roleName:roleName,
            	status:status,
            	temp:checkIds,
            	deptName:deptName,
            	groupId:groupId,
            	groupName:groupName,
            	remark:remark,
            	roleType:roleType,
            	addIds:addIds,
            	delIds:delIds,
            	privilegeRoleId:privilegeRoleId
            }, function(data) {
                if(data.status=='1'){
                	 if(privilegeRoleId==null || privilegeRoleId==""){
	                 	msgShow('系统提示', '恭喜，添加成功！', 'info');
                	 }else{
                		msgShow('系统提示', '恭喜，修改成功！', 'info');
                	 }
	                 close();
	                 $('#w').window('close');
				     var url='${pageContext.request.contextPath}/managerRole/getRoleMessage';
				     reload(url,appId);
                }else if(data.status=='0'){
                	if(privilegeRoleId==null || privilegeRoleId==""){
                		msgShow('系统提示', '添加失败！', 'info');
                	}else{
                		msgShow('系统提示', '修改失败！', 'info');
                	}
	                close();
	                $('#w').window('close');
                }
            });
        }
        //获取添加修改时需要的resId及funId
        function getCheckedIds(ui){
        	//清空之前选中的resource及function
        	checkedResIds='';
        	checkedFunIds='';
        	checkIds='';
        	
        	for(var i = 0;i<ui.length;i++){
    			//模块节点(ismodule自定义参数=0标记的是模块)
     			if(ui[i].ismodule=="0"){
     				if(checkIds.split(",").length!=0 || checkIds.split(",,").length!=0){
     					checkIds+="=";
     				}
     				id=ui[i].id.replace('m','');
     				checkIds=checkIds+id+",,,";//模块与模块区分
     			}else if(ui[i].ismodule=="2"){
     				id=ui[i].id.replace('f','');
     				//判断该方法的资源父类是否选中，如选中则不添加到checkedFunIds中
     				var pnode = $("#deptree1").tree('getParent',ui[i].target);
     				var pnodeId = pnode.id.replace('r','');
     				if(checkedResIds != ""){
 		            	var bool = false;
     					for(var j=0;j<checkedResIds.split(",").length;j++){
     						if(pnodeId == checkedResIds.split(",")[j]){
     							bool = true;
     						}
     					}
 	     				if(!bool){
 	        				checkIds=checkIds+id+",";//资源与资源区分
 	     					checkedFunIds+=id+",";
 	     				}
     				}else{
         				checkIds=checkIds+id+",";//资源与资源区分
     					checkedFunIds+=id+",";
     				}
     			}else{
     				id=ui[i].id.replace('r','');
     				checkIds=checkIds+id+",,";//模块与资源区分
     				checkedResIds+=id+",";
     			}
        	}
        }
        
        //获取添加删除的id
        function getIds(){
        	addIds='';
        	delIds='';
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
        				addIds+=checkedRes[i]+",,";
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
        				delIds+=initialRes[i]+",,";
        			}
        		}
        	}
        	if(checkedFunIds != initialFunIds){//function前后不一致
        		var checkedFun=checkedFunIds.split(",");
        		var initialFun=initialFunIds.split(",");
        		for(var i=0;i<checkedFun.length;i++){
        			var bool=false;
        			for(var j=0;j<initialFun.length;j++){
        				if(checkedFun[i]===initialFun[j]){
        					bool=true;
        				}
        			}
        			if(!bool){
        				addIds+=checkedFun[i]+",";
        			}
        		}
        		for(var i=0;i<initialFun.length;i++){
        			var bool=false;
        			for(var j=0;j<checkedFun.length;j++){
        				if(initialFun[i]===checkedFun[j]){
        					bool=true;
        				}
        			}
        			if(!bool){
        				delIds+=initialFun[i]+",";
        			}
        		}
        	}
        }
        //修改
        function editMessage(){
        	initialFunIds='';
        	initialResIds='';
        	
   			var row = $('#dg').datagrid('getSelected');
	   		if(row==null){
	   			msgShow('系统提示', '请选中要修改的数据', 'info');
	   		}
   			if (row){
   			$.messager.confirm('系统提示', '是否确定修改本条数据?', function(r){
   				if (r){
   					   var id=row.privilegeRoleId;
					   roleId=id;
   					   var name=row.roleName;
   					   var status=row.status;
   					   var roleType=row.roleType;
   					   var deptName=row.deptName;
   					   var groupName=row.groupName;
   					   var groupId=row.groupId;
   					   var remark=row.remark;
   					   $('#cc').combobox('select',groupId);
   					   document.getElementById("id").value = id; 
   					   $("#roleName").textbox("setValue",name);
   					   $("#deptName").textbox("setValue",deptName);
   					   document.getElementById("remark").value = remark; 
   					   if (roleType=='管理员') {
   						 	$("#roleType").val("2");
						}else {
							$("#roleType").val("1");
						}
					   if (status=='有效') {
						    $("#status").val('0');
						}else {
							$("#status").val('1');
						}
   					   clearChoose();
   					   $.ajax({
   						    url:"${pageContext.request.contextPath}/managerRole/QueryRoleDetails?appId=${appId}&id="+id, 
	   						success: function(data) {
	   							var node;
	   							$(data.nodeList).each(function(){
	   				            	if(this.funcId!=null && this.funcId!=""){
	   				            		node=$('#deptree1').tree('find',this.funcId);
	   				            		if(node!=null){
		   				            		$('#deptree1').tree('check', node.target);
		   				            		expand(node);//展开相应菜单
	   				            		}
	   				            		initialFunIds+=this.funcId.replace('f','')+",";
	   				            	}else{
	   				            		node=$('#deptree1').tree('find',this.resourceId);
	   				            		if(node!=null){
		   				            		$('#deptree1').tree('check', node.target);
		   				            		expand(node);//展开相应菜单
	   				            		}
	   				            		initialResIds+=this.resourceId.replace('r','')+",";
	   				            	}
	   							});
	   						}
   					   });
	  					$('#w').window({
			                title: '角色修改',
			                width: 550,
			                modal: true,
			                shadow: true,
			                closed: true,
			                height: 500,
			                resizable:false
			            });
	  					$('#w').window('open');
   					}
   			   });   			
   			}
   		}

        function expand(node){
        	$('#deptree1').tree('expandTo', node.target);
        	if($('#deptree1').tree('getChildren', node.target)!=null){
        		$('#deptree1').tree('expand', node.target);
        	}
        }
        
		function reload(url,appId){
   			$('#dg').datagrid('reload',{url: url, queryParams:{ appId:appId}, method: "post"}); 
   		}
	</script>
</html>