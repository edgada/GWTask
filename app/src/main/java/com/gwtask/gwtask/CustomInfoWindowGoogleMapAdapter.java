package com.gwtask.gwtask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMapAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMapAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_info_layout, null);

        TextView txtMarkerWord = view.findViewById(R.id.markerInfoWord);
        TextView txtMarkerKategorija = view.findViewById(R.id.markerInfoKategorija);

        txtMarkerWord.setText(Util.dabartinis.getReiksme());
        txtMarkerKategorija.setText(Util.dabartinis.getPartOfSpeech());

        return view;
    }
}

