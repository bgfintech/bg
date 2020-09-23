<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<%@include file="/taglib/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
<link href="<%=basePath%>/plug-in/merchant/fileinput/css/fileinput.css"
	media="all" rel="stylesheet" type="text/css" />
<link
	href="<%=basePath%>/plug-in/merchant/fileinput/themes/explorer-fa/theme.css"
	media="all" rel="stylesheet" type="text/css" />
<script
	src="<%=basePath%>/plug-in/merchant/fileinput/js/plugins/sortable.js"
	type="text/javascript"></script>
<script src="<%=basePath%>/plug-in/merchant/fileinput/js/fileinput.js"
	type="text/javascript"></script>
<script src="<%=basePath%>/plug-in/merchant/fileinput/js/locales/fr.js"
	type="text/javascript"></script>
<script src="<%=basePath%>/plug-in/merchant/fileinput/js/locales/es.js"
	type="text/javascript"></script>
<script
	src="<%=basePath%>/plug-in/merchant/fileinput/themes/explorer-fa/theme.js"
	type="text/javascript"></script>
<script
	src="<%=basePath%>/plug-in/merchant/fileinput/themes/fa/theme.js"
	type="text/javascript"></script>

<style type="text/css">
.inputfln {
	float: none;
	margin: 0;
}

th {
	text-align: center;
}

td {
	text-align: center;
}

.box {
	width: 400px;
	height: auto;
	overflow-y: auto;
	background-color: #fff;
	position: absolute;
	right: -400px;
	top: 0;
	z-index: 111111;
}
</style>
</head>
<body>

	<div class="wrapper wrapper-content fadeInRight"
		style="padding-left: 0px; padding-right: 0px; padding-top: 0px; padding-bottom: 0px;">
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox ">
					<div class="ibox-title">

						<div class="form-inline">
							<div class="col-xs-12">
								<div class="form-group posr">
									<label for="merid" class="control-label">客户号</label>
									<input type="text" class="form-control" name="custid" id="custid">
									<label for="merid" class="control-label">取现卡ID</label>
									<input type="text" class="form-control" name="cashbindid" id="cashbindid">
								</div>
								<div class="form-group posr">	
									<label for="merid" class="control-label">金额</label>
									<input type="text" class="form-control" name="amt" id="amt">
									<label for="merid" class="control-label">姓名</label>
									<input type="text" class="form-control" name="name" id="name">
								</div>
								<div class="form-group posr">	
									<label for="merid" class="control-label">身份证号</label>
									<input type="text" class="form-control" name="idcard" id="idcard">
									<label for="merid" class="control-label">账号</label>
									<input type="text" class="form-control" name="accountid" id="accountid">
								</div>
							</div>
							<div class="col-xs-12"></div>
						</div>						

					</div>
					<div class="ibox-content">
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" class="btn btn-success"
								class="btn btn-primary" data-toggle="modal" data-target="#"
								onclick="transfer();">手工转账</button>							
						</div>						
					</div>
					<div class="ibox-content">
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" class="btn btn-success"
								class="btn btn-primary" data-toggle="modal" data-target="#"
								onclick="kft();">手机快付通</button>							
						</div>						
					</div>
				</div>
			</div>
		</div>
	</div>
	


</body>

<script>
	
	function transfer() {
	alert(1);
		var custid = $("#custid").val();
		var cashbindid = $("#cashbindid").val();
		var amt = $("#amt").val();
		var name = $("#name").val();
		var idcard = $("#idcard").val();
		var accountid = $("#accountid").val();
		alert(custid);
		$.ajax({
            type: "POST",
            dataType: "json",
            url:"merController.do?transfer",
            data: {
                custid:custid,
                cashbindid:cashbindid,
                amt:amt,
                name:name,
                idcard:idcard,
                accountid:accountid
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   alert("成功");
                }else{
                   alert(data.FAIL);
                }
            }
        });
	}
	function kft() {
	alert(2);
		var accountid="13613312552";
		var pbankno="10310000";
		var pamt="1989200";
		var cd="1";
		$.ajax({
            type: "POST",
            dataType: "json",
            url:"transController.do?kft",
            data: {
                accountid:accountid,
                pbankno:pbankno,
                pamt:pamt,
                cd:cd
            },
            success: function(data) {   
                if(data.SUCCESS == "00"){
                   alert("成功");
                }else{
                   alert(data.FAIL);
                }
            }
        });
	}
	
</script>
</html>