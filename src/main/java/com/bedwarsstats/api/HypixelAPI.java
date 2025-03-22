package com.bedwarsstats.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

public class HypixelAPI implements StatProvider {
    private static final String API_URL = "https://api.hypixel.net/player";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 1000; // milliseconds

    private final String apiKey;
    private final ThreadPoolExecutor apiPool;

    public HypixelAPI(String apiKey) {
        this.apiKey = apiKey;
        this.apiPool = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
        );
    }

    @Override
    public void fetchPlayerStats(String playerName, StatCallback callback) {
        apiPool.execute(() -> {
            int attempts = 0;
            while (attempts < MAX_RETRIES) {
                try {
                    String response = makeApiRequest(playerName);
                    callback.onSuccess(parseStats(response));
                    return;
                } catch (IOException e) {
                    attempts++;
                    if (attempts >= MAX_RETRIES) {
                        callback.onError(e);
                    } else {
                        try {
                            Thread.sleep(RETRY_DELAY);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        });
    }

    public void fetchTopPlayers(StatCallback callback) {
        apiPool.execute(() -> {
            int attempts = 0;
            while (attempts < MAX_RETRIES) {
                try {
                    String response = makeApiRequest("topplayers");
                    callback.onSuccess(parseTopPlayers(response));
                    return;
                } catch (IOException e) {
                    attempts++;
                    if (attempts >= MAX_RETRIES) {
                        callback.onError(e);
                    } else {
                        try {
                            Thread.sleep(RETRY_DELAY);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        });
    }

    private String makeApiRequest(String endpoint) throws IOException {
        URL url = new URL(API_URL + "?key=" + apiKey + "&name=" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to fetch data: HTTP error code " + connection.getResponseCode());
        }

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        return response.toString();
    }

    private PlayerStats parseStats(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject playerObject = jsonObject.getAsJsonObject("player");

        PlayerStats stats = new PlayerStats();
        stats.setPlayerName(playerObject.get("displayname").getAsString());
        stats.setFkdr(playerObject.get("achievements").getAsJsonObject().get("bedwars_final_k_d").getAsDouble());
        stats.setWins(playerObject.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("wins_bedwars").getAsInt());
        stats.setLosses(playerObject.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("losses_bedwars").getAsInt());

        return stats;
    }

    private List<PlayerStats> parseTopPlayers(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        List<PlayerStats> topPlayers = new ArrayList<>();

        for (JsonObject playerObject : jsonObject.getAsJsonArray("players")) {
            PlayerStats stats = new PlayerStats();
            stats.setPlayerName(playerObject.get("displayname").getAsString());
            stats.setFkdr(playerObject.get("achievements").getAsJsonObject().get("bedwars_final_k_d").getAsDouble());
            stats.setWins(playerObject.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("wins_bedwars").getAsInt());
            stats.setLosses(playerObject.get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject().get("losses_bedwars").getAsInt());
            topPlayers.add(stats);
        }

        return topPlayers;
    }
}
