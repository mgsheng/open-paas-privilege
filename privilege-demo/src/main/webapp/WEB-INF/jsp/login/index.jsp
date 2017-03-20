<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html lang="en">
<head >
<meta charset="utf-8" />
<title>OES教务系统</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="OES教务" name="description" />
<meta content="" name="author" />
<link href="${pageContext.request.contextPath}/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/assets/layouts/layout/css/themes/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${pageContext.request.contextPath}/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/global/css/iconFont/iconfont.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
<script src="${pageContext.request.contextPath}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script src="http://10.100.133.80:8630/ImplyWCookie.aspx?jsessionId=${jsessionId}"></script>

<script type="text/javascript">

//添加导航菜单
function addNav(data) {
	 $.each(data, function(i, n) {
	 		var menulist ='<li class="nav-item"><a href="javascript:;" class="nav-link nav-toggle">'+
                '<i class="icon-diamond"></i><span class="title">'+n.text+'</span><span class="arrow"></span></a>';
            
	 		menulist +='<ul class="sub-menu">';
	        $.each(n.children, function(j, o) {
		        menulist+='<li class="nav-item  " style="background:url(${pageContext.request.contextPath}/'+o.attributes.menuRule+') no-repeat 20px;">'+
			        '<a  href="${pageContext.request.contextPath}/user/getMenu?menuId='+o.id+'" title="'+o.text+'" id="'+o.id+'" close="true"  class="nav-link iframeify">'+
                	'<span class="title">'+o.text+'</span></a></li>';
	         })
	 		menulist += '</ul></li>';
	 		$('#tapMenu').append(menulist);
	 });
}		
	//设置修改密码窗口
	function openPwd() {
    	$('#w').window({
        	title: '修改密码',
        	width: 300,
        	modal: true,
        	shadow: true,
        	closed: true,
        	height: 400,
        	resizable:false
    	});
	}
	//关闭窗口
	function closePwd() {
    	$('#w').window('close');
    	
	}
	function openWin() {
    	$('#w').window('open');
    	$('#txtOLdPass').val('');
		$('#txtNewPass').val('');
    	$('#txtRePass').val('');
	} 

//修改密码
	function serverLogin() {
		var $oldpass = $('#txtOLdPass');
		var $newpass = $('#txtNewPass');
    	var $rePass = $('#txtRePass');
    	if ($oldpass.val() == '') {
       	 	msgShow('系统提示', '请输入旧密码！', 'warning');
        	return false;
    	}
    	if ($newpass.val() == '') {
        	msgShow('系统提示', '请输入密码！', 'warning');
        	return false;
    	}else {
    		var a=$newpass.val().length;
    		if(a<6||a>20){
    		 	msgShow('系统提示', '请输入6~20位密码', 'warning');
    		 	return false;
        	}
		}
    	if ($rePass.val() == '') {
        	msgShow('系统提示', '请在一次输入密码！', 'warning');
        	return false;
    	}

    	if ($newpass.val() != $rePass.val()) {
        	msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
        	return false;
    	}
    	var userName="${username}";
    	$.post('${pageContext.request.contextPath}/user/update',
          	{newpass:$newpass.val(),oldpass:$oldpass.val(),userName:"${username}"},
           	function(data) {
             if (data.status=="1") {
        			msgShow('系统提示', '恭喜，密码修改成功！', 'info');
        			closePwd();
        			$newpass.val('');
                    $rePass.val('');
			}else if (data.status=="2"){
				msgShow('系统提示', data.errMsg, 'info');
			}else {
				msgShow('系统提示', data.errMsg, 'info');
				$newpass.val('');
                $rePass.val('');
			}
              
   	 	});
    
	}
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
	
        $(function() {
        	closePwd();
        	var data = ${menus};
        	if (data.status == "0") {
            	alert(data.errMsg);
            } else {
                if (data.menus.length<=0) {
					alert("没有相应菜单");
                } else {
                       //添加导航菜单
                    addNav(data.menus);
				}
            }
            $('#loginOut').click(function() {
                   window.location.href="${pageContext.request.contextPath}/user/loginOut";
            });
        });
		
    </script>
 
