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
        <link href="plug-in/weixin/merchant/css/reset.css?1009" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?1010" rel="stylesheet" type="text/css" />

        
    </head>
    <body style="background-color:#fff;padding:0.4rem 0.1rem 0;">
    	
        <div class="payWay">
            <div class="pay_success clearfix">
           		<span class="pay_successL2"></span>
            	<span class="pay_successR">请联系客户经理开通该功能</span>			
            </div>
            <a href="javascript:;" id="cls" class="back">关&nbsp;闭</a>
        </div>
    </body><!--payWay-->
<script type="text/javascript">
$("#cls").click(function() {
    WeixinJSBridge.call('closeWindow');
}); 
</script>
</html>

