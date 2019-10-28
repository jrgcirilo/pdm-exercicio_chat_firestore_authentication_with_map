package com.example.exercicio_chat_firestore_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.widget.ImageView;
import android.net.Uri;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText msgEditText;
    private ChatAdapter adapter;
    private CollectionReference colMessagesReference;
    private List<Message> messages;
    private RecyclerView msgRecyclerView;

    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_PERMISSION_GPS = 1001;

    private FirebaseUser fireUser;

    private void setupFirebase () {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        colMessagesReference =
                FirebaseFirestore.
                        getInstance().
                        collection("messages");

        colMessagesReference.addSnapshotListener((result, e) -> {
            messages.clear();
            for (DocumentSnapshot doc : result.getDocuments()){
                Message m = doc.toObject(Message.class);
                messages.add(m);
            }
            Collections.sort(messages);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupFirebase();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10,
                    locationListener
            );
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1001);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);
        msgEditText =
                findViewById(R.id.msgEditText);

        msgRecyclerView = findViewById(R.id.msgRecyclerView);

        messages = new ArrayList<>();
        adapter = new ChatAdapter(this, messages);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        msgRecyclerView.setAdapter(adapter);
        msgRecyclerView.setLayoutManager(llm);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GPS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            10,
                            locationListener);
                }
            }else {
                Toast.makeText(this,getString(R.string.noGPS),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }


        public void sendMessage(View view) {
            String text =
                    msgEditText.getText().toString();
            Message m =
                    new Message(text,
                            fireUser.getEmail(),new java.util.Date(),false);
            colMessagesReference.add(m);
            msgEditText.setText("");
        }

    public void sendMap(View view) {
        String local = getString(R.string.sinalGPS,latitude,longitude);
        Message m = new Message(local, fireUser.getEmail(), new java.util.Date(),true);
        colMessagesReference.add(m);
        msgEditText.setText("");
    }



    class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView userTextView;
        public TextView msgTextView2;
        public ImageView mapImageView;

        ChatViewHolder(View view) {
            super(view);
            this.userTextView = view.findViewById(R.id.userTextView);
            this.msgTextView2 = view.findViewById(R.id.msgTextView2);
            mapImageView = view.findViewById(R.id.mapImageView);
        }
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

        private Context context;
        private List<Message> messages;

        public ChatAdapter(Context context, List<Message> messages) {
            this.context = context;
            this.messages = messages;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.list_item, parent, false);

            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            Message m = messages.get(position);

            if (!m.getMsg()) {

                holder.userTextView.setText(
                        context.getString(R.string.user,
                                DateHelper.format(m.getDate()),
                                m.getEmail()));
                holder.msgTextView2.setText(m.getText());
                holder.mapImageView.setVisibility(View.INVISIBLE);


            } else {
                holder.userTextView.setText(context.getString(R.string.user, m.getEmail(), DateHelper.format(m.getDate())));
                holder.msgTextView2.setText(m.getText());
                holder.mapImageView.setOnClickListener(v -> {
                    Uri uri = Uri.parse(m.getText().replace("Lat:", "geo:").replace("Long:", ","));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                    holder.mapImageView.setVisibility(View.VISIBLE);
                });


            }
        }
        @Override
        public int getItemCount() {
            return messages.size();
        }
    }




}
