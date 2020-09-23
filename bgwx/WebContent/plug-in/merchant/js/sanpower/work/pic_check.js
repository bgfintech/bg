$(function(){
			
		 /*   $("#jq22").viewer({});*/
			appno=$('#imgAppNo').val();
			taskid=$('#imgTaskId').val();
	    	getBaseApplyData(appno);
	    	getPicData(appno);
	    	gobackBtnClick();
	    	reviewHisBtnClick();
	    	xiaoshiBtnClick();//点击小视
	    	riskBtnClick();//点击风险提示信息
	    	tdBtnClick();//点击同盾
	    	r360BtnClick();//点击融360
	    	xiaoshiBtnClick();//点击小视
	    	pengyuanBtnClick();//点击鹏元
	    	realnameBtnClick();//点击实名认证
	    	idcardBtnClick();//点击银行卡认证
	    	operatorsBtnClick();//点击运营商认证
	    	realnamepictureBtnClick();//点击实名认证图片查询
	    	$("#rejectBtn").click(function(){
	    		reject();
	    	});
	    	$("#passBtn").click(function(){
	    		passBtnClick();
	    		});
	    	$("#patchBtn").click(function(){
	    		addData();
	    	});
	    	$("#consoleBtn").click(function(){
	    		console();
	    	});
	    	var viewer = new Viewer(document.getElementById('jq22'),{
	    		navbar:false
	    	}); 
});
//TODO获取基本申请信息
function getBaseApplyData(appno){
	
	$.ajax({
        url: ctx+"/picCheck/showBaseData.do",
       type: "post",
       data:{appno:appno},
       dataType: "json",
       success: function (data) {
    	   
    	   $("#appno").val(data["order"].appno);
    	   $("#customername").val(data["order"].customername);
    	   $("#idcard").val(data["order"].idcard);
    	   $("#age").val(data["order"].age);
    	   $("#sex").val(data["order"].sex==0?"男":"女");
    	   $("#signorg").val(data["order"].signorg);
    	   var da = new Date(data["order"].icbegintime);
    	    var year = da.getFullYear();
    	    var month = da.getMonth()+1;
    	    var date = da.getDate();
    	   $("#icbegintime").val([year,month,date].join('-'));
    	   da = new Date(data["order"].icendtime);
    	   year = da.getFullYear();
    	   month = da.getMonth()+1;
    	   date = da.getDate();
    	   $("#icendtime").val([year,month,date].join('-'));
    	   $("#bankcardno").val(data["order"].bankcardno);
    	   $("#bankname").val(data["order"].bankname);
    	   $("#bankphone").val(data["order"].bankphone);
    	   $("#phone1").val(data["order"].phone);
    	   $("#unitname").val(data["order"].unitname);
    	   $("#unitphone").val(data["order"].unitphone);
    	   $("#bankcode").val(data["order"].bankcode);
    	   
    	   $("#innercode").val(data["order"].innercode);
    	   $("#parentcompany").val(data["merchantExp"].zmerchantName);
    	   $("#soncompany").val(data["merchantExp"].fmerchantName);
    	   $("#merchantname").val(data["merchantExp"].cmerchantName);
    	   $("#opratername").val(data["operatorInfo"].realName);
    	   $("#opraterphone").val(data["operatorInfo"].tel);
    	   $("#merchantaddr").val(data["operatorInfo"].storeAddress);
       }
});
}

//返回
function gobackBtnClick(){
	$('#gobackBtn').click(function(){
		history.go(-1); 
	});
}


function toDetailPage(){
	   href = href + appno;
	   window.location=href;
}


