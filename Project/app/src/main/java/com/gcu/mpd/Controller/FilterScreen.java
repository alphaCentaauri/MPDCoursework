package com.gcu.mpd.Controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcu.mpd.Model.Region;
import com.gcu.mpd.Model.Repository;
import com.gcu.mpd.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class FilterScreen extends AppCompatActivity implements View.OnClickListener {
    List<Region> regions;
    Calendar startCalendar;
    Calendar endCalendar;
    EditText startDatePicker, endDatePicker;
    DatePickerDialog.OnDateSetListener dateStart, dateEnd;
    Button runFilterBtn;
    LinearLayout filterDataDisplay;

    double MAULat = -20.251868;
    double MAULng = 57.870755;
    String myFormat = "MM/dd/yy";
    double shortestMU = 100000000000.0;
    double lgMag = 0.0;
    double deepestDpt = 0.0;
    double shallowestDpt = 1000.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);
        this.setTitle("Filter");

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        regions = Repository.regions;
        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);
        runFilterBtn = findViewById(R.id.runFilterBtn);
        filterDataDisplay = findViewById(R.id.filterData);

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDatePicker.setText(sdf.format(startCalendar.getTime()));
        endDatePicker.setText(sdf.format(endCalendar.getTime()));

        dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }

        };

        dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }

        };

        startDatePicker.setOnClickListener(this);
        endDatePicker.setOnClickListener(this);
        runFilterBtn.setOnClickListener(this);

    }

    private void updateEndLabel() {//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDatePicker.setText(sdf.format(endCalendar.getTime()));
    }

    private void updateStartLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDatePicker.setText(sdf.format(startCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startDatePicker:
                new DatePickerDialog(FilterScreen.this, dateStart, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.endDatePicker:
                new DatePickerDialog(FilterScreen.this, dateEnd, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.runFilterBtn:
                runFilter(filterDataDisplay);
                break;
            default:
                break;
        }
    }

    private void runFilter(LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        if(startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)){
            List<Region> filteredRegions = processData();

            DecimalFormat df = new DecimalFormat("#.0000");

            TextView distFromMAU = new TextView(getApplicationContext());
            TextView largestMagnitude = new TextView(getApplicationContext());
            TextView deepestEarthQuake = new TextView(getApplicationContext());
            TextView shallowestEarthQuake = new TextView(getApplicationContext());

            if(filteredRegions.size() <= 0){
                largestMagnitude.setText("No data for specified period");
            } else {
                distFromMAU.setText("Nearest to Mauritius: " + df.format(shortestMU) + " km");
                largestMagnitude.setText("Largest magnitude: " + lgMag);
                deepestEarthQuake.setText("Deepest Earthquake: " + deepestDpt + " km");
                shallowestEarthQuake.setText("Shallowest Earthquake: " + shallowestDpt + " km");
            }

            largestMagnitude.setPadding(15, 10, 15, 10);
            deepestEarthQuake.setPadding(15, 10, 15, 10);
            shallowestEarthQuake.setPadding(15, 10, 15, 10);
            distFromMAU.setPadding(15, 10, 15, 10);

            largestMagnitude.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(distFromMAU);
            linearLayout.addView(largestMagnitude);
            linearLayout.addView(deepestEarthQuake);
            linearLayout.addView(shallowestEarthQuake);
        } else {
            new AlertDialog.Builder(FilterScreen.this)
                    .setTitle("Wrong date input")
                    .setMessage("The start date cannot come after end date")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

//        System.out.println(startCalendar.getTime() + " --- " + endCalendar.getTime());
    }

    private List<Region> processData() {
        List<Region> filteredRegions = new ArrayList<>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date pubDate = null;

        startCalendar.add(Calendar.HOUR, -1);
        endCalendar.add(Calendar.HOUR, +1);
        //search for regions with pubDate within the start and end dates
        for(Region r: regions){
            String date = r.pubDate;
            String lat = r.description.split("; ")[2].split(": ")[1].split(",")[0];
            String lng = r.description.split("; ")[2].split(": ")[1].split(",")[1];
            String mag = r.description.split("; ")[4].split(": ")[1];
            String shallowDepth = r.description.split("; ")[3].split(": ")[1].split(" ")[0];
            String deepestDepth = r.description.split("; ")[3].split(": ")[1].split(" ")[0];

            try {
                pubDate = sdf2.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(startCalendar.getTime().before(pubDate) && endCalendar.getTime().after(pubDate)){
                filteredRegions.add(r);
                getShortestDistance(lat, lng);
                if(Double.parseDouble(mag) > lgMag){
                    lgMag = Double.parseDouble(mag);
                }
                if(Double.parseDouble(shallowDepth) < shallowestDpt){
                    shallowestDpt = Double.parseDouble(shallowDepth);
                }
                if(Double.parseDouble(deepestDepth) > deepestDpt){
                    deepestDpt = Double.parseDouble(deepestDepth);
                }
            }
        }

        return filteredRegions;
    }

    private void getShortestDistance(String lat, String lng) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(Double.parseDouble(lat) - MAULat);
        double dLng = Math.toRadians(Double.parseDouble(lng) - MAULng);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(dLat)) * Math.cos(Math.toRadians(MAULat)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        if(dist < shortestMU){
            shortestMU = dist/1000;
        }
    }
}
