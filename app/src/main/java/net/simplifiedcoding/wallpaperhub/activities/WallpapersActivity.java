package net.simplifiedcoding.wallpaperhub.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.simplifiedcoding.wallpaperhub.R;
import net.simplifiedcoding.wallpaperhub.adapters.WallpapersAdapter;
import net.simplifiedcoding.wallpaperhub.models.Wallpaper;

import java.util.ArrayList;
import java.util.List;

public class WallpapersActivity extends AppCompatActivity {

    List<Wallpaper> wallpaperList;
    RecyclerView recyclerView;
    WallpapersAdapter adapter;

    DatabaseReference dbWallpapers;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);

        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WallpapersAdapter(this, wallpaperList, category);

        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressbar);




        dbWallpapers = FirebaseDatabase.getInstance().getReference("images")
                .child(category);

        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);

                        Wallpaper w = new Wallpaper(id, title, desc, url);
                        wallpaperList.add(w);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
