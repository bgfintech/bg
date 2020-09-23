<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title></title>
        <script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
        
        <link href="plug-in/weixin/merchant/css/reset.css?101" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/font-awesome-4.7.0/css/font-awesome.min.css?101" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?102" rel="stylesheet" type="text/css" />

        
    </head>
    <body style="background-color:#f0eff5;">
        <div class="transact_detail">
            <div class="transact_way clearfix">
                <div class="transact_wayImg">
                <c:if test="${tran.payChannel == '01'}">
                	<img src="plug-in/weixin/merchant/images/19.png" alt="" />       
                </c:if>
                <c:if test="${tran.payChannel == '02'}">
                	<img src="plug-in/weixin/merchant/images/19b.png" alt="" />       
                </c:if>
                <c:if test="${tran.payChannel == '03'}">
                	<img src="plug-in/weixin/merchant/images/19a.png" alt="" />       
                </c:if>
                <c:if test="${tran.payChannel == '04'}">
                	<img src="plug-in/weixin/merchant/images/19a.png" alt="" />       
                </c:if>     
                </div>
                <div class="transact_way_wx">${tran.payChannelName}</div>
            </div>
            <div class="transact_con">
                <div class="transact_con1">
                    <div class="transact_con1_b clearfix">
                        <span class="transact_con1L">付款金额</span><span class="transact_con1R">￥${tran.amt}</span>
                    </div>
                </div>
                <ul class="transact_con2_list">
                    <li class="transact_con2_list_item clearfix">
                        <span class="transact_con2_list_item1 fl">当前状态</span><span class="transact_con2_list_item2 fr">${tran.status}</span>
                    </li>
                    <li class="transact_con2_list_item clearfix">
                        <span class="transact_con2_list_item1 fl">收钱时间</span><span class="transact_con2_list_item2 fr">${tran.transTime}</span>
                    </li>
                    <li class="transact_con2_list_item clearfix">
                        <span class="transact_con2_list_item1 fl">付款单号</span><span class="transact_con2_list_item2 fr">${tran.orderNo}</span>
                    </li>
                </ul>
            </div><!--transact_con-->
            <div class="transact_ques clearfix">
                <span class="transact_ques_des">常见问题</span>
                <a href="javascript:;" class="transact_ques_des_ico"><i class="fa fa-angle-right fa-lg fr"></i></a>
            </div>
        </div><!--transact_detail-->
    </body>
</html>
