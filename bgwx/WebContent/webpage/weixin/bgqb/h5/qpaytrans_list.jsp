<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
    <title></title>
    <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js?100"></script>
    <link href="plug-in/weixin/merchant/css/reset.css?100" rel="stylesheet" type="text/css" />
    <link href="plug-in/weixin/merchant/css/common.css?1001" rel="stylesheet" type="text/css" />

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
<body>
<div class="date clearfix">
  <span class="dateL fl"></span>
  <i class="dateR"></i> 
  <input type="text" placeholder="开始时间" name="appDate" id="appDate1" />
  <input type="hidden"  id="openid"  value="${openid}"/>
</div>
<ul id="datelist" class="date_list">

<c:forEach items="${trans}" var="tran">
  <li class="date_listItem">
  <a id="${tran.orderNo}" href="acctController.do?showtrans&orderno=${tran.orderNo}&openid=${openid}">
    <div class="date_listItemT clearfix">
      <span class="date_listItemT1 fl">￥</span>
      <span class="date_listItemT2 fl">${tran.amt}</span>
      <span class="date_listItemT3 fr">${tran.status}</span>
    </div>
    <div class="date_listItemB clearfix">
      <span class="date_listItemB1 fl">${tran.yymmdd}</span>
      <span class="date_listItemB2 fr">${tran.mmss}</span>
    </div>
  </a>
  </li>
</c:forEach>  
</ul>
</body>
</html>
<script>
$(function(){

   var currYear = (new Date()).getFullYear();
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
   startYear: currYear - 10, //开始年份
   endYear: currYear + 10, //结束年份
   dateFormat: 'yyyy年mm月dd日',
   };
   $("#appDate1").val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
   $('#appDate1').change(function(){
  	  var qDate = $('#appDate1').val();
      $('.dateL').text(qDate);
      search(qDate);
  });
});

function search(qDate){
 	var openid = $("#openid").val();
 	var s = qDate.substring(0,4)+qDate.substring(5,7)+qDate.substring(8,10);
 	$.ajax({
       type: "POST",
       dataType: "json",
       url:"acctController.do?querypaytrans",
       data: {
           qdate:s,
           openid:openid,
       },
       success: function(data) { 
       	   var html = '';             
           if(data.SUCCESS == "00"){
               var list = data.RESULT_CONTENT;
                   $(list).each(function(){
                   	   html += "<li class=\"date_listItem\">"
							+	  "<a id=\""+this.orderNo+"\" href=\"acctController.do?showtrans&orderno="+this.orderNo+"&openid="+openid+"\">"
							+	    "<div class=\"date_listItemT clearfix\">"
							+	      "<span class=\"date_listItemT1 fl\">￥</span>"
							+	      "<span class=\"date_listItemT2 fl\">"+this.amt+"</span>"
							+	      "<span class=\"date_listItemT3 fr\">"+this.status+"</span>"
							+	    "</div>"
							+	    "<div class=\"date_listItemB clearfix\">"
							+	      "<span class=\"date_listItemB1 fl\">"+this.yymmdd+"</span>"
							+	      "<span class=\"date_listItemB2 fr\">"+this.mmss+"</span>"
							+	    "</div></a>"
							+	  "</li>";
                      
                   });
           }
           $("#datelist").html(html);
       }
   });
}

</script>




