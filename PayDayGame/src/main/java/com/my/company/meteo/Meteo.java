package com.my.company.meteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Meteo {
    private static final String API_KEY = "6311b8668feec546fd040a35118d1c11"; 

    public static void getWeather(String city) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            String json = content.toString();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

            // Stampa le informazioni principali
            System.out.println("Città: " + jsonObject.get("name").getAsString());
            System.out.println("Temperatura: " + jsonObject.getAsJsonObject("main").get("temp").getAsDouble() + "°C");
            System.out.println("Condizioni: " + jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
