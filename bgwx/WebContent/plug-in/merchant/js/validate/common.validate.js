//手机号码 
jQuery.validator.addMethod("isPhone", function(value,element) { 
  var length = value.length; 
  var mobile = /^1[3|4|5|7|8][0-9]\d{8}$/;
  return this.optional(element) || mobile.test(value); 
}, "请正确填写手机号");

//联系电话(手机/电话皆可)验证 
jQuery.validator.addMethod("isCallPhone", function(value,element) { 
  var length = value.length; 
  var mobile = /^1[3|4|5|7|8][0-9]\d{8}$/; 
  var tel1 = /^[0]\d{2,4}\-\d{7,8}$/; 
  var tel2 = /^[0]\d{2,4}\d{7,8}$/; 
  var tel3 = /^[0]\d{2,3}\-\d{7,8}$/; 
  return this.optional(element) || (tel1.test(value) || mobile.test(value) ||tel2.test(value)||tel3.test(value)); 
}, "请正确填写联系电话,固话请填写区号"); 

//邮政编码验证
jQuery.validator.addMethod("isZipCode", function(value, element) { 
	  var tel = /^[0-9]{6}$/; 
	  return this.optional(element) || (tel.test(value)); 
}, "请正确填写邮政编码"); 

//中文字两个字节 
jQuery.validator.addMethod("byteRangeLength", function(value, element, param) { 
   var length = value.length; 
   for(var i = 0; i < value.length; i++){ 
	   if(value.charCodeAt(i) > 127){ 
		   length++; 
	   } 
   } 
return this.optional(element) || ( length >= param[0] && length <= param[1] ); 
}, "请确保输入的值在3-15个字节之间(一个中文字算2个字节)"); 
//中文字符校验
jQuery.validator.addMethod("isChinese",function(value,element){
	var cLanguage=/^[\u4e00-\u9fa5]{2,10}$/;
	return this.optional(element)||(cLanguage.test(value));
},"请输入中文,2~10个汉字");

//银行卡号
jQuery.validator.addMethod("isBankCard",function(value,element){
	var cLanguage=/^[0-9]{16,19}$/;
	return this.optional(element)||(cLanguage.test(value));
},"请输入正确的银行卡号");


