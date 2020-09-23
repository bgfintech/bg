<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title></title>
        <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>        
        <link href="plug-in/weixin/merchant/css/reset.css?102" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?103" rel="stylesheet" type="text/css" />        
        <script src="plug-in/weixin/merchant/js/layer.js" type="text/javascript"></script>
		<link rel="stylesheet" href="plug-in/weixin/merchant/css/layer.css?10009" />     
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>   
    </head>
    <body style="background-color:#eee;padding:0 0.1rem;">
    	<input id="openid" name="openid" type="hidden" value="${openid}" />
        <input id="source" name="source" type="hidden" value="${source}" />
        <input id="state" name="state" type="hidden" value="${state}" />
        <input id="headimgurl" name="headimgurl" type="hidden" value="${headimgurl}" />
        <input id="wclose" name="wclose" type="hidden" value="${wclose}" />
		<div class="logIn">
		    <div class="logIn_img"><img src="plug-in/weixin/merchant/images/23.png" alt=""></div>
		</div>
		<div class="log_reg">
		    <div class="log_reg_tabBtn clearfix">
		        <div class="log_tabBtn log_reg_oper fl"><a href="javascript:;" class="log_tabBtn_con lr_on">登录</a></div>		        
		        <div class="reg_tabBtn log_reg_oper fl"><a href="javascript:;" class="log_tabBtn_con">注册</a></div>
		    </div>
		    <div class="log_reg_content">
		        <div class="log_reg_conItem">
		            <div class="log_line clearfix">
		                <span class="log_lineL fl"></span>
		                <input type="text" id="user1" class="log_lineR fl" placeholder="请输入手机号"/>
		            </div>
		            <div class="log_line clearfix">
		                <span class="reg_lineL fl"></span>
		                <input type="password" id="passwd1" class="log_lineR fl" placeholder="请输入密码"/>
		            </div>
		            <a href="acctController.do?goretrievepasswd" style="color:#658392;letter-spacing:1px;" >找回密码</a>
		            <a href="javascript:;" onclick="dosubmit(1);" class="logIn_btn">登录</a>
		            
		        </div>
		        <div class="log_reg_conItem hide">
		            <div class="log_line clearfix">
		                <span class="log_lineL fl"></span>
		                <input type="text" id="user2" class="log_lineR fl" placeholder="请输入手机号"/>
		            </div>
		            <div class="log_line clearfix">
		                <span class="reg_lineL fl"></span>
		                <input type="password" id="passwd2" class="log_lineR fl" placeholder="请输入密码"/>
		            </div>
		            <div class="log_line clearfix hide" id="rec">
		                <span class="rec_lineL fl"></span>
		                <input type="text" id="recommend" class="log_lineR fl" placeholder="请输入推荐人手机号" value="${recommend}"/>
		            </div>
		            <div class="log_line clearfix">
		            	<input type="text" id="smscode" style="width:1.7rem;height:0.34rem;margin-left:0.2rem;" placeholder="输入验证码"/>
		            	<input type="button" id="vcode" style="width:0.85rem;height:0.34rem;line-height:0.34rem;border:1px solid #ecaba5;color:#e20200;border-radius:5px;font-size:0.14rem;text-align:center;" onclick="getSmsCode();" value="获取验证码">
		            </div>
		            <a href="javascript:;" onclick="dosubmit(2);" style="top:2.2rem;" class="logIn_btn">注册</a>
		        </div>
		    </div>
		</div>
		<div class="ser_support"><span class="ser_support_ico"></span><span>由布广信息提供技术支持</span></div>
	</body>
</html>

<script>
var wclose =$("#wclose").val();
if(wclose=='1'){
	close();
}
function close(){
    window.setTimeout('close()',50); 
	WeixinJSBridge.call('closeWindow');
}

