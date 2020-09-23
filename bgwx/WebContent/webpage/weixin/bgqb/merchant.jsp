<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title></title>
        <script type="text/javascript" src="./plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
        <script src="./plug-in/weixin/merchant/mobiscroll/js/mobiscroll.core-2.5.2.js" type="text/javascript"></script>
        <script src="./plug-in/weixin/merchant/mobiscroll/js/mobiscroll.core-2.5.2-zh.js" type="text/javascript"></script>
        <link href="./plug-in/weixin/merchant/mobiscroll/css/mobiscroll.core-2.5.2.css" rel="stylesheet" type="text/css" />
        <link href="./plug-in/weixin/merchant/mobiscroll/css/mobiscroll.animation-2.5.2.css" rel="stylesheet" type="text/css" />

		<link href="./plug-in/weixin/merchant/css/reset.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="./plug-in/weixin/merchant/css/LArea.css"/>
		<link href="./plug-in/weixin/merchant/css/common.css" rel="stylesheet" type="text/css" />

        <script src="./plug-in/weixin/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1.js" type="text/javascript"></script>
        <script src="./plug-in/weixin/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1-zh.js" type="text/javascript"></script>
        <!-- S 可根据自己喜好引入样式风格文件 -->
        <script src="./plug-in/weixin/merchant/mobiscroll/js/mobiscroll.android-ics-2.5.2.js" type="text/javascript"></script>
        <link href="./plug-in/weixin/merchant/mobiscroll/css/mobiscroll.android-ics-2.5.2.css" rel="stylesheet" type="text/css" />
        <!-- E 可根据自己喜好引入样式风格文件 -->
        <style type="text/css">

        .content {
        padding: 15px;
        background: #fff;
        }
        .invalid:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
            color: red;  
		}
		
		.invalid::-moz-placeholder { /* Mozilla Firefox 19+ */
		    color: red;
		}
		
		input.invalid:-ms-input-placeholder{
		    color: red;
		}
		
		input.invalid::-webkit-input-placeholder {
		    color: red;
		}
        </style>
        
    </head>
    <body>
        <div   id="message" class="message">显示提示信息</div>
    	<div class="step">
    		<img src="./plug-in/weixin/merchant/images/1.jpg" alt="" />
    	</div>
    	<form id="mer_form" method="post">
    	<div class="com_mess">
    		<h3 class="com_mess_tit">商户信息</h3>
    		<div class="com_mess_item clearfix">
    			<label for="">商户名称</label>
    			<input id="merName"  type="text" placeholder="请输入营业执照的企业名称"/>
    		</div>
    		<div class="com_mess_item clearfix">
    			<label for="">商户简称</label>
    			<input id="merShortName" type="text" placeholder="请输入商户简称"/>
    		</div>
    		<div class="content-block com_mess_item">
    			<label for="">主营类目</label>
	            <input id="mainCategory" type="text" readonly="" placeholder="未选择"  value=""/>
	            <input id="value1" type="hidden" value="20,234,504"/>
	        </div>
	        <div class="content-block com_mess_item">
    			<label for="">所在区域</label>
	            <input id="location" type="text" readonly="" placeholder="城市选择"  value=""/>
	            <input id="value2" type="hidden" value="20,234,504"/>
	        </div>
	        <div class="com_mess_item clearfix">
    			<label for="">详细地址</label>
    			<input id="address" type="text" placeholder="请填写您的详细地址"/>
    		</div>
    		 <div class="com_mess_item clearfix">
    			<label for="">经营地址</label>
    			<input id="bizAddress" type="text" placeholder="请填写您的详细地址"/>
    		</div>
    		<div class="com_mess_item clearfix">
    			<label for="">营业范围</label>
    			<input id="bizRange" type="text" placeholder="与企业工商营业执照上的一致"/>
    		</div>
    		<div class="com_mess_item clearfix">
    			<label for="">营业执照号</label>
    			<input id="licenseNo" type="text" placeholder="请输入营业执照上的执照号码"/>
    		</div>
    		<div class="com_mess_itemB clearfix">
    			<label for="">营业执照</label>
    			<div class="com_mess_photos clearfix">
    				<a href="javascript:;">上传副本</a>
    			</div>
    		</div>
    		<div class="com_mess_itemB clearfix">
    			<label for="">门店照片</label>
    			<div class="com_mess_photos clearfix">
    				<a href="javascript:;">门店</a>
    				<a href="javascript:;">营业场所</a>
    				<a href="javascript:;">入境照</a>
    			</div>
    		</div>
    	</div><!--com_mess-->
		
		<div class="shadow hide"></div>
    	<div class="pop_up hide">
    		<h3 class="pop_up_tit">
    			上传营业执照副本
    			<a href="javascript:;" class="pop_up_close"></a>
    		</h3>
    		<div class="pop_up_img"><img src="./plug-in/weixin/merchant/images/5.jpg" alt="" /></div>
    		<div class="pop_up_load">
    			<a href="javascript:;">从手机相册选择<input type="file" accept="plug-in/weixin/merchant/img/*" value="" /></a>
    		</div>
    	</div>

    	<div class="com_mess">
    		<h3 class="com_mess_tit">联系人</h3>
    		<div class="com_mess_item clearfix">
    			<label for="">法人姓名</label>
    			<input id="contactName" type="text" placeholder="请输入法人姓名"/>
    		</div>
    		
	        <div class="com_mess_item clearfix">
    			<label for="">证件号码</label>
    			<input id="cardNo" type="text" placeholder="请输入证件号码"/>
    		</div>
    		
    		<div class="com_mess_itemB clearfix">
    			<label for="">法人身份证</label>
    			<div class="com_mess_photos clearfix">
    				<a href="javascript:;">上传正面</a>
    				<a href="javascript:;">上传反面</a>
    			</div>
    		</div>
    	</div><!--com_mess-->

		<div class="com_mess">
    		<div class="com_mess_item clearfix">
    			<label for="">手机号</label>
    			<input type="text" name="phone" placeholder="请输入您的手机号"/>
    		</div>
    		
	        <div class="com_mess_item clearfix">
    			<label for="">验证码</label>
    			<input type="text" name="checkCode" placeholder="输入短信中的验证码" style="width:1.46rem;" />
    			<a href="javascript:;" class="send_code">获取验证码</a>
    		</div>
    	</div><!--com_mess-->

    	<div class="agree clearfix">
    		<input name="contractCheck" type="checkbox"  checked="checked" />
    		<label for="">我同意并遵守</label>
    		<a href="javascript:;">《天下分期服务协议》</a>
    	</div><!--agree-->

        <div class="next_step">
            <a href="javascript:;" onclick="submit_form()">下一步</a>
        </div>
        <p class="next_save">点击“下一步”自动保存页面信息</p>
        </form>

    </body>
