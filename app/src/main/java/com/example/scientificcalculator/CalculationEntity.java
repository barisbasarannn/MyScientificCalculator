package com.example.scientificcalculator;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calculations")
public class CalculationEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "expression")
    private final String expression;

    @ColumnInfo(name = "result")
    private final String result;

    @ColumnInfo(name = "calculation_date")
    private long calculationDate;


    public CalculationEntity(String expression, String result, long calculationDate) {
        this.expression = expression;
        this.result = result;
        this.calculationDate = calculationDate;
    }


    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(long calculationDate) {
        this.calculationDate = calculationDate;
    }
}
