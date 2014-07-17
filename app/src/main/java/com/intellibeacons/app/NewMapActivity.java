package com.intellibeacons.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class NewMapActivity extends Activity {

    public static final String MAP_ID = "map_id";
    public static final String MAP_LABEL = "map_label";
    public static final String MAP_URL = "map_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);

        Button finishButton = (Button)findViewById(R.id.finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish((Button)v);
            }
        });
    }

    private void handleFinish(Button view){
        Intent resultIntent = new Intent();
        EditText txtId = (EditText)findViewById(R.id.map_id);
        EditText txtUrl = (EditText)findViewById(R.id.floor_plan_image);
        EditText txtLabel = (EditText)findViewById(R.id.map_label);
        resultIntent.putExtra(MAP_ID, txtId.getText().toString());
        resultIntent.putExtra(MAP_URL, txtUrl.getText().toString());
        resultIntent.putExtra(MAP_LABEL, txtLabel.getText().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
