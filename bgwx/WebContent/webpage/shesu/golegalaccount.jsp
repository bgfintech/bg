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
    <link href="<%=basePath %>/plug-in/merchant/fileinput/css/fileinput.css" media="all"    rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>/plug-in/merchant/fileinput/themes/explorer-fa/theme.css"    media="all" rel="stylesheet" type="text/css" />
    
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
                            	<div class="form-group posr" id="p1">
                                   <label class="control-label" style="width:70px">账号</label>
                                   <input type="text" class="form-control" name="accountid" id="accountid"/>
                                </div>
                                <div class="form-group posr" id="p2">
                                   <label class="control-label" style="width:70px">统计版价格</label>
                                   <input type="text" class="form-control" name="entstat" id="entstat" />
                                </div>
                                <div class="form-group posr" id="p3" >
                                   <label class="control-label" style="width:70px">详细版价格</label>
                                   <input type="text" class="form-control" name="entout" id="entout"/>
                                </div> 
                                              
                                <div class="form-group posr">
                                   <button type="button" class="btn btn-warning"  onclick="create()">创建账户</button>
                                </div>
                            </div>
                            <div class="col-xs-12"></div>
                            <div class="col-xs-12">
                            	<div class="form-group posr" id="p1">
                                   <label class="control-label" style="width:60px">账号</label>
                                   <input type="text" class="form-control" name="accountid1" id="accountid1"/>
                                </div>
                                <div class="form-group posr" id="p2">
                                   <label class="control-label" style="width:60px">金额</label>
                                   <input type="text" class="form-control" name="amt" id="amt" />
                                </div>
                                <div class="form-group posr">
                                   <select id="type" class="single-line">
						                <option value="2" >赠送</option>
						                <option value="1" >充值</option>
						            </select>
                                </div>                                              
                                <div class="form-group posr">
                                   <button type="button" class="btn btn-warning"  onclick="cz()">确认添加金额</button>
                                </div>
                            </div>
                            <div class="col-xs-12"></div>
                        </div>
                        <div class="btn-group" role="group" aria-label="..."/>                           
                    </div>
                    <div class="ibox-content" id="content">     
                        
					</div>
                </div>
            </div>
        </div>
    </div>    
</body>

<script>
	function create(){
		var accountid = $("#accountid").val();
		var entstat = $("#entstat").val();
		var entout = $("#entout").val();
		var url = "/acctController?createaccount";	
		$.ajax({
            type: "POST",
            dataType: "json",
            url:url,
            data: {
               accountid:accountid,
               entstat:entstat,
               entout:entout
            },
            success: function(data) {                   
                if(data.resp_code == "00"){
                	alert("创建成功");
                }else{
                	alert("创建失败");
                }
            }
        });		
	}
	function cz(){
		var type = $("#type").val();
		var accountid = $("#accountid1").val();
		var amt = $("#amt").val();
		var url = "/acctController?recharge";	
		$.ajax({
            type: "POST",
            dataType: "json",
            url:url,
            data: {
               accountid:accountid,
               type:type,
               amt:amt
            },
            success: function(data) {         
                if(data.resp_code == "00"){
                	alert("新增费用成功");
                }else{
                	alert("新增费用失败");
                }
            }
        });		
	}
</script>
</html>