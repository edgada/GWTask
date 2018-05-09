package com.gwtask.gwtask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileOutputStream;

public class WordInfo extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private Toolbar toolbar;
    private TextView txtInfoZodis, txtInfoKategorija, txtInfoDefinition;

    private MapView mapView;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);

        toolbar = (Toolbar) findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        MainActivity.adapter.notifyDataSetChanged();
        saveToDB();

        txtInfoZodis = (TextView)findViewById(R.id.infoZodis);
        txtInfoKategorija = (TextView)findViewById(R.id.infoKategorija);
        txtInfoDefinition = (TextView)findViewById(R.id.infoDefinition);

        txtInfoZodis.setText(Util.dabartinis.getReiksme());
        txtInfoKategorija.setText(Util.dabartinis.getPartOfSpeech());
        txtInfoDefinition.setText(Util.dabartinis.getDefinition());

        Bundle mapViewBundle = null;

        mapView = findViewById(R.id.zemelapis);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        gmap.setIndoorEnabled(false);
        UiSettings uiSettings = gmap.getUiSettings();

        uiSettings.setMyLocationButtonEnabled(false);

        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);

        LatLng vno = new LatLng(54.6872, 25.2797);

        gmap.moveCamera(CameraUpdateFactory.newLatLng(vno));
        gmap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        MarkerOptions mo = new MarkerOptions()
                .position(point)
                .icon(getMarker());

        CustomInfoWindowGoogleMapAdapter customInfoWindow = new CustomInfoWindowGoogleMapAdapter(this);
        gmap.setInfoWindowAdapter(customInfoWindow);

        Marker m = gmap.addMarker(mo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_default_icon:
                showDefaultIcon();
                return true;
            case R.id.settings_cool_icon:
                showCoolIcon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    };

    private void showDefaultIcon(){
        Util.icon = "1";
    }
    private void showCoolIcon(){
        Util.icon = "2";
    }

    private void saveToDB(){
        String infoSaugojimui = "";
        for (Zodis zd:Util.history) {
            infoSaugojimui += ";;;"+zd.getReiksme()+";;"+zd.getPartOfSpeech()+";;"+zd.getPaieskosLaikas();
        }

        try{
            WordInfo.this.deleteFile("paieskos.txt");
        }
        catch (Exception e)
        {}
        try{
            String FILENAME = "paieskos.txt";

            FileOutputStream fos = openFileOutput(FILENAME, WordInfo.this.MODE_PRIVATE);
            fos.write(infoSaugojimui.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            String s = e.toString();
        }
    }

    private BitmapDescriptor getMarker(){
        BitmapDescriptor icon;
        if(Util.icon == "1") icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        else {
            int kelintaImam = (int )(Math.random() * 5 + 1);
            String tt = "@drawable/i" + kelintaImam;
            icon = BitmapDescriptorFactory.fromResource(getResources().getIdentifier(tt,"drawable", getPackageName()));
        }

        return icon;
    }
}
