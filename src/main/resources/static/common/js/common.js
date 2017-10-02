//解决IE8不支持console的bug
window.console = window.console || (function(){ 
	var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile 
	= c.clear = c.exception = c.trace = c.assert = function(){}; 
	return c; 
})();
jQuery(function ($) {
        // 备份jquery的ajax方法    
        var _ajax = $.ajax;
        // 重写ajax方法，先判断登录在执行success函数   
        $.ajax = function (opt) {
            var _success = opt && opt.success || function (a, b) {};
            var _opt = $.extend(opt, {
                success: function (data, textStatus) {
                    // 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要找到data是登录页的证据(标记)  
                    if (Object.prototype.toString.call(data) === "[object String]"&&data.indexOf("loginForm") != -1) {
                        var redirectUrl = "/Austin";
                        Vue.prototype.$Modal.info({
                            title: '登录超时',
                            content: '登录超时，请重新登录',
                            onOk: function() {
                            	window.location.href = redirectUrl;
                            	_success(data, textStatus);
                            },  	
                        });
                    } else {
                    	_success(data, textStatus);
                    }
                }
            });
            _ajax(_opt);
        };
        //initCustomerHref();
    });

function removeUselessCharCommon(val,num,type) {
	if(type == 1){ //不允许输入特殊字符
	    if(/["'<>%;&+]/.test(val)){
	    	val = val.replace(/["'<>%;&+]/g,"");
	    }
    } else if(type == 2){//只能输入数字但第一个字符不能为0
    	val = val.replace(/\D|^0/g,'');
    	if(/[^(\d)]/.test(val)){
    		val = val.replace(/[^(\d)]/g,'');
    	}
    } else if(type == 3){//只能输入数字
    	if(/[^(\d)]/.test(val)){
    		val = val.replace(/[^(\d)]/g,'');
    	}
    } else if(type == 4){//只能输入数字和-符号
    	if(/[^(\d|\-]/.test(val)){
    		val = val.replace(/[^(\d|\-]/g,'');
    	}
    }
	if(val.length > num) { // 超过字段长度的字符舍弃
		val = val.substring(0, num);
	}
	return val;
}

function removeUselessChar(th,num,type){ // th为HTML元素的this
	var val = th.value;
	th.value = removeUselessCharCommon(val,num,type);
}

Vue.prototype.removeUselessChar = function(val,num,type){ // $root可传入Vue的this
	return removeUselessCharCommon(val,num,type);
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

Date.prototype.toJSON = function () {
	return this.Format('yyyy-MM-dd');
}

function initCustomerHref(url,customerid){	
		$.ajax({
			url:url,
			type:'post',
			dataType:'html',
			success:function(html){
				$('#slidereveal').html(html);
				return false;
			},beforeSend: function() {
				$('#slidereveal').hide();
			},
			complete:function(){
				$('#slidereveal').show();
				$('.slimScrollDiv>.slimScrollBar').css({"top":"0"});
			}
		});
}



function searchBaidu(company){
 	window.open("https://www.baidu.com/s?wd=" + company);
}

function openUrl(web){
	if(web!= null){
		var index = web.indexOf("http://www");
		var index2 = web.indexOf("www");
		if(index == 0){
			window.open(web);
			return;
		}
		if(index2 == 0){
			window.open("http://" + web);
			return;
		}
	}
	if(/([a-zA-Z0-9]*.[a-zA-Z]+)+/.test(web)){
		web = "www."+web;
	}
 	window.open("http://" + web);
}
function UUID(){
	var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = new Array(36), rnd=0, r;
    for (var i = 0; i < 36; i++) {
      if (i==8 || i==13 ||  i==18 || i==23) {
        uuid[i] = '-';
      } else if (i==14) {
        uuid[i] = '4';
      } else {
        if (rnd <= 0x02) rnd = 0x2000000 + (Math.random()*0x1000000)|0;
        r = rnd & 0xf;
        rnd = rnd >> 4;
        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
      }
    }
    return uuid.join('');
}

function UUID32() {
	return UUID().replace(/-/g, '');
}

function download(url){
	var iframe = document.createElement("iframe");  
    document.body.appendChild(iframe);  
    iframe.src =url;
}

function toggleButtonState(event) {
	var $input;
	var _element = event.target;
	if(_element.tagName=="SPAN") {
		$input = $(_element).parent();
	} else if(_element.tagName=="BUTTON") {
		$input = $(_element);
	}
	if($input) {
		$input.attr("disabled", true);
		var func = function() {
			$input.attr('disabled', false);
		}
		window.setTimeout(func, 1500);
	}
}
function initScrollingMore(id,f,o) {
	if(Object.prototype.toString.call(f) === "[object Function]"){
		var _id = '#'+id;
		var topPos=$(window).height()-$(_id).offset().top-15;
		var _opt = $.extend({},o,{height:topPos+'px'});
		$(_id).slimScroll(_opt).bind('slimscrolling', function (e, pos) {
			var me = $(this);
			var vH = me.height();
	        var cH = me.get(0).scrollHeight;
	        var scrollTop = me.scrollTop();
			if( scrollTop/(cH -vH)>=0.95 ){
				f.call(this,{});
				_scrollContentX(this,scrollTop,cH);
			}
		});
		function _scrollContentX(th,scrollTop,cH) {
			var _cH = $(th).get(0).scrollHeight;
			if (cH == _cH) {
				setTimeout(function(){_scrollContentX(th,scrollTop,cH);},1000);
				return;
			}else{
				scrollContentX(th,scrollTop);
			}
		}
		function scrollContentX(th,y)
		{
			var me = $(th);
			var delta = y;
			var bar = me.siblings('.slimScrollBar');
			var maxTop = me.outerHeight() - bar.outerHeight();

			// calculate actual scroll amount
			percentScroll = parseInt(bar.css('top')) / (me.outerHeight() - bar.outerHeight());
			delta = percentScroll * (me[0].scrollHeight - me.outerHeight());

			delta = y;
			var offsetTop = delta / me[0].scrollHeight * me.outerHeight();
			offsetTop = Math.min(Math.max(offsetTop, 0), maxTop);
			bar.css({ top: offsetTop + 'px' });
		    
			me.scrollTop(delta);
		}
	}
	
}
//注册
Vue.filter('price-filter', function (value) {
  return value.toFixed(2);
})
// getter，返回已注册的过滤器
var myFilter = Vue.filter('price-filter');
