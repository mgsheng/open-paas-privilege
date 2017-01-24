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
	    <input class="easyui-textbox" name="groupId" id="groupId" label="组织机构ID" 
													prompt="选填" style="width:200px"></input> 
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="search"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" ></a>
	</div>
	<div class="easyui-panel" style="padding:5px;height: 95%;overflow-x:scroll;">
		  <ul id="deptree"  style="height: 100%"class="easyui-tree" 
			 data-options="method:'get',url:'${pageContext.request.contextPath}/department/tree?appId=${appId}'"> 
	 </ul>
	</div>
	</div>
	<!--添加模块-->
	<div id="wmodule" class="easyui-window" title="资源添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;overflow-y:scroll; ">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="resourceFrom" style="padding-top: 10px;" action="">
				<table >
					<tr>
						<td>机构名称：</td>
						
						<td>
						<input id="menuId" name="menuId" type="hidden" value=""/>
						<input id=groupName name="groupName" type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>状态：</td>
						<td>
			                 <select class="easyui-combobox" data-options="editable:false" id="status" name="status" style="width:100%">
								<option value="1">启用</option>
								<option value="0">禁用</option>
							</select>
						</td>
					</tr>
					
				</table>
				<div class="easyui-panel" style="padding:5px;margin-top:5px;overflow-y:scroll;">
				  <ul id="deptree1"  style="height: 245px;width: 200px" class="easyui-tree" 
					 data-options="method:'get'"> 
			 	  </ul>
			</div>
				</form>
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

	var initialResIds='';//用于存放修改界面中选中的resource
	var initialFunIds='';//用于存放修改界面中选中的function
	var checkedResIds='';//存放选中的resource
	var checkedFunIds='';//存放选中的function
	var addIds='';//存放修改时添加的权限Id
	var delIds='';//存放修改时删除的权限Id
	var checkIds='';//存放添加时的选中Id
	var appId=${appId};
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
        //关闭窗口
        function closePwd() {
            $('#wmodule').window('close');
        }
      $(function(){  
		  openPwd();
		 //加载树
		 $('#deptree1').tree({ 
      	 lines:true,//显示虚线效果 
      	 animate: true,
      	  checkbox:true,
            url: '${pageContext.request.contextPath}/department/buildTree2?appId=${appId}',  
        });
         //添加
		 $('#add').click(function() {
		   // $("#updateDiv").hide();
		   	clearChoose();
		   	$('#wmodule').window('open');
         });
         //编辑
         $('#edit').click(function() {
           // $("#updateDiv").show();
            initialFunIds='';
        	initialResIds='';
        	
   			var groupId=$("#groupId").val();
   					   $("#status").get(0).selectedIndex = status;//index为索引值
   					   clearChoose();
   					   $.ajax({
   						    url:"${pageContext.request.contextPath}/department/privilegeGroupQuery?appId=${appId}&groupId="+groupId, 
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
	   				            		console.log(node);
	   				            		if(node!=null){
		   				            		$('#deptree1').tree('check', node.target);
		   				            		expand(node);//展开相应菜单
	   				            		}
	   				            		initialResIds+=this.resourceId.replace('r','')+",";
	   				            	}
	   							});
	   						}
   					   });
	  					$('#wmodule').window({
			                title: '组织机构修改',
			                width: 550,
			                modal: true,
			                shadow: true,
			                closed: true,
			                height: 430,
			                resizable:false
			            });
	  					$('#wmodule').window('open');
            
            
            });
            //删除
             $('#delete').click(function() {
				var groupId=$("#groupId").val();
				 if (groupId!=null || groupId!=""){
					$.messager.confirm('系统提示', '是否确定删除?', function(r){
					   var url="${pageContext.request.contextPath}/department/removeDeptByID?id="+groupId+"&appId="+appId;
			            $.post(url, function(data) {
			            	if(data.status!=null){
			            		if(data.status=='1'){
					                  msgShow('系统提示', '恭喜，删除成功！', 'info');
					                  //刷新
						              reload();
					                }else{
					                  msgShow('系统提示', '删除失败！', 'info');
					                }
			            	}
			                
			            });
			    });
			  }
            });
            //确定提交
            $('#btnEp').click(function() {
                serverLogin();
            });
            //取消提交
            $('#btnCancel').click(function(){closePwd();});
		    });
		    
	  
        //添加資源
        function serverLogin() {
            var groupName = $('#groupName').val();
            var status= $("#status").combobox('getValue');
            var appId=${appId};
            var groupId=$('#groupId').val();
            //var method=$("#method").combobox('getValue');
            if (groupId==''&& groupName == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
          	var ui = $('#deptree1').tree('getChecked', ['checked','checked']);
        	getCheckedIds(ui,checkIds);
			var url="";
			if(groupId==null || groupId==""){
			 url=  '${pageContext.request.contextPath}/department/addDept';
		   // url=  '${pageContext.request.contextPath}/department/addDept?groupName='+groupName+'&status='+status+'&temp='+checkIds+'&appId='+appId;
		    }else{
		    getIds();
		    url=  '${pageContext.request.contextPath}/department/updateDept';
		    //url=  '${pageContext.request.contextPath}/department/updateDept?groupName='+groupName+'&status='+status+'&temp='+checkIds+'&appId='+appId+'&method='+method+'&groupId='+groupId+'&addIds='+addIds+'&delIds='+delIds;
		    }
		    url=encodeURI(encodeURI(url));
           //解析data===parentId=&resources=1&resources=3&resources=5&resources=7&name=aa&url=aa%2Faa%2Faa&code=aa&displayOrder=2&status=1
             $.post(url, 
             {
            	appId:appId,
            	groupName:groupName,
            	status:status,
            	temp:checkIds,
            	addIds:addIds,
            	delIds:delIds,
            	groupId:groupId
            },
             function(data) {
                if(data.status=='1'){
                 if(groupId==null || groupId==""){
	                 	msgShow('系统提示', '恭喜，添加成功！', 'info');
                	 }else{
                		msgShow('系统提示', '恭喜，修改成功！', 'info');
                	 }
                 close();
                 reload();
                $('#wmodule').window('close');
                }else{
                  if(groupId==null || groupId==""){
	                 	msgShow('系统提示', '恭喜，添加成功！', 'info');
                	 }else{
                		msgShow('系统提示', '恭喜，修改成功！', 'info');
                	 }
                 close();
                }
            }); 
        }
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
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
	
			function reload(){
			    var groupId=jQuery('#groupId').val();
			    $('#deptree').tree({
                  url:'${pageContext.request.contextPath}/department/tree?groupId='+groupId+'&appId=${appId}'
             });
			}
			   //查询
             $('#search').click(function() {
		     var groupId=jQuery('#groupId').val();
			 if (groupId!=''){
			  $('#deptree').tree({
                url:'${pageContext.request.contextPath}/department/tree?groupId='+groupId+'&appId=${appId}'
             });
		     }else{
		       msgShow('系统提示', '请输入组织机构id！', 'info');
		     }
              
            });
            
			function expandAll(){
			var node = $('#deptree').tree('getSelected');
			if (node){
				$('#deptree').tree('expandAll', node.target);
			} else {
				$('#deptree').tree('expandAll');
			}
		}
		//清空填入值和树选中节点并收起
		function clearChoose(){
		  $("#groupName").val('');
		  $("#status").get(0).selectedIndex = 0;//index为索引值
		    $('#deptree1').tree('collapseAll');
		    var node = $('#deptree1').tree('getChecked', ['checked','checked']);
	    	for(var i = 0;i<node.length;i++){
	       		$('#deptree1').tree('uncheck', node[i].target);
	    	}
		}  
		 function expand(node){
        	$('#deptree1').tree('expandTo', node.target);
        	if($('#deptree1').tree('getChildren', node.target)!=null){
        		$('#deptree1').tree('expand', node.target);
        	}
        }
        
	
</script>
</html>