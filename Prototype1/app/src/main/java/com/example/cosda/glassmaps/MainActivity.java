package com.example.cosda.glassmaps;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;




public class MainActivity extends Activity {
    private CardScrollView mCardScroller;
    private View mView;
    Handler hand = new Handler();
    double Longitude;
    double Latitude;
    private MapDisplay customCanvas;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mView = buildView();
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardScrollAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return mView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return mView;
            }

            @Override
            public int getPosition(Object item) {
                if (mView.equals(item)) {
                    return 0;
                }
                return AdapterView.INVALID_POSITION;
            }
        });
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
            }
        });
        setContentView(mCardScroller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    private View buildView() {
        View view = View.inflate(this, R.layout.main, null);
    //    customCanvas = (MapDisplay) findViewById(R.id.signature_canvas);
        hand.postDelayed(run, 1000);
        return view;
    }

    public String getLocation() {
        GPSTracker gps = new GPSTracker(this);
        String Location;
        Latitude = gps.getLatitude();
        Longitude = gps.getLongitude();
        Location = Latitude + ", " + Longitude;
        return Location;
    }

    Runnable run = new Runnable()
    {
        @Override
        public void run() {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        }
    };

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 9; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.footer);
            txt.setText(getLocation());
            Log.d("update", "update");

            hand.postDelayed(run, 1000);
        }
    }

}