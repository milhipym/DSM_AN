package com.dongbu.dsm.util;

import com.blankj.utilcode.util.ScreenUtils;
import com.dongbu.dsm.base.BaseActivity;

/**
 * Created by LandonJung on 2017-08-11.
 */

public class RotationUtil {

    public static int LANDSCAPE = 1;
    public static int PORTRAIT = 2;

    public static void ChangeRotation(BaseActivity activity, int rotation) {
        if(activity != null) {
            if(rotation == PORTRAIT && ScreenUtils.isLandscape()) {
                ScreenUtils.setPortrait(activity);
            } else if(rotation == LANDSCAPE && ScreenUtils.isPortrait()) {
                ScreenUtils.setLandscape(activity);
            }
        }
    }

    public static void CheckRotation(BaseActivity activity) {
        if(activity != null) {
            if(DSMUtil.isTablet()) {
                ChangeRotation(activity, LANDSCAPE);
            } else {
                //ChangeRotation(activity, PORTRAIT);
            }
        }
    }
}
