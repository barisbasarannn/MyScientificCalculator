package com.example.scientificcalculator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CalculationEntity.class}, version = 2, exportSchema = false)
public abstract class CalculationDatabase extends RoomDatabase {
    public abstract CalculationDao calculationDao();

    private static volatile CalculationDatabase INSTANCE;

    private static final String DATABASE_NAME = "scientific_calculator_database";

    static CalculationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CalculationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CalculationDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}