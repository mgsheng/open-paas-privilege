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
<style type="text/css">
	.close ul{display: none} 
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.1.2.2.js"></script>
	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
function menuClick(obj){
	var menu=$(obj).next();
	//var boo=$(obj).parent(".close").find('ul').first().css('display');
	var boo=menu.css('display');
	if(boo=='none'){
		$(obj).addClass('accordion-header-selected');
		menu.css('display','block');
	}else{
		$(obj).removeClass('accordion-header-selected');
		menu.css('display','none');
	} 
}
function GetMenuList(data, menulist) {
    if (data.children == null)
        return menulist;
    else {
        menulist += '<ul>';
        $.each(data.children, function(i, sm) {
            if (sm.attributes.baseUrl != null) {
                menulist += '<li><div style="margin-bottom:-5px"><a ref="'+sm.id+'" href="#" rel="' +sm.attributes.baseUrl + '" ><span class="icon icon-mini-add" >&nbsp;</span><span class="nav">' + sm.text+ '</span></a></div></li> ';
            }
            else {
            	 menulist += '<li class="close" style="margin-bottom: 15px" >'
            	 +'<div data-options="fit:true,border:false" style="margin-left:-8px;margin-right:-15px" class="easyui-accordion panel-header accordion-header" onclick="menuClick(this)"><span class="panel-icon icon icon-more"></span><span class=" panel-title panel-with-icon">'+ sm.text +'</span></div>';
            }
            menulist = GetMenuList(sm, menulist);
        })
        menulist += '</ul>';
    }
    return menulist;
}
function addNav(data) {

    $.each(data, function(i, sm) {
        var menulist1 = "";
        //sm 常用菜单  邮件 列表
        console.log(sm.pid);
        if(sm.pid=="0"){
        	menulist1 = GetMenuList(sm, menulist1);
            menulist1 = "<ul id='tt1' >" + menulist1.substring(4); 
            $('#nav').accordion('add', {
                title: sm.text,
                content: menulist1,
                iconCls: 'icon icon-more'
            });
          }
    });
    var nav=$('#nav');
    var childrens=nav.children();
    $.each(childrens,function(i,n){
    		$(n).addClass("easyui-panel");
    		$(n).attr('data-options','fit:true,border:true');
    		var body=$(n).children().eq(1);
    		
        });
    $('.easyui-accordion li a').click(function(){
		var tabTitle = $(this).children('.nav').text();
		var url=$(this).attr("rel");
		var string=url.substring(0,1);
		if(string=="/"){
			url = '${pageContext.request.contextPath}'+$(this).attr("rel")+"?appId=${appId}";
		}else{
			url = 'http://'+$(this).attr("rel");
			}
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
    var pp = $('#nav').accordion('panels');
    var t = pp[0].panel('options').title;
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
        	
        	if(${menus}!=null){
        		var data=${menus};
        		if(data.status=="0"){
            		alert(data.errMsg);
            	}else {
                	if(data.menus.length<=0){
						alert("没有相应菜单");
                    }else {
                    	addNav(data.menus);
					}
            		
            	}
        	}
        	
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
		style="width:230px;" id="west">
		<div id="nav" class="easyui-accordion" fit="true" border="false"  >
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