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
    private static String condizioni;
    private static String citta;
    private static double temperatura;

    /**
     * Metodo per ottenere le informazioni meteo da un'API per una specifica città.
     *
     * @param city la città per la quale ottenere le informazioni meteo
     */
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
            citta = jsonObject.get("name").getAsString();
            temperatura = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            condizioni = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per ottenere le condizioni meteo.
     *
     * @return le condizioni meteo come stringa
     */
    public static String getCondizioni() {
        return condizioni;
    }

    /**
     * Metodo per ottenere la città.
     *
     * @return la città come stringa
     */
    public static String getCitta() {
        return citta;
    }

    /**
     * Metodo per ottenere la temperatura.
     *
     * @return la temperatura come double
     */
    public static double getTemperatura() {
        return temperatura;
    }

    /**
     * Metodo per stampare le informazioni meteo per una specifica città.
     *
     * @param city la città per la quale stampare le informazioni meteo
     */
    public static void stampaMeteo(String city) {
        getWeather(city);

        // Ottenere le informazioni meteo
        String condizioni = getCondizioni();
        String citta = getCitta();
        double temperatura = getTemperatura();

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
    }
}
