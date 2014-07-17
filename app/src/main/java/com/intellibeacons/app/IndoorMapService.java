package com.intellibeacons.app;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.google.android.gms.maps.model.Tile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffjin on 2014-06-14.
 */
public class IndoorMapService {
    List<FloorMap> mapButtons;

    public IndoorMapService(){
        mapButtons = new ArrayList<FloorMap>();
    }

    //TODO retrieve maps
    public List<FloorMap> getMapsById(String uid){
        mapButtons.add(new FloorMap("1000", "Starbucks", "http://simpson.edu/wp-content/uploads/2013/07/Kent-First-Floor-Map.jpg"));
        mapButtons.add(new FloorMap("1001", "Tim Hortons", "http://staff.mq.edu.au/custom/files/media/bnt_latest_floor_plan.jpg"));
        mapButtons.add(new FloorMap("1002", "Walmart", "http://wetu.com/Map/Resources/32764/VillafloorPlans4bed.jpg"));
        mapButtons.add(new FloorMap("1003", "Home Depot", "http://www.executivecentre.com/locations/taiwan/taipei/taipei_taipei_101/floor_plan/fp_taipei_101_tower.jpg"));

        return mapButtons;
    }

    public FloorMap createFloorMap(String mid, String label, String mUrl) {

        return new FloorMap(mid, label, mUrl);
    }

}

