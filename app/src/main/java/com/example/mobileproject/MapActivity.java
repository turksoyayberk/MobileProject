package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ///Haritada ki markerların idlerini tutuyor.
    private Map<Marker, Integer> markerIdMap = new HashMap<>();
    ///Buttons
    private Button editButton;
    private Button deleteButton;
    private Button backButton;
    ///
    private AppDatabase db;
    private GoogleMap gMap;
    private Integer markerId;
    private Boolean addControl=false;//Haritaya marker ekledikten hemen sonra haritada gezinirken yada
    // başka bir şey yaparken yanlışlıkla haritaya basınca tekrar marker eklemesin diye.
    private Marker currentSelectedMarker = null;//Seçilen markerı tutmak için.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        ///UI tanımlamaları
        editButton = findViewById(R.id.editJourneyButton);
        deleteButton = findViewById(R.id.deleteJourneyButton);
        backButton = findViewById(R.id.backButton);
        //Back buton için click event.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ///Haritanın yüklenmesi için.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {//Map açıldığı zaman ki function
        gMap = googleMap;

        // Veritabanını başlat
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();

        // Mevcut işaretçileri ve ID'lerini yükle
        List<AppData> markers = db.appDataDao().getAllMarkers();
        if (markers != null) {
            for (AppData markerData : markers) {
                LatLng latLng = new LatLng(markerData.latitude, markerData.longitude);
                Marker marker = gMap.addMarker(new MarkerOptions().position(latLng));

                // Marker ve veritabanındaki ID'sini ilişkilendir
                markerIdMap.put(marker, markerData.id);
            }
        }

        // Marker'a tıklandığında currentSelectedMarker'ı güncelle
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentSelectedMarker = marker;
                marker.showInfoWindow();
                return true;
            }
        });

        ///Delete butonu için click event.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSelectedMarker != null) {
                    deleteMarker(currentSelectedMarker);
                    currentSelectedMarker = null; // Silme işleminden sonra referansı sıfırla
                }
            }
        });


        // Bilgi penceresi ayarları
        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Varsayılan görünüm kullanılır
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoView = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView titleTextView = infoView.findViewById(R.id.title);
                TextView noteTextView = infoView.findViewById(R.id.noteText);


                addControl = true;
                ShowButtons(View.VISIBLE);

                markerId = markerIdMap.get(marker);

                if (markerId != null) {
                    // Veritabanından ilgili AppData nesnesini al
                    AppData appData = db.appDataDao().getAppDataById(markerId);
                    if (appData != null) {
                        // AppData'nın title'ını kullan
                        titleTextView.setText(appData.title + markerId);
                        noteTextView.setText("Note: " + appData.note);
                    } else {
                        titleTextView.setText("Bilgi bulunamadı");
                    }
                } else {
                    titleTextView.setText("Yeni Marker");
                }

                return infoView;
            }
        });


        // Haritaya tıklama
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!addControl) {
                    addControl = true;
                    ShowButtons(View.VISIBLE);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    Marker marker = gMap.addMarker(markerOptions);

                    AppData newMarkerData = new AppData();

                    newMarkerData.latitude = latLng.latitude;
                    newMarkerData.longitude = latLng.longitude;

                    fetchLocationName(latLng.latitude, latLng.longitude);

                    newMarkerData.title = "Gezilecek yer";
                    long newMarkerId = db.appDataDao().insertMarker(newMarkerData);

                    //Edit yapmak için bir örnek
                  /*  AppData existingMarker = db.appDataDao().getAppDataById((int)newMarkerId);
                    existingMarker.title = "Yeni Başlık";
                    db.appDataDao().updateMarker(existingMarker);*/

                    markerIdMap.put(marker, (int) newMarkerId);

                    // Yeni eklenen Marker'ın bilgi penceresini göster
                    marker.showInfoWindow();
                } else {
                    addControl = false;
                    ShowButtons(View.GONE);
                }

            }
        });
    }
    private void fetchLocationName(double latitude, double longitude) {
        String apiKey = "AIzaSyBK6eF8WzzMQ-AZ8fsmZkkdPzJYAw73hXs"; // Buraya API anahtarınızı ekleyin

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latitude + "," + longitude + "&key=" + apiKey;

        // URL'den JSON cevabını alın ve işleyin (AsyncTask, Retrofit vb. ile)
        // JSON cevabından yer ismini (yer adı, şehir, bölge vb.) ayrıştırın
        // İşlemi arka planda yapmayı unutmayın!
    }
    private void deleteMarker(Marker marker) {
        // Veritabanından sil
        Integer markerId = markerIdMap.get(marker);
        if (markerId != null) {
            AppData markerData = db.appDataDao().getAppDataById(markerId);
            if (markerData != null) {
                db.appDataDao().deleteMarker(markerData);
            }
        }

        // Haritadan kaldır
        marker.remove();

        // Map'ten kaldır
        markerIdMap.remove(marker);
    }

    private void ShowButtons(int value) {
        editButton.setVisibility(value);
        deleteButton.setVisibility(value);
    }
}
