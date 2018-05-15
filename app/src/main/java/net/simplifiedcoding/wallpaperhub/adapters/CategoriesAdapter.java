package net.simplifiedcoding.wallpaperhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import net.simplifiedcoding.wallpaperhub.R;
import net.simplifiedcoding.wallpaperhub.activities.WallpapersActivity;
import net.simplifiedcoding.wallpaperhub.models.Category;

import java.util.List;

/**
 * Created by Belal on 4/21/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private Context mCtx;
    private List<Category> categoryList;

    private InterstitialAd mInterstitialAd;

    public CategoriesAdapter(Context mCtx, List<Category> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;

        mInterstitialAd = new InterstitialAd(mCtx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_categories, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category c = categoryList.get(position);
        holder.textView.setText(c.name);
        Glide.with(mCtx)
                .load(c.thumb)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_cat_name);
            imageView = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Toast.makeText(mCtx, "Ad not loaded", Toast.LENGTH_LONG).show();
            }

            int p = getAdapterPosition();
            Category c = categoryList.get(p);

            Intent intent = new Intent(mCtx, WallpapersActivity.class);
            intent.putExtra("category", c.name);

            mCtx.startActivity(intent);
        }
    }
}
