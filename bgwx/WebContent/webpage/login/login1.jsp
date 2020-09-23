<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%
  session.setAttribute("lang","zh-cn");
  
  String msg = (String)session.getAttribute("checkuser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>登录-后台管理系统</title>
<meta name="keywords"  content="设置关键词..." />
<meta name="description" content="设置描述..." />
<meta name="author" content="DeathGhost" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name='apple-touch-fullscreen' content='yes'>
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="address=no">
<link rel="icon" href="images/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="plug-in/bg/css/login/style.css" />
<script src="plug-in/bg/js/login/jquery.js"></script>
<script src="plug-in/bg/js/login/public.js"></script>
<script src="plug-in/bg/js/login/customScrollbar.min.js"></script>
<script ID=clientEventHandlersJS language='javascript'>
            var msg = '<%=msg %>';
            if(msg.length>0&&msg!='null'){
            	alert(msg);
            }
            
			function check() {				
				//自动关闭消息窗口
				var formParam = $("#formLogin").serialize();
				//alert(formParam);
				var userName = $("#userName").val();
				var password = $("#password").val();
				if(userName.length==0){
					alert("用户名不能为空");
					return false;
				}
				if(password.length==0){
					alert("用户密码不能为空");
					return false;
				}
				
				$.ajax({
					url:"loginController.do?checkuser",
					data:formParam,
					dataType:"JSON",
					type:"POST",
					success:function(data){			    		  
						if(data.success){	
							$('#formLogin').submit();
						}else{
							alert(data.msg);  
						}
					}
				});		    	
			}
			
</SCRIPT>
</head>
<body class="login-page" style="background:url() no-repeat center;">
	<section class="login-contain">
		<header>
			<h1>布广综合管理系统</h1>
			<p>management system</p>
		</header>
		<div class="form-content">
		 <form name="formLogin" id="formLogin" action="loginController.do?login" method="post" >
			<ul>
				<li>
					<div class="form-group">
						<label class="control-label">操作员账号：</label>
						<input type="text" placeholder="管理员账号..." class="form-control form-underlined" id="userName" name="userName"/>
					</div>
				</li>
				<li>
					<div class="form-group">
						<label class="control-label">操作员密码：</label>
						<input type="password" placeholder="管理员密码..." class="form-control form-underlined" id="password" name="password"/>
					</div>
				</li>
				<li>
					<label class="check-box">
						<input type="checkbox" name="remember"/>
						<span>记住账号密码</span>
					</label>
					
				</li>
				<li>
					<button class="btn btn-lg btn-primary btn-block" type="button" onClick="check();" style="background-color: #1ab394">立即登录</button>
				</li>
				<li>
					<p class="btm-info">京ICP备18000745号-1 <a href="#" target="_blank" title="DeathGhost">www.widefinance.cn</a></p>
					<address class="btm-info">布广信息服务有限公司</address>
				</li>
			</ul>
			
			</form>
		</div>
	</section>
<div class="mask"></div>
<div class="dialog">
	<div class="dialog-hd">
		<strong class="lt-title">标题</strong>
		<a class="rt-operate icon-remove JclosePanel" title="关闭"></a>
	</div>
	<div class="dialog-bd">
		<!--start::-->
		<p>这里是基础弹窗,可以定义文本信息，HTML信息这里是基础弹窗,可以定义文本信息，HTML信息。</p>
		<!--end::-->
	</div>
	<div class="dialog-ft">
		<button class="btn btn-info JyesBtn">确认</button>
		<button class="btn btn-secondary JnoBtn">关闭</button>
	</div>
</div>
</body>
</html>
