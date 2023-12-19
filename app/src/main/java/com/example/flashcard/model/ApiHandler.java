package com.example.flashcard.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiHandler {

    private static final String BASE_URL = "http://172.20.10.2/api/get_Account.php";
    private final OnDataReceivedListener onDataReceivedListener;

    public interface OnDataReceivedListener {
        void onDataReceived(Accounts account);
    }

    public ApiHandler(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }

    public void executeApiRequest(String username, String password) {
        new Thread(() -> {
            Accounts account = doInBackground(username, password);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (account != null && onDataReceivedListener != null) {
                    onDataReceivedListener.onDataReceived(account);
                } else {
                    Log.e("ApiHandler", "Failed to get account data");
                }
            });
        }).start();
    }

    private Accounts doInBackground(String username, String password) {
        try {
            String data = "username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");

            URL apiUrl = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Gửi dữ liệu đến server
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            outputStream.close();

            // Đọc phản hồi từ server
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.d("ApiResponse", "Response from server: " + response.toString());

            Gson gson = new GsonBuilder().setLenient().create();
            try {
                Accounts account = gson.fromJson(response.toString(), Accounts.class);
                Log.d("ApiHandler", "Gson object: " + gson.toJson(account));
                Log.d("ApiHandler", "Received account data:");
                Log.d("ApiHandler", "Username: " + account.getUsername());
                Log.d("ApiHandler", "Password: " + account.getPassword());
                return account;
            } catch (Exception e) {
                Log.e("ApiHandler", "Error parsing JSON: " + e.getMessage());
                return null;
            }


        } catch (IOException e) {
            Log.e("ApiHandler", "Error during API request: " + e.getMessage());
            return null;
        }
    }
}

