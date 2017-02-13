<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <meta charset="utf-8">
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js" type="text/javascript"></script>
    <script language="JavaScript">
        if(window != top){
            top.location.href="/";
            window.location.reload();
        }
    </script>
</head>
<body>

<div class="wrap">
    <form action="${pageContext.request.contextPath}/user/login" method="post" onsubmit="return login.loginSubmit(this, event);">

    <div class="login_box">
        <div class="login_title">登录</div>
        <ul>
             <li class="placeHolder userIcon">
                <input value="" id="username" name="username" placeholder="请输入用户名" class="username" type="text">
            </li>
            <li class="placeHolder pwdIcon">
                <input id="password" name="password" placeholder="请输入密码" class="pwd" type="password">
            </li>
             <li class="placeHolder pwdIcon" style="display: none">
                <input id="appId" name="appId" placeholder="请输入appId" class="appId" type="hidden">
            </li>
            <li>
                <div style="color: red;text-align: center;padding: 10px 0px 0px" id="error_code"></div>
            </li>
            
            <li class="placeHolder login_btn">
                <input id="login_button" name="login_button"  type="button"value="登 录" onclick="btnSubmit()" style="text-align: center;"  >
            </li>
            

        </ul>
    </div>
    </form>
</div>
<script type="text/javascript">
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