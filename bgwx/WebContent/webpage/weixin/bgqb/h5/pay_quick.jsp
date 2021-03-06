<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>快捷支付</title>
		<script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
        <link href="plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1002" rel="stylesheet" type="text/css" /> 
    </head>
    <body style="padding-top:0.08rem;">
    	
	    <input id="openid" name="openid" type="hidden" value="${openid}" />
    	<input id="bankno" name="bankno" type="hidden" value="${trans.BANKNO }" />
    	<input id="orderno" name="orderno" type="hidden" value="" />
    	<input id="bankphone" name="bankphone" type="hidden" value="" />
    	<input id="amt" name="amt" type="hidden" value="" />    	
    	<input id="ramt" type="hidden" value=""/>
    	<input id="name" type="hidden" value="${person.NAME }" />
    	<input id="idcard" type="hidden" value="${person.IDCARD }" />
    	<input id="hbankno" type="hidden" value="${person.BANKNO }" />
    	<input id="hbankcardno" type="hidden" value="${person.BANKCARDNO }" />
    	<input id="hvertify" type="hidden" value="${person.VERTIFY }" />
		<div class="pay_cash">
		  <div class="pay_cash_con clearfix">
		    <input type="number" id="tamt" class="pay_cash_conInput fl" placeholder="请输入金额" onkeyup="formatAmt(this)" onblur="checkMoney(this);"><span class="pay_cash_conIco fr">￥</span>
		  </div>
		</div>
		<div class="bank_card">
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">姓名</span>
		    <span class="bank_card_numR">${person.NAME }</span>
		    <!--  <i class="correct"></i>-->
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">身份证号</span>
		    <span class="bank_card_numR">${tidcard }</span>
		    <!--<i class="correct"></i>-->
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">银行卡号</span>
		    <input type="text" id="bankcardno" class="bank_card_nameR" placeholder="请输入银行卡号" value="${trans.BANKCARDNO }"/>
		    <!--<i class="error"></i>-->
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_bankL">发卡银行</span>
		    <span class="bank_card_bankR">${trans.BANKNAME }</span>
		    <i class="bank_card_bank_ico"></i>
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">有效期</span>
		    <input type="text" id="cardvaliddate" class="bank_card_nameR" placeholder="月份年份  比如2018年5月   0518" value=""/>
		    <!--<i class="correct"></i>-->
		  </div>
		  <div class="bank_card_bank clearfix">
		    <span class="bank_card_nameL">CVV</span>
		    <input type="number" id="cvv" class="bank_card_nameR" placeholder="卡背后最后3位数" value=""/>
		    <!--<i class="correct"></i>-->
		  </div>
		</div>
		<div class="bank_card" style="padding-bottom:0.5rem;">
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">预留手机号</span>
		    <input type="number" id="tbankphone" class="bank_card_nameR" placeholder="请输入银行预留手机号" value=""/>
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">验证码</span>
		    <input type="text" id="smscode" class="bank_card_nameR" placeholder="请输入验证码" style="width:1.0rem;" />
		    <input type="button" class="bank_vcode" onclick="getSmsCode();"  value="获取验证码" />
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">收款账号</span>
		    <span class="bank_card_numR">${tcbankcardno}</span>
		  </div>
		</div>
		<div class="shadow hide"></div>
		<div class="choose_bank">
		  <a href="javascript:;" class="choose_bank_close"></a>
		  <h3 class="choose_bank_tit">选择银行</h3>
		  <ul class="choose_bank_list">
		    <li class="choose_bank_listItem" bid="10510000">建设银行</li>
		    <li class="choose_bank_listItem" bid="10210000">工商银行</li>
		    <li class="choose_bank_listItem" bid="10410000">中国银行</li>
		     <!-- 
		    <li class="choose_bank_listItem" bid="3011000">交通银行</li>
		    <li class="choose_bank_listItem" bid="3091000">兴业银行</li>
		    <li class="choose_bank_listItem" bid="3031000">光大银行</li>
		    <li class="choose_bank_listItem" bid="3102900">浦发银行</li>
		   
		    <li class="choose_bank_listItem" bid="3085840">招商银行</li>	
		    <li class="choose_bank_listItem" bid="1031000">农业银行</li>	    
		    <li class="choose_bank_listItem" bid="0025840">邮储银行</li>		    
		    
		    <li class="choose_bank_listItem" bid="5315840">花旗银行</li>
		    <li class="choose_bank_listItem" bid="3154560">恒丰银行</li>
		    <li class="choose_bank_listItem" bid="3131000">北京银行</li>		    
		    <li class="choose_bank_listItem" bid="3135841">上海银行</li>
		    <li class="choose_bank_listItem" bid="3131210">河北银行</li>
		     -->
		    <li class="choose_bank_listItem" bid="30629000">广发银行</li>
		    <li class="choose_bank_listItem" bid="30758400">平安银行</li>
		    <li class="choose_bank_listItem" bid="30510000">民生银行</li>	
		    <li class="choose_bank_listItem" bid="30210001">中信银行</li>
		    <li class="choose_bank_listItem" bid="30410004">华夏银行</li>	    
		    
		    
		    
		  </ul>
		</div>
		<div class="quit">
			  <input type="button" onclick="paysubmit();" class="quit_btn confirBtn" value="确定">
		</div>
		
	</body>
