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
	
<script type="text/javascript">
//点击折叠菜单
function menuClick(obj) {
	var menu=$(obj).parent().next();
	var boo=menu.css('display');
	if(boo=='none'){
		menu.css('display','block');
	}else{
		menu.css('display','none');
	} 
}
function latestClick(obj) {
	var aa=$(obj);
	var tabTitle = aa.children('.name').text();
	var url=aa.attr("href");
	var string=url.substring(0,1);
	if(string=="/"){
		url = '${pageContext.request.contextPath}'+aa.attr("rel")+"?appId=${appId}";
	}else{
		url = 'http://'+aa.attr("rel");
		}
	var menuid = aa.attr("id");
	if (menuid!="") {
		$.post('${pageContext.request.contextPath}/user/saveLatestVisit',
				{
					menuId:menuid,
					menuName:tabTitle,
					
				},function(data){});
	}
	 
}

//添加菜单
function addMenu(data) {
	var menu = '';
	var url = '';
	//添加最近访问菜单
	 $.each(data.latestVisit, function(i, n) {
			url = n.url;
			var string=url.substring(0,1);
			if(string=="/"){
				url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
			}else{
				url = 'http://'+url;
			}
			menu +=	'<li><a href="'+url+'" title="'+n.name+'" id="'+n.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
            		'<div class="bg_bor"> <i class="icon iconfont icon-iconfont-LearningCenter"></i></div>'+
            		'<div class="name">'+n.name+'</div></a></li>';
 	});
	$('#latestVisit').append(menu);
	menu = '';
	$.each(data.frequentlyUsedMenu, function(i, n) {	
			url = n.url;
			var string=url.substring(0,1);
			if(string=="/"){
				url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
			}else{
				url = 'http://'+url;
			}
			menu +=	'<li><a href="'+url+'" title="'+n.name+'" id="'+n.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
            		'<div class="bg_bor"> <i class="icon iconfont icon-iconfont-LearningCenter"></i></div>'+
            		'<div class="name">'+n.name+'</div></a></li>';
	});	
	$('#frequentlyUsedMenu').append(menu);
}

        $(function() {
        		var data=${menus};
                //添加最近访问和常用菜单
                addMenu(data);
       });
		
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
                 
                 </ul>
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