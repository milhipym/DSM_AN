package com.dongbu.dsm.intro.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.model.api.AppInfos;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PortalListAdapter extends ArrayAdapter<AppInfos> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<AppInfos> data = new ArrayList<AppInfos>();

    public interface PortalListListener {
        public void onItemClick(View view, AppInfos appInfos);
    }

    public PortalListListener getmPortalListListener() {
        return mPortalListListener;
    }

    public void setmPortalListListener(PortalListListener mPortalListListener) {
        this.mPortalListListener = mPortalListListener;
    }

    private PortalListListener mPortalListListener;

    public PortalListAdapter(Context context, int layoutResourceId, ArrayList<AppInfos> data, PortalListListener portalListListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.mPortalListListener = portalListListener;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.appIconBtn = (ImageButton)row.findViewById(R.id.app_icon_btn);
            holder.appInstallText = (TextView)row.findViewById(R.id.app_install_text);
            holder.appNameText = (TextView)row.findViewById(R.id.app_name_text);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        AppInfos appInfos = data.get(position);
        if(appInfos != null) {
            Log.d(appInfos.toString(position));

            holder.appNameText.setText(appInfos.getAppNm());

            if(!appInfos.isInstalled()) {
                holder.appInstallText.setText("Install");
                holder.appInstallText.setVisibility(View.VISIBLE);
                if(StringUtils.isEmpty(appInfos.getIconFileUrl())) {
                    holder.appIconBtn.setImageResource(R.drawable.t_ico_default);
                } else {
                    ImageLoader.getInstance().displayImage(Uri.fromFile(new File(appInfos.getIconFileUrl())).toString(), holder.appIconBtn);
                }

            } else {
                try {
                    if ( appInfos.isNeedUpdate() ) {	// 업데이트가 필요하다면
                        holder.appInstallText.setText("Update");
                        holder.appInstallText.setVisibility(View.VISIBLE);
                    } else {
                        holder.appInstallText.setVisibility(View.GONE);
                        Log.d("최신버전 입니다.");
                    }
                    holder.appIconBtn.setImageDrawable(AppUtils.getAppIcon(appInfos.getAppPackage()));
                    holder.appIconBtn.setTag(appInfos.isNeedUpdate() ? "true" : "false");

                } catch ( Exception e ) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                }
            }

            if(appInfos.isEmpty()) {
                holder.appIconBtn.setImageResource(R.drawable.t_ico_default);
                holder.appInstallText.setVisibility(View.GONE);
            } else {

                holder.appIconBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mPortalListListener != null) {
                            mPortalListListener.onItemClick(view, data.get(position));
                        }
                    }
                });
            }
        }

//        ImageItem item = data.get(position);
//        holder.imageTitle.setText(item.getTitle());
//        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    static class ViewHolder {
//        TextView imageTitle;
//        ImageView image;
        ImageButton appIconBtn;
        TextView appInstallText;
        TextView appNameText;


    }
}