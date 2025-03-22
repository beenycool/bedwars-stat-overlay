package com.bedwarsstats.api;

public interface StatProvider {
    void fetchPlayerStats(String playerName, StatCallback callback);

    interface StatCallback {
        void onSuccess(PlayerStats stats);
        void onError(Exception e);
    }

    class PlayerStats {
        private String playerName;
        private double fkdr;
        private int wins;
        private int losses;

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public double getFkdr() {
            return fkdr;
        }

        public void setFkdr(double fkdr) {
            this.fkdr = fkdr;
        }

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getLosses() {
            return losses;
        }

        public void setLosses(int losses) {
            this.losses = losses;
        }

        @Override
        public String toString() {
            return "PlayerStats{" +
                    "playerName='" + playerName + '\'' +
                    ", fkdr=" + fkdr +
                    ", wins=" + wins +
                    ", losses=" + losses +
                    '}';
        }
    }
}
