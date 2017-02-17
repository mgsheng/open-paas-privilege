<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>授权资源</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/dataList.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<!-- 授权资源窗口 -->
	<div class="easyui-pannel">
		<div id="authorizeResWin" class="botton"
			style="margin-top: 0; width: 50%; height: 1000px; border: 1px;">
			<div id="tbFun" style="padding: 10px 10px;">
				<span style="text-align: left;" hidden="true"> <input
					class="easyui-textbox" id="${groupId}" name="${groupName}" hidden="true">
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
</body>
<script>
	var initialResIds='';//用于存放修改界面中选中的resource
	var checkedResIds='';//存放选中的resource
	var addIds='';//存放修改时添加的权限Id
	var delIds='';//存放修改时删除的权限Id
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
	//加载菜单树
	$('#tt').tree({ 
 	  url: '${pageContext.request.contextPath}/oesGroup/tree?appId=${appId}', 
      onLoadSuccess:function(node,data){
    	 //getRoot();
    	 //勾选用户用户有的资源
    	 selected();
      }
      
   });
	   //取消选中
	function cancelAuthorizeFun() {
		 $("#tt").tree("reload");
	}
	//勾选机构拥有的功能
	function selected(){
		initialResIds="";
		$.post('${pageContext.request.contextPath}/oesGroup/getRes?groupId=${groupId}&appId=${appId}',function(data){
			if(data.resourceIds!=null){
				$.each(data.resourceIds,function(i,m){
					var node=$("#tt").tree('find',"r"+m);
					if(node!=null){
						if(node.ismodule=="1"){
							var nn=$("#tt").tree('getChildren',node.target);
							if(nn.length<=0){
								$("#tt").tree('check',node.target);
							}
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
		checkedResIds="";
		var select=$('#tt').tree('getChecked', 'checked');
		$.each(select, function(i, n) {
			if(n.ismodule==1){
				if(n.id.substring(0,1)=='r'){
					checkedResIds+=n.id.substring(1,n.id.length)+",";
				}
			}
			console.log(n.text);
		});
		getIds();
		alert("addIds:"+addIds);
		alert("delIds:"+delIds);
		var url='${pageContext.request.contextPath}/oesGroup/authorizeRes?groupId=${groupId}&addIds='+addIds+'&delIds='+delIds+'&appId=${appId}';
        $.post(url, function(data) {
        	alert(data.result);
            if(data.result==true){
             	msgShow('系统提示', '恭喜，授权资源成功！', 'info');
            }else{
             	msgShow('系统提示', '授权资源失败！', 'error');
              	//刷新
              	$('#tt').tree('reload');
            }
        }); 
	}
	
	//获取添加删除的resId
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