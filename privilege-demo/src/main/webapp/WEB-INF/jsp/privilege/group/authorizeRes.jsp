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
				</span> &nbsp;&nbsp;&nbsp;&nbsp; <span style="float: right;"> <a
					href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true"
					id="ok" onclick="submitAuthorizeRes();">确认</a> <a href="#"
					class="easyui-linkbutton" iconCls="icon-undo" plain="true"
					id="undo" onclick="cancelAuthorizeRes();">取消</a>
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
	
</body>
<script>
	var initialResIds="";//用于存放修改界面中选中的resource
	var checkedResIds="";//存放选中的resource
	var addIds="";//存放修改时添加的权限Id
	var delIds="";//存放修改时删除的权限Id
   
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
		 getTree();
	 });
	
	
	
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