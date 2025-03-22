package com.bedwarsstats.cache;

import com.bedwarsstats.api.StatProvider;

public class EstimationEngine {
    public StatProvider.PlayerStats estimateStats(String playerName) {
        // Simulate estimation logic
        StatProvider.PlayerStats stats = new StatProvider.PlayerStats();
        stats.setPlayerName(playerName);
        stats.setFkdr(1.5);
        stats.setWins(100);
        stats.setLosses(50);
        return stats;
    }
}
