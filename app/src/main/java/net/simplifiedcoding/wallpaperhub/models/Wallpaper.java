package net.simplifiedcoding.wallpaperhub.models;

import com.google.firebase.database.Exclude;

public class Wallpaper {

    @Exclude
    public String id;

    public String title, desc, url;

    public Wallpaper(String id, String title, String desc, String url) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
