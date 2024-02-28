package com.example.scientificcalculator;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CalculationViewModel extends AndroidViewModel {
    private final CalculationRepository repository;
    private final LiveData<List<CalculationEntity>> allCalculations;

    public CalculationViewModel(Application application) {
        super(application);
        repository = new CalculationRepository(application);
        allCalculations = repository.getAllCalculations();
    }

    public LiveData<List<CalculationEntity>> getAllCalculations() {
        return allCalculations;
    }

    public void insertCalculation(CalculationEntity calculation) {
        repository.insertCalculation(calculation);
    }
    public void deleteAllCalculations() {
        repository.deleteAllCalculations();
    }

}