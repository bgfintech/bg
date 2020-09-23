//待领取页面
$(function () {
    initData();
    initDate();
    initClick();
    initHiden();
    
});

function initClick(){
	
	gobackBtnClick();//点击返回按钮
	saveBtnClick();//点击保存按钮
	passBtnClick();//点击通过按钮
	pathcBtnClick();//补件按钮点击
	//rejectBtnClick();//点击拒绝按钮
	cancleBtnClick();//点击取消按钮
	//---------------------------------
	accordionOrderImgTitleClick();//点击影像资料标题
	orderImgBtnClick();//点击影像资料
	xiaoshiBtnClick();//点击小视
	r360BtnClick();//点击融360
	tdBtnClick();//点击同盾
	contactBtnClick();//新增联系电话
	reviewHisBtnClick();//点击审批历史
	pengyuanBtnClick();//点击鹏元征信查询
	realnameBtnClick();//点击实名认证查询
	idcardBtnClick();//点击银行卡认证查询
	operatorsBtnClick();//运营商认证查询
	riskBtnClick();//点击风险提示
	realnamepictureBtnClick();//点击实名认证图片调查信息
	//----------------------------------
	saveContactBtnClick();//保存联系人按钮
	closeContactBtnClick();//关闭联系人按钮
	isFamChangeClick();//点击是否家庭关系
	
	//拒绝原因（一级二级三级）
	onereasonOnChange();
	tworeasonOnChange();
	rejectReasonSaveBtnClick();
	cancleReasonSaveBtnClick();
}

