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
		// 퍼블리싱 전용 (주의!!! 개발 완료시 모두 삭제)/////////////////////////////
		//  || location.port == '20001'
		if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
			header.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			footer.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			// footer_kcun.init(); // 개발언어로 변경시 이 부분 삭제 해야 합니다. (개발언어로 인클루드 필요.)
			// 스텝 aJax 추가

			setTimeout(function(){

				etUI.step.init();
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
			setTimeout(function(){
				mainIdCheck();
				if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
					etUI.uiGnb.init();
					etUI.uiAllMenu.init();
				}
				etUI.uiQuickMenu.init();
				etUI.floatingMenu.init();
				etUI.uiCpdTable.init();
			},100)
			// etUI.mainEvt.init();
			etUI.historyBox.init();
			etUI.carRegDiscord.init();
			etUI.gkeyPad.init(); // 장기 - 개발사항으로 바뀜
			// etUI.globalCommonScript.init();
			etUI.formControl.init();
			//etUI.swiperCom.init();

			/* 2017-09-29 콘텐츠개발자 수정.(주석풀지말것) */
			//s:활동,교육(컨텐츠) 스크립트
			if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
				etUI.activeUiAllMenu.init();
				etUI.activefloatingMenu.init();
				etUI.swiperCom.init();
			}
			//e:활동,교육(컨텐츠) 스크립트

			// etUI.contractSearch.init(); //개발작업
			etUI.formSlideLayer.init();
			etUI.tbSearch.init();
			etUI.tipAccordian.init();
			etUI.tooltips.init();
			etUI.commAccordian.init();
			etUI.ltJoinPlan.init();
			etUI.corpTotalUi.init();
			etUI.searchRegistBtn.init();
			etUI.selectCarList.init();
			etUI.selectCarCode.init();
			etUI.selectGoodsList.init();
			etUI.selectBuildList.init();
			etUI.selectSearchEvt.init();
			etUI.accountChoicePop.init();
			etUI.accountSetPop.init();
			etUI.originCaseActivePop.init();
			etUI.searchPlanList.init();
			etUI.issueSelect.init();
			etUI.drvInfoSelect.init();
			etUI.discountRatioSel.init();
			etUI.drvInfoInput.init();
			etUI.planInsuSelect.init();
			etUI.floatPayResult.init();
			etUI.insulPrdSelEvt.init();
			etUI.freePlanBox.init();
			etUI.mortgageBtnEvt.init();
			etUI.workChangeBtnEvt.init();
			etUI.etcTextBtnEvt.init();
			etUI.jobTypeSearch.init();
			etUI.secTypeView.init();
			etUI.helpGuide.init();
			etUI.consultNumSearch.init();
			etUI.requireDocSearch.init();
			etUI.orgChart.init();
			// etUI.settingMenu.init();
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

	// step include
	etUI.step = (function(){
		var el;

		function init(){
			el = $('.prd_step');
			file = location.hash.split("|");

			if(file == "" || file[1] == undefined){
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
				}else{
					return;
				}

				window.step.done(function(data){
					// el.html(data);
					var popupContent = jQuery("<div>").append( jQuery.parseHTML( data ) ).find( '.prd_step, .ip_info_layer, .anchor_wrap' );
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
			if($('.etc_step').length == 0){
				$('.etc_full').hide();
				$('.prd_step').addClass('fixed');
				$('#step').css({height:45})
				$('.prd_step').css({width:$(window).width()-96, zIndex:99});
			}else{
				$('.prd_step').hide();
				$('.etc_full').show();
				$('.prd_step').addClass('fixed');
				$('#step').css({height:45})
				$('.prd_step').css({width:$(window).width()-51, zIndex:99});
			}
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
	* s:활동,교육(컨텐츠) 스크립트
	*/
	/**
	 * @module [활동,교육(컨텐츠)]activeUiAllMenu
	 * @description 전체 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.activeUiAllMenu.init();
	 */
	etUI.activeUiAllMenu = (function(){
		var active, activityMBox, activeBox, active2Dep, active2DepSub1, active2DepSub2

		function init(){
			active = $('.utill_activity');
			if(active.length == 0){
				return;
			}
			activityMBox = $('#header_acti .allmenu_bx');
			activeBox = $('#header_acti .allmenu_gnb')
			active2Dep = activeBox.find('.gnb_2depth');
			active2DepSub1 = active2Dep.find('.sub_depth1');
			active2DepSub2 = active2DepSub1.find('.sub_depth2');

			// $('.side_bar,.side_menu').css({zIndex:106});

			bindEvents();

			return this;
		}

		function setup(){
			var active2DepHeight = active2Dep.height();
			var active2DepSub1Height = active2DepSub1.height();
			var sub1LastHeights = active2DepSub1.find('>li:last').height();
			var scrollHeights = (active2DepHeight - sub1LastHeights) + active2DepSub1Height;

			setTimeout(function(){
				active2DepSub1.css({height:scrollHeights});
			},500)
		}

		function bindEvents(){
			//전체 메뉴 open
			$('.at_all_menu').on('click.active', function(e){
				e.preventDefault();
				// var menuBox = $(this).closest('.all_menu').find('.allmenu_bx');

				// activityMBox.addClass('open');
				$('#header_acti .allmenu_bx').find('.dimm').show();
				// // $('.allmenu_bx').show();
				new TimelineMax()
				.set('#header_acti .allmenu_gnb', {left:450}, .0)
				// .set('#header_acti .gnb_top', {left:225}, .0)
				.set('#header_acti .gnb_1depth', {left:125}, .0)
				.set('#header_acti .gnb_body', {width:0, marginLeft:325}, .0)
				.set('#header_acti .allmenu_bx', {display:'block'}, .12)
				.to('#header_acti .allmenu_gnb', 0.37, {display:'block', left:0, ease: Power2.easeOut}, .12)
				// .to('#header_acti .gnb_top', 0.37, {left:0, ease: Power2.easeOut}, .12)
				.to('#header_acti .gnb_1depth', 0.3, {left:0, ease: Power2.easeOut}, .1)
				.to('#header_acti .gnb_body', 0.5, {width:325, marginLeft:0, ease: Power2.easeOut}, .2)
				// .to('.allmenu_bx', 0.37, {display:'block'}, .13)
				.addCallback(function(){setup()}, .37)
				// TweenMax.set('.allmenu_gnb', {autoAlpha:0, right:-551}, .1);
				// TweenMax.to('.allmenu_gnb', 0.37, {autoAlpha:1, display:'block', right:0, ease: Power2.easeOut}, .15);
			});

			var ignoreScrolls = false;
			// 메뉴 스크롤
			active2Dep.off('touchmove.activeScroll touchend.activeScroll scroll.activeScroll').on('touchmove.activeScroll touchend.activeScroll scroll.activeScroll', function(e){
				var $this = $(this);
				var objScrolls = $this.scrollTop();
				var intersections = 0;

				if(ignoreScrolls){
					return;
				}

				var menuCurTop =  function(cnts){
					var querys = [];
					$this.find('.sub_depth1 > li').each(function(index, obj){
						querys.push($(obj).outerHeight());
					});

					for(var i = 0 ; i < (cnts+1) ; i++){
						intersections += querys[i];
					}

					return intersections;
				}

				var offsetObj1 = menuCurTop(0);
				var offsetObj2 = menuCurTop(1) - offsetObj1;
				var offsetObj3 = menuCurTop(2) - offsetObj1 - offsetObj2;
				var offsetObj4 = menuCurTop(3) - offsetObj1 - offsetObj2 - offsetObj3;
				// var offsetObj05 = menuCurrTop(4) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04;

				if(objScrolls < offsetObj1){
					activeBox.find('.gnb_1depth li').removeClass('on');
					activeBox.find('.gnb_1depth li').eq(0).addClass('on');
				}
				else if(objScrolls < offsetObj2){
					activeBox.find('.gnb_1depth li').removeClass('on');
					activeBox.find('.gnb_1depth li').eq(1).addClass('on');
				}
				else if(objScrolls < offsetObj3){
					activeBox.find('.gnb_1depth li').removeClass('on');
					activeBox.find('.gnb_1depth li').eq(2).addClass('on');
				}
				else if(objScrolls < offsetObj4){
					activeBox.find('.gnb_1depth li').removeClass('on');
					activeBox.find('.gnb_1depth li').eq(3).addClass('on');
				}
				else if(objScrolls < offsetObj5){
					activeBox.find('.gnb_1depth li').removeClass('on');
					activeBox.find('.gnb_1depth li').eq(4).addClass('on');
				}
				// else{
				// 	activeBox.find('.gnb_1depth li').removeClass('on');
				// 	activeBox.find('.gnb_1depth li').eq(5).addClass('on');
				// }
			});

			// 메뉴 클릭
			activeBox.find('.gnb_1depth li a').off('click.actives').on('click.actives', function(e){
				e.preventDefault();
				var $this = $(this);
				var idx = $this.closest('li').index();
				var intersections = 0;
				var index = $(this).closest('li').index();

				activeBox.find('.gnb_1depth li').removeClass('on');
				$this.closest('li').addClass('on');

				ignoreScrolls = true;

				var position01 =  function(){
					var querys = [];

					active2Dep.find('.sub_depth1 > li').each(function(index, obj){
						querys.push($(obj).outerHeight());
					});

					for(var t = 0 ; t < idx ; t++){
						intersections += querys[t];
					}

					return intersections;

				}

				var targetPositions = position01();
				TweenMax.to($('#header_acti .gnb_2depth'), 0.3, {scrollTop:targetPositions, onComplete : function(){
					ignoreScrolls = false;
				}});

			});

			active2DepSub2.off('click.active').on('click.active', '>li>a', function(e){
				e.preventDefault();
				// var intersection = 0;
				if($(this).next('ul.sub_depth3').length == 0){
					return;
				}else{
					if($(this).closest('li').hasClass('on')){
						$(this).next('ul.sub_depth3').slideUp();
						$(this).closest('li').removeClass('on');
					}else{
						$(this).next('ul.sub_depth3').slideDown();
						$(this).closest('li').addClass('on');
					}
				}
				// var position =  function(){
				// 	var query = [];
				// 	el2Dep.find('.sub_depth1 > li').each(function(index, obj){
				// 		query.push($(obj).outerHeight());
				// 	});

				// 	for(var i = 0 ; i < idx ; i++){
				// 		intersection += query[i];
				// 	}

				// 	return intersection;

				// }
			})

			$('.allmenu_bx').on('click', '.allmenu_close', function(e){
				e.preventDefault();
				activeAllmenuClose();
			});

			$('.allmenu_bx').on('click', '.dimm', function(e){
				e.preventDefault();
				activeAllmenuClose();
			})

			$('.btn_main_tab').off('click.ActiveMdi').on('click.ActiveMdi', function(e){
				e.preventDefault();
				if($(this).hasClass('close')){
					$(this).removeClass('close');
					$('.docs_tab_context__').slideUp(250);
				}
				else{
					$(this).addClass('close');
					$('.docs_tab_context__').slideDown(250);
				}
			})

		}

		function activeAllmenuClose(){
		// 	$('.allmenu_bx').removeClass('open');
		// 	// $('.allmenu_bx').css({zIndex:101})
			TweenMax.to('.allmenu_gnb', 0.37, {left:450, ease: Power2.easeOut, onComplete: function(){
				$('.allmenu_gnb').hide();
				$('.gnb_body').removeAttr('style');
				active2DepSub1.removeAttr('style');
			}});
			$('.dimm').hide();
		}

		return {
			init: init
		};
	})();

	/**
	 * @module [활동,교육(컨텐츠)]activefloatingMenu
	 * @description 프로팅 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.activefloatingMenu.init();
	 */
	etUI.activefloatingMenu = (function(){
		var el;

		function init(){
			el = $('.floting_btn');
			icons = el.find('.sub_floating > .sub_floating, .activity > .save, .sub_floating > .share, .sub_floating > .bookmark, .sub_floating > .send').get().reverse();
			bindEvent();
		}

		function bindEvent(){

			el.on('click', '>.btn_floating', function(e){
				e.preventDefault();

				if(el.find('>.btn_floating').hasClass('close')){
					new TimelineMax()
					.to(el.find('>.btn_floating'), .3, {scale:.9}, 0)
					.to(el.find('>.btn_floating'), .3, {scale:1}, .1)
					.fromTo(icons, .2, {autoAlpha:1, y:0}, {autoAlpha:0, y: function (i) {
						return (i + 1) * 25
					}}, .2)
					.addCallback(function () {
						$('.sub_floating').removeClass('on');
						el.find('>.btn_floating').removeClass('close');
					},.38)
				}else{
					new TimelineMax()
					.to(el.find('>.btn_floating'), .3, {scale:.9}, 0)
					.to(el.find('>.btn_floating'), .3, {scale:1}, .1)
					.staggerFromTo(icons, .2, {autoAlpha:0, y:"+=25"}, {autoAlpha:1, y:0, ease:Power2.easeOut} , .05)
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
		var el, newContSwiper, bestSwiper, resultListSwiper, idx = 0, startSlide = 0;

		function init(){
			el = $('.swiper-container');
			if(!$('#Tab1').is(':visible')){
				$('#Tab1').show().addClass('tab_content_active__ visible__');
			}

			setTimeout(function(){

				if($('.visible__ .bx_activity').length != 0){

					if(!$('.new_rolling').hasClass('complete')){
						if($('.visible__ .new_rolling').find('.swiper-wrapper > div').length !=1){
							var loopBool = true;
						}else{
							var loopBool = false;
						}
						newContSwiper = new Swiper($('.visible__ .new_rolling'),{
							direction: 'horizontal',
							speed: 400,
							loop: loopBool,
							autoplay: 2000,
							spaceBetween : 100,
							initialSlide:0,
							pagination: $('.new_rolling > .swiper-pagination'),
							paginationClickable: true
						});
					}

					activeSwipe(0);
					$('.new_rolling').addClass('complete');
				}

				if($('.visible__ .view_cont_roll').length == 0){
					if(!$('.search_result_wrap').hasClass('complete')){
						var resultSwiper = new Swiper($('.visible__ .search_result_wrap.swiper-container'),{
							direction: 'horizontal',
							speed: 400,
							spaceBetween : 100,
							initialSlide:0,
							pagination: $('.search_result_wrap').find('>.swiper-pagination'),
							prevButton: '.swiper-button-prev',
							nextButton: '.swiper-button-next',
							paginationClickable: true
						});
					}
					$('.search_result_wrap').addClass('complete');
				}else{
					if(!$('.page_select .search_result_wrap').hasClass('complete')){
						var myPageSwiper = new Swiper($('.page_select .search_result_wrap.swiper-container'),{
							direction: 'horizontal',
							speed: 400,
							spaceBetween : 100,
							initialSlide:0,
							pagination: $('.page_select').find('.search_result_wrap >.swiper-pagination'),
							prevButton: '.swiper-button-prev',
							nextButton: '.swiper-button-next',
							paginationClickable: true
						});
					}
					$('.page_select .search_result_wrap').addClass('complete');
					$('.folder_select.pop_cont_wrap').removeClass('disno');
					if(!$('.folder_select .search_result_wrap').hasClass('complete')){
						var myfolderSwiper = new Swiper($('.folder_select .search_result_wrap.swiper-container'),{
							direction: 'horizontal',
							speed: 400,
							spaceBetween : 100,
							initialSlide:0,
							pagination: $('.folder_select').find('.search_result_wrap >.swiper-pagination'),
							prevButton: '.swiper-button-prev',
							nextButton: '.swiper-button-next',
							paginationClickable: true
						});
					}
					$('.folder_select.pop_cont_wrap').addClass('disno');
					$('.folder_select .search_result_wrap').addClass('complete');
				}

				var viewSwiper = new Swiper($('.visible__ .view_cont_wrap').find('>.swiper-container'),{
					direction: 'horizontal',
					speed: 400,
					spaceBetween : 100,
					// initialSlide:0,
					pagination: $('.visible__ .view_cont_wrap').find('.swiper-pagination'),
					prevButton: $('.visible__ .view_cont_wrap').find('.swiper-button-prev'),
					nextButton: $('.visible__ .view_cont_wrap').find('.swiper-button-next'),
					paginationClickable: true,
					touchReleaseOnEdges:true,
					onInit:function(){
						if($('.visible__ .view_cont_wrap').find('>.swiper-container > .swiper-wrapper > div').length == 1){
							$('.visible__ .view_cont_wrap').find('.swiper-button-prev, .swiper-button-next').hide();
						}else{
							$('.visible__ .view_cont_wrap').find('.swiper-button-prev, .swiper-button-next').show();
						}
						$('.visible__ .view_cont_wrap').find('.swiper-pagination').removeClass('on');
						$('.visible__ .view_cont_wrap').find('.swiper-button-prev, .swiper-button-next').removeClass('on');
					},
					onSlideChangeEnd:function(swiper,activeIndex){
						$('.visible__ .view_cont_wrap').find('.swiper-pagination > span').removeClass('swiper-pagination-bullet-active');
						$('.visible__ .view_cont_wrap').find('.swiper-pagination > span').eq(swiper.activeIndex).addClass('swiper-pagination-bullet-active');
					}
				});

				bindEvent();
			},500)
		}

		function activeSwipe(index){
			bestSwiper = new Swiper($('.visible__ .best_rolling').get(index),{
				direction: 'horizontal',
				speed: 400,
				spaceBetween : 100,
				pagination: $('.visible__ .best_rolling').eq(index).find('>.swiper-pagination'),
				initialSlide:startSlide,
				paginationClickable: true,
				onInit:function(){

				}
			});
		}

		function bindEvent(){
			$('.search_result_list .swiper-slide').each(function(){
				$(this).off('click').on('click', 'ul > li > a', function(e){
					e.preventDefault();
					$('.view_cont_wrap').removeClass('sm_layout');
					/* 2017-09-29 콘텐츠개발자 수정.(주석풀지말것) */
					//$('.search_result_list .swiper-slide').find('ul > li > span.select').remove();
					//$(this).before('<span class="select"></span>');
				})
			})

			$('.view_cont_wrap').off('click').on('click', '.swiper-slide > a',function(e){
				e.preventDefault();
				if($('.view_cont_roll').closest('.view_cont_list_wrap').is(':visible')){
					$('.view_cont_roll').closest('.view_cont_list_wrap').slideUp(function(){
						$('.view_cont_wrap').find('.swiper-pagination').show();
						// $('.view_cont_wrap').removeClass('sm_layout');
//						resultSwiper.destroy();
						// $('.view_cont_wrap').find('.swiper-button-prev, .swiper-button-next').removeClass('on');
					});
				}else{
					$('.view_cont_roll').closest('.view_cont_list_wrap').slideDown(function(){
						$('.view_cont_wrap').find('.swiper-pagination').hide();
						// $('.view_cont_wrap').addClass('sm_layout');
						// $('.view_cont_wrap').find('.swiper-button-prev, .swiper-button-next').addClass('on');
						activeSwipe(0);
						resultListSwiper = new Swiper($('.visible__ .view_cont_roll').get(0),{
							direction: 'horizontal',
							speed: 400,
							spaceBetween : 100,
							initialSlide:0,
							pagination: $('.visible__ .view_cont_roll > .swiper-pagination'),
							prevButton: $('.visible__ .view_cont_roll').find('.swiper-button-prev'),
							nextButton: $('.visible__ .view_cont_roll').find('.swiper-button-next'),
							paginationClickable: true,
							onInit:function(){

							}
						});
					});

				}
			})

			$('.best_wrap').off('click').on('click', 'ul>li>a', function(e){
				e.preventDefault();
				var index = $(this).closest('li').index();

				activeSwipe(index)
			})
		}

		return{
			init:init
		}
	})();
	//e:활동,교육(컨텐츠) 스크립트

	/**
	 * @module [공통]uiGnb
	 * @description gnb
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.uiGnb.init();
	 */
	etUI.uiGnb = (function(){
		var el, elList, elUtill;

		function init(){
			el = $('.gnb');
			elUtill = $('.utill');

			bindEvents();

			return this;
		}

		function bindEvents(){

			el.off('click').on('click', '>li>a', function(e){
				var idx = $(this).closest('li').index();
				if($(this).next('ul.m_2depth').length == 0){
					return;
				}else{
					e.preventDefault();
					if($(this).closest('li').hasClass('on')){
						TweenMax.to('.m_2depth', 0.2, {height: 0, ease: Power2.easeOut, onComplete:function(){
							$('.m_2depth').css({display:'none'});
							TweenMax.set($('.gnb > li'), {className:'-=on'});
						}});
					}else{
						$('.gnb > li').removeClass('on');
						$(this).closest('li').addClass('on');
						TweenMax.set('.m_2depth', {display:'none'});
						TweenMax.set($(this).next('ul.m_2depth'), {overflow:'hidden', top:47, bottom:0, height:0});
						TweenMax.to($(this).next('ul.m_2depth'), 0.2, {display:'block', height: 47, ease: Power2.easeOut});
					}
				}
			})

			$(document).off('touchstart.gnb').on('touchstart.gnb', function(e){
	 			if($(e.target).closest('.gnb').size() == 0){
					TweenMax.to('.m_2depth', 0.2, {height: 0, ease: Power2.easeOut, onComplete:function(){
						$('.m_2depth').css({display:'none'});
						TweenMax.set($('.gnb > li'), {className:'-=on'})
					}});
				}
			});

			elUtill.off('click').on('click', '>li.m01 > a', function(e){
				e.preventDefault();
				$('.search_box').find('.dimm').show();
				// $('.allmenu_bx').show();
				new TimelineMax()
				.set('.search_area', {autoAlpha:0, right:-420}, .1)
				.to('.search_area', 0.37, {autoAlpha:1, display:'block', right:0, ease: Power2.easeOut}, .12)
				.to('.search_box', 0.37, {display:'block'}, .13)
				// .addCallback(function(){setup()}, .2)
			})


			$('.search_box').off('click').on('click', '.search_close', function(e){
				e.preventDefault();
				// $('.allmenu_bx').removeClass('open');
				// $('.allmenu_bx').css({zIndex:101})
				TweenMax.to('.search_area', 0.37, {right:-420, ease: Power2.easeOut, onComplete: function(){
					$('.search_area').hide();
				}});
				$('.search_box').find('.dimm').hide();
				$('.search_box .search_area .search_input').find('input').val('');
			})

			$('.search_box').on('click', '>.dimm', function(e){
				e.preventDefault();
				TweenMax.to('.search_area', 0.37, {right:-420, ease: Power2.easeOut, onComplete: function(){
					$('.search_area').hide();
				}});
				$('.search_box').find('.dimm').hide();
				$('.search_box .search_area .search_input').find('input').val('');
			})
		}

		function subDepthClose(){
			TweenMax.to('.m_2depth', 0.2, {height: 0, ease: Power2.easeOut, onComplete:function(){
				$('.m_2depth').css({display:'none'});
				TweenMax.set($('.gnb > li'), {className:'-=on'})
			}});
		}

		return {
			init: init,
			subDepthClose:subDepthClose
		};
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
		var el, elBox, el1Dep, el2Dep, el2DepSub1, el2DepSub2

		function init(){
			el = $('.all_menu');
			if($('.utill_activity').length != 0){
				return;
			}
			elBox = $('.allmenu_gnb')
			el1Dep = elBox.find('.gnb_1depth');
			el2Dep = elBox.find('.gnb_2depth');
			el2DepSub1 = el2Dep.find('.sub_depth1');
			el2DepSub2 = el2DepSub1.find('.sub_depth2');

			$('.side_bar,.side_menu').css({zIndex:106});

			bindEvents();

			return this;
		}

		function setup(){
			var el2DepHeight = el2Dep.height();
			var el2DepSub1Height = el2DepSub1.height();
			var sub1LastHeight = el2DepSub1.find('>li:last').height();
			var scrollHeight = (el2DepHeight - sub1LastHeight) + el2DepSub1Height;
			var wH = $(window).height();

			el1Dep.css({height:wH - 50});
			el2Dep.css({height:wH - 50});

			setTimeout(function(){
				el2DepSub1.css({height:scrollHeight+ (wH - el2DepHeight - 50)});
			},500)
		}

		function bindEvents(){
			//전체 메뉴 open
			el.on('click', '.btn_all_menu', function(e){
				e.preventDefault();
				var menuBox = $(this).closest('.all_menu').find('.allmenu_bx');

				menuBox.addClass('open');
				$('.allmenu_bx').find('.dimm').show();
				// $('.allmenu_bx').show();
				new TimelineMax()
				.set('.allmenu_gnb', {autoAlpha:0, right:-685}, .1)
				.to('.allmenu_gnb', 0.37, {autoAlpha:1, display:'block', right:0, ease: Power2.easeOut}, .12)
				.to('.allmenu_bx', 0.37, {display:'block'}, .13)
				.addCallback(function(){
					setup();
					$('.btn_menu_search').off('click').on('click', function(e){
						e.preventDefault();
						allmenuClose();
						$('.utill > li.m01 > a').trigger('click');
					})
				}, .2)
				// TweenMax.set('.allmenu_gnb', {autoAlpha:0, right:-551}, .1);
				// TweenMax.to('.allmenu_gnb', 0.37, {autoAlpha:1, display:'block', right:0, ease: Power2.easeOut}, .15);
			});

			var ignoreScroll = false;
			// 메뉴 스크롤
			el2Dep.off('touchmove.evtScroll touchend.evtScroll scroll.evtScroll').on('touchmove.evtScroll touchend.evtScroll scroll.evtScroll', function(e){
				var $this = $(this);
				var objScroll = $this.scrollTop();
				var intersection = 0;
				var listObj = elBox.find('.gnb_1depth li');
				var objImg = listObj.find('>a>img');

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
				var offsetObj06 = menuCurrTop(5) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05;
				var offsetObj07 = menuCurrTop(6) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06;
				var offsetObj08 = menuCurrTop(7) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07;
				var offsetObj09 = menuCurrTop(8) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08;
				var offsetObj10 = menuCurrTop(9) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08 - offsetObj09;
				var offsetObj11 = menuCurrTop(10) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08 - offsetObj09 - offsetObj10;
				var offsetObj12 = menuCurrTop(11) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08 - offsetObj09 - offsetObj10 - offsetObj11;
				var offsetObj13 = menuCurrTop(12) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08 - offsetObj09 - offsetObj10 - offsetObj11 - offsetObj12;
				var offsetObj14 = menuCurrTop(13) - offsetObj01 - offsetObj02 - offsetObj03 - offsetObj04 - offsetObj05 - offsetObj06 - offsetObj07 - offsetObj08 - offsetObj09 - offsetObj10 - offsetObj11 - offsetObj12 - offsetObj13;

				if(objScroll < offsetObj01){
					listObj.removeClass('on');
					listObj.eq(0).addClass('on');
				}
				else if(objScroll < offsetObj02){
					listObj.removeClass('on');
					listObj.eq(1).addClass('on');
				}
				else if(objScroll < offsetObj03){
					listObj.removeClass('on');
					listObj.eq(2).addClass('on');
				}
				else if(objScroll < offsetObj04){
					listObj.removeClass('on');
					listObj.eq(3).addClass('on');
				}
				else if(objScroll < offsetObj05){
					listObj.removeClass('on');
					listObj.eq(4).addClass('on');
				}
				else if(objScroll < offsetObj06){
					listObj.removeClass('on');
					listObj.eq(5).addClass('on');
				}
				else if(objScroll < offsetObj07){
					listObj.removeClass('on');
					listObj.eq(6).addClass('on');
				}
				else if(objScroll < offsetObj08){
					listObj.removeClass('on');
					listObj.eq(7).addClass('on');
				}
				else if(objScroll < offsetObj09){
					listObj.removeClass('on');
					listObj.eq(8).addClass('on');
				}
				else if(objScroll < offsetObj10){
					listObj.removeClass('on');
					listObj.eq(9).addClass('on');
				}
				else if(objScroll < offsetObj11){
					listObj.removeClass('on');
					listObj.eq(10).addClass('on');
				}
				else if(objScroll < offsetObj12){
					listObj.removeClass('on');
					listObj.eq(11).addClass('on');
				}
				else if(objScroll < offsetObj13){
					listObj.removeClass('on');
					listObj.eq(12).addClass('on');
				}
				else if(objScroll < offsetObj14){
					listObj.removeClass('on');
					listObj.eq(13).addClass('on');
				}
				else{
					listObj.removeClass('on');
					listObj.eq(14).addClass('on');
				}

				elBox.find('.gnb_1depth li').each(function(idx, obj){
					if($(this).hasClass('on')){
						$(this).find('img').attr('src',$(this).find('img').attr('src').replace('_off.png','_on.png'));
					}
					else if(!$(this).hasClass('on')){
						$(this).find('img').attr('src',$(this).find('img').attr('src').replace('_on.png','_off.png'));
					}
				})
			});

			// 메뉴 클릭
			elBox.find('.gnb_1depth li a').off('click.allmenu').on('click.allmenu', function(e){
				e.preventDefault();
				var $this = $(this);
				var idx = $this.closest('li').index();
				var intersection = 0;
				var index = $(this).closest('li').index();

				elBox.find('.gnb_1depth li').removeClass('on');
				$this.closest('li').addClass('on');

				elBox.find('.gnb_1depth li a').each(function(idx, obj){
					$(obj).find('>img').attr('src',$(obj).find('>img').attr('src').replace('_on.png','_off.png'));
				})
				$this.find('>img').attr('src',$this.find('>img').attr('src').replace('_off.png','_on.png'));

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

			el2DepSub2.off('click.depth3').on('click.depth3', '>li>a', function(e){
				e.preventDefault();

				if($(this).closest('li').hasClass('on')){
					$(this).next('ul.sub_depth3').slideUp();
					$(this).closest('li').removeClass('on');
				}else{
					$(this).next('ul.sub_depth3').slideDown();
					$(this).closest('li').addClass('on');
				}
			})

			$('.allmenu_bx').on('click', '.dimm, .gnb_top > .allmenu_close', function(e){
				e.preventDefault();
				allmenuClose();
			});

		}

		function allmenuClose(){
			$('.allmenu_bx').removeClass('open');
			// $('.allmenu_bx').css({zIndex:101})
			TweenMax.to('.allmenu_gnb', 0.37, {right:-685, ease: Power2.easeOut, onComplete: function(){
				$('.allmenu_gnb').hide();
				el2DepSub1.removeAttr('style');
			}});
			$('.dimm').hide();
		}

		return {
			init: init
		};
	})();

	/**
	 * @module [공통]uiQuickMenu
	 * @description 퀵 메뉴 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.uiQuickMenu.init();
	 */
	etUI.uiQuickMenu = (function(){
		var el, idx, elMenu;

		function init(){
			el = $('.side_bar');
			elMenu = el.find('.side_menu');

			elMenu.find('ul > li').removeClass('on');

			bindEvents();
			// if($('.main').length > 0){
			// 	mdiTabAnim();
			// }else{
			// 	return;
			// }

			return this;
		}

		function mdiTabAnim(){
			$('.btn_main_tab').addClass('close');
			$('.docs_tab_context__').slideDown(250);

			setTimeout(function(){
				$('.btn_main_tab').removeClass('close');
				$('.docs_tab_context__').slideUp(250);
			},2000)
		}

		function bindEvents(){
			mdiTabAnim();
			elMenu.off('click.quick').on('click.quick', '> ul > li > a', function(e){
				e.preventDefault();
				idx = $(this).closest('li').index();
				console.log(idx)
				if($(this).closest('li').hasClass('on')){
					$(this).closest('li').removeClass('on');
					el.find('>.dimm').hide();
					TweenMax.to('.side_body_wrap', 0.37, {right:-$('.side_body_wrap').width(), ease: Power2.easeOut, onComplete: function(){
						$('.side_body_wrap').hide();
					}});
				}
				else if(idx == 5 || idx == 7){
					location.href = $(this).attr('href');
				}
				else if(idx == 6){
					$('.allmenu_gnb .allmenu_close').trigger('click');
					elMenu.find('li').removeClass('on');
					$(this).closest('li').addClass('on');
					$('.side_body_wrap').hide();
					el.find('>.dimm').css({display:'block', zIndex:99})//.show();
					$('.side_body_wrap').eq(5).show();
					TweenMax.set('.side_body_wrap:eq(5)', {right:-$('.side_body_wrap').eq(5).width()});
					TweenMax.to('.side_body_wrap:eq(5)', 0.37, {right:51, ease: Power2.easeOut, onComplete:function(){
						$('.side_body_wrap:eq(5)').find('>.body_area').css({height:$(window).height() - 90});
					}});
				}
				else{
					$('.allmenu_gnb .allmenu_close').trigger('click');
					elMenu.find('li').removeClass('on');
					$(this).closest('li').addClass('on');
					$('.side_body_wrap').hide();
					el.find('>.dimm').css({display:'block', zIndex:99})//.show();
					$('.side_body_wrap').eq(idx).show();
					TweenMax.set('.side_body_wrap:eq('+idx+')', {right:-$('.side_body_wrap').eq(idx).width()});
					TweenMax.to('.side_body_wrap:eq('+idx+')', 0.37, {right:51, ease: Power2.easeOut, onComplete:function(){
						$('.side_body_wrap:eq('+idx+')').find('>.body_area').css({height:$(window).height() - 90});
					}});

					$('.side_con05').find('.menu_list').removeClass('modify');
				}
			})

			$('.side_con03').off('click.work').on('click.work', '.tab_2depth > ul > li > a', function(e){
				e.preventDefault();
				$('.side_con03').find('.tab_2depth > ul > li > a').removeClass('on');
				$(this).addClass('on');
			})

			$('.side_con04').off('click.memo').on('click.memo', 'a.ui_btn_view', function(e){
				e.preventDefault();
				// $('.memo_popup').show();
				new TimelineMax()
				.set('.memo_popup', {autoAlpha:0, display:'block', scale:.7}, .1)
				.set('.memo_popup_cont', {autoAlpha:0}, .1)
				.to('.memo_popup', .3, {autoAlpha:1, scale:1, ease: Power2.easeOut}, .12)
				// .to('.memo_popup', .3, {scale:1, ease: Power2.easeOut}, .43)
				.to('.memo_popup_cont', .5, {autoAlpha:1, ease: Power2.easeOut}, .35)
				// .addCallback(function(){setup()}, .2)

				memoModify();
			})

			$('.side_con04').off('click.memoClose').on('click.memoClose', '.memo_popup a.btn_memo_close', function(e){
				e.preventDefault();
				$('.memo_popup').hide();
			})

			$('.side_con05').off('click.modify').on('click.modify', '.btn_area > a.green', function(e){
				e.preventDefault();
				$('.menu_list').addClass('modify');
			})

			$('.quick_close, .side_bar > .dimm').off('click').on('click', function(e){
				e.preventDefault();

				elMenu.find('li').removeClass('on');
				$('.dimm').hide();
				TweenMax.to('.side_body_wrap', 0.37, {right:-$('.side_body_wrap').width(), ease: Power2.easeOut, onComplete: function(){
						$('.side_body_wrap').hide();
					}});
			})

			elMenu.off('click.mdi').on('click.mdi', '>.btn_main_tab', function(e){
				e.preventDefault();
				if($(this).hasClass('close')){
					$(this).removeClass('close');
					$('.docs_tab_context__').slideUp(250);
				}
				else{
					$(this).addClass('close');
					$('.docs_tab_context__').slideDown(250);
				}
			})
		}

		function memoModify(){
			$('.side_con04 > .body_area').on('click', '.memo_popup .btn_area a.on', function(e){
				e.preventDefault();
				$(this).next('a').addClass('on');
				$('.memo_popup_cont').find('>input[type="text"]').focus();
			});
		}

		return {
			init: init,
			mdiTabAnim:mdiTabAnim
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
			el = $('.quick_fl_wrap');

			bindEvent();
		}

		function bindEvent(){

			el.on('click', '>.btn_floating', function(e){
				e.preventDefault();

				if($('.fl_menu').is(':visible')){
					floatMenuClose();
				}else{
					floatMenuOpen();
				}
			});

			$(document).off('touchstart click').on('touchstart click', function(e){
	 			if($(e.target).closest('.btn_floating').size() == 0){
	 				if($('.fl_menu').is(':visible')){
						floatMenuClose();
					}
				}
			});
		}

		function floatMenuOpen(){
			new TimelineMax()
			.set('.fl_menu',{autoAlpha:0, y:150, x:-50, scale:.5, display:'flex'}, .1)
			.to('.btn_floating', .3, {scale:0.9}, .1)
			.to('.btn_floating', .3, {scale:1},.3)
			.to('.fl_menu', .37, {autoAlpha:1, y:0, x:0, scale:1, ease:Power2.easeOut},.3)
			.addCallback(function () {
				$('.btn_floating').addClass('close');
			},0)
		}

		function floatMenuClose(){
			new TimelineMax()
			.to('.btn_floating', .3, {scale:.9}, 0)
			.to('.btn_floating', .3, {scale:1}, .3)
			.to('.fl_menu', .37, {autoAlpha:0, y:150, x:-70, scale:.5, ease:Power2.easeOut}, .3)
			.addCallback(function () {
				$('.btn_floating').removeClass('close');
			},0)
			.set('.fl_menu', {display:'none'},.5)
		}



		return{
			init:init,
			floatMenuOpen:floatMenuOpen,
			floatMenuClose:floatMenuClose
		}
	})();

	/**
	 * @module [공통]mainEvt
	 * @description 메인화면 이벤트
	 * @author 금정훈
	 * @example
	 * // 초기화
	 * etUI.mainEvt.init();
	 */
	etUI.mainEvt = (function(){
		var el, openTarget, elWrap, elCorp, mainLen, btnPrev, btnNext, bannerArea, corpSwiper, mainSwiper, bannerSwiper, bannerStart = 0, idx, startSlide = 1, mainIdx;

		function init(){
			el = $('.main');
			elWrap = $('.main_scene_wrap');
			elCorp = $('.corpor_container');

			mainLen = elWrap.find('>.swiper-wrapper > div').length;
			// elWrap = $('.main_scene_wrap');

			if(el.length == 0 && elWrap.length == 0 && elCorp.length == 0){
				return;
			}

			btnPrev = $('.main_scene_wrap > .swiper-button-prev');
			btnNext = $('.main_scene_wrap > .swiper-button-next');

			openTarget = $('.ui_layer_open');
			bannerArea = $('.ui_banner');

			$('.main_scene_wrap, .corpor_container').css({height:$(window).height()-83});

			setTimeout(function(){
				if(!elCorp.hasClass('complete')){
					corpSwiper = new Swiper(elCorp,{
						direction: 'horizontal',
						speed: 400,
						// pagination: elCorp.find('>.swiper-pagination'),
						paginationClickable: true,
						onSlideChangeEnd: function(swiper, activeIndex){
							elCorp.find('>.swiper-pagination > span').removeClass('swiper-pagination-bullet-active');
							elCorp.find('>.swiper-pagination > span').eq(swiper.activeIndex).addClass('swiper-pagination-bullet-active');
						},
						// onSliderMove:function(swiper,activeIndex){
						// 	TweenMax.set('.column02', {overflow:'hidden'});
						// 	TweenMax.set('.ui_main_layer', {autoAlpha:.3, margin:'0 12px 0 -609px'});
						// }
					});
				}

				if(!elWrap.hasClass('complete')){
					mainSwiper = new Swiper(elWrap.get(0),{
						direction: 'horizontal',
						speed: 400,
						pagination: elWrap.find('>.swiper-pagination'),
						initialSlide:startSlide,
						paginationClickable: true,
						prevButton: elWrap.find('.swiper-button-prev'),
						nextButton: elWrap.find('.swiper-button-next'),
						onInit : function(swiper, activeIndex){
							mainIdx = swiper.activeIndex
							$('.prd_main').show();
							arrowBtnAni(mainIdx);
							$('.header_content').on('click', '>h1>a', function(e){
								e.preventDefault();
								mainSwiper.slideTo(1,0,false)
							})
						},
						onSlideChangeEnd: function(swiper, activeIndex){
							mainIdx = swiper.activeIndex
							elWrap.find('>.swiper-pagination > span').removeClass('swiper-pagination-bullet-active');
							elWrap.find('>.swiper-pagination > span').eq(swiper.activeIndex).addClass('swiper-pagination-bullet-active');
							arrowBtnAni(mainIdx);
						},
						onSliderMove:function(swiper,activeIndex){
							TweenMax.set('.column02', {overflow:'hidden'});
							TweenMax.set('.ui_main_layer', {autoAlpha:.3, margin:'0 12px 0 -609px', className:'-=on'});
						}
					});

					new TimelineMax()
					.set('.column01',{y:150, autoAlpha:0}, 0)
					.to('.column01', .5, {y:0, autoAlpha:1}, .8)
					.set('.prd_wrqp',{y:150, autoAlpha:0}, 0)
					.to('.prd_wrqp', .5, {y:0, autoAlpha:1}, .5)
					.set('.noti_wrap',{y:150, autoAlpha:0}, 0)
					.to('.noti_wrap', .5, {y:0, autoAlpha:1}, 0.2)
				}
				elWrap.addClass('complete');
				elCorp.addClass('complete');
			},0)

			setTimeout(function(){
				if(!bannerArea.hasClass('complete')){
					if(bannerArea.find('.swiper-wrapper > div').length !=1){
						var loopBool = true;
					}else{
						var loopBool = false;
					}
					bannerSwiper = new Swiper(bannerArea,{
						direction: 'horizontal',
						speed: 400,
						loop: loopBool,
						autoplay: 3000,
						autoplayDisableOnInteraction: false,
						pagination: bannerArea.find('>.swiper-pagination'),
						paginationClickable: true,
						onSlideChangeEnd: function(swiper, activeIndex){
							bannerStart = swiper.activeIndex;
							// bannerSwiper.autoplay.start();
							// bannerArea.find('>.swiper-pagination > span').removeClass('swiper-pagination-bullet-active');
							// bannerArea.find('>.swiper-pagination > span').eq(swiper.activeIndex).addClass('swiper-pagination-bullet-active');
						}
						});
				}
				bannerArea.addClass('complete');
			},10)

			bindEvents();
		}

		function bindEvents(){
			openTarget.off('click.open').on('click.open', function(e){
				e.preventDefault();
				idx = $(this).closest('li').index();

				if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
					openAct(idx);
				}

				$('.ui_main_layer .bg_arrow').css({top:$(this).closest('.scroll_wrap').find('li').eq(idx).offset().top -56})
			});

			$('.ui_main_layer').off('click.close').on('click.close', '.btn_close', function(e){
				e.preventDefault();
				closeAct();
			})

			$('.ui_tab01').off('click').on('click', '> ul > li > a', function(e){
				e.preventDefault();
				var tabIndex = $(this).closest('li').index();
				$(this).closest('ul').find('>li>a').removeClass('on');
				$(this).addClass('on');
				$(this).closest('.ui_tab01').find('>.tab_cont').removeClass('on');
				$(this).closest('.ui_tab01').find('>.tab_cont').eq(tabIndex).addClass('on');
				closeAct();
			})
		}

		function openAct(){
			if(!$('.ui_main_layer').hasClass('on')){
				// $('.ui_main_layer .bg_arrow').css({top:$(this).closest('.scroll_wrap').find('li').eq(idx).offset().top -56})
				new TimelineMax()
				.set('.column02',{overflow:'hidden'}, 0)
				.to('.column02', .1, {overflow:'visible'}, .8)
				.set('.ui_main_layer',{autoAlpha:0, filter:'blur(20px)',  margin:'0 12px 0 -609px'}, 0)
				.to('.ui_main_layer', .5, {autoAlpha:1, filter:'blur(0px)', margin:'0 12px 0 5px', ease:Power2.easeOut}, .5)
				.set($('.ui_main_layer'), {className:'+=on'}, .53)
			}
		}

		function closeAct(){
			if($('.ui_main_layer').hasClass('on')){
				new TimelineMax()
				.to('.column02', .1, {overflow:'hidden'}, .1)
				.set('.ui_main_layer',  {autoAlpha:1, margin:'0 12px 0 5px'}, 0)
				.to('.ui_main_layer', .5, {autoAlpha:.3, margin:'0 12px 0 -609px', ease:Power2.easeOut}, .1)
				.set($('.ui_main_layer'), {className:'-=on'}, .53)
			}
		}

		function arrowBtnAni(idx){
			TweenMax.killAll();
			if(idx == 0){
				new TimelineMax()
				.to(btnNext, .5, {x:0, autoAlpha:1,}, .0)
				.to(btnNext, .5, {x:-5, autoAlpha:1, repeat: -1, yoyo: true, ease:Power1.easeInOut}, .51)
				.to(btnNext, .5, {x:70, autoAlpha:0}, 5)
				.to(btnPrev, .5, {x:-70, autoAlpha:0}, 0)
			}else if(idx == mainLen-1){
				new TimelineMax()
				.to(btnPrev, .5, {x:0, autoAlpha:1,}, .0)
				.to(btnPrev, .5, {x:5, autoAlpha:1, repeat: -1, yoyo: true, ease:Power1.easeInOut}, .51)
				.to(btnPrev, .5, {x:-70, autoAlpha:0}, 5)
				.to(btnNext, .5, {x:70, autoAlpha:0}, 0)
			}else{
				new TimelineMax()
				.to(btnPrev, .5, {x:0, autoAlpha:1}, .0)
				.to(btnPrev, .5, {x:5, autoAlpha:1, repeat: -1, yoyo: true, ease:Power1.easeInOut}, .51)
				.to(btnPrev, .5, {x:-70, autoAlpha:0}, 5)
				.to(btnNext, .5, {x:0, autoAlpha:1}, .0)
				.to(btnNext, .5, {x:-5, autoAlpha:1, repeat: -1, yoyo: true, ease:Power1.easeInOut}, .51)
				.to(btnNext, .5, {x:70, autoAlpha:0}, 5)
			}
		}

		return{
			init:init,
			openAct:openAct
		}
	})();

	etUI.historyBox = (function(){
		var el;

		function init(){
			el = $('#step'); //.prd_step
			$('.ip_info_layer').hide();
			bindEvents();
		}

		function bindEvents(){
			el.off('click.stepOpen').on('click.stepOpen', '.prd_step_scroll .btn_step', function(e){
				e.preventDefault();
				historyOpen();
			})

			el.off('click.stepClose').on('click.stepClose', '.ip_info_layer .btn_step', function(e){
				e.preventDefault();
				historyClose();
			})
		}

		function historyOpen(){
			$('.ip_info_layer').show();
			$('.bx_pd').hide();
			$('.ip_info_scroll').css({height:$(window).height()-95});
			setLineHeight();
		}

		function historyClose(){
			$('.ip_info_layer').hide();
			$('.bx_pd').show();
		}

		function setLineHeight(){
			var stepLen = $('.ip_info_area').find('>.step').length;
			var query = [];
			var stepHeight = 0;
			$('.ip_info_area').find('>.step').each(function(idx, obj){

				query.push($(obj).outerHeight()+33);

				return stepHeight;
			});

			for(var i = 0 ; i < stepLen-1 ; i++){
				stepHeight += query[i];
			}

			$('.ip_info_area').find('.bd_line').css({height:stepHeight});
		}

		return{
			init:init,
			historyOpen:historyOpen,
			historyClose:historyClose
		}
	})();

	/**
	 * @module [자동차 - 고객정보]carRegDiscord
	 * @description 소유자 불일치 영역 열기/닫기
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA010UM01
	 * @example
	 * // 초기화
	 * etUI.carRegDiscord.init();
	 */
	etUI.carRegDiscord = (function(){
		var el, eltb;

		function init(){
			el = $('.ui_car_csene01 .section');
			eltb = el.find('.tb_search');

			bindEvent();
		}

		function bindEvent(){
			eltb.off('click').on('click', '.btn_bg_add', function(e){
				e.preventDefault();
				if($(this).hasClass('on')){
					$(this).removeClass('on');
					el.find('>.etc_prd_box').addClass('disno');
				}else{
					$(this).addClass('on');
					el.find('>.etc_prd_box').removeClass('disno');
					$('html, body').stop().animate({scrollTop:el.find('>.etc_prd_box').offset().top - 60});
				}
			});

			$('.ui_car_csene01 .etc_prd_box').on('click', '.btn_common__', function(e){
				e.preventDefault();
				eltb.find('.btn_bg_add').removeClass('on');
				el.find('>.etc_prd_box').addClass('disno');
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기- 계약조회]gkeyPad
	 * @description 장기계약명세조회
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LCAA120UM
	 * @example
	 * // 초기화
	 * etUI.gkeyPad.init();
	 */

	etUI.gkeyPad = (function(){
	 	var el;
	 	var result = 0;
	 	var currentEntry = ''
	 	updateScreen(result)

	 	function init(){
	 		el = $('.keypad_wrap .key_pad');
	 		$('.key_pad > input[type="text"]').val(0);
	 		$('.second input[type="text"].ipt_txt').val(0);

	 		$('#tab2').find('.ly_keypad').hide();
	 		bindEvents();
	 	}

	 	function bindEvents(){
	 		el.off('click').on('click', '.num_key a', function(e){
	 			e.preventDefault();

	 			var buttonPress = $(this).text();

	 			if($(this).hasClass('m_won')){
	 				buttonPress = $(this).find('.hide_txt').text();
	 			}

	 			currentEntry = currentEntry+buttonPress;

	 			if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
	 				numCommas(currentEntry)
	 			}else{
	 				updateScreen(currentEntry)
	 			}
	 		})

	 		if($('.ly_keypad').length == 0){
		 		$('.btn_key > .btn_del').off('click').on('click', function(e){
		 			e.preventDefault();
		 			currentEntry = currentEntry.substring(0,currentEntry.length-1)

		 			if(location.port == '8888'  || location.hostname.indexOf('10.51.153.43') != -1){
		 				numCommas(currentEntry)
		 			}else{
		 				updateScreen(currentEntry)
		 			}
		 		})
	 		}

	 		$('.keypad_wrap .btn_key > .btn_ok').off('click').on('click', function(e){
	 			e.preventDefault();
	 			var getVal = $('.key_pad > input[type="text"]').val();

	 			setValues(getVal, tabIdx, chkIdx)
	 		})

	 		$('#tab2').on('click', '.three li:eq(2) input[type="text"].ipt_txt', function(e){
	 			e.preventDefault();
	 			$('#tab2').find('.ly_keypad').fadeIn();
	 		})

	 		$('.ly_keypad').on('click', 'a.btn_key_close', function(e){
	 			e.preventDefault();
	 			$('#tab2').find('.ly_keypad').fadeOut();
	 		})
 		}

 		function numCommas(currentEntry){
			var displayValue = currentEntry.toString().replace(/\B(?=(\d{3})+(?!\d))/g,",");
			updateScreen(displayValue);
		}

 		function updateScreen(displayValue){
			var displayValue = displayValue.toString();
			$('.key_pad > input[type="text"]').val(displayValue);
			$('.second input[type="text"].ipt_txt').val(displayValue);
			$('#tab2 .three li:eq(2) input[type="text"].ipt_txt').val(displayValue);

		}

		function setValues(getVal, tabIdx, chkIdx){
			// $('.one_column table.tb_list tbody tbody').each(function(idx, obj){
			// 	// (this).find(idx-1)
			// 	console.log(idx)
			// })
			// $('.one_column table.tb_list').eq(1).find('tbody').eq(0).find('tr').eq(1).find('td').eq(0).remove('text');
			$('.one_column table.tb_list').eq(tabIdx).find('tbody').eq(chkIdx).find('tr').eq(1).find('td').eq(0).text(getVal);
		}

	 	return{
	 		init:init,
	 		setValues:setValues
	 	}
	})();

	/**
	 * @module [장기- 계약조회]contractSearch
	 * @description 장기계약명세조회
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LCAA120UM
	 * @example
	 * // 초기화
	 * etUI.contractSearch.init();
	 */

	etUI.contractSearch = (function(){
	 	var el;

	 	function init(){
	 		el = $('.docs_contents__');

	 		bindEvents();
	 	}

	 	function bindEvents(){
	 		el.on('click', '.pst_rel > .link_point', function(e){
	 			e.preventDefault();

	 			var tb_center;
	 			var bottomLength = $(this).closest('tbody').nextAll().length+1;
	 			var tbodyH =  $(this).closest('tbody').height();
	 			var bottomHeight = bottomLength * tbodyH;

	 			if($(this).closest('td').attr('rowspan') != undefined && $(this).closest('td').find('span.txt').length == 0){
	 				tb_center = $(this).closest('tbody').height()/2;
	 			}
	 			else if($(this).closest('td').attr('rowspan') != undefined && $(this).closest('td').find('span.txt').length != 0){
	 				tb_center = ($(this).closest('tr').height()/2)+18;
	 			}
	 			else if($(this).closest('td').attr('rowspan') == undefined){
	 				tb_center = $(this).closest('tr').height()/2;
	 			}

	 			if($(this).next('.grid_layer_popup').is(':visible')){
	 				$('.grid_layer_popup').hide().removeAttr('style');
	 				$('.grid_layer_popup').removeClass('bot');
	 			}else{
	 				$('.grid_layer_popup').hide().removeAttr('style');
	 				$(this).next('.grid_layer_popup').css({display:'block', opacity:0});

	 				if($(this).next('.grid_layer_popup').height()+5 > bottomHeight || $(this).closest('tbody').length-1){
	 					$('.grid_layer_popup').addClass('bot');
	 					$(this).next('.grid_layer_popup.bot').css({top:-($(this).next('.grid_layer_popup').height()-tb_center)});
	 				}else{
	 					$('.grid_layer_popup').removeClass('bot');
	 					$(this).next('.grid_layer_popup').css({top:tb_center});
	 				}

	 				$(this).next('.grid_layer_popup').css({opacity:1});
	 			}

	 			$(this).closest('.tb_scroll').css({overflowX:'visible'});

	 		})

	 		$(document).off('touchstart.layerpop').on('touchstart.layerpop', function(e){
	 			if($(e.target).closest('.grid_layer_popup').size() == 0){
					$('.tb_scroll').css({overflowX:'scroll'});
					$('.grid_layer_popup').hide().removeAttr('style');
	 				$('.grid_layer_popup').removeClass('bot');
				}
			});
 		}

	 	return{
	 		init:init
	 	}
	})();

	/**
	 * @module [공통]formSlideLayer
	 * @description 상세정보 layer 열기/닫기
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MCAA020US01
	 * @example
	 * // 초기화
	 * etUI.formSlideLayer.init();
	 */
	etUI.formSlideLayer = (function(){
		var el, type_check, liWidth;

		function init(){
			el = $('.ipt_layer01, .ipt_layer02');
			//20170907 mw
			type_check = el.closest('.full_td').length === 1;

			$('.ipt_ct_layer').hide();

			bindEvent();
		}

		function bindEvent(){
			el.off('click.listOpen').on('click.listOpen', '>.btn_more', function(e){
				e.preventDefault();
				//20170907 mw
				liWidth = $(this).closest('li').width() - 46;

				if(el.attr('class') ==='ipt_layer01'){
					var elWidth = $(this).width() ||$(this).outerWidth();
				}else if(el.attr('class') ==='ipt_layer02'){
					var elWidth = $(this).closest('li').width()-40;
				}

				if($(this).hasClass('dis')){
					return;
				}

				$(this).next('.ipt_ct_layer').css({width:elWidth});
				$(this).next('.ipt_ct_layer').show();

				//20170907 mw
				if(type_check){
					$(this).next('.ipt_ct_layer').css('width' , liWidth);
					$(document).off().on('click', function(e){
						if($('.ipt_layer02').has(e.target).length === 0 && $('.ipt_ct_layer').is(':visible')){
							$('.ipt_ct_layer').hide();
						}
					});
				}

			});

			el.off('click.listClose').on('click.listClose', '.ipt_ct_layer > a.btn_close', function(e){
				e.preventDefault();
				$(this).closest('.ipt_ct_layer').hide();
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [공통]tbSearch
	 * @description 조회 폼의 열기/닫기 버튼 이벤트 바인드
	 * @author
	 * @see http://localhost:8888/dsm/mdi.html#m|MABB090UP00
	 * @example
	 * // 초기화
	 * etUI.tbSearch.init();
	 */
	etUI.tbSearch = (function(){
		var el, btnCtr;

		function init(){
			el = $('.ui_tb_acc');
			btnCtr = el.find('.ui_btn_area > .btn_ctr');

			bindEvent();
		}

		function bindEvent(){
			btnCtr.off('click.tbSearch').on('click.tbSearch', function(e){
				e.preventDefault();

				var tbSearchs = $(this).closest(".ui_tb_acc").find(".ui_acc_cont .tb_search");

				tbSearchs.not(":eq(0)").css({
					"margin-top" : "-" + tbSearchs.not(":eq(0)").height() + "px",
					"opacity" : "0",
					"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
				});

				if($(this).hasClass("on")) {
					tbSearchs.not(":eq(0)").css({
						"margin-top" : "-" + tbSearchs.not(":eq(0)").height() + "px",
						"opacity" : "0",
						"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
					});
					setTimeout(function() {
						tbSearchs.not(":eq(0)").hide();
					}, 300);
					$(this).text("열기");
					$(this).removeClass("on");
				} else {
					tbSearchs.not(":eq(0)").show(0, function(){
						$(this).css({
							"margin-top" : "0px",
							"opacity" : "1",
							"transition" : "opacity 0.9s cubic-bezier(0.65, -0.01, 0.21, 1), margin-top 0.36s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
						});
					});
					$(this).text("닫기");
					$(this).addClass("on");
				}
			})

			btnCtr.each(function() {
				if($(this).hasClass("on")) {
					// $(this).trigger("click.tbSearch");
				}
			});
		}

		function preOpen(){
			$(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").show(0, function(){
				$(this).css({
					"margin-top" : "0px",
					"opacity" : "1",
					"transition" : "opacity 0.9s cubic-bezier(0.65, -0.01, 0.21, 1), margin-top 0.36s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
				});
			});
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").text("닫기");
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").addClass("on");
		}

		function disableOpen(){
			$(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").css({
				"margin-top" : "-" + $(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").height() + "px",
				"opacity" : "0",
				"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
			});
			setTimeout(function() {
				$(".ui_tb_acc").find(".ui_acc_cont .tb_search").not(":eq(0)").hide();
			}, 300);
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").text("열기");
			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").removeClass("on");

			$(".ui_tb_acc").find(".ui_btn_area > .btn_ctr").off('click').on('click', function(e){
				e.preventDefault();
			});
		}

		function preClose(){
			$(".ui_tb_acc").each(function(){
				if($(this).hasClass('basic')){
					return
				}else{
					$(this).find(".ui_acc_cont .tb_search").not(":eq(0)").css({
						"margin-top" : "-" + $(this).find(".ui_acc_cont .tb_search").not(":eq(0)").height() + "px",
						"opacity" : "0",
						"transition" : "opacity 0.15s cubic-bezier(0.94, 0, 1, 1), margin-top 0.3s cubic-bezier(0.65, -0.01, 0.21, 1.3)"
					});
					setTimeout(function() {
						$(this).find(".ui_acc_cont .tb_search").not(":eq(0)").hide();
					}, 300);
					$(this).find(".ui_btn_area > .btn_ctr").text("열기");
					$(this).find(".ui_btn_area > .btn_ctr").removeClass("on");
				}
			});
		}

		return{
			init:init,
			preOpen:preOpen,
			disableOpen:disableOpen,
			preClose:preClose
		}
	})();

	/**
	 * @module [공통]tipAccordian
	 * @description 알아두실사항 펼침/접힘
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#M|MCAA010UM
	 * @example
	 * // 초기화
	 * etUI.tipAccordian.init();
	 */
	etUI.tipAccordian = (function(){
		var el;

		function init(){

			el = $('.ui_tip_acc');

			el.find('.ui_acc_cont').hide();

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
	 * @see http://localhost:8888/dsm/mdi.html#g|UABA020UM00
	 * @example
	 * // 초기화
	 * etUI.commAccordian.init();
	 */
	etUI.commAccordian = (function(){
		var el, ellist, btnIdx;

		function init(){

			el = $('.ui_acc_type01');
			// elStep = $('.ui_acc_step');
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
			ellist.off('click.accEvent').on('click.accEvent', '>a.btn_acc', function(e){
				e.preventDefault();

				if($(this).hasClass('off')){
					return;
				}

				if($(this).next('.acc_cont').is(':visible')){
					el.removeClass('acc_open');
					ellist.removeClass('ui_open');
					$(this).removeClass('on');
					$(this).next('.acc_cont').slideUp(300);
				}else{
					$(this).addClass('on');
					$(this).next('.acc_cont').slideDown(300);
					if(!$('.side_con06').is(':visible')){
						$('html, body').animate({scrollTop:$(this).offset().top -50});
					}else{
						return;
					}
				}
			})

			el.off('click.btnClose').on('click.btnClose', '.btn_area > a.green', function(e){
				e.preventDefault();

				btnIdx = $(this).closest('li').index();

				if($(this).closest('.acc_noti').hasClass('ui_acc_step')){
					ellist.removeClass('ui_open');
					etcAccNextStep(btnIdx);
					return;
				}else{
					$(this).closest('li').find('>a.btn_acc').removeClass('on');
					$(this).closest('.acc_cont').slideUp(300);
				}
			})

			el.off('click.btnPrevStep').on('click.btnPrevStep', '.btn_area > a.btn_sm_line', function(e){
				e.preventDefault();

				btnIdx = $(this).closest('li').index();

				if($(this).closest('.acc_noti').hasClass('ui_acc_step')){
					etcAccPrevStep(btnIdx);
					return;
				}
			})
		}

		function etcAccPrevStep(btnIdx){
			if(ellist.eq(btnIdx-1).find('.acc_cont').is(':visible')){
				ellist.not(':eq('+(btnIdx-1)+')').find('>a.btn_acc').removeClass('on');
				ellist.not(':eq('+(btnIdx-1)+')').find('.acc_cont').hide();
			}else{
				ellist.find('>a.btn_acc').removeClass('on');
				ellist.find('.acc_cont').hide();
				ellist.eq(btnIdx-1).find('>a.btn_acc').addClass('on');
				ellist.eq(btnIdx-1).find('.acc_cont').slideDown(300);
			}
			setTimeout(function(){
				$('html, body').animate({scrollTop:ellist.eq(btnIdx-1).offset().top -50});
			},300)
		}

		function etcAccNextStep(btnIdx){
			if(btnIdx == 3){
				if(ellist.eq(0).find('.acc_cont').is(':visible')){
					ellist.eq(3).find('>a.btn_acc').removeClass('on');
					ellist.eq(3).find('.acc_cont').slideUp(300);
				}else{
					ellist.find('>a.btn_acc').removeClass('on');
					ellist.find('.acc_cont').slideUp(300);
					ellist.eq(0).find('>a.btn_acc').addClass('on');
					ellist.eq(0).find('.acc_cont').slideDown(300);
				}
				setTimeout(function(){
					$('html, body').animate({scrollTop:ellist.eq(0).offset().top -50});
				},300)
			}else{
				if(ellist.eq(btnIdx+1).find('.acc_cont').is(':visible')){
					ellist.eq(btnIdx).find('>a.btn_acc').removeClass('on');
					ellist.eq(btnIdx).find('.acc_cont').slideUp(300);
				}else{
					ellist.find('>a.btn_acc').removeClass('on');
					ellist.find('.acc_cont').slideUp(300);
					ellist.eq(btnIdx+1).find('>a.btn_acc').addClass('on');
					ellist.eq(btnIdx+1).find('.acc_cont').slideDown(300);
				}
				setTimeout(function(){
					$('html, body').animate({scrollTop:ellist.eq(btnIdx+1).offset().top -50});
				},300)
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

			if(el.length == 0){
				return;
			}
			$('table').each(function(){
				$(this).find('tbody:last').find('a.ico_question').next('.grid_layer_tooltip').addClass('bot');
			})
			$('.grid_layer_tooltip').hide();

			bindEvent();
		}

		function bindEvent(){
			questionBox.off('click','>a.ico_question').on('click', '>a.ico_question', function(e){
				e.preventDefault();
				if($(this).next('.grid_layer_tooltip').is(':visible')){
					$('.grid_layer_tooltip').hide();
				}else{
					$('.grid_layer_tooltip').hide();
					$(this).next('.grid_layer_tooltip').show();
				}
			})

			$(document).off('touchstart.eventTooltip').on('touchstart.eventTooltip', function(e){

				if($(e.target).closest('.question_bx').size() == 0){
					$('.grid_layer_tooltip').hide();
				}
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기- 가입설계]ltJoinPlan
	 * @description 상품선택 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LAAA020UM00
	 * @example
	 * // 초기화
	 * etUI.ltJoinPlan.init();
	 */
	etUI.ltJoinPlan = (function(){
		var el, productListBox, productInfoArea, searchArea, planList;

		function init(){

			el = $('.product_step_wrap');
			productListBox = el.find('.pr_list');
			productInfoArea = el.find('.pr_info');
			searchArea = el.find('.search_area');
			planList = el.find('.pr_plan')

			productListBox.find('> ul > li > a').removeClass('on');
			productInfoArea.find('.scroll_wrap > ul > li > a').removeClass('on');
			productInfoArea.find('.info').hide();
			planList.find('.scroll_wrap > ul > li > a').removeClass('on');

			el.css({width:1608});
			el.closest('.bx_pd').css({overflow:'hidden'});
			$('.pr_btn_view > a').addClass('on');

			bindEvents();
		}

		function bindEvents(){
			productListBox.on('click', '> ul > li > a', function(e){
				e.preventDefault();
				productListBox.find('> ul > li > a').removeClass('on');
				$(this).addClass('on');
			});

			productInfoArea.on('click', '.scroll_wrap > ul > li > a', function(e){
				e.preventDefault();
				productInfoArea.find('.scroll_wrap > ul > li > a').removeClass('on');
				$(this).addClass('on');
				productInfoArea.find('.info').show();
				// $('.pr_btn_view > a').addClass('on');
			});

			//검색버튼 이벤트
			searchArea.on('click', '.btn_sch', function(e){
				e.preventDefault();
				searchArea.find('.sch_close').hide();
				new TimelineMax()
				.set(searchArea.find('.sch_open'), {display:'block', autoAlpha:0}, 0)
				.set(searchArea.find('.sch_open > .inp_search'), {width:35}, 0)//242
				.set(searchArea.find('.sch_open > .inp_search > .ipt_txt'), {width:35}, 0)//242
				.to(searchArea.find('.sch_open'), .3, {autoAlpha:1}, .3)
				.to(searchArea.find('.sch_open > .inp_search'), .37, {width:242}, .3)
				.to(searchArea.find('.sch_open > .inp_search > .ipt_txt'), .37, {width:242}, .3)
			});

			//검색필드 닫기
			searchArea.find('.sch_open').on('click', '.btn_close', function(e){
				e.preventDefault();
				searchArea.find('.sch_open').hide();
				searchArea.find('.sch_close').fadeIn(350);
			})

			//상품명 선택
			planList.on('click', '.scroll_wrap > ul > li > a', function(e){
				e.preventDefault();
				planList.find('.scroll_wrap > ul > li > a').removeClass('on');
				$(this).addClass('on');
			});
		}

		function removeStyle(){
			el.closest('.bx_pd').removeAttr('style');

			$(window).on('scroll', function(e){
				e.preventDefault();

				if($(window).scrollLeft() > 300){
					$('.pr_btn_view > a:eq(0)').hide();
					$('.pr_btn_view > a:eq(1)').css({display:'inline-block'});
				}else if($(window).scrollLeft() <= 300){
					$('.pr_btn_view > a:eq(1)').hide();
					$('.pr_btn_view > a:eq(0)').css({display:'inline-block'});
				}
			});

			$('.pr_btn_view > a:eq(0)').on('click', function(e){
				e.preventDefault();
				$('html, body').animate({scrollLeft:601});
				$(this).hide();
				$('.pr_btn_view > a:eq(1)').css({display:'inline-block'});
			});

			$('.pr_btn_view > a:eq(1)').on('click', function(e){
				e.preventDefault();
				$('html, body').animate({scrollLeft:0});
				$(this).hide();
				$('.pr_btn_view > a:eq(0)').css({display:'inline-block'});
			});
		}

		function addStyle(){
			el.closest('.bx_pd').css({overflow:'hidden'});
			$('.pr_btn_view > a:eq(1)').hide();
			$('.pr_btn_view > a:eq(0)').css({display:'inline-block'});
			$(window).off('scroll');
			$('.pr_btn_view > a:eq(0)').off('click');
			$('.pr_btn_view > a:eq(1)').off('click');
		}

		return{
			init:init,
			removeStyle:removeStyle,
			addStyle:addStyle
		}
	})();

	/**
	 * @module [고객- 법인고객통합조회]corpTotalUi
	 * @description 조회/등록 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#k|KABC010UM
	 * @example
	 * // 초기화
	 * etUI.corpTotalUi.init();
	 */
	etUI.corpTotalUi = (function(){
		var firstColumn, firstColumnGraph, secondColumn, secondColumnCont;

		function init(){
			firstColumn = $('.two_column_layout').find('.one_column');
			firstColumnGraph = firstColumn.find('.customer_link_wrap, .graph_link_wrap');
			secondColumn = $('.two_column_layout').find('.two_column');
			secondColumnCont = secondColumn.find('>div');

			bindEvent();
		}

		function bindEvent(){
			firstColumnGraph.on('click', '> li > a', function(e){
				e.preventDefault();

				var targetList = $(this).closest('li');
				var targetId = targetList.index();
				var classArr = firstColumnGraph.attr('class').split(' ');

				firstColumnGraph.find('> li').removeClass('on');
				targetList.addClass('on');

				// firstColumnGraph.attr('class',firstColumnGraph.attr('class').replace(classArr[1],'step0'+(targetId+1)));
				firstColumnGraph.removeClass(classArr[1]);
				if($('.customer_link_wrap').length > 0){
					firstColumnGraph.addClass('step0'+(targetId+1));
					secondColumnCont.removeClass('on');
					secondColumnCont.eq(targetId).addClass('on');
				}else{
					firstColumnGraph.addClass('t0'+(targetId+1));
					secondColumn.find('>.info > div').removeClass('on');
					secondColumn.find('>.info > div').eq(targetId).addClass('on');
				}
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기- 계약설계]searchRegistBtn
	 * @description 조회/등록 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LABA010UP99
	 * @example
	 * // 초기화
	 * etUI.searchRegistBtn.init();
	 */
	etUI.searchRegistBtn = (function(){
		var el;

		function init(){
			el = $('.ly_btn_list');

			el.find('> a.btn_sm_line').removeClass('on');
			el.find('.list_link').hide();

			bindEvent();
		}

		function bindEvent(){
			el.each(function(){
				$(this).off('click').on('click', '> a.btn_sm_line', function(e){
					e.preventDefault();

					if($(this).next('.list_link').is(':visible')){
						$(this).removeClass('on');
						$(this).next('.list_link').hide();
					}else{
						$(this).addClass('on');
						$(this).next('.list_link').show();
					}
				});
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 가입설계(차량정보)]selectCarList
	 * @description 차량정보 선택 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA010UM02
	 * @example
	 * // 초기화
	 * etUI.selectCarList.init();
	 */
	etUI.selectCarList = (function(){
		var el, carInfoList, carStep, stepIdx, listIdx;

		function init(){
			el = $('.car_slt_group');
			carListArea = $('.car_slt_area');
			carInfoList = carListArea.find('>.car_step_area');
			carStep = carInfoList.find('.car_step');

			if(el.hasClass('on')){
				TweenMax.set('.car_slt_area', {display:'block'});
				TweenMax.set('.car_slt_area', {height:458});
			}else{
				TweenMax.set('.car_slt_area', {display:'none'});
				TweenMax.to('.car_slt_area', .37, {height:0});
			}

			// carStep.find('>.item_list > li > a').removeClass('on');

			// $('.grid_layer_tooltip').hide();
			setup();
		}

		function setup(){
			carStep.find('>.item_list').not(':eq(0)').hide();

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '.list > li >a', function(e){
				e.preventDefault();
				listBoxOpen();
			});

			carStep.off('click').on('click', '.item_list > li > a', function(e){
				e.preventDefault();
				stepIdx = $(this).closest('.step_list').index();
				listIdx = $(this).closest('li').index();

				selectStep(stepIdx, listIdx);
				stepScroll(stepIdx, listIdx);
			});
		}

		function listBoxOpen(){
			if(el.hasClass('on')){
				return;
			}else{
				el.addClass('on');
				TweenMax.set('.car_slt_area', {display:'block'});
				TweenMax.to('.car_slt_area', .37, {height:458});
			}
		}

		function selectStep(idx, listIdx){
			if(carStep.eq(idx).find('>.item_list > li').eq(listIdx).find('>a').text() == " "){
				carStep.eq(idx).find('>.item_list > li > a').removeClass('on');
				return false;
			}else if(carStep.eq(idx).find('>.item_list > li').eq(listIdx).find('>a').text() != " "){
				//각 step별 항목 선택
				carStep.eq(idx).find('>.item_list > li > a').removeClass('on');
				carStep.eq(idx).find('>.item_list > li').eq(listIdx).find('>a').addClass('on');
				// carStep.eq(idx).find('>.item_list > li').eq(listIdx).find('>a.on').
				//선택후 다음 step 보여주기
				for(var j=0; j <= idx; j++){
					carStep.eq(j+1).find('>.item_list').show();
				}

				//특정 step에서 다시 선택시 다음 step들에 대해 초기화
				for(var i=idx; i <= carStep.length-1; i++){
					carStep.eq(i+2).find('>.item_list').hide();
					carStep.eq(i+1).find('>.item_list > li > a').removeClass('on');
				}
			}
		}

		function listBoxClose(){
			TweenMax.to('.car_slt_area', .37, {height:0, onComplete:function(){
				el.removeClass('on');
				$('.car_slt_area').css({display:'none'});
			}})
		}

		function stepScroll(idx, listIdx){
			carStep.eq(idx).stop().animate({scrollTop:carStep.eq(idx).find('>.item_list > li').height() * listIdx});
		}

		return{
			init:init,
			selectStep:selectStep,
			listBoxOpen:listBoxOpen,
			listBoxClose:listBoxClose,
			stepScroll:stepScroll
		}
	})();

	/**
	 * @module [자동차 - 가입설계(차량코드찾기)]selectCarCode
	 * @description 차량코드찾기 선택 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA100UP00
	 * @example
	 * // 초기화
	 * etUI.selectCarCode.init();
	 */
	etUI.selectCarCode = (function(){
		var el, carListStep, carDetailSel, registCarInfo, carName, carNameBox, stepIdx, listIdx;

		function init(){
			el = $('.car_name_search');
			carListStep = el.find('.car_step');
			carDetailSel = $('.ui_car_detail');
			registCarInfo = $('.ui_car_acco01');
			carName = registCarInfo.find('.car_name');
			carNameBox = registCarInfo.find('.acc_cont');

			// if($('input.ui_add_parts').prop('checked')){
			// 	$('.etc_prd_box').removeClass('disno');
			// }else if(!$('input.ui_add_parts').prop('checked')){
			// 	$('.etc_prd_box').addClass('disno');
			// }

			bindEvent();

		}

		function bindEvent(){
			carName.off('click').on('click', '>a.btn_acco', function(e){
				e.preventDefault();
				// if(carNameBox.is(':visible')){
				// 	$(this).addClass('on');
				// 	carNameBox.slideDown();
				// }else{
				// 	$(this).removeClass('on');
				// 	carNameBox.slideUp();
				// }

				// if($(this).hasClass('on')){
				// 	$(this).removeClass('on');
				// 	carNameBox.slideUp();
				// }else{
				// 	$(this).addClass('on');
				// 	carNameBox.slideDown();
				// }
			});

			carListStep.off('click').on('click', '>.item_list > li > a', function(e){
				e.preventDefault();
				stepIdx = $(this).closest('.car_step').parent('li').index();
				listIdx = $(this).closest('li').index();

				selectStep(stepIdx, listIdx);
				stepScroll(stepIdx, listIdx);
			});

			carDetailSel.off('click').on('click', 'table > tbody > tr', function(e){
				e.preventDefault();
				var cellIdx = $(this).index()
				carDetailSel.find('table > tbody > tr').removeClass('selected');
				$(this).addClass('selected');
				carDetailSel.find('.tbody_wrap__').stop().animate({scrollTop:$(this).height() * cellIdx});
			});

			// $('input.ui_add_parts').off('click').on('click', function(){
			// 	if($(this).prop('checked')){
			// 		// $('.etc_prd_box').slideDown();
			// 		$('.etc_prd_box').removeClass('disno');
			// 	}else{
			// 		// $('.etc_prd_box').slideUp();
			// 		$('.etc_prd_box').addClass('disno');
			// 	}
			// });
		}

		function carDetailEvt(idx){
			carDetailSel.find('.tbody_wrap__').stop().animate({scrollTop:carDetailSel.find('tbody').height() * idx});
		}

		function selectStep(idx, listIdx){
			//각 step별 항목 선택
			carListStep.eq(idx).find('>.item_list > li > a').removeClass('on');
			carListStep.eq(idx).find('>.item_list > li').eq(listIdx).find('>a').addClass('on');

			//선택후 다음 step 보여주기
			for(var j=0; j <= idx; j++){
				carListStep.eq(j+1).find('>.item_list').show();
			}

			//특정 step에서 다시 선택시 다음 step들에 대해 초기화
			for(var i=idx; i <= carListStep.length-1; i++){
				carListStep.eq(i+2).find('>.item_list').hide();
				carListStep.eq(i+1).find('>.item_list > li > a').removeClass('on');
			}
		}

		function listBoxClose(){
			TweenMax.to('.car_slt_area', .37, {height:0, onComplete:function(){
				el.removeClass('on');
				$('.car_slt_area').css({display:'none'});
			}})
		}

		function stepScroll(idx, listIdx){
			carListStep.eq(idx).stop().animate({scrollTop:carListStep.eq(idx).find('>.item_list > li').height() * listIdx});
		}

		return{
			init:init,
			carDetailEvt:carDetailEvt,
			selectStep:selectStep,
			listBoxClose:listBoxClose,
			stepScroll:stepScroll
		}
	})();

	/**
	 * @module [일반 - 재물보험간편설계(가입대상선택)]selectGoodsList
	 * @description 가입할 대상(재물) 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA143US00
	 * @example
	 * // 초기화
	 * etUI.selectGoodsList.init();
	 */
	etUI.selectGoodsList = (function(){
		var el, elList;

		function init(){
			el = $('.object_wrap');
			elList = el.find('>.list');

			elList.removeClass('on');

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '>.list input[type="checkbox"]', function(){
				if($(this).prop('checked') == true){
					$(this).prop({checked:true});
					$(this).closest('.list').addClass('on');
				}
				else{
					$(this).closest('.list').removeClass('on');
					$(this).prop({checked:false});
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [일반 - 재물보험간편설계(급수선택)]selectBuildList
	 * @description 급수선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA143US02
	 * @example
	 * // 초기화
	 * etUI.selectBuildList.init();
	 */
	etUI.selectBuildList = (function(){
		var el, leftColumn, rightColumn;

		function init(){
			el = $('.building_wrap');
			leftColumn = el.find('.one_column');
			rightColumn = el.find('.two_column');

			leftColumn.find('.item_list > li > a').removeClass('on');
			rightColumn.find('>.list > a').removeClass('on');

			bindEvent();
		}

		function bindEvent(){
			// leftColumn.off('click').on('click', '>.list > a', function(e){
			// 	e.preventDefault();
			// 	if($(this).hasClass('on')){
			// 		$(this).removeClass('on');
			// 	}
			// 	else{
			// 		rightColumn.find('>.list > a').removeClass('on');
			// 		$(this).addClass('on');

			// 	}
			// });
			leftColumn.find('.item_list').each(function(){
				$(this).off('click').on('click', 'li > a', function(e){
					e.preventDefault();
					if($(this).text() == " "){
						$(this).closest('ul').find('li > a').removeClass('on');
						// $(this).remove().append("&nbsp;")
						// console.log($(this).find('&nbsp;').length)
						return false;
					}else if($(this).text() != " "){
						$(this).closest('ul').find('li > a').removeClass('on');
						$(this).addClass('on');
					}
				})
			})

			rightColumn.off('click').on('click', '>.list > a', function(e){
				e.preventDefault();
				if($(this).hasClass('on')){
					$(this).removeClass('on');
				}
				else{
					rightColumn.find('>.list > a').removeClass('on');
					$(this).addClass('on');

				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기 - 계약설계(설계보관 - 계약설계(담보))]selectSearchEvt
	 * @description 검색박스 및 셀렉트 리스트 펼침
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LAAA010UM02
	 * @example
	 * // 초기화
	 * etUI.selectSearchEvt.init();
	 */
	etUI.selectSearchEvt = (function(){
		var serchBox, selectBox;

		function init(){
			serchBox = $('.search_zone');
			selectBox = $('.select_menu');
			warrantList = $('.warrant_wrap').find('.one_column');
			warrantInfoArea = $('.warrant_wrap').find('.two_column');

			serchBox.find('.search_bx').hide();

			bindEvent();
		}

		function bindEvent(){
			selectBox.off('click').on('click', '>.tit', function(e){
				e.preventDefault();
				var selBody = $(this).next('.select_body');
				if(selBody.is(':visible')){
					$(this).next('.select_body').slideUp(250);
					$(this).removeClass('on');
				}else{
					selectBox.find('.select_body').slideUp(250);
					selectBox.find('>.tit').removeClass('on');
					$(this).next('.select_body').slideDown(250);
					$(this).addClass('on');
				}
			});

			$(document).off('touchstart.selMenu').on('touchstart.selMenu', function(e){
	 			if($(e.target).closest('.select_menu').size() == 0){
					selectBox.find('.select_body').slideUp(250);
					selectBox.find('>.tit').removeClass('on');
				}
			});

			serchBox.off('click').on('click', '>.btn_bg_search', function(e){
				e.preventDefault();
				if($(this).closest('ul').hasClass('spc_agr_list')){
					var inputW = $(this).closest('li').width()-40;
				}else{
					var inputW = 308;
				}
				new TimelineMax()
				.set($(this).next('.search_bx').find('> .ipt_txt'), {width:0}, 0)
				.set($(this).next('.search_bx'), {display:'block', autoAlpha:0}, 0)
				.to($(this).next('.search_bx'), .3, {autoAlpha:1}, .3)
				.to($(this).next('.search_bx').find('> .ipt_txt'), .37, {width:inputW}, .3)
				.set($(this), {className:'+=close'}, .37)
			});

			serchBox.off('click', '.btn_bg_del').on('click', '.btn_bg_del', function(e){
				e.preventDefault();
				new TimelineMax()
				.to($(this).closest('.search_bx').find('> .ipt_txt'), .37, {width:0}, 0)
				.to($(this).closest('.search_bx'), .3, {autoAlpha:0}, .2)
				.set($(this).closest('.search_bx'), {display:'none'}, .37)
				.set($(this), {className:'-=close'}, .37)
			});

			//요약내역 열기/닫기
			warrantInfoArea.off('click').on('click', '>.btn_area_open > a', function(e){
				e.preventDefault();
				if(warrantInfoArea.find('.info_cont, .bx_tab').is(':visible')){
					infoClose();
					// TweenMax.to(warrantList, .3, {width:'96%'});
					// TweenMax.set(warrantInfoArea, {width:'0%'});
					// warrantInfoArea.find('.info_cont, .bx_tab').hide();
					// $(this).closest('.btn_area_open').addClass('close');
				}else{
					infoOpen();
					// $(this).closest('.btn_area_open').removeClass('close');
					// TweenMax.to(warrantList, .2, {width:'75%'});
					// TweenMax.to(warrantInfoArea, .2, {width:'25%', onComplete:function(){
					// 	warrantInfoArea.find('.info_cont, .bx_tab').show();
					// }});
				}
			});
		}

		function infoOpen(){
			$('.btn_area_open').removeClass('close');
			TweenMax.to(warrantList, .2, {width:'75%'});
			TweenMax.to(warrantInfoArea, .2, {width:'25%', onComplete:function(){
				warrantInfoArea.find('.info_cont, .bx_tab').show();
			}});
		}

		function infoClose(){
			TweenMax.to(warrantList, .3, {width:'96%'});
			TweenMax.set(warrantInfoArea, {width:'0%'});
			warrantInfoArea.find('.info_cont, .bx_tab').hide();
			$('.btn_area_open').addClass('close');
		}

		return{
			init:init,
			infoOpen:infoOpen,
			infoClose:infoClose
		}
	})();

	/**
	 * @module [자동차 - 가상계좌선택]accountChoicePop
	 * @description 가상계좌 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MABB071UP00
	 * @example
	 * // 초기화
	 * etUI.accountChoicePop.init();
	 */
	etUI.accountChoicePop = (function(){
		var el;

		function init(){
			el = $('.account_choice');

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '> ul > li >a', function(e){
				e.preventDefault();
				el.find('> ul > li').removeClass('on');
				$(this).closest('li').addClass('on');
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 청약설계(계좌입력)]accountSetPop
	 * @description 계좌입력
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MABA010UP00
	 * @example
	 * // 초기화
	 * etUI.accountSetPop.init();
	 */
	etUI.accountSetPop = (function(){
		var el;

		function init(){
			el = $('.account_wrap');
			elList = el.find('.list');

			elList.removeClass('on');

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '.list input[type="checkbox"]', function(){
				if($(this).prop('checked') == true){
					$(this).prop({checked:true});
					$(this).closest('.list').addClass('on');
				}
				else{
					$(this).closest('.list').removeClass('on');
					$(this).prop({checked:false});
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 가입설계(전체비교_원계약, 비교설계)]originCaseActivePop
	 * @description 원설계 열기/닫기
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAB010UP00
	 * @example
	 * // 초기화
	 * etUI.originCaseActivePop.init();
	 */
	etUI.originCaseActivePop = (function(){
		var el;

		function init(){
			el = $('.origin_case');

			bindEvent();
		}

		function bindEvent(){
			el.off('click').on('click', '.btn_sm_icon', function(){
				if(el.hasClass('on')){
					el.removeClass('on');
				}
				else{
					el.addClass('on');
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기 - 가입설계(설계명세)]searchPlanList
	 * @description 설계명세
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LAAA030UM99
	 * @example
	 * // 초기화
	 * etUI.searchPlanList.init();
	 */
	etUI.searchPlanList = (function(){
		var el, elTable, tableBody, deatilBox;

		function init(){
			el = $('.ui_search_list');
			elTable = el.find('table.li_tb');
			tableBody = elTable.find('tbody');
			deatilBox = $('.detail_info01');

			tableBody.find('tr').removeClass('active');

			bindEvent();
		}

		function bindEvent(){
			// tableBody.each(function(idx,obj){
			// 	var index = $(this).index()
			// 	$(this).attr({'data-row':index})
			// })
			// for(var i=0; i <= tableBody.length; i++){
			// 	tableBody.eq(i).attr({'data-row':i+1})
			// }

			elTable.off('click').on('click', 'tbody input[type="checkbox"]', function(){
				var rowIndex = $(this).closest('tbody').index()-1;
				setClass(rowIndex);
			});

			deatilBox.off('click').on('click', '>.btn_line > .btn_ctr', function(e){
				e.preventDefault();
				var c1Height = deatilBox.find('>.c01').height();

				if($(this).hasClass('on')){
					new TimelineMax()
					.to(deatilBox.find('>.c01'), .37, {display:'block', autoAlpha:1, marginTop:0}, 0)
					.to(deatilBox.find('>.c02'), .37, {autoAlpha:0}, .2)
					.to(deatilBox.find('>.c02'), .37, {display:'none'}, .37)
					.set($(this), {className:'-=on'}, .37)
					$(this).text('다음');
				}else{
					new TimelineMax()
					.set(deatilBox.find('>.c02'), {autoAlpha:0}, 0)
					.to(deatilBox.find('>.c01'), .37, {autoAlpha:0, marginTop:-c1Height}, 0)
					.to(deatilBox.find('>.c01'), .37, {display:'none'}, .37)
					.to(deatilBox.find('>.c02'), .37, {autoAlpha:1, display:'block'}, 0)
					.set($(this), {className:'+=on'}, .2)
					$(this).text('이전');
				}

			})
		}

		function setClass(rowIndex){
			if(tableBody.eq(rowIndex).find('input[type="checkbox"]').prop('checked')){
				tableBody.eq(rowIndex).find('tr').addClass('active');
			}else{
				tableBody.eq(rowIndex).find('tr').removeClass('active');
			}
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기 - 온라인발급]issueSelect
	 * @description 발급물 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|HAAA002UM00
	 * @example
	 * // 초기화
	 * etUI.issueSelect.init();
	 */
	etUI.issueSelect = (function(){
		var el, listBox;

		function init(){
			el = $('.online_list_wrap');
			listBox = el.find('.list_box');

			// tableBody.find('tr').removeClass('active');

			bindEvent();
		}

		function bindEvent(){

			el.each(function(idx, obj){
				$(this).off('click').on('click', '.step > ul > li input[type="checkbox"]', function(){
					var rowIndex = $(this).closest('li').index();
					var tabIdx = idx;
					setClass(tabIdx, rowIndex);
				});
			})

		}

		function setClass(tabIdx,rowIndex){
			if(el.eq(tabIdx).find('.step > ul > li').eq(rowIndex).find('input[type="checkbox"]').prop('checked')){
				el.eq(tabIdx).find('.step > ul > li').eq(rowIndex).addClass('on');
			}else{
				el.eq(tabIdx).find('.step > ul > li').eq(rowIndex).removeClass('on');
			}
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 가입특약]drvInfoSelect
	 * @description 운전자 정보 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA010UM03
	 * @example
	 * // 초기화
	 * etUI.drvInfoSelect.init();
	 */
	etUI.drvInfoSelect = (function(){
		var el, listBox;

		function init(){
			el = $('.ip_drv_scroll');

			// tableBody.find('tr').removeClass('active');

			bindEvent();
		}

		function bindEvent(){

			el.each(function(idx, obj){
				$(this).off('click').on('click', '.info_list > li > a', function(e){
					e.preventDefault();
					var rowIndex = $(this).closest('li').index();
					var columnIdx = idx;
					setClass(columnIdx, rowIndex);
				});
			})

		}

		function setClass(columnIdx,rowIndex){
			if(el.eq(columnIdx).find('.info_list > li').eq(rowIndex).find('a').hasClass('on')){
				// el.eq(columnIdx).find('.info_list > li').eq(rowIndex).find('a').removeClass('on');
			}else{
				el.eq(columnIdx).find('.info_list > li').find('a').removeClass('on');
				el.eq(columnIdx).find('.info_list > li').eq(rowIndex).find('a').addClass('on');
			}

			stepScroll(columnIdx,rowIndex)
		}

		function stepScroll(idx, listIdx){
			el.eq(idx).stop().animate({scrollTop:(el.eq(idx).find('>.info_list > li').height()+2) * listIdx})
		}

		function etcSpcHeight(){
			if($('.etc_spc_agr .spc_agr_list > li').length > 9){
				$('.spc_agr_list').css({height: 141, overflow:'auto'});
			}else{
				$('.spc_agr_list').removeAttr('style');
			}
		}

		return{
			init:init,
			stepScroll:stepScroll,
			etcSpcHeight:etcSpcHeight
		}
	})();

	etUI.uiCpdTable = (function(){
		var autoEl, total, check, tb_width, tb_length, wrap_width;

		function init(){
			tb_width = [];
			tb_length = $('.cpd_tb .wrap_tb').length;
			wrap_width = $('.cpd_tb').width();

			bindEvent();
		}

		function bindEvent(){
			eachTb();
			// clickEvent();
		}

		function eachTb(){
			$('.cpd_tb .wrap_tb').each(function(i , val){
				check = $(this).attr('style');
				if(check === undefined){
					autoEl = $(this);
				} else {
					tb_width[i] = $(this).width();
					wrap_width -= tb_width[i];

					$(this).css('width',tb_width[i]);
					// if(i === tb_length-1){
					// 	console.log(autoEl)
					// 	$(this).css('width',wrap_width);
					// }
				}
				return wrap_width;
			});
		}

		return{init:init}
	})();

	/**
	 * @module [자동차 - 차량정보(할인할증산출내역 요율수정)]discountRatioSel
	 * @description 운전자 정보 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA030UP01
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA030UP02
	 * @example
	 * // 초기화
	 * etUI.discountRatioSel.init();
	 */
	etUI.discountRatioSel = (function(){
		var el;

		function init(){
			el = $('.ui_chk_etcbox');

			bindEvent();
		}

		function bindEvent(){
			el.each(function(idx, obj){
				$(this).off('click.open').on('click.open', 'input[type="checkbox"]', function(){
					var chkIdx = $(this).closest('label').index()
					var columnIdx = idx;

					if(chkIdx == 0){
						setClass(columnIdx);
					}
				});
			})
		}

		function setClass(columnIdx){
			if(el.eq(columnIdx).find('label').eq(0).find('input[type="checkbox"]').prop('checked')){
				$('.etc_prd_box').eq(columnIdx).removeClass('disno');
			}else{
				$('.etc_prd_box').eq(columnIdx).addClass('disno');
			}
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 가입특약(운전자정보 입력)]drvInfoInput
	 * @description 운전자정보 입력
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA160UP00
	 * @example
	 * // 초기화
	 * etUI.drvInfoInput.init();
	 */
	etUI.drvInfoInput = (function(){
		var el;

		function init(){
			el = $('.ui_list_compo01');

			if(el.length == 0){
				return;
			}

			if($('input[type="checkbox"]').prop('checked')){
				$('.etc_prd_box').removeClass('disno');
			}else{
				$('.etc_prd_box').addClass('disno');
			}

			bindEvents();
		}

		function bindEvents(){
			el.off('click').on('click', '>ul>li>a', function(e){
				e.preventDefault();
				el.find('>ul>li>a').removeClass('on');
				$(this).addClass('on');
			});

			$('input[type="checkbox"]').off('click.chkInput').on('click.chkInput', function(){
				if($('input[type="checkbox"]').prop('checked')){
					$('.etc_prd_box').removeClass('disno');
				}else{
					$('.etc_prd_box').addClass('disno');
				}
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 가입담보(담보비교)]planInsuSelect
	 * @description 담보선택 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAB070UP00
	 * @example
	 * // 초기화
	 * etUI.planInsuSelect.init();
	 */
	etUI.planInsuSelect = (function(){
		var el, insuList, insuOptionList, firstPlanList, comparePlanList;

		function init(){
			el = $('.insul_cpr');
			insuList = el.find('.insur_choice');
			insuOptionList = insuList.find('.option_area');
			firstPlanList = el.find('.first');
			comparePlanList = el.find('.cpr_plan');

			if(el.length == 0){
				return;
			}

			bindEvents();
		}

		function bindEvents(){
			insuList.off('click').on('click', '.list>li>a', function(e){
				e.preventDefault();
				insuList.find('.list>li').removeClass('on');
				$(this).closest('li').addClass('on');
			});

			insuOptionList.on('click', '>.option>ul>li>a', function(e){
				e.preventDefault();
				insuOptionList.find('>.option>ul>li').removeClass('on');
				$(this).closest('li').addClass('on');
			});

			firstPlanList.on('click', '.list>li>a', function(e){
				e.preventDefault();
				firstPlanList.find('>.list>li').removeClass('on');
				$(this).closest('li').addClass('on');
			});

			comparePlanList.on('click', '.list>li>a', function(e){
				e.preventDefault();
				comparePlanList.find('>.list>li').removeClass('on');
				$(this).closest('li').addClass('on');
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [공통]floatPayResult
	 * @description 결제선택 영수대상액 플로팅
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LCAE110UP01
	 * @example
	 * // 초기화
	 * etUI.floatPayResult.init();
	 */
	etUI.floatPayResult = (function(){
		var el, st, restricHeight;

		function init(){
			el = $('.ui_scrolling_area, .payment_sum');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){
			$(window).off('touchmove.evtScroll touchend.evtScroll scroll.evtScroll').on('touchmove.evtScroll touchend.evtScroll scroll.evtScroll', function(){
				st = $(this).scrollTop();
				restricHeight = $('.one_column').outerHeight() - el.outerHeight();
				// restricHeight = $(document).outerHeight() - $('.one_column').outerHeight() -el.offset().top
				// console.log(el.offset().top)
				// console.log(restricHeight)
				// console.log(- el.outerHeight()-$('.one_column').outerHeight())
				// console.log(st)
				if(st < $('.ui_floating_sc').offset().top){
					return;
				}else{
					TweenMax.to(el, 0.3, {top : st-$('.ui_floating_sc').offset().top, ease:Power2.easeOut});
					// console.log($('.payment').offset().top)
				}
			});

			$('.msg_box__').off('touchmove.evtScrollpop touchend.evtScrollpop scroll.evtScrollpop').on('touchmove.evtScrollpop touchend.evtScrollpop scroll.evtScrollpop', function(){
				st = $(this).scrollTop();
				restricHeight = $('.one_column').outerHeight() - el.outerHeight();

				if(st > restricHeight){
					return;
				}else{
					TweenMax.to(el, 0.3, {top : st, ease:Power2.easeOut});
				}
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 간편산출]insulPrdSelEvt
	 * @description 산출보험료 담보 선택
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA220UM00
	 * @example
	 * // 초기화
	 * etUI.insulPrdSelEvt.init();
	 */
	etUI.insulPrdSelEvt = (function(){
		var el, elList, prdListArea;

		function init(){
			el = $('.ccl_insul_prd');
			sceneArea = $('.ui_scene_group');
			scene01 = sceneArea.find('.scene01');
			scene02 = sceneArea.find('.scene02');
			elList = el.find('.prd_list');
			prdListArea = elList.find('.ccl_cont');

			if(el.length == 0){
				return;
			}

			scene01.find('.btn_scene_area > a').addClass('on').hide();
			scene02.hide();

			bindEvent();
		}

		function bindEvent(){
			prdListArea.each(function(){
				$(this).off('click').on('click', '.result_box > .rs_box > a', function(e){
					e.preventDefault();
					$(this).closest('.ccl_cont').find('.result_box > .rs_box > a').removeClass('on');
					$(this).addClass('on');
				});
			});

			//가입 담보 보기
			scene01.off('click.scene01').on('click.scene01', '.btn_scene_area > a', function(e){
				e.preventDefault();
				downMotion();
			});

			//입력 정보 보기
			scene02.off('click.scene02').on('click.scene02', '.btn_scene_area > a', function(e){
				e.preventDefault();
				UpMotion();
			});

			scene01.off('click.result').on('click.result', '.btn_area > a', function(e){
				e.preventDefault();
				scene01.find('.btn_scene_area > a').show();
				TweenMax.to('html, body', .37, {scrollTop:$('.scene01 .btn_scene_area').offset().top});
				downMotion();
			})
		}

		function downMotion(){
			new TimelineMax()
			.set(scene02, {display:'block', autoAlpha:1}, 0)
			.to('html, body', .37, {scrollTop:$('.scene02 .btn_scene_area').offset().top, marginTop:55}, .1)
			.to(scene01, .37, {autoAlpha:0}, .05)
			.to(scene01, .1, {display:'none'}, .1)
			.set('.scene02 .btn_scene_area > a', {className:'-=on'}, .37)
		}

		function UpMotion(){
			new TimelineMax()
			.set(scene01, {display:'block', autoAlpha:1}, 0)
			.fromTo('html, body', .37, {scrollTop:$('.scene02 .btn_scene_area').offset().top},{scrollTop:0}, .1)
			.set('html, body', {marginTop:0}, .15)
			.to(scene02, .37, {autoAlpha:0}, .05)
			.to(scene02, .1, {display:'none'}, .1)
			.set('.scene01 .btn_scene_area > a', {className:'+=on'}, .37)
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 자유설계(플랜설계)]freePlanBox
	 * @description 자유설계(플랜설계) 영역 숨기기/감추기
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA110UP00
	 * @example
	 * // 초기화
	 * etUI.freePlanBox.init();
	 */
	etUI.freePlanBox = (function(){
		var el;

		function init(){
			el = $('.ui_plan_chk');

			if(el.length == 0){
				return;
			}

			if(el.find('input[type="checkbox"]').prop('checked')){
				$('.two_column_layout').show();
			}else{
				$('.two_column_layout').hide();
			}

			bindEvent();
		}

		function bindEvent(){
			el.on('click', 'input[type="checkbox"]', function(){
				if($(this).prop('checked')){
					$('.two_column_layout').show();
				}else{
					$('.two_column_layout').hide();
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [일반 - 가입설계(적하가입설계)]mortgageBtnEvt
	 * @description 관세담보입력 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA702US00
	 * @example
	 * // 초기화
	 * etUI.mortgageBtnEvt.init();
	 */
	etUI.mortgageBtnEvt = (function(){
		var el;

		function init(){
			el = $('.ui_mortgage_evt');

			if(el.length == 0){
				return;
			}

			el.find('.v0').hide();
			el.find('.btn_tb_line').hide();

			bindEvent();
		}

		function bindEvent(){
			el.off('click.openEvt').on('click.openEvt', '.btn_tb', function(e){
				e.preventDefault();
				$(this).hide();
				el.find('.v0').show();
				el.find('.btn_tb_line').show();
			})

			el.off('click.closeEvt').on('click.closeEvt', '.btn_tb_line', function(e){
				e.preventDefault();
				$(this).hide();
				el.find('.v0').hide();
				el.find('.btn_tb').show();
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 계약조회(계약상세조회)]workChangeBtnEvt
	 * @description 업무전환 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA702US00
	 * @example
	 * // 초기화
	 * etUI.workChangeBtnEvt.init();
	 */
	etUI.workChangeBtnEvt = (function(){
		var el;

		function init(){
			el = $('.layer_pop_bx');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){
			el.off('click.openEvt').on('click.openEvt', '>a', function(e){
				e.preventDefault();
				if(el.find('.layer_pop_list').is(':visible')){
					$(this).closest(el).removeClass('open');
				}else{
					$(this).closest(el).addClass('open');
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [일반 - 가입설계(컨소시엄담보사항)]etcTextBtnEvt
	 * @description  비고 영역 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA321UP00
	 * @example
	 * // 초기화
	 * etUI.etcTextBtnEvt.init();
	 */
	etUI.etcTextBtnEvt = (function(){
		var el;

		function init(){
			el = $('.rmks_textarea, .box_textarea');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){
			el.off('click.openText').on('click.openText', '>a', function(e){
				e.preventDefault();

				var txtAreaPosition = $(this).closest('table.tb_list').width() - $(this).closest('td').width();

				if($(this).closest(el).hasClass('open')){
					el.find('>a').removeClass('btn_bg_add04').addClass('btn_bg_add');
					el.removeClass('open');
					el.find('>a').removeClass('on');
				}else{
					if($(this).hasClass('btn_bg_add04')){
						return;
					}
					el.find('>a').removeClass('btn_bg_add').addClass('btn_bg_add04');
					el.removeClass('open');
					el.find('>a').removeClass('on');
					$(this).closest(el).addClass('open');
					$(this).addClass('on');
					$(this).removeClass('btn_bg_add04').addClass('btn_bg_add');
					$(this).prev('.txt_area').css({left:txtAreaPosition-1});
				}
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [일반 - 가입설계(컨소시엄담보사항)]jobTypeSearch
	 * @description 업종선택 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA143US01
	 * @example
	 * // 초기화
	 * etUI.jobTypeSearch.init();
	 */
	etUI.jobTypeSearch = (function(){
		var el, elBox;

		function init(){
			el = $('.type_per');
			elBox =$('.wrap_auto_input');

			// if(el.length == 0){
			// 	return;
			// }

			$('.wrap_auto_input').find('>.box').hide();

			bindEvent();
		}

		function bindEvent(){
			$('.wrap_auto_input > input[type="text"]').on('keyup', function(e){
				// $('.wrap_auto_input').find('>.box').show();
				var eventKeyArray = [9, 16, 17, 18, 32, 33, 34, 35, 36, 37, 38, 39, 40, 45, 46];
				if(e.keyCode === eventKeyArray[0] && $(this).val() == ''){

					$('.wrap_auto_input').find('>.box').hide();
				}

				for(var i=0;i<eventKeyArray.length;i++){

					if(e.keyCode === eventKeyArray[i]){
						e.preventDefault();
						return;
					}
				}

				if($(this).val() != '' ){
					$('.wrap_auto_input').find('>.box').show();
				}else{
					$('.wrap_auto_input').find('>.box').hide();
				}
			})

			$('.wrap_auto_input').find('>.box').off('click.txtInput').on('click.txtInput', '>ul>li>a', function(e){
				e.preventDefault();
				var listText = $(this).text();

				$('.wrap_auto_input > input[type="text"]').val(listText);
				$('.wrap_auto_input').find('>.box').hide();
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [일반 - 가입설계(유형별담보전체보기)]secTypeView
	 * @description 유형별담보보기 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#g|GAAA318US00
	 * @example
	 * // 초기화
	 * etUI.secTypeView.init();
	 */
	etUI.secTypeView = (function(){
		var el, elBox;

		function init(){
			el = $('.section');
			trBtn =$('.tb_acc_list');

			if(trBtn.length == 0){
				return;
			}

			trBtn.removeClass('on');

			bindEvent();
		}

		function bindEvent(){
			el.each(function(){
				$(this).off('click.trView').on('click.trView', '.tb_acc_list > td > span.btn_open', function(e){
					e.preventDefault();

					if($(this).closest('.tb_acc_list').hasClass('on')){
						$(this).closest('.tb_acc_list').removeClass('on')
					}else{
						$(this).closest('.tb_acc_list').addClass('on')
					}
				})

				$(this).off('click.trAllView').on('click.trAllView', '.btn_line_open', function(e){
					e.preventDefault();

					$(this).closest(el).find('.tb_acc_list').addClass('on');
				})

				$(this).off('click.trAllclose').on('click.trAllclose', '.btn_line_close', function(e){
					e.preventDefault();

					$(this).closest(el).find('.tb_acc_list').removeClass('on');
				})
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [장기]helpGuide
	 * @description 프로미 전산 Helper 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#l|LAAA010UM00
	 * @example
	 * // 초기화
	 * etUI.helpGuide.init();
	 */
	etUI.helpGuide = (function(){
		var el, elBox;

		function init(){
			el = $('.promy_helper');
			elGuide = $('.guide_wrap');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){
			/*개발 직접 적용*/
			// el.off('click').on('click', 'input[type="checkbox"]', function(){
			// 	if($(this).prop('checked')){
			// 		elGuide.addClass('on');
			// 	}else{
			// 		elGuide.removeClass('on');
			// 	}
			// });

			// $('.btn_chk').off('click').on('click', function(){
			// 	if($(this).prop('checked')){
			// 		etUI.tbSearch.preOpen();
			// 	}else{
			// 		etUI.tbSearch.preClose();
			// 	}
			// });
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [고객]orgChart
	 * @description 조직도 선탣 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#o|OBOM011UP01
	 * @example
	 * // 초기화
	 * etUI.orgChart.init();
	 */
	etUI.orgChart = (function(){
		var el;

		function init(){
			el = $('.client_oc');

			if(el.length == 0){
				return;
			}

			bindEvent();
		}

		function bindEvent(){

			el.off('click').on('click', 'ul.client_oc_list>li>a',function(e){
				e.preventDefault();
				el.find('ul.client_oc_list>li>a').removeClass('on');
				$(this).addClass('on');
			});
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 설계번호검색]consultNumSearch
	 * @description 유형별담보보기 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MAAA010UM01
	 * @example
	 * // 초기화
	 * etUI.consultNumSearch.init();
	 */
	etUI.consultNumSearch = (function(){
		var el, searchArea;
		function init(){
			el = $('.car_bsn_step');
			searchArea = el.find('.search_area');

			bintEvent();
		}

		function bintEvent(){
			searchArea.off('click').on('click', '>a.btn_inquiry', function(e){
				e.preventDefault();
				new TimelineMax()
				.set($('.inquiry_area'), {width:0}, 0)
				.set($('.inquiry_area'), {display:'block', autoAlpha:0}, 0)
				.to($('.inquiry_area'), .3, {autoAlpha:1}, .3)
				.to($('.inquiry_area'), .37, {width:153}, .3)
			});

			$('.inquiry_area').on('click', '>a.btn_search', function(e){
				// e.preventDefault();
				new TimelineMax()
				.to($('.inquiry_area'), .37, {width:55}, 0)
				.to($('.inquiry_area'), .3, {autoAlpha:0}, .2)
				.set($('.inquiry_area'), {display:'none'}, .4)
			})
		}

		return{
			init:init
		}
	})();

	/**
	 * @module [자동차 - 변경설계(구비서류조회)]requireDocSearch
	 * @description 유형별담보보기 버튼 이벤트
	 * @author 금정훈
	 * @see http://localhost:8888/dsm/mdi.html#m|MBAA091UP00
	 * @example
	 * // 초기화
	 * etUI.requireDocSearch.init();
	 */
	etUI.requireDocSearch = (function(){
		var el, elBox;
		var beforeSt = 0;
		var ignoreScroll = false;
		var direction = 0;
		function init(){
			el = $('.msg_box__');
			floatArea =$('.flt_area');

			if(floatArea.length == 0){
				return;
			}
			$('.flt_area').addClass('scroll_up')
			// trBtn.removeClass('on');

			bindEvent();
		}

		function bindEvent(){
			el.off('scroll.activeScroll').on('scroll.activeScroll', function(e){
				// e.preventDefault()
				direction = el.scrollTop() > beforeSt ? 'down' : 'up';
				var st = el.scrollTop();
				beforeSt = st;

				if(ignoreScroll == false && direction === 'down'){
					$('.flt_box_outer').stop().animate({height:0})
					// $('.flt_area').removeClass('scroll_up').addClass('scroll_down')
					ignoreScroll = true;
				}else if(ignoreScroll == true && direction === 'up'){
					$('.flt_box_outer').stop().animate({height:226})
					// $('.flt_area').removeClass('scroll_down').addClass('scroll_up')
					ignoreScroll = false;
				}
			})

			$('.flt_box').off('click').on('click', 'ul>li>a', function(e){
				e.preventDefault();
				var listIdx = $(this).closest('li').index();
				var clickId = $(this).attr('href');
				var intersection = 0;

				var position =  function(){
					var query = [];
					$('.table_list').find('>li').each(function(index, obj){
						query.push($(obj).outerHeight());
					});

					for(var i = 0 ; i < listIdx ; i++){
						intersection += query[i];
					}

					return intersection;

				}

				ignoreScroll = true;

				var targetPosition = position();
				TweenMax.to($('.msg_box__'), 0.3, {scrollTop:targetPosition + 266, onComplete : function(){

					$('.flt_box_outer').animate({height:0});
					ignoreScroll = false;

					setTimeout(function(){
						ignoreScroll = true; //스크롤 up일때 boolean값 바꿔줘야함
					},500)

				}});
			})
		}

		return{
			init:init
		}
	})();


	window.popupTest = function(width,height){

		console.log(width,height)
	}

}(jQuery);

// 로딩바
function lodingBar(){
	var wrapEl = $('#contents');
	var barHtml = '<div class="loding_wrap"><div class="dimm"></div><div class="loding_area"><span></span> <span></span> <span></span> <span></span> <span></span> <span></span></div></div>';
	var barCheck = $('.loding_wrap').length === 0;

	if(barCheck){
		wrapEl.prepend(barHtml);
	}
}

function lodingBarClose(){
	$('.loding_wrap').remove();
}

//로그인 정보 보여주기
function loginInfoShow(){
	TweenMax.set('.login_info',{display:'block', bottom:0, top: 44, height: 0, zIndex: 1, autoAlpha: 0})
	TweenMax.to('.login_info', .35, {height: 44, autoAlpha: 1});
	TweenMax.to('.login_info', .35, {delay: 3.5, height: 0, autoAlpha: 0, onComplete: function(){
		$('.login_info').removeAttr('style');
	}});
}

//로그인 정보 가리기
// function loginInfoHide(){
// $('.login_info').slideUp(350);
// }

// 20171011 초화면 체크
function mainIdCheck(){
	if($('section.DSMTOBOF010UM00__').hasClass('visible__') || $('section.DSMTZB00180UM00__').hasClass('visible__')){
		$('html').addClass('first_page');
		etUI.mainEvt.init();
	} else {
		$('html').removeClass('first_page');
	}
}

// 컨텐츠 체크
function contCheck(){
	if($('section.YEBA010UM00__').hasClass('visible__')){
		$('html').addClass('contents_page');
	} else {
		$('html').removeClass('contents_page');
	}
}

// 20171013 checkbox 체크 시 하단 form 열기/닫기
function checkPrdBox(id){
	$('#'+id).off('click.chkInput').on('click.chkInput', function(){
		if($('#'+id).prop('checked')){
			$('.etc_prd_box').removeClass('disno');
		}else{
			$('.etc_prd_box').addClass('disno');
		}
	});
}

//20171101 장기 계약설계 스크롤 이동
function scrollMove(id, mgt){
	$('html, body').stop().animate({scrollTop:$('#'+id).offset().top - mgt});
}

//20171101 컨텐츠 학습실 동영상 리스트 띄움
function videoListOpen(){
	$('.view_cont_list_wrap').removeClass('disno');
	$('.view_cont_list_wrap').show();
	var videoListSwiper = new Swiper($('.view_cont_roll'),{
		direction: 'horizontal',
		speed: 400,
		spaceBetween : 100,
		initialSlide:0,
		pagination: $('.view_cont_roll > .swiper-pagination'),
		prevButton: $('.view_cont_roll').find('.swiper-button-prev'),
		nextButton: $('.view_cont_roll').find('.swiper-button-next'),
		paginationClickable: true
	});
}

//20171101 grid 레이어 팝업 가운데 정렬
function gridLayerPopSet(){
	var wh = $(window).height();
	$('.grid_layer_popup').each(function(){
		var layerH = $(this).outerHeight();

		$(this).css({top:(wh - layerH + 100)/2});
	});
}
