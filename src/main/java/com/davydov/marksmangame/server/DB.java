package com.davydov.marksmangame.server;

import java.util.ArrayList;

public interface DB {
    ArrayList<Leader> getAllRecords();

    void addOrUpdateRec(Leader rec);
}
