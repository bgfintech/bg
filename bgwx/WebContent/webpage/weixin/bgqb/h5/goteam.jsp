<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title></title>
        <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>  
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
        <link href="plug-in/weixin/merchant/font-awesome-4.7.0/css/font-awesome.min.css?1009" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/reset.css?10009" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1012" rel="stylesheet" type="text/css" />       
    </head>
   <body style="background-color:#f0f0f0;">
   <div class="user_top">
      <div class="user_portrait"><img src="${headimgurl }" alt="" /></div>
      <div class="user_name">${accountinfo.name }</div>
      <div class="user_id">账号：${taccount }</div>
      <div class="cash_people_outer">
         <div class="cash_people clearfix">
            <div class="cash_peopleL">
               <p class="cash_peopleL1">${transamt }</p>
               <p class="cash_peopleL2">当月交易金额（元）</p>
            </div>
            <div class="cash_peopleL">
               <p class="cash_peopleL1">${tnum }</p>
               <p class="cash_peopleL2">团队总人数（人）</p>
            </div>
         </div>
      </div>
   </div><!--user_top-->
   <dl class="user_detail_list">
   	  <dt class="user_detail_list_tit clearfix">
         <span class="user_detail_list_tit1">姓名</span>
         <span class="user_detail_list_tit2">账户</span>
         <span class="user_detail_list_tit3">推广人数（人）</span>
      </dt>
   <c:forEach items="${tlist}" var="p">
      <dd class="user_detail_list_item clearfix">
         <span class="user_detail_list_item1">${p.NAME }</span>
         <span class="user_detail_list_item2">${p.TACCOUNT }</span>
         <span class="user_detail_list_item3">${p.CNUM }</span>
      </dd>
   </c:forEach>        
   </dl>
</body>
</html>

