<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=mobiscrollice-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title></title>
        <script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
        <script src="plug-in/merchant/mobiscroll/js/mobiscroll.core-2.5.2.js" type="text/javascript"></script>
        <script src="plug-in/merchant/mobiscroll/js/mobiscroll.core-2.5.2-zh.js" type="text/javascript"></script>
        <link href="plug-in/merchant/mobiscroll/css/mobiscroll.core-2.5.2.css" rel="stylesheet" type="text/css" />
        <link href="plug-in/merchant/mobiscroll/css/mobiscroll.animation-2.5.2.css" rel="stylesheet" type="text/css" />

		<link href="plug-in/merchant/css/reset.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="plug-in/merchant/css/LArea.css"/>
        <link href="plug-in/merchant/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		<link href="plug-in/merchant/css/common.css" rel="stylesheet" type="text/css" />

        <script src="plug-in/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1.js" type="text/javascript"></script>
        <script src="plug-in/merchant/mobiscroll/js/mobiscroll.datetime-2.5.1-zh.js" type="text/javascript"></script>
        <!-- S 可根据自己喜好引入样式风格文件 -->
        <script src="plug-in/merchant/mobiscroll/js/mobiscroll.android-ics-2.5.2.js" type="text/javascript"></script>
        <link href="plug-in/merchant/mobiscroll/css/mobiscroll.android-ics-2.5.2.css" rel="stylesheet" type="text/css" />
        <!-- E 可根据自己喜好引入样式风格文件 -->
        <style type="text/css">
        .content {
        padding: 15px;
        background: #fff;
        }
        </style>
        <script type="text/javascript">
        $(function() {
        var currYear = (new Date()).getFullYear();
        var opt = {};
        opt.date = {
        preset: 'date'
        };
        //opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
        opt.datetime = {
        preset: 'datetime'
        };
        opt.time = {
        preset: 'time'
        };
        opt.default = {
        theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式
        mode: 'scroller', //日期选择模式
        lang: 'zh',
        startYear: currYear - 10, //开始年份
        endYear: currYear + 10 //结束年份
        };
        $("#appDate1").val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
        $("#appDate2").val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));

        });
        </script>
    </head>
    <body>
        <div class="transact_rec clearfix">
            <div class="transact_recL">
                <div class="transact_recL1">2017年12月</div>
                <div class="transact_recL2">支出<span>1890</span>&nbsp;&nbsp;收入<span>1890</span></div>
            </div>
            <div class="transact_recR">
                <a href="javascript:;" class="transact_recR_cal">
                    <input type="text" placeholder="" name="appDate" id="appDate1" />
                    <i class="fa fa-calendar fa-2x" aria-hidden="true"></i>
                </a>
            </div>
        </div><!--transact_rec-->
        <ul class="transact_rec_list">
            <li class="transact_rec_listItem clearfix">
                <div class="transact_rec_listItemL1"><img src="plug-in/merchant/images/1.jpg" alt="" /></div>
                <div class="transact_rec_listItemL2">
                    <div class="transact_rec_listItemL2_1">12月22日</div>
                    <div class="transact_rec_listItemL2_2">15:00</div>
                </div>
                <div class="transact_rec_listItemR1">
                    <div class="transact_rec_listItemR1_1">-200</div>
                    <div class="transact_rec_listItemR1_2">已全额退款</div>
                </div>
            </li>
            <li class="transact_rec_listItem clearfix">
                <div class="transact_rec_listItemL1"><img src="plug-in/merchant/images/1.jpg" alt="" /></div>
                <div class="transact_rec_listItemL2">
                    <div class="transact_rec_listItemL2_1">12月22日</div>
                    <div class="transact_rec_listItemL2_2">15:00</div>
                </div>
                <div class="transact_rec_listItemR1">
                    <div class="transact_rec_listItemR1_1">-200</div>
                    <div class="transact_rec_listItemR1_2"></div>
                </div>
            </li>
            <li class="transact_rec_listItem clearfix">
                <div class="transact_rec_listItemL1"><img src="plug-in/merchant/images/1.jpg" alt="" /></div>
                <div class="transact_rec_listItemL2">
                    <div class="transact_rec_listItemL2_1">12月22日</div>
                    <div class="transact_rec_listItemL2_2">15:00</div>
                </div>
                <div class="transact_rec_listItemR1">
                    <div class="transact_rec_listItemR1_1">-200</div>
                    <div class="transact_rec_listItemR1_2">已全额退款</div>
                </div>
            </li>
        </ul><!--transact_rec_list-->

    </body>
</html>
    <!-- <script type="text/javascript">
    // 对浏览器的UserAgent进行正则匹配，不含有微信独有标识的则为其他浏览器
    var useragent = navigator.userAgent;
    if (useragent.match(/MicroMessenger/i) != 'MicroMessenger') {
        // 这里警告框会阻塞当前页面继续加载
        alert('已禁止本次访问：您必须使用微信内置浏览器访问本页面！');
        // 以下代码是用javascript强行关闭当前页面
        var opened = window.open('about:blank', '_self');
        opened.opener = null;
        opened.close();
    }
</script> -->
