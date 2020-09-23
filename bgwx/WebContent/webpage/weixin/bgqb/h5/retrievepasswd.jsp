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
    <span class="old_pass_des fl" style="margin-left:13px">账号</span>
    <input type="text" name="" id="account" class="comInput" placeholder="输入账号/手机号"/>
  </div>
  <div class="old_pass clearfix">
    <span class="old_pass_des fl" style="margin-left:13px">新密码</span>
    <input type="password" name="" id="passwd" class="comInput" placeholder="输入新密码"/>
  </div>
  <div class="old_pass clearfix">
    <span class="old_pass_des fl" style="margin-left:13px">确认密码</span>
    <input type="password" name="" id="passwd1" class="comInput" placeholder="重复输入新密码"/>
  </div>
  <div class="bank_card_name clearfix">
  	<input type="button" id="vcode" style="width:0.85rem;height:0.34rem;line-height:0.34rem;border:1px solid #ecaba5;color:#e20200;border-radius:5px;font-size:0.14rem;text-align:center;" onclick="getSmsCode();" value="获取验证码">
  	<input type="text" id="smscode" style="width:1.7rem;height:0.34rem;margin-left:0.2rem;" placeholder="输入验证码"/>
  	
  </div>
</div>
<div class="edit_complete">
  <a href="javascript:;" onclick="editpw();" class="quit_btn">确定</a>
</div>
</body>
<script>
var oCutdown = $("#vcode");
var count = 60;
var timer = null;
function cutdown(){
    count--;
    oCutdown.val(count+'s').attr("disabled", "disabled").addClass('disabled');
    if( count<0 ){
      clearInterval(timer);
      count = 60;//之前count已经被改成0了，所以这里要让count回到最开始的时间
      oCutdown.val('获取验证码').removeAttr('disabled').removeClass('disabled');
    }
}
function getSmsCode(){
	var account = $("#account").val();
	var newpwd = $("#passwd").val();
	var newpwd1 = $("#passwd1").val();
	var sz11 = /^[0-9]{11}$/;
	if(isNull(account)||!sz11.test(account)){
		alert("请输入正确账号");
		$("#account").val("");
		return;
	}
	if(isNull(passwd)){
		alert("密码不能为空");
		return;
	}
	if(newpwd!=newpwd1){
		alert("两次输入新密码不一致");
		return;
	}
	cutdown();
	timer = setInterval(cutdown,1000);
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?getpwdsmscode",
            data: {
                account:account
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                	
                }else{
                   alert(data.FAIL);
                }
            }
        });
}
function editpw(){
	var newpwd = $("#passwd").val();
	var newpwd1 = $("#passwd1").val();
	var account = $("#account").val();
	if(newpwd!=newpwd1){
		alert("两次输入新密码不一致");
		return;
	}
	var smscode = $("#smscode").val();
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?changepwdbysmscode",
            data: {
                account:account,
           		passwd:newpwd,
           		smscode:smscode
            },
            success:function(data) {   
                if(data.SUCCESS == "00"){
                  alert("密码已修改,请重新登陆");
                  WeixinJSBridge.call('closeWindow');
                }else{
                  alert(data.FAIL);
                }
            }
        });
	
}
function isNull( str ){
    if ( str == "" ) return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}
</script>
</html>