</head>
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed" style="overflow:hidden">
	<div class="page-wrapper">
            <!-- BEGIN HEADER -->
            <div class="page-header navbar navbar-fixed-top">
                <!-- BEGIN HEADER INNER -->
                <div class="page-header-inner ">
                    <!-- BEGIN LOGO -->
                    <div class="page-logo">
                        <a href="#">
                            <img src="${pageContext.request.contextPath}/${logo}" alt="logo" style="width: 140px ;height:45px;margin:0" class="logo-default" /> </a>
                        <div class="menu-toggler sidebar-toggler">
                            <span></span>
                        </div>
                    </div>
                    <!-- END LOGO -->
                    <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                    <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
                        <span></span>
                    </a>
                    <!-- END RESPONSIVE MENU TOGGLER -->
                    <!-- BEGIN TOP NAVIGATION MENU -->
                    <div class="top-menu">
                        <ul class="nav navbar-nav pull-right">
                            <!-- BEGIN USER LOGIN DROPDOWN -->
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                            <li class="dropdown dropdown-user">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <img alt="" class="img-circle" src="../assets/layouts/layout/img/avatar3_small.jpg" />
                                    <span class="username username-hide-on-mobile"> Open </span>
                                    <i class="fa fa-angle-down"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-default">
                                    <li>
                                        <a href="page_user_profile_1.html">
                                            <i class="icon-user"></i> 个人信息 </a>
                                    </li>
                                    <li>
                                        <a href="#"   onclick="openWin()">
                                            <i class="icon-key"></i> 修改密码  </a>
                                    </li> 
                                    <li>
                                        <a href="#"  data-toggle="modal" data-target="#myModal">
                                            <i class="icon-key"></i> 退出 </a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="clearfix"> </div>
            <div class="page-container">
                <div class="page-sidebar-wrapper">
                    <div class="page-sidebar navbar-collapse collapse">
                        <ul id="tapMenu" class="page-sidebar-menu page-header-fixed" data-keep-expanded="false" data-auto-scroll="false" data-slide-speed="200">
                            <li class="sidebar-toggler-wrapper hide">
                                <div class="sidebar-toggler">
                                    <span></span>
                                </div>
                            </li>
                            <li class="nav-item start active open">
                                <a href="${pageContext.request.contextPath}/user/getHomePage" title="首页" id="000-000" close="false" class="nav-link active nav-toggle iframeify">
                                    <i class="icon-home"></i>
                                    <span class="title">首页</span>
                                    
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="page-content-wrapper">
                    <div class="page-content">
                       <div class="theme-panel hidden-xs hidden-sm">
                            <div class="toggler"> </div>
                            <div class="toggler-close"> </div>
                            <div class="theme-options">
                                <div class="theme-option theme-colors clearfix">
                                    <span> 主题 </span>
                                    <ul>
                                        <li class="color-default current tooltips" data-style="default" data-container="body" data-original-title="Default"> </li>
                                        <li class="color-light2 tooltips" data-style="light2" data-container="body" data-html="true" data-original-title="Light 2"> </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

                                <div class="portlet">
                                    <div class="portlet-body">
                                        <div class="tabbable-custom tabbable-tabdrop">
                                            <ul class="nav nav-tabs"></ul>
                                            <div class="tab-content">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                        
                    </div>
                </div>
           
           	</div>
           	<div class="page-footer">
                <div class="page-footer-inner"> 2017 &copy; 奥鹏教育
                    <a target="_blank" href="http://www.open.com.cn/">OPEN</a> &nbsp;|&nbsp;
                    <a href="http://www.open.com.cn/" title="Open" target="_blank">OPEN</a>
                </div>
                <i class="icon-size-fullscreen pull-right btn-fullscreen"></i>
                <div class="scroll-to-top">
                    <i class="icon-arrow-up"></i>
                </div>
            </div>
           	
     </div>
             <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
							</button>
							<h4 class="modal-title" id="myModalLabel">
							</h4>
						</div>
						<div class="modal-body">
						是否退出登陆
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">
							关闭
							</button>
							<button type="button" class="btn btn-primary" id="loginOut">
								退出
							</button>
						</div>
					</div>
				</div>
			</div> 

	
	<div id="w" class="easyui-window" title="修改密码" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 200px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>旧密码：</td>
						<td><input id="txtOLdPass" type="password" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input id="txtNewPass" type="password" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" type="password" class="txt01" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" onclick="serverLogin()"
					href="javascript:void(0)"></a> <a id="btnCancel" onclick="closePwd()"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)"></a>
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
<script src="${pageContext.request.contextPath}/assets/global/plugins/bootstrap-tabdrop/js/bootstrap-tabdrop.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/layout/scripts/layout.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/layout/scripts/theme.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/global/scripts/quick-sidebar.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/global/scripts/quick-nav.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/global/scripts/quick-sidebar.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/layouts/global/scripts/quick-nav.min.js" type="text/javascript"></script>
</html>