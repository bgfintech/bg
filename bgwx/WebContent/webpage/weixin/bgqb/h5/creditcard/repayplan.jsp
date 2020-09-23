<%@ page language="java" import="java.util.*,java.text.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>还款计划</title>
		<script type="text/javascript" src="plug-in/weixin/merchant/js/jquery-1.11.1.min.js"></script>
        <link href="plug-in/weixin/merchant/css/reset.css?1002" rel="stylesheet" type="text/css" />
        <link href="plug-in/weixin/merchant/css/common.css?10100" rel="stylesheet" type="text/css" />       

    </head>
    <body padding-top:0.08rem;>  
    	<%
    		List<Map<String,Object>> planList = (List<Map<String,Object>>)request.getAttribute("planList");
    		String td = "";
    		for(int i=0;i<planList.size();i++){    			
    			Map<String,Object> m = planList.get(i);
				String transdate = (String)m.get("TRANSDATE");
				if(!td.equals(transdate)){
					if(i>0){
	    			%>
	    			</div>
	    			<%
	    			}
					td = transdate;
					String tstr = transdate.substring(0,4)+"年"+transdate.substring(4, 6)+"月"+transdate.substring(6,8)+"日";
		%>
					<h3 class="pcenter_tit"><%=tstr %></h3>	
					<div class="bank_card">
		<%		
				}
				String status = (String)m.get("STATUS");
				String statusStr = "1".equals(status)?"成功":"2".equals(status)?"失败":"";			
				String tranamt = (String)m.get("TRANAMT");
				double d = Double.parseDouble(tranamt);
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String amount = decimalFormat.format(d/100);
				String runtime = (String)m.get("RUNTIME");
				String time = runtime.substring(8,10)+":"+runtime.substring(10,12);
				String type = (String)m.get("TYPE");
				String typeStr = "01".equals(type)?"转出":"转入";
				%>
				<div class="bank_card_name clearfix">
				    <span class="bank_card_nameL"><%=amount %></span>
				    <span class="bank_card_nameL"><%=time %></span>
				    <span class="bank_card_nameL"><%=typeStr %></span>
				    <span class="bank_card_nameR1"><%=statusStr %></span>
				    <!--  <i class="correct"></i>-->
				 </div>
				<%
				if(i==planList.size()){
    			%>
    			</div>
    			<%
    			}
			}    	
    	 %>	
		<!--<div class="quit">	
		 	<input type="button" onclick="bind();" class="quit_btn confirBtn" value="确定">
		</div>
		-->
	</body>

</html>


