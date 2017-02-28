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
	<div class="easyui-panel" title="模块管理" style="width:100%;max-width:100%;padding:20px 30px;height:540px;">
	<div style="padding:2px 5px; text-align: right;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reload" onclick="reload()">reload</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRoot">AddRoot</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addMenu">AddMenu</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add">AddResFun</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete2" ></a>
	</div>
	<div class="easyui-panel" style="padding:5px;height: 95%;overflow-x:scroll;">
		  <ul id="deptree"  style="height: 100%"class="easyui-tree" > 
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
						<td><input id="moduleUrl" type="text" name="moduleUrl" class="txt01" />
						</td>
					</tr>
					<tr >
						<td>图标：</td>
						<td>
							<select id="icon"></select><a id="clearIcon" class="easyui-linkbutton" icon="icon-clear" href="javascript:void(0)">清空图标</a> 
							<div id="sp"></div>
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
<script>
	//清空所选图标
	$('#clearIcon').click(function(){
		  $('#icon').combo('setValue', null).combo('setText', null);
		});
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
            $('#icon').combo('setValue', null).combo('setText', null);
        }
        function closeFunction() {
            $('#function').window('close');
        }
      //添加或修改功能
        $('#addFunct2').click(function(){
        	var optUrl = $('#opturl').val();
        	if(optUrl == ''){
            	msgShow('系统提示', '请输入Url！', 'warning');
                return false;
            }
        	var resourceId = $('#resourceId2').val();
        	var functionId = $('#functionId').val();
        	
        	var optId = $('#operName').combobox('getValue');
        	var optName=$('#operName').combobox('getText');
        	if(functionId!=""){
        		$.post('${pageContext.request.contextPath}/module/detail',
                    	{'functionId':functionId,'optUrl':optUrl,'operationId':optId,'appId':'${appId}'},
                    	function(data){
                    		if(data.returnMsg=='2'){
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
                    		}else if(data.returnMsg=='0') {
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
                             	//reload();
                            	$('#function').window('close');
                            	
                    		}else if(data.returnMsg=='0'){
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
        var menuIconUrl='${menuIconUrl}';
        menuIconUrl=menuIconUrl.substring(1); 
        //添加菜单或修改菜单
        function serverLogin() {
            var name = $('#moduleName').val();
            var code = $('#code').val();
            var moduleUrl= $('#moduleUrl').val();
            var displayOrder= $('#display_order').val();
            var menuRule=$('#icon').combobox('getValue');
            if(menuRule!=null&&menuRule!=""){
            	  menuRule=menuIconUrl+menuRule;
                }
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
				url='${pageContext.request.contextPath}/module/addModuel';
		    }else{
				url='${pageContext.request.contextPath}/module/detail';
		    }
             $.post(url, {
                 	'name':name,
                 	'code':code,
                 	'parentId':parentId,
                 	'status':status,
                 	'displayOrder':displayOrder,
                 	'menuLevel':menuLevel,
                 	'menuRule':menuRule,
                 	'url':moduleUrl,
                 	'menuId':menuId,
                 	'resourceId':resourceId,
                 	'appId':'${appId}'
                 },function(data) {
                if(data.returnMsg=='1'){
                    //清除图标组合框的值
                    $('#icon').combo('setValue', null).combo('setText', null);
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
    									'parentId':parentId,
    									'menuRule':menuRule             				
                         			}
                         		}]});
                 			
						}
                 		//添加图标
                 		var nn = $('#deptree').tree('find', data.menuId);
                 		if(menuRule!=null&&menuRule!=""){
                     			$(nn.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+menuRule+")");
                     	}else {
                				$(nn.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
						}
                 		
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
                     				'menuLevel':parseInt(menuLevel)+1,
                     				'menuRule':menuRule   

                     			}
                     		}]});
                 		//添加图标
                 		var nn = $('#deptree').tree('find', data.resourceId);
                 		//menuRule不为空显示选择的图标
                 		if(menuRule!=null&&menuRule!=""){
                 			$(nn.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+menuRule+")");
                 		}else {
                 			$(nn.target).children(".tree-icon").addClass("icon icon-mini-add");
						}
                 	}
                 	reset();
                	$('#wmodule').window('close');
                }else if(data.returnMsg=='2'){
                 	msgShow('系统提示', '恭喜，修改成功！', 'info');
                 	$('#icon').combo('setValue', null).combo('setText', null);
                 	close();
                	reset();
                	var node = $('#deptree').tree('getSelected');
                	if(resourceId!==null&&resourceId!=""&&typeof(resourceId)!="undefined"){//资源菜单
                		//menuRule不为空显示选择的图标
                		if(menuRule!=null&&menuRule!=""){
                 			$(node.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+menuRule+")");
                 		}else {
                 			$(node.target).children(".tree-icon").removeAttr("style");
                 			$(node.target).children(".tree-icon").addClass("icon icon-mini-add");
    					}
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
								'menuRule':menuRule
                  			}
                  		}); 
                	}else {
                    	//menuRule不为空显示选择的图标
                		if(menuRule!=null&&menuRule!=""){
                 			$(node.target).children(".tree-icon").css("background","url(${pageContext.request.contextPath}/"+menuRule+")");
                 		}else {
                 			$(node.target).children(".tree-icon").css("background","");
                 			$(node.target).children(".tree-icon").removeClass('tree-file').addClass("tree-folder");
    					}
                		$('#deptree').tree('update',{//目录菜单
                  			target: node.target,
                  			'text':name,
                  			'attributes':{
                  				'parentId':parentId,
                  				'menuCode':code,
                  				'status':status,
                  				'dislayOrder':displayOrder,
                 				'menuLevel':parseInt(menuLevel)+1,
                 				'menuRule':menuRule
                  			}
                  		}); 
					}
                	
                 	$('#wmodule').window('close');
                }else if(data.returnMsg=='0'){
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
	     //点击图片设置组合框的值
	     function imgClick(obj) {
       	  	 var v = $(obj).attr('value');
             var s = $(obj).attr('text');
             $('#icon').combo('setValue', v).combo('setText', s).combo('hidePanel');
		}
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
	    	$('#icon').combo({
                required:true,
                editable:false ,
                width:255 ,
                panelHeight:300 
            });
	    	 $('#sp').appendTo($('#icon').combo('panel'));
	         //读取所有图标并显示
	    	 $.post("${pageContext.request.contextPath}/module/getIcon",function(data){
		    	 	$.each(data.icon,function(i,n){
		    	 			var img='<span>'
			                +'<img onclick="imgClick(this)" src="${pageContext.request.contextPath}/images/icon/'+n+'" value="'+n+'" text="'+n+'" style="width: 20px;height: 20px;"></img>'
			                +'</span>';
		    	 			$('#sp').append(img);
			    	 	});
		    	 });
	    	 
		     openPwd();
		     openFunction();
		    
            
            //删除
          $('#delete').click(function() {
    			if(node){
       			  	if(node.ismodule=="0"){
     			  	 	menuId=node.id;
     		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
     		    	 	if(childrenNodes.length>0){
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
     		    	 	url=encodeURI('${pageContext.request.contextPath}/module/deleteMenu?menuId='+menuId+'&resourceId='+resourceId+'&appId=${appId}');
       		     	}else if (node.ismodule=="1") {
       		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
       		    	 	console.log(childrenNodes);
       		    	 	if(childrenNodes.length>0){
       		    		 	$.each(childrenNodes,function(i,n){
       		    				 functionIds.push(n.id);
       			    	 	});
       		    		 	functionIds.join(",");
       		    		 	delFunctions(functionIds);
       		    		 }
       		    	 	
       		    	 	resourceId=node.id;
       		    		menuId=node.attributes.menuId;
       		    	 	url=encodeURI(encodeURI('${pageContext.request.contextPath}/module/deleteMenu?menuId='+menuId+'&resourceId='+resourceId+'&appId=${appId}'));
       				}else {
       					functionId=node.id;
       					url=encodeURI('${pageContext.request.contextPath}/module/delFunction?functionId='+functionId+'&appId=${appId}');
       				}
    		
    				 	if (boo){
    					 	$.post(url, function(data) {
    			                if(data.returnMsg=='1'){
    			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
    			                 	close();
    			                 	var Nodes = $('#deptree').tree('getSelected');
    			                 	$('#deptree').tree('pop',Nodes.target);
    			                 	getRoot();
    			                }else if(data.returnMsg=='0'){
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

          $('#delete2').click(function() {
              
         	 var node = $('#deptree').tree('getSelected');
  	   		 if(node==null){
  	   			msgShow('系统提示', '请选中要删除的数据', 'info');
  	   		 }
  	   		 if(node){
	   				$.messager.confirm('系统提示', '是否确定删除?', function(r){
	   					if(r){
	   						var functionIds=[];
	      		     		var menuIds=[];
	      		     		var resourceIds=[];
	      			 		var url;
	      					if(node.ismodule=="0"){
	      	  			  	 	//menuId=node.id;
	      	  			  	 	//本菜单id
	      	  			  		menuIds.push(node.id);
	      	  		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
	      	  		    	 	if(childrenNodes.length>0){
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
	      	  		    		 	
	      	  		    	 		}
	      	  		    	 		url=encodeURI('${pageContext.request.contextPath}/module/deleteModuel?menuId='+menuIds+'&resourceId='+resourceIds+'&functionId='+functionIds+'&appId=${appId}');
	      	    		     	}else if (node.ismodule=="1") {
	      	    		    	 	var childrenNodes = $('#deptree').tree('getChildren',node.target);
	      	    		    	 	console.log(childrenNodes);
	      	    		    	 	if(childrenNodes.length>0){
	      	    		    		 	$.each(childrenNodes,function(i,n){
	      	    		    				 functionIds.push(n.id);
	      	    			    	 	});
	      	    		    		 	functionIds.join(",");
	      	    		    		 }
	      	    		    	 	
	      	    		    	 	resourceIds.push(node.id);
	      	    		    	 	menuIds.push(node.attributes.menuId);
	      	    		    	 	menuIds.join(",");
	      			    		 	resourceIds.join(",");
	      			    		 	url=encodeURI('${pageContext.request.contextPath}/module/deleteModuel?menuId='+menuIds+'&resourceId='+resourceIds+'&functionId='+functionIds+'&appId=${appId}');
	      	    				}else {
	      	    					functionIds.push(node.id);
	      	    					functionIds.join(",");
	      	    					console.log(functionIds);
	      	    					url=encodeURI('${pageContext.request.contextPath}/module/deleteModuel?functionId='+functionIds+'&appId=${appId}');
	      	    				}
	      	 					$.post(url,function(data) {
	      	 			             if(data.returnMsg=='1'){
	      	 			             	msgShow('系统提示', '恭喜，删除成功！', 'info');
	      	 			             	var Nodes = $('#deptree').tree('getSelected');
	      	 			             	$('#deptree').tree('pop',Nodes.target);
	      	 			                getRoot();
	      	 			             }else if(data.returnMsg=='0'){
	      	 			                 msgShow('系统提示', '删除失败！', 'info');
	      	 			                 close();
	      	 			                 reload();
	      	 			             }
	      	 			        }); 
	      	 			     	
	 	   				}
  	   			});
	   		}
      });
  		
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
			         var value=node.attributes.menuRule;
			         value=value.substr(value.lastIndexOf("/")+1);
			         $('#icon').combo('setValue', value).combo('setText', value);
				}
	            
            }else{
           		 msgShow('系统提示', '请选择需要添加的节点！', 'info');
            }  
        });
		    //清空
			function reset(){
				jQuery('#parentName').val('');
				jQuery('#parentId').val('');
				jQuery('#moduleName').val('');
				jQuery('#moduleUrl').val('');
				jQuery('#code').val('');
				jQuery('#display_order').val('');
				$('#functionId').val('');
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
				var menuLevel = data.attributes.menuLevel-1;
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
				getTree();
			} 
			
	
</script>
</html>