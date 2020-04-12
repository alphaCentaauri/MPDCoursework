package com.gcu.mpd.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gcu.mpd.Model.Region;
import com.gcu.mpd.Model.Repository;
import com.gcu.mpd.R;

import java.util.List;
import java.util.Objects;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class QuakeRegions extends AppCompatActivity {
    GridView gridView;
    List<Region> regions;
//    ArrayList<String> regionTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_regions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        }

        this.setTitle("Recent earthquakes");
        regions = Repository.regions;
        /*
        Insert the various grid view items into the main grid view using a custom adapter
         */
        gridView = findViewById(R.id.homeGrid);
        CustomAdapter customAdapter = new CustomAdapter();

        gridView.setAdapter((customAdapter));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String title = regions.get(i).title.split(":")[2];
                String regionName = title.split(", ")[0];

                Intent intent = new Intent(getApplicationContext(), RegionDetails.class);
                intent.putExtra("regionName", regionName);
                intent.putExtra("regionIndex", i);
                startActivity(intent);
            }
        });

        Button filterButton = findViewById(R.id.filterBtn);
        filterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(regions.size() > 0) {
                    startActivity(new Intent(getBaseContext(), FilterScreen.class));
                }
            }
        });
    }

    /*
    This is a custom adapter that allows the display of various cities in the main view
     */
    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return regions.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.row_data, null);
            String title = regions.get(i).title.split(":")[2];
            String regionName = title.split(", ")[0];
            String quakeDate = title.split(", ")[1] + ", " +  title.split(", ")[2];
            String m = regions.get(i).title.split(":")[1];

            TextView name = view.findViewById(R.id.regions);
            TextView date = view.findViewById(R.id.quakeDate);
            TextView magnitude = view.findViewById(R.id.magnitude);

            name.setText(regionName);
            date.setText(quakeDate);
            magnitude.setText(m);

            return view;
        }
    }
}