function reject(){
	 var remark = $("#remark").val().replace(/(^\s*)|(\s*$)/g, "");
	if(remark==""){
		swal({
            title: "提示",
            text: "请在备注里填写拒绝原因！",
            confirmButtonText: "确认"
        });
		return;
	}
	swal({
        title: "提示",
        text: '您确认拒绝吗？',
        showCancelButton: true,
        type: "warning",
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        closeOnConfirm: false,
        closeOnCancel: true
    },
    function(isConfirm){
    	if(isConfirm){
    		$.ajax({
    	        url: ctx+"/picCheck/refuse.do",
    	       type: "post",
    	       data:{remark:remark
    	    	   ,appno:appno
    	    	   ,taskid:taskid},
    	       dataType: "json",
    	       success: function (data) {
    	    	   
    	    	   if(data.status=='success'){
    		    		swal({
    			            title: "提示",
    			            text: '拒绝成功!',
    			            confirmButtonText: "确认"
    			        },
    		            function(){
    		            	history.go(-1);
    		            });
    		    	}else{
    		    		swal({
    			            title: "警告",
    			            text: '拒绝失败,请联系管理员!',
    			            confirmButtonText: "确认"
    			        });
    		    	}
    	       }
    		});
    	}else{
    		return;
    	}
    });
	
}

function console(){
	 var remark = $("#remark").val().replace(/(^\s*)|(\s*$)/g, "");
	if(remark==""){
		swal({
           title: "提示",
           text: "请在备注里填写取消原因！",
           confirmButtonText: "确认"
       });
		return;
	}
	swal({
       title: "提示",
       text: '您确认取消订单吗？',
       showCancelButton: true,
       type: "warning",
       confirmButtonText: "确认",
       cancelButtonText: "取消",
       closeOnConfirm: false,
       closeOnCancel: true
   },
   function(isConfirm){
   	if(isConfirm){
   		$.ajax({
   	        url: ctx+"/picCheck/console.do",
   	       type: "post",
   	       data:{remark:remark
   	    	   ,appno:appno
   	    	   ,taskid:taskid},
   	       dataType: "json",
   	       success: function (data) {
   	    	   
   	    	   if(data.status=='success'){
   		    		swal({
   			            title: "提示",
   			            text: '取消成功!',
   			            confirmButtonText: "确认"
   			        },
   		            function(){
   		            	history.go(-1);
   		            });
   		    	}else{
   		    		swal({
   			            title: "警告",
   			            text: '取消失败,请联系管理员!',
   			            confirmButtonText: "确认"
   			        });
   		    	}
   	       }
   		});
   	}else{
   		return;
   	}
   });
	
}

function getPicData(appno){
	 var html = "";
	$.ajax({
        url: ctx+"/picCheck/showPicData.do",
       type: "post",
       async: false,
       data:{appno:appno},
       dataType: "json",
       success: function (data) {
    	   html+="<ul id='jq22'>";
    	   $.each(data,function(i,item){
    		   var picAddress = ""+item;
    		   if(picAddress.indexOf("idNoAImg")>0){
    			   html+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='身份证正面'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>身份证正面</p></li>";
    		   }else if(picAddress.indexOf("idNoBImg")>0){
    			   html+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='身份证反面'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>身份证反面</p></li>";
    		   }else if(picAddress.indexOf("mugShotImg")>0){
    			  html+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='大头照'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>大头照</p></li>";
        	  }else if(picAddress.indexOf("sceneImg")>0){
        		  html+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='现场照片'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>现场照片</p></li>";
        		}else if(picAddress.indexOf("elseImg")>0){
        			html+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='其它'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>其它</p></li>";
	        }
    	   });
    	   html+="</ul>";
       }
});
	 $("#picInfo").html(html);
}
//TODO点击通过按钮
function passBtnClick(){
	var taskid = $('#imgTaskId').val();
	var appno = $('#imgAppNo').val();
	swal({
        title: "提示",
        text: '您确认通过吗？',
        showCancelButton: true,
        type: "warning",
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        closeOnConfirm: false,
        closeOnCancel: true
    }, function(isConfirm){
    	if(isConfirm){
    		$.ajax({
    			type: "POST",  
    		    dataType: "json",  
    		    url:ctx+"/picCheck/todoTask.do",
    		    data: {
    		    	taskid:taskid,
    		    	appno:appno
    		    },                     
    		    success: function(data) {
    		    	
    		    	if(data.status=='success'){
    		    		swal({
    			            title: "提示",
    			            text: '办理成功!',
    			            confirmButtonText: "确认"
    			        },
    		            function(){
    		            	history.go(-1);
    		            });
    		    	}else{
    		    		swal({
    			            title: "警告",
    			            text: '办理失败,请联系管理员!',
    			            confirmButtonText: "确认"
    			        });
    		    	}
    		    }
    		});
    	}
    });
	
}
//补件
function addData(){
	 var remark = $("#remark").val().replace(/(^\s*)|(\s*$)/g, "");
		if(remark==""){
			swal({
	            title: "提示",
	            text: "请在备注里填写补件原因！",
	            confirmButtonText: "确认"
	        });
			return;
		}
	var taskid = $('#imgTaskId').val();
	var remark = $("#remark").val();
	$.ajax({
		type: "POST",  
	    dataType: "json",  
	    url:ctx+"/picCheck/addData.do",
	    data: {
	    	taskid:taskid,
	    	appno:appno,
	    	remark:remark
	    },                     
	    success: function(data) {
	    	
	    	if(data.status=='success'){
	    		swal({
		            title: "提示",
		            text: '补件成功!',
		            confirmButtonText: "确认"
		        },
	            function(){
	            	history.go(-1);
	            });
	    	}else{
	    		
	    		swal({
		            title: "警告",
		            text: '补件失败,请联系管理员!',
		            confirmButtonText: "确认"
		        });
	    	}
	    	
	    }
	});
}

