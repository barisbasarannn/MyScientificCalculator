package com.example.scientificcalculator;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CalculationRepository {
    private final CalculationDao calculationDao;
    private final LiveData<List<CalculationEntity>> allCalculations;

    public CalculationRepository(Application application) {
        CalculationDatabase db = CalculationDatabase.getDatabase(application);
        calculationDao = db.calculationDao();
        allCalculations = calculationDao.getAllCalculations();
    }

    public LiveData<List<CalculationEntity>> getAllCalculations() {
        return allCalculations;
    }

    public void insertCalculation(CalculationEntity calculation) {
        calculation.setCalculationDate(System.currentTimeMillis());
        new InsertAsyncTask(calculationDao).execute(calculation);
    }

    private static class InsertAsyncTask extends AsyncTask<CalculationEntity, Void, Void> {
        private final CalculationDao asyncTaskDao;

        InsertAsyncTask(CalculationDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CalculationEntity... params) {
            asyncTaskDao.insertCalculation(params[0]);
            return null;
        }
    }
    public void deleteAllCalculations() {
        new DeleteAllCalculationsAsyncTask(calculationDao).execute();
    }

    private static class DeleteAllCalculationsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CalculationDao calculationDao;

        public DeleteAllCalculationsAsyncTask(CalculationDao calculationDao) {
            this.calculationDao = calculationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            calculationDao.deleteAllCalculations();
            return null;
        }
    }
}
