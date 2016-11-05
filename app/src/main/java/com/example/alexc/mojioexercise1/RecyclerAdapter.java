package com.example.alexc.mojioexercise1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import io.moj.java.sdk.model.Trip;

/**
 * Created by alexc on 2016-11-03.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TripHolder> {

    List<Trip> trips = Collections.emptyList();
    TripClickListener listener;

    @Override
    public RecyclerAdapter.TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trips_view, parent, false);
        return new TripHolder(inflatedView, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.TripHolder holder, int position) {
        Trip tripItem = trips.get(position);
        holder.bindTrip(tripItem);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void setTrips(List<Trip> t) {
        trips = t;
        notifyDataSetChanged();
    }

    public void setListener(TripClickListener listener) {
        this.listener = listener;
    }


    public static class TripHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tripID;
        private TextView tripDate;
        private TripClickListener listener;

        public TripHolder(View itemView, TripClickListener listener) {
            super(itemView);

            this.listener = listener;
            tripID = (TextView) itemView.findViewById(R.id.trip_id);
            tripDate = (TextView) itemView.findViewById(R.id.trip_date);

            itemView.setOnClickListener(this);
        }

        public void bindTrip(Trip t) {
            tripID.setText(t.getId());
            tripDate.setText(t.getStartTimestamp().toString());
        }

        @Override
        public void onClick(View view) {
            System.out.println("Clicked on view! tripID = "+tripID.getText());
            listener.notify(tripID.getText().toString());
        }
    }

}
