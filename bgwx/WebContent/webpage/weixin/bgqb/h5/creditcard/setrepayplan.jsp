<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>设置还款计划</title>
		<script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
        <link href="plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1002" rel="stylesheet" type="text/css" />       

		<script src="plug-in/weixin/merchant/mobiscroll/js/mobiscroll.core-2.5.2.js?100" type="text/javascript"></script>
	    <script src="plug-in/weixin/merchant/mobiscroll/js/mobiscroll.core-2.5.2-zh.js?100" type="text/javascript"></script>
	    <link href="plug-in/weixin/merchant/mobiscroll/css/mobiscroll.core-2.5.2.css?100" rel="stylesheet" type="text/css" />
	    <link href="plug-in/weixin/merchant/mobiscroll/css/mobiscroll.animation-2.5.2.css?100" rel="stylesheet" type="text/css" />
	    <script src="plug-in/weixin/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1.js?100" type="text/javascript"></script>
	    <script src="plug-in/weixin/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1-zh.js?100" type="text/javascript"></script>
	    <!-- S 可根据自己喜好引入样式风格文件 -->
	    <script src="plug-in/weixin/merchant/mobiscroll/js/mobiscroll.android-ics-2.5.2.js?100" type="text/javascript"></script>
	    <link href="plug-in/weixin/merchant/mobiscroll/css/mobiscroll.android-ics-2.5.2.css?100" rel="stylesheet" type="text/css" />
    </head>
    <body style="padding-top:0.08rem;">
    	<input id="cardno" name="cardno" type="hidden" value="${cardinfo.BANKCARDNO}" />
    	<input id="sdate" name="sdate" type="hidden" value="" />
    	<input id="edate" name="edate" type="hidden" value="" />
		<div class="bank_card">
		  <div class="bank_card_name clearfix">
		    <span class="bank_card_nameL">姓名</span>
		    <span class="bank_card_numR">${cardinfo.NAME }</span>
		    <!--  <i class="correct"></i>-->
		  </div>
		   <div class="bank_card_name clearfix">
		    <span class="bank_card_bankL">发卡银行</span>
		    <span class="bank_card_bankR">${cardinfo.BANKNAME }</span>
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">银行卡号</span>
		    <span class="bank_card_bankR">${cardinfo.DCARDNO }</span>
		    <!--<i class="error"></i>-->
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">还款总金额</span>
		    <input type="number" id="amt"  class="bank_card_nameR" placeholder="请输入金额" onkeyup="formatAmt(this)" onblur="checkMoney(this);"/>
		    <!--<input type="text" class="bank_card_nameR" placeholder="开始时间" name="appDate" id="appDate1" />
		    <input type="text" id="bankcardno" class="bank_card_nameR" placeholder="请输入银行卡号" value="${trans.BANKCARDNO }"/>
		    <i class="error"></i>-->
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">开始日期</span>
		    <input type="text"  class="bank_card_nameR" placeholder="开始时间" name="startDate" id="startDate" />
		    <!--<input type="text" class="bank_card_nameR" placeholder="开始时间" name="appDate" id="appDate1" />
		    <input type="text" id="bankcardno" class="bank_card_nameR" placeholder="请输入银行卡号" value="${trans.BANKCARDNO }"/>
		    <i class="error"></i>-->
		  </div>	  
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">执行天数</span>
		    <input type="number"  class="bank_card_nameR" oninput="if(value.length>2)value=value.slice(0,2)" onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" placeholder="请输入计划执行天数" name="days" id="days" />
		    <!--<input type="text" class="bank_card_nameR" placeholder="开始时间" name="appDate" id="appDate1" />
		    <input type="text" id="bankcardno" class="bank_card_nameR" placeholder="请输入银行卡号" value="${trans.BANKCARDNO }"/>
		    <i class="error"></i>-->
		  </div>
		  <div class="bank_card_num clearfix">
		    <span class="bank_card_numL">单日预留额度</span>
		    <span class="bank_card_nameR" style="width:1.0rem;" id="ed"></span>
		    <input type="button" class="bank_vcode" onclick="countAmt();"  value="额度试算" />
		    
		  </div>
		  
		  
		  
		  
		</div>
		<h3 class="pcenter_tit">注意:计划还款时间越长,所需预留额度越低.建议最少预留15-20%以上额度</h3>
		<div class="quit">
			 <input type="button" onclick="doset();" class="quit_btn confirBtn" value="确定">
		</div>
		
	</body>
