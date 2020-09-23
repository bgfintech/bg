<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>二维码收款</title>
        <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>   
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>    
        <script src="plug-in/jquery-plugs/qrcode/jquery.qrcode.min.js"></script> 
        <link href="plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1005" rel="stylesheet" type="text/css" />       
    </head>
    <body>
    	<form id="transform" action="transController.do?goResult" method="post">
	    	<input id="orderno" name="orderno"  type="hidden" value="" />
	    	<input id="status" name="status"  type="hidden" value="" />
	    </form>
		<input id="openid" name="openid" type="hidden" value="${openid}" />
		<input id="payChannel" name="payChannel" type="hidden" value="${payChannel}" />
		<h3 class="pay_tit">支付宝收款</h3>
		<div class="pay_cash">
		  <div class="pay_cash_con clearfix">
		    <input type="number" id="tamt" class="pay_cash_conInput fl" placeholder="请输入金额" onkeyup="formatAmt(this)" onblur="checkMoney(this);" /><span class="pay_cash_conIco fr">￥</span>
		  </div>
		</div>
		<div class="other_pay">
		  <h3 class="other_pay_tit">其他付款方式</h3>
		  <ul class="other_pay_list">
		  <!-- 
		    <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL wechat fl"></div>
		      <span class="other_pat_des fl">微信</span>
		      <a href="javascript:;" class="other_pay_itemR_on other_pay_itemR fr" check="true"></a>
		    </li> -->
		    <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL ali fl"></div>
		      <span class="other_pat_des fl">支付宝</span>
		      <a href="javascript:;" class="other_pay_itemR_on other_pay_itemR fr" check="true"></a>
		    </li>
		   <!-- <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL yl fl"></div>
		      <span class="other_pat_des fl">银联</span>
		      <a href="javascript:;" class="other_pay_itemR fr" check="false"></a>
		    </li>-->
		  </ul>
		</div>
		<div class="quit">
		  <a href="javascript:;" onclick="scan();" class="quit_btn">生成收款码</a>
		</div>
		<div class="shadow hide"></div>
		<div class="shadow_top hide">
		  <a href="javascript:;" class="closeBtn"><img src="plug-in/weixin/merchant/images/11.png" alt=""></a>
		  <div class="qrCode" id="qrcode" ></div>
		</div>
	</body>    
<script>
  $('.other_pay_item').on('click',function(){
    var _this = $(this);
    var des = '.other_pat_des';
    var cBtn = '.other_pay_itemR';
    var tit = $('.pay_tit');
    _this.find(cBtn).addClass('other_pay_itemR_on').attr('check','true');
    _this.siblings().find(cBtn).removeClass('other_pay_itemR_on').attr('check','false');
    if( _this.find(des).text() == '微信' && _this.find(cBtn).attr('check') == 'true' ){
      tit.text('微信收款');
      $("#payChannel").val("01");
      //在这调微信支付
    }else if( _this.find(des).text() == '支付宝' && _this.find(cBtn).attr('check') == 'true' ){
      tit.text('支付宝收款');
      $("#payChannel").val("02");
      //在这调支付宝支付
    }else if( _this.find(des).text() == '银联' && _this.find(cBtn).attr('check') == 'true' ){
      tit.text('银联收款');
      $("#payChannel").val("03");
      //在这调银行卡支付
    }    
  });
  $('.closeBtn').click(function(){
      WeixinJSBridge.call('closeWindow');
  });



/**
$(function(){
    $('.b1').click(function() {
        $(this).addClass('b1_on');
        $('.b2').removeClass('b2_on');
        $('.b3').removeClass('b3_on');
        $("#tamt1").val("");
        $("#tamt2").val("");
        $("#tamt3").val("");
        $("#payChannel").val("02");
        $('.payWay_tab_item').eq($(this).index()).show().siblings().hide();
    });
    $('.b2').click(function() {
        $(this).addClass('b2_on');
        $('.b1').removeClass('b1_on');
        $('.b3').removeClass('b3_on');
        $("#tamt1").val("");
        $("#tamt2").val("");
        $("#tamt3").val("");
        $("#payChannel").val("03");
        $('.payWay_tab_item').eq($(this).index()).show().siblings().hide();
    }); 
    $('.b3').click(function() {
        $(this).addClass('b3_on');
        $('.b1').removeClass('b1_on');
        $('.b2').removeClass('b2_on');
        $("#tamt1").val("");
        $("#tamt2").val("");
        $("#tamt3").val("");
        $("#payChannel").val("01");
        $('.payWay_tab_item').eq($(this).index()).show().siblings().hide();
    }); 

    $('.closeBtn').click(function(){
        WeixinJSBridge.call('closeWindow');
    });
});
*/
var pay = 0;
function scan(){
	if(pay==1){
		alert("收款码生成中");
		return;
	}	
	if(!checkMoney($("#tamt").val())){
		$("#tamt").val("");
		alert("输入金额有误");
		return;
	}
	if($("#tamt").val()>1000){	    
		alert("单笔收款金额不能大于1000元");
		return;
	}
	pay = 1;
	var url ="transController.do?txqrcodepay&openid="+$("#openid").val()+"&payChannel="+$("#payChannel").val()+"&amt="+$("#tamt").val(); 	
    $.ajax({
		type: "POST",
		url: url,
		success: function(data){
			var callback =JSON.parse(data);
		    //$("#orderno").val(callback.attributes.orderno);
		    if(callback.success==true){
		    	$('.shadow,.shadow_top').show();    	
				$("#qrcode").qrcode({
					text: callback.msg
				});
		        //query();
		    }else{
				alert(callback.msg);
				pay=0;
		    }
		    
		    
		    //if(callback.msg==null||callback.msg==''){
			//   alert("收款码生成失败，请重试！");
			//   pay=0;
		   // }else{		
			//	$('.shadow,.shadow_top').show();    	
			//	$("#qrcode").qrcode({
			//		text: callback.msg
			//	});
		    //    query();
		    //}
		}
	});
}
function query(){
	var url ="transController.do?queryOrder&orderno="+$("#orderno").val(); 
	$.ajax({
		type: "POST",
		url: url,
		success: function(data){
		var callback =JSON.parse(data);
		    if(callback.status!='03'){
		    	$("#status").val(callback.msg);		    
		    	$("#transform").submit();		    	
		    }else{
		    	window.setTimeout(query,2000);  
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
