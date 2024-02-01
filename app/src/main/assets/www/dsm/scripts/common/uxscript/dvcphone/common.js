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
		// setTimeout(function(){
		// 	etUI.initUI.setup();
		// },500)

		// 퍼블리싱 전용 (주의!!! 개발 완료시 모두 삭제)/////////////////////////////
		//  || location.port == '20001'
		if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
			header.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			footer.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			// footer_kcun.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			// 스텝 aJax 추가
			setTimeout(function(){
				// etUI.step.init();
			},100);
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

			// $('.ui_slider').each(function(idx, obj){
			// 	var _inst = new etUI.Slider().init(obj);
			// 	$(this).data('_inst', _inst);
			// });
			// 	if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
			// 		etUI.uiGnb.init();
			// 	}
			setTimeout(function(){
				etUI.uiAllMenu.init();
				etUI.actAllMenu.init();
				etUI.uiBtnTopEvt.init();
				etUI.swiperCom.init(); //swipe 초기화 타이밍으로 시간차를 둠
			},100)
			// // etUI.globalCommonScript.init();
			etUI.formControl.init();
			etUI.tbSearch.init();
			etUI.tipAccordian.init();
			etUI.tooltips.init();
			etUI.commAccordian.init();
			etUI.contListEvent.init();
			etUI.lyBtnEvt.init();
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
			el = $('#header_m');

			if(el.length && el.children().length <= 0){
				window.header = $.get('/dsm/inc/layout/header_m.html');
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
			el = $('#footer_m');

			if(el.length && el.children().length <= 0){
				window.footer = $.get('/dsm/inc/layout/footer_m.html');
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

	// step include
	etUI.step = (function(){
		var el;

		function init(){
			el = $('.prd_step');
			file = location.hash.split("|");
			if(file == ""){
				return;
			}
			prdName = file[1].substr(0,1);

			if(!el.length){
				if( prdName === 'M' ){
					// 자동차
					window.step = $.get('/dsm/inc/layout/step_m.html');
				} else if ( prdName === 'L' ){
					// 장기
					window.step = $.get('/dsm/inc/layout/step_l.html');
				} else if ( prdName === 'G' || prdName === 'U' ){
					// 일반
					window.step = $.get('/dsm/inc/layout/step_g.html');
				}

				window.step.done(function(data){
					// el.html(data);
					var popupContent = jQuery("<div>").append( jQuery.parseHTML( data ) ).find( '.prd_step, .ip_info_layer' );
					// body에 삽입
					$('#step').append(popupContent);

					setTimeout(function(){
						complete();
					}, 0);
				});
			}else{
				complete();
			}
		}

		function complete(){
			$('.prd_step').addClass('fixed');
			$('#step').css({height:45})
			$('.prd_step').css({width:$(window).width()-96, zIndex:99});
			// etUI.stepCommon.init();
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

	/**
	 * @module [공통]tbSearch
	 * @description 조회 폼의 열기/닫기 버튼 이벤트 바인드
	 * @author
	 * @see http://localhost:8888/dsm/mdi_mobile.html#y|DSMTYACA100US00
	 * @example
	 * // 초기화
	 * etUI.tbSearch.init();
	 */
	etUI.tbSearch = (function(){
		var el, btnCtr, tbSearchs;

		function init(){
			el = $('.ui_tb_acc');
			btnCtr = el.find('.ui_btn_area > .btn_ctr');

			if(el.hasClass('tb_open')){
				preOpen();
				// btnCtr.trigger('click.tbSearch');
			}

			bindEvent();
		}

		function bindEvent(){
			btnCtr.off('click.tbSearch').on('click.tbSearch', function(e){
				e.preventDefault();

				var tbSearchs = $(this).closest(".ui_tb_acc").find(".ui_acc_cont table");

				if($(this).hasClass("on")) {
					tbSearchs.not(":eq(0)").css({
						"margin-top" : "-" + tbSearchs.not(":eq(0)").height() + "px",
						"opacity" : "0",
						"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
					});
					setTimeout(function() {
						tbSearchs.not(":eq(0)").hide();
						if($('.ui_Mgt').length > 0){
							etUI.commAccordian.init();
						}
					}, 500);
					$(this).text("열기");
					$(this).removeClass("on");
				} else {
					tbSearchs.not(":eq(0)").show(0, function(){
						$(this).css({
							"margin-top" : "0px",
							"opacity" : "1",
							"transition" : "opacity 0.36s cubic-bezier(0.65, -0.01, 0.21, 1), margin-top 0.36s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
						});
					});
					$(this).text("닫기");
					$(this).addClass("on");
					setTimeout(function() {
						if($('.ui_Mgt').length > 0){
							etUI.commAccordian.init();
						}
					}, 500);
				}

			})

			btnCtr.each(function() {
				// if($(this).hasClass("on")) {
				// 	// $(this).trigger("click.tbSearch");
				// }
			});
		}

		function preOpen(){
			$(".ui_tb_acc").find(".ui_acc_cont .tb_search, .ui_acc_cont table").not(":eq(0)").show(0, function(){
				$(this).css({
					"margin-top" : "0px",
					"opacity" : "1",
					"transition" : "opacity 0.36s cubic-bezier(0.65, -0.01, 0.21, 1), margin-top 0.36s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
				});
			});
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").text("닫기");
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").addClass("on");
			setTimeout(function() {
				if($('.ui_Mgt').length > 0){
					etUI.commAccordian.init();
				}
			}, 500);
		}

		function disableOpen(){
			$(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").css({
				"margin-top" : "-" + $(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").height() + "px",
				"opacity" : "0",
				"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
			});
			setTimeout(function() {
				$(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").hide();
				if($('.ui_Mgt').length > 0){
					etUI.commAccordian.init();
				}
			}, 500);
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").text("열기");
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").removeClass("on");

			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").off('click');
		}

		return{
			init:init,
			preOpen:preOpen,
			disableOpen:disableOpen
		}
	})();


	etUI.swiperCom = (function(){
		var el, mainSwiper, swipeIdx, viewSwiper, resultSwiper, newContSwiper, bestSwiper;

		function init(){

			mainSwiper = new Swiper('.ui_main_swipe',{
				speed: 400,
				spaceBetween : 100,
				pagination: '.swiper-pagination',
				prevButton: '.swiper-button-prev',
				nextButton: '.swiper-button-next',
				paginationClickable: true,
				onSlideChangeEnd: function(swiper){
					swipeIdx = swiper.activeIndex
					$('.swipe_head').find('ul > li').removeClass('acitve');
					$('.swipe_head').find('ul > li').eq(swiper.activeIndex).addClass('acitve');
				}
			});

			viewSwiper = new Swiper($('.view_cont_wrap').find('>.swiper-container').get(0),{
				direction: 'horizontal',
				speed: 400,
				spaceBetween : 100,
				// initialSlide:0,
				pagination: $('.view_cont_wrap').find('.swiper-pagination'),
				// prevButton: '.swiper-button-prev',
				// nextButton: '.swiper-button-next',
				paginationClickable: true
			});

			resultSwiper = new Swiper($('.search_result_wrap'),{
				direction: 'horizontal',
				speed: 400,
				spaceBetween : 100,
				initialSlide:0,
				pagination: $('.search_result_wrap').find('>.swiper-pagination'),
				prevButton: '.swiper-button-prev',
				nextButton: '.swiper-button-next',
				paginationClickable: true
			});

			newContSwiper = new Swiper($('.new_rolling').get(0),{
				direction: 'horizontal',
				speed: 400,
				spaceBetween : 100,
				initialSlide:0,
				pagination: $('.new_rolling > .swiper-pagination'),
				prevButton: '.swiper-button-prev',
				nextButton: '.swiper-button-next',
				paginationClickable: true,
				onInit:function(){
					activeSwipe(0)
				}
			});

			bindEvent();
		}

		function bindEvent(){
			$('.swipe_head').off('click').on('click', 'ul > li > a', function(e){
				e.preventDefault();
				swipeIdx = $(this).closest('li').index();

				$('.swipe_head').find('ul > li').removeClass('acitve');
				$('.swipe_head').find('ul > li').eq(swipeIdx).addClass('acitve');
				mainSwiper.slideTo(swipeIdx);
			});

			//컨텐츠(컨텐츠뷰) toggle 이벤트
			$('.con_org_toggle').on('click', '>.open_btn', function(e){
				e.preventDefault();
				if($(this).hasClass('on')){
					new TimelineMax()
					.set($(this), {className:'-=on'}, .1)
					.to('.con_org_toggle', 0.37, {bottom:-81, ease: Power2.easeOut}, .1)
					.to('.open_btn > span', .5, {rotation:0, ease: Power2.easeOut}, .2)
				}else{
					new TimelineMax()
					.set($(this), {className:'+=on'}, .1)
					.to('.con_org_toggle', 0.37, {bottom:0, ease: Power2.easeOut}, .1)
					.to('.open_btn > span', .5, {rotation:180, ease: Power2.easeOut}, .2)
				}
			});

			$('.best_wrap').off('click').on('click', 'ul>li>a', function(e){
				e.preventDefault();
				var index = $(this).closest('li').index();

				activeSwipe(index)
			})
			// setTimeout(function(){
			// 	$('.best_wrap ul > li.tab_active__ > a').trigger('click');
			// },500)
		}

		function activeSwipe(index){

			if(index == 0){
			// 	idx = 0
				// bestSwiper.destroy();
			}else{
				// console.log(bestSwiper.length)
				bestSwiper.destroy();
				// resultSwiper.destroy();
			// 	// newContSwiper.destroy();
				// bestSwiper.slideTo(0)
			// 	idx = index
			}
			setTimeout(function(){
				bestSwiper = new Swiper($('.best_rolling').get(index),{
					direction: 'horizontal',
					speed: 400,
					spaceBetween : 100,
					pagination: $('.best_rolling').eq(index).find('>.swiper-pagination'),
					initialSlide:0,
					prevButton: '.swiper-button-prev',
					nextButton: '.swiper-button-next',
					paginationClickable: true
				});
			},300)
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [공통]uiAllMenu
	 * @description 전체 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.uiAllMenu.init();
	 */
	etUI.uiAllMenu = (function(){
		var el, elBox, el2Dep, el2DepSub1, el2DepSub2, elSearch;

		function init(){
			el = $('#header_m');
			elBox = $('.menu_wrap')
			el1Dep = elBox.find('.sub_depth1');
			el2Dep = el1Dep.find('.sub_depth2');
			el3Dep = el2Dep.find('.sub_depth3');

			elSearch = el.find('.btn_search > a.spr_img');

			bindEvents();

			return this;
		}

		function setup(){
			var el2DepHeight = el2Dep.height();
			var el2DepSub1Height = el2Dep.height();
			var sub1LastHeight = el3Dep.find('>li:last').height();
			var scrollHeight = (el2DepHeight - sub1LastHeight) + el2DepSub1Height;

			setTimeout(function(){
				el1Dep.css({height:scrollHeight});
			},500)
		}

		function bindEvents(){
			elSearch.on('click', function(e) {
				e.preventDefault();
				new TimelineMax()
				.set('.search_bx', {display:'block', y:-150, scale:.5, autoAlpha:0}, 0)
				.to('.search_bx', 0.5, {autoAlpha:1, y:0, scale:1, ease: Power2.easeOut}, .1)
			})

			$('.search_bx').on('click', '.search_top > .search_close', function(e) {
				e.preventDefault();
				new TimelineMax()
				// .set('.search_bx', {display:'block', y:-150, scale:.5, autoAlpha:0}, 0)
				.to('.search_bx', 0.5, {scale:1.3, autoAlpha:0}, .1)
				.set('.search_bx', {display:'none'}, .3)
			})

			//전체 메뉴 open
			el.on('click', '.btn_allmenu >a.spr_img', function(e){
				e.preventDefault();
				var menuBox = $('.allmenu_bx');

				$('.allmenu_bx').find('.dimm').fadeIn();

				new TimelineMax()
				.set('.allmenu_bx', {className:'+=open'}, .1)
				.set('.allmenu_bx > .menu_wrap', {right:-290}, .1)
				.to('.allmenu_bx >.menu_wrap', 0.37, {display:'block', right:0, ease: Power2.easeOut}, .12)
				.to('.allmenu_bx > .menu_wrap', 0.37, {display:'block'}, .13)
				// .addCallback(function(){setup()}, .2)
			});

			el2Dep.off('click').on('click', '>li>a', function(e){
				e.preventDefault();

				if($(this).closest('li').hasClass('on')){
					$(this).next('ul.sub_depth3').slideUp();
					$(this).closest('li').removeClass('on');
				}else{
					$(this).next('ul.sub_depth3').slideDown();
					$(this).closest('li').addClass('on');
				}
				$('.allmenu_bx').animate({scrollTop:$(this).offset().top});
			})

			$('.allmenu_bx').on('click', '.top_area > .btn_close', function(e){
				e.preventDefault();
				allmenuClose();
			});

		}

		function allmenuClose(){
			TweenMax.to('.allmenu_bx > .menu_wrap', 0.37, {right:-290, ease: Power2.easeOut, onComplete: function(){
				$('.allmenu_bx > .menu_wrap').hide();
				$('.allmenu_bx').removeClass('open');
				// el2DepSub1.removeAttr('style');
			}});
			$('.dimm').fadeOut();
		}

		return {
			init: init
		};
	})();

	/**
	 * @module [활동]actAllMenu
	 * @description 활동 전체 메뉴/검색 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.actAllMenu.init();
	 */
	etUI.actAllMenu = (function(){
		var el, utilBox, utilMenu, actMenuBox, actMenuBody, utilSearch, el1Dep, el2Dep, el3Dep;

		function init(){
			el = $('#header_acti');
			utilBox = $('.utill_activity')
			utilMenu = utilBox.find('.menu');
			actMenuBox = $('.allmenu_gnb');
			actMenuBody = actMenuBox.find('.gnb_body');
			utilSearch = utilBox.find('.secrch');

			el1Dep = actMenuBody.find('.gnb_2depth');
			el2Dep = el1Dep.find('.sub_depth2');
			el3Dep = el2Dep.find('.sub_depth3');

			bindEvents();

			return this;
		}

		// function setup(){
		// 	var el2DepHeight = el2Dep.height();
		// 	var el2DepSub1Height = el2DepSub1.height();
		// 	var sub1LastHeight = el2DepSub1.find('>li:last').height();
		// 	var scrollHeight = (el2DepHeight - sub1LastHeight) + el2DepSub1Height;

		// 	setTimeout(function(){
		// 		el2DepSub1.css({height:scrollHeight});
		// 	},500)
		// }

		function bindEvents(){
			//전체 메뉴 open
			utilMenu.on('click', '.ui_allmenu', function(e){
				e.preventDefault();
				var menuBox = $('.allmenu_bx');

				$('.allmenu_gnb').find('.dimm').fadeIn();

				new TimelineMax()
				.set('.allmenu_bx', {className:'+=open'}, .1)
				.set('.allmenu_gnb', {right:$(window).width()}, .1)
				.to('.allmenu_gnb', 0.37, {display:'block', right:0, ease: Power2.easeOut}, .12)
				.to('.allmenu_gnb', 0.37, {display:'block'}, .13)
			});

			el2Dep.off('click').on('click', '>li>a', function(e){
				e.preventDefault();

				if($(this).closest('li').hasClass('on')){
					$(this).next('ul.sub_depth3').slideUp();
					$(this).closest('li').removeClass('on');
				}else{
					$(this).next('ul.sub_depth3').slideDown();
					$(this).closest('li').addClass('on');
				}
				$('html, body').animate({scrollTop:$(this).offset().top});
			});

			$('.allmenu_bx').on('click', '.gnb_top > .allmenu_close', function(e){
				e.preventDefault();
				actMenuClose();
			});

			utilSearch.on('click', '>a', function(e){
				e.preventDefault();
				new TimelineMax()
				.set('.search_bx', {className:'+=open'}, .1)
				.set('.search_top', {top:-55}, .1)
				.set('.activity_ipt_wrap', {width:'40%'}, .1)
				.set('.search_list_wrap', {display:'none'})
				.to('.search_top', 0.37, {top:0, ease: Power2.easeOut}, .12)
				.to('.activity_ipt_wrap', 0.37, {width:'100%', ease: Power2.easeOut}, .3)
				.addCallback(function(){
					$('.activity_ipt_wrap').find('>input[type="text"]').focus();
					$('.search_list_wrap > .dimm').show();
					$('.search_list_wrap').show();
				}, .4)
			});

			$('.search_bx').on('click', '.search_top > .btn_back', function(e){
				e.preventDefault();
				actSearchClose();
			});

		}

		function actMenuClose(){
			TweenMax.to('.allmenu_bx > .allmenu_gnb', 0.37, {right:$(window).width(), ease: Power2.easeOut, onComplete: function(){
				$('.allmenu_bx > .allmenu_gnb').hide();
				$('.allmenu_bx').removeClass('open');
				// el2DepSub1.removeAttr('style');
			}});
			$('.dimm').hide();
		}

		function actSearchClose(){
			TweenMax.to('.search_top', 0.37, {top:-55, ease: Power2.easeOut, onComplete: function(){
				$('.search_list_wrap').hide();
				$('.search_bx').removeClass('open');
				// el2DepSub1.removeAttr('style');
			}});
			$('.search_list_wrap > .dimm').hide();
			$('.search_list_wrap').hide();
		}

		return {
			init: init
		};
	})();

	etUI.uiBtnTopEvt = (function(){
		var el;

		function init(){

			el = $('#footer_m');

			el.find('.footer_btn').css({position:'fixed', width:$(window).width()-30, bottom:0});

			if($(window).scrollTop() == 0){
				el.hide();
			}else{
				el.show();
			}

			bindEvent();
		}

		function bindEvent(){
			$(window).on('scroll', function(e) {
				e.preventDefault();

				var SH = $(document).height();
				var SP = $(window).height() + $(window).scrollTop();

				if($(window).scrollTop() == 0){
					el.fadeOut();
				}else{
					el.fadeIn();
				}

				//scrollEnd check
				if((SH - SP) / SH === 0){
					el.find('.footer_btn').removeAttr('style');
				}else{
					el.find('.footer_btn').css({position:'fixed', width:$(window).width()-30, bottom:0});
				}
			})

			el.off('click').on('click', '.footer_btn > a.btn_page_top', function(e){
				e.preventDefault();

				$('html, body').animate({scrollTop:0});
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [공통]tipAccordian
	 * @description 알아두실사항 펼침/접힘
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi_mobile_main.html#main|main
	 * @example
	 * // 초기화
	 * etUI.tipAccordian.init();
	 */
	etUI.tipAccordian = (function(){
		var el;

		function init(){

			el = $('.ui_tip_acc');

			el.find('.ui_acc_cont').hide();
			// $(this).next('.ui_acc_cont').slideUp(300);
			if(el.hasClass('acc_open')){
                el.find('>a').addClass('on');
                el.find('.ui_acc_cont').show();
            }

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '>a', function(e){
				e.preventDefault();
				if($(this).next('.ui_acc_cont').is(':visible')){
					$(this).removeClass('on');
					$(this).next('.ui_acc_cont').slideUp(300);
				}else{
					$(this).addClass('on');
					$(this).next('.ui_acc_cont').slideDown(300);
				}
				$('html, body').animate({scrollTop:$(this).offset().top});
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [공통]commAccordian
	 * @description 아코디언 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi_mobile.html#s|DSMPSAAA010US01
	 * @example
	 * // 초기화
	 * etUI.commAccordian.init();
	 */
	etUI.commAccordian = (function(){
		var el, ellist;
		var st;
		function init(){

			el = $('.ui_acc_type01');
			ellist = el.find('.prd_list > li');

			if(el.hasClass('acc_open')){
				ellist.find('>a.btn_acc').addClass('on');
				el.find('.acc_cont').show();
			}

			if(ellist.hasClass('ui_open')){
				$('.ui_open').find('>a.btn_acc').addClass('on');
				$('.ui_open').find('.acc_cont').show();
			}

			bindEvent();
		}

		function bindEvent(){
			ellist.off('click').on('click', '>a.btn_acc', function(e){
				e.preventDefault();

				if($('article.ui_Mgt').length > 0){
					var mgt = 167
				}else{
					var mgt = 65
				}
				if($(this).next('.acc_cont').is(':visible')){
					$(this).removeClass('on');
					$(this).next('.acc_cont').slideUp(300);
				}else{
					$(this).addClass('on');
					$(this).next('.acc_cont').slideDown(300);
					$('html, body').animate({scrollTop:$(this).offset().top-mgt});
				}
			});

			$('.td_more_list').off('click.listOpen').on('click.listOpen', '.btn_box > .btn_bg_add', function(e){
				e.preventDefault();
				$(this).addClass('on');
			});

			$('.td_more_list').off('click.listClose').on('click.listClose', '.btn_box > .ipt_ct_layer > a.btn_bg_del02', function(e){
				e.preventDefault();
				$(this).closest('.td_more_list').find('.btn_box > .btn_bg_add').removeClass('on');
			});

			//http://localhost:8888/dsm/mdi_mobile.html#l|DSMPLCAA200UM99(계약조회)
			//스크롤 범위에 따라 탭영역 fixed(컨텐츠 내용이 너무 길어 기능 추가)
			if($('article.ui_Mgt').length > 0){
				var mT = $('.ui_tab_fixed').offset().top-77;
				$(window).on('scroll touchmove', function(e){
					st = $(window).scrollTop();

					$('#container').css({ '-webkit-overflow-scrolling': 'touch'});
					if(st < mT){
						$('.ui_tab_fixed').removeClass('top_fixed');
						$('article.ui_Mgt').removeAttr('style');
					}
					else if(st > mT){
						$('.ui_tab_fixed').addClass('top_fixed');
						$('article.ui_Mgt').css({marginTop:107});
					}
				});
			}
		}

		function accOpenEvt(liIdx){
			ellist.eq(liIdx).find('>a.btn_acc').addClass('on');
			ellist.eq(liIdx).find('.acc_cont').show();
		}

		return{
			init:init,
			accOpenEvt:accOpenEvt
		}
	})();

	/**
	 * @module [공통]tooltips
	 * @description 툴팁 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#s|SBAA110UM
	 * @example
	 * // 초기화
	 * etUI.tooltips.init();
	 */
	etUI.tooltips = (function(){
		var el, questionBox;

		function init(){
			el = $('.pst_box, .pst_rel, .question_wrap');
			questionBox = el.find('.question_bx');

			$('.grid_layer_tooltip').hide();

			bindEvent();
		}

		function bindEvent(){
			// questionBox.off('click','>a.ico_question').on('click', '>a.ico_question', function(e){
			// 	e.preventDefault();
			// 	if($(this).next('.grid_layer_tooltip').is(':visible')){
			// 		$('.grid_layer_tooltip').hide();
			// 	}else{
			// 		$('.grid_layer_tooltip').hide();
			// 		$(this).next('.grid_layer_tooltip').show();
			// 	}
			// })

			// $(document).off('touchstart.eventTooltip').on('touchstart.eventTooltip', function(e){

			// 	if($(e.target).closest('.question_bx').size() == 0){
			// 		$('.grid_layer_tooltip').hide();
			// 	}
			// });

			//http://localhost:8888/dsm/mdi_mobile.html#y|DSMTYACA100US00
			//레이어 팝업 닫기
			$('.grid_layer_popup').on('click', 'a.btn_close', function(e){
				e.preventDefault();
				$(this).closest('.grid_layer_popup').hide();
			});
		}

		return{
			init:init
		}
	})();


	/**
	 * @module [메인]contListEvent
	 * @description 컨텐츠 리스트 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi_mobile_main.html#main|main
	 * @example
	 * // 초기화
	 * etUI.contListEvent.init();
	 */
	etUI.contListEvent = (function(){
		var el, elBox;

		function init(){
			el = $('.mid_wrap');
			elBox = el.find('>ul');

			elBox.removeClass('active');
			elBox.find('li.spread').removeClass('active');

			bindEvent();
		}

		function bindEvent(){
			elBox.off('click').on('click', '> li.spread > a', function(e){
				e.preventDefault();
				if($(this).closest('li.spread').hasClass('active')){
					elBox.removeClass('active');
					elBox.find('li.spread').removeClass('active');
				}else{
					elBox.removeClass('active');
					$(this).closest('ul').addClass('active');
					elBox.find('li.spread').removeClass('active');
					$(this).closest('li.spread').addClass('active');
				}
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [고객 - 개인고객(개인고객등록)]lyBtnEvt
	 * @description 관련화면 layer 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi_mobile.html#k|KAAA010UM
	 * @example
	 * // 초기화
	 * etUI.lyBtnEvt.init();
	 */
	etUI.lyBtnEvt = (function(){
		var el;

		function init(){
			el = $('.ly_btn_wrap');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){
			el.off('click.openEvt').on('click.openEvt', '.ly_btn_list > a', function(e){
				e.preventDefault();
				if($(this).next('.list_link').is(':visible')){
					$(this).removeClass('on');
				}else{
					$(this).addClass('on');
				}
			})
		}

		return{
			init:init
		}
	})();
}(jQuery);
// 로딩바
function lodingBar(){
	// var wrapEl = $('#contents');
	// var barHtml = '<div class="loding_wrap"><div class="dimm"></div><div class="loding_area"><span></span> <span></span> <span></span> <span></span></div></div>';
	// var barCheck = $('.loding_wrap').length === 0;

	// if(barCheck){
	// 	wrapEl.prepend(barHtml);
	// }
}

function lodingBarClose(){
	// $('.loding_wrap').remove();
}

//로그인 정보 보여주기
function loginInfoShow(){
	TweenMax.set('.log_info',{display:'block', autoAlpha: 0})
	TweenMax.to('.log_info', .35, {autoAlpha: 1})
	TweenMax.to('.log_info', .35, {delay: 3.5,autoAlpha: 0, onComplete: function(){
		$('.log_info').removeAttr('style');
	}})
}

//로그인 정보 가리기
// function loginInfoHide(){
// 	$('.log_info').slideUp(350);
// }