var oCutdown = $("#vcode");
var count = 30;
var timer = null;
function cutdown(){
    count--;
    oCutdown.val(count+'s').attr("disabled", "disabled").addClass('disabled');
    if( count<0 ){
      clearInterval(timer);
      count = 30;//之前count已经被改成0了，所以这里要让count回到最开始的时间
      oCutdown.val('获取验证码').removeAttr('disabled').removeClass('disabled');
    }
}
$(function(){
    $('.log_reg_oper').on('click',function(){
        $(this).find('.log_tabBtn_con').addClass('lr_on');
        $(this).siblings().find('.log_tabBtn_con').removeClass('lr_on');
        $('.log_reg_conItem').eq($(this).index()).show().siblings().hide();
    });
    var recommend = $("#recommend").val();
    if(isNull(recommend)){    	
    	$("#rec").show();
    }
});
function getSmsCode(){
	var openid=$("#openid").val();
	var user = $("#user2").val();
	var passwd = $("#passwd2").val();
	var recommend = $("#recommend").val();
	var sz11 = /^[0-9]{11}$/;
	if(isNull(user)||!sz11.test(user)){
		alert("请输入正确账号");
		$("#user2").val("");
		return;
	}
	if(isNull(passwd)){
		alert("密码不能为空");
		return;
	}
	cutdown();
	timer = setInterval(cutdown,1000);
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?getsmscode",
            data: {
                user:user,
                recommend:recommend
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                	
                }else{
                   alert(data.FAIL);
                }
            }
        });
}
function dosubmit(id){
	var user = $("#user"+id).val().trim();
	var passwd = $("#passwd"+id).val().trim();
	var smscode = $("#smscode").val();
	var recommend = $("#recommend").val().trim();
	var url1 = 'acctController.do?login';
	var url2 = 'acctController.do?regist';
	var url = '';
	if(id==1){
		url = url1;
	}else{
		url = url2;
		if(isNull(recommend)){
			alert("请输入推荐人手机号");
			return;
		}
	}
	var sz11 = /^[0-9]{11}$/;
	if(isNull(user)||!sz11.test(user)){
		alert("请输入正确账号");
		$("#user"+id).val("");
		return;
	}
	if(isNull(passwd)){
		alert("密码不能为空");
		return;
	}
	var source=$("#source").val();
    var openid=$("#openid").val();
    var state=$("#state").val();
    var headimgurl=$("#headimgurl").val();
    $.ajax({
            type: "POST",
            url: url,
            data: {
            	user:user,
            	passwd:passwd,
            	openid:openid,
            	recommend:recommend,
            	smscode:smscode
            },
            success: function(data){
               var callback =JSON.parse(data);               
               if(callback.success==false){
               		alert(callback.msg);
               		$("#smscode").val("");
               		return;
               }
               var tmp="";
   			   if(source=='gotxqrcodepay'){
   				  tmp = "creditCardController.do?goqrcodepay&openid="+openid;
   			   }else if(source=='gotxscanpay'){
   				  tmp="creditCardController.do?goscanpay&openid="+openid+"&state="+state;   				 
   			   }else if(source=='gotrans'){
   				  tmp="acctController.do?gotrans&openid="+openid;
   			   }else if(source=='gopersonal'){
   				  tmp="acctController.do?gopersonal&openid="+openid;
   			   }else if(source=='goquickpay'){
   			      tmp="acctController.do?goquickpay&openid="+openid;
   			   }else if(source=='goqpaytrans'){
   			      tmp="acctController.do?gopaytrans&openid="+openid;
   			   }else if(source=='goshare'){
   			      tmp="acctController.do?goshare&openid="+openid+"&state="+state;
   			   }else if(source=='goteam'){
   			      tmp="acctController.do?goteam&openid="+openid+"&headimgurl"+headimgurl;
   			   }else if(source=='goinsteadrepay'){
   			   	  tmp="creditCardController.do?goinsteadrepay&openid="+openid;
   			   }else{
   				  tmp="acctController.do?gologin&openid="+openid;
   			   }
   			   window.location.href=tmp;
            }
     });
}
function isNull( str ){
    if ( str == "" ) return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}
function showmsg(msg)
{
	layer.open({
		  content: msg,
		  btn: '关闭',
		  shadeClose: false,
		  yes: function(){
		    layer.open({
		      content: msg
		      ,time: 2
		      ,skin: 'msg'
		    });
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
