package com.example.scientificcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<CalculationEntity> calculationEntities;

    public ResultAdapter(List<CalculationEntity> calculationEntities) {
        this.calculationEntities = calculationEntities;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calculation, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        CalculationEntity entity = calculationEntities.get(position);
        holder.bind(entity);
    }

    @Override
    public int getItemCount() {
        return calculationEntities.size();
    }

    public void setCalculationEntities(List<CalculationEntity> calculationEntities) {
        this.calculationEntities = calculationEntities;
        notifyDataSetChanged();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView calculationTextView;
        private TextView calculationDateTextView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            calculationTextView = itemView.findViewById(R.id.calculationTextView);
            calculationDateTextView = itemView.findViewById(R.id.calculationDateTextView);
        }

        public void bind(CalculationEntity entity) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String formattedDate = sdf.format(new Date(entity.getCalculationDate()));
            calculationDateTextView.setText(formattedDate);

            calculationTextView.setText(entity.getExpression() + " = " + entity.getResult());
        }
    }
}

