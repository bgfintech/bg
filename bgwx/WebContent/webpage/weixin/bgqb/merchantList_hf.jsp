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
<link href="<%=basePath %>/plug-in/merchant/fileinput/css/fileinput.css" media="all"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath %>/plug-in/merchant/fileinput/themes/explorer-fa/theme.css"
	media="all" rel="stylesheet" type="text/css" />
<script src="<%=basePath %>/plug-in/merchant/fileinput/js/plugins/sortable.js"
	type="text/javascript"></script>
<script src="<%=basePath %>/plug-in/merchant/fileinput/js/fileinput.js"
	type="text/javascript"></script>
<script src="<%=basePath %>/plug-in/merchant/fileinput/js/locales/fr.js"
	type="text/javascript"></script>
<script src="<%=basePath %>/plug-in/merchant/fileinput/js/locales/es.js"
	type="text/javascript"></script>
<script src="<%=basePath %>/plug-in/merchant/fileinput/themes/explorer-fa/theme.js"
	type="text/javascript"></script>
<script src="<%=basePath %>/plug-in/merchant/fileinput/themes/fa/theme.js"
	type="text/javascript"></script>




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
									<label for="merid" class="control-label">商户号</label> <input
										type="text" class="form-control" name="idcard" id="idcard">
								</div>

							</div>
							<div class="col-xs-12"></div>
						</div>
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" class="btn btn-warning"
								onclick="getList(1)">查询</button>
						</div>
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" class="btn btn-warning"
								onclick="cleanBut()">清空</button>
						</div>
						
						
					</div>
					<div class="ibox-content">
						<div class="btn-group" role="group" aria-label="...">
							<button type="button" class="btn btn-success"
								class="btn btn-primary" data-toggle="modal" data-target="#"
								onclick="newMer()">新增商户</button>
						</div>
						<table id="table" class="table table-striped">
							<thead class="clearfix">
								<tr class="clearfix">
									<th>商户号</th>
									<th>商户名称</th>
									<th>营业执照号</th>
									<th>法人姓名</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="table_list_2">
							</tbody>
						</table>
						<div class="btns clearfix">
							<div class="M-box4"></div>
						</div>
						<br>共<span id="total"></span>条记录
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--  s:添加 -->
	<div class="modal inmodal fade" id="myModal4" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title">添加商户</h3>
					<small class="font-bold"> </small>
				</div>
				<div class="modal-body">
					<div class="ibox-content pd0">
						<form role="form" id="my_add_M_form" onsubmit="return false">
							<div class="form-inline">
								<div class="form-group posr">

									<div class="form-group posr">
										<label>商户属性:</label>  <select
											class="form-control" id="isGroupMerShop" name="isGroupMerShop">
											<option value="0">集团商户</option>
											<option value="1">普通商户</option>
											<option value="2">门店</option>
										</select>
									</div>

								</div>


							</div>
							<div id="clean_add_id">
								<div class="form-inline">
									<div class="form-group posr">
										<label>商户名称:</label> <input type="text"
											class="form-control inputfln required" id="merName"
											name="merName">
									</div>

									<div class="form-group posr">
										<label>商户简称:</label> <input type="text"
											class="form-control inputfln required" id="merShortName"
											name="merShortName" onchange="">
									</div>

								</div>
								<div class="form-inline">
									<div class="form-group posr">
										<label>商户类型:</label> <select class="form-control "
											id="merType" name="merType">
											<option value="1">企业</option>
											<option value="2">个体户</option>
											<option value="3">自然人</option>
										</select>
									</div>


								</div>
								<div class="form-inline">

									<div class="form-group posr">
										<label>商户注册地址:</label> <input type="text"
											class="form-control inputfln required" id="addr" name="addr">
									</div>

								</div>

								<div class="form-inline">
									<div class="form-group posr">
										<label>商户经营地址:</label> <input type="text"
											class="form-control inputfln " id="installAddr"
											name="installAddr">
									</div>
									<div class="form-group posr">
										<label>营业证明文件类型:</label> <select class="form-control "
											id="netMchntSvcTp" name="netMchntSvcTp">
											<option value="01">营业执照</option>
											<option value="02">事业单位法人证书</option>
											<option value="03">身份证件</option>
											<option value="04">其他证明文件</option>
											<option value="05">营业执照(多证合一)</option>
										</select>
									</div>

								</div>
								<div id="add_from_show">
								
									<div class="form-inline">
										<div class="form-group posr">
											<label>营业证明文件号码:</label> <input type="text"
												class="form-control inputfln required" id="comregid"
												name="comregid" />
										</div>
										<div class="form-group posr">
											<label>经营类目:</label> <input type="text"
												class="form-control inputfln required" id="mcc"
												readonly="readonly" name="mcc">
											<button type="submit" class="btn btn-primary"
												onclick="showTip()">选择</button>
										</div>


									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>实际营业范围:</label> <input type="text"
												class="form-control inputfln required" id="realOptScope"
												name="realOptScope">
										</div>
										<div class="form-group posr">
											<label>(法人)证件类型:</label> <select class="form-control "
												id="artifCertifTp" name="artifCertifTp">
												<option value="00">法人代表</option>
												<option value="02">个体户</option>
												<option value="01">身份证</option>
											</select>
										</div>

									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>(法人)姓名 :</label> <input type="text"
												class="form-control inputfln required" id="legalName"
												name="legalName">
										</div>
										<div class="form-group posr">
											<label>(法人)身份证号码 :</label> <input type="text"
												class="form-control inputfln required" id="legalId"
												name="legalId">

										</div>


									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>(法人)手机号码 :</label> <input type="text"
												class="form-control inputfln required" id="mobilephone"
												name="mobilephone">
										</div>
										<div class="form-group posr">
											<label>银联地区号 :</label> <input type="text"
												class="form-control inputfln required" id="areaCode"
												readonly="readonly" name="areaCode">
											<button type="submit" class="btn btn-primary"
												onclick="showTip()">选择</button>

										</div>


									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>商户联系人 :</label> <input type="text"
												class="form-control inputfln required" id="contactor"
												name="contactor">
										</div>
										<div class="form-group posr">
											<label>商户联系人座机号码:</label> <input type="text"
												class="form-control inputfln required"
												id="contactorTelephone" name="contactorTelephone">
										</div>
									</div>
									<!-- 银行开户信息 -->
									<div class="form-inline">
										<div class="form-group posr">
											<label>集团商户编号:</label> <input type="text"
												class="form-control inputfln required" id="groupId"
												name="groupId">
										</div>
										<div class="form-group posr">
											<label>是否开通恒丰商户服务平台:</label> <select
												class="form-control required" id="enableMspfUser"
												name="enableMspfUser">
												<option value="1">启用</option>
												<option value="0">停用</option>
											</select>
										</div>
									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>结算账户名称:</label> <input type="text"
												class="form-control inputfln required" id="receiveAccName"
												name="receiveAccName">
										</div>
										<div class="form-group posr">
											<label>结算账户帐号:</label> <input type="text"
												class="form-control inputfln required" id="receiveAcc"
												name="receiveAcc">
										</div>
									</div>

									<div class="form-inline">
										<div class="form-group posr">
											<label>结算账户开户行行号:</label> <input type="text"
												class="form-control inputfln required" id="openBank"
												readonly="readonly" name="openBank" value="000000">
											<button type="submit" class="btn btn-primary"
												onclick="showTip()">选择</button>
										</div>
									</div>
									<div class="form-inline">

										<div class="form-group posr">
											<label>结算账户类型:</label> <select class="form-control required"
												id="receiveAccType" name="receiveAccType">
												<option value="1">对私</option>
												<option value="2">对公</option>
											</select>
										</div>
									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>清算周期:</label><select class="form-control required"
												id="receiveSettlementPeriod" name="receiveSettlementPeriod">
												<option value="T0">T+0</option>
												<option value="T1">T+1</option>
											</select>
										</div>
										<div class="form-group posr">
											<label>申请开通业务:</label> <select class="form-control required"
												id="weixinFlag" name="weixinFlag">
												<option value="1">恒星付</option>
												<option value="2">POS收单</option>
												<option value="3">恒星付+POS收单</option>
											</select>
										</div>
									</div>
									<div class="form-inline">
										<div class="form-group posr">
											<label>扫码手续费费率:</label> <input type="text"
												class="form-control inputfln required" id="payFee"
												name="payFee">%
										</div>

									</div>

								</div>
							</div>



						</form>
						<div class="form-inline">
							<form enctype="multpart/ form-data">
								<div class="form-group posr">
									<label for="input-25">影像信息</label>
									<div class="file-loading">
										<input id="input-25" name="input25[]" type="file" multiple>
									</div>
									<script>
												$(document).on('ready', function() {
												    	$("#input-25").fileinput({
												        
												        deleteUrl: "/site/file-delete",
												        overwriteInitial: true,
												        language:'us',
												        maxFileSize: 1000,
												        uploadUrl:'merController.do?uploadpic',
												        allowedFileExtensions:['jpg','png'],
												        initialCaption: "请选择要上传的商户照片"
												    });
												});
											</script>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
							<button type="submit" class="btn btn-primary"
								id="submit_add_M_id" onclick="addMer()">保存</button>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	<!--  e:添加 -->

	<!--  s:查看-->
	<div class="modal inmodal fade" id="queryOneM" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title">商户详情</h3>
					<small class="font-bold"> </small>
				</div>
				<div class="modal-body">
					<div class="ibox-content pd0">

						<div class="form-inline">
							<div class="form-group posr">

								<div class="form-group posr">
									<label>商户属性:</label> <input type="text"
										class="form-control inputfln required" id="isGroupMerShop"
										readonly="readonly" name="isGroupMerShop" >
								</div>
							</div>
						</div>

						<div class="form-inline">
							<div class="form-group posr">
								<label>商户名称:</label> <input type="text"
									class="form-control inputfln required" id="merName"
									readonly="readonly" name="merName">
							</div>

							<div class="form-group posr">
								<label>商户类型:</label> <input type="text"
									class="form-control inputfln required" id="merType"
									name="merType" readonly="readonly">
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>商户注册地址:</label> <input type="text"
									class="form-control inputfln required" id="addr" name="addr"
									readonly="readonly">
							</div>
							<div class="form-group posr">
								<label>商户经营地址:</label> 
								<input type="text"
									class="form-control inputfln required" id="installAddr" name="installAddr"
									readonly="readonly">
							</div>
						</div>
						<div class="form-inline">

							<div class="form-group posr">
								<label>营业证明文件类型:</label> <select class="form-control required"
									id="netMchntSvcTp" name="netMchntSvcTp" disabled="disabled">
									<input type="text"
									class="form-control inputfln required" id="netMchntSvcTp" name="netMchntSvcTp"
									readonly="readonly">
							</div>

							<div class="form-group posr">
								<label>营业证明文件号码:</label><input type="text"
									class="form-control inputfln required" id="comregid" name="comregid"
									readonly="readonly">

							</div>
						</div>

						<div class="form-inline">
							<div class="form-group posr">
								<label>经营类目:</label> <input type="text"
									class="form-control inputfln required" id=""
									name="mcc" readonly="readonly">
							</div>

							<div class="form-group posr">
								<label>实际营业范围:</label> <input type="text"
									class="form-control inputfln required" id="realOptScope"
									name="realOptScope" readonly="readonly">
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>(法人)证件类型:</label> <input type="text"
									class="form-control inputfln required" id="qq_payment_date"
									name="paymentDate" readonly="readonly">
							</div>

							<div class="form-group posr">
								<label>(法人)姓名:</label> <input type="text"
									class="form-control inputfln required" id="legalId"
									name=legalId readonly="readonly">
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>(法人)身份证号码:</label> <input type="text"
									class="form-control inputfln required" id="qq_open_bank_name"
									name="openBankName" readonly="readonly" />
							</div>


							<div class="form-group posr">
								<label>(法人)手机号码:</label> <input type="text"
									class="form-control inputfln required"
									id="mobilephone" name="mobilephone"
									readonly="readonly">
							</div>


						</div>
						<div class="form-inline">

							<div class="form-group posr">
								<label>银联地区号:</label> <select class="form-control "
									id="areaCode"
									name="areaCode" disabled="disabled">
								</select>
							</div>
							<div class="form-group posr">
								<label>商户联系人:</label> <input type="text"
									class="form-control inputfln required"
									id="contactor" name="contactor"
									readonly="readonly" />
							</div>

						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>商户联系扔座机号:</label> <input type="text"
									class="form-control inputfln required"
									id="contactorTelephone" name="contactorTelephone"
									readonly="readonly" />

							</div>

							<div class="form-group posr">
								<label>商户类别 :</label> <input type="text"
									class="form-control inputfln required" readonly="readonly"
									id="qq_merchant_type" name="merchantType">
							</div>

						</div>
						
						<!-- 银行开户信息 -->
						<div class="form-inline">
							<div class="form-group posr">
								<label>集团商户编号:</label> <input type="text"
									class="form-control inputfln required" readonly="readonly" id="groupId"
									name="groupId">
							</div>
							<div class="form-group posr">
								<label>是否开通恒丰商户服务平台:</label> <select
									class="form-control required" id="enableMspfUser"
									name="enableMspfUser">
									<option value="1">启用</option>
									<option value="0">停用</option>
								</select>
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>结算账户名称:</label> <input type="text"
									class="form-control inputfln required" readonly="readonly" id="receiveAccName"
									name="receiveAccName">
							</div>
							<div class="form-group posr">
								<label>结算账户帐号:</label> <input type="text"
									class="form-control inputfln required" id="receiveAcc"
									name="receiveAcc">
							</div>
						</div>

						<div class="form-inline">
							<div class="form-group posr">
								<label>结算账户开户行行号:</label> <input type="text"
									class="form-control inputfln required" id="openBank"
									readonly="readonly" name="openBank" value="000000">
								<button type="submit" class="btn btn-primary"
									onclick="showTip()">选择</button>
							</div>
						</div>
						<div class="form-inline">

							<div class="form-group posr">
								<label>结算账户类型:</label> <select class="form-control required"
									id="receiveAccType" name="receiveAccType">
									<option value="1">对私</option>
									<option value="2">对公</option>
								</select>
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>清算周期:</label><select class="form-control required"
									id="receiveSettlementPeriod" name="receiveSettlementPeriod">
									<option value="T0">T+0</option>
									<option value="T1">T+1</option>
								</select>
							</div>
							<div class="form-group posr">
								<label>申请开通业务:</label> <select class="form-control required"
									id="weixinFlag" name="weixinFlag">
									<option value="1">恒星付</option>
									<option value="2">POS收单</option>
									<option value="3">恒星付+POS收单</option>
								</select>
							</div>
						</div>
						<div class="form-inline">
							<div class="form-group posr">
								<label>扫码手续费费率:</label> <input type="text"
									class="form-control inputfln required" readonly="readonly" id="payFee"
									name="payFee">%
							</div>

						</div>

					</div>
						
						<div class="modal-footer">
							<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--  e:查看-->
	<!--  s:查看-->
	<div class="modal inmodal fade" id="mccdialog" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">

				<div class="modal-body">
					<div id="tree"></div>

					<input type="button" id="btn" name="btn" value="选择" />
				</div>
			</div>
		</div>

	</div>