<script type="text/javascript">

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
  //var oCutdown = $('.bank_vcode');
  //var count = 30;
  //var timer = null;

  //oCutdown.on('click',function(){
    //function cutdown(){alert(111);
     // count--;
     // oCutdown.val(count+'s').attr("disabled", "disabled").addClass('disabled');
     // if( count<0 ){
     //   clearInterval(timer);
     //   count = 30;//之前count已经被改成0了，所以这里要让count回到最开始的时间
     //   oCutdown.val('获取验证码').removeAttr('disabled').removeClass('disabled');
     // }
   // }
    //cutdown();
    //timer = setInterval(cutdown,1000);
  //});
});
var oCutdown = $('.bank_vcode');
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

var oConfir = $('.confirBtn');
var count2 = 60;
var timer2 = null;
function cutdown2(){
  count2--;
  oConfir.val(count2+'s').attr("disabled", "disabled").addClass('disabled');
  if( count2<0 ){
    clearInterval(timer2);
    count2 = 60;//之前count已经被改成0了，所以这里要让count回到最开始的时间
    oConfir.val('确定').removeAttr('disabled').removeClass('disabled');
  }
}


var pay=0;
function paysubmit(){
	
	var orderno = $("#orderno").val();
	var bankphone = $("#bankphone").val();
	var smscode = $("#smscode").val();
	var openid = $("#openid").val();
	var amt = $("#amt").val();
	var ramt = $("#ramt").val();
	var name = $("#name").val();
	var bankno = $("#hbankno").val();
	var bankcardno = $("#hbankcardno").val();
	var idcard = $("#idcard").val();
	var vertify = $("#hvertify").val();
	var pbankno = $.trim($("#bankno").val());
	var pbankcardno = $.trim($("#bankcardno").val());
	var cvv = $("#cvv").val();
	var cardvaliddate = $("#cardvaliddate").val();
	
	if(orderno==''||bankphone==''){
		alert("请重新获取验证码");
		return;
	}
	if(smscode==''){
		alert("请输入验证码");
		return;
	}
	
	if(pay==1){
		alert("请不要重复点击确定");
		return;
	}	
	cutdown2();
    timer2 = setInterval(cutdown2,1000);
	pay=1;	
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"transController.do?gbpqpconfirm",
            data: {
                openid:openid,
               	bankphone:bankphone,
               	smscode:smscode,
               	orderno:orderno,
               	name:name,
               	bankno:bankno,
               	bankcardno:bankcardno,
               	idcard:idcard,
               	vertify:vertify,
               	amt:amt,
               	ramt:ramt,
               	pbankno:pbankno,
               	pbankcardno:pbankcardno,
               	cvv:cvv,
               	cardvaliddate:cardvaliddate
            },
            success: function(data) {                   
                if(data.SUCCESS == "00"){
                   alert("支付已成功");
                   $("#smscode").val("");
                   $("#orderno").val("");
                   $("#amt").val("");
                   $("#tamt").val("");
                }else{
                   alert(data.FAIL);
                   $("#smscode").val("");
                   $("#orderno").val("");
                   $("#amt").val("");
                   $("#tamt").val("");
                }
                pay=0;
            }
        });
}  
  
function getSmsCode(){ 
    $("#smscode").val("");
    var amt = $("#tamt").val();
	if(!checkMoney(amt)){
		$("#tamt").val("");
		alert("输入金额有误")
		return;
	}	
	if(amt<35){
		alert("输入金额要大于35元");
		return;
	}
	var openid = $.trim($("#openid").val());
	var bankno = $.trim($("#bankno").val());
	$("#amt").val(amt);
	var name = $.trim($("#name").val());
	var idcard = $.trim($("#idcard").val());
	var bankcardno = $.trim($("#bankcardno").val());
	var cardvaliddate = $.trim($("#cardvaliddate").val());
	var cvv = $.trim($("#cvv").val());
	var tbankphone = $.trim($("#tbankphone").val());
	$("#bankphone").val(tbankphone);
	var bankphone = $("#bankphone").val();
	var sz = /^[0-9]{10,32}$/;
	var sfz = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var sz4 = /^[0-9]{4}$/;
	var sz3 = /^[0-9]{3}$/;
	var sz11 = /^[0-9]{11}$/;
	if(name==''){
		alert("请到个人中心绑定收款卡");
		return;
	}
	if(idcard==''||!sfz.test(idcard)){
		alert("请到个人中心绑定收款卡");
		return;
	}
	if(bankcardno==''||!sz.test(bankcardno)){
		alert("请输入正确卡号10-32位数字");
		return;
	}
	if(bankno==''){
		alert("请选择银行");
		return;
	}
	if(cardvaliddate==''||!sz4.test(cardvaliddate)){
		alert("请输入正确有效期");
		return;
	}
	if(cvv==''||!sz3.test(cvv)){
		alert("请输入正确cvv");
		return;
	}
	if(tbankphone==''||!sz11.test(tbankphone)){
		alert("请输入正确手机号");
		return;
	}
	cutdown();
    timer = setInterval(cutdown,1000);
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"transController.do?gbpqpapply",
            data: {
                openid:openid,
                bankno:bankno,
                amt:amt,
                name:name,
                idcard:idcard,
                bankcardno:bankcardno,
                cardvaliddate:cardvaliddate,
                cvv:cvv,
               	bankphone:bankphone
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   $("#orderno").val(data.orderno);
                   $("#ramt").val(data.ramt);
                }else{
                   alert(data.FAIL);
                }
            }
        });
}

//格式化金额  
function formatAmt(obj){  
  //alert(obj);
  obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  
  obj.value = obj.value.replace(/^\./g,"");  //验证第一个字符是数字而不是.  
  obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.  
  obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
  obj.value = obj.value.replace([\u4e00-\u9fa5],"");
}  
//失去焦点时再次校验  
function checkMoney(money){  
   var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;  
   if(exp.test(money)){  	    
    	return true;
   }else{  
		return false;
   }  
}  
</script>
</html>


