package com.example.main.Configuration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReverseGeocoding {
    public static String getAddress(String lat, String lng) {
        try {
            String urlString = "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                    + lat + "&lon=" + lng;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Required for Nominatim

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("display_name")) {
                return jsonResponse.getString("display_name");
            } else {
                return "No address found!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching address";
        }
    }
}