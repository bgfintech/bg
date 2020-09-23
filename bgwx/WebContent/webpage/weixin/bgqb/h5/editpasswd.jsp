<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
    <title></title>
    <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
    <link href="plug-in/weixin/merchant/css/reset.css?100" rel="stylesheet" type="text/css" />
    <link href="plug-in/weixin/merchant/css/common.css?100" rel="stylesheet" type="text/css" />
</head>
<body>
<input type="hidden"  id="openid"  value="${openid}"/>
<div class="tip">密码为8-12个字符，其中包含字母及数字。</div>
<div class="pass_con">
  <div class="old_pass clearfix">
    <i class="old_passIco fl"></i>
    <span class="old_pass_des fl">原密码</span>
    <input type="password" name="" id="oldpwd" class="comInput" />
  </div>
  <div class="old_pass clearfix">
    <i class="new_passIco fl"></i>
    <span class="old_pass_des fl">新密码</span>
    <input type="password" name="" id="newpwd" class="comInput"/>
  </div>
  <div class="old_pass clearfix">
    <i class="confir_passIco fl"></i>
    <span class="old_pass_des fl">确认密码</span>
    <input type="password" name="" id="newpwd1" class="comInput"/>
  </div>
</div>
<div class="edit_complete">
  <a href="javascript:;" onclick="editpw();" class="quit_btn">确定</a>
</div>
</body>
<script>
function editpw(){
	var newpwd = $("#newpwd").val();
	var newpwd1 = $("#newpwd1").val();
	var openid = $("#openid").val();
	if(newpwd!=newpwd1){
		alert("两次输入新密码不一致");
		return;
	}
	var oldpwd = $("#oldpwd").val();
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?editpasswd",
            data: {
                oldpwd:oldpwd,
           		newpwd:newpwd,
           		openid:openid
            },
            success:function(data) {   
                if(data.SUCCESS == "00"){
                  alert("密码修改成功");
                  $("#newpwd").val("");
                  $("#newpwd1").val("");
                  $("#oldpwd").val("");
                }else{
                  alert(data.FAIL);
                }
            }
        });
	
}
</script>
</html>