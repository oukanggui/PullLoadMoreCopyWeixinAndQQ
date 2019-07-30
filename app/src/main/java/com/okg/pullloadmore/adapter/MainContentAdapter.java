package com.okg.pullloadmore.adapter;

import android.view.View;
import android.widget.TextView;

import com.okg.pullloadmore.R;
import com.okg.pullloadmore.adapter.base.CommonAdapter;
import com.okg.pullloadmore.adapter.base.ViewHolder;

import java.util.List;

/**
 * @author oukanggui
 * @date 2019-07-28
 * 描述
 */
public class MainContentAdapter extends CommonAdapter<String> {

    public MainContentAdapter(List<String> datas) {
        super(R.layout.item_home_chat, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        String data = getData(position);
        TextView tv = holder.getView(R.id.name);
        tv.setText(data);
        if (mItemClickListener != null) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(position, v);
                }
            });
        }
    }


}

