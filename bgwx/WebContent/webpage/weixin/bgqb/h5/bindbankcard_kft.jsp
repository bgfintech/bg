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
    <link href="plug-in/weixin/merchant/css/reset.css?1001" rel="stylesheet" type="text/css" />
    <link href="plug-in/weixin/merchant/css/common.css?10099" rel="stylesheet" type="text/css" />
</head>
<body style="padding-top:0.08rem;">
<input id="openid" name="openid" type="hidden" value="${openid}" />
<input id="bankno" name="bankno" type="hidden" value="" />
<div class="bank_card">
  <%
  		boolean bool = (Boolean)request.getAttribute("isvalid");
  		if(bool){
  %>		
  	  <div class="bank_card_name clearfix">
	    <span class="bank_card_nameL">开户姓名</span>
	    <span class="bank_card_numR">${accountinfo.name }</span>
	    <input type="hidden" id="name" value="${accountinfo.name }"/>
	  </div>
	  <div class="bank_card_name clearfix">
	    <span class="bank_card_nameL">身份证号</span>
	    <span class="bank_card_numR">${accountinfo.idcard }</span>
	    <input type="hidden" id="idcard"  value="${accountinfo.idcard }"/>
	  </div>
  <%		
  		}else{
   %>
	  <div class="bank_card_name clearfix">
	    <span class="bank_card_nameL">开户姓名</span>
	    <input type="text" id="name" class="bank_card_nameR" placeholder="请输入开户姓名"/>
	  </div>
	  <div class="bank_card_name clearfix">
	    <span class="bank_card_nameL">身份证号</span>
	    <input type="text" id="idcard"  class="bank_card_nameR" placeholder="请输入身份证号"/>
	  </div>
   <%
  		}
   %>
  
  <div class="bank_card_num clearfix">
    <span class="bank_card_numL">银行卡号</span>
    <input type="text" id="bankcardno"  class="bank_card_nameR" placeholder="请输入银行卡号"/>
  </div>
  <div class="bank_card_bank clearfix">
    <span class="bank_card_bankL">选择银行</span>
    <span class="bank_card_bankR">－－－</span>
    <i class="bank_card_bank_ico"></i>
  </div>
</div>
<div class="bank_card_phone clearfix">
  <span class="bank_card_nameL">预留手机号</span>
  <input type="number" id="bankphone" class="bank_card_nameR" placeholder="请输入银行预留手机号"/>
</div>
<div class="edit_complete">
  <a href="javascript:;"  onclick="bind();" class="quit_btn">确定绑卡</a>
</div>

<div class="shadow hide"></div>
<div class="choose_bank">
  <a href="javascript:;" class="choose_bank_close"></a>
  <h3 class="choose_bank_tit">选择银行</h3>
  <ul class="choose_bank_list">
    <li class="choose_bank_listItem" bid="1051000">建设银行</li>
    <li class="choose_bank_listItem" bid="1021000">工商银行</li>
    <li class="choose_bank_listItem" bid="1031000">农业银行</li>
    <li class="choose_bank_listItem" bid="3091000">兴业银行</li>
    <li class="choose_bank_listItem" bid="3031000">光大银行</li>
    <li class="choose_bank_listItem" bid="3065810">广发银行</li>
    <li class="choose_bank_listItem" bid="3135840">平安银行</li>
    <li class="choose_bank_listItem" bid="3051000">民生银行</li>
    <li class="choose_bank_listItem" bid="1041000">中国银行</li>
    <li class="choose_bank_listItem" bid="0025840">邮储银行</li>
    
    <li class="choose_bank_listItem" bid="3085840">招商银行</li>
    <li class="choose_bank_listItem" bid="3041000">华夏银行</li>
    <li class="choose_bank_listItem" bid="3021000">中信银行</li>
    <li class="choose_bank_listItem" bid="3011000">交通银行</li>
  </ul>
</div>
</body>
</html>
<script>
$(function(){
  $('.bank_card_bankR').on('click', function() {
      $('.shadow').show();
      $('.choose_bank').stop().animate({ 'bottom': '0' }, 20).show();
  });
  $('.choose_bank_close').on('click', function() {
      $('.shadow').hide();
      $('.choose_bank').stop().animate({ 'bottom': '-100%' }, 20).hide();
  });
  $('.choose_bank_listItem').on('click', function() {
    $('.bank_card_bankR').text( $(this).text() );
    $("#bankno").val($(this).attr("bid"));
    $('.shadow').hide();
    $('.choose_bank').stop().animate({ 'bottom': '-100%' }, 20).hide();
  });
});
function bind(){
	var bankphone = $("#bankphone").val().trim();
	var name = $("#name").val().trim();
	var idcard = $("#idcard").val().trim();
	var bankcardno = $("#bankcardno").val().trim();
	var bankno = $("#bankno").val();
	var openid = $("#openid").val();
	var sz = /^[0-9]{10,32}$/;
	var sfz = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var sz11 = /^[0-9]{11}$/;
	if(name==''){
		alert("请输入姓名");
		return;
	}
	if(idcard==''||!sfz.test(idcard)){
		alert("请输入正确身份证号");
		return;
	}
	if(bankcardno==''||!sz.test(bankcardno)){
		alert("请输入正确卡号10-32位数字");
		return;
	}
	if(bankphone==''||!sz11.test(bankphone)){
		alert("请输入正确手机号");
		return;
	}
	if(bankno==''){
		alert("请选择所属银行");
		return;
	}
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?bindbankcard",
            data: {
                openid:openid,
               	bankphone:bankphone,
               	name:name,
				idcard:idcard,
				bankno:bankno,
				bankcardno:bankcardno
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   alert("绑卡成功");
                   window.location.href="acctController.do?gopersonal&openid="+openid;
                }else{
                   alert(data.FAIL);
                }
            }
        });

}

</script>