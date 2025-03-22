package com.bedwarsstats.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final String CONFIG_FILE = "config/bedwarsstats.json";
    private JsonObject config;

    public ConfigManager() {
        loadConfig();
    }

    private void loadConfig() {
        try (JsonReader reader = new JsonReader(new FileReader(CONFIG_FILE))) {
            config = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            config = new JsonObject();
            saveConfig();
        }
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            new Gson().toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String defaultValue) {
        return config.has(key) ? config.get(key).getAsString() : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        return config.has(key) ? config.get(key).getAsInt() : defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return config.has(key) ? config.get(key).getAsBoolean() : defaultValue;
    }

    public void setString(String key, String value) {
        config.addProperty(key, value);
        saveConfig();
    }

    public void setInt(String key, int value) {
        config.addProperty(key, value);
        saveConfig();
    }

    public void setBoolean(String key, boolean value) {
        config.addProperty(key, value);
        saveConfig();
    }
}
