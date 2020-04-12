package com.gcu.mpd.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
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
public class RegionDetails extends AppCompatActivity {
    List<Region> regions;
    String regionName;
    int regionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        }

        regions = Repository.regions;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            regionName = extras.getString("regionName");
            regionIndex = extras.getInt("regionIndex");
        }
        // Set the current page title to the title passed in from the main activity
        this.setTitle(regionName);
        populateView();
    }

    private void populateView() {
        Region region = regions.get(regionIndex);
        String[] description = region.description.split("; ");

        TextView depth = findViewById(R.id.depth);
        TextView location = findViewById(R.id.location);
        TextView originTime = findViewById(R.id.originTIme);
        TextView magnitude = findViewById(R.id.magnitude);

        originTime.setText(description[0]);
        location.setText(description[2]);
        depth.setText(description[3]);
        magnitude.setText(description[4]);

    }
}