//TODO 点击审批历史
function reviewHisBtnClick(){
	$('#reviewHisBtn').click(function(){
		initreviewHisTable();
		$('#reviewHisDialog').modal('show');
	});
	
}

function initreviewHisTable(){
	var url = ctx+"/work/queryActByAppNo.do";
    var reviewHisTable = $('#reviewHisTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        url:url,
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        showColumns:true,
        queryParams : queryParamsReviewHis,
        responseHandler:responseHandlerReviewHis,
        pagination: false,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 50,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        strictSearch: true,
        clickToSelect: true,                //是否启用点击选中行
        columns: [
		{
		    field : 'taskname',
		    title : '流程环节名称',
		    align : 'left',
		    valign : 'middle',
		    width: '80px'
		}, {
            field : 'assignee',
            title : '办理人',
            align : 'left',
            valign : 'middle',
            width:  '80px'
        }, {
            field : 'reviewstatus',
            title : '审批状态',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'onereason',
            title : '一级拒绝原因',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'tworeason',
            title : '二级拒绝原因',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'threereason',
            title : '三级拒绝原因',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'remark',
            title : '备注',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'createtime',
            title : '创建时间',
            align : 'center',
            valign : 'middle'
        }]
        
    });
}

function responseHandlerReviewHis(res) { 
    if (res) {
    	debugger;
        return {
        	 "total": res.data.rowCount,//总记录数
             "rows": res.data.list   //数据
        };
    } else {
        return {
            "rows" : [],
            "total" : 0
        };
    }
}

function queryParamsReviewHis(params) {
	var appno = $('#imgAppNo').val();
    var param = {
        appno : appno
    }
    return param;
}

