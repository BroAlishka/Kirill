package com.example.mapchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapchat.data.LocalStorage;
import com.example.mapchat.model.User;

public class AuthActivity extends AppCompatActivity {
    private LocalStorage localStorage;

    private EditText registerNameEditText;
    private EditText registerLoginEditText;
    private EditText registerPasswordEditText;
    private EditText loginEditText;
    private EditText loginPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage = new LocalStorage(this);

        User activeUser = localStorage.getActiveUser();
        if (activeUser != null) {
            openMain();
            return;
        }

        setContentView(R.layout.activity_auth);
        bindViews();
        setupListeners();
    }

    private void bindViews() {
        registerNameEditText = findViewById(R.id.registerNameEditText);
        registerLoginEditText = findViewById(R.id.registerLoginEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        loginEditText = findViewById(R.id.loginEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
    }

    private void setupListeners() {
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(view -> register());
        loginButton.setOnClickListener(view -> login());
    }

    private void register() {
        String displayName = registerNameEditText.getText().toString().trim();
        String login = registerLoginEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(displayName) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean registered = localStorage.registerUser(displayName, login, password);
        if (!registered) {
            Toast.makeText(this, R.string.login_already_exists, Toast.LENGTH_SHORT).show();
            return;
        }

        User user = localStorage.authenticate(login, password);
        if (user == null) {
            Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            return;
        }

        localStorage.saveActiveUser(user);
        Toast.makeText(this, R.string.registration_success, Toast.LENGTH_SHORT).show();
        openMain();
    }

    private void login() {
        String login = loginEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        User user = localStorage.authenticate(login, password);
        if (user == null) {
            Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
            return;
        }

        localStorage.saveActiveUser(user);
        openMain();
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
