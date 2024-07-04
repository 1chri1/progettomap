package com.mycompany.meteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Meteo {
    private static final String API_KEY = "6311b8668feec546fd040a35118d1c11";
    private static String condizioni;

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

            // Ottenere le informazioni principali
            System.out.println("Città: " + jsonObject.get("name").getAsString());
            System.out.println("Temperatura: " + jsonObject.getAsJsonObject("main").get("temp").getAsDouble() + "°C");
            condizioni = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
            System.out.println("Condizioni: " + condizioni);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCondizioni() {
        return condizioni;
    }

    public static void stampaMeteo(String city) {
        System.out.println("Informazioni meteo per la città:");
        getWeather(city);

        // Ottenere le informazioni meteo
        String condizioni = getCondizioni();
        StringBuilder messaggio = new StringBuilder("È sera e ");

        switch (condizioni.toLowerCase()) {
            case "rain":
                messaggio.append("sta piovendo. Le strade sono bagnate e scivolose.");
                break;
            case "clear":
                messaggio.append("il cielo è sereno. La luna illumina la città.");
                break;
            case "hail":
                messaggio.append("sta grandinando. Fai attenzione ai pezzi di ghiaccio che cadono.");
                break;
            case "snow":
                messaggio.append("sta nevicando. Le strade sono coperte di neve.");
                break;
            case "clouds":
                messaggio.append("è nuvoloso. La visibilità è ridotta.");
                break;
            default:
                messaggio.append("il tempo è incerto. Sii prudente.");
                break;
        }

        System.out.println(messaggio.toString());
    }
}
