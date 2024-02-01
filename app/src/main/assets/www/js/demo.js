
    /*
     * @설 명 : 동부화재 영업용 모바일 APP<->WEB 연동 스크립트.
     * @===========================================================================
     * @ 명세 ( WEB -> APP )
     *   doOcrCall(callback)                    : 신분증 인식 촬영 액티비티
     *   doIdCall(callback)                     : 명함 인식 촬영 액티비티
     *   doPaperCall(callback)                  : A4 촬영 액티비티
     *   doUpdateSessionTime(callback)          : 세션 타임 업데이트
     *   doStartExternalApp(callback)           : 외부 앱 실행
     *   doLogout(callback)                     : 로그아웃
     *   doAppExit(callback)                    : App 종료요청(종료하시겠습니까? [확인/취소])
     *   doStartProgress(callback)              : 프로그래스바 시작
     *   doStopProgress(callback)               : 프로그래스바 종료
     *   doCameraGalleryCall(callback)          : 카메라 / 갤러리 호출
     *   doSMSCall(callback)                    : 문자 보내기 화면 호출
     *   doShareKakaoCall(callback)             : 카톡 공유 호출
     *   getContractCall(callback)              : 연락처 가져오기
     *   doSendEmailCall(callback)              : 이메일 보내기
     *
     *   getInfo(callback)                      : 디바이스 및 앱 정보 얻어오기
     *
     * @===========================================================================
     * @ 명세 ( APP -> WEB )
     *   setSessionTime()
     */
        function bridgeLog(logContent) {
            document.getElementById("log").innerHTML = logContent;
        }

        // 디바이스 및 앱 정보
        function getInfoBtnClick() {
            bridgeLog('디바이스 및 앱 정보 실행');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'getInfo'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 신분증 인식 촬영 액티비티
        function doOcrCallBtnClick() {
            bridgeLog('신분증 인식 촬영 액티비티 실행');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doOcrCall'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 명함 인식 촬영 액티비티
        function doIdCallBtnClick() {
            bridgeLog('명함 인식 촬영 액티비티');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doIdCall'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        //A4 촬영 액티비티
        function doPaperCallBtnClick() {
            bridgeLog('A4 촬영 액티비티');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doPaperCall'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        //카메라 / 갤러리 호출
        function doCameraGalleryCallBtnClick(type) {
            bridgeLog('카메라 / 갤러리 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doCameraGalleryCall'
                , {'param1': type}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 문자 보내기 화면 호출
        function doSMSCallBtnClick(phoneNum, content) {
            bridgeLog('문자 보내기 화면 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doSMSCall'
                , {'param1': phoneNum , 'param2': content}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 카카오톡 공유 호출
        function doShareKakaoCallBtnClick(content, link, photo, title) {
            bridgeLog('카카오톡 공유 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doShareKakaoCall'
                , {'param1': content , 'param2': link , 'param3': photo , 'param4': title}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 연락처 가져오기 호출
        function getContractCallBtnClick() {
            bridgeLog('연락처 가져오기 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'getContractCall'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 이메일 보내기 화면 호출
        function doSendEmailCallBtnClick(address, subject, body) {
            bridgeLog('이메일 보내기 화면 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doSendEmailCall'
                , {'param1': address , 'param2': subject, 'param3': body}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 파라메터 셋팅
        function setValueCallBtnClick(key, value) {
            bridgeLog('파라메터 셋팅 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'setValue'
                , {'param1': key , 'param2': value}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        // 파라메터 셋팅
        function getValueCallBtnClick(key) {
            bridgeLog('파라메터 가져오기 호출');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'getValue'
                , {'param1': key}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }



        //세션 타임 업데이트
        function doUpdateSessionTimeBtnClick() {
            bridgeLog('세션 타임 업데이트');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doUpdateSessionTime'
                , null
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        //외부 앱 실행
        function doStartExternalAppBtnClick(packageName) {
            bridgeLog('외부 앱 실행');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doStartExternalApp'
                , {'param1': packageName}
                , function(responseData) {
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        //App 종료요청(종료하시겠습니까? [확인/취소])
        function doAppExitBtnClick() {
            bridgeLog('App 종료요청');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doAppExit'
                , null
                , function(responseData) {
                    alert("aaa");
                    document.getElementById("resultStr").innerHTML = responseData
                }
            );
        }

        //프로그래스바 시작
        function doStartProgressBtnClick() {
            bridgeLog('프로그래스바 시작');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doStartProgress'
            );
        }

        //프로그래스바 종료
        function doStopProgressBtnClick() {
            bridgeLog('프로그래스바 종료');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doStopProgress'
            );
        }

        //로그아웃
        function doLogoutBtnClick() {
            bridgeLog('로그아웃');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'doLogout'
            );
        }

        //메인으로
        function doGoMainBtnClick() {
            bridgeLog('메인으로');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'goMain'
            );
        }

        //앱 종료
        function doAppFinishBtnClick() {
            bridgeLog('앱 종료');
            //call native method
            window.WebViewJavascriptBridge.callHandler(
                'appFinish'
            );
        }


        function connectWebViewJavascriptBridge(callback) {
            if (window.WebViewJavascriptBridge) {
                callback(WebViewJavascriptBridge)
            } else {
                if (window.WVJBCallbacks) {
                    alert('WVJBCallbacks');
                    return window.WVJBCallbacks.push(callback);
                }
                window.WVJBCallbacks = [callback];
                var WVJBIframe = document.createElement('iframe');
                WVJBIframe.style.display = 'none';
                WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
                document.documentElement.appendChild(WVJBIframe);
                setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
//                document.addEventListener(
//                    'WebViewJavascriptBridgeReady'
//                    , function() {
//                        callback(WebViewJavascriptBridge)
//                    },
//                    false
//                );
            }
        }

        connectWebViewJavascriptBridge(function(bridge) {
            bridge.init(function(message, responseCallback) {
                bridgeLog('connectWebViewJavascriptBridge 시작');
                console.log('JS got a message', message);
                var data = {
                    'Javascript Responds': '시험 전송!'
                };
                bridgeLog('connectWebViewJavascriptBridge data = ' + data);
                console.log('JS responding with', data);
                responseCallback(data);
            });

            //현재 남은 세션 타임을 받아온다. ( App -> Web )
            bridge.registerHandler("setSessionTime", function(data, responseCallback) {
                var obj = JSON.parse(data);
                document.getElementById("sessionTime").innerHTML = (obj.data.param1);
            });
        })



