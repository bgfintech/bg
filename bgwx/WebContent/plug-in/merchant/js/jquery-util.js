/**
 * @author liucx
 * @createTime 2017/05/11
 * @see jQuery
 * @see iCheck
 */

$.getChecked = function(elementName){
	var str = '';
	$("input[name='" + elementName + "']:checkbox").each(function(){
		if(true == $(this).is(':checked')){
			str += $(this).val() + ",";
		}
	});
	return str;
}

$.getCheckedLength = function(elementName){
	return $.getCheckedAppNum(elementName).split(',').length;
}