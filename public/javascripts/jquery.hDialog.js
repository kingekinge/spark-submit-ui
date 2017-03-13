/**
+-------------------------------------------------------------------
* jQuery hDialog - 多功能弹出层插件
+-------------------------------------------------------------------
* @version 2.0.2
* @update 2016.03.02
* @author haibao <hhb219@163.com> <http://www.hehaibao.com/>
+-------------------------------------------------------------------
*/
;(function($, window, document, undefined) {	
	var $D = $(document), $W = $(window), $B = $('body');
	methods = {
        init: function (options) {
           	return this.each(function() {
				var T = $(this), _O = $(this).data('hDialog');
                	var defaults = {
		        		title: '',              	    //弹框标题
					box: '#HBox',               //弹框默认选择器
					boxBg: '#ffffff',           //弹框默认背景颜色
					modalBg: 'rgba(0,0,0,0.5)', //遮罩默认背景颜色
					width: 300,                 //弹框默认宽度
					height: 270,                //弹框默认高度
					positions: 'center',        //弹框位置(默认center：居中，top：顶部居中，left：顶部居左，bottom：底部居右)
					effect: 'zoomOut',          //弹框关闭效果(结合animate.css里的动画，默认：zoomOut)
					hideTime: 0,				    //弹框定时关闭(默认0:不自动关闭，以毫秒为单位)
					modalHide: true,            //是否点击遮罩背景关闭弹框(默认true：关闭，false：不可关闭)
					isOverlay: true,            //是否显示遮罩背景(默认true：显示，false：不显示)
		        		escHide: true,              //是否支持ESC关闭弹框(默认true：关闭，false：不可关闭)
		        		autoShow: false,            //是否页面加载完成后自动弹出(默认false点击弹出，true：自动弹出)
		        		types: 1,                   //弹框类型(默认：1正常弹框，2：iframe框架)
		        		iframeSrc: '',              //弹框类型为iframe时使用的 链接地址
		        		iframeId: 'iframeHBox',     //弹框类型为iframe时使用的 ID
		        		beforeShow: function(){},   //显示前的回调方法
		        		afterHide: function(){}     //隐藏后的回调方法
		        	};
				_O = $.extend({}, defaults, options);
				T.data('hDialog', _O);
				_O.autoShow ? methods.open(_O,T) : T.on('click',function() { methods.open(_O,T); });
			});
        },
        open: function (o,T) {
        		var w,h,t,l,m,$close, headTpl = overlayTpl = iframeTpl = '', $o = o.autoShow ? T : $(o.box);
			w = parseInt(o.width) || 300, h = parseInt(o.height) || 270, m = ""+parseInt(-(h/2))+'px 0 0 '+parseInt(-(w/2))+"px";
        		
			//显示前的回调
			this.fire.call(this, o.beforeShow); 
			
			//弹框位置
			switch (o.positions) {
				case 'top': t = 0; l = '50%'; m = "0 0 0 "+parseInt(-(w/2))+"px"; break;
				case 'left': t = l = m = 0; break;
				case 'bottom': t = parseInt($W.height() - h)+'px'; l = parseInt($W.width() - w)+'px'; m = 0; break;
				default: t = l = '50%';
			}

			//弹框标题和关闭按钮
			headTpl = (o.title != '' && $('#HTitle').length <= 0) ? '<div id="HTitle">'+o.title+'</div> <a id="HCloseBtn" title="关闭"><span>&times;</span></a>' : '';
			
			//遮罩背景层
			overlayTpl = (o.isOverlay && $('#HOverlay').length <= 0) ? "<div id='HOverlay' style='width:"+$D.width()+"px;height:"+$D.height()+"px;background-color:"+o.modalBg+";position:fixed;top:0;left:0;z-index:9999;'></div>" : '';
       	 	
			//iframe框架
			iframeTpl = o.types == 2 ? '<iframe id="'+o.iframeId+'" width="'+o.width+'" height="'+o.height+'" frameborder="0" scrolling="auto" src="'+o.iframeSrc+'"></iframe>' : '';
			
        		//显示弹框
        		$B.stop().append(overlayTpl).find($o).css({'background-color':o.boxBg,'position':'fixed','top':t,'left':l,'z-index':999999,'margin':m,'width':o.width,'height':o.height}).removeAttr('class').addClass('animated '+T.attr("class")).prepend(headTpl+iframeTpl).show();
        
        		//默认关闭
        		$close = o.modalHide ? $('#HOverlay,#HCloseBtn') : $('#HCloseBtn');
			$close.on('click',function(){ methods.close(o,T); });
			
			//定时关闭
        		o.hideTime != 0 && setTimeout(function(){ methods.close(o,T); }, parseInt(o.hideTime));
        		
			//支持ESC关闭
			o.escHide && $D.on('keyup',function(){ if(event.keyCode === 27) methods.close(o,T); });
        },
	    close: function (o,T) {
	    		var $o = o.autoShow ? T : $(o.box);
	    		$o.removeAttr('class').addClass('animated '+o.effect);
			$o.hasClass(o.effect) && setTimeout(function(){ $o.removeAttr('style').hide(); },300);
			this.remove('#HOverlay,#HTitle,#'+o.iframeId);
			methods.fire.call(this, o.afterHide);
	    },
        remove: function (a) { $(a).remove(); },
	    fire: function (event, data) { if($.isFunction(event)) { return event.call(this, data); } }
    };
    
	$.fn.hDialog = function (method) {
		if(methods[method]) { return methods[method].apply(this, Array.prototype.slice.call(arguments, 1)); }
		else if(typeof method === 'object' || !method) {return methods.init.apply(this, arguments); }
		else{$.error('Error! Method' + method + 'does not exist on jQuery.hDialog！'); return this; }
	};
	
	/**
	+----------------------------------------------------------
	* 内置扩展
	+----------------------------------------------------------
	*/
	$.extend({  
		/**
		 * @decription 给方法添加加载方法
		 * @param t : string 提示文字
		 * @param w : string 提示框宽度
		 * @param h : string 提示框高度
		 */
	    showLoading: function (t, w, h) { //显示加载
	    		t = t != undefined ? t : '正在加载...'; w = w != undefined ? parseInt(w) : 140; 	h = h != undefined ? parseInt(h) : 48;
	    		var closeBtn = '<a style="position:absolute;top:-5px;right:-5px;width:15px;height:15px;line-height:15px;display:inline-block;border-radius:50%;text-align:center;background-color:rgba(0,0,0,0.5);color:#fff;font-size:10px;" href="javascript:;" id="closeBtn">X</a>', margin = ""+parseInt(-(h/2))+'px 0 0 '+parseInt(-(w/2))+"px";
	        methods.remove('#HLoading');
	        	$B.stop().append('<div id="HLoading" style="width:'+w+'px;height:'+h+'px;line-height:'+h+'px;border-radius:4px;background:rgba(0,0,0,0.6);color:#fff;text-align:center;position:fixed;top:50%;left:50%;margin:'+margin+';">'+t+closeBtn+'</div>');
	   		$('#closeBtn').on('click',function(){ $.hideLoading(); }); //关闭按钮点击事件
	    },
	    hideLoading: function () { //移除加载
        		methods.remove('#HLoading');
        },
        /**
		 * @decription 给方法添加提示方法
		 * @param t1 : string 提示文字
		 * @param t2 : int 提示时间
		 * @param t3 : boolean 提示类型，默认为false
		 * @param callback : 回调函数
		 */
        tooltip: function (t1, t2, t3, callback) {
        		t1 = t1 != undefined ? t1 : 'Error...'; 	t2 = t2 != undefined ? parseInt(t2) : 2500;
	    		var tip = (t3 && t3 != undefined) ? '<div class="HTooltip bounceInDown animated" style="width:280px;padding:10px;text-align:center;background-color:#5cb85c;color:#fff;position:fixed;top:10px;left:50%;z-index:100001;margin-left:-150px;box-shadow:1px 1px 5px #333;-webkit-box-shadow:1px 1px 5px #333;">'+t1+'</div>' : '<div class="HTooltip shake animated" style="width:280px;padding:10px;text-align:center;background-color:#D84C31;color:#fff;position:fixed;top:10px;left:50%;z-index:100001;margin-left:-150px;box-shadow:1px 1px 5px #333;-webkit-box-shadow:1px 1px 5px #333;">'+t1+'</div>';
	    		methods.remove('.HTooltip');
	    		$B.stop().append(tip);
	    		clearTimeout(t);
	    		var t = setTimeout(function() { 
	    			methods.remove('.HTooltip'); 
	    			callback != undefined && methods.fire.call(this, callback); //隐藏后的回调方法 
	    		},t2);
        },
        /**
		 * @decription 返回顶部
		 * @param b : string 和屏幕底部的距离
		 * @param r : string 和屏幕右侧的距离
		 */
        goTop: function (b, r) {
	        	b = b != undefined ? b : '30px'; r = r != undefined ? r : '20px';
	        	methods.remove('#HGoTop');
	        	$B.stop().append('<a id="HGoTop" href="javascript:;" style="width:40px;height:40px;line-height:40px;border-radius:50%;display:inline-block;text-align:center;background:#333;color:#fff;position:fixed;bottom:'+b+';right:'+r+';z-index:100000;">Top</a>').find('#HGoTop').hide();
	        	$T = $('#HGoTop');
	        	$W.on('scroll',function(){
	        		$W.scrollTop() > 150 ? $T.removeAttr('class').addClass('animated rollIn').show() : $T.removeAttr('class').addClass('animated rollOut');
	        	});
	        	$T.on('click',function(){ 
	        		$('body,html').animate({ scrollTop:0 },500); return; //返回顶部按钮点击事件
	        	});
        },
        /**
		 * @decription 消息框
		 * @param type : string 消息框类型(alert/confirm)
		 * @param title : string 消息框标题
		 * @param content : string 消息框内容
		 * @param time : int 消息框自动关闭时间，以毫秒为单位(默认0：不自动关闭)
		 * @param callback : 回调函数
		 */
        dialog: function (type, title, content, time, callback) {
        		var t, tpl = '', footerTpl = '', isLock = false, okText = '确定', cancelText = '取消', width = 260, margin = '0 0 0 '+parseInt(-(width/2))+'px', type = type != undefined ? type : 'alert', time = time != undefined ? parseInt(time) : 0,
        		headerTpl = '<div id="hDialog-header" style="border-bottom:1px solid #ddd;padding:10px;font-size:1.2em;color:#333;">' + title +'</div>',
    			okTpl = '<a href="javascript:;" style="padding:5px 10px;display:inline-block;border-radius:3px;background-color:#337ab7;color:#fff;" id="h_ok">'+ okText +'</a>',
    			cancelTpl = '<a href="javascript:;" style="padding:5px 10px;display:inline-block;border-radius:3px;margin-left:10px;background-color:#eee;color:#333;" id="h_cancel">'+ cancelText +'</a>',
    			contentTpl = '<div id="hDialog-content" style="padding:25px 15px;text-align:center;">' + content + '</div>',
    			maskTpl = '<div id="hDialog-mask" style="width:100%;height:100%;background-color:rgba(0,0,0,0.6);position:fixed;top:0;left:0;z-index:99999;"></div>';  		
    			if(type === 'confirm'){ footerTpl = '<div id="hDialog-footer" style="padding:10px;border-top:1px solid #ddd;text-align:right;">'+ okTpl + cancelTpl +'</div>'; }
    			if(!isLock) tpl += maskTpl;
    			tpl += '<div id="hDialog-wrap" class="animated fadeIn" style="width:'+width+'px;border-radius:5px;box-shadow:4px 4px 10px #666;-webkit-box-shadow:4px 4px 10px #666;background-color:#ffffff;border:1px solid #eee;position:fixed;top:35%;left:50%;margin:'+margin+';z-index:100000;">' + headerTpl + contentTpl + footerTpl + '</div>'; 
    			methods.remove('#hDialog-wrap,#hDialog-mask');
    			$B.stop().append(tpl);
    			if(time !== 0) { clearTimeout(t); t = setTimeout(function() { $.closeDialog(callback); },time); }
    			$('a:contains('+okText+')').on("click", function() { callback != undefined && methods.fire.call(this, callback); });
            $('a:contains('+cancelText+')').on("click", function() { $.closeDialog(); });
        },
        /**
		 * @decription 关闭消息框
		 * @param callback : 回调函数
		 */
        closeDialog: function (callback) {
        		methods.remove('#hDialog-wrap,#hDialog-mask'); 
        		callback != undefined && methods.fire.call(this, callback);
        }
	});
})(jQuery, window, document);
