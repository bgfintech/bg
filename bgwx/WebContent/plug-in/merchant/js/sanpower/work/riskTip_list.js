//待领取页面
$(function() {
	initTable();
});


function initTable() {
	var tableContent=$("#riskTipInfo");
	$.ajax({

		type:"POST",
		async:false,
		url:ctx+"/riskTopInfo/getRiskTipInfo.do",
		data:{appNo:appNo},
		dataType:"json",
		success:function(result){
			var tdHtml="";
			var riskTipInfoList = result.riskTipPhone;
			//同一电话对应不同联系人             01
			var spousePhone= "";
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
					
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">同一电话对应不同联系人</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
						
					}else{
						
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
					
				}
			}
			//同一身份证号多个合同申请         02
			riskTipInfoList= result.riskTipIdcard;
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">同一身份证号多个合同申请</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
						
					}else{
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
				}
			}
			//此申请单的联系人目前处于逾期  03
			riskTipInfoList = result.riskTipCustomer;
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">此申请单的联系人目前处于逾期</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
					}else{
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
				}
			}
			//此申请人还款6期内重复申请       04
			riskTipInfoList = result.riskTipRepeatOn;	
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">此申请人还款6期内重复申请</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
						
					}else{
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
				}
			}
			//此申请人还款6期后重复申请       05
			riskTipInfoList = result.riskTipRepeatOut;	
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">此申请人还款6期后重复申请</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
						
					}else{
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
				}
			}
			//联系人作为申请人申请进件          06
			riskTipInfoList = result.riskTipInto;
			if(riskTipInfoList!=null &&riskTipInfoList!=undefined && riskTipInfoList!=""&&riskTipInfoList.length>1){
				for ( var i in riskTipInfoList) {
					if(i==0){
						tdHtml+="<tr class=\"clearfix\"><td rowspan="+riskTipInfoList.length+" style=\"text-align: center; vertical-align: middle;\">联系人作为申请人申请进件</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appNo+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spouseName+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].spousePhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyRelate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].familyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].connectedPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyName+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].companyPhone+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].appStatus+"</td>" +
						"<td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject1+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].reject2+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDate+"</td><td style=\"background-color:#BEBEBE;\">"+riskTipInfoList[i].overdueDays+"</td></tr>"					
						
					}else{
						tdHtml+="<tr class=\"clearfix\"><td>"+riskTipInfoList[i].appNo+"</td><td>"+riskTipInfoList[i].customerName+"</td><td>"+riskTipInfoList[i].customerPhone+"</td><td>"+riskTipInfoList[i].spouseName+"</td>" +
						"<td>"+riskTipInfoList[i].spousePhone+"</td><td>"+riskTipInfoList[i].familyName+"</td><td>"+riskTipInfoList[i].familyRelate+"</td><td>"+riskTipInfoList[i].familyPhone+"</td><td>"+riskTipInfoList[i].customerRelation+"</td>" +
						"<td>"+riskTipInfoList[i].connectedName+"</td><td>"+riskTipInfoList[i].connectedPhone+"</td><td>"+riskTipInfoList[i].companyName+"</td><td>"+riskTipInfoList[i].companyPhone+"</td><td>"+riskTipInfoList[i].appStatus+"</td>" +
						"<td>"+riskTipInfoList[i].reject1+"</td><td>"+riskTipInfoList[i].reject2+"</td><td>"+riskTipInfoList[i].overdueDate+"</td><td>"+riskTipInfoList[i].overdueDays+"</td></tr>"
					}
				}
			}
			tableContent.append(tdHtml);
		},
		error:function(result){
			alert("sorry,没查到数据");
		}
	});
}
