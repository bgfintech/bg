<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${pageContext.request.contextPath=='/'}" var="condition" scope="request">
　    <c:set var="ctx" value="" />
</c:if>

<script type="text/javascript">
	var $rootPath = "${ctx}"
    //alert($rootPath);
</script>
<link href="${ctx}/plug-in/merchant/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${ctx }/plug-in/merchant/css/style.css" media="screen"
	type="text/css" />
<link rel="stylesheet" href="${ctx }/plug-in/merchant/css/ion.calendar.css" type="text/css">	

<link rel="stylesheet" href="${ctx }/plug-in/merchant/js/layer/skin/default/layer.css" type="text/css">	


<link href="${ctx}/plug-in/merchant/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/animate.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/style.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="${ctx}/plug-in/merchant/css/user-defined.css" rel="stylesheet">

<link
	href="${ctx}/plug-in/merchant/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css"
	rel="stylesheet">

<script type="text/JavaScript" src="${ctx}/plug-in/merchant/js/jquery-2.1.1.js"></script>
<script type="text/JavaScript"  src="${ctx}/plug-in/merchant/js/sanpower/treeview/bootstrap-treeview.js"></script>

<script type="text/JavaScript" src="${ctx}/plug-in/merchant/js/bootstrap.min.js"></script>
<script type="text/JavaScript" src="${ctx}/plug-in/merchant/js/util/bootstrap3.modal.expend.js"></script><!-- 模态框扩展 -->
<script type="text/JavaScript" src="${ctx}/plug-in/merchant/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script type="text/JavaScript" src="${ctx}/plug-in/merchant/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>


<script type="text/JavaScript"  src='${ctx }/plug-in/merchant/js/jquery.from.min.js'></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/validate/jquery.validate.min.js"></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/validate/messages_zh.min.js"></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/validate/common.validate.js"></script>
<script type="text/JavaScript"  src='${ctx }/plug-in/merchant/js/jquer.formload.data.js'></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/moment.min.js"></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/moment.zh-cn.js"></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/ion.calendar.min.js"></script>
<link rel="stylesheet" href="${ctx}/plug-in/merchant/css/plugins/sweetalert/sweetalert.css">
<script src="${ctx }/plug-in/merchant/js/layer/layer.js"></script>

<!-- 邮箱提示 -->
<script src="${ctx}/plug-in/merchant/js/emailAutoComplete.js"></script>

<!-- Custom and plugin javascript -->
<script src="${ctx}/plug-in/merchant/js/inspinia.js"></script>
<script src="${ctx}/plug-in/merchant/js/plugins/pace/pace.min.js"></script>

<!-- Chosen -->
<script src="${ctx}/plug-in/merchant/js/plugins/chosen/chosen.jquery.js"></script>

<!-- JSKnob -->
<script src="${ctx}/plug-in/merchant/js/plugins/jsKnob/jquery.knob.js"></script>

<!-- Input Mask-->
<script src="${ctx}/plug-in/merchant/js/plugins/jasny/jasny-bootstrap.min.js"></script>

<!--日历-->
<script src="${ctx}/plug-in/merchant/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- NouSlider -->
<script src="${ctx}/plug-in/merchant/js/plugins/nouslider/jquery.nouislider.min.js"></script>

<!-- Switchery -->
<script src="${ctx}/plug-in/merchant/js/plugins/switchery/switchery.js"></script>

<!-- IonRangeSlider -->
<script src="${ctx}/plug-in/merchant/js/plugins/ionRangeSlider/ion.rangeSlider.min.js"></script>

<!-- iCheck -->
<script src="${ctx}/plug-in/merchant/js/plugins/iCheck/icheck.min.js"></script>
<script>
	$(document).ready(function() {
		$('.i-checks').iCheck({
			checkboxClass : 'icheckbox_square-green',
			radioClass : 'iradio_square-green',
		});
	});
</script>


<!-- MENU -->
<script src="${ctx}/plug-in/merchant/js/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- Color picker -->
<script src="${ctx}/plug-in/merchant/js/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>

<!-- Clock picker -->
<script src="${ctx}/plug-in/merchant/js/plugins/clockpicker/clockpicker.js"></script>

<!-- Image cropper -->
<script src="${ctx}/plug-in/merchant/js/plugins/cropper/cropper.min.js"></script>

<!-- Date range use moment.js same as full calendar plugin日历 -->
<script src="${ctx}/plug-in/merchant/js/plugins/fullcalendar/moment.min.js"></script>

<script src="${ctx}/plug-in/merchant/js/plugins/sweetalert/sweetalert.min.js"></script>


  
  
<!-- 分页 -->  
<link href="${ctx }/plug-in/merchant/js/pagination/style/pagination.css" type="text/css" rel="stylesheet" >
<link href="${ctx }/plug-in/merchant/css/style.css" rel="stylesheet">
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/ion.calendar.min.js"></script>
<script src="${ctx}/plug-in/merchant/js/sanpower/jquery.formload.data.js"></script>
<script src="${ctx}/plug-in/merchant/js/jquery.json.min.js"></script>
<script src="${ctx}/plug-in/merchant/js/layer/layer.js"></script>
<script type="text/JavaScript"  src="${ctx }/plug-in/merchant/js/pagination/script/jquery.pagination.js"></script>
