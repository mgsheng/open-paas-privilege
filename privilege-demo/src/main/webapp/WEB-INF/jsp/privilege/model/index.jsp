<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
		<link href="${pageContext.request.contextPath}/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/assets/global/css/iconFont/iconfont.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.txt01{
			width: 230px;
		}
		.easyui-tabs>.tabs-panels>.panel>.panel-body-noborder{
				overflow: hidden;
		}
	
	</style>
</head>
<body style="overflow: hidden;">
	<div id="tt" class="easyui-tabs" fit="true" style="font-size:1em;">
		<div title="模块管理">
			<div style="border:0 solid;margin-bottom:0;" fit="true" >
				<!-- <div class="top" style="width: 100%;overflow:hidden; "> -->
					<div class="easyui-panel" title="模块管理" style="width:100%;max-width:100%;padding:20px 30px;height:520px;overflow:hidden;">
						<div style="padding:2px 5px; text-align: right;">
							<a href="#" class="easyui-linkbutton" iconCls="icon-set" plain="true" id="editIcon">设置图标</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reload" onclick="reload()">刷新</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRoot">添加根菜单</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addMenu">添加菜单</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add">添加资源</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit">修改</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete2" >删除</a>
						</div>
						<div class="easyui-panel" style="padding:5px;height: 95%;overflow:auto;">
		 					 <ul id="deptree"  style="height: 100%"class="easyui-tree" data-options="
		 					 animate:true,lines:true"> 
	 							</ul>
						</div>
					</div>
				<!-- </div> -->
			</div>	
		</div>
	</div>		
	<!--添加模块-->
	<div id="wmodule" class="easyui-window" title="资源添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="resourceFrom" style="padding-top: 10px;" action="">
				<table cellpadding=6>
					<tr>
					
						<td width="50px">父节点：</td>
						<td>
						<div  id="parentName"></div>
						<input id="menuId" name="menuId" type="hidden" value=""/>
						<input id="resourceId" name="resourceId" type="hidden" value=""/>
						<input type="hidden" id="parentId" name="parentId" value=""/>
						<input type="hidden" id="menuLevel" name="menuLevel" value=""/>
						</td>
						
					</tr>
					<tr>
						<td>名称：</td>
						<td><input id="moduleName" class="easyui-textbox" name="name" type="text" class="txt01" />
						</td>
					</tr>
					<tr id="url">
						<td>URL：</td>
						<td><input id="moduleUrl" class="easyui-textbox" type="text" name="moduleUrl" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>code：</td>
						<td><input id="code" type="text" class="easyui-textbox" name="code" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>排序：</td>
						<td><input id="display_order" class="easyui-textbox" name="displayOrder"type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>状态：</td>
						<td>
			                 <select class="easyui-combobox" data-options="editable:false" id="status" name="status" style="width:100%">
								<option value="0">有效</option>
								<option value="1">无效</option>
							</select>
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)"> 确定</a> 
				<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
	<!-- 资源功能 -->
	<div id="function" class="easyui-window" title="功能" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 500px; height: 300px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="resourceFrom" style="padding-top: 10px;" action="">
				<table cellpadding=6>
					<tr>
					
						<td width="50px">父节点：</td>
						<td>
						<div  id="parentName1"></div>
							<input id="resourceId2" name="resourceId" type="hidden" value=""/>
							<input id="functionId" name="functionId" type="hidden" value=""/>
						</td>
						
					</tr>
					<tr>
						<td>名称：</td>
						<td><select class="easyui-combobox" data-options="editable:false"  id="operName" name="operName" 
							style="width:100%;height:35px;padding:5px;">
						</select>
						</td>
					</tr>
					<tr>
						<td>URL：</td>
						<td><input id="opturl" class="easyui-textbox" type="text" name="url"class="txt01" />
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="addFunct2" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel1"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
	<div id='loading' style='position:absolute;left:0;width:100%;height:100%;top:0;background:#E0ECFF;opacity:0.8;filter:alpha(opacity=80);'>
			<div style='position:absolute;  cursor1:wait;left:50%;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;border:2px solid #ccc;color:#000;'> 
 				正在加载，请等待...
			</div>
	</div> 
