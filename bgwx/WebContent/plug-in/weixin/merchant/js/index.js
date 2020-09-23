/**
 *
 * @authors FH
 * @date    2018-01-16 13:48:25
 * @version 20180116
 */
$(function() {
    var fucs = {
        init: function() {
            fucs.editBox();fucs.com();fucs.check();
        },
        btns: {
            min: '.minus',
            plu: '.plus',
            txt: '.amount_input'
        },
        oper: function(){
            fucs.m(fucs.btns.min, fucs.btns.txt);
            fucs.p(fucs.btns.min, fucs.btns.plu, fucs.btns.txt);
            fucs.empty(fucs.btns.txt);
        },
        m: function(b1, txt) {
            $(document).off('click').on('click', b1, function() {
                var _this = $(this);
                var mb = _this.next(txt);
                parseInt(mb.val()) <= 1 ? mb.val(1) : mb.val(parseInt(mb.val()) - 1);
                parseInt(mb.val()) <= 1 ? _this.addClass('disable') : _this.removeClass('disable');
                _this.parents('.shopping_con_listItem_des').find('.shopping_con_listItem_des_mm').text( mb.val() );
            });
        },
        p: function(b1, b2, txt) {
            $(document).on('click', b2, function() {
                var _this = $(this);
                var mp = _this.prev(txt);
                mp.val(parseInt(mp.val()) + 1);
                parseInt(mp.val()) <= 1 ? _this.siblings(b1).addClass('disable') : _this.siblings(b1).removeClass('disable');
                $(this).parents('.shopping_con_listItem_des').find('.shopping_con_listItem_des_mm').text( mp.val() );
            });
        },
        empty: function(txt) {
            $(document).on('blur', txt, function() {
                var _this = $(this);
                if (_this.val() == "" || parseInt(_this.val()) < 1) {
                    _this.val(1);
                };
                _this.parents('.shopping_con_listItem_des').find('.shopping_con_listItem_des_mm').text( _this.val() );
            });
        },
        com: function(){
            $('.commodity_listItem_priceR,.commodity_cho,.footer_btns_join,.footer_btns_buy').on('click',function(){
                fucs.oper();
            });
        },
        editBox: function() {
            var edit = $('.shopping_con_edit');
            var num = 0;
            var cart_item = '.shopping_con_listItem';
            var cart_item = '.shopping_con_listItem';
                var price = '.shopping_con_listItem_des_p';//单个价格
                var amount = '.shopping_con_listItem_des_mm';//单个数量
                var total = 0;

            edit.off('.click').on('click', function() {
                // var cart_item = '.shopping_con_listItem';
                // var price = '.shopping_con_listItem_des_p';//单个价格
                // var amount = '.shopping_con_listItem_des_mm';//单个数量
                // var total = 0;
                num++;
                if (num % 2) {
                    $('.shopping_del').show();
                    edit.text('完成');
                    $('.shopping_con_listItem_cha').html('<div class="commodity_buy_amount_detailR clearfix"><a href="javascript:;" class="minus fl">-</a><input type="number" class="amount_input fl" value="1"><a href="javascript:;" class="plus fl">+</a></div>');
                    $('.pay_cash').hide();
                    $('.payBtn').html('删除').on('click',function(){
                        $('.shopping_con_list').find('[check="true"]').parents(cart_item).remove();
                        $('.check_allBtn').removeClass('check_allBtn_on').attr('check','fasle');
                        //$('.check_allBtn').removeClass('check_itemBtn_on');
                    });
                    fucs.oper();
                    $('.amount_input').each(function(){
                        $(this).val($(this).parents('.shopping_con_listItem_des').find('.shopping_con_listItem_des_mm').text());
                    });
                    $('.shopping_del').on('click',function(){
                        $(this).parents('.shopping_con_listItem').remove();
                    });

                } else {
                    $('.shopping_del').hide();
                    edit.text('编辑');
                    $('.shopping_con_listItem_cha').html('<h4 class="shopping_con_listItem_des_tit">充电神器 手机通用无线充电器 含手机接口</h4>');
                    $('.pay_cash').show();
                    $('.payBtn').html('结算(<i class="pay_amount">0</i>)')
                    num = 0;
                    
                }
            });
            $(cart_item).each(function(i){
                        var a = parseFloat( $(this).find(price).text().substr(1) );
                        var b = parseFloat( $(this).find(amount).text() );
                        if( $(this).find('[check]').attr('check') == 'true' ){
                            total += a*b;
                        }
                        //debugger
                       // total += a*b;
                    });

                    $('.pay_cash1').text('合计:￥'+ total);
        },
        check: function(){
            var all = $('.check_allBtn');//全选按钮
            var item = $('.check_itemBtn');//每个选择按钮
            var cart_item = '.shopping_con_listItem';
            var num = 0;
            var count = 0;//选择计数
            var price = '.shopping_con_listItem_des_p';//单个价格
            var amount = '.shopping_con_listItem_des_mm';//单个数量
            var total = 0;


            all.off('.click').on('click', function(){
                total = 0;
                if( $(this).attr('check') == "" || $(this).attr('check') == 'false' ){
                    all.addClass('check_allBtn_on').attr('check','true');
                    $('.check_itemBtn').addClass('check_itemBtn_on').attr('check','true');
                    count = item.length;

                    $(cart_item).each(function(i){
                        var a = parseFloat( $(this).find(price).text().substr(1) );
                        var b = parseFloat( $(this).find(amount).text() );
                        total += a*b;
                    });

                    $('.pay_cash1').text('合计:￥'+ total);

                }else {
                    all.removeClass('check_allBtn_on').attr('check','false');
                    $('.check_itemBtn').removeClass('check_itemBtn_on').attr('check','false');
                    count = 0;

                    total = 0;
                    $('.pay_cash1').text('合计:￥'+ total);
                };
                $('.pay_amount').text(count);

                if( count > 0 ){
                    $('.payBtn').addClass('payBtn_on');
                    $('.pay_cash1').addClass('pay_cash1_on');
                }else{
                    $('.payBtn').removeClass('payBtn_on');
                    $('.pay_cash1').removeClass('pay_cash1_on');
                }
            });


            item.each(function(i){
                item.eq(i).on('click', function(){
                    var _this = $(this);
                    var a = _this.parents(cart_item).find(price).text().substr(1);
                    var b = _this.parents(cart_item).find(amount).text();
                    if( _this.attr('check') == "" || _this.attr('check') == 'false' ){
                        _this.addClass('check_itemBtn_on').attr('check','true');
                        total += a*b;
                        $('.pay_cash1').text('合计:￥'+ total);
                    } else{
                        _this.removeClass('check_itemBtn_on').attr('check','false');
                        total -= a*b;
                        $('.pay_cash1').text('合计:￥'+ total);
                    };
                    if( _this.attr('check') == 'true' ){
                        count++;
                    }else if( _this.attr('check') == 'false' ){
                        count--;
                    };//debugger
                    $('.pay_amount').text(count);
                    if( count == item.length ){
                        all.addClass('check_allBtn_on').attr('check','true');
                    }else{
                        all.removeClass('check_allBtn_on').attr('check','false');
                    };
                    if( count > 0 ){
                        $('.payBtn').addClass('payBtn_on');
                        $('.pay_cash1').addClass('pay_cash1_on');
                    }else{
                        $('.payBtn').removeClass('payBtn_on');
                        $('.pay_cash1').removeClass('pay_cash1_on');
                    }
                });
            });
        }

    }; //fucs
    fucs.init();




    $('.commodity_buy_style_listItem').on('click', function() {
        $(this).addClass('commodity_buy_style_listItem_on').siblings().removeClass('commodity_buy_style_listItem_on');
        $('.commodity_choL').text('已选');
        $('.commodity_choR').text($(this).text());
    });
    $('.phone_binding_close').on('click',function(){
        $(this).parent('.phone_binding').hide();
    });//购物车页绑定手机

    var up = {
        init: function() {
            var clp = $('.commodity_listItem_priceR');
            var pstate = $('.price_stateDes'); //价格说明
            var priBox = $('.price_intro');
            var comCho = $('.commodity_cho'); //选择规格
            var comBox = $('.commodity_buy_outer');
            var fbj = $('.footer_btns_join'); //加入购物车
            var fbb = $('.footer_btns_buy'); //立即购买
            var cjhtml = '<a href="javascript:;" class="join_cartB">加入购物车</a>';
            var cbhtml = '<a href="javascript:;" class="buy_btnB">下一步</a>';
            up.upBox1(clp, comBox); //首页直接购买
            up.upBox1(pstate, priBox); //价格说明
            up.upBox1(comCho, comBox); //选择规格
            up.upBox2(fbj, comBox, cjhtml); //加入购物车
            up.upBox2(fbb, comBox, cbhtml); //立即购买
        },
        upBox1: function(strike, popUp) {
            strike.on('click', function() {
                $('.shadow').show();
                popUp.stop().animate({ 'bottom': '0' }, 20).show();
            });
            $('.commodity_buy_close').on('click', function() {
                $('.shadow').hide();
                popUp.stop().animate({ 'bottom': '-100%' }, 20).hide();
            });
        },
        upBox2: function(strike, popUp, cBtn) {
            strike.on('click', function() {
                $('.shadow').show();
                popUp.stop().animate({ 'bottom': '0' }, 20).show();
                $('.commodity_buy_btns').html(cBtn);
            });
            $('.commodity_buy_close').on('click', function() {
                $('.shadow').hide();
                popUp.stop().animate({ 'bottom': '-100%' }, 20).hide();
                $('.commodity_buy_btns').html('<a href="javascript:;" class="join_cart">加入购物车</a><a href="javascript:;" class="buy_btn">立即购买</a>');
            });
        }
    }; //up
    up.init();
});