package com.example.firebasestore;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String CAR_ID = "CarID";
    private static final String TRIP_ID = "TripID";
    private static final String ServerTimeStamp = "ServerTimeStamp";
    private static final String GEOPOINT = "GeoPoint";
    private static final String ELEVATION = "Elevation";
    private static final String SPEED = "Speed";
    private static final String GPS_LOCKSTATUS = "GPSLockStatus";
    private static final String GPS_SATELLITE_INVIEW = "GPSSatelliteInView";
    private static final String TERRAIN_BUMP_COUNT = "TerrainBumpCount";
    private static final String BATTERY_SOC = "BatterySOC";
    private static final String CHARGER_CONNECTED = "ChargerConnected";
    private static final String AC_STATUS = "ACStatus";
    private static final String GPSDate = "GPSRenderDate";
    private static final String GPSTime = "GPSTime";
    private static final String DistanceTravelled = "DistanceTravelled";
    private static final String IPAddress = "IPAddress";


    private static final String TAG = "MainActivity";
    private static final String DB = "CarGPSLocation";
    private static final String OrderBy = "ServerTimeStamp";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button button;
    private TextView textView;

    RecyclerView recyclerView;
    Adapter adapter;
    String[] items = new String[8];

//    String[] items = {"1","2"};

    String[] fixedItems = {"CarID", "TripID", "SoC", "Distance", "Speed", "TerrainBumpCounts", "Location", "ACStatus"};
//    String[] fixedItems = {"CarID", "TripID"};


    LinearLayoutManager layoutManager;
    DecimalFormat df = new DecimalFormat();
    Long GPSLockStatus;

    public static int Text_Green = R.color.colorPrimaryDark;
    public static int Text_Red = R.color.colorAccent;

    public static final String KEy_T = "Title";

    public static int trip= 3000 ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration noteListener;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        df.setMaximumFractionDigits(3);
        textView = (TextView) findViewById(R.id.text1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


    }

    private void initRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(this, items, fixedItems, GPSLockStatus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onStart() {

        super.onStart();
//        doc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e);
//                    return;
//                }
//
//                if (value.exists()) {
////                            String CarID = value.getString(KEy_T);
//                    String CarID = value.getString("Title");
//                    String TripID = value.getString("Description");
////                    GeoPoint GeoPoint = value.getGeoPoint("GeoPoint");
////                    Long BatterySoc = value.getLong("BatterySOC");
////                    Long BatteryConnected = value.getLong("ChargerConnected");
////                    Long DistanceTravelled = value.getLong("DistanceTravelled");
////                    Long Speed = value.getLong("Speed");
////                    Long ACStatus = value.getLong("ACStatus");
////                    String TerrainBumpCount = value.getString("TerrainBumpCount");
////                    double lat = GeoPoint.getLatitude();
////                    double longi = GeoPoint.getLongitude();
////                    Long GPSSatelliteinView = value.getLong("GPSSatelliteInView");
////                    GPSLockStatus = value.getLong("GPSLockStatus");
//
//
////                                    String title = document.getString("CarID");
////                                    String description = document.getString("Title");
//
//                    System.out.println("CarID:" + CarID);
//
//
////                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                    Toast.makeText(getApplicationContext(), CarID, Toast.LENGTH_SHORT).show();
//                    items[0] = CarID;
//                    items[1] = TripID;
////                    items[2] = BatteryConnected + " , " + String.valueOf(BatterySoc) + " V";
////                    items[3] = String.valueOf(DistanceTravelled) + " Km";
////                    items[4] = String.valueOf(Speed) + " Km/hr";
////                    items[5] = (TerrainBumpCount);
////                    items[6] = GPSSatelliteinView + " - " + df.format(lat) + " , " + df.format(longi);
////                    items[7] = String.valueOf(ACStatus);
//
////
////                            String TripID= value.getString("TripID");
//                    initRecyclerView();
//
//                }
//            }
//        });
//        // [END listen_multiple]

        db.collection(DB).orderBy(OrderBy, Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }



                        for (QueryDocumentSnapshot doc : value) {
                            String CarID = doc.getString("CarID");
                            String TripID = doc.getString("TripID");
                            String Time = doc.getString("GPSTime");
                            String Date = doc.getString("GPSRenderDate");
                            GeoPoint GeoPoint = doc.getGeoPoint("GeoPoint");
                            Long BatterySoc = doc.getLong("BatterySOC");
                            Long BatteryConnected = doc.getLong("ChargerConnected");
                            Long DistanceTravelled = doc.getLong("DistanceTravelled");
                            Long Speed = doc.getLong("Speed");
                            Long ACStatus = doc.getLong("ACStatus");
                            String TerrainBumpCount = doc.getString("TerrainBumpCount");
                            double lat = GeoPoint.getLatitude();
                            double longi = GeoPoint.getLongitude();
                            Long GPSSatelliteinView = doc.getLong("GPSSatelliteInView");
                            GPSLockStatus = doc.getLong("GPSLockStatus");

//                            Toast.makeText(getApplicationContext(), CarID, Toast.LENGTH_SHORT).show();
                            items[0] = CarID;
                            items[1] = TripID;
                            items[2] = BatteryConnected + " , " + String.valueOf(BatterySoc) + " V";
                            items[3] = String.valueOf(DistanceTravelled) + " Km";
                            items[4] = String.valueOf(Speed) + " Km/hr";
                            items[5] = (TerrainBumpCount);
                            items[6] = GPSSatelliteinView + " -  [" + df.format(lat) + " , " + df.format(longi)+"] , "+ Date+";"+Time;
                            items[7] = String.valueOf(ACStatus);
                            initRecyclerView();

                        }
                    }
                });


    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        noteListener.remove();
