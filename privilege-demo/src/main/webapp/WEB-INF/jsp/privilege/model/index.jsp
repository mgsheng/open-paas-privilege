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
</head>
<body>
	<div class="easyui-panel" title="模块管理" style="width:100%;max-width:100%;padding:20px 30px;height:540px;">
	<div style="padding:2px 5px; text-align: right;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRoot">AddRoot</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addMenu">AddMenu</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add">AddResFun</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" ></a>
	</div>
	<div class="easyui-panel" style="padding:5px;height: 95%;overflow-x:scroll;">
		  <ul id="deptree"  style="height: 100%"class="easyui-tree" 
			 data-options="method:'get',url:'${pageContext.request.contextPath}/module/tree'"> 
	 </ul>
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
						<td><input id="moduleName" name="name" type="text" class="txt01" />
						</td>
					</tr>
					<tr id="url">
						<td>URL：</td>
						<td><input id="moduleUrl" type="text" name="moduleUrl"class="txt01" />
						</td>
					</tr>
					<tr>
						<td>code：</td>
						<td><input id="code" type="text" name="code" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>排序：</td>
						<td><input id="display_order" name="displayOrder"type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>状态：</td>
						<td>
			                 <select class="easyui-combobox" data-options="editable:false" id="status" name="status" style="width:100%">
								<option value="1">有效</option>
								<option value="0">无效</option>
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
						<td><input id="opturl" type="text" name="url"class="txt01" />
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="addFunct" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel1"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
</body>
<script>
	
	function getRoot(){
		var node=$('#deptree').tree('getRoots');
		$.each(node,function(i,n){
			if($(n.target).children(".tree-icon").hasClass('tree-file')){
				$(n.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
			}
			$(n.target).children(".tree-icon").addClass("tree-folder");
			var children=$('#deptree').tree('getChildren',n.target);
			$.each(children,function(i,m){
				if(m.ismodule=="0"){
					if($(m.target).children(".tree-icon").hasClass('tree-file')){
						$(m.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
					}
					//$(m.target).children(".tree-icon").addClass("tree-folder");
				}else if (m.ismodule=="1") {
					$(m.target).children(".tree-icon").addClass("icon icon-mini-add");
				}
			});
		});
	}
	$('#deptree').tree({
		onLoadSuccess:function(node, data){
			getRoot();
		},
		onClick:function(node){
			console.log("text=="+node.text);
			console.log("parentId=="+node.attributes.parentId);
			if(node.ismodule=="0"){
				console.log("menuId=="+node.id+"status=="+node.attributes.status);
				console.log("menuLevel="+node.attributes.menuLevel);
			}else if (node.ismodule=="1") {
				console.log("menuID="+node.attributes.menuId);
				console.log("menuLevel="+node.attributes.menuLevel);
				console.log("resourceId=="+node.id+"status=="+node.attributes.status+"url="+node.attributes.baseUrl);
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
        //关闭窗口
        function closePwd() {
            $('#wmodule').window('close');
        }
        function closeFunction() {
            $('#function').window('close');
        }
        $('#addFunct').click(function(){
        	var resourceId = $('#resourceId2').val();
        	var functionId = $('#functionId').val();
        	var optUrl = $('#opturl').val();
        	var optId = $('#operName').combobox('getValue');
        	var optName=$('#operName').combobox('getText');
        	if(functionId!=""){
        		$.post('${pageContext.request.contextPath}/module/editFunction',
                    	{'functionId':functionId,'optUrl':optUrl,'operationId':optId},
                    	function(data){
                    		if(data.returnMsg=='1'){
                             	msgShow('系统提示', '恭喜，修改成功！', 'info');
                             	close();
                             	 //var funNode=$('#deptree').tree('getSelected');
                             	var node = $('#deptree').tree('getSelected');
                              	$('#deptree').tree('update',{
                              		target: node.target,
                              			'text':optName,
                              			'attributes':{
                              				'optId':optId,
                              				'optUrl':optUrl
                              			}
                              		});
                             	//reload();
                            	$('#function').window('close');
                    		}else {
                    			msgShow('系统提示', '修改失败！', 'info');
                    			reload();
                            	$('#function').window('close');
							}
                        });
        	}else {
        		$.post('${pageContext.request.contextPath}/module/addFunction',
                    	{'resourceId':resourceId,'optUrl':optUrl,'operationId':optId},
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
                             	//reload();
                            	$('#function').window('close');
                    		}else{
                    			msgShow('系统提示', '添加失败！', 'info');
                             	close();
                             	reload();
                            	$('#function').window('close');
                    		}
                        });
               
			}
        });
        //添加資源
        function serverLogin() {
            var name = $('#moduleName').val();
            var code = $('#code').val();
            var moduleUrl= $('#moduleUrl').val();
            var displayOrder= $('#display_order').val();
            
            var status= $('#status').combobox('getValue');
            var parentId=$('#parentId').val();
            var menuLevel=$('#menuLevel').val();
            var menuId=$('#menuId').val();
            var resourceId=$('#resourceId').val();
            if (name == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            if (displayOrder == '') {
                msgShow('系统提示', '请输入排序！', 'warning');
                return false;
            }
            if(!$('#moduleUrl').is(':hidden') &&  moduleUrl == ''){
            	msgShow('系统提示', '请输入Url！', 'warning');
                return false;
            }
			var url="";
			
		    if(menuId==""){
				url='${pageContext.request.contextPath}/module/addMenu';
		    }else{
				url='${pageContext.request.contextPath}/module/edit';
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
                 	'resourceId':resourceId
                 },function(data) {
                if(data.returnMsg=='1'){
                 	msgShow('系统提示', '恭喜，添加成功！', 'info');
                 	$('#wmodule').window('close');
                 		var menu=$('#deptree').tree('getSelected');
                 	if(data.resourceId==null){
                 		if(parentId=="0"){
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
    									'parentId':parentId               				
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
    									'parentId':parentId               				
                         			}
                         		}]});
                 			
						}
                 		
                 		var nn = $('#deptree').tree('find', data.menuId);
                 		if($(nn.target).children(".tree-icon").hasClass('tree-file')){
            				$(nn.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
            			}
             			//$(nn.target).children(".tree-icon").addClass("icon icon-mini-add");
                 	}else{//添加资源菜单
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
                     				'menuLevel':parseInt(menuLevel)+1

                     			}
                     		}]});
                 		var nn = $('#deptree').tree('find', data.resourceId);
                 		$(nn.target).children(".tree-icon").addClass("icon icon-mini-add");
             			//$(nn.target).children(".tree-icon").removeClass("tree-file").addClass("tree-folder");
                 	}
                 	reset();
                	$('#wmodule').window('close');
                }else if(data.returnMsg=='2'){
                 	msgShow('系统提示', '恭喜，修改成功！', 'info');
                 	close();
                	reset();
                	var node = $('#deptree').tree('getSelected');
                	if(resourceId!==null&&resourceId!=""&&typeof(resourceId)!="undefined"){//资源菜单
                		$('#deptree').tree('update',{
                  			target: node.target,
                  			'text':name,
                  			'attributes':{
                  				'menuId':node.id,
                  				'baseUrl':moduleUrl,
                  				'menuCode':code,
                  				'status':status,
                  				'menuId':menuId,
                  				'baseUrl':moduleUrl,
                  				'parentId':parentId,
                 				'menuCode':code,
                  				'dislayOrder':displayOrder,
                 				'menuLevel':menuLevel,
								'parentId':parentId    
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
                 				'menuLevel':displayOrder
                  			}
                  		}); 
					}
                	
                 	$('#wmodule').window('close');
                }else{
                 	msgShow('系统提示', '系统错误！', 'info');
                	reset();
                	 close();
                }
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
	    $(function(){  
		     openPwd();
		     openFunction();
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
					}else if(node.ismodule=="00"){
						 msgShow('系统提示', '请选择子菜单', 'info');
					}else{
						$('#wmodule').window('open');
				         updateModel(node);
					}
		            
	            }else{
	           		 msgShow('系统提示', '请选择需要添加的节点！', 'info');
	            }  
            });
            //删除
          $('#delete').click(function() {
            	 var node = $('#deptree').tree('getSelected');
    		     var menuId;
    		     var resourceId;
    		     var functionId;
    		     var functionIds=[];
    		     var menuIds=[];
    		     var resourceIds=[];
    			 var url;
    			 var boo=true;
    			if(node){
    				if(node.ismodule=="00"){
       				  	msgShow('系统提示', '请选择子菜单！', 'info');
       				  	boo=false;
       			  	 }else if(node.ismodule=="0"){
       			  	 		menuId=node.id;
       		    	 		var childrenNodes = $('#deptree').tree('getChildren',node.target);
       		    	 		if(childrenNodes!=null){
       		    		 		$.each(childrenNodes,function(i,n){
       		    		 			if(n.ismodule=="1"){
       		    		 				menuIds.push(n.attributes.menuId);
       			    		 			resourceIds.push(n.id);
       		    		 			}else if (n.ismodule=="0") {
       		    		 				menuIds.push(n.id);
									}else {
										functionIds.push(n.id);
									}
       		    		 		 	
       			    	 		});
       		    		 		menuIds.join(",");
       		    		 		resourceIds.join(",");
       		    		 		functionIds.join(",");
       		    		 		if(functionIds.length>0){
    		    			 		delFunctions(functionIds);
    		    		 		}
    		    		 		if(menuIds.length>0){
    		    			 		delMenu(resourceIds, menuIds);
    		    		 		}
       		    	 		}
       		    	 		url=encodeURI('${pageContext.request.contextPath}/module/deleteMenu?menuId='+menuId+'&resourceId='+resourceId);
       		     	}else if (node.ismodule=="1") {
       		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
       		    	 	if(childrenNodes!=null){
       		    		 	$.each(childrenNodes,function(i,n){
       		    				 functionIds.push(n.id);
       			    	 	});
       		    		 	functionIds.join(",");
       		    		 	delFunctions(functionIds);
       		    		 }
       		    	 
       		    	 	resourceId=node.id;
       		    		menuId=node.attributes.menuId;
       		    	 	url=encodeURI(encodeURI('${pageContext.request.contextPath}/module/deleteMenu?menuId='+menuId+'&resourceId='+resourceId));
       				}else {
       					functionId=node.id;
       					url=encodeURI('${pageContext.request.contextPath}/module/delFunction?functionId='+functionId);
       				}
    		
    				 	if (boo){
    					 	$.post(url, function(data) {
    			                if(data.returnMsg=='1'){
    			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
    			                 	close();
    			                 	var Nodes = $('#deptree').tree('getSelected');
    			                 	$('#deptree').tree('pop',Nodes.target);
    			                 	getRoot();
    			                }else{
    			                 	msgShow('系统提示', '删除失败！', 'info');
    			                 	close();
    			                 	reload();
    			                }
    			            }); 
    			     	}
    			 }else{
		       		msgShow('系统提示', '请选择需要删除的节点！', 'info');
		     	}
              
         });
            
             $('#btnEp').click(function() {
                 serverLogin();
             });

			$('#btnCancel').click(function(){closePwd();});
			$('#btnCancel1').click(function(){closeFunction();});
			
			
});
	    	//删除子菜单
	    	function delMenu(resourceIds,menuIds){
	    		$.post('${pageContext.request.contextPath}/module/deleteMenu?resourceId='+resourceIds+'&menuId='+menuIds,
	    				function(data){});
	    	}
	    	//删除子功能
	    	function delFunctions(functionIds){
	    		$.post('${pageContext.request.contextPath}/module/delFunction?functionId='+functionIds,
	    				
	    				function(data){});
	    	}
		    //清空
			function reset(){
				jQuery('#parentName').val('');
				jQuery('#parentId').val('');
				jQuery('#moduleName').val('');
				jQuery('#moduleUrl').val('');
				jQuery('#code').val('');
				jQuery('#display_order').val('');
				$('#menuLevel').val('');
				$('#resourceId').val('');
				$('#opturl').val('');
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
				$('#opturl').val(optUrl);
				$('#operName').combobox('select',optId);
				
		    }
		     function updateModel(data){
				reset();
				if(data.id==null || data.id==0){
					return;
				}
				data=$('#deptree').tree('getSelected');
				//赋值
				var menuId;
				var resourceId;
				if(data.ismodule=="0"){
					menuId=data.id;
					$('#moduleUrl').val("");
					$('#url').hide();
				}else if (data.ismodule=="1") {
					menuId=data.attributes.menuId;
					resourceId=data.id;
					$('#resourceId').val(resourceId);
					$('#url').show();
					var url = data.attributes.baseUrl;
					$('#moduleUrl').val(url);
				}
				var parentId = data.attributes.parentId;
				var name = data.text;
				var code = data.attributes.menuCode;
				var displayOrder = data.attributes.dislayOrder;
				var status = data.attributes.status;
				var menuLevel = data.attributes.menuLevel;
				$('#menuId').val(menuId);
				if(parentId==0){
					$('#parentName').html('根节点');
				}
				else{
					var node=$('#deptree').tree('find',parentId);
					$('#parentName').html(node.text);
				}
				jQuery('#parentId').val(parentId);
				jQuery('#moduleName').val(name);
				jQuery('#code').val(code);
				jQuery('#display_order').val(displayOrder);
				jQuery('#menuLevel').val(menuLevel);

				$('#status').combobox('setValue', status);

			}
			function reload(){
			    $('#deptree').tree({
                	url:'${pageContext.request.contextPath}/module/tree'
             	});
			}
			/*function expandAll(){
				var node = $('#deptree').tree('getSelected');
				if (node){
					$('#deptree').tree('expandAll', node.target);
				} else {
					$('#deptree').tree('expandAll');
				}
			}*/
	
</script>
</html>