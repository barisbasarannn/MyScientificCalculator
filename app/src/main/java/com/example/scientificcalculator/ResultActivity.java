package com.example.scientificcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ResultAdapter resultAdapter;
    private CalculationViewModel calculationViewModel;
    private ImageButton backButton;
    private Button deletePreviousCalculationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.recyclerView);
        deletePreviousCalculationButton = findViewById(R.id.deletePreviousCalculationButton);
        backButton = findViewById(R.id.backButton);
        resultAdapter = new ResultAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultAdapter);

        calculationViewModel = new ViewModelProvider(this).get(CalculationViewModel.class);
        calculationViewModel.getAllCalculations().observe(this, this::updateResultList);
        deletePreviousCalculationButton.setOnClickListener(this::deletePreviousCalculation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deletePreviousCalculation(View view) {
        calculationViewModel.deleteAllCalculations();
    }

    private void updateResultList(List<CalculationEntity> calculationEntities) {
        resultAdapter.setCalculationEntities(calculationEntities);
    }
}
