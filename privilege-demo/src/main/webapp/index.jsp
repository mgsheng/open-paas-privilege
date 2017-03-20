<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>奥鹏教育</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" charset="utf-8">
    <link href="${pageContext.request.contextPath}/css/default.css"
	rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/pages/login-yb.css"
	rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css" />
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js" type="text/javascript"></script>
    <script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.1.2.2.js"></script>
    <script language="JavaScript">
        if(window != top){
            top.location.href="/";
            window.location.reload();
        }
    </script>
</head>
<body>
	<header>
    <div class="head">
        <img src="${pageContext.request.contextPath}/images/login/logo-yb.jpg">
    </div>
</header>
<div class="con">
    <div class="login">
        <div class="l_con">
            <p class="l_text">登录</p>
            <div class="l_input">
                <input type="text" id="username" name="username" placeholder="用户名"><br>
                <input type="password" id="password" name="password" placeholder="密码" >
            </div>
            <a href="#"><button class="l_btn" onclick="btnSubmit()">登录</button></a>
            <p class="fr cz" onclick="clear()"><a href="javascript:void(0)" ></a>重置</p>
        </div>
    </div>
</div>
<footer>
    <p>版权所有：奥鹏教育 Copyright ©2003-2015 open.com.cn ALL rights reserved</p>
</footer>
<script type="text/javascript">
	function msgShow(title, msgString, msgType) {
		$.messager.alert(title, msgString, msgType);
	}
    var login = {};
    login.restoreCookies = function(){
        var username=document.getElementById("username");
        if(!username.value){
            username.value=login.getCookie("username");
        }
    };
    login.setCookie = function(key, val){
        document.cookie=key+"="+escape(val)+";expires="+(new Date(2099,12,31)).toGMTString();
    };
    login.getCookie = function(key){
        var _b=new RegExp("(^| )"+key+"=([^;]*)(;|$)","gi");
        var _c=_b.exec(document.cookie);
        return unescape((_c||[])[2]||"");
    };
    login.init = function(focused){
        if(focused){
            var c = document.getElementById(focused)||document.getElementsByName(focused)[0];
            if(c!=null){
                c.focus();
            }
        }
        login.restoreCookies();
    };
    login.loginSubmit = function (form,event){
        login.setCookie("username",document.getElementById("username").value);
        return true;
    };
    login.init("username"); 
    
    	function btnSubmit(){
				var username=$('#username').val();
				var password=$('#password').val();
				if (username == '') {
					msgShow('系统提示', '请输入用户名！', 'warning');
	                return false;
	            }
				if (password == '') {
	                msgShow('系统提示', '请输入密码！', 'warning');
	                return false;
	            }
				var appId=$('#appId').val();
				$.post("${pageContext.request.contextPath}/user/loginVerify",
					{username:username,password:password,appId:appId},
	   				function(data){
	   					if(data.flag){
	   						window.location.href="${pageContext.request.contextPath}/user/login";
	   					}
	   					else{
	   						jQuery('#error_code').html(data.errorCode);
	   					}
	   				}
   				);
			}
</script>


</body></html>