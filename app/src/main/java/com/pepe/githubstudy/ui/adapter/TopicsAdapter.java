package com.pepe.githubstudy.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.common.GlideApp;
import com.pepe.githubstudy.mvp.model.Topic;
import com.pepe.githubstudy.ui.adapter.base.BaseAdapter;
import com.pepe.githubstudy.ui.adapter.base.BaseViewHolder;
import com.pepe.githubstudy.ui.fragment.base.BaseFragment;
import com.pepe.githubstudy.utils.PrefUtils;
import com.pepe.githubstudy.utils.StringUtils;
import com.pepe.githubstudy.utils.ViewUtils;
import com.pepe.githubstudy.utils.WindowUtil;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/12/29 11:11:24
 */

public class TopicsAdapter extends BaseAdapter<TopicsAdapter.ViewHolder, Topic> {

    public TopicsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_topic;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Topic model = data.get(position);
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDesc());

        if(StringUtils.isBlank(model.getImage())){
            int padding = WindowUtil.dipToPx(context, 16);
            holder.image.setPadding(padding, padding, padding, padding);
            holder.image.setBackgroundColor(ViewUtils.getWindowBackground(context));
            holder.image.setImageTintList(ColorStateList.valueOf(ViewUtils.getSecondaryTextColor(context)));
            holder.image.setImageResource(R.drawable.ic_topic);
        } else {
            holder.image.setPadding(0, 0, 0, 0);
            int transparentColor = context.getResources().getColor(R.color.transparent);
            holder.image.setBackgroundColor(transparentColor);
            holder.image.setImageTintList(null);
            GlideApp.with(fragment)
                    .load(model.getImage())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(holder.image);
        }
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.image)
        AppCompatImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.desc)
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