</body>
 <script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/iframeWin/jquery.iframeWin.js"></script>
<script>
	//遍历树，显示图标
	function getRoot(){
		var node = $('#deptree').tree('getRoots');
		$.each(node,function(i,n){
			var children = $('#deptree').tree('getChildren',n.target);
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
		$('#deptree').tree({
			//资源菜单展开时，移除tree自带图标
			onExpand: function(node){
				if(node.ismodule == "1"){
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
	
	   //设置=窗口
        function openPwd() {
            $('#wmodule').window({
                title: '模块',
                width: 600,
                modal: true,
                shadow: true,
                closed: true,
                top:150,
                left:400,
                height: 400,
                resizable:false
            });
        }
       
        function openFunction() {
            $('#function').window({
                title: '功能',
                width: 500,
                modal: true,
                shadow: true,
                closed: true,
                top:150,
                left:400,
                height: 400,
                resizable:false
            });
        } 
        //更换图标
        function editIcon(menuRule){
        	var node = $('#deptree').tree('getSelected');
        	$(node.target).children('.tree-hit').next().prop("class", menuRule);
        	$('#deptree').tree('update',{
                  				target: node.target,
                  				'text':node.text,
                  				'attributes':{
                  					'baseUrl':node.attributes.baseUrl,
                  					'status':node.attributes.status,
                  					'menuId':node.attributes.menuId,
                 					'menuCode':node.attributes.menuCode,
                  					'dislayOrder':node.attributes.dislayOrder,
                 					'menuLevel':node.attributes.menuLevel,
									'parentId':node.attributes.parentId,  
									'menuRule':menuRule
                  				}
                  			}); 
        }
        //关闭窗口
        function closePwd() {
            $('#wmodule').window('close');
            $('#icon').combo('setValue', null).combo('setText', null);
        }
        function closeFunction() {
            $('#function').window('close');
        }
        //添加tab页面
		function addPanel(node){
			if ($('#tt').tabs('exists', node.text+'-设置图标')){
			 	$('#tt').tabs('select', node.text+'-设置图标');
			} else {
				 var menuId = node.attributes.menuId;
				 var menuRule = node.attributes.menuRule;//class 样式
				 var parentId = node.pid;
				 var status = node.attributes.status;//状态
				 var menuLevel = node.attributes.menuLevel;//层级
				 var dislayOrder = node.attributes.dislayOrder;//排序
				 var menuCode = node.attributes.menuCode;
				 var url = '${pageContext.request.contextPath}/module/icon?appId=${appId}&menuId='+menuId+
				 '&menuRule='+menuRule+'&parentId='+parentId+'&status='+status+'&menuLevel='+menuLevel+
				 '&dislayOrder='+dislayOrder+'&menuCode='+menuCode;
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:node.text+'-设置图标',
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}
        //设置图标按钮点击
        $('#editIcon').click(function(){
        	var node = $('#deptree').tree('getSelected');
			if (node){
				if (node.ismodule == 1) {
					var menuName = node.text;
					var menuId = node.attributes.menuId;
					var menuRule = node.attributes.menuRule;
					var parentId = node.pid;
					addPanel(node);
				} else {
					msgShow('系统提示', '请选择资源菜单！', 'warning');
				}
			} else {
				msgShow('系统提示', '请选择模块！', 'warning');
			}
        });
        
      //添加或修改功能
        $('#addFunct2').click(function(){
        	var optUrl = $("#opturl").textbox("getValue");
        	if(optUrl == ''){
            	msgShow('系统提示', '请输入Url！', 'warning');
                return false;
            }
        	var resourceId = $('#resourceId2').val();
        	var functionId = $('#functionId').val();
        	
        	var optId = $('#operName').combobox('getValue');
        	var optName=$('#operName').combobox('getText');
        	if(functionId!=""){
        		$.post('${pageContext.request.contextPath}/module/updateModule',
                    	{'functionId':functionId,'optUrl':optUrl,'operationId':optId,'appId':'${appId}'},
                    	function(data){
                    		if(data.returnMsg=='2'){
                             	msgShow('系统提示', '恭喜，修改成功！', 'info');
                             	close();
                             	var node = $('#deptree').tree('getSelected');
                              	$('#deptree').tree('update',{
                              		target: node.target,
                              			'text':optName,
                              			'attributes':{
                              				'optId':optId,
                              				'optUrl':optUrl
                              			}
                              		});
                            	$('#function').window('close');
                    		}else if (data.returnMsg == '0') {
                    			msgShow('系统提示', '修改失败！', 'info');
                    			reload();
                            	$('#function').window('close');
							}
                    		$('#function').window('close');
                });
        		$('#functionId').val('');
        	}else {
        		$.post('${pageContext.request.contextPath}/module/addModuel',
                    	{'resourceId':resourceId,'optUrl':optUrl,'operationId':optId,'appId':'${appId}'},
                    	function(data){
                    		if(data.returnMsg=='1'){
                             	msgShow('系统提示', '恭喜，添加成功！', 'info');
                             	close();
                             	 var resNode=$('#deptree').tree('getSelected');
                             	$('#deptree').tree('append',{parent: resNode.target,
                             		data:[{
                             			'id':data.functionId,
                             			'text':optName,
                             			'ismodule':'2',
                             			'attributes':{
                             				'optId':optId,
                             				'optUrl':optUrl
                             			}
                             		}]});
                            	$('#function').window('close');
                            	
                    		}else if (data.returnMsg == '0') {
                    			msgShow('系统提示', '添加失败！', 'info');
                             	close();
                             	reload();
                            	$('#function').window('close');
                    		}
                    		$('#function').window('close');
                        });
        		$('#functionId').val("");
			}
        });
        //添加菜单或修改菜单
        function serverLogin() {
        	var regex_dislayOrder = /^\d{1,}$/;
            var name =  $("#moduleName").textbox("getValue");
            var code = $("#code").textbox("getValue");
            var moduleUrl = $("#moduleUrl").textbox("getValue");
            var displayOrder = $("#display_order").textbox("getValue");
            var status = $('#status').combobox('getValue');
            var parentId = $('#parentId').val();
            var menuLevel = $('#menuLevel').val();
            var menuId = $('#menuId').val();
            var resourceId = $('#resourceId').val();
            var menuRule = '';
            if (name == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
           	console.log(displayOrder);
            if (displayOrder == '' || regex_dislayOrder.test(displayOrder) != true) {
                msgShow('系统提示', '请输入正确的排序！', 'warning');
                return false;
            }
            if (!$('#url').is(':hidden') &&  moduleUrl == ''){
            	msgShow('系统提示', '请输入Url！', 'warning');
                return false;
            }
			var url = "";
			
		    if(menuId == ""){//如果菜单Id不存在，则添加菜单，反之更新菜单
				url = '${pageContext.request.contextPath}/module/addModuel';
		    } else {
				url = '${pageContext.request.contextPath}/module/updateModule';
				var node = $("#deptree").tree('getSelected');
				menuRule = node.attributes.menuRule;
		    }
             $.post(url, {
                 	'name':name,
                 	'code':code,
                 	'parentId':parentId,
                 	'status':status,
                 	'displayOrder':displayOrder,
                 	'menuLevel':menuLevel,
                 	'url':moduleUrl,
                 	'menuId':menuId,
                 	'resourceId':resourceId,
                 	'appId':'${appId}',
                 	'menuRule':menuRule
                 },
                 function (data) {
                	if (data.returnMsg == '1') {
                 		msgShow('系统提示', '恭喜，添加成功！', 'info');
                 		$('#wmodule').window('close');
                 		var menu = $('#deptree').tree('getSelected');
                 		if (data.resourceId == null) {
                 			if (parentId == "0") {
                 				var root = $("#deptree").tree('getRoot');
                 				$('#deptree').tree('append',{parent:null,
                         			data:[{
                         				'id':data.menuId,
                         				'pid':parentId,
                         				'text':name,
                         				'ismodule':'0',
                         				'attributes':{
                         					'menuCode':code,
                         					'status':status,
                         					'dislayOrder':displayOrder,
                         					'menuLevel':parseInt(menuLevel)+1,
    										'parentId':parentId,
    										'menuRule':""                				
                         				}
                         		}]});
                 			}else {
                 				$('#deptree').tree('append',{parent: menu.target,
                         			data:[{
                         				'id':data.menuId,
                         				'pid':parentId,
                         				'text':name,
                         				'ismodule':'0',
                         				'attributes':{
                         					'menuCode':code,
                         					'status':status,
                         					'dislayOrder':displayOrder,
                         					'menuLevel':parseInt(menuLevel)+1,
    										'parentId':parentId,
    										'menuRule':""             				
                         				}
                         		}]});
                 			
							}
                 		
                 		} else {//添加资源菜单
                 			$('#deptree').tree('append',{parent: menu.target,
                     			data:[{
                     				'id':data.resourceId,
                     				'pid':parentId,
                     				'text':name,
                     				'ismodule':'1',
                     				'attributes':{
                     					'baseUrl':moduleUrl,
                     					'menuId':data.menuId,
                     					'parentId':parentId,
                     					'menuCode':code,
                     					'status':status,
                     					'dislayOrder':displayOrder,
                     					'menuLevel':parseInt(menuLevel)+1,
                     					'menuRule':""   

                     				}
                     		}]});
                     		//添加默认图标
                 			var nn = $('#deptree').tree('find', data.resourceId);
                 			$(nn.target).children(".tree-icon").attr("class","icon iconfont icon-iconfont-LearningCenter");
                 		}
                 		reset();
                		$('#wmodule').window('close');
                	}else if(data.returnMsg == '2') {
                 		msgShow('系统提示', '恭喜，修改成功！', 'info');
                 		close();
                		reset();
                		var node = $('#deptree').tree('getSelected');
                		if (resourceId != null && resourceId != "" && typeof(resourceId) != "undefined"){//资源菜单
                			$('#deptree').tree('update',{
                  				target: node.target,
                  				'text':name,
                  				'attributes':{
                  					'baseUrl':moduleUrl,
                  					'status':status,
                  					'menuId':menuId,
                 					'menuCode':code,
                  					'dislayOrder':displayOrder,
                 					'menuLevel':parseInt(menuLevel)+1,
									'parentId':parentId,  
									'menuRule':node.attributes.menuRule
                  				}
                  			}); 
                		}else {
                			$('#deptree').tree('update',{//目录菜单
                  				target: node.target,
                  				'text':name,
                  				'attributes':{
                  					'parentId':parentId,
                  					'menuCode':code,
                  					'status':status,
                  					'dislayOrder':displayOrder,
                 					'menuLevel':parseInt(menuLevel)+1,
                 					'menuRule':node.attributes.menuRule
                  				}
                  			}); 
						}
                	
                 		$('#wmodule').window('close');
                	} else if(data.returnMsg == '0'){
                 		msgShow('系统提示', '系统错误！', 'info');
                		reset();
                		close();
               	 	}
                	$('#wmodule').window('close');
            	}); 
            
        	}
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
        
        //添加根菜单
        $('#addRoot').click(function(){
        	reset();
        	$("#url").hide();
     		$('#parentName').html('根节点');
		 	$('#parentId').val('0');
		 	$('#menuLevel').val('0');
	   		$('#wmodule').window('open');
        });
        //添加不带Url菜单
        $('#addMenu').click(function(){
        	reset();
        	var node = $('#deptree').tree('getSelected');
			if (node){
				if(node.attributes.baseUrl!="" && typeof(node.attributes.baseUrl)!="undefined"){
					msgShow('系统提示', '资源菜单下不能添加菜单目录！', 'info');
				}else if(node.ismodule=="2"){
					msgShow('系统提示', '功能下不能添加菜单目录！');
				}else{
					$("#url").hide();
			 		$('#parentName').html(node.text);
				 	$('#parentId').val(node.id);
				 	$('#menuLevel').val(node.attributes.menuLevel);
			   		$('#wmodule').window('open');
				}
			}else{
				 msgShow('系统提示', '请选择父菜单！', 'info');
			}
        });
        //添加资源或功能
	     $('#add').click(function() {
	    	reset();
	     	var node = $('#deptree').tree('getSelected');
			if (node){
				if(node.ismodule=="1"){//node为resource
					$("#url").show();
			 		$('#parentName1').html(node.text);
					$('#resourceId2').val(node.id);
					$('#operName').combobox({
						url:'${pageContext.request.contextPath}/module/getAllOperation',
						valueField:'id',
						textField:'name'
					});
					$('#function').window('open');
				}else if(node.ismodule=="2"){//node为function
					msgShow('系统提示', '请选择父菜单', 'info');
				}else if(node.ismodule=="0"){//node为根菜单或不带Url菜单
					$("#url").show();
			 		$('#parentName').html(node.text);
				 	$('#parentId').val(node.id);
				 	$('#menuLevel').val(node.attributes.menuLevel);
			   		$('#wmodule').window('open');
			   		$('#menuId').val('');
				}
	     	}else{
	     		msgShow('系统提示', '请选择父菜单！', 'info');
	     	}
       });
	     
		function getTree() {
			$('#loading').show();  
	    	$.ajax({type:'GET',
				url:'${pageContext.request.contextPath}/module/tree?appId=${appId}',
				success:function(data) {
						if(data.status=="0"){
							msgShow('系统提示', '该应用无菜单！', 'info');
						}else {
							var json=data.tree;
							if (json.length>0) {
								$('#deptree').tree({data: json});
								getRoot();
							}else {
								msgShow('系统提示', '该应用无菜单！', 'info');
							}
							
						}
						$('#loading').hide();  
					}
			});
		}
	    $(function(){  
	    	getTree();
		    openPwd();
		    openFunction();
		});
		//删除菜单
		$('#delete2').click(function() {
         	 var node = $('#deptree').tree('getSelected');
  	   		 if (node == null) {
  	   			msgShow('系统提示', '请选中要删除的数据', 'info');
  	   		 }
  	   		 if (node) {
	   				$.messager.confirm('系统提示', '是否确定删除?', function(r){
	   					if (r) {
	   						var functionIds = [];
	      		     		var menuIds = [];
	      		     		var resourceIds = [];
	      			 		var url;
	      					if (node.ismodule == "0") {
	      	  			  	 	//menuId=node.id;
	      	  			  	 	//本菜单id
	      	  			  		menuIds.push(node.id);
	      	  		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
	      	  		    	 	if (childrenNodes.length > 0) {
	      	  		    	 			$.each(childrenNodes,function(i,n){
	      	  		    		 			if (n.ismodule == "1") {
	      	  		    		 				menuIds.push(n.attributes.menuId);
	      	  			    		 			resourceIds.push(n.id);
	      	  		    		 			} else if (n.ismodule == "0") {
	      	  		    		 				menuIds.push(n.id);
	      									} else {
	      										functionIds.push(n.id);
	      									}
	      	  			    	 		});
	      	  		    		 		menuIds.join(",");
	      	  		    		 		resourceIds.join(",");
	      	  		    		 		functionIds.join(",");
	      	  		    		 	
	      	  		    	 		}
	      	  		    	 		url = encodeURI('${pageContext.request.contextPath}/module/deleteModuel?menuId='+menuIds+'&resourceId='+resourceIds+'&functionId='+functionIds+'&appId=${appId}');
	      	    		     	}else if (node.ismodule == "1") {
	      	    		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
	      	    		    	 	if (childrenNodes.length > 0) {
	      	    		    		 	$.each(childrenNodes,function(i,n){
	      	    		    				 functionIds.push(n.id);
	      	    			    	 	});
	      	    		    		 	functionIds.join(",");
	      	    		    		 }
	      	    		    	 	
	      	    		    	 	resourceIds.push(node.id);
	      	    		    	 	menuIds.push(node.attributes.menuId);
	      	    		    	 	menuIds.join(",");
	      			    		 	resourceIds.join(",");
	      			    		 	url = encodeURI('${pageContext.request.contextPath}/module/deleteModuel?menuId='+menuIds+'&resourceId='+resourceIds+'&functionId='+functionIds+'&appId=${appId}');
	      	    				} else {
	      	    					functionIds.push(node.id);
	      	    					functionIds.join(",");
	      	    					url = encodeURI('${pageContext.request.contextPath}/module/deleteModuel?functionId='+functionIds+'&appId=${appId}');
	      	    				}
	      	 					$.post(url,function(data) {
	      	 			             if (data.returnMsg == '1') {
	      	 			             	msgShow('系统提示', '恭喜，删除成功！', 'info');
	      	 			             	var Nodes = $('#deptree').tree('getSelected');
	      	 			             	$('#deptree').tree('pop',Nodes.target);
	      	 			                getRoot();
	      	 			             } else if (data.returnMsg == '0') {
	      	 			                 msgShow('系统提示', '删除失败！', 'info');
	      	 			                 close();
	      	 			                 reload();
	      	 			             }
	      	 			        }); 
	      	 			     	
	 	   				}
  	   				});
	   			}
      	});
	    $('#btnEp').click(function() {
            serverLogin();
        });

		$('#btnCancel').click(function(){
			closePwd();
			});
		$('#btnCancel1').click(function(){closeFunction();});
	 	 //编辑
        $('#edit').click(function() {
	     var node = $('#deptree').tree('getSelected');
	     if (node){
				if(node.ismodule=="2"){
					$('#function').window('open');
					$('#operName').combobox({
						url:'${pageContext.request.contextPath}/module/getAllOperation',
						valueField:'id',
						textField:'name'
					});
					updataFunction(node);
				}else{
					$('#wmodule').window('open');
			         updateModel(node);
				}
	            
            }else{
           		 msgShow('系统提示', '请选择需要添加的节点！', 'info');
            }  
        });
		    //清空
			function reset(){
				jQuery('#parentName').val('');
				jQuery('#parentId').val('');
				$("#moduleName").textbox("setValue",'');
				$("#moduleUrl").textbox("setValue",'');
				$("#code").textbox("setValue",'');
				$("#display_order").textbox("setValue",'');
				$('#functionId').val('');
				$('#menuLevel').val('');
				$('#resourceId').val('');
				$("#opturl").textbox("setValue",'');
				$('#menuId').val('');
				$('#status').combobox('setValue', '1');
			}
		    function updataFunction(node){
		    	if(node.id==null || node.id==0){
					return;
				}
		    	var pnode = $("#deptree").tree("getParent",node.target);
		    	if(pnode!=null){
			    	var parentName1=pnode.text;
					$('#parentName1').html(parentName1);
		    	}
		    	var functionId=node.id;
		    	var optUrl=node.attributes.optUrl;
				var optId=node.attributes.optId;
				$('#functionId').val(functionId);
				$("#opturl").textbox("setValue",optUrl);
				$('#operName').combobox('select',optId);
				
		    }
		     function updateModel(data){
				reset();
				if (data.id == null || data.id == 0) {
					return;
				}
				data = $('#deptree').tree('getSelected');
				//赋值
				var menuId;
				var resourceId;
				if (data.ismodule == "0") {
					menuId = data.id;
					$("#moduleUrl").textbox("setValue",'');
					$('#url').hide();
				} else if (data.ismodule == "1") {
					menuId = data.attributes.menuId;
					resourceId = data.id;
					$('#resourceId').val(resourceId);
					$('#url').show();
					var url = data.attributes.baseUrl;
					$("#moduleUrl").textbox("setValue",url);
				}
				var parentId = data.attributes.parentId;
				var name = data.text;
				var code = data.attributes.menuCode;
				var displayOrder = data.attributes.dislayOrder;
				var status = data.attributes.status;
				var menuLevel = data.attributes.menuLevel-1;
				$('#menuId').val(menuId);
				if (parentId == 0) {
					$('#parentName').html('根节点');
				} else {
					var node = $('#deptree').tree('find',parentId);
					$('#parentName').html(node.text);
				}
				jQuery('#parentId').val(parentId);
				$("#moduleName").textbox("setValue",name);
				$("#code").textbox("setValue",code);
				$("#display_order").textbox("setValue",displayOrder);
				jQuery('#menuLevel').val(menuLevel);
				$('#status').combobox('setValue', status);

			}
			function reload(){
				getTree();
			} 
			
	
</script>
</html>