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
		var n = $("#name").val();	
        var data = JSON.parse(n);      
                if(data.resp_code == "00"){
                	var html="";
                	var detail = "";
                	
                	var preservation = data.preservation;
                	if(preservation!=null&&preservation!=''){
                		html += countDetail2html(preservation.count,"非诉财产保全-统计");
                		if(preservation.cases!=null&&preservation.cases!=''){
                			detail+=pre2html(preservation.cases,"非诉财产保全-详情");
                		}
                	}
                	var civil = data.civil;                	
                	if(civil!=null&&civil!=''){
                		html += countDetail2html(civil.count,"民事-统计");
                		if(civil.cases!=null&&civil.cases!=''){
                			detail+=case2html(civil.cases,"民事-详情");
                		}
                	}
                	var criminal = data.criminal;
                	if(criminal!=null&&criminal!=''){
                		html += countDetail2html(criminal.count,"刑事-统计");
                		if(criminal.cases!=null&&criminal.cases!=''){
                			detail+=cri2html(criminal.cases,"刑事-详情");
                		}
                	}               	             	
                	var administrative = data.administrative;
                	if(administrative!=null&&administrative!=''){
                		html += countDetail2html(administrative.count,"行政-统计");
                		if(administrative.cases!=null&&administrative.cases!=''){
                			detail+=case2html(administrative.cases,"行政-详情");
                		}
                	}
                	var bankrupt = data.bankrupt;
                	if(bankrupt!=null&&bankrupt!=''){
                		html += countDetail2html(bankrupt.count,"强制清算-统计");
                		if(bankrupt.cases!=null&&bankrupt.cases!=''){
                			detail+=ban2html(bankrupt.cases);
                		}
                	}
                	var implement = data.implement;
                	if(implement!=null&&implement!=''){
                		html += countDetail2html(implement.count,"执行-统计");
                		if(implement.cases!=null&&implement.cases!=''){
                			detail+=imp2html(implement.cases);
                		}
                	}               	
                	var caseTree = data.cases_tree;
                	if(caseTree!=null){
                		html+=caseTree2html(caseTree);
                	}               	
                	html += detail;
                	$("#content").html(html);
                }
	}
	function caseTree2html(ct){
		var html="<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>案件串联概要</th></tr></thead>"		
			+ "<tbody ><tr class='clearfix'><td style='width: 130px'>案件类型</td><td style='width: 50px'>序号</td><td>案号</td></tr>";
		
		if(ct.civil!=null){
			html+=tree2html(ct.civil,"民事");
		}
		if(ct.criminal!=null){
			html+=tree2html(ct.criminal,"刑事");
		}	
		if(ct.preservation!=null){
			html+=tree2html(ct.preservation,"非诉财产保全");
		}			
		if(ct.administrative!=null){
			html+=tree2html(ct.administrative,"行政");
		}			
		if(ct.bankrupt!=null){
			html+=tree2html(ct.bankrupt,"强制清算");
		}
		if(ct.implement!=null){
			html+=tree2html(ct.implement,"执行");
		}
		html += "</tbody></table>";
		return html;
	}
	
	
	function caseT2html(ct){
		var html = ""+ct.c_ah+"["+ct.stage_type+"]";
		if(ct.next!=null){
			html+="&nbsp; &nbsp;--&nbsp;"+caseT2html(ct.next);
		}
		return html;
	}
	
	
	function tree2html(cts,n){		
		var s = 0;
		var h = "";
		var h1 = "";
		$(cts).each(function(){
			s=s+1;
			if(s==1){
				h1+="<td>"+s+"</td><td style='text-align:left'>"+caseT2html(this)+"</td>";
			}else{
				h+="<tr class='clearfix'><td>"+s+"</td><td style='text-align:left'>"+caseT2html(this)+"</td></tr>";
			}
		});	
		var html = "<tr class='clearfix'><td style='width: 130px' rowspan='"+s+"'>"+n+"</td>"+h1+"</tr>"+h;
		return html;
	}
	
	function ban2html(ba){
		var html = "<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>破产-详情</th></tr></thead>"		
			+ "<tbody id='table_list_2'>";
		$(ba).each(function(){	
			html+="<tr class='clearfix'><td style='width: 130px'>案号</td><td style='width: 130px'>案件类型</td><td>经办法院</td><td>案件阶段</td><td>立案日期</td><td>结案日期</td><td>结案方式</td><td>诉讼地位</td></tr>"
			+ "<tr class='clearfix'><td rowspan='7'>"+n(this.c_ah)+"</td>"
			+ "<td>"+n(this.n_ajlx)+"</td>"
			+ "<td>"+n(this.n_jbfy)+"</td>"
			+ "<td>"+n(this.n_ajjzjd)+"</td>"
			+ "<td>"+n(this.d_larq)+"</td>"
			+ "<td>"+n(this.d_jarq)+"</td>"
			+ "<td>"+n(this.n_jafs)+"</td>"
			+ "<td>"+n(this.n_ssdw)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>相关案号</td><td colspan='6'>"+n(this.c_gkws_glah)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>当事人</td><td colspan='6'>"+n(this.c_gkws_dsr)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>判决结果</td><td colspan='6'>"+n(this.c_gkws_pjjg)+"</td></tr>";
		});	
		html += "</tbody></table>";
		return html;
	}
	
	function imp2html(imp){
		var html = "<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>执行-详情</th></tr></thead>"		
				+ "<tbody id='table_list_2'>";
		$(imp).each(function(){	
			html+="<tr class='clearfix'><td style='width: 130px'>案号</td><td style='width: 130px'>案件类型</td><td>原审案号</td><td>后续案号</td><td>经办法院</td><td>案件阶段</td><td>立案日期</td><td>结案日期</td><td>案由</td><td>申请执行金额</td><td>结案金额</td><td>实际到位金额</td><td>结案方式</td><td>诉讼地位</td></tr>	"
			+ "<tr class='clearfix'><td rowspan='7'>"+n(this.c_ah)+"</td>"
			+ "<td>"+n(this.n_ajlx)+"</td>"
			+ "<td>"+n(this.c_ah_ys)+"</td>"
			+ "<td>"+n(this.c_ah_hx)+"</td>"
			+ "<td>"+n(this.n_jbfy)+"</td>"
			+ "<td>"+n(this.n_ajjzjd)+"</td>"
			+ "<td>"+n(this.d_larq)+"</td>"
			+ "<td>"+n(this.d_jarq)+"</td>"
			+ "<td>"+n(this.n_laay)+"</td>"
			+ "<td>"+n(this.n_sqzxbdje)+"</td>"
			+ "<td>"+n(this.n_jabdje)+"</td>"
			+ "<td>"+n(this.n_sjdwje)+"</td>"
			+ "<td>"+n(this.n_jafs)+"</td>"
			+ "<td>"+n(this.n_ssdw)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>相关案号</td><td colspan='12'>"+n(this.c_gkws_glah)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>当事人</td><td colspan='12'>"+n(this.c_gkws_dsr)+"</td></tr>"
			+ "<tr></tr><tr class='clearfix'><td>判决结果</td><td colspan='12'>"+n(this.c_gkws_pjjg)+"</td></tr>";
		});	
		html += "</tbody></table>";
		return html;
	}	
	
	function pre2html(pre,str){
		var html = "<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>"+str+"</th></tr></thead>"
		+	"<tbody id='table_list_2'>";
		$(pre).each(function(){
			html+="<tr class='clearfix'><td style='width: 130px'>案号</td><td style='width: 130px'>原审案号</td><td>后续案号</td><td>经办法院</td><td>案件阶段</td><td>立案日期</td><td>结案日期</td><td>申请保全金额</td><td>保全标的物</td><td>结案方式</td><td>诉讼地位</td></tr>"
				+ "<tr class='clearfix'><td rowspan='7'>"+n(this.c_ah)+"</td>"
				+ "<td>"+n(this.c_ah_ys)+"</td>"
				+ "<td>"+n(this.c_ah_hx)+"</td>"
				+ "<td>"+n(this.n_jbfy)+"</td>"
				+ "<td>"+n(this.n_ajjzjd)+"</td>"
				+ "<td>"+n(this.d_larq)+"</td>"
				+ "<td>"+n(this.d_jarq)+"</td>"
				+ "<td>"+n(this.n_sqbqse)+"</td>"
				+ "<td>"+n(this.c_sqbqbdw)+"</td>"
				+ "<td>"+n(this.n_jafs)+"</td>"
				+ "<td>"+n(this.n_ssdw)+"</td>"
				+ "</tr><tr></tr><tr class='clearfix'><td>相关案号</td><td colspan='9'>"+n(this.c_gkws_glah)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>当事人</td><td colspan='9'>"+n(this.c_gkws_dsr)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>判决结果</td><td colspan='9'>"+n(this.c_gkws_pjjg)+"</td></tr>";
		});
		html += "</tbody></table>";
		return html;
	}
	
	function cri2html(cri,str){
		var html = "<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>"+str+"</th></tr></thead>"
				+	"<tbody id='table_list_2'>";
		$(cri).each(function(){
			html+="<tr class='clearfix'><td style='width: 130px'>案号</td><td style='width: 130px'>原审案号</td><td>后续案号</td><td>经办法院</td><td>案件阶段</td><td>审理程序</td><td>立案日期</td><td>结案日期</td><td>案由</td><td>犯罪金额</td><td>请求赔偿金额</td><td>财产刑执金额</td><td>判处赔偿金额</td><td>判处结果</td><td>诉讼地位</td></tr>"
				+ "<tr class='clearfix'><td rowspan='9'>"+n(this.c_ah)+"</td>"
				+ "<td>"+n(this.c_ah_ys)+"</td>"
				+ "<td>"+n(this.c_ah_hx)+"</td>"
				+ "<td>"+n(this.n_jbfy)+"</td>"
				+ "<td>"+n(this.n_ajjzjd)+"</td>"
				+ "<td>"+n(this.n_slcx)+"</td>"
				+ "<td>"+n(this.d_larq)+"</td>"
				+ "<td>"+n(this.d_jarq)+"</td>"
				+ "<td>"+n(this.n_laay)+"</td>"
				+ "<td>"+n(this.n_fzje)+"</td>"
				+ "<td>"+n(this.n_bqqpcje)+"</td>"
				+ "<td>"+n(this.n_ccxzxje)+"</td>"
				+ "<td>"+n(this.n_pcpcje)+"</td>"
				+ "<td>"+n(this.n_pcjg)+"</td>"
				+ "<td>"+n(this.n_ssdw)+"</td>"
				+ "</tr>"
				+ "<tr></tr><tr class='clearfix'><td>审理相关信息</td><td colspan='13'>"+n(this.c_slfsxx)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>相关案号</td><td colspan='13'>"+n(this.c_gkws_glah)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>当事人</td><td colspan='13'>"+n(this.c_gkws_dsr)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>判决结果</td><td colspan='13'>"+n(this.c_gkws_pjjg)+"</td></tr>";
		});
		html += "</tbody></table>";
		return html;		
	}
	
	function case2html(cs,str){
		var html = "<table id='table' class='table table-striped'><thead class='clearfix'><tr><th colspan='2' align='left'>"+str+"</th></tr></thead>"
				+	"<tbody id='table_list_2'>";
		$(cs).each(function(){
			html+="<tr class='clearfix'><td style='width: 130px'>案号</td><td style='width: 130px'>原审案号</td><td>后续案号</td><td>经办法院</td><td>案件阶段</td><td>审理程序</td><td>立案日期</td><td>结案日期</td><td>案由</td><td>起诉金额</td><td>结案金额</td><td>结案方式</td><td>诉讼地位</td><td>一审诉讼地位</td></tr>"
				+ "<tr class='clearfix'><td rowspan='9'>"+n(this.c_ah)+"</td>"
				+ "<td>"+n(this.c_ah_ys)+"</td>"
				+ "<td>"+n(this.c_ah_hx)+"</td>"
				+ "<td>"+n(this.n_jbfy)+"</td>"
				+ "<td>"+n(this.n_ajjzjd)+"</td>"
				+ "<td>"+n(this.n_slcx)+"</td>"
				+ "<td>"+n(this.d_larq)+"</td>"
				+ "<td>"+n(this.d_jarq)+"</td>"
				+ "<td>"+n(this.n_laay)+"</td>"
				+ "<td>"+n(this.n_qsbdje)+"</td>"
				+ "<td>"+n(this.n_jabdje)+"</td>"
				+ "<td>"+n(this.n_jafs)+"</td>"
				+ "<td>"+n(this.n_ssdw)+"</td>"
				+ "<td>"+n(this.n_ssdw_ys)+"</td>"
				+ "</tr>"
				+ "<tr></tr><tr class='clearfix'><td>审理相关信息</td><td colspan='12'>"+n(this.c_slfsxx)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>相关案号</td><td colspan='12'>"+n(this.c_gkws_glah)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>当事人</td><td colspan='12'>"+n(this.c_gkws_dsr)+"</td></tr>"
				+ "<tr></tr><tr class='clearfix'><td>判决结果</td><td colspan='12'>"+n(this.c_gkws_pjjg)+"</td></tr>";
		});
		html += "</tbody></table>";
		return html;		
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
			  +   "<td>"+n(cd.money_other)+"["+n(cd.money_wei_other)+"]</td>"
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
	    var msg = "验证成功";
	   	
	    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/.test(code)){  
	       row=false,  
	       msg = "被保人身份证号格式错误"; 
	    }else if(!city[code.substr(0,2)]){  
	       row=false,  
	       msg = "被保人身份证号地址编码错误";
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
	       			msg = "被保人身份证号校验位错误";
	            }  
	        }  
	    }  
	    //alert(msg);
	    return row;  
	}  
	
	function tt(){
		var t = "";
		
		
		
		
		var html="";
    	var civil = data.civil;
    	var detail = "";
    	if(civil!=null&&civil!=''){
    		html += countDetail2html(civil.count,"民事-统计");
    		if(civil.cases!=null&&civil.cases!=''){
    			detail+=case2html(civil.cases,"民事-详情");
    		}
    	}
    	var implement = data.implement;
    	if(implement!=null&&implement!=''){
    		html += countDetail2html(implement.count,"执行-统计");
    		if(implement.cases!=null&&implement.cases!=''){
    			detail+=imp2html(implement.cases);
    		}
    	}
    	var criminal = data.criminal;
    	if(criminal!=null&&criminal!=''){
    		html += countDetail2html(criminal.count,"刑事-统计");
    		if(criminal.cases!=null&&criminal.cases!=''){
    			detail+=case2html(criminal.cases,"刑事-详情");
    		}
    	}
    	var administrative = data.administrative;
    	if(administrative!=null&&administrative!=''){
    		html += countDetail2html(administrative.count,"行政-统计");
    		if(administrative.cases!=null&&administrative.cases!=''){
    			detail+=case2html(administrative.cases,"行政-详情");
    		}
    	}
    	var bankrupt = data.bankrupt;
    	if(bankrupt!=null&&bankrupt!=''){
    		html += countDetail2html(bankrupt.count,"强制清算-统计");
    		if(bankrupt.cases!=null&&bankrupt.cases!=''){
    			detail+=ban2html(bankrupt.cases);
    		}
    	}
    	var caseTree = data.cases_tree;
    	if(caseTree!=null){
    		html+=caseTree2html(caseTree);
    	}               	
    	html += detail;
    	$("#content").html(html);
	}
	 

</script>
</html>