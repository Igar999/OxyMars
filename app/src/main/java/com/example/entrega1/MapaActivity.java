package com.example.entrega1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    FusedLocationProviderClient proveedordelocalizacion;
    LocationCallback actualizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment elfragmento = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        proveedordelocalizacion =
                LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        proveedordelocalizacion.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng nuevascoordenadas= new LatLng(location.getLatitude(), location.getLongitude());

                            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapaActivity.this, R.raw.estilo_mapa));
                            /*CameraUpdate actualizar = CameraUpdateFactory.newLatLngZoom(new LatLng(43.26, -2.95),9);
                               googleMap.moveCamera(actualizar);*/

                            googleMap.getUiSettings().setTiltGesturesEnabled(false);
                            googleMap.getUiSettings().setZoomGesturesEnabled(false);
                            CameraPosition Poscam = new CameraPosition.Builder()
                                    .target(nuevascoordenadas)
                                    .zoom(18)
                                    //.bearing(54)
                                    .tilt(80)
                                    .build();
                            CameraUpdate otravista = CameraUpdateFactory.newCameraPosition(Poscam);
                            googleMap.moveCamera(otravista);

                        } else {
                            return;
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        actualizador = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult!=null){
                    CameraUpdate actualizar = CameraUpdateFactory.newLatLng(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()));
                    googleMap.moveCamera(actualizar);
                }
                else{
                    return;
                }
            }
        };

        LocationRequest peticion = LocationRequest.create();
        peticion.setInterval(5000);
        peticion.setFastestInterval(1000);
        peticion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        proveedordelocalizacion.requestLocationUpdates(peticion,actualizador,null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proveedordelocalizacion.removeLocationUpdates(actualizador);
    }
}