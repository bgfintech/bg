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
	<script language="javascript" type="text/javascript" src="<%=basePath %>/plug-in/My97DatePicker/WdatePicker.js"></script>
    
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
									<label class="control-label" style="width:60px">姓名</label> <input
										type="text" class="form-control" name="name" id="name" />
								</div>
								<div class="form-group posr" id="p2">
									<label class="control-label" style="width:60px">身份证</label> <input
										type="text" class="form-control" name="idcard" id="idcard" />
								</div>
								<div class="form-group posr" id="o1" style="display:none">
									<label class="control-label" style="width:60px">企业全称</label> <input
										type="text" class="form-control" name="org" id="org" />
								</div>
								<div class="form-group posr">
									<select id="type" class="single-line">
										<option value="1">个人</option>
										<option value="2">企业</option>
									</select>
								</div>
								<div class="form-group posr">
                                   <label for="date" class="control-label" style="width:60px">开始时间</label>
                                   <input type="text" class="form-control" onClick="WdatePicker({dateFmt: 'yyyyMMdd' ,minDate:'%y-%M-%d'})" name="sdate" id="sdate"/>
                                </div>
								<div class="form-group posr">
									<select id="days" class="single-line">
										<option value="1">1个月</option>
										<option value="3">3个月</option>
										<option value="6">6个月</option>
									</select>
								</div>
								<div class="form-group posr">
									<button type="button" class="btn btn-warning" onclick="add()">新增</button>
								</div>
							</div>
							<div class="col-xs-12"></div>
						</div>
						<div class="btn-group" role="group" aria-label="..." />
					</div>
					<div class="ibox-content" id="content">
						<table id="claim-table"></table>
					</div>
				</div>
			</div>
        </div>
    </div>    
</body>

<script>
	$("#type").change(function(data){
		var t = $("#type").val();
		if(t==1){
			$("#p1").show();
			$("#p2").show();
			$("#o1").hide();
		}else{
			$("#p1").hide();
			$("#p2").hide();
			$("#o1").show();
		}
	});
	
	var nt = "";
	var ot = "";
	var it = "";
	function add(){
		var type = $("#type").val();
		var name = $("#name").val();
		var idcard = $("#idcard").val();
		var org = $("#org").val();
		var days = $("#days").val();
		var sdate = $("#sdate").val();    
		if(type==1){
			if(name==''||name==null||idcard==''||idcard==null){
				alert('姓名和身份证不能为空');
				return;
			}
			if(nt==name&&it==idcard){
				alert("请不要重复设置");
				return;
			}
			nt=name;
			it=idcard;
			//if(!IdentityCodeValid(idcard)){
			//	alert('身份证号有误');
			//	return;
			//}
		}else{
			if(org==null||org==''){
				alert('企业名称不能为空');
				return;
			}
			if(ot==org){
				if(!confirm("是否重复查询?")){
					return;
				}
			}
			ot=org;
		}
        if(isNull(sdate)){
        	alert('请选择开始时间');
        	return;
        }	
		var url = "/shesu/setmonitor";	
		$.ajax({
            type: "POST",
            dataType: "json",
            url:url,
            data: {
                name:name,
                idcard:idcard,
                org:org,
                type:type,
                sdate:sdate,
                days:days
            },
            success: function(data) {                   
                alert(data);
                $('#claim-table').bootstrapTable('refresh');
            }
        });		
	}
	

	$(function() {
		initTable();
	});
	function initTable() {
		var url = "/shesu/getmonitorlist";
		var table = $('#claim-table').bootstrapTable({
			method : 'POST',
			dataType : 'json',
			contentType : "application/x-www-form-urlencoded",
			cache : false,
			striped : true,//是否显示行间隔色
			singleSelect : true,
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			url : url,
			showColumns : true,
			pagination : true,
			queryParams : queryParamsClaim,
			minimumCountColumns : 2,
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 20, //每页的记录行数（*）
			pageList : [ 20, 50 ], //可供选择的每页的行数（*）
			//responseHandler:responseHandlerClaim,
			smartDisplay : true, // 智能显示 pagination 和 cardview 等
			columns : [{
						field : 'NAME',
						title : '姓名',
						valign : 'middle',
					},
					{
						field : 'SDATE',
						title : '开始时间',
						valign : 'middle',
					},
					{
						field : 'EDATE',
						title : '结束时间',
						valign : 'middle'
					},
					{
						field : 'ISRUN',
						title : '是否运行中',
						valign : 'middle',
					},
					{
						title : '操作',
						field : 'option',
						align : 'center',
						formatter : function(value, row, index) {
							var e = '<button class="btn btn-primary" onclick="opend(\''+ row.ID+ '\')">详情</button>';
							return e;
						}
					}]
		});
	}

	function opend(id) {
		//window.location.href = "/shesu/monitordetail?id=" + id;
	}

	function queryParamsClaim(params) {
		var currPage = this.pageNumber;
		var param = {
			limit : params.limit, //页面大小
			offset : currPage
		//页码params.offset
		}
		return param;
	}
	function responseHandlerClaim(res) {
		if (res) {
			return {
				"total" : res.total,//总记录数
				"rows" : res.rows
			//数据
			};
		} else {
			return {
				"rows" : [],
				"total" : 0
			};
		}
	}
	function isNull( str ){
	    if ( str == "" ) return true;
	    var regu = "^[ ]+$";
	    var re = new RegExp(regu);
	    return re.test(str);
	}
	function IdentityCodeValid(code) {
		code=code.toLocaleUpperCase();
    	$("#idcard").val(code);
        var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
        var tip = "";
        var pass= true;
        
        if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
            tip = "身份证号格式错误";
            pass = false;
        } else if(!city[code.substr(0,2)]){
            tip = "地址编码错误";
            pass = false;
        } else{
            //18位身份证需要验证最后一位校验位
            if(code.length == 18){
                code = code.split('');
                //∑(ai×Wi)(mod 11)
                //加权因子
                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                //校验位
                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++)
                {
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                var last = parity[sum % 11];
                if(parity[sum % 11] != code[17]){
                    tip = "校验位错误";
                    pass =false;
                }
            }
        }
        return pass;
    }
</script>
</html>