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

public class TodayFragment extends Fragment {

    private TextView stepsBox, distanceBox, caloriesBox;
    private PedometerViewModel pedometerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        stepsBox = view.findViewById(R.id.stepsBox);
        distanceBox = view.findViewById(R.id.distanceBox);
        caloriesBox = view.findViewById(R.id.caloriesBox);

        pedometerViewModel = new ViewModelProvider(requireActivity()).get(PedometerViewModel.class);

        pedometerViewModel.getStepCount().observe(getViewLifecycleOwner(), steps -> stepsBox.setText(String.valueOf(steps)));
        pedometerViewModel.getDistance().observe(getViewLifecycleOwner(), distance -> distanceBox.setText(String.format("%.2f km", distance)));
        pedometerViewModel.getCalories().observe(getViewLifecycleOwner(), calories -> caloriesBox.setText(String.format("%.2f kcal", calories)));

        return view;
    }
}
