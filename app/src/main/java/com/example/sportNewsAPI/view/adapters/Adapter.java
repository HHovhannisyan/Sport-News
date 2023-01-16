package com.example.sportNewsAPI.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sportNewsAPI.GlideApp;
import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.Utils;

import com.example.sportNewsAPI.databinding.ItemBinding;
import com.example.sportNewsAPI.databinding.LayoutAdsBinding;
import com.example.sportNewsAPI.model.Article;
import com.example.sportNewsAPI.view.activities.WebActivity;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Article> articles;
    Context context;
    private OnItemClickListener onItemClickListener;
    final int TYPE_ADS = 4;
    ItemBinding itemBinding;
    LayoutAdsBinding layoutAdsBinding;

    public Adapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ADS) { // for ads layout
            layoutAdsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_ads, parent, false);

            return new AdsViewHolder(layoutAdsBinding, onItemClickListener);

        } else { // for article layout

            itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false);

            return new ArticleViewHolder(itemBinding, onItemClickListener);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position % 8;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {

        super.onViewRecycled(holder);

        if (holder instanceof ArticleViewHolder) {
            GlideApp.with(holder.itemView.getContext()).clear(((ArticleViewHolder) holder).itemRowBinding.img);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_ADS) {
            ((AdsViewHolder) viewHolder).setAds();

        } else {
            Article model = articles.get(position);
            context = viewHolder.itemView.getContext();

            GlideApp.with(context)
                    .load(model.getUrlToImage())
                    .centerInside()
                    .placeholder(R.drawable.top_shadow)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.img_error)
                    .transform(new RoundedCorners(5))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ((ArticleViewHolder) viewHolder).itemRowBinding.progressLoadPhoto.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ((ArticleViewHolder) viewHolder).itemRowBinding.progressLoadPhoto.setVisibility(View.GONE);

                            return false;
                        }
                    })

                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((ArticleViewHolder) viewHolder).itemRowBinding.img);
            ((ArticleViewHolder) viewHolder).itemRowBinding.title.setText(model.getTitle());
            ((ArticleViewHolder) viewHolder).itemRowBinding.source.setText(model.getSource().getName());
            ((ArticleViewHolder) viewHolder).itemRowBinding.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()));
        }
    }

    @Override
    public int getItemCount() {

        try {
            return articles.size();
        } catch (Exception ex) {
            return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final OnItemClickListener onItemClickListener;
        public final ItemBinding itemRowBinding;

        public ArticleViewHolder(ItemBinding itemRowBinding, OnItemClickListener onItemClickListener) {
            super(itemRowBinding.getRoot());

            itemView.setOnClickListener(this);

            this.onItemClickListener = onItemClickListener;
            this.itemRowBinding = itemRowBinding;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.OnItemClick(v, getAdapterPosition());
        }
    }


    public static class AdsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final OnItemClickListener onItemClickListener;
        public final LayoutAdsBinding layoutAdsBinding;

        AdsViewHolder(LayoutAdsBinding layoutAdsBinding, OnItemClickListener onItemClickListener) {
            super(layoutAdsBinding.getRoot());

            layoutAdsBinding.imgAds.setOnClickListener(v -> {

                Intent intent = new Intent(itemView.getContext(), WebActivity.class);
                intent.putExtra("adsLink", "https://www.google.com/");
                itemView.getContext().startActivity(intent);
            });

            this.onItemClickListener = onItemClickListener;
            this.layoutAdsBinding = layoutAdsBinding;
        }

        void setAds() {
            layoutAdsBinding.imgAds.setBackgroundResource(R.drawable.ads);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.OnItemClick(v, getAdapterPosition());
        }

    }
}
