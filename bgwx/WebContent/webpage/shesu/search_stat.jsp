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
                                   <label class="control-label" style="width:60px">姓名</label>
                                   <input type="text" class="form-control" name="name" id="name"/>
                                </div>
                                <div class="form-group posr" id="p2">
                                   <label class="control-label" style="width:60px">身份证</label>
                                   <input type="text" class="form-control" name="idcard" id="idcard" />
                                </div>
                                <div class="form-group posr" id="o1" style="display:none">
                                   <label class="control-label" style="width:60px">企业全称</label>
                                   <input type="text" class="form-control" name="org" id="org"/>
                                </div>
                                <div class="form-group posr">
                                   <select id="type" class="single-line">
						                <option value="1" >个人</option>
						                <option value="2" >企业</option>
						            </select>
                                </div>
                                <div class="form-group posr">
                                   <button type="button" class="btn btn-warning"  onclick="find()">查询</button>
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
	function find(){		
		var type = $("#type").val();
		var name = $("#name").val();
		name = name.replace(/\s+/g, "").replace("（", "(").replace("）", ")");
		var idcard = $("#idcard").val();
		var org = $("#org").val();
		org = org.replace(/\s+/g, "").replace("（", "(").replace("）", ")");
		var nc = "";
		if(type==1){
			if(name==''||name==null||idcard==''||idcard==null){
				alert('姓名和身份证不能为空');
				return;
			}
			if(nt==name&&it==idcard){
				if(!confirm("再次查询需重新计费,是否继续?")){
					return;
				}
			}			
			if(!IdCodeValid(idcard)){				
				if(!confirm("身份证号可能有误,是否继续?")){
					return;
				}
			}
			nt=name;
			nc=name;
			it=idcard;
		}else{
			if(org==null||org==''){
				alert('企业名称不能为空');
				return;
			}
			if(ot==org){
				if(!confirm("再次查询需重新计费,是否继续?")){
					return;
				}
			}
			ot=org;
			nc=org;
		}		
		$("#content").html("");
		var url = "/shesu/entstat";	
		$.ajax({
            type: "POST",
            dataType: "json",
            url:url,
            data: {
                name:name,
                idcard:idcard,
                org:org,
                type:type                
            },
            success: function(data) {                   
                if(data.resp_code == "00"){
                	var html="";                	
                	var detail = "";
                	var preservation = data.preservation;
                	if(preservation!=null&&preservation!=''){
                		html += countDetail2html(preservation,"非诉财产保全");                		
                	}
                	var civil = data.civil;
                	if(civil!=null&&civil!=''){
                		html += countDetail2html(civil,"民事");                		
                	}                	
                	var criminal = data.criminal;
                	if(criminal!=null&&criminal!=''){
                		html += countDetail2html(criminal,"刑事");
                	}
                	var implement = data.implement;
                	if(implement!=null&&implement!=''){
                		html += countDetail2html(implement,"执行");                		
                	}
                	
                	var administrative = data.administrative;
                	if(administrative!=null&&administrative!=''){
                		html += countDetail2html(administrative,"行政");
                	}
                	var bankrupt = data.bankrupt;
                	if(bankrupt!=null&&bankrupt!=''){
                		html += countDetail2html(bankrupt,"强制清算");
                	} 
                	$("#content").html(html);
                }else{
                	var html = "<h3>"+nc+":&nbsp;&nbsp;"+data.resp_desc+"</h3>";
                	$("#content").html(html);
                }
            }
        });		
	}
	
	function countDetail2html(cd,str){		
		var html="<table id='table' class='table table-striped'>"
      	  +   "<thead class='clearfix'><tr><th colspan='2' align='left'>"+str+"</th></tr></thead>"
            +   "<tbody id='table_list_2'>"
			  +   "<tr class='clearfix'><td style='width: 130px'>原告案数</td><td>被告案数</td><td>第三人案数</td><td>作为原告金额</td><td>作为被告金额</td><td>作为第三人金额</td><td>立案时间分布</td><td>案由分布</td><td>结案方式分布</td><td>涉案地域分布</td></tr>"
			  +   "<tr class='clearfix'>"
			  +   "<td>"+n(cd.count_yuangao)+"["+n(cd.count_wei_yuangao)+"]</td>"
			  +   "<td>"+n(cd.count_beigao)+"["+n(cd.count_wei_beigao)+"]</td>"
			  +   "<td>"+n(cd.count_other)+"["+n(cd.count_wei_other)+"]</td>"
			  +   "<td>"+n(cd.money_yuangao)+"["+n(cd.money_wei_yuangao)+"]</td>"
			  +   "<td>"+n(cd.money_beigao)+"["+n(cd.money_wei_beigao)+"]</td>"
			  +   "<td>"+n(cd.money_other)+"["+n(cd.money_wei_yuangao)+"]</td>"
			  +   "<td>"+n(cd.larq_stat)+"</td>"
			  +   "<td>"+n(cd.ay_stat)+"</td>"
			  +   "<td>"+n(cd.jafs_stat)+"</td>"
			  +   "<td>"+n(cd.area_stat)+"</td>"
			  +"</tr></tbody></table>";			  
        return html;
	}
	
	function n(a){
		if(a==null||a==''){
			return "-";
		}
		return a;
	}
	

	function IdCodeValid(code){  
	    //身份证号合法性验证  
	    //支持15位和18位身份证号  
	    //支持地址编码、出生日期、校验位验证  
	    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};  
	    var row = true;
	   	
	    if(!code || !/^\d{6}(18|19|20)\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/.test(code)){  
	       row=false,  
	    }else if(!city[code.substr(0,2)]){  
	       row=false,  
	    }else{  
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
	            if(parity[sum % 11] != code[17].toUpperCase()){  
	                row=false,  
	            }  
	        }else{
	        	row = false;
	        }
	    }  
	    //alert(msg);
	    return row;  
	}  
	 

</script>
</html>