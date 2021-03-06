<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head id="Head1">
<meta charset="UTF-8">

    <title></title>
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
	<style type="text/css">
		a{text-decoration: none;}
	
	</style>
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
	//记录访问菜单
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
			},3000); 
		} 
	 
	}
	//添加第三级菜单
	function addNav(data) {
		var dom = '<div class="portlet box default"><div class="portlet-title"></div><div class="portlet-body" ><ul class="icon_lists clear">';
		if (data.length > 0) {
			dom += data ;
			dom += '</ul></div></div>';
			$('.page-content').append(dom);
		}
	}
	//添加菜单
	function addMenu(data) {
		var menu = '';
		var url = '';
		$.each(data, function(i, n) {
			//如果为资源菜单
			if (typeof(n.attributes.baseUrl) != "undefined") {
				url = n.attributes.baseUrl;
				var string = url.substring(0,1);
				if ( string == "/") {
					url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
				} else {
					url = 'http://'+url;
				}
				var menuRule = 'icon iconfont icon-iconfont-LearningCenter';
				if (n.attributes.menuRule.length > 0) {
					menuRule = n.attributes.menuRule;
				}
				menu +=	'<li><a href="'+url+'" title="'+n.text+'" id="'+n.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
            			'<div class="bg_bor"> <i class="'+menuRule+'"></i></div>'+
            			'<div class="name" style="text-align:center">'+n.text+'</div></a></li>';
			
			} else {
				addNav(menu);
				menu = '';
				menulist = '<div class="portlet box default"><div class="portlet-title">'+
           					'<div class="caption">'+n.text+'</div><div class="actions pull-left">'+
           					' &nbsp;&nbsp;</div><div class="tools pull-left" onclick="menuClick(this)">'+
           					'<a href="javascript:;" class="collapse"></a></div></div>';
				menulist += '<div  class="portlet-body"><ul class="icon_lists clear">';
				$.each(n.children, function(j, o) {
					var menuRule = 'icon iconfont icon-iconfont-LearningCenter';
					if (o.attributes.menuRule.length > 0) {
						menuRule = o.attributes.menuRule;
					}
					url = o.attributes.baseUrl;
					var string = url.substring(0,1);
					if (string == "/") {
						url = '${pageContext.request.contextPath}'+url+"?appId=${appId}";
					} else {
						url = 'http://'+url;
					}
					menulist += '<li><a href="'+url+'" title="'+o.text+'" id="'+o.id+'" onclick="latestClick(this)" close="true" class="nav-link iframeify">'+
                				'<div class="bg_bor"> <i class="'+menuRule+'"></i></div>'+
                				'<div class="name" style="text-align:center">'+o.text+'</div></a></li>';
				})
				menulist += '</ul></div>';
				$('.page-content').append(menulist);
			}
 	  });
	  addNav(menu);
 	
    }

	$(function() {
    	var data = ${menu};
        if (data.menus.length <= 0) {
			alert("没有相应菜单");
        } else {
            //添加菜单
            addMenu(data.menus);
		} 
        
    });
		
    </script>
</head>
<body   class="page-content-white">

	<div  class="page-content" >
   		
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