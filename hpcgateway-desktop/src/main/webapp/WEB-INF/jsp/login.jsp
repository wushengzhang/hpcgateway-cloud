<%@page language="java" pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  

<!DOCTYPE html> 
<html style="width:100%;height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Login</title>
<link href='<%=request.getContextPath()%>/css/hpcgateway.css' type="text/css" rel='stylesheet'/>
<script src="<%=request.getContextPath()%>/js/lib/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/jquery.i18n.properties.js"></script>
</head>
<body class="body" style="width:100%;height:100%;">


<header style="position:absolute;top:0px;left:0px;right:0px;height:190px;">
</header>

<div id="content" style="position:absolute;top:150px;bottom:60px;left:0px;right:0px;vertical-align:middle;text-align:center;">
    <div style="width:400px;margin:0 auto;border:1px solid #0996e9;padding:20px;box-shadow: 2px 2px 5px 9px #0996e9;
">
<!--  
        <p style="width:100%;text-align:left;margin:0 auto;">
        <img src="../../images/logo1.png" style="width:100px;height:20px;">
        </p>
-->
    <h1 style="font-size:28px;">Cluster Manager</h1>
<form id="form1" action="dologin" method="POST">
<table style="margin:auto;">
<tr>
    <td colspan="2">
    <label style="color:red;">${message }</label>
    </td>
</tr>
<tr>
	<td>
		<label id="username">用户</label>
	</td>
	<td>
		<input type="text" name="username" style="width:160px;height:25px;" value="${username}" placeholder="username">
	</td>
</tr>

<tr>
	<td style="">
		<label id="password">密码</label>
	</td>
	<td>
		<input type="password" name="password" style="width:160px;height:25px;" placeholder="password">
	</td>
</tr>

<tr>
	<td colspan="3">&nbsp;</td>
</tr>
<tr>
	<td colspan="2" style="text-align:center;">
		<button type="submit" id="login" style="width:200px;height:30px;background:#0996E9;">Login</button>
	</td>
</tr>
</table>
</form>
</div>
    </div>

<footer style="position:absolute;bottom:0px;left;0px;height:60px;">
</footer>    

<script>
var setupLoginPage = function()
{
	$("title").empty().append($.i18n.map["login.title"]);
	$("label#title").empty().append($.i18n.map["login.title"]);
	$("label#username").empty().append($.i18n.map["login.username"]);
	$("label#password").empty().append($.i18n.map["login.password"]);
	$("button#login").empty().append($.i18n.map["login.login"]);
  $("h1").empty().append($.i18n.map["sys.name"]);
};

$(document).ready(function(){
	var lang = lang || navigator.language;
	console.log("lang = "+lang);
	$.i18n.properties({
		 name:'hpcgateway',
		 path: app.contextPath + '/js/login/', 
		 mode:'map', 
		 language: lang,
		 callback: function() {
			 setupLoginPage();
		 }
	}); 
});
</script>
</body>
</html>
