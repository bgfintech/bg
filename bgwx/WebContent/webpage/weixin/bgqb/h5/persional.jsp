<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
<title></title>
<script type="text/javascript"	src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
<link href="plug-in/weixin/merchant/css/reset.css?101" rel="stylesheet"	type="text/css" />
<link href="plug-in/weixin/merchant/css/common.css?101" rel="stylesheet" type="text/css" />
</head>
<body style="padding-bottom:0.6rem;">
	<input id="accountid" name="accountid" type="hidden" value="${personinfo.ACCOUNT}" />
	<input id="openid" name="openid" type="hidden" value="${openid}" />
	<input id="iscertified" name="iscertified" type="hidden" value="${accountinfo.iscertified}" />
	<input id="balance" name="balance" type="hidden" value="${balance}" />
	<div class="">
		<h3 class="pcenter_tit">个人信息</h3>
		<ul class="pcenter_list">
			<li class="pcenter_listItem">
				<div class="pcenter_listItem_con clearfix">
					<span class="pcenter_listItem_conL">姓名</span><span
						class="pcenter_listItem_conR">${personinfo.NAME}</span>
				</div>
			</li>
			<li class="pcenter_listItem">
				<div class="pcenter_listItem_con clearfix">
					<span class="pcenter_listItem_conL">银行卡号</span><span
						class="pcenter_listItem_conR">${personinfo.TBANKCARDNO }</span>
				</div>
			</li>
			<li class="pcenter_listItem">
				<div class="pcenter_listItem_con clearfix">
					<span class="pcenter_listItem_conL">所属银行</span><span
						class="pcenter_listItem_conR">${personinfo.BANKNAME }</span>
				</div>
			</li>
			<li class="pcenter_listItem">
				<div class="pcenter_listItem_con clearfix">
					<span class="pcenter_listItem_conL">预留手机号</span><span
						class="pcenter_listItem_conR">${personinfo.TBANKPHONE }</span>
				</div>
			</li>
			<!-- <li class="pcenter_listItem">
      <div class="pcenter_listItem_con clearfix">
        <span class="pcenter_listItem_conL">当前费率</span><span class="pcenter_listItem_conR">${personinfo.GBPSTR }</span>
      </div>
    </li> -->
		</ul>		
		<a href="acctController.do?gobindbankcard&openid=${openid}">
			<div class="mod_pass clearfix">
				<span class="mod_passL">绑定或更换银行卡(储蓄卡)</span><span class="mod_passR"></span>
			</div>
		</a>
		<a href="acctController.do?goeditpasswd&openid=${openid}">
			<div class="mod_pass clearfix">
				<span class="mod_passL">修改密码</span><span class="mod_passR"></span>
			</div>
		</a>
		<div class="bank_card_name clearfix">
			<span class="bank_card_nameL">钱包余额</span> 
			<span class="bank_card_numR" id="tbalance" style="width:1rem;">${balance }元</span> 
			<input type="button" class="bank_vcode" onclick="getcash();" value="提现" />
		</div>
		<div>温馨提示:提现T+1到账.目前20元以下暂不支持提现</div>
	</div>	<div class="quit">
		<a href="acctController.do?logout&openid=${openid}&source=gopersonal" class="quit_btn">退出登录</a>
	</div>
</body>
<script type="text/javascript">
function getcash(){ 
	var balance = $("#balance").val();
	var openid = $("#openid").val();
	var accountid = $("#accountid").val();
	if(balance<20){
		alert('余额少于20元暂不支持提现');
		return;
	}
	$.ajax({
            type: "POST",
            dataType: "json",
            url:"transController.do?balancetocash",
            data: {
                openid:openid,
                accountid:accountid,
                balance:balance
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   $("#balance").val("0");
                   $("#tbalance").html("0.00元");
                   alert('提现成功 T+1到账');
                }else{
                   alert(data.FAIL);
                }
            }
        });
}
</script>
</html>



