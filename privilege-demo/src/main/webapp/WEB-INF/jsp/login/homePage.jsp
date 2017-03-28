<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head id="Head1">
<meta charset="UTF-8">
	<script src="${pageContext.request.contextPath}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/layouts/layout/css/themes/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
    <link href="${pageContext.request.contextPath}/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
    <link rel="shortcut icon" href="favicon.ico" /> 
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/global/css/iconFont/demo.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/global/css/iconFont/iconfont.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/pages/css/system_management.min.css"/>
    <link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/easyui.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/master.css" rel="stylesheet" type="text/css" /> 
	<link href="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/icon.css" rel="stylesheet" type="text/css" /> 
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script src="${pageContext.request.contextPath}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/global/plugins/jquery-easyui/themes/insdep/jquery.insdep-extend.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	//点击折叠菜单
	function menuClick(obj) {
		var menu = $(obj).parent().next();
		var boo = menu.css('display');
		if (boo == 'none') {
			menu.css('display','block');
		} else {
			menu.css('display','none');
		} 
	}

	//记录最近访问菜单
	function latestClick(obj) {
		var menu = $(obj);
		var tabTitle = menu.children('.name').text();//菜单名称
		var menuId = menu.attr("id");//菜单id
		if (menuId != "") {
			window.setTimeout(function(){
				$.post('${pageContext.request.contextPath}/user/saveLatestVisit',
					{
						menuId : menuId,
						menuName : tabTitle,
					},function(data){
				});
			
			},5000); 
		} 
	 
	}

	//添加菜单
	function addMenu(data) {
		var menu = '';
		var url = '';
		//添加最近访问菜单
	 	$.each(data.latestVisit, function(i, n) {
			url = n.url;
			var string = url.substring(0,1);
			if (string == "/") {
				url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
			} else {
				url = 'http://'+url;
			}
			var menuRule = 'icon iconfont icon-iconfont-LearningCenter';
			if (n.menuRule.length > 0) {
				menuRule = n.menuRule;
			}
			menu +=	'<li><a href="'+url+'" title="'+n.name+'" id="'+n.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
            		'<div class="bg_bor"> <i class="'+menuRule+'"></i></div>'+
            		'<div class="name" style="text-align:center">'+n.name+'</div></a></li>';
 		});
		$('#latestVisit').append(menu);
		menu = '';
		//添加常用菜单
		$.each(data.frequentlyUsedMenu, function(i, n) {
			var menuRule = 'icon iconfont icon-iconfont-LearningCenter';
			if (n.menuRule.length > 0) {
				menuRule = n.menuRule;
			}	
			url = n.url;
			var string = url.substring(0,1);
			if (string == "/") {
				url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
			}else{
				url = 'http://'+url;
			}
			menu +=	'<li><a href="'+url+'" title="'+n.name+'" id="'+n.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
            		'<div class="bg_bor"> <i class="'+menuRule+'"></i></div>'+
            		'<div class="name" style="text-align:center">'+n.name+'</div></a></li>';
		});	
		$('#frequentlyUsedMenu').append(menu);
	}
		
    $(function() {
       	closeWin();
        var data = ${menus};
        //添加最近访问和常用菜单
        addMenu(data);
        //获取菜单tree
        getTree();
       });
        //获取树菜单
    function getTree() {
    	$.ajax({type:'GET',
    		url:'${pageContext.request.contextPath}/user/tree',
    		data:{
    				appId:'${appId}',
    				appUserId:'${appUserId}'
    			},
    		success:function(data) {
    				if ( data.status == "0") {
    					msgShow('系统提示', '您没有菜单！', 'info');
    				} else {
    					var json = data.tree;
    					if (json.length > 0) {
    						$('#deptree').tree({data: json});
    						selected();
    					}
    				}
    		}	
    	});
    }
    function Win() {
         $('#w').window({
                title: '角色添加',
                width: 550,
                modal: true,
                shadow: true,
                closed: true,
                height: 500,
                resizable:false
            });
    }
      	//打开窗口
    function openWin() {
         $('#w').window('open');
    }
         //关闭窗口
    function closeWin() {
         $('#w').window('close');
    }
      //弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
    function msgShow(title, msgString, msgType) {
    	$.messager.alert(title, msgString, msgType);
    }
    //关闭窗口
    function cancel() {
        closeWin();
        getTree();
	}
          
         //确认添加常用菜单
    function btnEnt() {
    	//资源菜单id
    	var resId = [];
    	var select = $('#deptree').tree('getChecked', 'checked');
    	$.each(select, function(i, n) {
    		if (n.ismodule == 1) {
    			resId.push(n.id);
    		}
    	});
    	resId.join(",");
    	$.messager.confirm('系统提示', '您确定修改么?', function(r) {
              if (r) {
                  $.ajax({
                      type:'POST',
                	  url:'${pageContext.request.contextPath}/user/saveMenu?appId=${appId}&appUserId=${appUserId}&resId='+resId,
                	  success:function(data) {
                				if (data.status == "0") {
                					msgShow('系统提示', '设置失败！', 'info');
                				} else {
                					msgShow('系统提示', '设置成功！', 'info');
                					closeWin();
                					getTree();
                				}
                	   }	
                   });
               }
         });
    }
    	//勾选用户常用菜单
    function selected(){
    	$.post('${pageContext.request.contextPath}/user/getFrequentlyMenu?appUserId=${appUserId}',function(data){
    			if (data.resourceId != null) {
    				$.each(data.resourceId,function(i,m){
    					var node = $("#deptree").tree('find',m);
    					if (node != null) {
    						if (node.ismodule == "1") {
    							$("#deptree").tree('check',node.target);
    						}
    					}
    				});
    			}
    	},"json");
    }
    </script>
