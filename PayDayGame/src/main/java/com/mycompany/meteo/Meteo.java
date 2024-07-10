package com.mycompany.meteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.swing.GameWindow;  // Aggiungi questa importazione

public class Meteo {

    private static final String API_KEY = "6311b8668feec546fd040a35118d1c11";

    /**
     * Metodo per ottenere e stampare le informazioni meteo per una specifica città.
     *
     * @param city la città per la quale ottenere e stampare le informazioni meteo
     */
    public static void stampaMeteo(String city) {
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
            String citta = jsonObject.get("name").getAsString();
            double temperatura = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            String condizioni = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

            // Costruisce il messaggio completo
            StringBuilder messaggio = new StringBuilder("\nSei a ").append(citta)
                .append(", la temperatura è di ").append(temperatura).append("°C.\nÈ sera ");

            // Aggiunge le condizioni meteo
            switch (condizioni.toLowerCase()) {
                case "rain":
                    messaggio.append("e sta piovendo. Le strade sono bagnate e scivolose.");
                    break;
                case "clear":
                    messaggio.append("e il cielo è sereno. La luna illumina la città.");
                    break;
                case "hail":
                    messaggio.append("e sta grandinando. Fai attenzione ai pezzi di ghiaccio che cadono.");
                    break;
                case "snow":
                    messaggio.append("e sta nevicando. Le strade sono coperte di neve.");
                    break;
                case "clouds":
                    messaggio.append("ed è nuvoloso. La visibilità è ridotta.");
                    break;
                default:
                    messaggio.append("ed il tempo è incerto. Sii prudente.");
                    break;
            }

            GameWindow.appendOutput(messaggio.toString());

        } catch (Exception e) {
            e.printStackTrace();
            GameWindow.appendOutput("Errore nel recupero delle informazioni meteo.");
        }
    }
}