</body>


<script>
	
    $(function(){
    	
        
    	getList(1);
        
       
        $("#btn").click(function (e) {

            var arr = $('#tree').treeview('getSelected');

            alert(JSON.stringify(arr));
            for (var key in arr) {
                alert(arr[key].id);
            }

        })
        $('#myModal4').on('hide.bs.modal', function () {
        	getList(1);
        })
    });

    function cleanBut(){
        $("#merid").val("");
      
    };
    
    /* 初始化pager */
    var pager = {}
    function getList(currentPage){
         
        var id = $("#merid").val();
        
        $.ajax({
            type: "POST",
            dataType: "json",
            url:"merController.do?getmerList",
            data: {
                currentPageNumb:currentPage,
                id:id
            },
            success: function(data) {
            	
                if(data.SUCCESS == "00"){
                    pager = data["RESULT_CONTENT"];
                    var html = '';
                    if(pager.rowCount!=0){
                        var list = pager.list;
                        $(list).each(function(){
                            html += "<tr class='clearfix'>\n" +
                                "                <td>"+this.id+"</td>\n" +
                                "                <td>"+this.merName+"</td>\n" +
                                "                <td>"+this.comregid+"</td>\n" +
                                "                <td>"+this.legalName+"</td>\n" +
                                "                <td>"+this.updtime+"</td>\n" +
                                "                <td><button class='btn btn-primary' onclick='detail_query(\""+this.id+"\")'>详情</button>"+
                                                    "&nbsp&nbsp<div class='btn-group' role='group' aria-label='...'><button class='btn btn-primary' onclick='audit(\""+this.id+"\")'>审核</button></div>"+
                                                    "&nbsp&nbsp<div class='btn-group' role='group' aria-label='...'><button class='btn btn-primary' onclick='delete(\""+this.id+"\")'>删除</button></div>"
                                                    
		            						"</td>\n" +
		            		"            </tr>";


                        });
                    }else{
                        html = "<tr><td colspan='13'>暂无数据</td></tr>"
                    }
                    //显示总记录条数
                    $("#total").text(pager.rowCount);
                }
                $("#table_list_2").html(html);
            },
            complete:function(){
                pagination();
            }
        });
    }

    function newMer(){
    	$('#myModal4').modal('show');
    }
    
    function detail_query(id){
    	 $('#queryOneM').modal('show');
    }
    function upload_photo(id){
    	 $('#uploadphoto').modal('show');
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
    
    function addMer(){
    	var str_data=$("#my_add_M_form input").map(function(){
         	  return ($(this).attr("id")+'='+$(this).val());
         	   }).get().join("&") ;
    	alert(str_data);
    	
    	str_data+="&"+$("#my_add_M_form select").map(function(){
       	  return ($(this).attr("id")+'='+$(this).val());
  	   }).get().join("&") ;
    	alert(str_data);
    	$.ajax({
      	   type: "POST",
      	   url: "merController.do?savemer",
      	   data: str_data,
      	   success: function(data){
      		var callback =JSON.parse(data);
    		   if(callback.success){
    			   alert(callback);
    		   }
      	 }
      });
    }
    function getTree() {
        // Some logic to retrieve, or generate tree structure


        var data = [{
            text: "p1",
            nodes: [{ text: "p1-1", id: '00001', nodeId: '00001' }, { text: "p1-2", id: '00002' }, { text: "p1-3", id: '00003' }, { text: "p1-4", id: '00004', nodes: [{ text: 'p1-1-1', id: '00005',nodes:[{text: 'p1-1-1', id: '00005'}]}]}]

        }]
        return data;
    }
    function showTip(){
    	$('#mccdialog').on('show.bs.modal', function (e) {  
    	    $(this).find('.modal-dialog').css({  
    	        'margin-top': function () {  
    	            var modalHeight = $('#mccdialog').find('.modal-dialog').height();  
    	            return ($(window).height() / 2 - (modalHeight / 5));  
    	        }  
    	    });      
    	}); 
    	 var obj = {};
         obj.text = "123";
         $('#tree').treeview({
             data: getTree(),         // data is not optional
             levels: 5,
             multiSelect: true

         });

    	 $('#mccdialog').modal('show');
    }
   

</script>
</html>