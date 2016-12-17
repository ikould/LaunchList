package com.ikould.launchlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikould.launchlist.R;
import com.ikould.launchlist.data.LaunchData;

import java.util.List;


/**
 * describe
 * Created by liudong on 2016/6/21.
 */
public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.RecycleHolder> {
    private List<LaunchData> lists;
    private Context context;
    private RecyclerLister recyclerLister;

    public PackagesAdapter(List<LaunchData> lists, Context context, RecyclerLister recyclerLister) {
        this.lists = lists;
        this.context = context;
        this.recyclerLister = recyclerLister;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //初始化Holder
        return new RecycleHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_package, parent, false));
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        //设置数据
        LaunchData launchData = lists.get(position);
        holder.iv_item.setBackgroundDrawable(launchData.getLaunchIcon());
        String itemMsg = launchData.getAppName() + "（" + launchData.getPackageName() + "）";
        holder.tv_item.setText(itemMsg);
    }

    @Override
    public int getItemCount() {
        //长度
        return lists.size();
    }

    /**
     * 自定义RecyclerView
     */
    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout ll_item;
        TextView tv_item;
        ImageView iv_item;

        public RecycleHolder(View itemView) {
            super(itemView);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            tv_item = (TextView) itemView.findViewById(R.id.tv_packageitem_text);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_packageitem_ico);
            ll_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerLister.recycleOnClickListener(v, getPosition());
        }
    }

    public interface RecyclerLister {
        void recycleOnClickListener(View v, int position);
    }
}
