//待领取页面
$(function () {
    initTable();
    initClick();
    initHiden();
    todoBtnClick();//点击办理按钮
});
var currTabContext="";
function initClick(){
	 $('#myTab').on('shown.bs.tab', function (e) {
        // 获取已激活的标签页的名称
        var activeTab = $(e.target).context.hash;
        currTabContext=activeTab;
    });

}
//TODO点击办理按钮
function todoBtnClick(){
	$('#todoBtn').click(function(){
		$('#todoform').submit();
	});
}
var countdown=3;
function doQuery(val){
	if(countdown==3){
		if(currTabContext=="#claim"){
			$('#claim-table').bootstrapTable('refresh');    //待领取刷新表格
		}else{
			$('#todo-table').bootstrapTable('refresh');    //待办刷新表格			
		}
	}
	if (countdown == 0) { 
		val.removeAttribute("disabled"); 
		val.value="查询"; 
	 } else {
		val.setAttribute("disabled", true); 
		val.value="查询(" + countdown + ")"; 
		countdown--; 
	 } 
	clearTimeout();
	setTimeout(function(){
		if(countdown==0){
			val.removeAttribute("disabled"); 
			val.value="查询"; 
			countdown=3;
			return false;
		}else{
			doQuery(val);
		}
	},1000);
}

function initHiden(){
	//$('#demo-table').bootstrapTable('hideColumn', 'status');
}
function initTable(){
	claimTask();//待领取任务
	todoTask();//待办任务
    
}
//TODO待领取任务
function claimTask(){
	var url = ctx+"/work/getUnclaimedTaskByPage.do";
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
            field : 'taskname',
            title : '任务名称',
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
            align : 'center',
        }, {
            field : 'productcode',
            title : '产品编码',
            align : 'center',
            valign : 'left'
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
//            	var e = '<a href="#" mce_href="#" onclick="cliemTaskClick(\''+ row.taskid + '\')">领取任务</a> ';  
            	var e = '<button class="btn btn-primary" onclick="cliemTaskClick(\''+ row.taskid + '\')">领取任务</button> ';  
              return e;  
          } 
        }]
    });
}
//TODO 待办任务
function todoTask(){
	var url = ctx+"/work/getTodoTaskByPage.do";
    var table = $('#todo-table').bootstrapTable({
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
        queryParams : queryParamsTodo,
        minimumCountColumns:2,
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        responseHandler:responseHandlerTodo,
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        columns: [
       {
            field : 'taskname',
            title : '任务名称',
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
            align : 'center',
        }, {
            field : 'productcode',
            title : '产品编码',
            align : 'center',
            valign : 'left'
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
        },{
            title: '操作',
            field: 'option',
            align: 'center',
            formatter:function(value,row,index){  
//            	var e = '<a href="#" mce_href="#" onclick="todoTaskClick('+row.appno+','+row.taskid+')">办理</a> ';  
            	var e = '<button class="btn btn-primary" onclick="todoTaskClick(\''+row.appno+"\',\'"+row.taskid + '\')">办理</button> '; 
              return e;  
          } 
        }]
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
        taskdefkey : 'imageCheck',
        idcard : idcard
    }
    return param;
}

function queryParamsTodo(params) {
	var appno = $("#appno").val();
	var customername = $("#customername").val();
	var idcard = $("#idcard").val();
	var currPage = this.pageNumber;
    var param = {
   		limit: params.limit,   //页面大小
        offset: currPage,  //页码params.offset
        appno : appno,
        customername : customername,
        taskdefkey : 'imageCheck',
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

function responseHandlerTodo(res) { 
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

function cliemTaskClick(taskid){
	$.ajax({
		type: "POST",  
        dataType: "json",  
        url:ctx+"/work/cliemTask.do",
        data: {
        	taskid:taskid,
        },                     
        success: function(data) {
        	if(data.status=='succ'){
        		swal({
		            title: "提示",
		            text: '领取成功!',
		            confirmButtonText: "确认"
		        },function(){
		        	$('#todo-table').bootstrapTable('refresh');    
	        	    $('#claim-table').bootstrapTable('refresh'); 
		        });
        	}else{
        		swal({
		            title: "警告",
		            text: '领取失败,请联系管理员!',
		            confirmButtonText: "确认"
		        });
        	}
        	
        }
	});
};

function todoTaskClick(appno,taskid){
	$('#todoAppno').val(appno);
	$('#todoTaskid').val(taskid);
	$('#todoform').submit();
}