<script type="text/javascript">
$(function(){
   var now = new Date();
   var currYear = now.getFullYear();
   var opt = {};
   opt.date = {
   preset: 'date',
   };
   //opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
   opt.datetime = {
   preset: 'datetime',
   };
   opt.time = {
   preset: 'time',
   };
   opt.default = {
   //theme: 'android-ics light', //皮肤样式
   display: 'modal', //显示方式
   mode: 'scroller', //日期选择模式
   lang: 'zh',
   startYear: currYear, //开始年份
   endYear: currYear + 1, //结束年份
   dateFormat: 'yyyy年mm月dd日',
   };
   $("#startDate").val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
   $('#startDate').change(function(){
  	  var qDate = $('#startDate').val();
  	  var s = qDate.substring(0,4)+qDate.substring(5,7)+qDate.substring(8,10);
  	  if(s<((new Date()).format('yyyyMMdd'))){
  	    $('#startDate').val("");
  	    $('#sdate').val("");
  	  	alert('不能小于当前日期');  	  	
  	  }
  	  $('#sdate').val(s);
  });
   
});

Date.prototype.format = function (format) {
           var args = {
               "M+": this.getMonth() + 1,
               "d+": this.getDate(),
               "h+": this.getHours(),
               "m+": this.getMinutes(),
               "s+": this.getSeconds(),
               "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
               "S": this.getMilliseconds()
           };
           if (/(y+)/.test(format))
               format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
           for (var i in args) {
               var n = args[i];
               if (new RegExp("(" + i + ")").test(format))
                   format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
           }
           return format;
       };
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

function doset(){
	var cardno = $('#cardno').val();
	var amt = $('#amt').val();
	var sdate = $('#sdate').val();
	var days = $('#days').val();
	if(!checkMoney(amt)){
		$("#amt").val("");
		alert("输入金额有误");
		return;
	}
	if(sdate==''){
		alert('请选择开始日期');
		return;
	}
	var tmpd = new Date();
	var thour = tmpd.getHours();
	if(sdate==tmpd.format('yyyyMMdd') && "21"<=thour){
		alert("21点后不能设置当天为开始日期");
		return;
	}
	if(days==''||days==0){
		alert('请输入计划执行天数');
		return;
	}
	if(days>10){
		alert('计划执行天数不能大于10天');
		return;
	}
	var ed = Math.ceil((amt/days+2)/0.9935);
	if(ed<200){
		alert('单日最少还款金额不能少于200');
		return;
	}
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"creditCardController.do?setrepayplan",
            data: {
                cardno:cardno,
                amt:amt,
                sdate:sdate,
                days:days
            },
            success: function(data) {                   
                if(data.SUCCESS == "00"){
                    alert('还款计划设置成功');
                	window.location.href="creditCardController.do?viewrepayplan&planid="+data.PLANID;
                }else{
                   alert(data.FAIL);
                }
            }
        });
}
function countAmt(){
var cardno = $('#cardno').val();
	var amt = $('#amt').val();
	var days = $('#days').val();
	if(!checkMoney(amt)){
		$("#amt").val("");
		alert("输入金额有误");
		return;
	}
	if(days==''||days==0){
		alert('请输入计划执行天数');
		return;
	}
	if(days>20){
		alert('计划执行天数不能大于20天');
		return;
	}
	var o = Math.ceil((amt/days+2)/0.9935);
	var ed = Math.ceil(  o+ ( o-(amt/days) )*(days-1)   );
	$('#ed').html(ed+".00");
}
</script>
</html>


