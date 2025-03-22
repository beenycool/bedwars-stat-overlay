package com.bedwarsstats.api;

public interface StatProvider {
    void fetchPlayerStats(String playerName, StatCallback callback);

    interface StatCallback {
        void onSuccess(PlayerStats stats);
        void onError(Exception e);
    }

    class PlayerStats {
        // Placeholder for player stats fields
    }
}