//TODO 点击小视
function xiaoshiBtnClick(){
	$('#xiaoshiBtn').click(function(){
		$('#xiaoshiDialog').modal('show');
		var customername = $('input[name="customername"]').val();
		var phone = $('input[name="phone1"]').val();
		var idcard = $('input[name="idcard"]').val();
		var appno = $('#imgAppNo').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/xiaoshi.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard,
		    	phone:phone,
		    	status:'1',
		    	appno:appno
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#xiaoshiIframe').contents().find("#commonData").html(result);
		    }
		});
	});
}
//TODO 点击风险提示信息
function riskBtnClick(){
	$('#riskBtn').click(function(){
		$('#riskDialog').modal('show');
		var appno = $('#imgAppNo').val();
		$.ajax({
			type: "get",   
		    url:ctx+"/riskTopInfo/riskTipIndex.do",
		    data: {
		    	appNo:appno
		    },                     
		    success: function(data) {
		    	$('#riskcontent').html(data);
		    },
		    error:function(data){
		    	alert("fail");
		    }
		});
		
	});
}
//TODO 银行卡认证
function idcardBtnClick(){
	$('#idcardBtn').click(function(){
		
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var bankcode = $('input[name="bankcode"]').val();
		var bankcardno = $('input[name="bankcardno"]').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/bankCard.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard,
		    	bankcode:bankcode,
		    	bankcardno:bankcardno
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#idcardResult').html(result);
		    }
		});
		$('#idcardDialog').modal('show');
	});
}
//TODO 运营商认证
function operatorsBtnClick(){
	$('#operatorsBtn').click(function(){
		var appno = $('#imgAppNo').val();
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var phone = $('input[name="phone1"]').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/yys.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard,
		    	appno:appno,
		    	status:'1',
		    	phone:phone
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#operatorsResult').html(result);
		    	
		    }
		});
		$('#operatorsDialog').modal('show');
	});
}
//TODO 点击实名认证
function realnameBtnClick(){
	$('#realnameBtn').click(function(){
		
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/realName.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#realnameResult').html(result);
		    }
		});
		$('#realnameDialog').modal('show');
	});
}

//TODO 点击鹏元
function pengyuanBtnClick(){
	$('#pengyuanBtn').click(function(){
		var appno = $('#imgAppNo').val();
		var phone = $('input[name="phone1"]').val();
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var bankcardno = $('input[name="bankcardno"]').val();
		var bankphone = $('input[name="bankphone"]').val();
		var unitname = $('input[name="unitname"]').val();
		var unitphone = $('input[name="unitphone"]').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/py.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard,
		    	phone:phone,
		    	appno:appno,
		    	bankcardno:bankcardno,
		    	bankphone:bankphone,
		    	unitname:unitname,
		    	status:'1',
		    	unitphone:unitphone
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	var status = data.status;
		    	if(status=="succ"){
		    		$('#pengyuanIframe').contents().find("#commonData").html(result);
		    	}else{
		    		$('#pengyuanIframe').contents().find("#commonData").html(data.msg);
		    	}
		    	
		    }
		});
		$('#pengyuanDialog').modal('show');
	});
}



//TODO 点击同盾
function tdBtnClick(){
	$('#tdBtn').click(function(){
		var appno = $('#imgAppNo').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/td.do",
		    dataType: "json", 
		    data: {
		    	status:'1',
		    	appno:appno
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#tdIframe').contents().find("#commonData").html(result);
		    }
		});
		$('#tdDialog').modal('show');
	});
}

//TODO 点击融360
function r360BtnClick(){
	$('#r360Btn').click(function(){
		var phone = $('input[name="phone1"]').val();
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var appno = $('#imgAppNo').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/r360.do",
		    dataType: "json", 
		    data: {
		    	customername:customername,
		    	idcard:idcard,
		    	phone:phone,
		    	status:'1',
		    	appno:appno
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#360Iframe').contents().find("#commonData").html(result);
		    }
		});
		$('#360Dialog').modal('show');
	});
}

//TODO 点击实名认证图片
function realnamepictureBtnClick(){
	$('#realnamepictureBtn').click(function(){
		var idcard = $('#idcard').val();
		$.ajax({
			type: "POST",   
		    url:ctx+"/third/realnamepicture.do",
		    async: false,
		    dataType: "json", 
		    data: {
		    	idcard:idcard
		    },                     
		    success: function(data) {
		    	var result = data.result;
		    	$('#realnamepicture').attr("src",result);
		    }
		});
		$('#realnamepictureDialog').modal('show');
	});
}