//    }

    public void savenote(View view) {

//        final String title = editTextTitle.getText().toString();
////        String description = editTextDescription.getText().toString();
//        Toast.makeText(getApplicationContext(),"Note on Saved",Toast.LENGTH_SHORT).show();

        GeoPoint geoPoint = new GeoPoint(19.4369131,72.8445453);
        trip = trip+1;

        Map<String, Object> note = new HashMap<>();
        note.put(CAR_ID, "STROMR3B");
        note.put(TRIP_ID, String.valueOf(trip));
        note.put(ServerTimeStamp, FieldValue.serverTimestamp());
        note.put(ELEVATION, 234);
        note.put(GEOPOINT, geoPoint);
        note.put(SPEED, 67);
        note.put(GPS_LOCKSTATUS, 1);
        note.put(GPS_SATELLITE_INVIEW, 7);
        note.put(TERRAIN_BUMP_COUNT, "None");
        note.put(BATTERY_SOC, 40);
        note.put(CHARGER_CONNECTED, 1);
        note.put(AC_STATUS, 1);
        note.put(GPSDate, "05/22/2020");
        note.put(GPSTime, "6:06:00PM");
        note.put(DistanceTravelled, 2345);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(DB).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    private void getNotes() {
        // [START listen_multiple]
        db.collection("Hello").whereEqualTo("Title", "Name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        System.out.println("Value:" + value);


                        List<String> cities = new ArrayList<>();
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("Title") != null) {
                                cities.add(doc.getString("Title"));
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });
        // [END listen_multiple]


//
//            db.collection("CarGPSLocation").orderBy("ServerTimeStamp", Query.Direction.DESCENDING).limit(1)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    String CarID = document.getString("CarID");
//                                    String TripID= document.getString("TripID");
//                                    GeoPoint GeoPoint = document.getGeoPoint("GeoPoint");
//                                    Long BatterySoc = document.getLong("BatterySOC");
//                                    Long BatteryConnected = document.getLong("ChargerConnected");
//                                    Long DistanceTravelled = document.getLong("DistanceTravelled");
//                                    Long Speed = document.getLong("Speed");
//                                    Long ACStatus = document.getLong("ACStatus");
//                                    String TerrainBumpCount = document.getString("TerrainBumpCount");
//                                    double lat  = GeoPoint.getLatitude();
//                                    double longi = GeoPoint.getLongitude();
//                                    Long GPSSatelliteinView = document.getLong("GPSSatelliteInView");
//                                    GPSLockStatus = document.getLong("GPSLockStatus");
//
//
////                                    String title = document.getString("CarID");
////                                    String description = document.getString("Title");
//
//
////                                    Log.d(TAG, document.getId() + " => " + document.getData());
////                                    Toast.makeText(getApplicationContext(), document.getId(), Toast.LENGTH_SHORT).show();
//                                    items[0] = CarID;
//                                    items[1] = TripID;
//                                    items[2] = BatteryConnected +" , "+ String.valueOf(BatterySoc)+" V";
//                                    items[3] = String.valueOf(DistanceTravelled)+" Km";
//                                    items[4] = String.valueOf(Speed)+" Km/hr";
//                                    items[5] = (TerrainBumpCount);
//                                    items[6] = GPSSatelliteinView+ " - "+df.format(lat)+" , "+ df.format(longi) ;
//                                    items[7] = String.valueOf(ACStatus);
//
//                                    initRecyclerView();
//
//                                }
//                            } else {
//                                Log.w(TAG, "Error getting documents.", task.getException());
//                            }
//                        }
//                    });
    }


}
