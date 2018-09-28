package com.example.chung.nhacvieccanhan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chung.nhacvieccanhan.ultils.ConstClass;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapsActivity extends FragmentActivity {

    protected static int START_INDEX = -2, DEST_INDEX = -1;
    Button searchDepButton;
    MapView map;
    EditText departureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initView();

        searchDepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(20.982295, 105.832841);
        IMapController mapController = map.getController();
        mapController.setZoom(ConstClass.ZOOM_MAP);
        mapController.setCenter(startPoint);
        map.setBuiltInZoomControls(true);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.drawable.start_blue));
        startMarker.setTitle("Start point");
        map.invalidate();
    }

    private void initView() {
        map = (MapView) findViewById(R.id.map);
        searchDepButton = (Button) findViewById(R.id.buttonSearchDep);
        departureText = (EditText) findViewById(R.id.editDeparture);
    }
}