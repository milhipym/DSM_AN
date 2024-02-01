// Avoid `console` errors in browsers that lack a console.
(function() {
	var method;
	var noop = function () {};
	var methods = [
		'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
		'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
		'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
		'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'
	];
	var length = methods.length;
	var console = (window.console = window.console || {});

	while (length--) {
		method = methods[length];

		// Only stub undefined methods.
		if (!console[method]) {
			console[method] = noop;
		}
	}
}());

// cookie plugin
!function(e){var n=!1;if("function"==typeof define&&define.amd&&(define(e),n=!0),"object"==typeof exports&&(module.exports=e(),n=!0),!n){var t=window.Cookies,o=window.Cookies=e();o.noConflict=function(){return window.Cookies=t,o}}}(function(){function e(){for(var e=0,n={};e<arguments.length;e++){var t=arguments[e];for(var o in t)n[o]=t[o]}return n}function n(t){function o(n,r,i){var c;if("undefined"!=typeof document){if(arguments.length>1){if(i=e({path:"/"},o.defaults,i),"number"==typeof i.expires){var a=new Date;a.setMilliseconds(a.getMilliseconds()+864e5*i.expires),i.expires=a}try{c=JSON.stringify(r),/^[\{\[]/.test(c)&&(r=c)}catch(s){}return r=t.write?t.write(r,n):encodeURIComponent(String(r)).replace(/%(23|24|26|2B|3A|3C|3E|3D|2F|3F|40|5B|5D|5E|60|7B|7D|7C)/g,decodeURIComponent),n=encodeURIComponent(String(n)),n=n.replace(/%(23|24|26|2B|5E|60|7C)/g,decodeURIComponent),n=n.replace(/[\(\)]/g,escape),document.cookie=[n,"=",r,i.expires?"; expires="+i.expires.toUTCString():"",i.path?"; path="+i.path:"",i.domain?"; domain="+i.domain:"",i.secure?"; secure":""].join("")}n||(c={});for(var p=document.cookie?document.cookie.split("; "):[],u=/(%[0-9A-Z]{2})+/g,d=0;d<p.length;d++){var f=p[d].split("="),l=f.slice(1).join("=");'"'===l.charAt(0)&&(l=l.slice(1,-1));try{var m=f[0].replace(u,decodeURIComponent);if(l=t.read?t.read(l,m):t(l,m)||l.replace(u,decodeURIComponent),this.json)try{l=JSON.parse(l)}catch(s){}if(n===m){c=l;break}n||(c[m]=l)}catch(s){}}return c}}return o.set=o,o.get=function(e){return o.call(o,e)},o.getJSON=function(){return o.apply({json:!0},[].slice.call(arguments))},o.defaults={},o.remove=function(n,t){o(n,"",e(t,{expires:-1}))},o.withConverter=n,o}return n(function(){})});

//jQuery.noConflict();
!function($) {
	var etUI = {};
	var etUtil = {};
	window.etUI = etUI;
	window.etUtil = etUtil;

	// mac os 일 경우 html 태그에 mac_os 클래스 붙임
	if (navigator.userAgent.indexOf('Mac OS X') != -1) {
		$("html").addClass("mac_os");
	}

	etUtil.isIE8 = $('html').hasClass('ie8');
	etUtil.isIE = (function detectIE() {
		var ua = window.navigator.userAgent;

		// Test values; Uncomment to check result …

		// IE 10
		// ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';
		// IE 11
		// ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';
		// Edge 12 (Spartan)
		// ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';
		// Edge 13
		// ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';

		var msie = ua.indexOf('MSIE ');
		if (msie > 0) {
			// IE 10 or older => return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
		}

		var trident = ua.indexOf('Trident/');
		if (trident > 0) {
			// IE 11 => return version number
			var rv = ua.indexOf('rv:');
			return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
		}

		var edge = ua.indexOf('Edge/');
		if (edge > 0) {
			// Edge (IE 12+) => return version number
			return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
		}

		// other browser
		return false;
	})();

	etUtil.isIOS = (/iPad|iPhone|iPod/.test(navigator.userAgent || navigator.vendor || window.opera) && !window.MSStream);

	$(function(){
// loadInitFn()
		// setTimeout(function(){
		etUI.initUI.setup();
		// },500)

		// 삭제 필요
		// $(document).ready(function(){
		// 	var mySwiper = new Swiper('.swiper-container',{
		// 		speed: 400,
		// 		spaceBetween : 100,
		// 		pagination: '.swiper-pagination',
		// 		paginationClickable: true
		// 	});
		// });
		// 퍼블리싱 전용 (주의!!! 개발 완료시 모두 삭제)/////////////////////////////
		//  || location.port == '20001'
		if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
			header.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			footer.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			// footer_kcun.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
		}
	});

	etUI.initUI = (function(){
		var isLoaded;

		function setup(){
			if(location.href.indexOf('dsm/codinglist.html') == -1){
				if(isLoaded){
					return;
				}
			}
			// isLoaded = true;

			// 공통 적용
			etUI.textPlaceHolderInit(); // ie7,8 가능한 placeholder

			$('.ui_slider').each(function(idx, obj){
				var _inst = new etUI.Slider().init(obj);
				$(this).data('_inst', _inst);
			});
			setTimeout(function(){
				etUI.uiAllMenu.init();
				etUI.floatingMenu.init();
			},100)
			etUI.swiperCom.init();
		}

		return {
			setup: setup
		};
	})();

	// placeholder
	etUI.textPlaceHolderInit = function(_selector){

		var havePlaceholder = false;
		var input = document.createElement('input');
		havePlaceholder = ('placeholder' in input);
		var selectEl;

		if(_selector && _selector.length > 0){
			selectEl = _selector.find('input[type=text], textarea, input[type=password], textarea');
		}else{
			selectEl = $('input[type=text], textarea, input[type=password], textarea');
		}

		if(!havePlaceholder){
			selectEl.each(function(idx, obj){
				var _this = $(this);
				var placeholderAttr = 'placeholder';

				var placeholderText = _this.attr(placeholderAttr);

				/*
				if(_this.val() == ''){
					_this.val(placeholderText);
				}
				*/
				if(_this.prev('.placeholder_guidetext').length <= 0){
					_this.wrap('<span class="placeholder_wrap" style="display:inline-block;position:relative;"></span>');

					if(_this.hasClass('input_b')){
						_this.before('<span class="placeholder_guidetext bold"></span>');
					}else{
						_this.before('<span class="placeholder_guidetext"></span>');
					}

					var prevGuideText = _this.prev('.placeholder_guidetext');
					prevGuideText.text(placeholderText);
					prevGuideText.hide();
					if(_this.val() == ''){
						prevGuideText.show();
					}

					if(_this.css('text-align') == 'right'){
						prevGuideText.css({left: 'auto', right: 13});
					}

					prevGuideText.addClass('placeholder_text');

					_this.bind('mousedown focusin', function(e){
						if(!$(this).attr('disabled') || !$(this).attr('readonly')){
							prevGuideText.hide();
						}
					}).bind('focusout', function(e){
						if($(this).val() == ''){
							prevGuideText.show();
						}
					});

					prevGuideText.bind('mousedown', function(e){
						if(!$(this).next(input).attr('disabled') || !$(this).next(input).attr('readonly')){
							$(this).hide();
						}
						setTimeout(function(){
							_this.focus();
						}, 100);
					});
				}
			});
		}
	};

	// header include
	var header = (function(){
		var el;

		function init(){
			el = $('#header');

			if(el.length && el.children().length <= 0){
				window.header = $.get('/dsm/inc/layout/header.html');
				window.header.done(function(data){
					el.html(data);

					setTimeout(function(){
						complete();
					}, 0);
				});
			}else{
				complete();
			}
		}

		function complete(){
			// etUI.headerCommon.init();
		}

		return {init: init};
	})();

	// footer include
	var footer = (function(){
		var el;

		function init(){
			el = $('#footer');

			if(el.length && el.children().length <= 0){
				window.footer = $.get('/dsm/inc/layout/footer.html');
				window.footer.done(function(data){
					el.html(data);

					setTimeout(function(){
						complete();
					}, 0);
				});
			}else{
				complete();
			}
		}

		function complete(){
			/*
			ex)

			familysite등의 footer에 속한 스크립트는 footer안에서 서술
			*/

			// etUI.footerCommon.init();
		}

		return {init: init};
	})();

	etUI.globalCommonScript = (function(){
		function init(){
			// etUI.headerCommon.init();
			// etUI.footerCommon.init();
		}

		return {
			init: init
		};
	})();

	// selectbox
	etUI.SelectboxUI = function(){
		var el, selTitle;
		var $text, $list, $select;
		var selectedIndex, htmlSelectList, selectListTimer = -1;

		function init(_el){
			el = $(_el);

			setup();

			el.addClass('ui_complete');

			return this;
		}

		function setup(){
			$text = el.find('> .select_result');
			$list = el.find('>ul');
			$select = el.find('>select');
			selectedIndex = -1;

			htmlSelectList = '';

			if($select.attr('disabled')){
				el.addClass('disabled');
			}else{
				el.removeClass('disabled');
			}

			setList();

			setSelectedIndex(selectedIndex);

			bindEvents();
		}

		function bindEvents(){
			$select.on('change', function(e){
				setSelectedIndex();
			});

			if(el.hasClass('disabled')){
				$text.on('click', function(e){
					e.preventDefault();
				});
			}else{
				$text.on('click', function(e){
					e.preventDefault();

					if(!el.hasClass('active')){
						showList();
					}else{
						hideList();
					}
				});
			}
		}

		function showList(){
			el.addClass('active');

			var windowInnerHeight = window.innerHeight || $(window).height();
			if(!el.data('origin-top')){
				el.data('origin-top', $list.css('top'));
			}

			if(el.hasClass('reversal')){
				$list.addClass('reversal');
			}else{
				if(windowInnerHeight > $list.height() && el.offset().top + el.height() + $list.height() - $(window).scrollTop() > windowInnerHeight){
					// $list.css({top: -$list.height()});
					$list.addClass('reversal');
				}else{
					$list.removeClass('reversal');//$list.css({top: el.data('origin-top')});
				}
			}

			$list.show().css({zIndex: 10});
			setTimeout(function(){
				$list.addClass('on');
				if($list.find('>li[data-selected=true]').length > 0){
					$list.find('>li[data-selected=true] a').focus();
				}
			}, 0);

			$list.off('.listEvent').on('click.listEvent', '>li>a', function(e){
				e.preventDefault();

				var index = $(this).closest('li').index();

				$select.get(0).selectedIndex = index;

				$select.trigger('change');
				setSelectedIndex();
				hideList();
			}).on('focusin.listEvent', function(e){
				clearTimeout(selectListTimer);

				$list.find('>li').removeClass('on');
				$(e.target).closest('li').addClass('on');
			}).on('focusout.listEvent', function(e){
				// selectListTimer = setTimeout(function(){
				// 	hideList(true);
				// }, 100);
			}).on('mouseover.listEvent', function(e){
				$list.find('>li').removeClass('on');
				$(e.target).closest('li').addClass('on');
			});

			$('body').off('mousedown').on('mousedown.listEvent', function(e){
				if($(e.target).closest(el).length <= 0){
					hideList(true);
				}
			});

			$(document).off('keyup').on('keyup.listEvent', function(e){
				if(e.keyCode == 27){
					hideList();
				}
			});

			$list.off('mousewheel.listEvent DOMMouseScroll.listEvent').on('mousewheel.listEvent DOMMouseScroll.listEvent', function(e){
				var delta = e.originalEvent.wheelDelta || -e.originalEvent.detail;

				if(delta > 0 && $(this).scrollTop() <= 0){
					return false;
				}
				if(delta < 0 && $(this).scrollTop() >= this.scrollHeight - $(this).height()){
					return false;
				}

				return true;
			});
		}

		function hideList(notFocus){
			el.removeClass('active');
			$list.hide().css({zIndex: 5}).removeClass('on');

			$list.off('.listEvent');
			if(!notFocus){
				$text.focus();
			}

			$('body').off('.listEvent');
			$(document).off('.listEvent');
			$list.off('.listEvent');
		}

		function setList(){
			htmlSelectList += '<a href="#" class="select_result"></a>';

			htmlSelectList += '<ul class="sel_list">';

			$select.find('>option').each(function(idx, obj){
				var value = $(this).attr('value');
				if(value){
					value = ' data-value="' + value + '"';
				}else{
					value = '';
				}
				htmlSelectList += '<li><a href="#"' + value + '>' + $(this).text() + '</a></li>';
			});
			htmlSelectList += '</ul>';

			$list.remove();

			el.find('> .select_result').remove();
			el.append(htmlSelectList);

			$list = el.find('>ul');
			$text = el.find('> .select_result');
			$list.width(el.width());
			$text.width(el.width() - 22-8);

			selTitle = el.find('select').attr('title');
			// if(selTitle.indexOf('선택') == -1){
			// 	selTitle = selTitle + ' 선택'
			// }
			$text.attr('title', selTitle);

			if($select.find('>option').length > 6){
				$list.css({height: 252});
			}else{
				$list.css({height: 'auto'});
			}
		}

		function setSelectedIndex(){
			if($select.length <= 0){
				selectedIndex = 0;
			}else{
				selectedIndex = $select.get(0).selectedIndex;
			}

			$text.text($list.find('>li>a').eq(selectedIndex).text());
			$list.find('>li').removeClass('on').eq(selectedIndex).addClass('on');
			$list.find('>li').attr({'data-selected': false}).eq(selectedIndex).attr({'data-selected': true});
		}

		function refresh(){
			setup();
		}

		return {
			init: init
			, refresh: refresh
		};
	};

	// form controls
	etUI.formControl = (function(){
		// var selectEl, checkEl, radioEl;

		function init(){
			// selectEl = $('.selectbox_wrap:not(.ui_complete)');
			// checkEl = $('.input_checkbox:not(.ui_complete)');
			// radioEl = $('.input_radio:not(.ui_complete)');

			initSelect();
		}

		function initSelect(){
			$('.selectbox_wrap').each(function(idx, obj){
				var $obj = $(obj);
				var $select = $obj.find('select');

				if($obj.hasClass('ui_complete')){
					if($select.data('selectbox')) $select.data('selectbox').refresh();
				}else{
					var selectbox = new etUI.SelectboxUI();
					selectbox.init($obj);
					$select.data('selectbox', selectbox);
				}
			});
		}

		return {
			init: init
			, initSelect: initSelect
		}
	})();

	etUI.Slider = function(){
		var el, btnLeft, btnRight, sliderWrap, sliderBody, sliderItem, paging;
		var currentId, pageSize, pastId, activeItemIndex;
		var sliderWidth;
		var lastItemWidth;
		var options;
		var btnDot, btnPlay, isPlay, direction;
		var autoTimer, autoTimerSec, currentClass;

		function init(_el, _options){
			if(!el){
				el = $(_el);
			}

			if(el.hasClass('ui_complete')){
				return this;
			}

			if(!el.attr('data-lazy-load') || el.attr('data-lazy-load') && !el.attr('data-lazy-load-ok')){
				options = {
					speed: 500
					, entire: true
					, fade: false
				};

				if(el.attr('data-slider-entire') != undefined){
					options.entire = el.attr('data-slider-entire');
				}

				if(el.attr('data-slider-fade') != undefined){
					options.fade = el.attr('data-slider-fade');
				}

				if(el.attr('data-slider-infinite') != undefined){
					options.infinite = el.attr('data-slider-infinite');
				}

				if(el.attr('data-slider-initno') != undefined){
					options.initno = el.attr('data-slider-initno');
				}

				if(el.attr('data-slider-time') != undefined){
					options.autoTimerSec = el.attr('data-slider-time');
				}

				options = $.extend(true, options, _options);

				currentId = 0;
				pastId = 0;
				btnLeft = el.find('.ui_btn_prev');
				btnRight = el.find('.ui_btn_next');
				sliderWrap = el.find('.ui_slider_wrap');
				sliderBody = el.find('.ui_slider_body');
				sliderItem = sliderBody.find('.ui_slider_item');
				btnDot = el.find('.ui_btn_dot');
				btnPlay = el.find('.ui_btn_play');
				paging = el.find('.ui_paging');
				if(options.autoTimerSec){
					autoTimerSec = options.autoTimerSec;
				}else{
					autoTimerSec = 3000;
				}

				if(btnPlay.length){
					isPlay = true;
				}
			}

			sliderWidth = sliderWrap.width();
			pageSize = sliderItem.length;
			sliderBody.css({width: (pageSize*100)+'%'});
			lastItemWidth = sliderItem.eq(pageSize-1).width();

			if(paging.length){
				setPaging();
			}

			if(el.attr('data-lazy-load') && !el.attr('data-lazy-load-ok')){
				el.attr('data-lazy-load-ok', true);

				if(!el.is(':visible')){
					return this;
				}
			}

			if(options.infinite && pageSize > 1){
				if(etUtil.isIE8){
					var tempSlideBodyWidth = sliderItem.eq(0).width();
					var tempSlideBodyHeight = sliderItem.eq(0).height();

					// TweenMax.set(sliderItem, {position: 'absolute', left: 0, width: tempSlideBodyWidth});
					sliderItem.velocity({position: 'absolute', left: 0, width: tempSlideBodyWidth}, {duration: 0});

					sliderBody.height(tempSlideBodyHeight);
					for(var i = 1 ; i < pageSize ; i++){
						// TweenMax.set(sliderItem.eq(i), {left: -sliderWidth});
						sliderItem.eq(i).velocity({left: -sliderWidth}, {duration: 0});
					}
				}else{
					for(var i = 1 ; i < pageSize ; i++){
						// TweenMax.set(sliderItem.eq(i), {marginLeft: -sliderWidth});
						sliderItem.eq(i).css({marginLeft: -sliderWidth});
					}

					// TweenMax.set(sliderItem, {x: sliderWidth});
					// TweenMax.set(sliderItem.eq(0), {x: 0});
					// TweenMax.set(sliderItem.eq(-1), {x: -sliderWidth});
					sliderItem.velocity({translateX: sliderWidth}, {duration: 0});
					sliderItem.eq(0).velocity({translateX: 0}, {duration: 0});
					sliderItem.eq(-1).velocity({translateX: -sliderWidth}, {duration: 0});
				}
			}

			// 한페이지가 안될경우
			if(pageSize <= 1){
				btnLeft.hide();
				btnRight.hide();

				// 자동재생 오른쪽 정렬
				if(el.find('.btn_auto').length){
					el.find('.btn_auto').addClass('alone');
				}
			}

			// 자동재생 정렬 후 나타남
			if(el.find('.btn_auto').length){
				el.find('.btn_auto').show();
			}

			disableBtn();

			bindEvents();

			if(btnDot.length){
				var dotHTML = '';
				sliderItem.each(function(idx, obj){
					dotHTML += '<li class="ui_dot"><a href="">총 '+pageSize+'개 컨텐츠 중 '+(idx+1)+'번째</a></li>\n';
				});
				btnDot.prepend(dotHTML);
				btnDot.find('.ui_dot:eq(0)').addClass('on');
				initDot();

				btnDot.show();
			}

			if(btnPlay.length){
				btnPlay.show();
				initPlay();
			}

			el.addClass('ui_complete');

			if(sliderItem.find('>li.on').length){
				activeItemIndex = sliderItem.find('>li.on').closest('.ui_slider_item').index();
				currentId = activeItemIndex;
				moveSlide(0);
			}else{
				if(options.initno){
					currentId = options.initno;
					moveSlide(0);
				}else{
					loadImages(0);
				}

				addCurrentClass(currentId);
			}

			return this;
		}

		function bindEvents(){
			btnLeft.on('click', function(e){
				e.preventDefault();

				moveLeft();
				moveDot(currentId);
			});

			btnRight.on('click', function(e){
				e.preventDefault();

				moveRight();
				moveDot(currentId);
			});
		}

		function setPaging(){
			paging.html('<strong>'+(currentId+1)+'</strong>/'+ pageSize);
		}

		function moveLeft(){
			direction = 'LEFT';
			if(el.find('.velocity-animating').length) return;

			if(options.infinite){
				if(currentId > 0){
					--currentId;
				}else{
					currentId = pageSize-1;
				}
				moveSlide();
			}else{
				if(currentId > 0){
					--currentId;
				}else{
					currentId = 0;
				}
				moveSlide();
			}
		}

		function moveRight(){
			direction = 'RIGHT';
			if(el.find('.velocity-animating').length) return;

			if(options.infinite){
				if(currentId < pageSize-1){
					++currentId;
				}else{
					currentId = 0;
				}
				moveSlide();
			}else{
				if(currentId < pageSize-1){
					++currentId;
				}else{
					currentId = pageSize-1;
				}
				moveSlide();
			}
		}

		function initDot(){
			btnDot.find('.ui_dot').on('click', function(e){
				e.preventDefault();
				if(el.find('.velocity-animating').length) return;
				var index = $(this).index();

				if(currentId == index) return;

				moveDot(index);

				if(currentId == 0 && index == pageSize-1){
					direction = 'LEFT';
				}else if(currentId == pageSize-1 && index == 0){
					direction = 'RIGHT';
				}else{
					if(currentId > index){
						direction = 'LEFT';
					}else{
						direction = 'RIGHT';
					}
				}
				currentId = index;

				moveSlide();
			});
		}

		function moveDot(index){
			btnDot.find('.ui_dot').removeClass('on').eq(index).addClass('on');
		}

		function play(){
			autoTimer = setInterval(function(){
				if(document.hasFocus()){
					direction = 'RIGHT';
					currentId = (currentId+1)%pageSize;
					// console.log(pastId, currentId);
					btnDot.find('.ui_dot').removeClass('on').eq(currentId).addClass('on');
					moveSlide();
				}
			}, autoTimerSec);
		}

		function stop(){
			clearInterval(autoTimer);
		}

		function initPlay(){
			el.on('mouseenter', function(e){
				stop();
			}).on('mouseleave', function(e){
				if(isPlay){
					stop();
					play();
				}
			});

			btnPlay.on('click', 'a', function(e){
				e.preventDefault();

				if($(this).hasClass('play')){
					$(this).removeClass('play');

					play();
					isPlay = true;
				}else{
					$(this).addClass('play');

					stop();
					isPlay = false;
				}
			});

			if(pageSize <= 1){
				btnPlay.hide();
				btnDot.hide();
			}else{
				play();
				isPlay = true;
			}
		}

		function moveSlide(_speed){
			var speed;
			sliderWrap.scrollLeft(0);
			setPaging();
			if(options.fade){
				if(_speed === 0){
					speed = _speed;
				}else{
					speed = options.speed*2;
				}

				stop();
				if(isPlay){
					play();
				}

				addCurrentClass(currentId);

				// TweenMax.to(sliderItem.eq(pastId), speed, {autoAlpha: 0, ease: Power2.easeInOut});
				// TweenMax.to(sliderItem.eq(currentId), speed, {autoAlpha: 1, ease: Power2.easeInOut});
				sliderItem.eq(pastId).velocity({opacity: 0}, {duration: speed});
				sliderItem.eq(currentId).velocity({opacity: 1}, {duration: speed});
			}else if(options.infinite){
				if(_speed === 0){
					speed = _speed;
				}else{
					speed = options.speed;
				}

				addCurrentClass(currentId);

				if(etUtil.isIE8){
					if(direction == 'LEFT'){
						// TweenMax.set(sliderItem.eq(pastId), {left: 0});
						// TweenMax.set(sliderItem.eq(currentId), {left: -sliderWidth});
						// TweenMax.to(sliderItem.eq(pastId), speed, {left: sliderWidth, ease: Power2.easeInOut});
						// TweenMax.to(sliderItem.eq(currentId), speed, {left: 0, ease: Power2.easeInOut});
						sliderItem.eq(pastId).velocity({left: 0}, {duration: 0});
						sliderItem.eq(currentId).velocity({left: -sliderWidth}, {duration: 0});
						sliderItem.eq(pastId).velocity({left: sliderWidth}, {duration: speed});
						sliderItem.eq(currentId).velocity({left: 0}, {duration: speed});
					}else if(direction == 'RIGHT'){
						// TweenMax.set(sliderItem.eq(pastId), {left: 0});
						// TweenMax.set(sliderItem.eq(currentId), {left: sliderWidth});
						// TweenMax.to(sliderItem.eq(pastId), speed, {left: -sliderWidth, ease: Power2.easeInOut});
						// TweenMax.to(sliderItem.eq(currentId), speed, {left: 0, ease: Power2.easeInOut});
						sliderItem.eq(pastId).velocity({left: 0}, {duration: 0});
						sliderItem.eq(currentId).velocity({left: sliderWidth}, {duration: 0});
						sliderItem.eq(pastId).velocity({left: -sliderWidth}, {duration: speed});
						sliderItem.eq(currentId).velocity({left: 0}, {duration: speed});
					}
				}else{
					if(direction == 'LEFT'){
						// TweenMax.set(sliderItem.eq(pastId), {x: 0});
						// TweenMax.set(sliderItem.eq(currentId), {x: -sliderWidth});
						// TweenMax.to(sliderItem.eq(pastId), speed, {x: sliderWidth, ease: Power2.easeInOut});
						// TweenMax.to(sliderItem.eq(currentId), speed, {x: 0, ease: Power2.easeInOut});
						sliderItem.eq(pastId).velocity({translateX: 0}, {duration: 0});
						sliderItem.eq(currentId).velocity({translateX: -sliderWidth}, {duration: 0});
						sliderItem.eq(pastId).velocity({translateX: sliderWidth}, {duration: speed});
						sliderItem.eq(currentId).velocity({translateX: 0}, {duration: speed});
					}else if(direction == 'RIGHT'){
						// TweenMax.set(sliderItem.eq(pastId), {x: 0});
						// TweenMax.set(sliderItem.eq(currentId), {x: sliderWidth});
						// TweenMax.to(sliderItem.eq(pastId), speed, {x: -sliderWidth, ease: Power2.easeInOut});
						// TweenMax.to(sliderItem.eq(currentId), speed, {x: 0, ease: Power2.easeInOut});
						sliderItem.eq(pastId).velocity({translateX: 0}, {duration: 0});
						sliderItem.eq(currentId).velocity({translateX: sliderWidth}, {duration: 0});
						sliderItem.eq(pastId).velocity({translateX: -sliderWidth}, {duration: speed});
						sliderItem.eq(currentId).velocity({translateX: 0}, {duration: speed});
					}
				}
			}else{
				if(_speed === 0){
					speed = _speed;
				}else{
					speed = options.speed;
				}

				// console.log(currentId, pageSize);
				if(currentId >= pageSize-1){
					if(options.entire){
						addCurrentClass(currentId);

						if(etUtil.isIE8){
							// TweenMax.to(sliderBody, speed, {marginLeft: (-currentId) * sliderWidth, ease: Power2.easeInOut});
							sliderBody.velocity({marginLeft: (-currentId) * sliderWidth}, {duration: speed});
						}else{
							// TweenMax.to(sliderBody, speed, {x: (-currentId) * sliderWidth, ease: Power2.easeInOut});
							sliderBody.velocity({translateX: (-currentId) * sliderWidth}, speed);
						}
					}else{
						if(lastItemWidth == sliderWidth){
							addCurrentClass(currentId);
						}else{
							addCurrentClass(currentId-1);
							addCurrentClass(currentId, 'last_current');
						}
						if(etUtil.isIE8){
							// TweenMax.to(sliderBody, speed, {marginLeft: (-currentId+1) * sliderWidth - lastItemWidth + (currentId*1), ease: Power2.easeInOut});
							sliderBody.velocity({marginLeft: (-currentId+1) * sliderWidth - lastItemWidth + (currentId*1)}, {duration: speed});
						}else{
							// TweenMax.to(sliderBody, speed, {x: (-currentId+1) * sliderWidth - lastItemWidth + (currentId*1), ease: Power2.easeInOut});
							sliderBody.velocity({translateX: (-currentId+1) * sliderWidth - lastItemWidth + (currentId*1)}, {duration: speed});
						}
					}
				}else{
					addCurrentClass(currentId);

					if(etUtil.isIE8){
						if(options.entire){
							// TweenMax.to(sliderBody, speed, {marginLeft: -currentId * sliderWidth, ease: Power2.easeInOut});
							sliderBody.velocity({marginLeft: -currentId * sliderWidth}, {duration: speed});
						}else{
							// TweenMax.to(sliderBody, speed, {marginLeft: -currentId * sliderWidth + (currentId*1), ease: Power2.easeInOut});
							sliderBody.velocity({marginLeft: -currentId * sliderWidth + (currentId*1)}, {duration: speed});
						}
					}else{
						if(options.entire){
							// TweenMax.to(sliderBody, speed, {x: -currentId * sliderWidth, ease: Power2.easeInOut});
							sliderBody.velocity({translateX: -currentId * sliderWidth}, {duration: speed});
						}else{
							// TweenMax.to(sliderBody, speed, {x: -currentId * sliderWidth + (currentId*1), ease: Power2.easeInOut});
							sliderBody.velocity({translateX: -currentId * sliderWidth + (currentId*1)}, {duration: speed});
						}
					}
				}
			}

			disableBtn();

			if(lastItemWidth == sliderWidth){
				loadImages(currentId);
			}else{
				loadImages(currentId);
				loadImages(currentId-1);
			}


			pastId = currentId;
		}

		function addCurrentClass(index, etcClass){
			currentClass = 'current';
			if(etcClass){
				currentClass = etcClass;
				sliderItem.removeClass(currentClass).eq(index).addClass(currentClass);
			}else{
				sliderItem.removeClass('current last_current').eq(index).addClass(currentClass);
			}
		}

		function loadImages(index){
			sliderBody.find('.ui_slider_item').eq(index).find('img[data-src]').each(function(idx, obj){
				$(this).attr({src: $(this).attr('data-src')}).removeAttr('data-src');
			});
		}

		function disableBtn(){
			if(pageSize <= 1){
				btnLeft.addClass('disabled');
				btnRight.addClass('disabled');
			}else if(currentId <= 0){
				btnLeft.addClass('disabled');
				btnRight.removeClass('disabled');
			}else if(currentId >= pageSize-1){
				btnRight.addClass('disabled');
				btnLeft.removeClass('disabled');
			}else{
				btnLeft.removeClass('disabled');
				btnRight.removeClass('disabled');
			}
		}

		return {
			init: init
			, moveLeft: moveLeft

		};
	};

	/**
	 * @module [공통]uiAllMenu
	 * @description 전체 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.uiAllMenu.init();
	 */
	etUI.uiAllMenu = (function(){
		var el, menuBox, elBox, el2Dep, el2DepSub1, el2DepSub2

		function init(){
			el = $('.utill_activity');
			activityMBox = $('.allmenu_bx');
			elBox = $('.allmenu_gnb')
			el2Dep = elBox.find('.gnb_2depth');
			el2DepSub1 = el2Dep.find('.sub_depth1');
			el2DepSub2 = el2DepSub1.find('.sub_depth2');

			// $('.side_bar,.side_menu').css({zIndex:106});

			bindEvents();

			return this;
		}

		function setup(){
			var el2DepHeight = el2Dep.height();
			var el2DepSub1Height = el2DepSub1.height();
			var sub1LastHeight = el2DepSub1.find('>li:last').height();
			var scrollHeight = (el2DepHeight - sub1LastHeight) + el2DepSub1Height;

			setTimeout(function(){
				el2DepSub1.css({height:scrollHeight});
			},500)
		}

		function bindEvents(){
			//전체 메뉴 open
			el.on('click', '.ui_allmenu', function(e){
				e.preventDefault();
				// var menuBox = $(this).closest('.all_menu').find('.allmenu_bx');

				// activityMBox.addClass('open');
				$('.allmenu_bx').find('.dimm').show();
				// // $('.allmenu_bx').show();
				new TimelineMax()
				.set('.allmenu_gnb', {left:-450}, .0)
				.set('.gnb_top', {left:-225}, .0)
				.set('.gnb_1depth', {left:-125}, .0)
				.set('.gnb_body', {width:0, marginLeft:-325}, .0)
				.set('.allmenu_bx', {display:'block'}, .12)
				.to('.allmenu_gnb', 0.37, {display:'block', left:0, ease: Power2.easeOut}, .12)
				.to('.gnb_top', 0.37, {left:0, ease: Power2.easeOut}, .12)
				.to('.gnb_1depth', 0.3, {left:0, ease: Power2.easeOut}, .1)
				.to('.gnb_body', 0.5, {width:325, marginLeft:0, ease: Power2.easeOut}, .2)
				// .to('.allmenu_bx', 0.37, {display:'block'}, .13)
				.addCallback(function(){setup()}, .37)
				// TweenMax.set('.allmenu_gnb', {autoAlpha:0, right:-551}, .1);
				// TweenMax.to('.allmenu_gnb', 0.37, {autoAlpha:1, display:'block', right:0, ease: Power2.easeOut}, .15);
			});

			var ignoreScroll = false;
			// 메뉴 스크롤
			el2Dep.off('touchmove.evtScroll touchend.evtScroll scroll.evtScroll').on('touchmove.evtScroll touchend.evtScroll scroll.evtScroll', function(e){
				var $this = $(this);
				var objScroll = $this.scrollTop();
				var intersection = 0;

				if(ignoreScroll){
					return;
				}

				var menuCurrTop =  function(cnt){
					var query = [];
					$this.find('.sub_depth1 > li').each(function(index, obj){
						query.push($(obj).outerHeight());
					});

					for(var i = 0 ; i < (cnt+1) ; i++){
						intersection += query[i];
					}

					return intersection;

				}

				var offsetObj01 = menuCurrTop(0);
				var offsetObj02 = menuCurrTop(1) - offsetObj01;
				var offsetObj03 = menuCurrTop(2) - offsetObj01 - offsetObj02;
				var offsetObj04 = menuCurrTop(3) - offsetObj01 - offsetObj02 - offsetObj03;
				var offsetObj05 = menuCurrTop(4) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04;

				if(objScroll < offsetObj01 - 40){
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(0).addClass('on');
				}
				else if(objScroll < offsetObj02 - 40){
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(1).addClass('on');
				}
				else if(objScroll < offsetObj03 - 40){
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(2).addClass('on');
				}
				else if(objScroll < offsetObj04 - 40){
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(3).addClass('on');
				}
				else if(objScroll < offsetObj05 - 40){
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(4).addClass('on');
				}
				else{
					elBox.find('.gnb_1depth li').removeClass('on');
					elBox.find('.gnb_1depth li').eq(5).addClass('on');
				}
			});

			// 메뉴 클릭
			elBox.find('.gnb_1depth li a').off('click').on('click', function(e){
				e.preventDefault();
				var $this = $(this);
				var idx = $this.closest('li').index();
				var intersection = 0;
				var index = $(this).closest('li').index();

				elBox.find('.gnb_1depth li').removeClass('on');
				$this.closest('li').addClass('on');

				ignoreScroll = true;

				var position =  function(){
					var query = [];
					el2Dep.find('.sub_depth1 > li').each(function(index, obj){
						query.push($(obj).outerHeight());
					});

					for(var i = 0 ; i < idx ; i++){
						intersection += query[i];
					}

					return intersection;

				}

				var targetPosition = position();
				TweenMax.to($('.gnb_2depth'), 0.3, {scrollTop:targetPosition, onComplete : function(){
					ignoreScroll = false;
				}});

			});

			el2DepSub2.off('click').on('click', '>li>a', function(e){
				e.preventDefault();

				if($(this).closest('li').hasClass('on')){
					$(this).next('ul.sub_depth3').slideUp();
					$(this).closest('li').removeClass('on');
				}else{
					$(this).next('ul.sub_depth3').slideDown();
					$(this).closest('li').addClass('on');
				}
			})

			$('.allmenu_bx').on('click', '.allmenu_close', function(e){
				e.preventDefault();
				allmenuClose();
			});

		}

		function allmenuClose(){
		// 	$('.allmenu_bx').removeClass('open');
		// 	// $('.allmenu_bx').css({zIndex:101})
			TweenMax.to('.allmenu_gnb', 0.37, {left:-450, ease: Power2.easeOut, onComplete: function(){
				$('.allmenu_gnb').hide();
				$('.gnb_body').removeAttr('style');
				el2DepSub1.removeAttr('style');
			}});
			$('.dimm').hide();
		}

		return {
			init: init
		};
	})();

	/**
	 * @module [공통]floatingMenu
	 * @description 프로팅 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.floatingMenu.init();
	 */
	etUI.floatingMenu = (function(){
		var el;

		function init(){
			el = $('.floting_btn');
			icons = el.find('.sub_floating > .sub_floating, .activity > .save, .sub_floating > .share, .sub_floating > .bookmark').get().reverse();
			bindEvent();
		}

		function bindEvent(){

			el.on('click', '>.btn_floating', function(e){
				e.preventDefault();

				if(el.find('>.btn_floating').hasClass('close')){
					new TimelineMax()
					.to(el.find('>.btn_floating'), .3, {scale:.9}, 0)
					.to(el.find('>.btn_floating'), .3, {scale:1}, .1)
					.fromTo(icons, .2, {autoAlpha:1, y:0},  {autoAlpha:0, y: function (i) {
						return (i + 1) * 65
					}}, .2)
					.addCallback(function () {
						$('.sub_floating').removeClass('on');
						el.find('>.btn_floating').removeClass('close');
					},.38)
				}else{
					new TimelineMax()
					.to(el.find('>.btn_floating'), .3, {scale:.9}, 0)
					.to(el.find('>.btn_floating'), .3, {scale:1}, .1)
					.staggerFromTo(icons, .2, {autoAlpha:0, y:"+=65"}, {autoAlpha:1, y:0, ease:Power2.easeOut} , .05)
					.addCallback(function () {
						$('.sub_floating').addClass('on');
						el.find('>.btn_floating').addClass('close');
					},.13)
				}
			});
		}

		return{
			init:init
		}
	})();

	etUI.swiperCom = (function(){
		var el;

		function init(){
			var mySwiper = new Swiper('.swiper-container',{
				speed: 400,
				spaceBetween : 100,
				pagination: '.swiper-pagination',
				prevButton: '.swiper-button-prev',
				nextButton: '.swiper-button-next',
				paginationClickable: true
			});

			bindEvent();
		}

		function bindEvent(){
			$('.search_result_list .swiper-slide').each(function(){
				$(this).off('click').on('click', 'ul > li > a', function(e){
					e.preventDefault();
					$('.search_result_list .swiper-slide').find('ul > li > span').remove();
					$(this).before('<span class="select"></span>');
				})
			})
		}

		return{
			init:init
		}
	})();
}(jQuery);