//TODO 点击小视
function xiaoshiBtnClick(){
	$('#xiaoshiBtn').click(function(){
		$('#xiaoshiDialog').modal('show');
		var customername = $('input[name="customername"]').val();
		var phone = $('input[name="phone"]').val();
		var idcard = $('input[name="idcard"]').val();
		var appno = $('#initAppNo').val();
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
		var appno = $('#initAppNo').val();
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

//TODO 点击实名认证图片
function realnamepictureBtnClick(){
	$('#realnamepictureBtn').click(function(){
		var idcard = $('input[name="idcard"]').val();
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
		var appno = $('#initAppNo').val();
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var phone = $('input[name="phone"]').val();
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
		var appno = $('#initAppNo').val();
		var phone = $('input[name="phone"]').val();
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
	var appno = $('#initAppNo').val();
    var param = {
        appno : appno
    }
    return param;
}

//TODO点击新增联系人
function contactBtnClick(){
	$('#contactBtn').click(function(){
		var isfam = $('#isfam').val();
		if(isfam=='Y'){
			//加载其他联系人关系数据
			$.ajax({
				type: "POST",  
		        dataType: "json",  
		        url:ctx+"/contact/getRelationFam.do",
		        async: false,                    
		        success: function(data) {
		        	var dics = data.dics;
		        	var rlt="";
		        	$.each(dics,function(index,item){
		        		var code = item.code;
		        		var name = item.name;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#crelation').html(rlt);
		        }
			});
		}else{
			//加载其他联系人关系数据
			$.ajax({
				type: "POST",  
		        dataType: "json",  
		        url:ctx+"/contact/getRelationOther.do",
		        async: false,                    
		        success: function(data) {
		        	var dics = data.dics;
		        	var rlt="";
		        	$.each(dics,function(index,item){
		        		var code = item.code;
		        		var name = item.name;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#crelation').html(rlt);
		        }
			});
		}
		$('#contactDialog').modal('show');
	});
}
function isFamChangeClick(){
	$('#isfam').change(function(){
		var isfam = $('#isfam').val();
		if(isfam=='Y'){
			//加载其他联系人关系数据
			$.ajax({
				type: "POST",  
		        dataType: "json",  
		        url:ctx+"/contact/getRelationFam.do",
		        async: false,                    
		        success: function(data) {
		        	var dics = data.dics;
		        	var rlt="";
		        	$.each(dics,function(index,item){
		        		var code = item.code;
		        		var name = item.name;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#crelation').html(rlt);
		        }
			});
		}else{
			//加载其他联系人关系数据
			$.ajax({
				type: "POST",  
		        dataType: "json",  
		        url:ctx+"/contact/getRelationOther.do",
		        async: false,                    
		        success: function(data) {
		        	var dics = data.dics;
		        	var rlt="";
		        	$.each(dics,function(index,item){
		        		var code = item.code;
		        		var name = item.name;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#crelation').html(rlt);
		        }
			});
		}
		
	});
}

function closeContactBtnClick(){
	$('#closeContactBtn').click(function(){
		$('#contactform')[0].reset();
	})
}
//TODO保存联系人
function saveContactBtnClick(){
	$('#saveContactBtn').click(function(){
		//表单验证通过
		if(contactformValidator().form()){
			var appno = $('#initAppNo').val();
			var name = $('#cname').val();
			var relation = $('#crelation').val();
			var phone = $('#cphone').val();
			var cremark = $('#cremark').val();
			var isfam = $('#isfam').val();
			$.ajax({
				type: "POST",  
		        dataType: "json",  
		        url:ctx+"/contact/insert.do",
		        dataType: "json",
		        data: {
			    	appno:appno,
			    	name:name,
			    	phone:phone,
			    	relation:relation,
			    	remark:cremark,
			    	isfam:isfam
			    },
		        success: function(data) {
		        	var status = data.status;
		        	$('#contactform')[0].reset();
		        	if(status=='succ'){
		        		initData();
		        		$('#contactDialog').modal('hide');
		        	}else{
		        		swal({
				            title: "提示",
				            text: '新增联系人失败',
				            confirmButtonText: "确认"}
				        );
		        	}
		        }
			});
		}else{
			//表单验证未通过不操作
		}
		
	})
}

function contactformValidator(){
	return $("#contactform").validate({ 
	    rules: {
	    	name: {
	        required: true,
	        maxlength: 20
	      },
	      phone: {
	        required: true,
	        maxlength:11,
	        isPhone:true
	      },
	      relation:{
	    	  required: true
	      },
	      cremark:{
	    	  maxlength:20 
	      }
	    }
	});
}

function rejectReasonFormValidator(){
	return $("#rejectReasonForm").validate({ 
	    rules: {
	    	onereason: {
	        required: true
	      },
	      rejectDesc: {
	        maxlength:50
	      }
	    }
	});
}

function cancleReasonFormValidator(){
	return $("#cancleReasonForm").validate({ 
	    rules: {
	    	onereasoncancle: {
	        required: true
	      }
	    }
	});
}

//TODO 点击同盾
function tdBtnClick(){
	$('#tdBtn').click(function(){
		var appno = $('#initAppNo').val();
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
		var phone = $('input[name="phone"]').val();
		var idcard = $('input[name="idcard"]').val();
		var customername = $('input[name="customername"]').val();
		var appno = $('#initAppNo').val();
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

//TODO 点击影像资料
function orderImgBtnClick(){
	$('#orderImgBtn').click(function(){
		var appno = $('#initAppNo').val();
		getPicData(appno);
		var viewer = new Viewer(document.getElementById('jq22'),{
    		navbar:false
    	});
		$('#accordionOrderImg').show();
		//$("#orderImgDialog").modal('show');
	});
}

function accordionOrderImgTitleClick(){
	$('#accordionOrderImgTitle').click(function(){
		$('#accordionOrderImg').hide();
	})
}

function getPicData(appno){
	$.ajax({
        url: ctx+"/picCheck/showPicData.do",
       type: "post",
       async: false,
       data:{appno:appno},
       dataType: "json",
       success: function (data) {
    	   var pichtml="";
    	   $.each(data,function(i,item){
    		   var picAddress = ""+item;
    		   if(picAddress.indexOf("idNoAImg")>0){
    			   pichtml+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='身份证正面'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>身份证正面</p></li>";
    		   }else if(picAddress.indexOf("idNoBImg")>0){
    			   pichtml+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='身份证反面'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>身份证反面</p></li>";
    		   }else if(picAddress.indexOf("mugShotImg")>0){
    			   pichtml+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='大头照'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>大头照</p></li>";
        	  }else if(picAddress.indexOf("sceneImg")>0){
        		  pichtml+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='现场照片' ><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>现场照片</p></li>";
        		}else if(picAddress.indexOf("elseImg")>0){
        			 pichtml+="<li><img data-original='"+picAddress+"' src='"+picAddress+"' alt='其它'><p style='font-size:14px;text-align:center;height:20px;line-height:20px;'>其它</p></li>";
	        }
    	   });
    	   $("#jq22").html(pichtml);
       }
});
}

//TODO 点击补件按钮
function pathcBtnClick(){
	$('#pathcBtn').click(function(){
		var ramarkTask = $('#remark_task').val();
		if(ramarkTask==""){
			swal({
	            title: "提示",
	            text: '请填写备注信息',
	            confirmButtonText: "确认"}
	        );
			return;
		}
		swal({
			title:"您确定要办理这条信息吗",  
            text:"请谨慎操作！",  
            showCancelButton:true,  
            confirmButtonColor:"#DD6B55",  
            confirmButtonText:"是的，我要办理！",  
            cancelButtonText:"让我再考虑一下…",  
            closeOnConfirm:false,  
            closeOnCancel:false  
            },  
            function(isConfirm)  
            {  
                if(isConfirm)  
                {  
                	var taskid = $('#initTaskId').val();
            		$.ajax({
            			type: "POST",  
            		    dataType: "json",  
            		    url:ctx+"/work/todoTaskParam.do",
            		    async: false,
            		    data: {
            		    	taskid:taskid,
            		    	remark:$('#remark_task').val(),
            		    	appno:$('#initAppNo').val(),
            		    	taskparamkey:"initialCheck_result",
            		    	taskparam:'addData'
            		    },                     
            		    success: function(data) {
            		    	if(data.status=='succ'){
            		    		swal({
            			            title: "提示",
            			            text: '办理成功!',
            			            confirmButtonText: "确认"},
            			            function(){
            			            	history.go(-1); 
            			            }
            			        );
            		    	}else{
            		    		swal({
            			            title: "警告",
            			            text: data.msg,
            			            confirmButtonText: "确认"
            			        });
            		    	}
            		    	
            		    }
            		});
                }  
                else{  
                    swal({title:"提示",  
                        text:"您取消了办理操作！",  
                        confirmButtonText: "确认"})  
                }  
           }
        )  
		
	});
}

//TODO点击拒绝按钮
function rejectBtnClick(){
	$('#rejectBtn').click(function(){
		$('#rejectDialog').modal('show');
		$.ajax({
			type: "POST",  
		    dataType: "json",  
		    url:ctx+"/work/queryRejectReason.do",
		    async: false,
		    data: {
		    	reasonkind:"Reject",
		    	parentcode:""
		    },                     
		    success: function(data) {
		    	var list = data.list
		    	if(data.status=='succ'){
		    		var rlt='<option value=""></option>';
		        	$.each(list,function(index,item){
		        		var code = item.reasoncode;
		        		var name = item.reasonname;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#onereason').html(rlt);
		        	$('#tworeason').html(rlt);
		        	$('#threereason').html(rlt);
		    	}else{
		    		swal({
			            title: "警告",
			            text: "数据初始化失败",
			            confirmButtonText: "确认"
			        });
		    	}
		    	
		    }
		});
        
	});
}

//TODO点击取消按钮
function cancleBtnClick(){
	$('#cancleBtn').click(function(){
		$('#cancleDialog').modal('show');
		$.ajax({
			type: "POST",  
		    dataType: "json",  
		    url:ctx+"/work/queryRejectReason.do",
		    async: false,
		    data: {
		    	reasonkind:"Cancel",
		    	parentcode:""
		    },                     
		    success: function(data) {
		    	var list = data.list
		    	if(data.status=='succ'){
		    		var rlt='<option value=""></option>';
		        	$.each(list,function(index,item){
		        		var code = item.reasoncode;
		        		var name = item.reasonname;
		        		rlt+='<option value="'+code+'">'+name+'</option>';
		        	});
		        	$('#onereasoncancle').html(rlt);
		    	}else{
		    		swal({
			            title: "警告",
			            text: "数据初始化失败",
			            confirmButtonText: "确认"
			        });
		    	}
		    	
		    }
		});
        
	});
}

function onereasonOnChange(){
	$('#onereason').change(function(){
		var code = $('#onereason').val();
		if(code!=""){
			$.ajax({
				type: "POST",  
			    dataType: "json",  
			    url:ctx+"/work/queryRejectReason.do",
			    async: false,
			    data: {
			    	parentcode:code
			    },                     
			    success: function(data) {
			    	var list = data.list;
			    	if(data.status=='succ'){
			    		var rlt='';
			        	$.each(list,function(index,item){
			        		var code = item.reasoncode;
			        		var name = item.reasonname;
			        		rlt+='<option value="'+code+'">'+name+'</option>';
			        	});
			        	$('#tworeason').html(rlt);
			        	$('#threereason').html("");
			    	}else{
			    		swal({
				            title: "警告",
				            text: "数据初始化失败",
				            confirmButtonText: "确认"
				        });
			    	}
			    	
			    }
			});
		}
	})
	
}

function tworeasonOnChange(){
	$('#tworeason').change(function(){
		var code = $('#tworeason').val();
		if(code!=""){
			$.ajax({
				type: "POST",  
			    dataType: "json",  
			    url:ctx+"/work/queryRejectReason.do",
			    async: false,
			    data: {
			    	parentcode:code
			    },                     
			    success: function(data) {
			    	var list = data.list;
			    	if(data.status=='succ'){
			    		var rlt='';
			        	$.each(list,function(index,item){
			        		var code = item.reasoncode;
			        		var name = item.reasonname;
			        		rlt+='<option value="'+code+'">'+name+'</option>';
			        	});
			        	$('#threereason').html(rlt);
			    	}else{
			    		swal({
				            title: "警告",
				            text: "数据初始化失败",
				            confirmButtonText: "确认"
				        });
			    	}
			    	
			    }
			});
		}
	})
	
}

function rejectReasonSaveBtnClick(){
	$('#rejectReasonSave').click(function(){
		if(rejectReasonFormValidator().form()){
			var taskid = $('#initTaskId').val();
			$.ajax({
				type: "POST",  
			    dataType: "json",  
			    url:ctx+"/work/todoTaskParam.do",
			    async: false,
			    data: {
			    	taskid:taskid,
			    	remark:$('#remark_task').val(),
			    	appno:$('#initAppNo').val(),
			    	taskparamkey:"initialCheck_result",
			    	onereason:$("#onereason").find("option:selected").text(),
			    	tworeason:$("#tworeason").find("option:selected").text(),
			    	threereason:$("#threereason").find("option:selected").text(),
			    	//rejectDesc:$("#rejectDesc").val(),
			    	taskparam:'reject'
			    },                     
			    success: function(data) {
			    	if(data.status=='succ'){
			    		swal({
				            title: "提示",
				            text: '办理成功!',
				            confirmButtonText: "确认"},
				            function(){
				            	history.go(-1); 
				            }
				        );
			    	}else{
			    		swal({
				            title: "警告",
				            text: data.msg,
				            confirmButtonText: "确认"
				        });
			    	}
			    	
			    }
			});
		}
		
	});
}

//TODO取消原因保存
function cancleReasonSaveBtnClick(){
	$('#cancleReasonSave').click(function(){
		if(cancleReasonFormValidator().form()){
			var taskid = $('#initTaskId').val();
			$.ajax({
				type: "POST",  
			    dataType: "json",  
			    url:ctx+"/work/todoTaskParam.do",
			    async: false,
			    data: {
			    	taskid:taskid,
			    	remark:$('#remark_task').val(),
			    	appno:$('#initAppNo').val(),
			    	taskparamkey:"initialCheck_result",
			    	onereason:$("#onereasoncancle").find("option:selected").text(),
			    	taskparam:'console'
			    },                     
			    success: function(data) {
			    	if(data.status=='succ'){
			    		swal({
				            title: "提示",
				            text: '办理成功!',
				            confirmButtonText: "确认"},
				            function(){
				            	history.go(-1); 
				            }
				        );
			    	}else{
			    		swal({
				            title: "警告",
				            text: data.msg,
				            confirmButtonText: "确认"
				        });
			    	}
			    	
			    }
			});
		}
		
	});
}

//TODO点击保存按钮
function saveBtnClick(){
	$('#saveBtn').click(function(){
		//配偶信息
		var spouseQuestionTable = $('#spouseQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(spouseQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		//朋友信息
		var friendQuestionTable = $('#friendQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(friendQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		//亲属信息
		var relateQuestionTable = $('#relateQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(relateQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//办公信息
		var workQuestionTable = $('#workQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(workQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//同事信息
		var colleageQuestionTable = $('#colleageQuestionTable').bootstrapTable('getData');//获取同事信息表格的所有内容行
		$.each(colleageQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//客户信息
		var customQuestionTable = $('#customQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(customQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		var dataArray1 = workQuestionTable.concat(colleageQuestionTable);
		var dataArray2 =  dataArray1.concat(friendQuestionTable);
		var dataArray3 = dataArray2.concat(relateQuestionTable);
		var dataArray4 = dataArray3.concat(spouseQuestionTable);
		var dataArray5 = dataArray4.concat(customQuestionTable);
		var colleageResult = JSON.stringify(dataArray5);
		
		//保存结果
		var appno=$("#initAppNo").val();
		$.ajax({
			type: "POST",  
	        dataType: "json",  
	        url:ctx+"/work/saveReviewAnswer.do",
	        data: {
	        	appno:appno,
	        	answer:colleageResult
	        },                     
	        success: function(data) {
	        	if(data.status=='succ'){
	        		swal({
			            title: "提示",
			            text: '保存成功!',
			            confirmButtonText: "确认"
			        });
	        	}else{
	        		swal({
			            title: "警告",
			            text: '保存失败,请联系管理员!',
			            confirmButtonText: "确认"
			        });
	        	}
	        	
	        }
		});
		
		
	});
}
//TODO点击返回按钮
function gobackBtnClick(){
	$('#gobackBtn').click(function(){
		history.go(-1); 
	});
}

//TODO点击通过按钮
function passBtnClick(){
	$('#passBtn').click(function(){
		//配偶信息
		var spouseQuestionTable = $('#spouseQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(spouseQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		//朋友信息
		var friendQuestionTable = $('#friendQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(friendQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		//亲属信息
		var relateQuestionTable = $('#relateQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(relateQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//办公信息
		var workQuestionTable = $('#workQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(workQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//同事信息
		var colleageQuestionTable = $('#colleageQuestionTable').bootstrapTable('getData');//获取同事信息表格的所有内容行
		$.each(colleageQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		//客户信息
		var customQuestionTable = $('#customQuestionTable').bootstrapTable('getData');//获取办公信息表格的所有内容行
		$.each(customQuestionTable,function(index,item){
			var id = item.id;
			item.answer = $("input:radio[name="+id+"]:checked").val();
			item.remark = $("input[type=text][name="+id+"]").val();
		});
		
		var dataArray1 = workQuestionTable.concat(colleageQuestionTable);
		var dataArray2 =  dataArray1.concat(friendQuestionTable);
		var dataArray3 = dataArray2.concat(relateQuestionTable);
		var dataArray4 = dataArray3.concat(spouseQuestionTable);
		var dataArray5 = dataArray4.concat(customQuestionTable);
		var colleageResult = JSON.stringify(dataArray5);
		if(true==true){
			swal({
				title:"您确定要办理这条信息吗",  
	            text:"请谨慎操作！",  
	            showCancelButton:true,  
	            confirmButtonColor:"#DD6B55",  
	            confirmButtonText:"是的，我要办理！",  
	            cancelButtonText:"让我再考虑一下…",  
	            closeOnConfirm:false,  
	            closeOnCancel:false  
	            },  
	            function(isConfirm)  
	            {  
	                if(isConfirm)  
	                {  
	                	//保存结果
	        			var taskid = $('#initTaskId').val();
	        			$.ajax({
	        				type: "POST",  
	        			    dataType: "json",  
	        			    async: false,
	        			    url:ctx+"/work/todoTask.do",
	        			    data: {
	        			    	taskid:taskid,
	        			    	remark:$('#remark_task').val(),
	        			    	appno:$('#initAppNo').val(),
	        			    	answer:colleageResult,
	        			    	taskparamkey:"initialCheck_result",
	        			    	taskparam:"approve"
	        			    },                     
	        			    success: function(data) {
	        			    	if(data.status=='succ'){
	        			    		swal({
	        				            title: "提示",
	        				            text: '办理成功!',
	        				            confirmButtonText: "确认"},
	        				            function(){
	        				            	history.go(-1);
	        				            }
	        				        );
	        			    	}else{
	        			    		swal({
	        				            title: "警告",
	        				            text: data.msg,
	        				            confirmButtonText: "确认"
	        				        });
	        			    	}
	        			    	
	        			    }
	        			});
	                }  
	                else{  
	                    swal({title:"提示",  
	                        text:"您取消了办理操作！",  
	                        confirmButtonText: "确认"})  
	                }  
	           }
	        ) 
			
		}else{
			swal({
	            title: "警告",
	            text: notPassWarning,
	            confirmButtonText: "确认"
	        });
		}
		
	});
}

function doQuery(params){
   /* $('#todo-table').bootstrapTable('refresh');    //待办刷新表格
    $('#claim-table').bootstrapTable('refresh');    //待领取刷新表格
*/}

function initHiden(){
	//$('#colleageQuestionTable').bootstrapTable('hideColumn', 'questionCode');
}
function initData(){
	initOrderData();
	initCustomQuestionTable();//初始化客户信息
	initspouseQuestionTable();//初始化配偶信息
	initRelateQuestionTable();//初始化亲属信息
	initFriendQuestionTable();//初始化朋友信息
	initColleageQuestionTable();//初始化同事信息
	initWorkQuestionTable();//初始化办公信息
}

function initColleageQuestionTable(){
    var colleageQuestionTable = $('#colleageQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
  		{
  		    field : 'id',
  		    title : '问题编号',
  		    align : 'left',
  		    valign : 'middle',
  		    width: '80px'
  		},
          {
              field : 'typename',
              title : '问题类型',
              align : 'left',
              valign : 'middle',
              width: '150px'
          },
          {
              field : 'groups',
              title : '问题组',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'name',
              title : '问题名称',
              align : 'left',
              valign : 'middle',
              width:  '200px'
          }, {
              field : 'realanswer',
              title : '标准答案',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'answer',
              title : '问题答案',
              align : 'left',
              valign : 'middle'
         }, {
             field : 'remark',
             title : '备注',
             align : 'left',
             valign : 'middle'
         }]
    });
    if(colleageList!=null){
    	$('#colleageQuestionTable').bootstrapTable('load',colleageList); 
        var columnName="typename";
        mergeTable(columnName);
        
        //设置单选按钮被选中(问题答案)
        $.each(canswer,function(index,item){
        	var questionid = item.questionid;
        	var answer = item.answer;
        	var zdybz = item.remark;
        	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
        	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
        });
    }
}
//TODO 初始化办公信息
function initWorkQuestionTable(){
	var workQuestionTable = $('#workQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
		{
		    field : 'id',
		    title : '问题编号',
		    align : 'left',
		    valign : 'middle',
		    width: '80px'
		},
        {
            field : 'typename',
            title : '问题类型',
            align : 'left',
            valign : 'middle',
            width: '150px'
        },
        {
            field : 'groups',
            title : '问题组',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'name',
            title : '问题名称',
            align : 'left',
            valign : 'middle',
            width:  '200px'
        }, {
            field : 'realanswer',
            title : '标准答案',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'answer',
            title : '问题答案',
            align : 'left',
            valign : 'middle'
       }, {
           field : 'remark',
           title : '备注',
           align : 'left',
           valign : 'middle'
       }]
    });
	$('#workQuestionTable').bootstrapTable('load',workList); 
	 var columnName="typename";
	 mergeTableWorkQuestion(columnName);
	 
	//设置单选按钮被选中(问题答案)
	 $.each(canswer,function(index,item){
	    	var questionid = item.questionid;
	    	var answer = item.answer;
	    	var zdybz = item.remark;
	    	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
	    	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
	    });
}

//TODO 初始化亲属信息
function initRelateQuestionTable(){
	var relateQuestionTable = $('#relateQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
  		{
  		    field : 'id',
  		    title : '问题编号',
  		    align : 'left',
  		    valign : 'middle',
  		    width: '80px'
  		},
          {
              field : 'typename',
              title : '问题类型',
              align : 'left',
              valign : 'middle',
              width: '150px'
          },
          {
              field : 'groups',
              title : '问题组',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'name',
              title : '问题名称',
              align : 'left',
              valign : 'middle',
              width:  '200px'
          }, {
              field : 'realanswer',
              title : '标准答案',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'answer',
              title : '问题答案',
              align : 'left',
              valign : 'middle'
         }, {
             field : 'remark',
             title : '备注',
             align : 'left',
             valign : 'middle'
         }]
        
    });
	$('#relateQuestionTable').bootstrapTable('load',relateList); 
	 var columnName="typename";
	 mergeTableRelationQuestion(columnName);
	 
	//设置单选按钮被选中(问题答案)
	 $.each(canswer,function(index,item){
	    	var questionid = item.questionid;
	    	var answer = item.answer;
	    	var zdybz = item.remark;
	    	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
	    	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
	    });
}

//TODO 初始化配偶信息
function initspouseQuestionTable(){
	var spouseQuestionTable = $('#spouseQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
        		{
        		    field : 'id',
        		    title : '问题编号',
        		    align : 'left',
        		    valign : 'middle',
        		    width: '80px'
        		},
                {
                    field : 'typename',
                    title : '问题类型',
                    align : 'left',
                    valign : 'middle',
                    width: '150px'
                },
                {
                    field : 'groups',
                    title : '问题组',
                    align : 'left',
                    valign : 'middle'
                }, {
                    field : 'name',
                    title : '问题名称',
                    align : 'left',
                    valign : 'middle',
                    width:  '200px'
                }, {
                    field : 'realanswer',
                    title : '标准答案',
                    align : 'left',
                    valign : 'middle'
                }, {
                    field : 'answer',
                    title : '问题答案',
                    align : 'left',
                    valign : 'middle'
               }, {
                   field : 'remark',
                   title : '备注',
                   align : 'left',
                   valign : 'middle'
               }]
    });
	
	if(spouseList!=null){
		$('#spouseQuestionTable').bootstrapTable('load',spouseList);
		var columnName="typename";
		 mergeTableSpouseQuestion(columnName);
		 
		//设置单选按钮被选中(问题答案)
		 $.each(canswer,function(index,item){
		    	var questionid = item.questionid;
		    	var answer = item.answer;
		    	var zdybz = item.remark;
		    	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
		    	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
		    });
	}
}

//TODO 初始化朋友信息
function initFriendQuestionTable(){
	var friendQuestionTable = $('#friendQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
  		{
  		    field : 'id',
  		    title : '问题编号',
  		    align : 'left',
  		    valign : 'middle',
  		    width: '80px'
  		},
          {
              field : 'typename',
              title : '问题类型',
              align : 'left',
              valign : 'middle',
              width: '150px'
          },
          {
              field : 'groups',
              title : '问题组',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'name',
              title : '问题名称',
              align : 'left',
              valign : 'middle',
              width:  '200px'
          }, {
              field : 'realanswer',
              title : '标准答案',
              align : 'left',
              valign : 'middle'
          }, {
              field : 'answer',
              title : '问题答案',
              align : 'left',
              valign : 'middle'
         }, {
             field : 'remark',
             title : '备注',
             align : 'left',
             valign : 'middle'
         }]
    });
	if(friendList!=null){
		$('#friendQuestionTable').bootstrapTable('load',friendList); 
		//设置单选按钮被选中(问题答案)
		$.each(canswer,function(index,item){
	    	var questionid = item.questionid;
	    	var answer = item.answer;
	    	var zdybz = item.remark;
	    	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
	    	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
	    });
	}
	
}

//TODO初始化客户信息
function initCustomQuestionTable(){
	var customQuestionTable = $('#customQuestionTable').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        striped: true,//是否显示行间隔色
        singleSelect: false,
        pagination:false,
        columns: [
		{
		    field : 'id',
		    title : '问题编号',
		    align : 'left',
		    valign : 'middle',
		    width: '80px'
		},
        {
            field : 'typename',
            title : '问题类型',
            align : 'left',
            valign : 'middle',
            width: '150px'
        },
        {
            field : 'groups',
            title : '问题组',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'name',
            title : '问题名称',
            align : 'left',
            valign : 'middle',
            width:  '200px'
        }, {
            field : 'realanswer',
            title : '标准答案',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'answer',
            title : '问题答案',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'remark',
            title : '备注',
            align : 'left',
            valign : 'middle'
        }]
    });
	$('#customQuestionTable').bootstrapTable('load',customList); 
	var columnName="typename";
	mergeTableCustomQuestion(columnName);
	
	//设置单选按钮被选中(问题答案)
    $.each(canswer,function(index,item){
    	var questionid = item.questionid;
    	var answer = item.answer;
    	var zdybz = item.remark;
    	$("input:radio[name='"+questionid+"'][value='"+answer+"']").attr('checked','true');
    	$("input[option=zdyremark][type=text][name="+questionid+"]").val(zdybz);
    });
    $('#customQuestionTable').bootstrapTable('mergeCells', {index: 1, field: 'groups', colspan: 1, rowspan: 2});
	$('#customQuestionTable').bootstrapTable('mergeCells', {index: 3, field: 'groups', colspan: 1, rowspan: 4});
	$('#customQuestionTable').bootstrapTable('mergeCells', {index: 7, field: 'groups', colspan: 1, rowspan: 8});
	
}


function mergeTable(field){
    $table=$("#colleageQuestionTable");
    var obj=getObjFromTable($table,field);

     for(var item in obj){  
        $("#colleageQuestionTable").bootstrapTable('mergeCells',{
        index:obj[item].index,
        field:field,
        colspan:1,
        rowspan:obj[item].row,
        });
      }
}

function mergeTableWorkQuestion(field){
    $table=$("#workQuestionTable");
    var obj=getObjFromTable($table,field);

     for(var item in obj){  
        $("#workQuestionTable").bootstrapTable('mergeCells',{
        index:obj[item].index,
        field:field,
        colspan:1,
        rowspan:obj[item].row,
        });
      }
}

function mergeTableRelationQuestion(field){
	$table=$("#relateQuestionTable");
    var obj=getObjFromTable($table,field);

     for(var item in obj){  
        $("#relateQuestionTable").bootstrapTable('mergeCells',{
        index:obj[item].index,
        field:field,
        colspan:1,
        rowspan:obj[item].row,
        });
      }
}

function mergeTableSpouseQuestion(field){
	$table=$("#spouseQuestionTable");
    var obj=getObjFromTable($table,field);

	 for(var item in obj){  
	    $("#spouseQuestionTable").bootstrapTable('mergeCells',{
	    index:obj[item].index,
	    field:field,
	    colspan:1,
	    rowspan:obj[item].row,
	    });
	  }
}

function mergeTableCustomQuestion(field){
	$table=$("#customQuestionTable");
    var obj=getObjFromTable($table,field);

	 for(var item in obj){  
	    $("#customQuestionTable").bootstrapTable('mergeCells',{
	    index:obj[item].index,
	    field:field,
	    colspan:1,
	    rowspan:obj[item].row,
	    });
	  }
}

function initOrderData(){
	var appno=$("#initAppNo").val();
	//初始化表单数据
	$.ajax({
		type: "POST",  
        dataType: "json",  
        url:ctx+"/work/initOrderData.do",
        async: false,
        data: {
        	appno:appno	
        },                     
        success: function(data) {
        	var price2;
        	var commoditytype2 = data.order.commoditytype2;
        	if(commoditytype2==null){
        		price2="";
        	}else{
        		price2 = data.order.price2;
        	}
        	var order = data.order;
        	var productcode = order.productcode;
        	var productname = order.productname;
        	var status = data.status;
        	if(status=='succ'){
        		corder=order;
            	var html = '';
            	$('#customBaseForm').formEdit(order);
            	$('#loanApplyForm').formEdit(order);
            	$('#price2').val(price2);
            	$('#productcode').val(productcode+'-'+productname);
            	var contacts = data.contact;
            	//联系人信息
            	$.each(contacts,function(index,contact){
            		var name=contact.name;
            		var relation = contact.relation;
            		var phone = contact.phone;
            		var remark = contact.remark;
            		if(remark==null){
            			remark="";
            		}
            		html += "<tr class='clearfix'>\n" +
               		"<td>"+name+"</td>\n" +
               		"<td>"+relation+"</td>\n" +
               		"<td>"+phone+"</td>\n" +
               		"<td>"+remark+"</td>\n" +
               		"</tr>";
            		$('#table_list_2').html(html);
            	});
            	
            	/*//门店信息
            	var zmerchant = data.zmerchant;
            	$('input[name="zmerchantName"]').val(zmerchant.zmerchantName);
            	$('input[name="fmerchantName"]').val(zmerchant.fmerchantName);
            	$('input[name="cmerchantName"]').val(zmerchant.cmerchantName);*/
            	//操作员信息
            	var operator = data.operator;
            	$('#storeForm').formEdit(operator);
            	$('#innercode').val(order.innercode);
            	customList = data.customList;
         	    spouseList=data.spouseList;
         	    relateList=data.relateList;
         	    friendList=data.friendList;
         	    workList=data.workList;
         	    colleageList=data.colleageList;
         	    
        	}else{
        		swal({
		            title: "警告",
		            text: '数据初始化失败,请联系管理员!',
		            confirmButtonText: "确认"
		        });
        	}
        	
        }
	});
	
	//查询问题答案
	$.ajax({
		type: "POST",  
        dataType: "json",  
        url:ctx+"/question/queryAnswerByAppNo.do",
        async: false,
        data: {
        	appno:appno	
        },                     
        success: function(data) {
        	var order = data.list;
        	if(order!=null){
        		canswer = order;
        	}
        }
	});
} 


function initDate(){
	/* 日历初始化 */
    $('.time').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true,
        format: "yyyy-mm-dd"
    });
}

function getObjFromTable($table,field){
    var obj=[];
    var maxV=$table.find("th").length;

    var columnIndex=0;
    var filedVar;
    for(columnIndex=0;columnIndex<maxV;columnIndex++){
        filedVar=$table.find("th").eq(columnIndex).attr("data-field");
        if(filedVar==field) break;

    }
    var $trs=$table.find("tbody > tr");
    var $tr;
    var index=0;
    var content="";
    var row=1;
    for (var i = 0; i <$trs.length;i++)
    {   
        $tr=$trs.eq(i);
        var contentItem=$tr.find("td").eq(columnIndex).html();
        //exist
        if(contentItem.length>0 && content==contentItem ){
            row++;
        }else{
            //save
            if(row>1){
                obj.push({"index":index,"row":row});
            }
            index=i;
            content=contentItem;
            row=1;
        }
    }
    if(row>1)obj.push({"index":index,"row":row});
    return obj; 
}

