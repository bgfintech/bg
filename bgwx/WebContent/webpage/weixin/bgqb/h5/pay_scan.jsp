<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>扫一扫收款</title>
		<script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>   
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>    
        <link href="plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1002" rel="stylesheet" type="text/css" />       

    </head>
    <body>
    <form id="transform" action="transController.do?scanPay" method="post">
		<input id="timestamp" type="hidden" value="${timestamp}" />
	    <input id="noncestr" type="hidden" value="${nonceStr}" />
	    <input id="signature" type="hidden" value="${signature}" />
	    <input id="appid" name="appid" type="hidden" value="${appid}" />
	    <input id="openid" name="openid" type="hidden" value="${openid}" />
	    <input id="payChannel" name="payChannel" type="hidden" value="01" />
	    <input id="authCode" name="authCode" type="hidden" value="" />
	    <input id="amt" name="amt" type="hidden" value="" />
    </form>    
		<h3 class="pay_tit">微信收款</h3>
		<div class="pay_cash">
		  <div class="pay_cash_con clearfix">
		    <input type="number" id="tamt" class="pay_cash_conInput fl" placeholder="请输入金额" onkeyup="formatAmt(this)" onblur="checkMoney(this);" /><span class="pay_cash_conIco fr">￥</span>
		  </div>
		</div>
		<div class="other_pay">
		  <h3 class="other_pay_tit">其他付款方式</h3>
		  <ul class="other_pay_list">
		    <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL wechat fl"></div>
		      <span class="other_pat_des fl">微信</span>
		      <a href="javascript:;" class="other_pay_itemR_on other_pay_itemR fr" check="true"></a>
		    </li>
		    <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL ali fl"></div>
		      <span class="other_pat_des fl">支付宝</span>
		      <a href="javascript:;" class="other_pay_itemR fr" check="false"></a>
		    </li>
		    <li class="other_pay_item clearfix">
		      <div class="other_pay_itemL yl fl"></div>
		      <span class="other_pat_des fl">银联</span>
		      <a href="javascript:;" class="other_pay_itemR fr" check="false"></a>
		    </li>
		  </ul>
		</div>
		<div class="quit">
		  <a href="javascript:;" onclick="scan();" class="quit_btn">扫码收款</a>
		</div>
		
	</body> 
<script type="text/javascript">
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

$(function() {
    var timestamp = $("#timestamp").val();//时间戳
    var nonceStr = $("#noncestr").val();//随机串
    var signature = $("#signature").val();//签名
    var appId = $("#appid").val();
    wx.config({
        debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId : appId, // 必填，公众号的唯一标识
        timestamp : timestamp, // 必填，生成签名的时间戳
        nonceStr : nonceStr, // 必填，生成签名的随机串
        signature : signature,// 必填，签名，见附录1
        jsApiList : [ 'scanQRCode' ]
    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
});

function scan(){
	if(!checkMoney($("#tamt").val())){
		$("#tamt").val("");
		alert("输入金额有误")
		return;
	}
	
    wx.scanQRCode({
        // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
        needResult : 1,
        desc : 'scanQRCode desc',
        success : function(res) {
            //扫码后获取结果参数赋值给Input
            var url = res.resultStr;
            //商品条形码，取","后面的
            if(url.indexOf(",")>=0){
                var tempArray = url.split(',');
                var tempNum = tempArray[1];
                $("#authCode").val(tempNum);
            }else{
                $("#authCode").val(url);
            } 
            var tmp = $("#tamt").val();
            $("#amt").val(tmp);
            $("#transform").submit();
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


