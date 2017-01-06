<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head id="Head1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奥鹏教育</title>
<link href="${pageContext.request.contextPath}/css/default.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/icon.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.1.2.2.js"></script>
	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
var _menus;
var signature;
var timestamp;
var signatureNonce;
var appKey;
		 $.post('http://localhost:8080/privilege-demo/signature/getSignature',{appId:'${appId}'},function(data){
			  signature=data.signature;
			     timestamp=data.timestamp;
			     signatureNonce=data.signatureNonce;
			     appKey=data.appKey; 
			     $.post('${getUserPrivilegeUrl}?appId=${appId}&appUserId=${appUserId}&appKey='+
						 appKey+"&signatureNonce="+signatureNonce+"&timestamp="+timestamp+"&signature="+signature ,function(data) {
			    console.log("timestamp="+timestamp+"---appKey="+appKey+"--signatureNonce="+signatureNonce+"--signature="+signature);
			    	//是否为系统管理员
					 var isAdministrator=false;
					 $.each(data.roleList, function(i, o) {
						 if(o.roleType==2){
							 isAdministrator=true;
						 }
					 });
					 if(!isAdministrator){
						 showMenu(data);
					 }else{
						 console.log('${appId}');
						 showAllMenu('${appId}');
					 }
					    
			     });
		 }); 
			   /*  $.post('${getUserPrivilegeUrl}?appId=${appId}&appUserId=${appUserId}&appKey='+
						 appKey+"&signatureNonce="+signatureNonce+"&timestamp="+timestamp+"&signature="+signature ,function(data) {
			    console.log("timestamp="+timestamp+"---appKey="+appKey+"--signatureNonce="+signatureNonce+"--signature="+signature);
			    	//是否为系统管理员
					 var isAdministrator=false;
					 $.each(data.roleList, function(i, o) {
						 if(o.roleType==2){
							 isAdministrator=true;
						 }
					 });
					 if(!isAdministrator){
						 showMenu(data);
					 }else{
						 console.log('${appId}');
						 showAllMenu('${appId}');
					 }
					    
			     }); */
		/* 是系统管理员 显示所有菜单 */
		function showAllMenu(appid){
			var resData;
			$.post('${appResRedisUrl}',{appId:appid},function(data){
				resData=data;
				console.log(data);
			},"json");
			$.post('${appMenuRedisUrl}',{appId:appid},function(data){
				var a = [];// 创建数组
				console.log(resData);
				$.each(data.menuList, function(i, o) {
					var menu = new Object();
					var menulist='<ul>';
					if(o.parentId=="0"){
						menu.title = o.menuName;
					 	$.each(data.menuList, function(j, n) {
							if(n.parentId==o.menuId){
								$.each(resData.resourceList, function(i, m) {
							  		if(m.menuId==n.menuId){
									 	menulist += '<li><div><a ref="'+n.menuId+'" href="#" rel="' + '${pageContext.request.contextPath}'+m.baseUrl + '" ><span class="icon '+n.icon+'" >&nbsp;</span><span class="nav">' + n.menuName + '</span></a></div></li>';
							  			}
							  	  });
							}
						});
							  menulist+='</ul>';
							  menu.content = menulist;
							  a.push(menu);
					}
				}); 
				 $.each(a,function(i){
				 	$('#nav').accordion('add', {
				    	title: a[i].title,
				    	content: a[i].content,
				             //iconCls: 'icon ' 
				         });
				 });
				 $('.easyui-accordion li a').click(function(){
			    		var tabTitle = $(this).children('.nav').text();
			    		var url = $(this).attr("rel");
			    		var menuid = $(this).attr("ref");
			    		//var icon = getIcon(menuid,icon);

			    		addTab(tabTitle,url,"");
			    		$('.easyui-accordion li div').removeClass("selected");
			    		$(this).parent().addClass("selected");
			    	}).hover(function(){
			    		$(this).parent().addClass("hover");
			    	},function(){
			    		$(this).parent().removeClass("hover");
			    	});

			    	//选中第一个
			    	var panels = $('#nav').accordion('panels');
			    	var t = panels[0].panel('options').title;
			        $('#nav').accordion('select', t);  
			},"json");
		}
		function showMenu(data){
			console.log(data);
			var a = [];// 创建数组
			$.each(data.menuList, function(i, o) {
				var menu = new Object();
				var menulist='<ul>';
				if(o.parentId=="0"){
					menu.title = o.menuName;
				 	$.each(data.menuList, function(j, n) {
						if(n.parentId==o.menuId){
							$.each(data.resourceList, function(i, m) {
						  		if(m.menuId==n.menuId){
								 	menulist += '<li><div><a ref="'+n.menuId+'" href="#" rel="' +'${pageContext.request.contextPath}'+ m.baseUrl + '" ><span class="icon '+n.icon+'" >&nbsp;</span><span class="nav">' + n.menuName + '</span></a></div></li>';
						  			}
						  	  });
						}
					});
						  menulist+='</ul>';
						  console.log(menulist);
						  menu.content = menulist;
						  a.push(menu);
				}
			}); 
			 $.each(a,function(i){
			 	$('#nav').accordion('add', {
			    	title: a[i].title,
			    	content: a[i].content,
			             //iconCls: 'icon ' 
			         });
			 });
				 $('.easyui-accordion li a').click(function(){
			    		var tabTitle = $(this).children('.nav').text();
			    		var url = $(this).attr("rel");
			    		var menuid = $(this).attr("ref");
			    		//var icon = getIcon(menuid,icon);

			    		addTab(tabTitle,url,"");
			    		$('.easyui-accordion li div').removeClass("selected");
			    		$(this).parent().addClass("selected");
			    	}).hover(function(){
			    		$(this).parent().addClass("hover");
			    	},function(){
			    		$(this).parent().removeClass("hover");
			    	});

			    	//选中第一个
			    	var panels = $('#nav').accordion('panels');
			    	var t = panels[0].panel('options').title;
			        $('#nav').accordion('select', t);  
		}

	 /* var _menus = {"menus":[
					{"menuid":"","icon":"icon-sys","menuname":"用户管理",
							"menus":[
										   {"menuid":"24","menuname":"用户信息列表","icon":"icon-man","url":"${pageContext.request.contextPath}/managerUser/userList"}							]
						},{"menuid":"","icon":"icon-sys","menuname":"权限管理",
							"menus":[{"menuid":"26","menuname":"资源管理","icon":"icon-nav","url":"${pageContext.request.contextPath}/resource/index"},
									 {"menuid":"27","menuname":"模块管理","icon":"icon-nav","url":"${pageContext.request.contextPath}/module/index"},
									 {"menuid":"28","menuname":"角色管理","icon":"icon-role","url":"${pageContext.request.contextPath}/managerRole/roleMessage"},
									 {"menuid":"29","menuname":"公共权限","icon":"icon-set","url":"${pageContext.request.contextPath}/privilegePublic/index"}
								   ]
						}
				]}; */
        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }

        

        //修改密码
        function serverLogin() {
            var $newpass = $('#txtNewPass');
            var $rePass = $('#txtRePass');

            if ($newpass.val() == '') {
                msgShow('系统提示', '请输入密码！', 'warning');
                return false;
            }
            if ($rePass.val() == '') {
                msgShow('系统提示', '请在一次输入密码！', 'warning');
                return false;
            }

            if ($newpass.val() != $rePass.val()) {
                msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
                return false;
            }
            var userName="${userName}";
            $.post('${pageContext.request.contextPath}//user/updatePassword?newpass=' + $newpass.val()+'&userName='+userName, function(msg) {
                msgShow('系统提示', '恭喜，密码修改成功！', 'info');
                $newpass.val('');
                $rePass.val('');
                close();
                $('#w').window('close');
            });
            
        }

        $(function() {

            openPwd();

            $('#editpass').click(function() {
                $('#w').window('open');
            });

            $('#btnEp').click(function() {
                serverLogin();
            });

			$('#btnCancel').click(function(){closePwd();});

            $('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

                    if (r) {
                      <%
                      session.invalidate();
                      %>
                        location.href = '${pageContext.request.contextPath}/index.jsp';
                    }
                });
            });
        });
		
    </script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<noscript>
		<div
			style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
			<img
				src="${pageContext.request.contextPath}/images/CategorizeMenu.png"
				alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div region="north" split="true" border="false"
		style="overflow: hidden; height: 50px;
        background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
		<span style="float:right; padding-right:20px;padding-top: 20px;"class="head">
		
		           欢迎您<span id="realName"> ${realName}</span> <a href="#" id="editpass">修改密码</a> 
			<a href="#" id="loginOut">安全退出</a>
		</span> 
		<span style="padding-left:10px; font-size: 16px;  ">
		<img  src="${pageContext.request.contextPath}/images/open_logo.png"
			width="140" height="45" align="absmiddle" />
		</span>
	</div>
	<div region="south" split="true"
		style="height: 30px; background: #D2E0F2; ">
		<div class="footer">
		           版权所有：奥鹏教育 Copyright ©2003-2015 open.com.cn
			ALL rights reserved  登记序号：京ICP备12003892号-3 京ICP证150086号
			公安机关备案号：110102005577号-4</div>
	</div>
	<div region="west" hide="true" split="true" title="导航菜单"
		style="width:180px;" id="west">
		<div id="nav" class="easyui-accordion" fit="true" border="false">
			<!--  导航内容 -->
			
		</div>

	</div>
	<div id="mainPanle" region="center"
		style="background: #eee; overflow-y:hidden">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="首页" style="padding:20px;overflow:hidden; color:red; ">
				 <img src="${pageContext.request.contextPath}/images/welcome_1.png" style="display: block;width: 40%;margin:20px auto;">
			</div>
		</div>
	</div>


	<!--修改密码窗口-->
	<div id="w" class="easyui-window" title="修改密码" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>新密码：</td>
						<td><input id="txtNewPass" type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" type="text" class="txt01" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>

	<div id="mm" class="easyui-menu" style="width:150px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>


</body>
</html>