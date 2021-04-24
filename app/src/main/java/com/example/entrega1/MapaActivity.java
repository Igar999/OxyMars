package com.example.entrega1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.entrega1.runnables.ActualizarDatosUsuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class MapaActivity extends Actividad implements OnMapReadyCallback {

    FusedLocationProviderClient proveedordelocalizacion;
    LocationCallback actualizador;
    Marker jugador;
    Boolean seguir = true;
    ArrayList<Marker> listaMarkers = new ArrayList<>(5);
    ArrayList<GroundOverlay> listaCirculos = new ArrayList<>(5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        findViewById(R.id.fragmentoMapa).getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seguir = false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    77);
        } else {
            SupportMapFragment elfragmento = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
            elfragmento.getMapAsync(this);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(MapaActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        proveedordelocalizacion =
                LocationServices.getFusedLocationProviderClient(this);
        proveedordelocalizacion.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng nuevascoordenadas = new LatLng(location.getLatitude(), location.getLongitude());

                            //Web para mapas editados: https://mapstyle.withgoogle.com/
                            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapaActivity.this, R.raw.estilo_mapa));
                            /*CameraUpdate actualizar = CameraUpdateFactory.newLatLngZoom(new LatLng(43.26, -2.95),9);
                               googleMap.moveCamera(actualizar);*/

                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    seguir = false;
                                }
                            });

                            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                @Override
                                public void onMapLongClick(LatLng latLng) {
                                    seguir = false;
                                }
                            });

                            googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                                @Override
                                public void onCameraMoveStarted(int reason) {
                                    if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                                        seguir = false;
                                    }
                                }
                            });

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return true;
                                }
                            });

                            googleMap.getUiSettings().setTiltGesturesEnabled(false);
                            //googleMap.getUiSettings().setZoomGesturesEnabled(false);
                            googleMap.setMinZoomPreference(17);
                            googleMap.setMaxZoomPreference(19);
                            googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            CameraPosition Poscam = new CameraPosition.Builder()
                                    .target(nuevascoordenadas)
                                    .zoom(18)
                                    .tilt(80)
                                    .build();
                            CameraUpdate otravista = CameraUpdateFactory.newCameraPosition(Poscam);

                            ImageView botonCentrar = findViewById(R.id.botonCentrar);
                            botonCentrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LocationServices.getFusedLocationProviderClient(MapaActivity.this);
                                    seguir = true;
                                    if (ActivityCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    proveedordelocalizacion.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                            CameraUpdate actualizar = CameraUpdateFactory.newLatLng(pos);
                                            googleMap.animateCamera(actualizar);
                                        }
                                    });
                                }
                            });

                            jugador = googleMap.addMarker(new MarkerOptions()
                                    .position(nuevascoordenadas)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador))
                                    .title("Yo"));
                            iniciarMarcadores(googleMap);
                            actualizarMarcadores(googleMap);

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
                if (locationResult != null) {
                    LatLng pos = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    CameraUpdate actualizar = CameraUpdateFactory.newLatLng(pos);
                    if (seguir){googleMap.animateCamera(actualizar);}
                    jugador.remove();
                    jugador = googleMap.addMarker(new MarkerOptions()
                            .position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador)));
                    actualizarMarcadores(googleMap);
                } else {
                    return;
                }
            }
        };

        LocationRequest peticion = LocationRequest.create();
        peticion.setInterval(3000);
        peticion.setFastestInterval(1000);
        peticion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        proveedordelocalizacion.requestLocationUpdates(peticion, actualizador, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.getUtils().setListaMarkers(listaMarkers);
        if (proveedordelocalizacion != null) {
            proveedordelocalizacion.removeLocationUpdates(actualizador);
        }
    }

    //https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime/59857846
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 77: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        SupportMapFragment elfragmento = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
                        elfragmento.getMapAsync(this);
                    }
                } else {
                    Intent i = new Intent(MapaActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(MapaActivity.this, MainActivity.class);
        i.putExtra("usu", Utils.getUtils().getUsuario());
        startActivity(i);
        finish();
    }

    @SuppressLint("ResourceAsColor")
    private void iniciarMarcadores(GoogleMap googleMap){
        for (Marker marcador : Utils.getUtils().getListaMarkers()){
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(marcador.getPosition())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.brote)));
            marker.setDraggable(false);
            ponerCirculo(googleMap,marcador.getPosition());
            listaMarkers.add(marker);
        }
    }

    private void actualizarMarcadores(GoogleMap googleMap){
        for (int cont = listaMarkers.size()-1; cont >= 0; cont--){
            double distancia = Math.sqrt((Math.abs(jugador.getPosition().latitude-listaMarkers.get(cont).getPosition().latitude))+(Math.abs(jugador.getPosition().longitude-listaMarkers.get(cont).getPosition().longitude)));
            if (distancia < 0.0275){
                int sumaCont = 1;
                while (sumaCont <=100){
                    Oxigeno.getOxi().aumentarOxigenoToque();
                    sumaCont++;
                }
                listaMarkers.get(cont).remove();
                listaCirculos.get(cont).remove();
                listaMarkers.remove(cont);
                listaCirculos.remove(cont);
                Oxigeno oxi = Oxigeno.getOxi();
                ActualizarDatosUsuario actualizar = new ActualizarDatosUsuario(Utils.getUtils().getUsuario(),oxi.getOxigeno(),oxi.getOxiToque(),oxi.getOxiSegundo(),oxi.getDesbloqueadoToque(),oxi.getDesbloqueadoSegundo());
                new Thread(actualizar).start();
                DialogoBrote brote = new DialogoBrote(this);
                brote.show();
            } else if (distancia > 0.05){
                Marker oldMarker = listaMarkers.get(cont);
                oldMarker.remove();
                GroundOverlay oldOverlay = listaCirculos.get(cont);
                oldOverlay.remove();
                listaMarkers.remove(cont);
                listaCirculos.remove(cont);
            }
        }

        while (listaMarkers.size() < 5){
            double distancia = 0;
            Double lat = 0.0;
            Double lon = 0.0;
            while (distancia < 0.0325){
                LatLng pos = jugador.getPosition();
                float randomLat = ThreadLocalRandom.current().nextFloat();
                float randomLon = ThreadLocalRandom.current().nextFloat();
                int ran1 = ThreadLocalRandom.current().nextInt(0,2);
                int ran2 = ThreadLocalRandom.current().nextInt(0,2);
                if (ran1 == 1){randomLat = -1 * randomLat;}
                if (ran2 == 1){randomLon = -1 * randomLon;}
                lat = pos.latitude + (randomLat/500);
                lon = pos.longitude + (randomLon/500);
                distancia = Math.sqrt((Math.abs(jugador.getPosition().latitude-lat))+(Math.abs(jugador.getPosition().longitude-lon)));
            }
            LatLng newPos = new LatLng(lat, lon);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(newPos)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.brote)));
            marker.setDraggable(false);
            ponerCirculo(googleMap,newPos);
            listaMarkers.add(marker);
        }
    }

    //https://stackoverflow.com/questions/14358328/how-to-draw-circle-around-a-pin-using-google-maps-api-v2
    private void ponerCirculo(GoogleMap googleMap, LatLng pos){
        int d = 100; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        c.drawCircle(d/2, d/2, d/2, p);

        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

        // mapView is the GoogleMap
        GroundOverlay groundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(pos,d,d).
                transparency(0.4f));
        listaCirculos.add(groundOverlay);
    }

}