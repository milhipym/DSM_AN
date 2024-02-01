package com.dongbu.dsm.push.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dongbu.dsm.R;
import com.dongbu.dsm.push.activity.MessageListActivity;

import kr.mtcom.smartmessage.data.SmartMessage;


public class ListAdater extends RecyclerView.Adapter<ListAdater.MsgViewHolder> {

    private String TAG = "ListAdater";

    private SparseArray<SmartMessage> items;
    private MessageListActivity.RemoveListMessageOnClickListener mRemoveListener;
    private MessageListActivity.CheckMessageAccessOnClickListener mAccessListener;
    private MessageListActivity.DetailMessageOnClickListener mDetailListener;

    public ListAdater(SparseArray<SmartMessage> resultItemSparseArray, MessageListActivity.RemoveListMessageOnClickListener removeListener, MessageListActivity.CheckMessageAccessOnClickListener accessListener, MessageListActivity .DetailMessageOnClickListener detailListener) {
        super();
        items = resultItemSparseArray;
        mRemoveListener = removeListener;
        mAccessListener = accessListener;
        mDetailListener = detailListener;
    }


    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_push_list_item, parent, false);

        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {
        SmartMessage item = items.get(position);

        holder.setMsgWithInvaildateView(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class MsgViewHolder extends RecyclerView.ViewHolder {

        private Button mAccessButton;
        private Button mDeleteButton;
        private TextView mTextView;
        private SmartMessage mMsg;

        public MsgViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.list_text);

            mDeleteButton = (Button) itemView.findViewById(R.id.list_remove_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRemoveListener!=null && mMsg !=null) {

                        mRemoveListener.onListClick(mMsg);
                    }
                }
            });

            mAccessButton = (Button) itemView.findViewById(R.id.list_access_button);
            mAccessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAccessListener!=null && mMsg !=null) {

                        mAccessListener.onAccessClick(mMsg);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDetailListener!=null && mMsg !=null) {
                        mDetailListener.onDetailClick(mMsg);
                    }
                }
            });

        }

        public void setMsgWithInvaildateView(SmartMessage msg) {
//            LOG.d(TAG, "setMsgWithInvaildateView : " + msg.msgResultSeqno);

            mMsg = msg;

            if (msg.msgAccessed.equals("Y")) {
                mAccessButton.setEnabled(false);
            } else {
                mAccessButton.setEnabled(true);
            }

            mTextView.setText(
                    "msgResultSeqno : " + String.valueOf(msg.msgResultSeqno) + "\n" +
                            "msgSubject : " + String.valueOf(msg.msgSubject) + "\n" +
                            "msgContent : " + String.valueOf(msg.msgContent) + "\n" +
                            "msgAccessed : " + String.valueOf(msg.msgAccessed)
            );
        }

    }
}
