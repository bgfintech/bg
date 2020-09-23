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
        <script src="plug-in/jquery-plugs/qrcode/jquery.qrcode.min.js"></script>     
        <link href="plug-in/weixin/merchant/css/reset.css?1009" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?10102" rel="stylesheet" type="text/css" />       
    </head>
    <body >
    	<input id="timestamp" type="hidden" value="${timestamp}" />
	    <input id="noncestr" type="hidden" value="${nonceStr}" />
	    <input id="signature" type="hidden" value="${signature}" />
	    <input id="appid" name="appid" type="hidden" value="${appid}" />
	    <input id="iscertified" name="iscertified" type="hidden" value="${accountinfo.iscertified}" />
	    <input id="qrurl"  type="hidden" value="${qrurl}" />
	    <form id="shareform" action="acctController.do?share" method="post">
	    	<input id="openid" name="openid" type="hidden" value="${openid}" />
	    </form>
	    <div class="poster">
	    <%
	    	int x=(int)(Math.random()*2);
	     %>
		   <img src="plug-in/weixin/merchant/images/3<%=x %>.jpg?1" class="poster_img" alt="" />
		   <!--<a href="" class="posterBtnL"></a>-->
		   <a href="https://creditcardapp.bankcomm.com/applynew/front/apply/track/record.html?trackCode=A0228101830729" class="posterBtnR"></a>
		   <div class="qrCode_dyna" id="img"></div>
		   <div id="qrcode" ></div>
		</div>
	    
	    
        <!-- <div class="payWay">
            <div class="pay_success clearfix">
           		<span class="pay_successL2"></span>
            	<span class="pay_successR" id="ta">敬请期待！</span>			
            </div>
            <a href="javascript:;" onclick="cls();" class="back">关&nbsp;闭</a>
        </div> 
        <div class="shadow_top">
		  <a href="javascript:;" class="closeBtn"><img src="plug-in/weixin/merchant/images/11.png" alt=""></a>
		  <div id="qrcode" ></div>
		  <div class="qrCode" id="img" ></div>
		</div>-->
    </body><!--payWay-->
<script type="text/javascript">
var qrurl = $("#qrurl").val();
var openid = $("#openid").val();
var iscertified = $("#iscertified").val();
if(iscertified=='0'){
	alert("确保奖励金到账,请需先绑定储蓄卡");
	window.location.href="acctController.do?gobindbankcard&openid="+openid;
}
$(function() {
    wx.config({
        debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId : '${appid}', // 必填，公众号的唯一标识
        timestamp : '${timestamp}', // 必填，生成签名的时间戳
        nonceStr : '${nonceStr}', // 必填，生成签名的随机串
        signature : '${signature}',// 必填，签名，见附录1
        jsApiList : [ 'onMenuShareTimeline','onMenuShareAppMessage' ]
    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    var linkurl = "http://wx.widefinance.cn/acctController.do?goshare&openid="+openid;
    wx.ready(function(){
                // 分享事件必须放在这里
                wx.onMenuShareTimeline({
                      title: '信用卡收款可自用秒到',
                      desc: '布广钱包 商家收款好帮手 信用卡收款可自用秒到',
                      link: linkurl,
                      imgUrl: 'http://wx.widefinance.cn/plug-in/login/images/jeecg-aceplus.png',
                      success: function (res) {
                      	//$("#shareform").submit();
                      }
                    });
                    //分享给朋友
                wx.onMenuShareAppMessage({
					title:'信用卡收款可自用秒到',
                    desc: '布广钱包 商家收款好帮手 信用卡收款可自用秒到',
                    link: linkurl,
                    imgUrl: 'http://wx.widefinance.cn/plug-in/login/images/jeecg-aceplus.png',
					success: function (res) {
						// 用户确认分享后执行的回调函数
					}
				});
            });
    $("#qrcode").qrcode({
     	render : "canvas", 
    	background : "#ffffff",       //二维码的后景色
        foreground : "#000000",        //二维码的前景色
		text: qrurl,
		src:"plug-in/weixin/merchant/images/26.png"
	});
	//alert(1);
	//获取网页中的canvas对象 
	var mycans=$("canvas")[0];  
	//调用convertCanvasToImage函数将canvas转化为img形式  
	var img=convertCanvasToImage(mycans); 
	//将img插入容器 
	$("#img").append(img); 
	$("#qrcode").remove();
});	
function cls(){
	WeixinJSBridge.call('closeWindow');
}

function convertCanvasToImage(canvas){

     //新Image对象,可以理解为DOM;
     var image = new Image();

     //canvas.toDataURL返回的是一串Base64编码的URL,当然,浏览器自己肯定支持
     //指定格式PNG
     image.src = canvas.toDataURL("image/png");
     return image;
}
</script>
</html>

