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
        <link href="plug-in/weixin/merchant/css/common.css?1009" rel="stylesheet" type="text/css" />

        
    </head>
    <body style="background-color:#fff;padding:0.4rem 0.1rem 0;">
    	<input id="orderno" type="hidden" value="${orderno}" />
    	<input id="status" type="hidden" value="${status}" />
    	<input id="status" type="hidden" value="${failureDetails}" />
    	
        <div class="payWay">
            <div class="pay_success clearfix">
            <% 
				String status = (String)request.getAttribute("status");
				if("01".equals(status)){
			%>
			 	<span class="pay_successL"></span>
                <span class="pay_successR">付款成功</span>
			<%	
				}else if("02".equals(status)){
			%>
				<span class="pay_successL2"></span>
                <span class="pay_successR">付款失败   ${failureDetails}</span>
			<%	
				}else{
			%>
				<span id="rcls" class="pay_successL1"></span>
                <span id="rval" class="pay_successR">付款处理中</span>
			<%	
				}
			%>
            </div>
            <a href="javascript:;" id="cls" class="back">关&nbsp;闭</a>
        </div>
    </body><!--payWay-->
<script type="text/javascript">
$("#cls").click(function() {
    WeixinJSBridge.call('closeWindow');
}); 
var url ="transController.do?queryOrder&orderno="+$("#orderno").val(); 
$(function(){
	var status = $("#status").val();
	if(status=='01'||status=='02'){
		return;
	}	
	aq();
});
function aq(){
	$.ajax({
		type: "POST",
		url: url,
		success: function(data){
		var callback =JSON.parse(data);
		    if(callback.status=='01'){
		    	$("#rcls").attr("class","pay_successL");
		    	$("#rval").text("付款成功");
		    }else if(callback.status=='02'){
		    	$("#rcls").attr("class","pay_successL2");
		    	$("#rval").text("付款失败   "+callback.msg);
		    }else{
		    	window.setTimeout(aq,1000);  
		    }			
		}
	});
}
</script>
</html>

