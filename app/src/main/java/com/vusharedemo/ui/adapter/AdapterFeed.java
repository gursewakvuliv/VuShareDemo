package com.vusharedemo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.vusharedemo.R;
import com.vusharedemo.ui.activity.ActivityVideoPlayer;
import com.vusharesdk.model.EntityFeedModel;
import com.vusharesdk.model.FileModel;

import java.util.List;

/**
 * Created by MB0000003 on 24-Apr-18.
 */

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.ViewHolder> {

    private List<EntityFeedModel> feedList;
    private Context context;

    public AdapterFeed(Context context, List<EntityFeedModel> feedList) {
        this.feedList = feedList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FileModel fileModel = feedList.get(position).getFileModelList().get(0);
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.color.grey_300)
                        .placeholder(R.color.grey_300))
                .load(fileModel.getThumbnail())
                .into(holder.ivThumb);

        holder.ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hostUrl = fileModel.getHostUrl();
                Intent intent = new Intent(context, ActivityVideoPlayer.class);
                intent.putExtra(ActivityVideoPlayer.PATH, hostUrl);
                context.startActivity(intent);
            }
        });

        holder.tvUser.setText(feedList.get(position).getUserModel().getName());
    }

    public void addFeed(EntityFeedModel updatedFeedModel) {
        feedList.add(0, updatedFeedModel);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumb;
        private TextView tvUser;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }
}