package com.gcu.mpd.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gcu.mpd.Model.Region;
import com.gcu.mpd.Model.Repository;
import com.gcu.mpd.Model.XMLParser;
import com.gcu.mpd.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class MainActivity extends AppCompatActivity
{
    private String result;
    private Button startButton;
    Repository repository;
    // Traffic Scotland URLs
    private String urlSource = "https://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        startProgress();
        repository = new Repository();
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repository.getRegions().size() > 0) {
                    startActivity(new Intent(getBaseContext(), QuakeRegions.class));
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("No internet")
                            .setMessage("Please turn on your internet to enable data fetching from the server")
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    startProgress();
                }
            }
        });
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                System.out.println(repository.toString());
                aurl = new URL(url);
                InputStream inputStream = aurl.openConnection().getInputStream();
                List<Region> mFeedModelList = XMLParser.parseFeed(inputStream);

                repository.setRegions(mFeedModelList);

                for(Region r: repository.getRegions()) {
                    System.out.println(r.title);
                }
                inputStream.close();
            }
            catch (IOException | XmlPullParserException ae)
            {
                Log.e("MyTag", ae.toString());
            }

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
                }
            });
        }

    }

} // End of MainActivity
