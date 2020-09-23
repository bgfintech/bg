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
    <link href="<%=basePath %>/plug-in/bg/css/bootstrap-table.css" rel="stylesheet">
	<script src="<%=basePath %>/plug-in/bg/js/bootstrap-table.min.js"></script>
	<script src="<%=basePath %>/plug-in/bg/js/bootstrap-table-zh-CN.min.js"></script>
    
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
                        <div class="btn-group" role="group" aria-label="..."/>                           
                    </div>
                    <div class="ibox-content" id="content">     
                        <table id="claim-table" ></table>
					</div>
                </div>
            </div>
        </div>
    </div>    
</body>

<script>
$(function () {
    initTable();
});
function initTable(){
	var url = "/cost/getlist";
    var table = $('#claim-table').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        striped: true,//是否显示行间隔色
        singleSelect: true,
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        url:url,
        showColumns:true,
        pagination:true,
        queryParams : queryParamsClaim,
        minimumCountColumns:2,
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 20,                       //每页的记录行数（*）
        pageList: [20,50],        //可供选择的每页的行数（*）
        //responseHandler:responseHandlerClaim,
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        columns: [{
            field : 'INPUTTIME',
            title : '账单日期',
            valign : 'middle',
        }, {
            field : 'OCCURTYPE',
            title : '产品',
            valign : 'middle',
        }, {
            field : 'NAME',
            title : '产品明细',
            valign : 'middle'
        }, {
            field : 'BALANCEAMT',
            title : '费用',
            valign : 'middle',
        }, {
            field : 'AFTERBALANCE',
            title : '余额',
            valign : 'middle',
        }]
    });
}
function queryParamsClaim(params) {
	var currPage = this.pageNumber;
    var param = {
   		limit: params.limit,   //页面大小
        offset: currPage  //页码params.offset
    }
    return param;
}
function responseHandlerClaim(res) { 
    if (res) {
        return {
        	 "total": res.total,//总记录数
             "rows": res.list   //数据
        };
    } else {
        return {
            "rows" : [],
            "total" : 0
        };
    }
}
</script>
</html>