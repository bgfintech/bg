<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>快捷支付</title>
		<script type="text/javascript" src="/plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="plug-in/bg/js/bank.js?1"></script>
        <link href="/plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="/plug-in/weixin/merchant/css/common.css?1002" rel="stylesheet" type="text/css" /> 
    </head>
    <body style="padding-top:0.08rem;">    	
    	<input id="retstatus" name="retstatus" type="hidden" value="${retstatus}" />
    	<input id="retmsg" name="retmsg" type="hidden" value="${retmsg}" />
	    <input id="openid" name="openid" type="hidden" value="${openid}" />
	    <input id="accountid" name="accountid" type="hidden" value="${accountinfo.account}" />
    	<input id="orderno" name="orderno" type="hidden" value="" />
    	<input id="orderdate" name="orderdate" type="hidden" value="" />
    	<input id="bankphone" name="bankphone" type="hidden" value="" />
    	<input id="amt" name="amt" type="hidden" value="" />    	
    	<input id="ramt" type="hidden" value=""/>
    	<input id="name" type="hidden" value="${person.NAME }" />    	
		<input id="bindcardid" name="bindcardid" type="hidden" value="" />
		<input id="iscashbindcard" name="bindcardid" type="hidden" value="${person.HFTXBINDID}" />
		<input id="isdeposit" name="isdeposit" type="hidden" value="${isDeposit}" />
		<div class="pay_cash">
		  <div class="pay_cash_con clearfix">
		    <input type="number" id="tamt" class="pay_cash_conInput fl" placeholder="请输入金额" onkeyup="formatAmt(this)" onblur="checkMoney(this);"><span class="pay_cash_conIco fr">￥</span>
		  </div>
		</div>
		<h3 id="hd" style="text-align:center;color:#FF0000;"></h3>
		<div class="bank_card">
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">姓名</span>
		    <span class="bank_card_numR">${person.NAME }</span>
		    <!--  <i class="correct"></i>-->
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">银行卡号</span>
		    <input type="text" id="bankcardno" class="bank_card_nameR" placeholder="请输入银行卡号" value="${trans.BANKCARDNO }"/>
		    <!--<i class="error"></i>-->
		  </div>

		</div>
		<div class="bank_card">
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">预留手机号</span>
		    <input type="number" id="tbankphone" class="bank_card_nameR" placeholder="请输入银行预留手机号" value="${trans.BANKPHONE }"/>
		  </div>
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">验证码</span>
		    <input type="text" id="smscode" class="bank_card_nameR" placeholder="请输入验证码" style="width:1.0rem;" />
		    <input type="button" class="bank_vcode" onclick="getSmsCode();"  value="获取验证码" />
		  </div>
		  <!-- <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">收款账号</span>
		    <span class="bank_card_numR">${tcbankcardno}</span>
		  </div> -->
		</div>
		<h3>&nbsp;选择历史银行卡:</h3>
		<div class="bank_card" id="cards">
			
		</div>
		
		<div class="quit">
			  <input type="button" onclick="paysubmit();" class="quit_btn confirBtn" value="确定">
		</div>
		
	</body>
<script type="text/javascript">
$(function(){    	
     var cards =  eval('${cards}');
     html='';     
     $(cards).each(function(){
		var bankobj = bankCardAttribution(this.BANKCARDNO);
		var bc = bankobj.bankCode;
		var bn = bankobj.bankName;
		if(bankobj=='error'){
			bn='';
		}
       	html += "<div class=\"bank_card_name clearfix\" onclick=\"change('"+this.BANKCARDNO+"','"+this.BANKPHONE+"')\">" +
				"<span class=\"mod_passL\">"+ bn + "("+ this.TBCO+")"+				
				"</span></div>";
   	 });
   	 $("#cards").html(html);   	 
   	 var isdeposit = $("#isdeposit").val();
   	 isdeposit='1';
   	 alert(isdeposit);
   	 if(isdeposit=="1"){
   	 	var t = $("#tamt");
   	 	t.val("198");
   	 	t.attr({"readonly":"readonly"}); 
   	 	$("#hd").html("首刷押金198元");
   	 }
});


function change(cardno,phone){
	$("#bankcardno").val(cardno);
	$("#tbankphone").val(phone);
}


var retstatus = $("#retstatus").val();
var retmsg = $("#retmsg").val();
if(retstatus=='01'){
	alert('支付已成功');
}else if(retstatus=='02'){
	alert(retmsg);
}else if(retstatus=='03'){
	alert(retmsg);
}

