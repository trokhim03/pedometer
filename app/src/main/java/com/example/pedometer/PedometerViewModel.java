package com.example.pedometer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PedometerViewModel extends ViewModel {

    private final MutableLiveData<Integer> stepCount = new MutableLiveData<>();
    private final MutableLiveData<Double> distance = new MutableLiveData<>();
    private final MutableLiveData<Double> calories = new MutableLiveData<>();

    public LiveData<Integer> getStepCount() {
        return stepCount;
    }

    public LiveData<Double> getDistance() {
        return distance;
    }

    public LiveData<Double> getCalories() {
        return calories;
    }

    public void setStepCount(int steps) {
        stepCount.setValue(steps);
    }

    public void setDistance(double dist) {
        distance.setValue(dist);
    }

    public void setCalories(double cal) {
        calories.setValue(cal);
    }
}
