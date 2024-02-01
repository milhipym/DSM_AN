package com.dongbu.dsm.push.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.Toast;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.push.adapter.ListAdater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.mtcom.smartmessage.common.BaseError;
import kr.mtcom.smartmessage.data.SmartMessage;
import kr.mtcom.smartmessage.manager.AccountManager;
import kr.mtcom.smartmessage.network.SMApiEngine;
import kr.mtcom.smartmessage.network.SMApiEngineListener;

public class MessageListActivity extends AppCompatActivity {

    private String TAG = "MessageListActivity";

    private final int PAGE_SIZE = 20;

    private RecyclerView mListRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;

    private SparseArray<SmartMessage> mMsgSparseArray;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;

    private int mPageEnd = 0;
    private int mTotalCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_push_message_list);
        getSupportActionBar().setTitle("Message List");

        mListRecyclerView = (RecyclerView) findViewById(R.id.listrecycler_view);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mListRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reqestData(true);
            }
        });

        mMsgSparseArray = new SparseArray<>();
        mRecyclerViewAdapter = new ListAdater(mMsgSparseArray,
                new RemoveListMessageOnClickListener() {
                    @Override
                    public void onListClick(SmartMessage msg) {

                        JSONArray deleteMessages = new JSONArray();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("msgResultSeqno", msg.msgResultSeqno);      // 삭제할 메시지의 Seqno
                            obj.put("monthlyNo", msg.getMonthOfSendDate());
                        } catch (JSONException e) {
                            if(Config.DISPLAY_LOG) e.printStackTrace();
                        }
                        deleteMessages.put(obj);

                        // 메시지 삭제 API 호출
                        SMApiEngine.getInstance().smMessageDeleteRequest(deleteMessages, MessageListActivity.this, new SMApiEngineListener() {
                            public void booleanResponse(boolean boolValue) {

                                if (boolValue) {

                                    reqestData(true);
                                    Toast.makeText(MessageListActivity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                },
                new CheckMessageAccessOnClickListener() {
                    @Override
                    public void onAccessClick(SmartMessage msg) {
                        JSONArray messages = new JSONArray();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("msgResultSeqno", msg.msgResultSeqno);      // 읽음 확인할 메시지의 Seqno
                            obj.put("monthlyNo", msg.getMonthOfSendDate());
                        } catch (JSONException e) {
                            if(Config.DISPLAY_LOG) e.printStackTrace();
                        }
                        messages.put(obj);

                        // 메시지 (읽음) 확인 API 호출 (여러개의 메시지에 대해 한번에 호출 가능)
                        SMApiEngine.getInstance().smMessageAccessRequest(messages, MessageListActivity.this, new SMApiEngineListener() {
                            public void booleanResponse(boolean boolValue) {

                                if (boolValue) {
                                    reqestData(true);
                                }
                            }
                        });
                    }
                },
                new DetailMessageOnClickListener() {
                    @Override
                    public void onDetailClick(SmartMessage msg) {

                        Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
                        intent.putExtra("msg_seq", msg.msgResultSeqno);
                        intent.putExtra("msg_monthlyNo", msg.getMonthOfSendDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                }
        );

        mListRecyclerView.setAdapter(mRecyclerViewAdapter);

        mListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int comLastPos = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = mLinearLayoutManager.getItemCount();

                if (itemCount == 0) {
                    return;
                }

                // 보여지는 아이템의 끝까지 스크롤 되었을 경우 신규 데이터를 새로 요청한다
                if (comLastPos == (itemCount - 1) && (mSwipeRefreshLayout.isRefreshing() == false)) {
                    reqestData(false);
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        reqestData(true);
    }

    /**
     * 메시지 목록 API 호출
     * @param isRefresh    pull to refresh 여부 -> 리스트 리셋하고 첫번째 페이지를 가져옴
     */
    private void reqestData(boolean isRefresh) {

        if (isRefresh) {
            mTotalCount = 0;
            mPageEnd = 0;

            mMsgSparseArray.clear();
        }

        if (mTotalCount > 0 && mPageEnd >= mTotalCount) return;

        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", PAGE_SIZE);          // api 호출시 한번에 가져오는 페이지의 크기
            params.put("startNum", mPageEnd + 1);       // 페이지의 시작 item 인덱스

            int userSeqNo = AccountManager.getMyUserSeqNo(this); //login시 받은 CompanyUser 정보
            params.put("companyUserSeqno", userSeqNo);

        } catch (JSONException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

        SMApiEngine.getInstance().smMessageListRequest(params, this, new SMApiEngineListener() {
            public void smListResponse(ArrayList<SmartMessage> msgs, JSONObject result) {

                mSwipeRefreshLayout.setRefreshing(false);

                mTotalCount = result.optInt("totalCount");      // 데이타 전체 갯수
                mPageEnd = result.optInt("pageEnd");            // 가져온 데이타의 마지막 item 인덱스 -> 이 값을 기준으로 다음 페이지의 api를 호출함

                int index = mMsgSparseArray.size();

                if (msgs!=null) {
                    for (int i=0; i< msgs.size(); i++) {
                        mMsgSparseArray.append(i+index, msgs.get(i));
                    }
                }

                mRecyclerViewAdapter.notifyDataSetChanged();
            }

            public void errorResponse(BaseError error) {

                mSwipeRefreshLayout.setRefreshing(false);
            }

            public void networkErrorResponse() {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    public interface RemoveListMessageOnClickListener {
        abstract void onListClick(SmartMessage msg);
    }

    public interface CheckMessageAccessOnClickListener {
        abstract void onAccessClick(SmartMessage msg);
    }

    public interface DetailMessageOnClickListener {
        abstract void onDetailClick(SmartMessage msg);
    }
}
