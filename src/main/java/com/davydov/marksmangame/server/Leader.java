package com.davydov.marksmangame.server;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Leader {
    @Id
    public String playerName;
    public int playerWins;

    public Leader(String winnerName, int playerWins) {
        this.playerName = winnerName;
        this.playerWins = playerWins;
    }

    public Leader() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerWins() {
        return playerWins;
    }

    public void setPlayerWins(int playerWins) {
        this.playerWins = playerWins;
    }
}