var oCutdown = $('.bank_vcode');
var count = 90;
var timer = null;
function cutdown(){
    count--;
    oCutdown.val(count+'s').attr("disabled", "disabled").addClass('disabled');
    if( count<0 ){
      clearInterval(timer);
      count = 90;//之前count已经被改成0了，所以这里要让count回到最开始的时间
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
	var orderdate = $("#orderdate").val();
	var bankphone = $("#bankphone").val();
	var smscode = $("#smscode").val();
	var openid = $("#openid").val();
	var accountid = $("#accountid").val();
	var name = $("#name").val();
	var pbankno = $.trim($("#bankno").val());
	var pbankcardno = $.trim($("#bankcardno").val());
	var bindcardid=$.trim($("#bindcardid").val());
	var isdeposit = $("#isdeposit").val();
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
            url:"transController.do?gbphftxpayconfirm",
            data: {
                openid:openid,
                accountid:accountid,
               	bankphone:bankphone,
               	smscode:smscode,
               	orderno:orderno,
               	orderdate:orderdate,
               	bindcardid:bindcardid,
               	isdeposit:isdeposit,
               	name:name
            },
            success: function(data) {                   
                if(data.SUCCESS == "00"){
                   var url = "/npay/merchantRequest";
                   var pra = new Object();
		           pra.cmd_id = data.cmd_id;
		           pra.version = data.version;
		           pra.mer_cust_id = data.mer_cust_id;
		           pra.check_value = data.check_value;		 
		           
		           /**
		           var temp_form = document.createElement("form");
					temp_form.action = url;
					//如需打开新窗口，form的target属性要设置为'_blank'
					temp_form.target = "_self";
					temp_form.method = "post";
					temp_form.style.display = "none";
					//添加参数
					for (item in pra) {
					    var opt = document.createElement("hidden");
					    opt.name = item;
					    opt.value = pra[item];
					    alert(opt.name+":"+opt.value)
					    temp_form.appendChild(opt);
					}
					document.body.appendChild(temp_form);
					//提交数据
					temp_form.submit();		
					*/           
		           var body = $(document.body);
		           var form = $("<form method='post'></form>");
		           form.attr({ "action": url });
		           for (arg in pra) {
		               var input = $("<input type='hidden'>");
		               input.attr({ "name": arg });
		               input.val(pra[arg]);
		               form.append(input);
		           }
		           form.appendTo(document.body);
		           form.submit();   
		           document.body.removeChild(form[0]); 
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
	var openid = $.trim($("#openid").val());
	var isbindcashcard = $("#iscashbindcard").val();
	if(isbindcashcard==''||isbindcashcard==null){
		alert('请先到-个人中心-绑定收款银行卡');
        window.location.href="acctController.do?gobindbankcard&openid="+openid;
        return;
	}
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
	var accountid = $.trim($("#accountid").val());
	var name = $.trim($("#name").val());
	$("#amt").val(amt);
	var bankcardno = $.trim($("#bankcardno").val());
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
	if(bankcardno==''||!sz.test(bankcardno)){
		alert("请输入正确卡号10-32位数字");
		return;
	}
	var bankobj = bankCardAttribution(bankcardno);
	var bc = bankobj.bankCode;
	var bn = bankobj.bankName;
	if(bc=='10210000'&&amt>50000){
		alert(bn+'单笔单日限额5万/5万!');
		return;
	}else if(bc=='10510000'&&amt>10000){
		alert(bn+'单笔单日限额1万/1万!');
		return;
	}else if((bc=='10310000'||bc=='30858400'||bc=='30129001'||bc=='30410004')&&amt>20000){
		alert(bn+'单笔单日限额2万/5万!');
		return;
	}else if((bc=='30310000'||bc=='30939100'||bc=='30629000'||bc=='30510000'||bc=='30758400'||bc=='31029009'||bc=='40310000')&&amt>20000){
		alert(bn+'单笔单日限额2万/2万!');
		return;
	}else if(bankobj!='error'&&bc!='10210000'&&bc!='10510000'&&bc!='10310000'&&bc!='30858400'&&bc!='30129001'&&bc!='30410004'&&bc!='30310000'&&bc!='30939100'&&bc!='30629000'&&bc!='30510000'&&bc!='30758400'&&bc!='31029009'&&bc!='40310000'&&bc!='10410000'&&bc!='30210001'&&bc!='32529000'&&bc!='31310000'){
		alert('暂不支持'+bn);
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
            url:"transController.do?gbphftxpayapply",
            data: {
                openid:openid,
                accountid:accountid,
                amt:amt,
                name:name,
                bankcode:bc,
                bankcardno:bankcardno,
               	bankphone:bankphone
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   $("#orderno").val(data.orderno);
                   $("#orderdate").val(data.orderdate);
                   $("#bindcardid").val(data.bindcardid);
                }else{
                   if(data.FAIL=='user_cust_id is invalid!'){
                   	alert('请先到-个人中心-绑定收款银行卡');
                   	window.location.href="acctController.do?gobindbankcard&openid="+openid;
                   }else{
                   	alert(data.FAIL);
                   }
                   
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
  //obj.value = obj.value.replace([\u4e00-\u9fa5],"");
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


