package com.dongbu.dsm.webview.agentweb;

import androidx.appcompat.app.AppCompatActivity;
import android.webkit.JavascriptInterface;

import java.lang.ref.WeakReference;

/**
 * Created by cenxiaozhong on 2017/5/24.
 */

public class AgentWebJsInterfaceCompat implements AgentWebCompat, FileUploadPop<IFileUploadChooser> {

    private WeakReference<AgentWeb> mReference = null;
    private IFileUploadChooser mIFileUploadChooser;
    private WeakReference<AppCompatActivity> mActivityWeakReference = null;

    AgentWebJsInterfaceCompat(AgentWeb agentWeb, AppCompatActivity activity) {
        mReference = new WeakReference<AgentWeb>(agentWeb);
        mActivityWeakReference = new WeakReference<AppCompatActivity>(activity);
    }


    @JavascriptInterface
    public void uploadFile() {


        if (mActivityWeakReference.get() != null && mReference.get() != null) {
            mIFileUploadChooser = new FileUpLoadChooserImpl.Builder()
                    .setActivity(mActivityWeakReference.get())
                    .setJsChannelCallback(new FileUpLoadChooserImpl.JsChannelCallback() {
                        @Override
                        public void call(String value) {
                            if (mReference.get() != null)
                                mReference.get().getJsEntraceAccess().quickCallJs("uploadFileResult", value);
                        }
                    }).setFileUploadMsgConfig(mReference.get().getDefaultMsgConfig().getChromeClientMsgCfg().getFileUploadMsgConfig())
                    .setPermissionInterceptor(mReference.get().getPermissionInterceptor())
                    .setWebView(mReference.get().getWebCreator().get())
                    .build();
            mIFileUploadChooser.openFileChooser();
        }


    }

    @Override
    public IFileUploadChooser pop() {
        IFileUploadChooser mIFileUploadChooser = this.mIFileUploadChooser;
        this.mIFileUploadChooser = null;
        return mIFileUploadChooser;
    }
}
