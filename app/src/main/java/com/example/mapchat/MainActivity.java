package com.example.mapchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapchat.adapter.ChatAdapter;
import com.example.mapchat.data.LocalStorage;
import com.example.mapchat.model.ChatMessage;
import com.example.mapchat.model.User;
import com.google.android.material.button.MaterialButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String MAP_PREFS = "map_state";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ZOOM = "zoom";

    private static final double DEFAULT_LATITUDE = 55.751244;
    private static final double DEFAULT_LONGITUDE = 37.618423;
    private static final double DEFAULT_ZOOM = 10.5;

    private LocalStorage localStorage;
    private User currentUser;

    private MapView mapView;
    private EditText messageEditText;
    private RecyclerView messagesRecyclerView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage = new LocalStorage(this);
        currentUser = localStorage.getActiveUser();

        if (currentUser == null) {
            openAuthScreen();
            return;
        }

        setContentView(R.layout.activity_main);
        bindViews();
        setupHeader();
        setupMap(savedInstanceState);
        setupChat();
        setupListeners();
        refreshMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mapView != null) {
            saveMapState();
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView == null) {
            return;
        }

        outState.putDouble(KEY_LATITUDE, mapView.getMapCenter().getLatitude());
        outState.putDouble(KEY_LONGITUDE, mapView.getMapCenter().getLongitude());
        outState.putDouble(KEY_ZOOM, mapView.getZoomLevelDouble());
    }

    private void bindViews() {
        mapView = findViewById(R.id.mapView);
        messageEditText = findViewById(R.id.messageEditText);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
    }

    private void setupHeader() {
        TextView userTextView = findViewById(R.id.currentUserTextView);
        userTextView.setText(getString(R.string.current_user_template, currentUser.getDisplayName()));
    }

    private void setupMap(Bundle savedInstanceState) {
        SharedPreferences osmdroidPrefs = getSharedPreferences("osmdroid", MODE_PRIVATE);
        Configuration.getInstance().load(getApplicationContext(), osmdroidPrefs);
        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        IMapController controller = mapView.getController();
        controller.setZoom(DEFAULT_ZOOM);
        controller.setCenter(new GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE));

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LATITUDE)) {
            controller.setZoom(savedInstanceState.getDouble(KEY_ZOOM, DEFAULT_ZOOM));
            controller.setCenter(new GeoPoint(
                    savedInstanceState.getDouble(KEY_LATITUDE, DEFAULT_LATITUDE),
                    savedInstanceState.getDouble(KEY_LONGITUDE, DEFAULT_LONGITUDE)
            ));
            return;
        }

        SharedPreferences mapPrefs = getSharedPreferences(MAP_PREFS, MODE_PRIVATE);
        controller.setZoom(mapPrefs.getFloat(KEY_ZOOM, (float) DEFAULT_ZOOM));
        controller.setCenter(new GeoPoint(
                Double.longBitsToDouble(mapPrefs.getLong(KEY_LATITUDE, Double.doubleToLongBits(DEFAULT_LATITUDE))),
                Double.longBitsToDouble(mapPrefs.getLong(KEY_LONGITUDE, Double.doubleToLongBits(DEFAULT_LONGITUDE)))
        ));
    }

    private void setupChat() {
        chatAdapter = new ChatAdapter();
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(chatAdapter);
    }

    private void setupListeners() {
        MaterialButton sendButton = findViewById(R.id.sendButton);
        MaterialButton logoutButton = findViewById(R.id.logoutButton);

        sendButton.setOnClickListener(view -> sendMessage());
        logoutButton.setOnClickListener(view -> logout());
    }

    private void refreshMessages() {
        List<ChatMessage> messages = localStorage.getMessages();
        chatAdapter.setMessages(messages, currentUser.getLogin());
        if (!messages.isEmpty()) {
            messagesRecyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    private void sendMessage() {
        String text = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_SHORT).show();
            return;
        }

        localStorage.addMessage(new ChatMessage(
                currentUser.getLogin(),
                currentUser.getDisplayName(),
                text,
                System.currentTimeMillis()
        ));
        messageEditText.setText("");
        refreshMessages();
    }

    private void logout() {
        localStorage.clearActiveUser();
        openAuthScreen();
    }

    private void openAuthScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveMapState() {
        if (mapView == null) {
            return;
        }

        getSharedPreferences(MAP_PREFS, MODE_PRIVATE)
                .edit()
                .putLong(KEY_LATITUDE, Double.doubleToLongBits(mapView.getMapCenter().getLatitude()))
                .putLong(KEY_LONGITUDE, Double.doubleToLongBits(mapView.getMapCenter().getLongitude()))
                .putFloat(KEY_ZOOM, (float) mapView.getZoomLevelDouble())
                .apply();
    }
}
