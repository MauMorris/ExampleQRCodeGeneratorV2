package com.example.generadordeqrs.repository;

import com.example.generadordeqrs.datasource.SimulationData;
import com.example.generadordeqrs.domain.RepositoryCallback;

public class RepositorySimulationData implements RepositoryCallback {
    private static RepositorySimulationData sInstance;

    private RepositorySimulationData(){}

    public static RepositorySimulationData getInstance() {
        if(sInstance == null)
            sInstance = new RepositorySimulationData();
        return sInstance;
    }

    @Override
    public String[] getListIncertidumbre() {
        return SimulationData.incertidumbre;
    }

    @Override
    public String[] getListClabeTdc() {
        return SimulationData.clabeTdc;
    }

    @Override
    public String[] getListOfClabes() {
        return SimulationData.dataClabeQr;
    }

    @Override
    public String[] getListOfTdc() {
        return SimulationData.dataTdcQr;
    }
}