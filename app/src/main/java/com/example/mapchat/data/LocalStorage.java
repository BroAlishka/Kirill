package com.example.mapchat.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mapchat.model.ChatMessage;
import com.example.mapchat.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalStorage {
    private static final String PREFS_NAME = "map_chat_storage";
    private static final String KEY_USERS = "users";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ACTIVE_LOGIN = "active_login";

    private final SharedPreferences sharedPreferences;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        seedMessagesIfNeeded();
    }

    public boolean registerUser(String displayName, String login, String password) {
        String normalizedLogin = normalizeLogin(login);
        if (normalizedLogin.isEmpty()) {
            return false;
        }

        List<User> users = getUsers();
        for (User user : users) {
            if (user.getLogin().equals(normalizedLogin)) {
                return false;
            }
        }

        users.add(new User(displayName.trim(), normalizedLogin, password));
        saveUsers(users);
        return true;
    }

    public User authenticate(String login, String password) {
        String normalizedLogin = normalizeLogin(login);
        for (User user : getUsers()) {
            if (user.getLogin().equals(normalizedLogin) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void saveActiveUser(User user) {
        sharedPreferences.edit().putString(KEY_ACTIVE_LOGIN, user.getLogin()).apply();
    }

    public User getActiveUser() {
        String activeLogin = sharedPreferences.getString(KEY_ACTIVE_LOGIN, "");
        if (activeLogin == null || activeLogin.trim().isEmpty()) {
            return null;
        }

        for (User user : getUsers()) {
            if (user.getLogin().equals(activeLogin)) {
                return user;
            }
        }
        return null;
    }

    public void clearActiveUser() {
        sharedPreferences.edit().remove(KEY_ACTIVE_LOGIN).apply();
    }

    public List<ChatMessage> getMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        String rawJson = sharedPreferences.getString(KEY_MESSAGES, "[]");

        try {
            JSONArray array = new JSONArray(rawJson);
            for (int index = 0; index < array.length(); index++) {
                JSONObject item = array.getJSONObject(index);
                messages.add(new ChatMessage(
                        item.optString("authorLogin"),
                        item.optString("authorName"),
                        item.optString("text"),
                        item.optLong("timestamp")
                ));
            }
        } catch (JSONException ignored) {
            return new ArrayList<>();
        }

        return messages;
    }

    public void addMessage(ChatMessage chatMessage) {
        List<ChatMessage> messages = getMessages();
        messages.add(chatMessage);
        saveMessages(messages);
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String rawJson = sharedPreferences.getString(KEY_USERS, "[]");

        try {
            JSONArray array = new JSONArray(rawJson);
            for (int index = 0; index < array.length(); index++) {
                JSONObject item = array.getJSONObject(index);
                users.add(new User(
                        item.optString("displayName"),
                        item.optString("login"),
                        item.optString("password")
                ));
            }
        } catch (JSONException ignored) {
            return new ArrayList<>();
        }

        return users;
    }

    private void saveUsers(List<User> users) {
        JSONArray array = new JSONArray();
        for (User user : users) {
            JSONObject item = new JSONObject();
            try {
                item.put("displayName", user.getDisplayName());
                item.put("login", user.getLogin());
                item.put("password", user.getPassword());
                array.put(item);
            } catch (JSONException ignored) {
                return;
            }
        }

        sharedPreferences.edit().putString(KEY_USERS, array.toString()).apply();
    }

    private void saveMessages(List<ChatMessage> messages) {
        JSONArray array = new JSONArray();
        for (ChatMessage message : messages) {
            JSONObject item = new JSONObject();
            try {
                item.put("authorLogin", message.getAuthorLogin());
                item.put("authorName", message.getAuthorName());
                item.put("text", message.getText());
                item.put("timestamp", message.getTimestamp());
                array.put(item);
            } catch (JSONException ignored) {
                return;
            }
        }

        sharedPreferences.edit().putString(KEY_MESSAGES, array.toString()).apply();
    }

    private void seedMessagesIfNeeded() {
        if (!getMessages().isEmpty()) {
            return;
        }

        List<ChatMessage> initialMessages = new ArrayList<>();
        initialMessages.add(new ChatMessage(
                "system",
                "Система",
                "Чат готов к работе. Зарегистрируйтесь и отправьте первое сообщение.",
                System.currentTimeMillis()
        ));
        saveMessages(initialMessages);
    }

    private String normalizeLogin(String login) {
        return login == null ? "" : login.trim().toLowerCase(Locale.ROOT);
    }
}
