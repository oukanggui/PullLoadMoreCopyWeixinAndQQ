package com.okg.pullloadmore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.okg.pullloadmore.R;
import com.okg.pullloadmore.adapter.ExtendHeadAdapter;
import com.okg.pullloadmore.adapter.MainContentAdapter;
import com.okg.pullloadmore.adapter.base.CommonAdapter;
import com.okg.pullloadmore.view.pullextend.ExtendListFooter;
import com.okg.pullloadmore.view.pullextend.ExtendListHeader;
import com.okg.pullloadmore.view.pullextend.PullExtendLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ExtendListHeader mPullNewHeader;
    ExtendListFooter mPullNewFooter;
    PullExtendLayout mPullExtendLayout;
    RecyclerView mListHeader, mListFooter,mListContent;
    List<String> mDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        mPullNewHeader = findViewById(R.id.extend_header);
        mPullNewFooter = findViewById(R.id.extend_footer);
        mPullExtendLayout = findViewById(R.id.pull_extend);
        mListContent = findViewById(R.id.content);
        mListHeader = mPullNewHeader.getRecyclerView();
        mListFooter = mPullNewFooter.getRecyclerView();

        mListContent.setLayoutManager(new LinearLayoutManager(this));
        mListHeader.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mListFooter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData(){
        mDatas.add("测试1");
        mDatas.add("测试2");
        mDatas.add("测试3");
        mDatas.add("测试4");
        mDatas.add("测试5");
        mDatas.add("测试6");
        mDatas.add("测试7");
        mDatas.add("测试8");
        mDatas.add("测试9");
        mDatas.add("测试10");
        mDatas.add("测试11");
        mDatas.add("测试12");
        mDatas.add("测试13");
        mListContent.setAdapter(new MainContentAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Toast.makeText(MainActivity.this,mDatas.get(position) + " 功能待实现",Toast.LENGTH_SHORT).show();
            }
        }));
        mListHeader.setAdapter(new ExtendHeadAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Toast.makeText(MainActivity.this,mDatas.get(position) + " 功能待实现",Toast.LENGTH_SHORT).show();
            }
        }));
        mListFooter.setAdapter(new ExtendHeadAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Toast.makeText(MainActivity.this,mDatas.get(position) + " 功能待实现",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
