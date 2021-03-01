<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<%@include file="/taglib/taglibs.jsp"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link href="/plug-in/merchant/fileinput/css/fileinput.css" media="all"    rel="stylesheet" type="text/css" />
    <link href="/plug-in/merchant/fileinput/themes/explorer-fa/theme.css"    media="all" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>   
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>    
    <script src="/plug-in/jquery-plugs/qrcode/jquery.qrcode.min.js"></script> 
    <link href="/plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
    <link href="/plug-in/weixin/merchant/css/common.css?1005" rel="stylesheet" type="text/css" />   
<style type="text/css">
.inputfln {
    float: none;
    margin: 0;
}
th {
    text-align: center;
    border:0px;
}
td {
    text-align: center;
    border:1px solid #dddddd;;
}
.box{width: 400px;height: auto;overflow-y: auto;background-color: #fff;position: absolute;right: -400px;top: 0;z-index: 111111;}

html{margin:0;padding:0;font-size:14px;}
.form1_rec{width:710px;margin:0 auto;padding:60px 40px 200px;border:1px solid #ccc;}
.form1_rec label{margin-top:26px;display:block;font-weight:400;}
.form1_rec div{width:500px;float:right;margin-right:56px;}
.form1_rec span{width:140px;height:35px;display:inline-block;text-align:right;line-height:35px;color:#333;font-size:14px;}
/* 设置相对定位 */
.form1_rec label .a-num{width:98px;height:36px;line-height:36px;border:1px solid #ececec;border-radius:4px;display:inline-block;font-size:14px;color:#333;text-align:center;margin-right:20px;margin-bottom:10px;position:relative;cursor:pointer;}
/* 设置绝对定位 选隐藏 */
.form1_rec label .a-num .select-img{position:absolute;bottom:0;right:0;display:none;}
/* 选中后加蓝色边框 显示小图片 */
.form1_rec label .active{border:1px solid #229fe7;}
.form1_rec label .active .select-img{display:block;}
.num_rmb{margin-right:0;width:120px;height:36px;line-height:36px;border:1px solid #ececec;border-radius:4px;display:inline-block;font-size:14px;color:#333;padding:0 12px;}


</style>
</head>
<body>
	<input id="amt"  type="hidden" value="20000" />
    <div class="wrapper wrapper-content fadeInRight"
        style="padding-left: 0px; padding-right: 0px; padding-top: 0px; padding-bottom: 0px;">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox ">
                    <div class="ibox-title">                        
                        <div class="btn-group" role="group" aria-label="..."/>                           
                    </div>
                    <div id="content">
                        <form class="form1_rec">
							<label class="type1" style="height:100px;">
							<span>微信支付-充值金额：</span>
							<div class="clearfix">
							<span class="a-num active" RMB="20000">200元<img class="select-img" src="images/icon/select-icon.png" alt=""></span>
							<span class="a-num" RMB="50000">500元<img class="select-img" src="images/icon/select-icon.png" alt=""></span>
							<span class="a-num" RMB="100000">1000元<img class="select-img" src="images/icon/select-icon.png" alt=""></span>
							<span class="a-num" RMB="200000">2000元<img class="select-img" src="images/icon/select-icon.png" alt=""></span>
							</div>							
							</label>
							<!-- 	
							<label class="type1" style="height:20px;">
							<span style="width:200px;color:#FF4500;font-size:12px" >注:充值后可在账单明细中查看结果</span>							
							</label> -->
							<label class="type1" style="height:80px;">
							<span style="width:100%;color:#FF4500;font-size:16px;text-align:left" >牛年三月活动开门红:&nbsp;A.单次充值1000元赠150元;&nbsp;&nbsp;&nbsp;
							<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;B.单次充值2000元赠500元;
							<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;C.单次充值5000元赠1500元;&nbsp;&nbsp;&nbsp;</span>						
							</label>
							<label class="type1" style="height:50px;text-align:center">									
							<div>
								<button type="button" class="btn btn-warning" onclick="get()">获取二维码扫码充值</button>
							</div>
							</label>
							<label class="type1" style="height:180px;text-align:center">		
								<div id="qrcode"></div>
							</label>			
						</form>
					</div>
                </div>
            </div>
        </div>
    </div>    
</body>

<script>
function get(){
	$("#qrcode").html("");
	var amt=$("#amt").val();
	var url ="/cost/getqrcode"; 	
    $.ajax({
		type: "POST",
		url: url,
		data: {
            amt:amt             
        },
		success: function(data){
			var callback =JSON.parse(data);
		    if(callback.resp_code=="00"){
				$("#qrcode").qrcode({
					text: callback.qrcode
				});
		    }else{
				alert(callback.resp_desc);
		    }
		}
	});
}

//点击a-num事件
$(".a-num").click(function(){
    $(this).addClass("active").siblings().removeClass("active");   // 点击后选中添加active除其他兄弟元素的active
    // console.log(RMB);
    //$(".num_rmb").val(RMB);
   // $(".num_rmb").parent().children("e").text($(".num_rmb").val()*99) // 将值填入type2下面的(e)中
   // $(".num_rmb").parent().children("f").text($(".num_rmb").val()*990)    // 将值填入type2下面的(f)中
    $("#amt").val($(this).attr("RMB"));  
    $("#qrcode").html("");
})
// 改变事件
$(".num_rmb").change(function(){
    //if($(this).val()<10){                                              // 判断里面的值是否大于10，小于10让他大于10
    //    $(".num_rmb").val(10);
    //}a-num
    $(".a-num").removeClass("active");                                 // 移除a-num的选中事件
    //$(this).parent().children("e").text($(this).val()*99)               // 将值填入type2下面的(e)中
   // $(this).parent().children("f").text($(this).val()*990)              // 将值填入type2下面的(f)中
   })
</script>
</html>