package com.dongbu.dsm.share;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.util.Log;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

/**
 * Created by LandonJung on 2017-09-18.
 * 카카오톡 공유
 * @since 0, 1
 */
public class KakaoTalkShareActivity extends BaseActivity {

    private String mText;
    private String mPhotoUrl;
    private String mLinkUrl;
    private String mTitle;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            intent      =   getIntent();

            if(intent != null) {
                mTitle      =   intent.getStringExtra(CommonData.KAKAO_TITLE);
                mText        =   intent.getStringExtra(CommonData.KAKAO_CONTENTS);
                mPhotoUrl   =   intent.getStringExtra(CommonData.KAKAO_PHOTO_URL);
                mLinkUrl    =   intent.getStringExtra(CommonData.KAKAO_LINK_URL);
                Toast.makeText(KakaoTalkShareActivity.this, mPhotoUrl+" , "+mLinkUrl+" , "+mText+" , "+mTitle, Toast.LENGTH_SHORT).show();
                sendKakaoTalkLink(mTitle,mText, mPhotoUrl,mLinkUrl);
            }

        } catch (Exception e) {
            Toast.makeText(KakaoTalkShareActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 카카오톡 링크 공유하기
     * @param text  내용
     * @param linkUrl   링크 Url
     * @param photoUrl  사진 Url
     */
    public void sendKakaoTalkLink(String title,String text, String photoUrl,String linkUrl){
        try {
            /*

            String sendText = text;// + "\n\n" + linkUrl;

            if (!StringUtils.isEmpty(photoUrl)){  // 사진이 있다면 추가
                kakaoTalkLinkMessageBuilder.addImage(photoUrl, 200, 200);
            }
            if (!StringUtils.isEmpty(sendText)){  // 내용이 있다면 추가
                kakaoTalkLinkMessageBuilder.addText(sendText);
            }

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
            */
            Toast.makeText(KakaoTalkShareActivity.this, title+" , "+text+" , "+photoUrl+" , "+linkUrl, Toast.LENGTH_SHORT).show();
            feedTemplet(title,text, photoUrl,linkUrl);

        } catch (Exception e) {
            Toast.makeText(KakaoTalkShareActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void feedTemplet(String title,String text,  String photoUrl,String linkUrl) {

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(title,photoUrl,
                        LinkObject.newBuilder().setWebUrl(linkUrl).setMobileWebUrl(linkUrl).build())
                        //.setImageHeight(240)
                        //.setImageWidth(320)
                        .setImageHeight(365)
                        .setImageWidth(544)
                        .setDescrption(text).build()
                )
                .addButton(new ButtonObject("더보기", LinkObject.newBuilder()
                                                        .setWebUrl(linkUrl)
                        .setMobileWebUrl(linkUrl)
                        .build())).build();

        KakaoLinkService.getInstance().sendDefault(this, params, callback );

    }

    ResponseCallback<KakaoLinkResponse> callback = new ResponseCallback<KakaoLinkResponse>() {
        @Override
        public void onFailure(ErrorResult errorResult) {
            Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(KakaoLinkResponse result) {
            //Toast.makeText(getApplicationContext(), "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("requestCode = " + requestCode + "  resultCode = " + resultCode);

        switch(requestCode){

            case CommonData.ACTIVITY_KAKAOTALK_SHARE: // 카카오톡 공유하기
                setResult(RESULT_OK);
                finish();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
