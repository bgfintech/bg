//待领取页面
$(function () {
    initTable();
    initDate();
    initClick();
    initHiden();
});

var currTabContext="";
function initClick(){
	 $('#myTab').on('shown.bs.tab', function (e) {
         // 获取已激活的标签页的名称
         var activeTab = $(e.target).context.hash;
         currTabContext=activeTab;
     });
}

function doQuery(val){
	$('#claim-table').bootstrapTable('refresh');    //待领取刷新表格
}


function initHiden(){
	$('#claim-table').bootstrapTable('hideColumn', 'storecode');
	$('#claim-table').bootstrapTable('hideColumn', 'taskid');
}
function initTable(){
	claimTask();//待领取任务
    
}
//TODO待领取任务
function claimTask(){
	var url = ctx+"/work/getTaskListByPage.do";
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
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        responseHandler:responseHandlerClaim,
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        columns: [
        {
            field: 'state',
            checkbox: true,
            align: 'center',
            valign: 'middle'
          },
        {
            field : 'taskid',
            title : '任务ID',
            valign : 'middle',
            sortable : true
        }, {
            field : 'taskname',
            title : '任务名称',
            valign : 'middle',
            sortable : true
        }, {
            field : 'taskdefkey',
            title : '任务名称EN',
            valign : 'middle',
            sortable : true
        }, {
            field : 'appno',
            title : '申请件编号',
            valign : 'middle'
        }, {
            field : 'customername',
            title : '申请人姓名',
            valign : 'middle',
        }, {
            field : 'idcard',
            title : '身份证号',
            valign : 'middle',
        }, {
            field : 'productcode',
            title : '产品编码',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'principal',
            title : '贷款本金',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'phone',
            title : '移动电话',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'storecode',
            title : '门店编码',
            align : 'center',
            valign : 'middle'
        }, {
            field : 'storename',
            title : '门店名称',
            valign : 'middle'
        }, {
            field : 'zmerchantname',
            title : '总公司',
            valign : 'middle'
        }, {
            field : 'storerisklevel',
            title : '门店风险等级',
            valign : 'middle'
        }, {
            field : 'createtime',
            title : '开始时间',
            align : 'center',
            valign : 'middle'
        },
        {
            title: '操作',
            field: 'option',
            align: 'center',
            formatter:function(value,row,index){  
            	var e = '<button class="btn btn-primary" onclick="cliemTaskCansoleClick(\''+row.appno+"\',\'"+row.taskid + "\',\'"+row.taskdefkey + '\')">取消任务</button> ';  
          
              return e;  
          } 
        }]
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

function queryParamsClaim(params) {
	var appno = $("#appno").val();
	var customername = $("#customername").val();
	var idcard = $("#idcard").val();
	var currPage = this.pageNumber;
    var param = {
   		limit: params.limit,   //页面大小
        offset: currPage,  //页码params.offset
        appno : appno,
        customername : customername,
        idcard : idcard
    }
    return param;
}

function responseHandlerClaim(res) { 
    if (res) {
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


function cliemTaskCansoleClick(appno,taskid,taskdefkey){
	debugger;
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
        		$.ajax({
        			type: "POST",  
        		    dataType: "json",  
        		    url:ctx+"/work/taskCancel.do",
        		    async: false,
        		    data: {
        		    	taskid:taskid,
        		    	appno:appno,
        		    	taskparamkey:taskdefkey,
        		    	taskparam:'console'
        		    },                     
        		    success: function(data) {
        		    	$('#claim-table').bootstrapTable('refresh');    //待领取刷新表格
        		    	if(data.status=='succ'){
        		    		swal({
        			            title: "提示",
        			            text: '办理成功!',
        			            confirmButtonText: "确认"}
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
};
