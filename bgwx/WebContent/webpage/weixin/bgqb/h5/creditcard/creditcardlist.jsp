<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
<title></title>
<script type="text/javascript"
	src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
<link href="plug-in/weixin/merchant/css/reset.css?101" rel="stylesheet"
	type="text/css" />
<link href="plug-in/weixin/merchant/css/common.css?101" rel="stylesheet"
	type="text/css" />
</head>
<body style="padding-bottom:0.6rem;">
	<div class="">
		<h3 class="pcenter_tit">信用卡列表</h3>
		<c:forEach items="${cardlist}" var="card">
		  <ul class="pcenter_list">
			<li class="pcenter_listItem">
				<div class="pcenter_listItem_con clearfix">
					<span class="pcenter_listItem_conL">${card.BANKNAME}(${card.DCARDNO})</span>
					<span class="pcenter_listItem_conR">${card.NAME}</span>
				</div>
			</li>
			<li class="pcenter_listItem">
				<c:choose>
					<c:when test="${card.ISPLAN=='1'}">
						<a href="creditCardController.do?viewrepayplan&planid=${card.PLANID}">
						<div class="pcenter_listItem_con clearfix">
							<span class="mod_passL">查看还款计划</span><span class="mod_passR"></span>
						</div>
						</a>
					</c:when>
					<c:otherwise>
						<a href="creditCardController.do?gosetrepayplan&cardno=${card.BANKCARDNO}">
						<div class="pcenter_listItem_con clearfix">
							<span class="mod_passL">设置还款计划</span><span class="mod_passR"></span>
						</div>
						</a>
					</c:otherwise>
				</c:choose>				
			</li>
		  </ul>
		</c:forEach>  		
	</div>
	<div class="edit_complete">
		<a href="creditCardController.do?gobindcreditcard&openid=${openid}" class="quit_btn">+添加信用卡</a>
	</div>
</body>
</html>





