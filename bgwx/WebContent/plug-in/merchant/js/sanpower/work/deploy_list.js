$(function () {
	initdata();
    initTable();
    initDate();
    initClick();
    initHiden(); 
});

function initdata(){
	$("#deployfile").fileinput({
        uploadUrl:null,//上传的地址
        uploadAsync: true,
        language : "zh",//设置语言
        showCaption: true,//是否显示标题
        showUpload: true, //是否显示上传按钮
        browseClass: "btn btn-primary", //按钮样式 
        allowedFileExtensions: ["zip"], //接收的文件后缀
        maxFileCount: 1,//最大上传文件数限制
        uploadAsync: true,
        previewFileIcon: '<i class="glyphicon glyphicon-file"></i>',
        allowedPreviewTypes: null, 
        previewFileIconSettings: {
            'docx': '<i class="glyphicon glyphicon-file"></i>',
            'xlsx': '<i class="glyphicon glyphicon-file"></i>',
            'pptx': '<i class="glyphicon glyphicon-file"></i>',
            'jpg': '<i class="glyphicon glyphicon-picture"></i>',
            'pdf': '<i class="glyphicon glyphicon-file"></i>',
            'zip': '<i class="glyphicon glyphicon-file"></i>'
        }
    });
}

function initClick(){
	//主界面-提前还款预约按钮
	$("#deployBtn").click(function(){
		$("#deplayDialog").modal('show');
	});
	
	$('#closeBtn').click(function(){
		$("#deplayDialog").modal('hide');
	});
	
	$('#uploadDeploy').click(function(){
		var filename = $('#filename').val();
		var deployfile = $("#deployfile").val();
		if(filename==""){
			swal({
	            title: "警告",
	            text: "流程名称不能为空!",
	            confirmButtonText: "确认"
	        });
			return;
		}
		if(deployfile==""){
			swal({
	            title: "警告",
	            text: "请上传流程文件",
	            confirmButtonText: "确认"
	        });
			return
		}
		$('#uploadDeployForm').submit();
	});
	
	
}	
	

function doQuery(params){
    $('#demo-table').bootstrapTable('refresh');    //刷新表格
}

function initHiden(){
	//$('#demo-table').bootstrapTable('hideColumn', 'status');
}
function initTable(){
    var url = ctx+"/flow/initDeploy.do";
    var table = $('#demo-table').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        striped: true,//是否显示行间隔色
        singleSelect: false,
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        url:url,
        showColumns:true,
        pagination:true,
        queryParams : queryParams,
        minimumCountColumns:2,
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        uniqueId: "loanno",                     //每一行的唯一标识，一般为主键列
        showExport: true,                    
        exportDataType: 'all',
        responseHandler:responseHandler,
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        columns: [
        {
            field: 'state',
            checkbox: true,
            align: 'center',
            valign: 'middle'
          },{
            field: '',
                    title: '序号',
                    formatter: function (value, row, index) {
                    return index+1;
             }
        },
        {
            field : 'defid',
            title : '流程定义ID',
            align : 'left',
            valign : 'middle',
            sortable : true
        }, {
            field : 'defname',
            title : '流程定义名称',
            align : 'left',
            valign : 'middle',
            sortable : true
        }, {
            field : 'defkey',
            title : '流程定义key',
            align : 'left',
            valign : 'middle'
        }, {
            field : 'defversion',
            title : '流程定义版本',
            align : 'left',
            valign : 'middle',
            sortable : true
        }, {
            field : 'deployid',
            title : '流程部署ID',
            align : 'left',
            valign : 'middle',
            sortable : true
        }, {
            field : 'deployname',
            title : '流程部署名称',
            align : 'left',
            valign : 'middle',
            sortable : true
        }, {
            field : 'deploytime',
            title : '流程发布时间',
            align : 'left',
            valign : 'middle',
            sortable : true
        },
        {
            title: '操作',
            field: 'option',
            align: 'center',
            formatter:function(value,row,index){  
            	var e = '<button class="btn btn-primary" onclick="optionClick(\''+ row.deployid + '\')">删除</button> ';  
          
              return e;  
          } 
        }]
    });
}
function optionClick(deployid){
	$.ajax({
		type: "POST",  
        dataType: "json",  
        url:ctx+"/flow/delDeployment.do",
        data: {
        	deployid:deployid,
        },                     
        success: function(data) {
        	if(data.status=='succ'){
        		
        		swal({
		            title: "提示",
		            text: '删除成功!',
		            confirmButtonText: "确认"},
		            function(){
		            	$('#demo-table').bootstrapTable('refresh');
		            }
		        );
        	}else{
        		swal({
		            title: "警告",
		            text: '删除失败',
		            confirmButtonText: "确认"
		        });
        	}
        	
        }
	});
};

function initDate(){
	/* 日历初始化 */
    $('.time').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true,
        format: "yyyy-mm-dd"
    });
}

function queryParams(params) {
	var contractno = $("#contractno").val();
	var loanno = $("#loanno").val();
	var defname = $("#defname").val();
	var deployname = $("#deployname").val();
	var currPage = this.pageNumber;
    var param = {
   		limit: params.limit,   //页面大小
        offset: currPage,//页码params.offset
        defname:defname,
        deployname:deployname
    }
    return param;
} 

function responseHandler(res) { 
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