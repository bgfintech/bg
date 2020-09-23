<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%
	session.setAttribute("lang", "zh-cn");
	String ctx = request.getContextPath();
	String msg = (String) session.getAttribute("checkuser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>布广B+</title>
<meta name="author" content="DeathGhost" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name='apple-touch-fullscreen' content='yes'>
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="address=no">
<link rel="icon" href="images/favicon.ico" type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta name="keywords" content="布广信息">
<link href="webpage/login/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="webpage/login/css/supersized.css">
<link href="webpage/login/css/style.min.css" rel="stylesheet">
<link href="webpage/login/css/login.min.css" rel="stylesheet">

<script src="plug-in/bg/js/login/jquery.js"></script>
<script src="plug-in/bg/js/login/public.js"></script>
<script src="plug-in/bg/js/login/customScrollbar.min.js"></script>
<script ID=clientEventHandlersJS language='javascript'>
    var msg = '<%=msg%>';
	if (msg.length > 0 && msg != 'null') {
		alert(msg);
	}

	function check() {
		//自动关闭消息窗口
		//var formParam = $("#formLogin").serialize();
		//alert(formParam);
		var userName = $("#userName").val();
		var password = $("#password").val();
		if (userName.length == 0) {
			alert("用户名不能为空");
			return false;
		}
		if (password.length == 0) {
			alert("用户密码不能为空");
			return false;
		}

		$.ajax({
			url : "loginController.do?checkuser",
			data : {
				userName : userName,
				password : password
			},
			dataType : "JSON",
			type : "POST",
			success : function(data) {
				if (data.success) {
					$('#formLogin').submit();
				} else {
					alert(data.msg);
				}
			}
		});
	}
</SCRIPT>
</head>
<body class="signin">
	<div class="signinpanel">
		<div class="row">
			<div class="col-sm-7">
				<div class="signin-info">
					<div class="logopanel m-b">
						<h1>欢迎使用 布广B+</h1>
					</div>
					<div class="m-b"></div>
					<h4>
						请扫描二维码<strong> 微信小程序版</strong>
					</h4>
					<div class="logopanel m-b">
						<img src="webpage/login/img/5.png" style="width:150px" />
					</div>
					<!-- <ul class="m-b">
						<li><img src="webpage/login/img/4.png" style="width:150px" /></li>
					</ul> -->
				</div>
			</div>
			<div class="col-sm-5">				
				<h2 class="no-margins">布广信息 BPlus系统</h2>
				<p class="m-t-md">用户登录</p>
				<form name="formLogin" id="formLogin" action="loginController.do?login" method="post">
					<input id="userName" name="userName" type="text" class="form-control uname" placeholder="用户名" />
					<input id="password" name="password" type="password" class="form-control pword m-b"	placeholder="密码" />
				</form>
				<button class="btn btn-success btn-block" onClick="check();">登录</button>				
			</div>
		</div>
		<div class="signup-footer">
			<div class="pull-left">京ICP备18000745号 布广信息服务有限公司</div>
		</div>
	</div>
	<ul id="supersized" class="quality" style="visibility: visible;">
		<li class="slide-0 activeslide"	style="visibility: visible; opacity: 1;">
			<div target="_blank">
				<img src="webpage/login/img/1.jpg"	style="width: 100%; height: 100%; left: 0px; top: -124px;">
			</div>
		</li>		
	</ul>
</body>
</html>
