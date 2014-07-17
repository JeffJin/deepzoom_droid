package com.intellibeacons.app;

/**
 * Created by jeffjin on 2014-06-14.
 */
public class FloorMap  {
    private String mLabel;
    private String mMapId;
    private String mUrl;

    public FloorMap(String mMapId, String mLabel, String mUrl) {

        this.mLabel = mLabel;
        this.mMapId = mMapId;
        this.mUrl = mUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getMapId() {
        return mMapId;
    }

    public String getLabel() {
        return mLabel;
    }
}
