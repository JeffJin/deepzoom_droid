package com.intellibeacons.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    static final int REGISTER_NEW_MAP = 1000;
    static final int LOGIN_USER = 1001;
    static final int CAMERA_PICTURE = 1002;
    List<FloorMap> floorMaps;
    IndoorMapService mapService = new IndoorMapService();

    private void setupMapButton(final FloorMap map){
        Button myButton = new Button(this);
        myButton.setLayoutParams(new LinearLayout.LayoutParams(100, 10));
        Drawable arrow_right = getResources().getDrawable(R.drawable.arrow_right);
        myButton.setCompoundDrawablesWithIntrinsicBounds(null, null, arrow_right, null);
        myButton.setText(map.getLabel());
        final Intent mapIntent = new Intent(this, IndoorMapActivity.class);

        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapIntent.putExtra("mapId", map.getMapId());
                mapIntent.putExtra("mapUrl", map.getUrl());
                startActivity(mapIntent);
            }
        });

        LinearLayout ll = (LinearLayout)findViewById(R.id.map_buttons_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);
    }

    private boolean mapExists(FloorMap map) {
        if(floorMaps == null || floorMaps.size() == 0){
            return false;
        }
        for(int i = 0; i < floorMaps.size(); i ++){
            FloorMap temp = floorMaps.get(i);
            if(temp.getMapId() == map.getMapId()){
                return true;
            }
        }
        return false;
    }

    private void initFloorPlans(String userId){
        if(floorMaps == null)
        {
            floorMaps = mapService.getMapsById(userId);
        }

        for(int i =0; i < floorMaps.size(); i ++) {
            setupMapButton(floorMaps.get(i));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFloorPlans("default user id");

    }

    private boolean registerNewMap(){
        try {
            final Intent registerMapIntent = new Intent(this, NewMapActivity.class);
            startActivityForResult(registerMapIntent, REGISTER_NEW_MAP);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        if(resultCode != RESULT_OK){
            LogHelper.Log("Registering new map failed");
            return;
        }

        switch(requestCode){
            case REGISTER_NEW_MAP:
                FloorMap map = getNewMap(resultIntent);
                if(mapExists(map)){
                    return;
                }
                floorMaps.add(map);
                setupMapButton(map);
                return;
            case LOGIN_USER:
                return;
            case CAMERA_PICTURE:
                return;
        }

    }

    private FloorMap getNewMap(Intent resultIntent){
        String mid = resultIntent.getStringExtra(NewMapActivity.MAP_ID);
        String label = resultIntent.getStringExtra(NewMapActivity.MAP_LABEL);
        String mUrl = resultIntent.getStringExtra(NewMapActivity.MAP_URL);
        return mapService.createFloorMap(mid, label, mUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_register_new:
                return registerNewMap();
            case R.id.action_set_profile:
                LogHelper.Log("onOptionsItemSelected.action_set_profile");
                return true;
            case R.id.action_quit:
                LogHelper.Log("onOptionsItemSelected.action_quit");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