</head>
<body   class="page-content-white" >
	<noscript>
		<div
			style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
			<img
				src="${pageContext.request.contextPath}/images/CategorizeMenu.png"
				alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div class="page-content" >
   		 <div class="portlet box default">
   		 	<div class="portlet-title">
                 <div class="caption">最近访问菜单 </div>
                 <div class="actions pull-left">
                        &nbsp;&nbsp;
                 </div>
                 <div class="tools pull-left " onclick="menuClick(this)">
                      <a href="javascript:;" class="collapse"> </a>
                 </div>
             </div>
             <div class="portlet-body">
                 <ul class="icon_lists clear" id="latestVisit">
                 
                 </ul>
             </div>
   		 </div>
   		  <div class="portlet box default">
   		 	<div class="portlet-title">
                 <div class="caption">常用菜单 </div>
                 <div class="actions pull-left">
                        &nbsp;&nbsp;
                 </div>
                 <div class="tools pull-left " onclick="menuClick(this)">
                      <a href="javascript:;" class="collapse"> </a>
                 </div>
             </div>
             <div class="portlet-body">
                 <ul class="icon_lists clear" id="frequentlyUsedMenu">
                 	 <li>
                         <a href="javascript:;"  onclick="openWin();">
                             <div class="bg_bor">
                                  <i class="icon fa fa-plus-square"></i>
                                    </div>
                             <div class="name" style="text-align: center;">添加常用菜单</div>
                         </a>
                     </li>
                 </ul>
             </div>
   		 </div>
	</div>

	<div id="w" class="easyui-window" title="常用菜单管理" collapsible="false" hidden="hidden"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 500px; height: 500px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<div class="easyui-panel" style="padding:5px;height: 400px;widows:300px;margin-top:5px;overflow-y:scroll;">
				  <ul id="deptree"  style="height: 100%;width: 200px" class="easyui-tree" 
					 data-options="method:'get',lines:true,animate: true,checkbox:true"> 
			 	  </ul>
				</div>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="ok" class="easyui-linkbutton" icon="icon-ok" onclick="btnEnt()"
					href="javascript:void(0)"> 确定</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reload" onclick="getTree()">刷新</a>
					 <a id="btnCancel" onclick="cancel()"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
</body>
	 <script src="${pageContext.request.contextPath}/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
    
    <script src="${pageContext.request.contextPath}/assets/global/scripts/app.min.js" type="text/javascript"></script>
    
    <script src="${pageContext.request.contextPath}/assets/pages/scripts/system_management.js" type="text/javascript"></script>
</html>