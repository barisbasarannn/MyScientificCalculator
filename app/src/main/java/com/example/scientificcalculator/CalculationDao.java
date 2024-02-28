package com.example.scientificcalculator;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalculationDao {

    @Insert
    void insertCalculation(CalculationEntity calculation);

    @Update
    void updateCalculation(CalculationEntity calculation);

    @Query("SELECT * FROM calculations WHERE id = :id")
    CalculationEntity getCalculationById(int id);

    @Query("SELECT * FROM calculations ORDER BY calculation_date DESC") // tarih bilgisine göre sıralama eklendi
    LiveData<List<CalculationEntity>> getAllCalculations();

    @Query("DELETE FROM calculations")
    void deleteAllCalculations();

}
