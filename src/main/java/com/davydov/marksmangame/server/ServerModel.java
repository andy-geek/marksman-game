package com.davydov.marksmangame.server;

import java.util.ArrayList;
import java.util.List;

public class ServerModel {
    public boolean gameIsRunning = false;
    public List<Boolean> playersReady = new ArrayList<>();

    public double aimOneY = 250.0;
    public double aimTwoY = 250.0;
    public List<Double> arrowsX = new ArrayList<>();
    public List<Boolean> arrowsFired = new ArrayList<>();

    public List<String> playerNames = new ArrayList<>();
    public List<Integer> playerScores = new ArrayList<>();
    public List<Integer> playerShots = new ArrayList<>();
    public List<Integer> playerWins = new ArrayList<>();

    public List<Leader> leaders = new ArrayList<>();

    public String winnerName;
}
