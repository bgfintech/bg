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
    <link href="plug-in/weixin/merchant/css/common.css?100101" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="plug-in/bg/js/area.js?4"></script>
</head>
<body style="padding-top:0.08rem;">
<input id="openid" name="openid" type="hidden" value="${openid}" />
<input id="accountid" name="accountid" type="hidden" value="${accountinfo.account}" />
<input id="bankno" name="bankno" type="hidden" value="" />
<input id="prov" name="prov" type="hidden" value="" />
<input id="area" name="area" type="hidden" value="" />
<input id="iscertified" name="iscertified" type="hidden" value="${accountinfo.iscertified}" />
<div class="bank_card">
  <%
  		String isvalid = (String)request.getAttribute("isvalid");
  		if("1".equals(isvalid)){
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
  <div class="bank_card_bank clearfix">
    <span class="bank_card_bankL1">开户省份</span>
    <span class="bank_card_bankR1">－－－</span>
    <i class="bank_card_bank_ico"></i>
  </div>
  <div class="bank_card_bank clearfix">
    <span class="bank_card_bankL2">开户城市</span>
    <span class="bank_card_bankR2">－－－</span>
    <i class="bank_card_bank_ico"></i>
  </div>
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
	<input type="button" onclick="bind();" class="quit_btn confirBtn" value="确定储蓄卡绑卡"/>
</div>

<div class="shadow hide"></div>
<div class="choose_bank">
  <a href="javascript:;" class="choose_bank_close"></a>
  <h3 class="choose_bank_tit">选择银行</h3>
  <ul class="choose_bank_list">
    <li class="choose_bank_listItem" bid="10510000">建设银行</li>
    <li class="choose_bank_listItem" bid="10210000">工商银行</li>
    <li class="choose_bank_listItem" bid="10310000">农业银行</li>
    <li class="choose_bank_listItem" bid="30939100">兴业银行</li>
    <li class="choose_bank_listItem" bid="30310000">光大银行</li>
    <li class="choose_bank_listItem" bid="30629000">广发银行</li>
    <li class="choose_bank_listItem" bid="30758400">平安银行</li>
    <li class="choose_bank_listItem" bid="30510000">民生银行</li>
    <li class="choose_bank_listItem" bid="10410000">中国银行</li>
    <li class="choose_bank_listItem" bid="40310000">邮储银行</li>    
    <li class="choose_bank_listItem" bid="30858400">招商银行</li>
    <li class="choose_bank_listItem" bid="30410004">华夏银行</li>
    <li class="choose_bank_listItem" bid="30210001">中信银行</li>
    <li class="choose_bank_listItem" bid="30129001">交通银行</li>
  </ul>
</div>

<div class="shadow1 hide"></div>
<div class="choose_bank1">
  <a href="javascript:;" class="choose_bank_close1"></a>
  <h3 class="choose_bank_tit1">选择省份</h3>
  <ul class="choose_bank_list1">
    <li class="choose_bank_listItem1" pid="110000"  parr="0">北京</li>
    <li class="choose_bank_listItem1" pid="130000"  parr="1">河北省</li>
    <li class="choose_bank_listItem1" pid="120000"  parr="2">天津</li>
    <li class="choose_bank_listItem1" pid="310000"  parr="3">上海</li>
    <li class="choose_bank_listItem1" pid="500000"  parr="4">重庆</li>
    <li class="choose_bank_listItem1" pid="440000"  parr="5">广东省</li>
    <li class="choose_bank_listItem1" pid="410000"  parr="6">河南省</li>
    <li class="choose_bank_listItem1" pid="210000"  parr="7">辽宁省</li>
  </ul>
</div>

<div class="shadow2 hide"></div>
<div class="choose_bank2">
  <a href="javascript:;" class="choose_bank_close2"></a>
  <h3 class="choose_bank_tit2">选择城市</h3>
  <ul class="choose_bank_list2" id="citylist">
  </ul>
</div>
</body>
</html>
<script>

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
  
  $('.bank_card_bankR1').on('click', function() {
      $('.shadow1').show();
      $('.choose_bank1').stop().animate({ 'bottom': '0' }, 20).show();
  });
  $('.choose_bank_close1').on('click', function() {
      $('.shadow1').hide();
      $('.choose_bank1').stop().animate({ 'bottom': '-100%' }, 20).hide();
  });
  $('.choose_bank_listItem1').on('click', function() {
    $('.bank_card_bankR1').text( $(this).text() );
    $("#prov").val($(this).attr("pid"));
    $('.shadow1').hide();
    $('.choose_bank1').stop().animate({ 'bottom': '-100%' }, 20).hide();
    changeprov($(this).attr("parr"));
  });
  
  $('.bank_card_bankR2').on('click', function() {
      $('.shadow2').show();
      $('.choose_bank2').stop().animate({ 'bottom': '0' }, 20).show();
  });
  $('.choose_bank_close2').on('click', function() {
      $('.shadow2').hide();
      $('.choose_bank2').stop().animate({ 'bottom': '-100%' }, 20).hide();
  });
  $('.choose_bank_listItem2').on('click', function() {
    $('.bank_card_bankR2').text( $(this).text() );
    $("#area").val($(this).attr("cid"));
    $('.shadow2').hide();
    $('.choose_bank2').stop().animate({ 'bottom': '-100%' }, 20).hide();
  });
  
});

function changeprov(i){
	var cname = city_name[i];
	var ccode = city_code[i];
	var html='';
	for(var x=0;x<cname.length;x++){
    	html+="<li class='choose_bank_listItem2' cid='"+ccode[x]+"'>"+cname[x]+"</li>";
    }
    $("#citylist").html(html);
    $('.choose_bank_listItem2').on('click', function() {
	    $('.bank_card_bankR2').text( $(this).text() );
	    $("#area").val($(this).attr("cid"));
	    $('.shadow2').hide();
	    $('.choose_bank2').stop().animate({ 'bottom': '-100%' }, 20).hide();
    });
}


function bind(){
	var bankphone = $("#bankphone").val().trim();
	var name = $("#name").val().trim();
	var idcard = $("#idcard").val().trim();
	var bankcardno = $("#bankcardno").val().trim();
	var bankno = $("#bankno").val();
	var openid = $("#openid").val();
	var prov = $("#prov").val();
	var area =  $("#area").val();
	var iscertified = $("#iscertified").val();
	var accountid = $("#accountid").val();
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
	cutdown2();
    timer2 = setInterval(cutdown2,1000);
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"acctController.do?bindbankcard",
            data: {
                openid:openid,
                accountid:accountid,
                iscertified:iscertified,
               	bankphone:bankphone,
               	name:name,
				idcard:idcard,
				bankno:bankno,
				bankcardno:bankcardno,
				prov:prov,
				area:area
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