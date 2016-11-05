package com.example.alexc.mojioexercise1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.lang.ref.WeakReference;
import java.util.List;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.User;
import io.moj.java.sdk.model.response.ListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  TripClickListener{

    private RecyclerView myRecycler;
    private RecyclerAdapter mAdapter;
    public final static String TRIP_ID = "trip id";
    MojioClient myClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myRecycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        myRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(llm);
        mAdapter = new RecyclerAdapter();
        myRecycler.setAdapter(mAdapter);

//        if (myClient != null) {
            System.out.println("Get Mojio Client");
            myClient = ((App) getApplicationContext()).getMojioClient();
            myClient.login("alextest", "Test123").enqueue(new LoginCallback(this));
//        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    public void onSuccessfulLogin() {
        myClient.rest().getTrips().enqueue(new TripsGetterCallback(this));
    }

    public void onSuccessfulGetTrips(List<Trip> trips) {
        for (int i = 0; i < trips.size(); i++) {
            System.out.println("Trip ID# " + trips.get(i).getId());
        }
        mAdapter.setTrips(trips);
        mAdapter.setListener(this);
    }

    @Override
    public void notify(String tripID) {
        System.out.println("Send trip ID to 2nd screen: "+tripID);
        Intent detailedTrip = new Intent(this, Main2Activity.class);
        detailedTrip.putExtra(TRIP_ID, tripID);
        startActivity(detailedTrip);
    }

    // region Callback
    class LoginCallback implements Callback<User> {
        WeakReference<MainActivity> caller;

        public LoginCallback(MainActivity activity) {
            caller = new WeakReference<>(activity);
        }

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (caller != null && response.isSuccessful()) {
                caller.get().onSuccessfulLogin();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {

        }
    }

    class TripsGetterCallback implements Callback<ListResponse<Trip>> {
        WeakReference<MainActivity> caller;

        public TripsGetterCallback(MainActivity activity) {
            caller = new WeakReference<>(activity);
        }


        @Override
        public void onResponse(Call<ListResponse<Trip>> call, Response<ListResponse<Trip>> response) {
            if (caller != null && response.isSuccessful()) {
                caller.get().onSuccessfulGetTrips(response.body().getData());
            }
        }

        @Override
        public void onFailure(Call<ListResponse<Trip>> call, Throwable t) {
        }
    }

        // endregion

}
