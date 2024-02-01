package com.dongbu.dsm.network;

import android.content.Context;
import android.os.Handler;

import com.dongbu.dsm.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import kr.mtcom.smartmessage.common.ApiEngineListener;
import kr.mtcom.smartmessage.common.AppBaseUtil;
import kr.mtcom.smartmessage.common.BaseApiEngine;
import kr.mtcom.smartmessage.common.BaseError;
import kr.mtcom.smartmessage.common.CompanyUser;
import kr.mtcom.smartmessage.common.LOG;
import kr.mtcom.smartmessage.manager.AccountManager;
import kr.mtcom.smartmessage.manager.DongbuPushManager;
import kr.mtcom.smartmessage.manager.SMAccountManager;

/**
 * Created by msbahng0 on 15. 4. 7..
 */
public class DongbuAccountManager {

    private static String TAG = "DongbuAccountManager";

    private static DongbuAccountManager instance = null;
    private CompanyUser mMe;
    private Handler handler = new Handler();

    public static DongbuAccountManager getInstance() {
        if (instance == null)
            instance = new DongbuAccountManager();
        return instance;
    }

    public DongbuAccountManager() {
    }

    public void registerPush(Context context) {
        // chatting push
        LOG.d(TAG, "registerPush");
        String packageName = context.getPackageName();
        int userKey = AccountManager.getMyUserSeqNo(context);
        String pushSenderId = Config.GCM.SENDER_ID; //AppBaseUtil.getMetaData(context).getString("kr.mdongbu.smartmessage.push_sender_id");
        String baseUrl = AppBaseUtil.getBaseUrl(context);
        if (userKey > 0 && pushSenderId != null) {
            DongbuPushManager.getInstance().setRegisterData(baseUrl, String.valueOf(userKey), packageName, pushSenderId);
            DongbuPushManager.getInstance().registerPushNoti(context);
        }
    }

    public CompanyUser getMe() {
        return mMe;
    }

    public void setMe(CompanyUser user) {
        LOG.d(TAG, "setMe");
        mMe = user;
    }

    public boolean isMe(CompanyUser user, Context context) {
        if (AccountManager.getDongbuUserId(context).equals(user.loginId)) {
            return true;
        }
        return false;
    }

    public void doAppLoginCheck(final Context context) {
        LOG.d(TAG, "doLoginCheckApp");

        if (!AccountManager.isAuthenticated(context)) {
            LOG.d(TAG, "not authenticated");
        } else {
            LOG.d(TAG, "authenticated");
            getLoggedInUser(context);
        }
    }


    public void getLoggedInUser(final Context context) {
        LOG.d(TAG, "getLoggedInUser");

        if (!AccountManager.isAuthenticated(context)) {
            return;
        }

        if (AccountManager.getMyUserSeqNo(context) < 0) {
            return;
        }

        BaseApiEngine.companyUserInfoRequest(AccountManager.getMyUserSeqNo(context), context, new ApiEngineListener() {
            public void baseUserResponse(CompanyUser user) {
                if (user != null) {
                    setMe(user);
                }
            }

            public void errorResponse(BaseError error) {
            }

            public void networkErrorResponse() {
            }
        });
    }

    public void removeAllFiles(Context context) {
        String folder = AppBaseUtil.getImageFileFolder(context);
        File topPath = new File(folder);
        if (topPath.exists()) {
            DongbuAccountManager.removeFiles(topPath, context);
        }
    }

    public static void removeFiles(File file, Context context) {
        LOG.d(TAG, "removeFiles");
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            if (files!=null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        SMAccountManager.removeFiles(f, context);
                    }
                    f.delete();
                }
            }
        }
    }

    public void logout(final Context context) {
        LOG.d(TAG, "logout");

        removeAllFiles(context);
        String packageName = context.getPackageName();
        int userKey = AccountManager.getMyUserSeqNo(context);

        JSONObject data = new JSONObject();
        try {
            String companyCode = AppBaseUtil.getCompanyCodeMetaData(context);
            if (companyCode != null) {
                data.put("companyCode", companyCode);
            }
            data.put("companyUserSeqno", userKey);
            data.put("appId", packageName);
        } catch (JSONException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

        DongbuPushManager.getInstance().deleteRequest(data, AccountManager.getAccessToken(context), context, null);
        SMAccountManager.getInstance().logoutSM(context);
    }
}
