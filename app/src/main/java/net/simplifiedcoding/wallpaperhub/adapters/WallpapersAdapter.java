package net.simplifiedcoding.wallpaperhub.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.simplifiedcoding.wallpaperhub.R;
import net.simplifiedcoding.wallpaperhub.models.Category;
import net.simplifiedcoding.wallpaperhub.models.Wallpaper;

import java.util.List;

/**
 * Created by Belal on 4/21/2018.
 */

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder> {

    private Context mCtx;
    private List<Wallpaper> wallpaperList;
    private String category;

    public WallpapersAdapter(Context mCtx, List<Wallpaper> wallpaperList, String category) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;
        this.category = category;
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpapers, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {
        Wallpaper w = wallpaperList.get(position);
        holder.textView.setText(w.title);
        Glide.with(mCtx)
                .load(w.url)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView textView;
        ImageView imageView;

        CheckBox checkBoxFav;
        ImageButton buttonShare, buttonDownload;


        public WallpaperViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.image_view);

            checkBoxFav = itemView.findViewById(R.id.checkbox_favourite);
            buttonShare = itemView.findViewById(R.id.button_share);
            buttonDownload = itemView.findViewById(R.id.button_download);

            checkBoxFav.setOnCheckedChangeListener(this);
            buttonShare.setOnClickListener(this);
            buttonDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(mCtx, "Please login first...", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                return;
            }

            DatabaseReference dbFavs = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
                    .child(category);

            int position = getAdapterPosition();
            Wallpaper w = wallpaperList.get(position);

            if (b) {
                dbFavs.child(w.id).setValue(w);
            } else {
                dbFavs.child(w.id).setValue(null);
            }
        }
    }
}
