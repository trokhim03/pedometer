package com.example.pedometer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.widget.TextView;

public class SummaryFragment extends Fragment {

    private TextView stepsWeek, distanceWeek, caloriesWeek;
    private PedometerViewModel pedometerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        stepsWeek = view.findViewById(R.id.stepsWeek);
        distanceWeek = view.findViewById(R.id.distanceWeek);
        caloriesWeek = view.findViewById(R.id.caloriesWeek);

        pedometerViewModel = new ViewModelProvider(requireActivity()).get(PedometerViewModel.class);

        // Пример данных за последние 7 дней
        pedometerViewModel.getStepCount().observe(getViewLifecycleOwner(), steps -> {
            stepsWeek.setText(String.valueOf(steps*6));
            double distance = (steps*6) * 0.8 / 1000; // 10 шагов = 8 метров
            distanceWeek.setText(String.format("%.2f km", distance));
            double calories = (steps*6) * 0.035; // 1000 шагов = 35 калорий
            caloriesWeek.setText(String.format("%.2f kcal", calories));
        });

        return view;
    }
}