</html>
<script src="./plug-in/weixin/merchant/js/LAreaData1.js"></script>
    <script src="./plug-in/weixin/merchant/js/LAreaData2.js"></script>
    <script src="./plug-in/weixin/merchant/js/LArea.js"></script>
    <script>
    /*下一步提交*/
    function submit_form(){
       	//if(!checkForm()){
       	  //  return false;	
       	//}
       	var str_data=$("#mer_form input").map(function(){
         	  return ($(this).attr("id")+'='+$(this).val());
         	   }).get().join("&") ;
       	alert(str_data);
       	$.ajax({
       	 
       	   type: "POST",
       	   url: "merController.do?savemer",
       	   data: str_data,
       	   success: function(data){
       		var callback =JSON.parse(data);
       		alert(callback.msg);
     		alert(callback.success);
       	 }
       });
    }
    
    
    /*
                   校验表单
    */
    function checkForm(){
    	if(isNull($("#merName").val())){
    		$("#message").text($("#merName").attr("placeholder"));
            $("#message").css("display","block");
            $("#merName").addClass('invalid');
            return false;
    	}
    	if(isNull($("#mainCategory").val())){
    		$("#mainCategory").text($("#mainCategory").attr("placeholder"));
            $("#mainCategory").css("display","block");
            $("#mainCategory").addClass('invalid');
            return false;
    	}
		if(isNull($("#location").val())){
			$("#location").text($("#location").attr("placeholder"));
            $("#location").css("display","block");
            $("#location").addClass('invalid');
            return false;		
		}
		if(isNull($("#address").val())){
			$("#address").text($("#address").attr("placeholder"));
            $("#address").css("display","block");
            $("#address").addClass('invalid');
            return false;
		}
		if(isNull($("#bizAddress").val())){
			$("#bizAddress").text($("#merName").attr("placeholder"));
            $("#bizAddress").css("display","block");
            $("#bizAddress").addClass('invalid');
            return false;
		}
		if(isNull($("#bizRange").val())){
			$("#bizRange").text($("#bizRange").attr("placeholder"));
            $("#bizRange").css("display","block");
            $("#bizRange").addClass('invalid');
            return false;
		}
		if(isNull($("#licenseNo").val())){
			$("#licenseNo").text($("#licenseNo").attr("placeholder"));
            $("#licenseNo").css("display","block");
            $("#licenseNo").addClass('invalid');
            return false;
		}
		if(isNull($("#contactName").val())){
			$("#contactName").text($("#contactName").attr("placeholder"));
            $("#contactName").css("display","block");
            $("#contactName").addClass('invalid');
            return false;
		}
		if(isNull($("#cardNo").val())){
			$("#cardNo").text($("#cardNo").attr("placeholder"));
            $("#cardNo").css("display","block");
            $("#cardNo").addClass('invalid');
            return false;
		}
		return true;
    	
    }
    function isNull( str ){
    	if ( str == "" ) return true;
    	var regu = "^[ ]+$";
    	var re = new RegExp(regu);
    	return re.test(str);
    }
    
    </script>
    <script>
        var area1 = new LArea();
        area1.init({
            'trigger': '#demo1', //触发选择控件的文本框，同时选择完毕后name属性输出到该位置
            'valueTo': '#value1', //选择完毕后id属性输出到该位置
            'keys': {
                id: 'id',
                name: 'name'
            }, //绑定数据源相关字段 id对应valueTo的value属性输出 name对应trigger的value属性输出
            'type': 1, //数据源类型
            'data': LAreaData //数据源
        });
        area1.value=[1,15,3];//控制初始位置，注意：该方法并不会影响到input的value
        var area2 = new LArea();
        area2.init({
            'trigger': '#demo2',
            'valueTo': '#value2',
            'keys': {
                id: 'value',
                name: 'text'
            },
            'type': 2,
            'data': [provs_data, citys_data, dists_data]
        });

       
    </script>
    <script>
    $(function(){
    	$('.com_mess_photos a').click(function(){
    		$('.shadow,.pop_up').show();
    	});
    	$('.pop_up_close').click(function(){
    		$('.shadow,.pop_up').hide();
    	});
    });

    
    
    </script>