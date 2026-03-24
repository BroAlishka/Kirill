package com.example.mapchat.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapchat.R;
import com.example.mapchat.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> messages = new ArrayList<>();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());

    private String currentUserLogin = "";

    public void setMessages(List<ChatMessage> newMessages, String currentUserLogin) {
        this.currentUserLogin = currentUserLogin == null ? "" : currentUserLogin;
        messages.clear();
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        boolean isOwnMessage = message.getAuthorLogin().equals(currentUserLogin);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.bubbleContainer.getLayoutParams();
        params.gravity = isOwnMessage ? Gravity.END : Gravity.START;
        holder.bubbleContainer.setLayoutParams(params);
        holder.bubbleContainer.setBackgroundResource(
                isOwnMessage ? R.drawable.bg_message_outgoing : R.drawable.bg_message_incoming
        );

        holder.authorTextView.setText(message.getAuthorName());
        holder.authorTextView.setVisibility(isOwnMessage ? View.GONE : View.VISIBLE);
        holder.messageTextView.setText(message.getText());
        holder.timeTextView.setText(timeFormat.format(new Date(message.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout bubbleContainer;
        private final TextView authorTextView;
        private final TextView messageTextView;
        private final TextView timeTextView;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            bubbleContainer = itemView.findViewById(R.id.bubbleContainer);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
