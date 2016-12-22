<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h2>Spring Security&Oauth2 Client is work!</h2>


<div>
    操作说明:
    <ol>
        <li>
            <p>
                spring-oauth-client 的实现没有使用开源项目 <a
                    href="https://github.com/spring-projects/spring-security-oauth/tree/master/spring-security-oauth2"
                    target="_blank">spring-security-oauth2</a> 中提供的代码与配置, 如:<code>&lt;oauth:client
                id="oauth2ClientFilter" /&gt;</code>
            </p>
        </li>
        <li>
            <p>
                按照Oauth2支持的grant_type依次去实现. 共5类.
                <br/>
            <ul>
                <li>authorization_code</li>
                <li>password</li>
                <li>client_credentials</li>
                <li>implicit</li>
                <li>refresh_token</li>
            </ul>
        </li>
        <li>
            <p>
                <em>
                    在开始使用之前, 请确保 <a href="http://git.oschina.net/shengzhao/spring-oauth-server" target="_blank">spring-oauth-server</a>
                    项目已正确运行, 且 spring-oauth-client.properties (位于项目的src/main/resources目录) 配置正确
                </em>
            </p>
        </li>
    </ol>
</div>
<br/>

<p>
    &Delta; 注意: 项目前端使用了 Angular-JS 来处理动态数据展现.
</p>
<hr/>

<div>
    <strong>菜单</strong>
    <ul>
          <li>
          <p><a href="addPrivilege">组织机构权限初始创建接口</a> <br/>组织机构权限初始创建接口</p>
          </li>
         <li>
            <p><a href="modifyPrivilege">组织机构权限授权接口</a> <br/>组织机构权限授权接口</p>
         <li>
            <p><a href="delPrivilege">组织机构权限删除接口</a> <br/>组织机构权限删除接口（删除当前组织机构所有权限）</p>
        </li>
         <li>
            <p><a href="getGroupPrivilege">组织机构权限查询接口</a> <br/>组织机构权限查询接口</p>
            </li>
        <li>
            <p><a href="addPrivilegeRole">角色初始创建</a> <br/>角色初始创建接口测试</p>
        </li>
         <li>
            <p><a href="modifyPrivilegeRole">角色权限修改</a> <br/>角色权限修改接口测试</p>
        </li>
          <li>
            <p><a href="delPrivilegeRole">角色删除</a> <br/>角色删除接口测试</p>
        </li>
        <li>
            <p><a href="getPrivilegeRole">角色权限查询</a> <br/>角色权限查询接口测试</p>
        </li>
          <li>
            <p><a href="addPrivilegeUserRole">用户角色初始创建</a> <br/>用户角色初始创建接口测试</p>
        </li>
        <li>
            <p><a href="modiPrivilegeUserRole">用户角色权限修改</a> <br/>用户角色权限修改接口测试</p>
        </li>
        <li>
            <p><a href="delPrivilegeUserRole">用户角色删除</a> <br/>用户角色删除接口测试</p>
        </li>
        <li>
            <p><a href="getPrivilegeUserRole">用户权限查询</a> <br/>用户权限查询接口测试</p>
        </li>
        <li>
            <p><a href="verifyPrivilegeUserRole">用户权限认证</a> <br/>用户权限认证接口测试</p>
        </li>
          <li>
            <p><a href="addMenu">菜单创建</a> <br/>菜单创建接口</p>
        </li>
         <li>
            <p><a href="modifyMenu">菜单修改</a> <br/>菜单修改接口</p>
        </li>
          <li>
            <p><a href="delMenu">菜单删除</a> <br/>菜单删除接口</p>
        </li>
          <li>
            <p><a href="getMenus">菜单查询</a> <br/>菜单查询接口</p>
        </li>
          <li>
            <p><a href="addResource">资源创建</a> <br/>资源创建接口</p>
        </li>
         <li>
            <p><a href="modifyResource">资源修改</a> <br/>资源修改接口</p>
        </li>
          <li>
            <p><a href="delResource">资源删除</a> <br/>资源删除接口</p>
        </li>
          <li>
            <p><a href="addFunction">权限功能添加接口</a> <br/>权限功能添加接口</p>
        </li>
         <li>
            <p><a href="modifyFunction">权限功能修改接口</a> <br/>权限功能修改接口</p>
        </li>
          <li>
            <p><a href="delFunction">权限功能删除接口</a> <br/>权限功能删除接口</p>
        </li>
          <li>
            <p><a href="getGroupPrivilegeTest">查询组织机构测试</a> <br/>查询组织机构测试</p>
        </li>
         <li>
            <p><a href="getUserRoleRedisPrivilegeTest">用户角色缓存查询</a> <br/>用户角色缓存查询</p>
        </li>
      
    </ul>
</div>
</body>
</html>