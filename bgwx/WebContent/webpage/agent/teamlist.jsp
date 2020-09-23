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
   <!--  <script src="<%=basePath %>/plug-in/merchant/fileinput/js/plugins/sortable.js"    type="text/javascript"></script>
    <script src="<%=basePath %>/plug-in/merchant/fileinput/js/fileinput.js"    type="text/javascript"></script>
    <script src="<%=basePath %>/plug-in/merchant/fileinput/js/locales/fr.js"    type="text/javascript"></script>
    <script src="<%=basePath %>/plug-in/merchant/fileinput/js/locales/es.js"    type="text/javascript"></script>
    <script src="<%=basePath %>/plug-in/merchant/fileinput/themes/explorer-fa/theme.js"    type="text/javascript"></script>
    <script src="<%=basePath %>/plug-in/merchant/fileinput/themes/fa/theme.js"    type="text/javascript"></script>-->	
	<script language="javascript" type="text/javascript" src="<%=basePath %>/plug-in/My97DatePicker/WdatePicker.js"></script>
 
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
                            	<div class="form-group posr">
                                   <label for="account" class="control-label">客户账号</label>
                                   <input type="text" class="form-control" name="accountid" id="accountid"/>
                                </div>
                                <div class="form-group posr">
                                   <label for="date" class="control-label">查询日期</label>
                                   <input type="text" class="form-control" onClick="WdatePicker({dateFmt: 'yyyyMM' , isShowToday: false, isShowClear: false })" name="date" id="date"/>
                                </div>
                            </div>
                            <div class="col-xs-12"></div>
                        </div>
                        <div class="btn-group" role="group" aria-label="...">
                            <button type="button" class="btn btn-warning"  onclick="getList('-1')">查询</button>
                        </div>
                    </div>
                    <div class="ibox-content">                        
                        <table id="table" class="table table-striped">
                            <thead class="clearfix">
                                <tr class="clearfix">
                                	<th>账号</th>
                                    <th>姓名</th>
                                    <th>团队人数(人)</th>
                                    <th>团队当月交易额(元)</th>
                                    <th>本人当月交易额(元)</th> 
                                    <th>操作</th>                                 
                                </tr>
                            </thead>
                            <tbody id="table_list_2"></tbody>
                        </table>
                        <div class="btns clearfix">
                            <div class="M-box4"></div>
                        </div>
                        <br>
                        共
                        <span id="total">0</span>
                        条记录
                    </div>
                </div>
            </div>
        </div>
    </div>    
</body>

    <script>   
    $(function(){    	
        getList('-1');          
    });
    /* 初始化pager */
    var pager = {};
    function qteam(id){
    	getList(id);
    }
    
    function getList(id){
        var accountid;
        if(id=='-1'){
        	accountid = $("#accountid").val();   
        }else{
        	accountid = id;
        }        
        var date = $("#date").val();    		
        $.ajax({
            type: "POST",
            dataType: "json",
            url:"agentController.do?countteamtrans",
            data: {
                accountid:accountid,
                date:date
            },
            success: function(data) {   
            	var html = '';             
                if(data.SUCCESS == "00"){
                    pager = data["RESULT_CONTENT"];
                    if(pager.rowCount!=0){
                        var list = pager.list;
                        $(list).each(function(){
                            html += "<tr class='clearfix'>\n" +
                            	"                <td>"+this.TACCOUNT+"</td>\n" +
                                "                <td>"+this.NAME+"</td>\n" +
                                "                <td>"+this.CNUM+"</td>\n" +
                                "                <td>"+this.STAMT+"</td>\n" +
                                "                <td>"+this.BAMT+"</td>\n";
                                if(this.CNUM>0){
                                	html+="				 <td><div><button class='btn btn-primary' onclick='qteam(\""+this.SUBACCOUNT+"\")'>详情</button></td></div>\n";
                                }else{
                                	html+="<td></td>\n";
                                }
                                
                                html+="            </tr>";


                        });
                    }else{
                        html = "<tr><td colspan='13'>暂无数据</td></tr>";
                    }
                    //显示总记录条数
                    $("#total").text(pager.rowCount);
                    $("#table_list_2").html(html);
                }else if(data.NULL=='00'){
                	alert(2);
                	location.reload(true);
                }else{
                	alert(data.FAIL);
                }               
            },
            complete:function(){
                pagination();
            }
        });
    }
    function pagination(){
        $('.M-box4').pagination({
            pageCount: pager.pageCount,
            current:pager.currentPageNumb,
            coping:true,
            homePage:'首页',
            endPage:'末页',
            prevContent:'上页',
            nextContent:'下页',
            keepShowPN:true,
            jump:true,
            callback:function(api){
                getList(api.getCurrent());
            }
        });
    }
    function isNull( str ){
	    if ( str == "" ) return true;
	    var regu = "^[ ]+$";
	    var re = new RegExp(regu);
	    return re.test(str);
	}
</script>
</html>