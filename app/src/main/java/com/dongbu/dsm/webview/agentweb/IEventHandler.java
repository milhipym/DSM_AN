package com.dongbu.dsm.webview.agentweb;

import android.view.KeyEvent;

/**
 * <b>@项目名：</b> agentweb<br>
 * <b>@包名：</b>com.dongbu.dsm.webview.agentweb<br>
 * <b>@创建者：</b> cxz --  just<br>
 * <b>@创建时间：</b> &{DATE}<br>
 * <b>@公司：</b><br>
 * <b>@邮箱：</b> cenxiaozhong.qqcom@qq.com<br>
 * <b>@描述</b><br>
 */

public interface IEventHandler {

    boolean onKeyDown(int keyCode, KeyEvent event);


    boolean back();
}
