package com.example.alexc.mojioexercise1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra(MainActivity.TRIP_ID);

        MojioClient client = ((App)getApplicationContext()).getMojioClient();
        client.rest().getTrip(tripId).enqueue(new TripGetter(this));
    }

    public void onSuccessfulGetTrip(Trip t) {
        System.out.println("Got 1 trip!");

        TextView id = (TextView)findViewById(R.id.text_trip_start);
        id.setText(t.getStartTimestamp().toString());
        System.out.println(t.getId());
        System.out.println(t.getDuration());
        System.out.println(t.getName());
        System.out.println(t.getVehicleId());
        System.out.println(t.getMojioId());
        System.out.println(t.getStartTimestamp());
    }

    // region Callback
    static public class TripGetter implements Callback<Trip> {

        WeakReference<Main2Activity> caller;

        public TripGetter(Main2Activity activity) {
            caller = new WeakReference<Main2Activity>(activity);
        }

        @Override
        public void onResponse(Call<Trip> call, Response<Trip> response) {
            if (caller != null && response.isSuccessful()) {
                caller.get().onSuccessfulGetTrip(response.body());
            }
        }

        @Override
        public void onFailure(Call<Trip> call, Throwable t) {

        }
    }
    // endregion
}
