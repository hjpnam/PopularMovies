package com.example.popularmovies.shared;

public class Trailer {
    private String mKey;
    private String mName;
    private String mSite;

    public Trailer(String key, String name, String site) {
        mKey = key;
        mName = name;
        mSite = site;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }
}
